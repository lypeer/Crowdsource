package com.tesmple.crowdsource.utils;

import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ESIR on 2015/10/11.
 */
public class TimeUtils {

    private static Calendar calendar = Calendar.getInstance();
    /**
     * 获得当前年份
     * 返回int值的年份值
     */
    public static int getNowYear(){
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前月份
     * 返回int值的月份值
     */
    public static int getNowMonth(){
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * 获得当前月中的日子
     * 返回int值的日号
     */
    public static int getNowDay(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前小时(24小时制)
     * 返回int值的小时数
     */
    public static int getNowHour(){
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得当前分钟数
     * 返回int值的分钟数
     */
    public static int getNowMinute(){
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static java.util.Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        java.util.Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     *  将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     *  @param dateDate  * @return  */
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @param k
     * @return
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = (Date) formatter.parse(strDate, pos);
        return strtodate;
    }
}
