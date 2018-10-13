package com.tmo.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static SimpleDateFormat sdfmmdd = new SimpleDateFormat("MM-dd");
    private static SimpleDateFormat sdfhhmm = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sdf = new SimpleDateFormat("mm分ss秒");
    private static SimpleDateFormat simpleDateFormat;



    public static Date StringToDate(SimpleDateFormat smf, String dateString) {
        Date date = null;
        try {
            date = smf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String DateToString(SimpleDateFormat smf, Date date) {
        return smf.format(date);
    }

    public static String DateToStringHHmm(Date date) {
        return sdfhhmm.format(date);
    }

    /**
     * 把yyyy年MM月dd日格式的字符串转化成日期
     *
     * @param dateString
     * @return
     */
    public static Date StringMMddToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return StringToDate(sdf, dateString);
    }

    /**
     * 把Date转换成yyyy年MM月dd日格式
     *
     * @param date
     * @return
     */
    public static String DateToStringMMdd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return DateToString(sdf, date);
    }


    /**
     * 获取 9:00-10:00 的开始时间
     *
     * @param dateString
     * @return
     */
    public static Date getStartAt(String dateString) {
        String startAtString = dateString.split("-")[0];
        if(startAtString.contains(" ")){
            return StringToDate(new SimpleDateFormat("yyyy年MM月dd日 HH:mm"), startAtString);
        }else {
            return StringToDate(sdfhhmm, startAtString.trim());
        }
    }

    /**
     * 获取 9:00-10:00 的结束时间
     *
     * @param dateString
     * @return
     */
    public static Date getEndAt(String dateString) {
        String startAtString = dateString.split("-")[0];
        String endAtString = dateString.split("-")[1];
        if(startAtString.contains(" ")){
            String date = startAtString.split(" ")[0];
            return StringToDate(new SimpleDateFormat("yyyy年MM月dd日 HH:mm"), date.trim()+" "+endAtString.trim());
        }
        return StringToDate(sdfhhmm, endAtString.trim());
    }


    /**
     * 如果是错误的年，则换成今年
     * @param date
     * @return
     */
    public static Date ToCurrYear(Date date){
        System.out.println("====== =======  "+date.getYear());
        if(date.getYear() < 110) {
            Date now = new Date(System.currentTimeMillis());
            date.setYear(now.getYear());
        }
        return date;
    }

    /**
     * 获取今天开始的时间
     * @return
     */
    public static Date getTodayBegin(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取今天结束时间
     * @return
     */
    public static Date getTodayEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTodayBegin());
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 获取几小时前的Date
     * @param hours
     * @return
     */
    public static Date getHoursAgo(int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.HOUR, hours*(-1));
        return calendar.getTime();
    }


    public static Date AddMinute(Date date, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static Date getmmss(String source){
        Date date;
        try {
            date = sdf.parse(source);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取分钟+秒钟
     * @param date
     * @return
     */
    public static String getMinuteAndSecond(Date date){
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();

        minute = minute + hour * 60;
        StringBuilder sb = new StringBuilder();
        if(minute < 10){
            sb.append("0");
        }
        sb.append(minute);
        sb.append("分");
        if(second < 10){
            sb.append("0");
        }
        sb.append(second);
        sb.append("秒");
        return sb.toString();
    }

    /**
     * 获得当前时间对应的指定格式
     *
     * @param pattern
     * @return
     */
    public static String currentFormatDate(String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());

    }

    //if target before date, return true
    public static boolean before(Date target, Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(target);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        if(cal1.compareTo(cal2) == -1) return true;
        return false;
    }


}
