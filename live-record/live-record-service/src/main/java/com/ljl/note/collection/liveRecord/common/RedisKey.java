package com.ljl.note.collection.liveRecord.common;

public class RedisKey {

    /**
     * 录播 视频获取处理 锁
     */
    public static final String LIVERECORD_VIDEO_GET_LOCK = "live:liveRecord:videoGet:lock";

    /**
     * 录播 视频合并任务查询处理 锁
     */
    public static final String LIVERECORD_VIDEO_MERGETASK_LOCK = "live:liveRecord:videoMergeTask:lock";

    /**
     * 录播 存储空间溢出处理任务(溢出29天) 锁
     */
    public static final String LIVERECORD_VIDEO_DELETEBEYOND_LOCK = "live:liveRecord:deleteBeyondTask:lock";

    /**
     * 录播 存储空间溢出处理任务(溢出28天以内) 锁
     */
    public static final String LIVERECORD_VIDEO_HANDLEBEYOND_LOCK = "live:liveRecord:handleBeyondTask:lock";

    /**
     * 录播 目前商家回放视频暂用的存储空间 key
     */
    public static final String LIVERECORD_VIDEO_USESTORAGE_KEY="live:liveRecord:video:useStorage:%s";

    /**
     * 录播 云点播视频流量统计 锁
     */
    public static final String LIVERECORD_VIDEO_VODCOUNT_LOCK = "live:liveRecord:vodCount:lock";

    /**
     * 录播 云点播视频流量统计 锁
     */
    public static final String LIVERECORD_VIDEO_VODCOUNT_KEY = "live:liveRecord:vodCount:lock:%s";

    /**
     * 录播 查询零点时商户剩余流量 锁
     */
    public static final String LIVERECORD_FLUX_ZEROCOUNT_LOCK = "live:liveRecord:fluxZeroCount:lock";

    /**
     * 录播 统计前一天单个直播场次消耗流量总和 锁
     */
    public static final String LIVERECORD_FLUX_COUNTBYROOM_LOCK = "live:liveRecord:flux:CountByRoom:lock";

    /**
     * 录播 统计暂停、结束直播场次消耗流量 锁
     */
    public static final String LIVE_FLUX_COUNTBYROOM_LOCK = "live:flux:CountByRoom:pause:lock";

    /**
     * 录播 单个视频合并任务查询处理 锁
     */
    public static final String LIVERECORD_VIDEO_MERGETASK_LIVERECORD_LOCK = "live:liveRecord:videoMergeTask:lock:%s";

    /**
     * 录播 单个录播视频获取处理 锁
     */
    public static final String LIVERECORD_VIDEO_GET_LIVERECORD_LOCK = "live:liveRecord:videoGet:lock:%s";

    /**
     * 录播 单个pid删除溢出部分回放视频 锁
     */
    public static final String LIVERECORD_VIDEO_DELETEBEYOND_PID_LOCK = "live:liveRecord:deleteBeyondTask:lock:%s";

    /**
     * 录播 视频获取处理队列 key
     */
    public static final String LIVERECORD_VIDEO_GET_QUEUE = "live:liveRecord:videoGet:queue";

    /**
     * 录播 视频合并任务查询处理队列 key
     */
    public static final String LIVERECORD_VIDEO_MERGETASK_QUEUE = "live:liveRecord:videoMergeTask:queue";

    /**
     * 录播 录播视频片段fileId key
     */
    public static final String LIVERECORD_VIDEO_PART_FILEID = "live:liveRecord:videopart:fileId:%s";

    /**
     * 录播 存储空间连续溢出天数 key
     */
    public static final String LIVERECORD_VIDEO_BEYONDDAY_QUEUE = "live:liveRecord:beyondDay:queue";

    /**
     * 录播 零点时商户剩余流量 key
     */
    public static final String LIVERECORD_FLUX_ZEROCOUNT_KEY = "live:liveRecord:flux:ZeroCount:%s";

    /**
     * 录播 目前回放消耗流量总数 key
     */
    public static final String LIVERECORD_FLUX_USETOTAL_KEY = "live:flux:liveRecordTotal:%s";

    /**
     * 录播 目前直播消耗流量总数 key
     */
    public static final String LIVE_FLUX_USETOTAL_KEY = "live:flux:liveUseTotal:%s";

    /**
     * 录播 目前购买流量变动总数 key
     */
    public static final String LIVE_FLUX_BUYTOTAL_KEY = "live:flux:buyTotal:%s";

    /**
     * 录播 目前套餐到期流量变动总数 key
     */
    public static final String LIVE_FLUX_PACKAGE_EXPTRED_TOTAL_KEY = "live:flux:package:expired:total:%s";

    /**
     * 录播 目前套餐升级流量变动总数 key
     */
    public static final String LIVE_FLUX_PACKAGE_UPGRADE_TOTAL_KEY = "live:flux:package:upgrade:total:%s";

    /**
     * 录播 暂停、结束直播后第二天需要统计的直播场次 queue
     */
    public static final String LIVE_ROOM_NEEDCOUNT_QUEUE = "live:room:needCount:queue:%s";

    /**
     * 录播 暂停、结束直播后5分钟需要统计直播消耗流量的场次 延迟队列
     */
    public static final String LIVE_ROOM_NEEDCOUNTUSE_QUEUE="live:room:needCountUse:queue";

    /**
     * 录播 暂停、结束直播时需要统计的直播场次信息 key
     */
    public static final String LIVE_ROOM_NEEDCOUNT_INFO_KEY = "live:room:needCount:info:%s:%s";
}
