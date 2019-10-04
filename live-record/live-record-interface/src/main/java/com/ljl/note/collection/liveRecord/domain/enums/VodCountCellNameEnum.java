package com.ljl.note.collection.liveRecord.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum VodCountCellNameEnum {
    Date("date","播放日期"),
    FileId("file_id","视频文件ID"),
    IpCount("ip_count","去重后的客户端IP数"),
    Flux("flux","播放流量，单位：字节（byte）"),
    PlayTimes("play_times","总的播放次数"),
    PlayTimesPC("play_times_pc","PC 端播放次数"),
    PlayTimesMobile("play_times_mobile","移动端播放次数"),
    IphonePlayTimes("iphone_play_times","iPhone 端播放次数"),
    AndroidPlayTimes("android_play_times","Android 端播放次数"),
    HostName("host_name","云点播域名");

    @Getter
    private final String cell;

    @Getter
    private final String desc;
}
