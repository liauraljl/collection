package com.ljl.note.collection.liveRecord.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "t_live_record_flux_count")
public class LiveRecordFluxCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 视频文件id(腾讯)
     */
    @Column(name = "file_id")
    private String fileId;

    /**
     * 统计时间
     */
    @Column(name = "count_time")
    private Date countTime;

    /**
     * 回放视频名称
     */
    @Column(name = "live_record_name")
    private String liveRecordName;

    /**
     * 商家pid
     */
    private Long pid;

    @Column(name = "live_record_id")
    private Long liveRecordId;

    /**
     * 消耗流量(带单位B、KB、MB、GB、TB、PB)
     */
    @Column(name = "use_flux_str")
    private String useFluxStr;

    /**
     * 消耗流量(不带单位，B)
     */
    @Column(name = "use_flux")
    private Long useFlux;

    /**
     * 剩余流量(不带单位，B)
     */
    @Column(name = "left_flux")
    private Long leftFlux;

    /**
     * 剩余流量(带单位B、KB、MB、GB、TB、PB)
     */
    @Column(name = "left_flux_str")
    private String leftFluxStr;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private Integer deleted;
}