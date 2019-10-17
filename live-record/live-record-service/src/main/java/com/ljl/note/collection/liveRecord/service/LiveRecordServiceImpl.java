package com.ljl.note.collection.liveRecord.service;

import com.alibaba.fastjson.JSON;
import com.ljl.note.collection.common.enums.BaseErrorCode;
import com.ljl.note.collection.common.exception.BaseException;
import com.ljl.note.collection.common.utils.DateUtils;
import com.ljl.note.collection.common.utils.LocalDateTimeUtil;
import com.ljl.note.collection.common.utils.SoaResponseUtil;
import com.ljl.note.collection.live.domain.bo.LiveRoomInfoBO;
import com.ljl.note.collection.live.domain.dto.LiveRoomInfoGetDTO;
import com.ljl.note.collection.live.service.LiveRoomExport;
import com.ljl.note.collection.liveRecord.common.RedisKey;
import com.ljl.note.collection.liveRecord.common.RedisService;
import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskEndDTO;
import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskStartDTO;
import com.ljl.note.collection.liveRecord.domain.enums.*;
import com.ljl.note.collection.liveRecord.enums.SentinelMethodTypeEnum;
import com.ljl.note.collection.liveRecord.mapper.LiveRecordMapper;
import com.ljl.note.collection.liveRecord.model.LiveRecord;
import com.ljl.note.collection.liveRecord.qcloud.sdk2018.QCloudLiveRecordService;
import com.ljl.note.collection.liveRecord.sentinel.SentinelUtil;
import com.tencentcloudapi.live.v20180801.models.CreateLiveRecordRequest;
import com.tencentcloudapi.live.v20180801.models.CreateLiveRecordResponse;
import com.tencentcloudapi.live.v20180801.models.StopLiveRecordRequest;
import com.tencentcloudapi.vod.v20180717.models.*;
import jodd.typeconverter.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LiveRecordServiceImpl {


    @Autowired
    private LiveRoomExport liveRoomExport;

    @Autowired
    private QCloudLiveRecordService qCloudLiveRecordService;

    @Value("${pushDomain:sss}")
    private String pushDomain;

    @Value("${qcloud.video.bizId:sss}")
    private String bizId;

    private static final String LIVERECORDFILEFORMAT = "mp4";

    @Autowired
    private LiveRecordMapper liveRecordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    @Qualifier("liveRecordPartDeleteExecutor")
    private ThreadPoolTaskExecutor liveRecordPartDeleteExecutor;

    @Autowired
    @Qualifier("liveRecordMergeTaskExecutor")
    private ThreadPoolTaskExecutor liveRecordMergeTaskExecutor;

    @Autowired
    @Qualifier("queryLiveRecordMergeResultExecutor")
    private ThreadPoolTaskExecutor queryLiveRecordMergeResultExecutor;

    /**
     * 开始录播(是否存在录制中的文件和任务) (开始、恢复直播时调用)
     *
     * @param liveRecordTaskStartDTO
     * @return
     */
    public Boolean startLiveRecordTask(LiveRecordTaskStartDTO liveRecordTaskStartDTO) {
        log.info("startLiveRecordTask:{}", JSON.toJSONString(liveRecordTaskStartDTO));
        LiveRoomInfoGetDTO liveRoomInfoGetDTO = new LiveRoomInfoGetDTO();
        liveRoomInfoGetDTO.setRoomId(liveRecordTaskStartDTO.getRoomId());
        LiveRoomInfoBO liveRoomInfo = SoaResponseUtil.unpack(liveRoomExport.queryRoomByRoomId(liveRoomInfoGetDTO));
        if (null == liveRoomInfo || null == liveRoomInfo.getLiveCode()) {
            log.error("创建录播失败！roomId:{}", liveRecordTaskStartDTO.getRoomId());
            throw new BaseException(BaseErrorCode.LIVE_ROOM_DELETED);
        }
        try {
            Date startTime = new Date();
            Date endTime = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(startTime).plusHours(24));//直播时长
            //创建录播任务
            Future<CreateLiveRecordResponse> future = liveRecordPartDeleteExecutor.submit(() -> {
                CreateLiveRecordRequest request = new CreateLiveRecordRequest();
                request.setDomainName(pushDomain);
                request.setStreamName(bizId + "_" + liveRoomInfo.getLiveCode());
                request.setStartTime(DateUtils.dateToStr(startTime, "yyyy-MM-dd HH:mm:ss"));
                request.setEndTime(DateUtils.dateToStr(endTime, "yyyy-MM-dd HH:mm:ss"));
                request.setFileFormat(LIVERECORDFILEFORMAT);
                //sentinel限流
                SentinelUtil.getAccessKey(SentinelMethodTypeEnum.CreateLiveRecordTaskMethod);
                return qCloudLiveRecordService.createLiveRecordTask(request);
            });
            LiveRecord liveRecord = new LiveRecord();
            liveRecord.setStartTime(startTime);
            liveRecord.setEndTime(endTime);
            liveRecord.setCusOpenFlag(LiveRecordCusOpenTypeEnum.Off.getType());
            liveRecord.setRoomId(liveRoomInfo.getRoomId());
            liveRecord.setLiveRoomId(liveRoomInfo.getLiveRoomId());
            liveRecord.setName(liveRoomInfo.getTitle());
            liveRecord.setPid(liveRecordTaskStartDTO.getPid());
            liveRecord.setRecordTaskStatus(LiveRecordTaskStatusEnum.Processing.getType());
            liveRecord.setShareTitle(liveRoomInfo.getTitle());
            liveRecord.setDefaultCover(liveRoomInfo.getCoverImgUrl());
            liveRecord.setCustomizeCover(liveRoomInfo.getCoverImgUrl());
            liveRecord.setAppletType(liveRoomInfo.getAppletType());
            liveRecord.setDefaultSharePhoto(liveRoomInfo.getShareImgUrl());
            liveRecord.setCustomizeSharePhoto(liveRoomInfo.getShareImgUrl());
            liveRecord.setStoreId(liveRecordTaskStartDTO.getStoreId());
            liveRecord.setRecordTaskId(future.get().getTaskId());
            return liveRecordMapper.insertSelective(liveRecord) == 1;
        } catch (Exception e) {
            log.error("createLiveRecord error![{}]", e.getMessage());
            throw new BaseException(BaseErrorCode.CREATE_LIVERECORDTASK_FAILED);
        }
    }

    /**
     * 结束录播(视频合并)  (暂停、结束直播时调用)
     *
     * @param liveRecordTaskEndDTO
     * @return
     */
    public Boolean endLiveRecordTask(LiveRecordTaskEndDTO liveRecordTaskEndDTO) {
        log.info("endLiveRecordTask:{}", JSON.toJSONString(liveRecordTaskEndDTO));
        LiveRoomInfoGetDTO liveRoomInfoGetDTO = new LiveRoomInfoGetDTO();
        liveRoomInfoGetDTO.setRoomId(liveRecordTaskEndDTO.getRoomId());
        LiveRoomInfoBO liveRoomInfo = SoaResponseUtil.unpack(liveRoomExport.queryRoomByRoomId(liveRoomInfoGetDTO));
        if (null == liveRoomInfo) {
            log.error("终止录播任务失败！roomId:{}", liveRecordTaskEndDTO.getRoomId());
            throw new BaseException(BaseErrorCode.LIVE_ROOM_DELETED);
        }
        Example example = new Example(LiveRecord.class);
        example.createCriteria().andEqualTo("pid", liveRecordTaskEndDTO.getPid())
                .andEqualTo("roomId", liveRecordTaskEndDTO.getRoomId())
                .andEqualTo("recordTaskStatus", LiveRecordTaskStatusEnum.Processing.getType())
                .andGreaterThan("recordTaskId", 0)
                .andEqualTo("deleted", 0);
        List<LiveRecord> liveRecordList = liveRecordMapper.selectByExample(example);
        if (null == liveRecordList || liveRecordList.size() == 0) {
            log.info("该直播场次roomId:{},没有录制中的任务!", liveRecordTaskEndDTO.getRoomId());
            return false;
        }
        LiveRecord liveRecord = liveRecordList.get(0);
        StopLiveRecordRequest request = new StopLiveRecordRequest();
        request.setStreamName(bizId + "_" + liveRoomInfo.getLiveCode());
        request.setTaskId(liveRecord.getRecordTaskId());
        //sentinel限流
        SentinelUtil.getAccessKey(SentinelMethodTypeEnum.EndLiveRecordTaskMethod);
        //终止录制任务
        Boolean endLiveRecordTaskResult = qCloudLiveRecordService.endLiveRecordTask(request);
        //加入视频文件获取队列(延迟队列)
        if (endLiveRecordTaskResult) {
            liveRecord.setRecordTaskStatus(LiveRecordTaskStatusEnum.StopRecord.getType());
            liveRecord.setEndTime(new Date());
            liveRecordMapper.updateByPrimaryKeySelective(liveRecord);
            long handleTime = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(new Date()).plusMinutes(1)).getTime();
            redisTemplate.opsForZSet().add(RedisKey.LIVERECORD_VIDEO_GET_ZSET, liveRecord.getId(), handleTime);
        }
        return true;
    }

    /**
     * 合并录播片段（录播可能只有一段，也可能有多段）
     * 只有一段时视频来源为录制，合并后来源为视频处理！！！
     * <p>
     * 首先获取录播时间段内的片段
     * 如果只有1段：直接处理
     * 如果存在多段：1、发起合并任务 2、查询任务进度(进行中的任务重复请求)  3、获取文件详情  4、删除片段 ->处理
     *
     * @return
     */
    public Boolean mergeLiveRecord(Long liveRecordId) {
        LiveRecord liveRecord = liveRecordMapper.selectByPrimaryKey(liveRecordId);
        if (null == liveRecord) {
            log.info("录播视频不存在或已删除！liveRecordId:{}", liveRecordId);
            return true;
        }
        LiveRoomInfoGetDTO liveRoomInfoGetDTO = new LiveRoomInfoGetDTO();
        liveRoomInfoGetDTO.setRoomId(liveRecord.getRoomId());
        LiveRoomInfoBO liveRoomInfo = SoaResponseUtil.unpack(liveRoomExport.queryRoomByRoomId(liveRoomInfoGetDTO));
        if (null == liveRoomInfo) {
            log.info("直播场次不存在或已删除！roomId:{}", liveRecord.getRoomId());
            return true;
        }
        if (liveRecord.getRecordTaskStatus().equals(LiveRecordTaskStatusEnum.StopRecord.getType())) {
            try {
                //double check解决重复提交任务问题
                redisService.getLock(String.format(RedisKey.LIVERECORD_VIDEO_GET_LIVERECORD_LOCK, liveRecordId));
                liveRecord = liveRecordMapper.selectByPrimaryKey(liveRecordId);
                if (liveRecord.getRecordTaskStatus().equals(LiveRecordTaskStatusEnum.StopRecord.getType())) {
                    SearchMediaRequest searchMediaRequest = new SearchMediaRequest();
                    //转换为UTC时间
                    Date utcStartTime = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(liveRecord.getStartTime()).minusHours(8));
                    //按文件创建时间搜索，加个延迟
                    Date utcEndTime = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(liveRecord.getEndTime()).minusHours(8).plusSeconds(30));
                    searchMediaRequest.setStartTime(DateUtils.dateToStr(utcStartTime, "yyyy-MM-dd'T'HH:mm:ss'Z'"));
                    searchMediaRequest.setEndTime(DateUtils.dateToStr(utcEndTime, "yyyy-MM-dd'T'HH:mm:ss'Z'"));
                    searchMediaRequest.setSourceType("Record");
                    searchMediaRequest.setStreamId(bizId + "_" + liveRoomInfo.getLiveCode());
                    SortBy sortBy = new SortBy();
                    sortBy.setField("CreateTime");
                    sortBy.setOrder("Asc");
                    searchMediaRequest.setSort(sortBy);
                    searchMediaRequest.setLimit(5000L);
                    //sentinel限流
                    SentinelUtil.getAccessKey(SentinelMethodTypeEnum.SearchMediaIOMethod);
                    //搜索录播文件
                    SearchMediaResponse response = qCloudLiveRecordService.searchMediaIO(searchMediaRequest);
                    MediaInfo[] mediaInfos = response.getMediaInfoSet();
                    if (response.getTotalCount() == 0) {//未获取到,下次执行任务重新获取（1小时内重试）
                        return false;
                    } else if (response.getTotalCount() == 1) {
                        MediaInfo mediaInfo = mediaInfos[0];
                        DescribeMediaInfosResponse describeMediaInfosResponse = getFileDetailInfo(mediaInfo.getFileId());

                        MediaInfo mediaInfoDetail = describeMediaInfosResponse.getMediaInfoSet()[0];
                        handleLiveRecordVideoInfo(liveRecord, mediaInfoDetail.getBasicInfo(), mediaInfoDetail.getTranscodeInfo().getTranscodeSet()[0], mediaInfo.getFileId());
                    } else {
                        //创建合并视频任务
                        EditMediaRequest editMediaRequest = new EditMediaRequest();
                        editMediaRequest.setInputType("File");
                        EditMediaFileInfo[] mediaFileInfos = new EditMediaFileInfo[mediaInfos.length];
                        String filePartKey = String.format(RedisKey.LIVERECORD_VIDEO_PART_FILEID, liveRecordId);
                        BoundSetOperations setOperations = redisTemplate.boundSetOps(filePartKey);
                        for (int i = 0; i < mediaInfos.length; i++) {
                            EditMediaFileInfo editMediaFileInfo = new EditMediaFileInfo();
                            editMediaFileInfo.setFileId(mediaInfos[i].getFileId());
                            mediaFileInfos[i] = editMediaFileInfo;
                            setOperations.add(mediaInfos[i].getFileId());
                        }
                        redisTemplate.expire(filePartKey, 1, TimeUnit.DAYS);
                        editMediaRequest.setFileInfos(mediaFileInfos);
                        //sentinel限流
                        SentinelUtil.getAccessKey(SentinelMethodTypeEnum.EditMediaMethod);
                        EditMediaResponse editMediaResponse = qCloudLiveRecordService.editMedia(editMediaRequest);
                        liveRecord.setRecordTaskStatus(LiveRecordTaskStatusEnum.Merging.getType());
                        liveRecord.setMergeTaskId(editMediaResponse.getTaskId());
                        liveRecordMapper.updateByPrimaryKeySelective(liveRecord);
                        //延迟队列查询文件合并结果
                        long handleTime = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(new Date()).plusMinutes(1)).getTime();
                        redisTemplate.opsForZSet().add(RedisKey.LIVERECORD_VIDEO_GETMERGE_ZSET, liveRecord.getId(), handleTime);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    redisService.unLock(String.format(RedisKey.LIVERECORD_VIDEO_GET_LIVERECORD_LOCK, liveRecordId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 获取文件详细信息
     */
    private DescribeMediaInfosResponse getFileDetailInfo(String fileId) {
        DescribeMediaInfosRequest describeMediaInfosRequest = new DescribeMediaInfosRequest();
        String[] fileIdArr = new String[1];
        fileIdArr[0] = fileId;
        describeMediaInfosRequest.setFileIds(fileIdArr);
        //describeMediaInfosRequest.setFilters(new String[]{"basicInfo","metaData"});
        //sentinel限流
        SentinelUtil.getAccessKey(SentinelMethodTypeEnum.DescribeMediaInfosMethod);
        DescribeMediaInfosResponse describeMediaInfosResponse = qCloudLiveRecordService.describeMediaInfos(describeMediaInfosRequest);
        return describeMediaInfosResponse;
    }


    /**
     * 查询合并任务处理结果
     */
    public Boolean queryMergeTaskResult(Long liveRecordId) {
        LiveRecord liveRecord = liveRecordMapper.selectByPrimaryKey(liveRecordId);
        if (null == liveRecord) {
            log.info("录播视频不存在或已删除！liveRecordId:{}", liveRecordId);
            return true;
        }
        LiveRoomInfoGetDTO liveRoomInfoGetDTO = new LiveRoomInfoGetDTO();
        liveRoomInfoGetDTO.setRoomId(liveRecord.getRoomId());
        LiveRoomInfoBO liveRoomInfo = SoaResponseUtil.unpack(liveRoomExport.queryRoomByRoomId(liveRoomInfoGetDTO));
        if (null == liveRoomInfo) {
            log.info("直播场次不存在或已删除！roomId:{}", liveRecord.getRoomId());
            return true;
        }
        if (liveRecord.getRecordTaskStatus().equals(LiveRecordTaskStatusEnum.Merging.getType())) {
            try {
                //double check解决重复提交任务问题
                redisService.getLock(String.format(RedisKey.LIVERECORD_VIDEO_GETMERGE_LOCK, liveRecordId));
                liveRecord = liveRecordMapper.selectByPrimaryKey(liveRecordId);
                if (liveRecord.getRecordTaskStatus().equals(LiveRecordTaskStatusEnum.Merging.getType())) {
                    DescribeTaskDetailRequest describeTaskDetailRequest = new DescribeTaskDetailRequest();
                    describeTaskDetailRequest.setTaskId(liveRecord.getMergeTaskId());
                    //sentinel限流
                    SentinelUtil.getAccessKey(SentinelMethodTypeEnum.DescribeTaskDetailMethod);
                    //查询任务处理结果
                    DescribeTaskDetailResponse describeTaskDetailResponse = qCloudLiveRecordService.describeTaskDetail(describeTaskDetailRequest);
                    if (describeTaskDetailResponse.getTaskType().equals(VodTaskTypeEnum.EditMedia.getType()) && describeTaskDetailResponse.getStatus().equals(VodTaskStatusEnum.Finish.getType())) {
                        //获取文件详细信息
                        EditMediaTaskOutput editMediaTaskOutput = describeTaskDetailResponse.getEditMediaTask().getOutput();
                        DescribeMediaInfosResponse describeMediaInfosResponse = getFileDetailInfo(editMediaTaskOutput.getFileId());
                        MediaInfo mediaInfo = describeMediaInfosResponse.getMediaInfoSet()[0];
                        handleLiveRecordVideoInfo(liveRecord, mediaInfo.getBasicInfo(), mediaInfo.getTranscodeInfo().getTranscodeSet()[0], mediaInfo.getFileId());
                        //删除文件片段
                        String filePartKey = String.format(RedisKey.LIVERECORD_VIDEO_PART_FILEID, liveRecordId);
                        Set<String> fileIdSet = redisTemplate.opsForSet().members(filePartKey);
                        liveRecordPartDeleteExecutor.execute(() -> {
                            for (String fielId : fileIdSet) {
                                DeleteMediaRequest deleteMediaRequest = new DeleteMediaRequest();
                                deleteMediaRequest.setFileId(fielId);
                                //sentinel限流
                                SentinelUtil.getAccessKey(SentinelMethodTypeEnum.DeleteMediaMethod);
                                qCloudLiveRecordService.deleteMedia(deleteMediaRequest);
                            }
                            redisTemplate.delete(filePartKey);
                        });
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.unLock(String.format(RedisKey.LIVERECORD_VIDEO_GETMERGE_LOCK, liveRecordId));
            }
        }
        return true;
    }


    /**
     * 删除视频文件(包含回放列表和删除直播间时用)
     */
    public Boolean deleteSingleVideo(List<Long> liveRecordIdList) {
        Example example = new Example(LiveRecord.class);
        example.createCriteria().andIn("id", liveRecordIdList);
        List<LiveRecord> liveRecords = liveRecordMapper.selectByExample(example);
        if (null == liveRecords || liveRecords.size() == 0) {
            log.info("录播视频不存在或已删除！liveRecordId:{}", JSON.toJSONString(liveRecordIdList));
            return false;
        }

        DeleteMediaRequest deleteMediaRequest = new DeleteMediaRequest();
        liveRecordPartDeleteExecutor.execute(() -> {
            for (LiveRecord liveRecord : liveRecords) {
                deleteMediaRequest.setFileId(liveRecord.getQcloudFileId());
                //sentinel限流
                SentinelUtil.getAccessKey(SentinelMethodTypeEnum.DeleteMediaMethod);
                qCloudLiveRecordService.deleteMedia(deleteMediaRequest);
            }
        });
        Map<Long, List<LiveRecord>> listMap = liveRecords.stream()
                .filter(o -> o.getPid() != null).collect(Collectors.groupingBy(LiveRecord::getPid));
        for (Long pid : listMap.keySet()) {
            String videoUseStorageKey = String.format(RedisKey.LIVERECORD_VIDEO_USESTORAGE_KEY, pid);
            if (redisTemplate.hasKey(videoUseStorageKey)) {
                Long fileSizeTotal = 0L;
                for (LiveRecord liveRecord : listMap.get(pid)) {
                    fileSizeTotal += liveRecord.getTotalFileSize();
                }
                redisTemplate.opsForValue().increment(videoUseStorageKey, -1 * fileSizeTotal);
            }
        }
        return liveRecordMapper.deleteRecordByIds(liveRecordIdList) == 1;
    }

    /**
     * 视频文件信息处理
     *
     * @param liveRecord
     * @param basicInfo
     * @param mediaTranscodeItem
     * @return
     */
    private Boolean handleLiveRecordVideoInfo(LiveRecord liveRecord, MediaBasicInfo basicInfo, MediaTranscodeItem mediaTranscodeItem, String fileId) {
        log.info("handleLiveRecordVideoInfo:{}", JSON.toJSONString(liveRecord));
        liveRecord.setVideoDownloadUrl(basicInfo.getMediaUrl());
        liveRecord.setTotalFileSize(mediaTranscodeItem.getSize());
        liveRecord.setTotalDuration(mediaTranscodeItem.getDuration());
        liveRecord.setRecordTaskStatus(LiveRecordTaskStatusEnum.Finish.getType());
        //默认不开启回放
        //liveRecord.setCusOpenFlag(LiveRecordCusOpenTypeEnum.On.getType());
        liveRecord.setQcloudFileId(fileId);
        liveRecordMapper.updateByPrimaryKeySelective(liveRecord);
        String videoUseStorageKey = String.format(RedisKey.LIVERECORD_VIDEO_USESTORAGE_KEY, liveRecord.getPid());
        if (redisTemplate.hasKey(videoUseStorageKey)) {
            redisTemplate.opsForValue().increment(videoUseStorageKey, mediaTranscodeItem.getSize());
        }
        return true;
    }

    /**
     * 通过liveRecordId获取录播视频文件名称
     */
    public List<LiveRecord> getLiveRecordByIds(Set<Long> liveRecords) {
        Example example = new Example(LiveRecord.class);
        Example.Criteria criteria = example.createCriteria();
        if (liveRecords != null && liveRecords.size() > 0) {
            criteria.andIn("id", liveRecords);
        }
        return liveRecordMapper.selectByExample(example);
    }

    /**
     * 获取当前存在已完成并且未删除的回放视频
     *
     * @return
     */
    public List<LiveRecord> getAllFinishedAndUndeletedLiveRecord(Long pid) {
        Example example = new Example(LiveRecord.class);
        Example.Criteria criteria = example.createCriteria();
        if (pid != null) {
            criteria.andEqualTo("pid", pid);
        }
        criteria.andEqualTo("recordTaskStatus", LiveRecordTaskStatusEnum.Finish.getType())
                .andEqualTo("deleted", 0);
        return liveRecordMapper.selectByExample(example);
    }

    /**
     * 获取录播视频任务
     */
    @PostConstruct
    public void mergeLiveRecordFromListTask() {
        new Thread(() -> {
            while (true) {
                try {
                    Long liveRecordId = Convert.toLong(redisTemplate.opsForList()
                            .rightPop(RedisKey.LIVERECORD_VIDEO_GET_LIST, 10, TimeUnit.SECONDS));//阻塞读
                    if (liveRecordId == null) {
                        continue;
                    }
                    String retryKey = String.format(RedisKey.LIVERECORD_VIDEO_GET_RETRY, liveRecordId);
                    if (redisTemplate.hasKey(retryKey)) {
                        liveRecordMergeTaskExecutor.execute(() -> handleTask(LiveRecordTaskTypeEnum.MergeLiveRecordFromListTask, liveRecordId));
                    } else {
                        log.info("超过一小时，未查询到录播视频，liveRecordId:{}", liveRecordId);
                    }
                } catch (Exception e) {
                    System.out.println("空闲链接自动断开异常处理，继续执行！");
                }
            }
        }).start();
    }

    /**
     * 查询视频合并任务处理结果
     */
    @PostConstruct
    public void getMergeResultFromListTask() {
        new Thread(() -> {
            while (true) {
                try {
                    Long liveRecordId = Convert.toLong(redisTemplate.opsForList()
                            .rightPop(RedisKey.LIVERECORD_VIDEO_GETMERGE_LIST, 10, TimeUnit.SECONDS));//阻塞读
                    if (liveRecordId == null) {
                        continue;
                    }
                    String retryKey = String.format(RedisKey.LIVERECORD_VIDEO_GETMERGE_RETRY, liveRecordId);
                    if (redisTemplate.hasKey(retryKey)) {
                        queryLiveRecordMergeResultExecutor.execute(() -> handleTask(LiveRecordTaskTypeEnum.GetMergeResultFromListTask, liveRecordId));
                    } else {
                        log.info("超过一小时，未查询到视频合并任务处理结果，liveRecordId:{}", liveRecordId);
                    }
                } catch (Exception e) {
                    System.out.println("空闲链接自动断开异常处理，继续执行！");
                }
            }
        }).start();
    }

    /**
     * 根据任务类型调用任务
     *
     * @param liveRecordTaskTypeEnum
     * @param liveRecordId
     */
    private void handleTask(LiveRecordTaskTypeEnum liveRecordTaskTypeEnum, Long liveRecordId) {
        Boolean result;
        switch (liveRecordTaskTypeEnum) {
            case MergeLiveRecordFromListTask:
                result = mergeLiveRecord(liveRecordId);
                if (!result) {
                    //失败重试
                    redisTemplate.opsForList().leftPush(RedisKey.LIVERECORD_VIDEO_GET_LIST, liveRecordId);
                }
                break;
            case GetMergeResultFromListTask:
                result = queryMergeTaskResult(liveRecordId);
                if (!result) {
                    //失败重试
                    redisTemplate.opsForList().leftPush(RedisKey.LIVERECORD_VIDEO_GETMERGE_LIST, liveRecordId);
                }
                break;
            default:
                break;
        }
    }
}

















