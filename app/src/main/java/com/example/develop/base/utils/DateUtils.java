package com.example.develop.base.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by develop on 2016/10/18.
 */

public class DateUtils {


    public static String formatDate(long time) {
        return formatDate("yyyy年MM月dd日", new Date(time));
    }

    public static String formatDate(String template, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取时间 2016-10-18
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String d = formatter.format(date);
        return d;
    }

    /**
     * 获取时间 2016年10月18日
     *
     * @param time
     * @return
     */
    public static String getDate2(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String d = formatter.format(date);
        return d;
    }


    /**
     * @param date 2016-10-18
     * @return
     */
    public static String getDisplayDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d= formatter.parse(date);
            String currDate = getDate(System.currentTimeMillis());

            if (currDate.equals(date)) {
                return "今日 " + date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
            } else if (currDate.substring(0, 4).equals(date.substring(0, 4))) {
                return date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
            } else {
                return currDate.substring(0, 4) + "年" + date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param date 2015-3-02
     * @return
     */

    public static String getDisplayStr(String date) {

        String currDate = getDate(System.currentTimeMillis());
        if (currDate.equals(date)) {
            return "今日 " + date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
        } else if (currDate.substring(0, 4).equals(date.substring(0, 4))) {
            return date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
        } else {
            return date.substring(0, 4) + "年" + date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
        }
    }
}
