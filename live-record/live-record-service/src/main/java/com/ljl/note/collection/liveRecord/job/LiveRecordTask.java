package com.ljl.note.collection.liveRecord.job;

import com.google.common.primitives.Longs;
import com.ljl.note.collection.common.utils.DateUtils;
import com.ljl.note.collection.common.utils.GZIPUtil;
import com.ljl.note.collection.common.utils.LocalDateTimeUtil;
import com.ljl.note.collection.common.utils.StringUtils;
import com.ljl.note.collection.liveRecord.common.RedisKey;
import com.ljl.note.collection.liveRecord.common.RedisService;
import com.ljl.note.collection.liveRecord.model.LiveRecord;
import com.ljl.note.collection.liveRecord.qcloud.vod2017.QCloudVodCountService;
import com.ljl.note.collection.liveRecord.service.LiveRecordCountServiceImpl;
import com.ljl.note.collection.liveRecord.service.LiveRecordServiceImpl;
import jodd.typeconverter.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LiveRecordTask {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("liveRecordPartDeleteExecutor")
    private ThreadPoolTaskExecutor liveRecordPartDeleteExecutor;

    @Value("${limitTime:30}")
    private Integer limitTime;

    @Autowired
    private LiveRecordServiceImpl liveRecordService;

    @Autowired
    private QCloudVodCountService qCloudVodCountService;

    @Autowired
    private LiveRecordCountServiceImpl liveRecordCountServiceImpl;

    @Autowired
    private RedisService redisService;

    /**
     * 延迟队列,获取需要处理的录播任务,入队列
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void searchLiveRecordVideoFromZSetTask() {
        try {
            redisService.getLock(RedisKey.LIVERECORD_VIDEO_INTOLIST_LOCK);
            log.info("searchLiveRecordVideoFromZSetTask exec start");
            long maxScore = System.currentTimeMillis();
            long minScore = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(new Date()).minusHours(1)).getTime();
            Set<Long> liveRecordIds = redisTemplate.opsForZSet()
                    .rangeByScore(RedisKey.LIVERECORD_VIDEO_GET_ZSET, minScore, maxScore, 0, -1);
            /*for (ZSetOperations.TypedTuple<Long> zSetModel : liveRecordIdWithScores) {
                searchLiveRecordVideoTaskExecutor.execute(() -> {
                    Boolean result = liveRecordService.mergeLiveRecord(Convert.toLong(zSetModel.getValue()));
                    if (result) {//未完成，1小时内重试
                        redisTemplate.opsForZSet().remove(RedisKey.LIVERECORD_VIDEO_GET_ZSET, zSetModel.getValue());
                    }
                });
            }*/
            redisTemplate.opsForList().leftPushAll(RedisKey.LIVERECORD_VIDEO_GET_LIST,liveRecordIds);
            redisTemplate.executePipelined((RedisCallback<Object>) redisConnection->{
               redisConnection.openPipeline();
               for(Long liveRecordId:liveRecordIds){
                   redisConnection.setEx(String.format(RedisKey.LIVERECORD_VIDEO_GET_RETRY,liveRecordId).getBytes(),3600, Longs.toByteArray(liveRecordId));
               }
               redisConnection.closePipeline();
               return null;
            });
            redisTemplate.opsForZSet().remove(RedisKey.LIVERECORD_VIDEO_GET_ZSET,liveRecordIds);
            log.info("searchLiveRecordVideoFromZSetTask exec end");
        } catch (Exception e) {
            log.info("searchLiveRecordVideoFromZSetTask exec error");
            e.printStackTrace();
        } finally {
            try {
                redisService.unLock(RedisKey.LIVERECORD_VIDEO_INTOLIST_LOCK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 延迟队列,查询视频合并任务处理结果 入队列
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void mergeLiveRecordTaskHandleTask() {
        try {
            redisService.getLock(RedisKey.LIVERECORD_VIDEO_GETMERGE_INTOLIST_LOCK);
            log.info("mergeLiveRecordTaskHandleTask exec start");
            long maxScore = System.currentTimeMillis();
            long minScore = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(new Date()).minusHours(1)).getTime();
            Set<Long> liveRecordIds = redisTemplate.opsForZSet()
                    .rangeByScore(RedisKey.LIVERECORD_VIDEO_GETMERGE_ZSET, minScore, maxScore, 0, -1);
            redisTemplate.opsForList().leftPushAll(RedisKey.LIVERECORD_VIDEO_GETMERGE_LIST,liveRecordIds);
            redisTemplate.executePipelined((RedisCallback<Object>) redisConnection->{
                redisConnection.openPipeline();
                for(Long liveRecordId:liveRecordIds){
                    redisConnection.setEx(String.format(RedisKey.LIVERECORD_VIDEO_GETMERGE_RETRY,liveRecordId).getBytes(),3600, Longs.toByteArray(liveRecordId));
                }
                redisConnection.closePipeline();
                return null;
            });
            redisTemplate.opsForZSet().remove(RedisKey.LIVERECORD_VIDEO_GETMERGE_ZSET,liveRecordIds);
            log.info("mergeLiveRecordTaskHandleTask exec end");
        } catch (Exception e) {
            log.info("mergeLiveRecordTaskHandleTask exec error");
            e.printStackTrace();
        } finally {
            try {
                redisService.unLock(RedisKey.LIVERECORD_VIDEO_GETMERGE_INTOLIST_LOCK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 统计录播视频回放流量消耗
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void countLiveRecordFluxTask() {
        try {
            redisService.getLock(RedisKey.LIVERECORD_VIDEO_VODCOUNT_LOCK);
            long firstTime = DateUtils.getFistTimeOfDate(new Date()).getTime() / 1000;
            String key = String.format(RedisKey.LIVERECORD_VIDEO_VODCOUNT_KEY, firstTime);
            Boolean lockFlag = redisTemplate.opsForValue().setIfAbsent(key, firstTime);
            if (lockFlag) {
                redisTemplate.expire(key, 2, TimeUnit.HOURS);
                log.info("countLiveRecordFluxTask exec start");
                String fileUrl = qCloudVodCountService.getPlayStatLogList();
                if (StringUtils.isEmpty(fileUrl)) {
                    log.info("yesToday has no liveRecordFluxCount,fileUrl is null!");
                    log.info("countLiveRecordFluxTask exec end");
                    return;
                }
                GZIPUtil.getVodLiveRecordCountByFileUrl(fileUrl, liveRecordCountServiceImpl::dealVodCountFile);
                log.info("countLiveRecordFluxTask exec end");
            }
        } catch (Exception e) {
            log.info("countLiveRecordFluxTask exec error");
            e.printStackTrace();
        } finally {
            try {
                redisService.unLock(RedisKey.LIVERECORD_VIDEO_VODCOUNT_LOCK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前存在已完成并且未删除的回放视频的商户当天零点时剩余流量
     */
    @Scheduled(cron = "59 59 23 * * ?")
    public void countLeftFluxByZeroTask() {
        try {
            redisService.getLock(RedisKey.LIVERECORD_FLUX_ZEROCOUNT_LOCK);
            log.info("countLeftFluxByZeroTask exec start");
            //获取当前存在已完成并且未删除的回放视频的商户
            List<Long> allPids = liveRecordService.getAllFinishedAndUndeletedLiveRecord(null).stream()
                    .map(LiveRecord::getPid).distinct().collect(Collectors.toList());
            for (Long pid : allPids) {
                liveRecordPartDeleteExecutor.execute(() -> {
                    /*redisTemplate.opsForValue().set(String.format(RedisKey.LIVERECORD_FLUX_ZEROCOUNT_KEY, pid), "剩余流量", 1, TimeUnit.DAYS);*/
                });
            }
            log.info("countLeftFluxByZeroTask exec end");
        } catch (Exception e) {
            log.info("countLeftFluxByZeroTask exec error");
            e.printStackTrace();
        } finally {
            try {
                redisService.unLock(RedisKey.LIVERECORD_FLUX_ZEROCOUNT_LOCK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
   