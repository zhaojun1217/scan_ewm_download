package com.zhaoj.scan_ewm_download.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static final String ALL_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String SHOW_MONTH_PATTERN = "MM-dd HH:mm";
    boolean isDebug = true;
    long startTime, endTime;

    public TimeUtil() {
        if (isDebug) {
            startTime = System.currentTimeMillis();
        }
    }

    public void showTime(String msg) {
        if (isDebug) {
            endTime = System.currentTimeMillis();
        }
    }

    public static String formatTime(Date date, String targetFormat) {
        SimpleDateFormat formater = new SimpleDateFormat(targetFormat);
        return formater.format(date);
    }

    // 为小于10的日期添加一个0
    public static String addZero(int oneDate) {
        if (oneDate < 10) {
            return "0" + oneDate;
        } else {
            return "" + oneDate;
        }
    }

    public static String parseTime(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(ALL_TIME_PATTERN);
        String arriveTime = null;
        Date convertDate = null;
        try {
            convertDate = formatter.parse(dateStr);
            arriveTime = formatTime(convertDate, "MM-dd HH:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return arriveTime;
    }

    /**
     * 获得指定日期的前/后天
     *
     * @param specifiedDay  指定日期
     * @param specifiedSpan 时间跨度 1 后一天  -1 前一天 以此类推
     * @return
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int specifiedSpan) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + specifiedSpan);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }


    /**
     * 根据传入的指定时间和预获取时间类型,截取年/月/日
     *
     * @param appointTime 指定时间 xxxx-xx-xx
     * @param YMD         Y M D
     * @return
     */
    public static String getAppointTimeYMD(String appointTime, String YMD) {
        String result = "";
        Calendar calendar;
        if (!TextUtils.isEmpty(appointTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parse = format.parse(appointTime);
                calendar = Calendar.getInstance();
                calendar.setTime(parse);
                switch (YMD) {
                    case "Y":
                        result = String.valueOf(calendar.get(Calendar.YEAR));
                        break;
                    case "M":
                        result = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                        break;
                    case "D":
                        result = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getHourTime(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat formater2 = new SimpleDateFormat("HH:mm");
        Date convertDate = formatter.parse(dateStr);
        return formater2.format(convertDate);
    }

    public static String parseTimeCustom(String formatPattern, String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(ALL_TIME_PATTERN);
        SimpleDateFormat formater2 = new SimpleDateFormat(formatPattern);
        Date convertedTime = formatter.parse(time);
        return formater2.format(convertedTime);
    }

    public static int parseTimeToInt(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat formater2 = new SimpleDateFormat("HHmm");
        Date converDate = formatter.parse(dateStr);
        return Integer.parseInt(formater2.format(converDate));
    }

    // yyyy-MM-dd 转变成 yyyy年MM月dd日
    public static String formatTimeToYMD(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy年MM月dd日");
        return f1.format(convertStringToDate(dateStr, "yyyy-MM-dd"));
    }

    /**
     * &
     * 把字符串转化成Date对象
     *
     * @param dateStr       日期字符串
     * @param formatPattern 字符串的格式
     * @return
     */
    public static Date convertStringToDate(String dateStr, String formatPattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentTimeYMDAgo(String whatTimeTypeAgo) {
        String resultTime = "";
        //        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        switch (whatTimeTypeAgo) {
            case "Y":
                //过去一年
                c.setTime(new Date());
                c.add(Calendar.YEAR, -1);
                Date y = c.getTime();
                String year = format.format(y);
                resultTime = year;
                break;
            case "M":
                //过去一月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -1);
                Date m = c.getTime();
                String mon = format.format(m);
                resultTime = mon;
                break;
            case "3M":
                //过去三个月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -3);
                Date m3 = c.getTime();
                String mon3 = format.format(m3);
                resultTime = mon3;
                break;
            case "W":
                //过去七天
                c.setTime(new Date());
                c.add(Calendar.DATE, -7);
                Date d7 = c.getTime();
                String day7 = format.format(d7);
                resultTime = day7;
                break;
            // 过去一天
            case "-D":
                c.setTime(new Date());
                c.add(Calendar.DATE, -1);
                Date d1 = c.getTime();
                String day1 = format.format(d1);
                resultTime = day1;
                break;
            // 过去一天
            case "+D":
                c.setTime(new Date());
                c.add(Calendar.DATE, 1);
                Date d11 = c.getTime();
                String day11 = format.format(d11);
                resultTime = day11;
                break;
            default:
                resultTime = formateNowTimeYY_MM_DD();
                break;
        }
        return resultTime;
    }

    public static String reformatDateString(String dateStr, String oldPattern, String newPattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(oldPattern);
        SimpleDateFormat formater2 = new SimpleDateFormat(newPattern);
        String newDateStr = null;
        try {
            Date converDate = formatter.parse(dateStr);
            newDateStr = formater2.format(converDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDateStr;
    }

    public static String formateNowTimeWithForm(String form) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat(form);
        return f1.format(c1.getTime());
    }

    /**
     * 把当前时间格式化yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formateNowTime() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f1.format(c1.getTime());
    }

    public static String formateNowTime_() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        return f1.format(c1.getTime());
    }

    public static String formateNowTime_MMdd() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("MM-dd");
        return f1.format(c1.getTime());
    }


    public static String formateTomorrowTime() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f1.format(c1.getTime());
    }

    public static String formateTomorrowTime_() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        return f1.format(c1.getTime());
    }

    public static String formateYesterDayTime() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f1.format(c1.getTime());
    }

    public static String formateNowTime_HH() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("HH");
        return f1.format(c1.getTime());
    }

    public static String formateNowTime_mm() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("mm");
        return f1.format(c1.getTime());
    }

    public static String formateYearNowTime() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy");
        return f1.format(c1.getTime());
    }

    public static String formateNowTimeForZip() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return f1.format(c1.getTime());
    }

    public static String formateNowTimeYY_MM_DD() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        return f1.format(c1.getTime());
    }

    public static String formateNowTime_MM() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("MM");
        return f1.format(c1.getTime());
    }

    public static String formateNowTime_DD() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("dd");
        return f1.format(c1.getTime());
    }

    public static String getYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(time));
    }

    public static String getMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(new Date(time));
    }

    public static String getDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(new Date(time));
    }

    public static String getMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(new Date(time));
    }

    public static String formateTime2(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f1.format(c1.getTime());
    }

    public static String formateTime(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return f1.format(c1.getTime());
    }

    public static String _formateTime(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f1.format(c1.getTime());
    }

    public static String formateTimeYYMMDD(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy年MM月dd日");
        return f1.format(c1.getTime());
    }

    public static String formateTimeYYMM(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy年MM月");
        return f1.format(c1.getTime());
    }

    public static String formateTimeYY_MM_DD(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        return f1.format(c1.getTime());
    }

    public static String formateTimeWeekDay(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("EEEE");
        return f1.format(c1.getTime());
    }

    public static String formateTimeMM_DD(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("MM-dd");
        return f1.format(c1.getTime());
    }

    public static String[] formateTimeMM(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        String[] result = f1.format(c1.getTime()).split("-");
        return result;
    }

    public static int[] formateTimeYYMMDD() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        int[] result = new int[3];
        result[0] = Integer.parseInt(f1.format(c1.getTime()).split("-")[0]);
        result[1] = Integer.parseInt(f1.format(c1.getTime()).split("-")[1]);
        result[2] = Integer.parseInt(f1.format(c1.getTime()).split("-")[2]);
        return result;
    }

    public static final String formateTime_yyyyMMdd(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMdd");
        return f1.format(c1.getTime());
    }

    public static String formatTimeMM_dd_HH_mm(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("MM-dd HH:mm");
        return f1.format(c1.getTime());
    }

    public static String[] formateTimeMM() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        String[] result = f1.format(c1.getTime()).split("-");
        return result;
    }

    public static String[] formateTimeHHdd() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
        String[] result = f1.format(c1.getTime()).split(":");
        return result;
    }

    public static String[] formateTimeHHdd(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
        String[] result = f1.format(c1.getTime()).split(":");
        return result;
    }

    public static String formatTime_HHmm(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("HHmm");
        return f1.format(c1.getTime());
    }

    public static String formatTime_HH_mm(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
        return f1.format(c1.getTime());
    }


    public static String formateTime_yyyy_MM_dd(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        return f1.format(c1.getTime());
    }

    public static String formateTime_MM_dd(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("MM-dd");
        return f1.format(c1.getTime());
    }

    public static String formateTime_yyyy_MM_dd_hh(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd-HH");
        return f1.format(c1.getTime());
    }

    public static String formateTime_yyyy_MM_dd_hh_mm(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        return f1.format(c1.getTime());
    }


    public static String yesterdayTime_YY_MM_DD(long time) {
        time = time - 24 * 60 * 60 * 1000l;
        String MMdd = formateTimeYY_MM_DD(time);
        return MMdd;
    }

    public static String getTomorrowData_YY_MM_DD(long time) {
        time = time + 24 * 60 * 60 * 1000l;
        return formateTimeYY_MM_DD(time);
    }


    public static String yesterdayTime_MM_DD(long time) {
        time = time - 24 * 60 * 60 * 1000l;
        String MMdd = formateTimeMM_DD(time);
        return MMdd;
    }

    public static String getTomorrowData_MM_DD(long time) {
        time = time + 24 * 60 * 60 * 1000l;
        return formateTimeMM_DD(time);
    }

    public static int formateTimeHH() {
        String time = formateNowTime();
        String dayTime = time.split(" ")[1];
        int hour = Integer.parseInt(dayTime.split(":")[0]);
        if (hour >= 0 && hour < 12)
            return 1;
        else if (hour >= 12 && hour < 18)
            return 2;
        else if (hour >= 18 && hour <= 23)
            return 3;

        return 1;
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static boolean isTheSameDay(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int temp = date2.getDate() - date1.getDate();
        if (temp == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isToday(String date) {

        if (TextUtils.equals(formateNowTime_(), date)) {
            return true;
        }
        return false;
    }

    public static boolean isTheSameDayYearMonthDay(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//24小时制
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((date2.getYear() - date1.getYear() == 0)) {
            if ((date2.getMonth() - date1.getMonth() == 0)) {
                int temp = date2.getDate() - date1.getDate();
                if (temp == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static boolean isTomorrow(String data) {

        if (isTheSameDayYearMonthDay(formateTomorrowTime_(), data)) {
            return true;
        }
        return false;
    }

    public static boolean isYesterdayAgo(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//24小时制
        Date date1 = new Date();
        Date date2 = new Date();

        try {
            date1 = sdf.parse(date);
            date2 = sdf.parse(formateNowTime_());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1.getYear() < date2.getYear()) {
            return true;
        } else if (date1.getYear() > date2.getYear()) {
            return false;
        } else {
            if (date1.getMonth() < date2.getMonth()) {
                return true;
            } else if (date1.getMonth() > date2.getMonth()) {
                return false;
            } else {
                if (date1.getDate() < date2.getDate()) {
                    return true;
                } else if (date1.getDate() > date2.getDate()) {
                    return false;
                } else {
                    return false;
                }
            }
        }
    }

    public static boolean isAgo(String dateStr1, String dateStr2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//24小时制
        Date date1 = new Date();
        Date date2 = new Date();

        try {
            date1 = sdf.parse(dateStr1);
            date2 = sdf.parse(dateStr2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1.getYear() < date2.getYear()) {
            return true;
        } else if (date1.getYear() > date2.getYear()) {
            return false;
        } else {
            if (date1.getMonth() < date2.getMonth()) {
                return true;
            } else if (date1.getMonth() > date2.getMonth()) {
                return false;
            } else {
                if (date1.getDate() < date2.getDate()) {
                    return true;
                } else if (date1.getDate() > date2.getDate()) {
                    return false;
                } else {
                    return false;
                }
            }
        }
    }


    // string类型的时间转成long
    public static long timeParseToLong(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        Date date1 = new Date();
        try {
            date1 = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1.getTime();
    }

    // string类型的时间转成long
    public static long timeParseToLongYY_MM_dd(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//24小时制
        Date date1 = new Date();
        try {
            date1 = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1.getTime();
    }

    public static long timeParseToLongNoHour(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//24小时制
        Date date1 = new Date();
        try {
            date1 = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1.getTime();
    }

    public static long calDatePeriod(Date date1, Date date2, TimeUnit timeUnit) {
        long timeMills = date1.getTime() - date2.getTime();
        return timeUnit.convert(timeMills, TimeUnit.MILLISECONDS);
    }

    public static long calDatePeriod(Date date1, long date2, TimeUnit timeUnit) {
        long timeMills = date1.getTime() - date2;
        return timeUnit.convert(timeMills, TimeUnit.MILLISECONDS);
    }

    public static String reformatDateForM5(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd HH:mm");
        Date formattedDate = null;
        try {
            formattedDate = formatter.parse(Long.toString(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return Long.toString(date);
        }
        return formatter2.format(formattedDate);
    }

    public static String reformatDateForH1(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd HH");
        Date formattedDate = null;
        try {
            formattedDate = formatter.parse(Long.toString(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return Long.toString(date);
        }
        return formatter2.format(formattedDate) + ":00";
    }

    public static String getTimeForTouzi(long time) {
        long sys = System.currentTimeMillis();
        String now = formateTimeYY_MM_DD(sys);
        String yes = formateTimeYY_MM_DD(sys - 86400000);
        if (now.equals(formateTimeYY_MM_DD(time))) {
            return "今天";
        }
        if (yes.equals(formateTimeYY_MM_DD(time))) {
            return "昨天";
        } else {
            return formateTimeMM_DD(time);
        }
    }

    /**
     * 获取时间的日期(日)
     *
     * @param time
     * @return
     */
    public static String formateTimeDD(long time) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        SimpleDateFormat f1 = new SimpleDateFormat("dd");
        return f1.format(c1.getTime());
    }

    /**
     * 将服务器返回的年月日 时分秒 去除 秒
     *
     * @param time
     * @return
     */
    public static String getTimeWithOutSecond(String time) {
        long timeLong = timeParseToLong(time);
        return formatTimeMM_dd_HH_mm(timeLong);
    }

    /**
     * 获得两个日期间距多少天(含正负)
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getTimeDistance(String beginDate, String endDate) {
        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = format.parse(beginDate);
            date2 = format.parse(endDate);
        } catch (ParseException e) {

            e.printStackTrace();

        }
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date1);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date2);
        long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
        return dayDistance;
    }

    /**
     * @return
     */

    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
