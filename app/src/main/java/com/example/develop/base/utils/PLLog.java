package com.example.develop.base.utils;

import android.util.Log;

import com.tencent.bugly.crashreport.BuglyLog;

/**
 * Created by Tbxark on 9/8/16.
 */

public class PLLog {
    public static int LOWEST_LOG_LEVEL = 1;// 最低日志显示级别
    private static int SYSTEM = 1;
    private static int VERBOS = 2;
    private static int DEBUG = 3;
    private static int INFO = 4;
    private static int WARN = 5;
    private static int ERROR = 6;

    public static void i(String tag, String message) {
        if (LOWEST_LOG_LEVEL <= INFO) {//
            Log.i(tag, message + "");
        } else {
            BuglyLog.e(tag, message + "");
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (LOWEST_LOG_LEVEL <= ERROR) {
            Log.e(tag, message + "", throwable);
        }else {
            BuglyLog.e(tag, message + "");
        }
    }

    public static void e(String tag, String message) {
        if (LOWEST_LOG_LEVEL <= ERROR) {
            Log.e(tag, message + "");
        }else {
            BuglyLog.e(tag, message + "");
        }
    }

    public static void d(String tag, String message) {
        if (LOWEST_LOG_LEVEL <= DEBUG) {
            Log.d(tag, message + "");
        }else {
            BuglyLog.e(tag, message + "");
        }
    }

    public static void w(String tag, String message) {
        if (LOWEST_LOG_LEVEL <= WARN) {
            Log.w(tag, message + "");
        }else {
            BuglyLog.e(tag, message + "");
        }
    }

    public static void v(String tag, String message) {
        if (LOWEST_LOG_LEVEL <= VERBOS) {
            Log.v(tag, message + "");
        }else {
            BuglyLog.e(tag, message + "");
        }
    }

    public static void s(String message) {
        if (LOWEST_LOG_LEVEL <= SYSTEM) {
            System.out.println(message + "");
        }
    }

}