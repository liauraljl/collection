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
    public DateUtils() {
    }

    /**
     * 日期转换成指定格式的字符串
     * <pre>
     * DateUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")   =2016-04-12 11:51:24
     * </pre>
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String str = format.format(date);
        return str;
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
        Date endDate = getNextMonth(startDate, new Long(1L));
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

    public static void main(String[] args) {
        Date date = getDate(20060131);
        String registerTime = formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(registerTime);
    }

    public static int compareDate(Date date1, Date date2, int precision) {
        Calendar c = Calendar.getInstance();
        List<Integer> fields = new ArrayList();
        fields.add(new Integer(1));
        fields.add(new Integer(2));
        fields.add(new Integer(5));
        fields.add(new Integer(12));
        fields.add(new Integer(13));
        fields.add(new Integer(14));
        Date d1 = date1;
        Date d2 = date2;
        if(fields.contains(new Integer(precision))) {
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

    public static Date getFistTimeOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static String getCurrentDayString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return sdf.format(new Date());
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static int getCurrentMonth() {
        return getMonth(new Date());
    }

    public static Date getStartTimeOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        calendar.clear();
        calendar.set(1, year);
        calendar.set(2, month - 1);
        calendar.set(5, 1);
        calendar.set(11, 0);
        return calendar.getTime();
    }

    public static Date getDateBefore(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(5, now.get(5) - day);
        return now.getTime();
    }

    public static Date getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(5, now.get(5) + day);
        return now.getTime();
    }
}
