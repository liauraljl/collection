package com.ljl.note.collection.common.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtils {
    public static final String formatPattern = "yyyy-MM-dd";
    public static final String formatPattern_Short = "yyyyMMdd";
    public static final String formatPattern_chinese = "yyyy年MM月dd日";
    public static final String FORMAT_PATTERN_MEDIUM = "yyyy-MM-dd HH:mm:ss";
    public static final String[] WEEK_DAYS = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public DateUtils() {
    }

    public static Date strToDate(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = sdf.parse(time);
        return d;
    }

    public static Date strToDate(String time, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = sdf.parse(time);
        return d;
    }

    public static Date strToTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd:HH");
        Date d = sdf.parse(time);
        return d;
    }

    public static String getHourStr() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        return sdf.format(new Date());
    }

    public static Date getDate(int year, int month, int day) {
        GregorianCalendar d = new GregorianCalendar(year, month - 1, day);
        return d.getTime();
    }

    public static Date getDate(int yyyyMMdd) {
        int dd = yyyyMMdd % 100;
        int yyyyMM = yyyyMMdd / 100;
        int mm = yyyyMM % 100;
        int yyyy = yyyyMM / 100;
        GregorianCalendar d = new GregorianCalendar(yyyy, mm - 1, dd);
        return d.getTime();
    }

    public static Date getDate(int year, int month, int day, int hour) {
        GregorianCalendar d = new GregorianCalendar(year, month - 1, day, hour, 0);
        return d.getTime();
    }

    public static Date getRoundedHourCurDate() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.clear(12);
        cal.clear(13);
        cal.clear(14);
        return cal.getTime();
    }

    public static Date getThisYearLastDate() {
        Calendar cal = GregorianCalendar.getInstance();
        GregorianCalendar d = new GregorianCalendar(cal.get(1), 11, 31, 23, 59, 59);
        return d.getTime();
    }

    public static Date getRoundedDayCurDate() {
        Calendar cal = new GregorianCalendar();
        return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5))).getTime();
    }

    public static Date getRoundedHourDate(Date dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.clear(12);
        cal.clear(13);
        cal.clear(14);
        return cal.getTime();
    }

    public static Date getRoundedDayDate(Date dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5))).getTime();
    }

    public static Date getNextDay(Date dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5) + 1)).getTime();
    }

    public static Date getWeekDay(Date dt, int weekDay) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        if(weekDay == 7) {
            weekDay = 1;
        } else {
            ++weekDay;
        }

        cal.set(7, weekDay);
        return cal.getTime();
    }

    public static Date getNextDay(Date dt, Long n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5) + n.intValue())).getTime();
    }

    public static Date getNextMonth(Date dt, Long n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        Calendar firstCal = new GregorianCalendar(cal.get(1), cal.get(2) + n.intValue(), 1);
        return firstCal.getActualMaximum(5) < cal.get(5)?(new GregorianCalendar(cal.get(1), cal.get(2) + n.intValue(), firstCal.getActualMaximum(5))).getTime():(new GregorianCalendar(cal.get(1), cal.get(2) + n.intValue(), cal.get(5))).getTime();
    }

    public static Date getNextHour(Date dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.set(11, cal.get(11) + 1);
        return cal.getTime();
    }

    public static long getBetweenDate(Date startDate, Date endDate) {
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long dayTime = 86400000L;
        long days = (endDateTime - startDateTime) / dayTime;
        return days;
    }

    public static long getMonthLength(String countDate) throws ParseException {
        String firstDay = countDate.substring(0, countDate.length() - 2) + "01";
        Date startDate = strToDate(firstDay);
        Date endDate = getNextMonth(startDate, Long.valueOf(1L));
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long dayTime = 86400000L;
        long days = (endDateTime - startDateTime) / dayTime;
        return days;
    }

    public static Date getNextDay() {
        Calendar cal = GregorianCalendar.getInstance();
        return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5) + 1)).getTime();
    }

    public static final Date getNextDays(int days) {
        Calendar cal = GregorianCalendar.getInstance();
        return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5) - days)).getTime();
    }

    public static final Date getLastDateByHours(int hours) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(10, -hours);
        return cal.getTime();
    }

    public static final Date getLastDateByHours(Date date, int hours) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(10, -hours);
        return cal.getTime();
    }

    public static final Date getLastDateByMins(int mins) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(12, -mins);
        return cal.getTime();
    }

    public static final Date getLastDateByMins(Date date, int mins) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(12, -mins);
        return cal.getTime();
    }

    public static Timestamp convertSqlDate(Date dt) {
        return dt == null?new Timestamp(0L):new Timestamp(dt.getTime());
    }

    public static String formatCurrrentDate() {
        Date pdate = new Date();
        return formatDate(pdate, "yyyyMMdd");
    }

    public static String formatDate(Date pDate, String format) {
        if(pDate == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(pDate);
        }
    }

    public static String getHour(Date pDate) {
        return formatDate(pDate, "HH");
    }

    public static Calendar getTheLastDayOfTheMonth(int year, int month) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, 1);
        return new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5) - 1);
    }

    public static boolean validateDateString(String dateString) {
        if(dateString != null && !dateString.equals("")) {
            String regDate = "^(((([02468][048])|([13579][26]))[0]{2})(02)(([0][1-9])|([1-2][0-9])))|(((([02468][1235679])|([13579][01345789]))[0]{2})(02)(([0][1-9])|([1][0-9])|([2][0-8])))|(([0-9]{2}(([0][48])|([2468][048])|([13579][26])))(02)(([0][1-9])|([1-2][0-9])))|(([0-9]{2}(([02468][1235679])|([13579][01345789])))(02)(([0][1-9])|([1][0-9])|([2][0-8])))|(([0-9]{4})(([0]{1}(1|3|5|7|8))|10|12)(([0][1-9])|([1-2][0-9])|30|31))|(([0-9]{4})(([0]{1}(4|6|9))|11)(([0][1-9])|([1-2][0-9])|30))$";
            return dateString.matches(regDate);
        } else {
            return false;
        }
    }

    public static boolean validateDateString(String dateString, String format) {
        if(dateString != null && !dateString.equals("")) {
            if(format != null && !format.equals("")) {
                Date date = null;
                SimpleDateFormat df = new SimpleDateFormat(format);
                df.setLenient(false);

                try {
                    df.parse(dateString);
                    return true;
                } catch (Exception var5) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws ParseException {
        Date date = getDate(20060131);
        String registerTime = formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(registerTime);
        int space = getMonthSpace(getDate(20150106), getDate(20151221));
        System.out.println(space);
        System.out.println(getStartOfDate(new Date()));
        System.out.println("last day of month" + getMonthLast(11));
        System.out.println(getPrefixDate("1"));
    }

    public static int compareDate(Date date1, Date date2, int precision) {
        Calendar c = Calendar.getInstance();
        List<Integer> fields = new ArrayList();
        fields.add(Integer.valueOf(1));
        fields.add(Integer.valueOf(2));
        fields.add(Integer.valueOf(5));
        fields.add(Integer.valueOf(12));
        fields.add(Integer.valueOf(13));
        fields.add(Integer.valueOf(14));
        Date d1 = date1;
        Date d2 = date2;
        if(fields.contains(Integer.valueOf(precision))) {
            c.setTime(date1);

            int i;
            int field;
            for(i = 0; i < fields.size(); ++i) {
                field = ((Integer)fields.get(i)).intValue();
                if(field > precision) {
                    c.set(field, 0);
                }
            }

            d1 = c.getTime();
            c.setTime(date2);

            for(i = 0; i < fields.size(); ++i) {
                field = ((Integer)fields.get(i)).intValue();
                if(field > precision) {
                    c.set(field, 0);
                }
            }

            d2 = c.getTime();
        }

        return d1.compareTo(d2);
    }

    public static int compareDateByDay(Date date1, Date date2) {
        return BigDecimal.valueOf(date2.getTime() - date1.getTime()).divide(BigDecimal.valueOf(86400000L)).intValue();
    }

    public static Date getLastNDay(Date dt, Long n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5) - n.intValue())).getTime();
    }

    public static Date getLastMonth(Date dt, Long n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return (new GregorianCalendar(cal.get(1), cal.get(2) - n.intValue(), cal.get(5))).getTime();
    }

    public static Date getDateFromStr(String str, String format) {
        if(StringUtils.isEmptyOrNull(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        if(StringUtils.isEmptyOrNull(str)) {
            return null;
        } else {
            SimpleDateFormat df = new SimpleDateFormat(format);

            try {
                return df.parse(str);
            } catch (ParseException var4) {
                throw new IllegalArgumentException("根式不正确");
            }
        }
    }

    public static boolean isDate(String str, String format) {
        if(StringUtils.isEmptyOrNull(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        if(StringUtils.isEmptyOrNull(str)) {
            return false;
        } else {
            SimpleDateFormat df = new SimpleDateFormat(format);

            try {
                df.parse(str);
                return true;
            } catch (ParseException var4) {
                return false;
            }
        }
    }

    public static Date getDateForEighteenHour(Date dt) {
        if(dt == null) {
            dt = new Date();
        }

        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.set(11, 18);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getChangeDateAddTime(Date date, Date time, String type, int iQuantity) {
        int year = Integer.parseInt(formatDate(date, "yyyy"));
        int month = Integer.parseInt(formatDate(date, "MM"));
        --month;
        int day = Integer.parseInt(formatDate(date, "dd"));
        int hour = Integer.parseInt(formatDate(time, "HH"));
        int mi = Integer.parseInt(formatDate(time, "mm"));
        int ss = Integer.parseInt(formatDate(time, "ss"));
        GregorianCalendar gc = new GregorianCalendar(year, month, day, hour, mi, ss);
        if(iQuantity != 0 && type != null) {
            if(type.equalsIgnoreCase("y")) {
                gc.add(1, iQuantity);
            } else if(type.equalsIgnoreCase("m")) {
                gc.add(2, iQuantity);
            } else if(type.equalsIgnoreCase("d")) {
                gc.add(5, iQuantity);
            } else if(type.equalsIgnoreCase("h")) {
                gc.add(10, iQuantity);
            } else if(type.equalsIgnoreCase("mi")) {
                gc.add(12, iQuantity);
            } else if(type.equalsIgnoreCase("s")) {
                gc.add(13, iQuantity);
            }

            return gc.getTime();
        } else {
            return gc.getTime();
        }
    }

    public static String getLastMonday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(2);
        c.set(7, 2);
        c.add(5, -7);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getTwoMonday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(2);
        c.set(7, 2);
        c.add(5, -14);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getThrMonday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(2);
        c.set(7, 2);
        c.add(5, -21);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getLastSunday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(2);
        c.set(7, 1);
        c.add(5, -7);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getPreMonthFirst(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(5, 1);
        c.add(2, -1);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getPreMonthLast(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(5, 1);
        c.add(5, -1);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getThrMonthFirst(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(2);
        if(month != 0 && month != 1 && month != 2) {
            if(month != 3 && month != 4 && month != 5) {
                if(month != 6 && month != 7 && month != 8) {
                    c.set(2, 6);
                } else {
                    c.set(2, 3);
                }
            } else {
                c.set(2, 0);
            }
        } else {
            c.set(2, 9);
            c.add(1, -1);
        }

        c.set(5, 1);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getThrMonthLast(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(2);
        if(month != 0 && month != 1 && month != 2) {
            if(month != 3 && month != 4 && month != 5) {
                if(month != 6 && month != 7 && month != 8) {
                    c.set(2, 9);
                } else {
                    c.set(2, 6);
                }
            } else {
                c.set(2, 3);
            }
        } else {
            c.set(2, 0);
        }

        c.set(5, 1);
        c.add(5, -1);
        String result = sdf.format(c.getTime());
        return result;
    }

    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static String getDesignatedDate(long timeDiff) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long nowTime = System.currentTimeMillis();
        long designTime = nowTime - timeDiff;
        return format.format(Long.valueOf(designTime));
    }

    public static String getPrefixDate(String count) {
        Calendar cal = Calendar.getInstance();
        int day = 0 - Integer.parseInt(count);
        cal.add(5, day);
        Date datNew = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(datNew);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String dateToString(Date date, String formatPattern) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);
    }

    public static Date stringToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(!str.equals("") && str != null) {
            try {
                return format.parse(str);
            } catch (ParseException var3) {
                var3.printStackTrace();
            }
        }

        return null;
    }

    /*public static boolean timeSubtract(String date1, String date2) {
        SimpleDateFormat dfs = new SimpleDateFormat("HH:mm");
        Date begin = null;
        Date end = null;

        try {
            begin = dfs.parse(date1);
            end = dfs.parse(date2);
            return begin.before(end);
        } catch (ParseException var7) {
            String msg = String.format("Exception when try to timeSubtract date1 %s, date2 %s: %s", new Object[]{date1, date2, var7.getMessage()});
            throw new UtilityRuntimeException(msg, var7);
        }
    }*/

    public static Date getFistYearAgo(Date dt, Long n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return (new GregorianCalendar(cal.get(1) - n.intValue(), 0, 1)).getTime();
    }

    public static Date getLastYearAgo(Date dt, Long n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return (new GregorianCalendar(cal.get(1) - n.intValue(), 11, 31)).getTime();
    }

    public static int getMonthSpace(Date dateBegin, Date dateEnd) {
        try {
            long startDateTime = dateBegin.getTime();
            long endDateTime = dateEnd.getTime();
            long dayTime = 86400000L;
            double days = (double)(Math.abs(endDateTime - startDateTime) / dayTime);
            int month = (int)Math.ceil(days / 30.0D);
            return month;
        } catch (Exception var11) {
            return 2147483647;
        }
    }

    public static int getSecondSpace(Date dateBegin, Date dateEnd) {
        long startDateTime = dateBegin.getTime();
        long endDateTime = dateEnd.getTime();
        long secondTime = 1000L;
        double seconds = (double)(Math.abs(endDateTime - startDateTime) / secondTime);
        int result = (int)Math.ceil(seconds);
        return result;
    }

    public static Date getStartOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(9);
        calendar.clear(10);
        calendar.clear(11);
        calendar.clear(12);
        calendar.clear(13);
        calendar.clear(14);
        return calendar.getTime();
    }

    public static Date getStartOfNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(9);
        calendar.clear(10);
        calendar.clear(11);
        calendar.clear(12);
        calendar.clear(13);
        calendar.clear(14);
        calendar.add(5, 1);
        return calendar.getTime();
    }

    public static String getLeftTimeString(Date deadDate) {
        Date currentDate = new Date();
        long diff = (deadDate.getTime() - currentDate.getTime()) / 1000L;
        if(diff <= 0L) {
            return "";
        } else {
            long second = diff % 60L;
            long minute = diff / 60L % 60L;
            long hour = diff / 3600L % 24L;
            long day = diff / 86400L;
            StringBuilder builder = new StringBuilder();
            if(day > 0L) {
                builder.append(day);
                builder.append("天");
            }

            if(hour > 0L) {
                builder.append(hour);
                builder.append("小时");
            }

            if(minute > 0L) {
                builder.append(minute);
                builder.append("分");
            }

            builder.append(second);
            builder.append("秒");
            return builder.toString();
        }
    }

    public static Date getCurrentYearLast() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(1);
        return getYearLast(currentYear);
    }

    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1, year);
        calendar.roll(6, -1);
        Date date = calendar.getTime();
        return date;
    }

    public static Date getMonthLast(int month) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        calendar.clear();
        calendar.set(1, year);
        calendar.set(2, month - 1);
        calendar.roll(5, -1);
        Date date = calendar.getTime();
        return date;
    }

    public static String getWeekDayChinese(Date date) {
        return WEEK_DAYS[date.getDay()];
    }
}
