package com.tesmple.crowdsource.utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;

import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ESIR on 2015/10/11.
 */
public class TimeUtils {

    private static Calendar calendar = Calendar.getInstance();

    /**
     * 获得当前年份
     * 返回int值的年份值
     */
    public static int getNowYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前月份
     * 返回int值的月份值
     */
    public static int getNowMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前月中的日子
     * 返回int值的日号
     */
    public static int getNowDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前小时(24小时制)
     * 返回int值的小时数
     */
    public static int getNowHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得当前分钟数
     * 返回int值的分钟数
     */
    public static int getNowMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
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
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate * @return
     */
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
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

    /**
     * 将date类型转换成long类型的毫秒数
     * 传入java.util.Date类型的date
     * 返回Long类型的毫秒数
     */
    public static Long dateToLong(java.util.Date date) {
        return date.getTime();
    }

    public static String longToDate(Long longTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date dt = new Date(longTime);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * 将一个long数字转换成小时分钟秒数格式
     *
     * @param fucktime 传入一个long数字（秒数）
     * @return 返回一个装着小时、分钟、秒数的ArrayList
     */
    public static ArrayList<String> long2hourminutesecond(long fucktime) {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add(0, "0");
        arrayList.add(1, "0");
        arrayList.add(2, "0");
        for (int hour = 0; ; hour++) {
            if (fucktime < 60 * 60 * 1000) {
                hour = hour;
                arrayList.set(0, String.valueOf(hour));
                break;
            }
            fucktime = fucktime - 60 * 60 * 1000;
        }
        for (int minute = 0; ; minute++) {
            if (fucktime < 60 * 1000) {
                minute = minute;
                arrayList.set(1, String.valueOf(minute));
                break;
            }
            fucktime = fucktime - 60 * 1000;
        }
        for (int second = 0; ; second++) {
            if (fucktime < 1000) {
                second = second;
                arrayList.set(2, String.valueOf(second));
                break;
            }
            fucktime = fucktime - 1000;
        }
        return arrayList;
    }

    /**
     * 判断过去的时间已经过去多久的方法
     *
     * @param time       发生的时候的毫秒值
     * @param timePassed 时间夺取了多久的毫秒值
     * @return 判断时间之后的字段
     */
    public static String judgeTime(Long time, Long timePassed) {
        String handledTime;
        if (timePassed < StringUtils.ONE_MINUTE) {
            handledTime = App.getContext().getString(R.string.prompt_just_now);
        } else if (timePassed < StringUtils.ONE_HOUR) {
            Long timeIntoFormat = timePassed / StringUtils.ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            handledTime = String.format(App.getContext().getString(R.string.prompt_how_many_minutes), value);
        } else if (timePassed < StringUtils.ONE_DAY) {
            handledTime = new SimpleDateFormat("HH:mm").format(time);
        } else if(timePassed < StringUtils.TWO_DAY){
            handledTime = App.getContext().getString(R.string.prompt_one_day_ago);
        }else {
            handledTime = new SimpleDateFormat("MM月dd日").format(time);
        }
        return handledTime;
    }
}