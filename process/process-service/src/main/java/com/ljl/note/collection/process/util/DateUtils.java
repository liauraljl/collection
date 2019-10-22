package com.ljl.note.collection.process.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @description: DateUtils 时间工具类
 */
public class DateUtils {

    public static DateTime toDateTime(Date date) {
        return new DateTime(date);
    }

    /**
     * 获取当前时间的  0点的时间戳
     *
     * @param currentTimestamps  当前时间的时间戳
     * @return
     */
    public static long getTodayZeroPointTimestamps(long currentTimestamps) {
        return toDateTime(new Date(currentTimestamps)).dayOfWeek().roundFloorCopy().toDate().getTime();
    }

    /**
     * 获取当前时间的  24点的时间戳
     *
     * @param currentTimestamps 当前时间的时间戳
     * @return
     */
    public static long getTodayLastPointTimestamps(long currentTimestamps) {
        return toDateTime(new Date(currentTimestamps)).dayOfWeek().roundCeilingCopy().toDate().getTime();
    }

    public static void main(String[] args) {
        System.out.println(getTodayZeroPointTimestamps(System.currentTimeMillis()));
        System.out.println(getTodayLastPointTimestamps(System.currentTimeMillis()));
    }
}
