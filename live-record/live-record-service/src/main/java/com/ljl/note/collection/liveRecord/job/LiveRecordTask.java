package com.ljl.note.collection.liveRecord.job;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
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
    @Qualifier("searchLiveRecordVideoTaskExecutor")
    private ThreadPoolTaskExecutor searchLiveRecordVideoTaskExecutor;

    @Autowired
    @Qualifier("liveRecordMergeTaskExecutor")
    private ThreadPoolTaskExecutor liveRecordMergeTaskExecutor;

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
     * 延迟队列,获取录播视频文件
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void searchLiveRecordVideoTask() {
        try {
            redisService.getLock(RedisKey.LIVERECORD_VIDEO_GET_LOCK);
            log.info("searchLiveRecordVideoTask exec start");
            long maxScore = System.currentTimeMillis();
            long minScore = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(new Date()).minusHours(1)).getTime();
            Set<ZSetOperations.TypedTuple<Long>> liveRecordIdWithScores = redisTemplate.opsForZSet()
                    .rangeByScoreWithScores(RedisKey.LIVERECORD_VIDEO_GET_QUEUE, minScore, maxScore, 0, -1);
            for (ZSetOperations.TypedTuple<Long> zSetModel : liveRecordIdWithScores) {
                searchLiveRecordVideoTaskExecutor.execute(() -> {
                    Boolean result = liveRecordService.mergeLiveRecord(Convert.toLong(zSetModel.getValue()));
                    if (result) {//未完成，1小时内重试
                        redisTemplate.opsForZSet().remove(RedisKey.LIVERECORD_VIDEO_GET_QUEUE, zSetModel.getValue());
                    }
                });
            }
            log.info("searchLiveRecordVideoTask exec end");
        } catch (Exception e) {
            log.info("searchLiveRecordVideoTask exec error");
            e.printStackTrace();
        } finally {
            try {
                redisService.unLock(RedisKey.LIVERECORD_VIDEO_GET_LOCK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 延迟队列,处理视频合并任务
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void mergeLiveRecordTaskHandleTask() {
        try {
            redisService.getLock(RedisKey.LIVERECORD_VIDEO_MERGETASK_LOCK);
            log.info("mergeLiveRecordTaskHandleTask exec start");
            long maxScore = System.currentTimeMillis();
            long minScore = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(new Date()).minusHours(1)).getTime();
            Set<ZSetOperations.TypedTuple<Long>> liveRecordIdWithScores = redisTemplate.opsForZSet()
                    .rangeByScoreWithScores(RedisKey.LIVERECORD_VIDEO_MERGETASK_QUEUE, minScore, maxScore, 0, -1);
            for (ZSetOperations.TypedTuple<Long> zSetModel : liveRecordIdWithScores) {
                liveRecordMergeTaskExecutor.execute(() -> {
                    Boolean result = liveRecordService.queryMergeTaskResult(Convert.toLong(zSetModel.getValue()));
                    if (result) {//未完成，1小时内重试
                        redisTemplate.opsForZSet().remove(RedisKey.LIVERECORD_VIDEO_MERGETASK_QUEUE, zSetModel.getValue());
                    }
                });
            }
            log.info("mergeLiveRecordTaskHandleTask exec end");
        } catch (Exception e) {
            log.info("mergeLiveRecordTaskHandleTask exec error");
            e.printStackTrace();
        } finally {
            try {
                redisService.unLock(RedisKey.LIVERECORD_VIDEO_MERGETASK_LOCK);
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
   