package com.ljl.note.collection.liveRecord.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "t_live_record")
public class LiveRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商户id
     */
    private Long pid;

    /**
     * 直播间id
     */
    @Column(name = "live_room_id")
    private Long liveRoomId;

    /**
     * 直播场次id
     */
    @Column(name = "room_id")
    private Long roomId;

    /**
     * 录制任务id
     */
    @Column(name = "record_task_id")
    private Integer recordTaskId;

    /**
     * 合并任务id
     */
    @Column(name = "merge_task_id")
    private String mergeTaskId;

    /**
     * 腾讯云文件FileId
     */
    @Column(name = "qcloud_file_id")
    private String qcloudFileId;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 分享标题
     */
    @Column(name = "share_title")
    private String shareTitle;

    /**
     * 视频总时长(s)
     */
    @Column(name = "total_duration")
    private Float totalDuration;

    /**
     * 总视频大小
     */
    @Column(name = "total_file_size")
    private Long totalFileSize;

    /**
     * 回放视频url
     */
    @Column(name = "video_download_url")
    private String videoDownloadUrl;

    /**
     * 默认封面
     */
    @Column(name = "default_cover")
    private String defaultCover;

    /**
     * 自定义封面
     */
    @Column(name = "customize_cover")
    private String customizeCover;

    /**
     * 默认分享图片
     */
    @Column(name = "default_share_photo")
    private String defaultSharePhoto;

    /**
     * 自定义分享图片
     */
    @Column(name = "customize_share_photo")
    private String customizeSharePhoto;

    /**
     * 视频录制状态（0-录制中，1-结束录制，2-合并中，3-成功(合并完成)，4-录制失败）
     */
    @Column(name = "record_task_status")
    private Integer recordTaskStatus;

    /**
     * 是否对客户开启回放视频(0未开启，1已开启)
     */
    @Column(name = "cus_open_flag")
    private Integer cusOpenFlag;

    /**
     * 开始录制时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束录制时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 是否删除(0未删除，1已删除)
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 绑定小程序类型0：商家 1.平台
     */
    @Column(name = "applet_type")
    private Integer appletType;

    /**
     * 门店id
     */
    @Column(name="store_id")
    private Long storeId;
}