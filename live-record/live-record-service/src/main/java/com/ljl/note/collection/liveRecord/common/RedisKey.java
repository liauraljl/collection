package com.ljl.note.collection.liveRecord.common;

public class RedisKey {

    /**
     * 录播 视频获取处理zset集合 key
     */
    public static final String LIVERECORD_VIDEO_GET_ZSET = "liveRecord:videoGet:zset";

    /**
     * 录播 视频获取处理 入队列 锁
     */
    public static final String LIVERECORD_VIDEO_INTOLIST_LOCK = "liveRecord:video:intoList:lock";

    /**
     * 录播 视频获取处理list集合 key
     */
    public static final String LIVERECORD_VIDEO_GET_LIST = "liveRecord:videoGet:list";

    /**
     * 录播 获取录播视频失败重试 key
     */
    public static final String LIVERECORD_VIDEO_GET_RETRY="liveRecord:video:get:retry:%s";

    /**
     * 录播 单个录播视频获取处理 锁
     */
    public static final String LIVERECORD_VIDEO_GET_LIVERECORD_LOCK = "liveRecord:videoGet:lock:%s";



    /**
     * 录播 视频合并任务查询处理在zset集合 key
     */
    public static final String LIVERECORD_VIDEO_GETMERGE_ZSET = "liveRecord:video:getMerge:zset";

    /**
     * 录播 视频合并任务查询处理 锁
     */
    public static final String LIVERECORD_VIDEO_GETMERGE_INTOLIST_LOCK = "liveRecord:video:getMerge:intoList:lock";

    /**
     * 录播 视频合并结果查询list集合 key
     */
    public static final String LIVERECORD_VIDEO_GETMERGE_LIST = "liveRecord:video:getMerge:list";

    /**
     * 录播 查询视频合并处理结果失败重试 key
     */
    public static final String LIVERECORD_VIDEO_GETMERGE_RETRY="liveRecord:video:getMerge:retry:%s";

    /**
     * 录播 单个视频合并任务查询处理 锁
     */
    public static final String LIVERECORD_VIDEO_GETMERGE_LOCK = "liveRecord:video:getMerge:lock:%s";



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
     * 录播 录播视频片段fileId key
     */
    public static final String LIVERECORD_VIDEO_PART_FILEID = "live:liveRecord:videopart:fileId:%s";

    /**
     * 录播 零点时商户剩余流量 key
     */
    public static final String LIVERECORD_FLUX_ZEROCOUNT_KEY = "live:liveRecord:flux:ZeroCount:%s";

    /**
     * 录播 目前回放消耗流量总数 key
     */
    public static final String LIVERECORD_FLUX_USETOTAL_KEY = "live:flux:liveRecordTotal:%s";
}
