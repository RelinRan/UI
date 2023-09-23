package androidx.ui.text;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间日期
 */
public class Time {

    /**
     * 年-月-日
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 24小时 - 时：分：秒
     */
    public static final String H24_MM_SS = "HH:mm:ss";
    /**
     * 12小时 - 时：分：秒
     */
    public static final String H12_MM_SS = "hh:mm:ss";
    /**
     * 24小时 - 时：分
     */
    public static final String H24_MM = "24H:mm";
    /**
     * 12小时 - 时：分
     */
    public static final String H12_MM = "12H:mm";
    /**
     * 月-日
     */
    public static final String MM_DD = "MM-dd";
    /**
     * 24小时，年-月-日 时：分
     */
    public static final String YYYY_MM_DD_H24_MM = "yyyy-MM-dd HH:mm";
    /**
     * 24小时，年-月-日 时：分：秒
     */
    public static final String YYYY_MM_DD_H24_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 12小时，年-月-日 时：分
     */
    public static final String YYYY_MM_DD_H12_MM = "yyyy-MM-dd hh:mm";
    /**
     * 12小时，年-月-日 时：分：秒
     */
    public static final String YYYY_MM_DD_H12_MM_SS = "yyyy-MM-dd hh:mm:ss";

    /**
     * 现在的日期时间
     *
     * @return
     */
    public static String now() {
        return format(YYYY_MM_DD_H24_MM_SS).format(new Date());
    }

    /**
     * 现在的日期时间
     *
     * @param pattern 日期格式
     * @return
     */
    public static String now(String pattern) {
        return format(pattern).format(new Date());
    }

    /**
     * @return 现在时间
     */
    public static long time() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * 现在时间
     *
     * @param time    时间
     * @param pattern 时间格式
     * @return
     */
    public static long time(String time, String pattern) {
        try {
            return format(pattern).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 时间戳转时间
     *
     * @param timestamp 时间戳，单位秒
     * @return
     */
    public static String parseTime(String timestamp) {
        return parseTime(timestamp, TimeUnit.SECONDS);
    }

    /**
     * 时间戳转时间
     *
     * @param timestamp 时间戳
     * @param unit      单位
     * @return
     */
    public static String parseTime(String timestamp, TimeUnit unit) {
        if (TextUtils.isEmpty(timestamp)) {
            return "";
        }
        long time = Long.parseLong(timestamp);
        if (unit == TimeUnit.SECONDS) {
            time *= 1000;
        }
        if (unit == TimeUnit.MILLISECONDS) {
            time *= 1;
        }
        if (unit == TimeUnit.MINUTES) {
            time = 1000 * 60;
        }
        return format(YYYY_MM_DD_H24_MM_SS).format(new Date(time));
    }

    /**
     * 时间戳转时间
     *
     * @param timestamp 时间戳
     * @param pattern    格式
     * @param unit      单位
     * @return
     */
    public static String parseTime(String timestamp, String pattern, TimeUnit unit) {
        if (TextUtils.isEmpty(timestamp)) {
            return "";
        }
        long time = Long.parseLong(timestamp);
        if (unit == TimeUnit.SECONDS) {
            time *= 1000;
        }
        if (unit == TimeUnit.MILLISECONDS) {
            time *= 1;
        }
        if (unit == TimeUnit.MINUTES) {
            time = 1000 * 60;
        }
        return format(pattern).format(new Date(time));
    }

    /**
     * 时间转时间戳
     *
     * @param time 时间字符串
     * @return
     */
    public static long parseTimestamp(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0;
        }
        return parseTimestamp(time, YYYY_MM_DD_H24_MM_SS, TimeUnit.SECONDS);
    }

    /**
     * 时间转时间戳
     *
     * @param time   时间字符
     * @param pattern 时间格式
     * @param unit   单位
     * @return
     */
    public static long parseTimestamp(String time, String pattern, TimeUnit unit) {
        if (TextUtils.isEmpty(time)) {
            return 0;
        }
        if (unit == TimeUnit.SECONDS) {
            return parse(time, pattern).getTime() / 1000;
        }
        if (unit == TimeUnit.MILLISECONDS) {
            return parse(time, pattern).getTime();
        }
        if (unit == TimeUnit.MINUTES) {
            return parse(time, pattern).getTime() / 1000 / 60;
        }
        return parse(time, pattern).getTime() / 1000;
    }

    /**
     * 字符串转时间
     *
     * @param time 时间字符串
     * @return
     */
    public static Date parse(String time) {
        Date date = null;
        try {
            date = format(YYYY_MM_DD_H24_MM_SS).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转时间
     *
     * @param time   时间字符
     * @param pattern 时间格式
     * @return
     */
    public static Date parse(String time, String pattern) {
        Date date = null;
        try {
            date = format(pattern).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期时间差
     *
     * @param start  开始时间
     * @param end    结束时间
     * @param pattern 时间格式
     * @return
     */
    public static long diff(String start, String end, String pattern) {
        return parse(end, pattern).getTime() - parse(start, pattern).getTime();
    }

    /**
     * 日期时间差
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static long diff(Date start, Date end) {
        return end.getTime() - start.getTime();
    }

    /**
     * 创建时间日期格式对象
     *
     * @param pattern 格式
     * @return
     */
    public static SimpleDateFormat format(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 通过年份和月份获取对应的月份的天数
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public static int days(int year, int month) {
        if (year % 100 == 0 && year % 400 == 0 && month == 2) return 29;
        else {
            switch (month) {
                case 2:
                    return 28;
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return 31;
                case 4:
                case 6:
                case 9:
                case 11:
                    return 30;
            }
        }
        return 0;
    }

    /**
     * 分割时间
     *
     * @param time    时间字符串，yyyy-MM-dd HH:mm:ss
     * @param pattern 时间格式
     * @return
     */
    public static int[] split(String time, String pattern) {
        int[] array = new int[6];
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format(pattern).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            array[0] = calendar.get(Calendar.YEAR);
            array[1] = calendar.get(Calendar.MONTH + 1);
            array[2] = calendar.get(Calendar.DAY_OF_MONTH);
            array[3] = calendar.get(Calendar.HOUR_OF_DAY);
            array[4] = calendar.get(Calendar.MINUTE);
            array[5] = calendar.get(Calendar.SECOND);
        }
        return array;
    }

}
