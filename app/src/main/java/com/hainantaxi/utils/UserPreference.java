package com.hainantaxi.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference extends BasePreference {

    private static final String PREFER_NAME = "UserPreference";
    public static final String TOKEN = "access_token";
    public static final String USERID = "user_id";
    public static final String EXPIRES = "expires";
    private static final String TS = "ts";
    private static final String LAST_CHECK_UPDATE = "lastcheckupdate";


    public static void setLastCheckUpdate(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(LAST_CHECK_UPDATE, value);
        edit.commit();
    }


    public static String getLastCheckUpdate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        return sp.getString(LAST_CHECK_UPDATE, null);
    }


    /**
     * 保存用户登陆token
     */
    public static void setToken(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(TOKEN, value);
        edit.commit();
    }

    /**
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        return sp.getString(TOKEN, null);
    }

    /**
     * 保存用户登陆userid
     */
    public static void setUserID(Context context, String userid) {
        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(USERID, userid);
        edit.commit();
    }

    /**
     * @return
     */
    public static String getUserID(Context context) {

        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        return sp.getString(USERID, null);
    }

    /**
     * 保存用户登陆expires
     */
    public static void setExpires(Context context, String expires) {

        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(EXPIRES, expires);
        edit.commit();
    }

    public static String getExpires(Context context) {

        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        return sp.getString(EXPIRES, null);
    }

    /**
     * 保存一个时间戳
     */
    public static void setTs(Context context, String ts) {

        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(TS, ts);
        edit.commit();
    }

    public static String getTs(Context context) {

        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND| Context.MODE_PRIVATE);
        return sp.getString(TS, null);
    }

    private static final String PUSH_STATE = "push_state";

    public static void setPushState(Context context, int state) {

        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(PUSH_STATE, state);
        edit.commit();
    }

    public static int getPushState(Context context) {

        SharedPreferences sp = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_APPEND | Context.MODE_PRIVATE);
        return sp.getInt(PUSH_STATE, -1);
    }
}
