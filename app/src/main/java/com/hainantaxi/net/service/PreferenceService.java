package com.hainantaxi.net.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.hainantaxi.modle.entity.response.UserToken;

/**
 * 本地SharePreference服务，储存这用户的token，expires，user_id信息
 * Created by developer on 17-5-16.
 */
public class PreferenceService {
    public static UserToken sUserInfo;
    private static SharedPreferences mSharedPreferences;

    private Context mContext;

    private static final String PREFER_NAME = "UserPreference";
    public static final String TOKEN = "access_token";
    public static final String USERID = "user_id";
    public static final String EXPIRES = "expires";
    private static final String TS = "ts";

    public PreferenceService(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        sUserInfo = new UserToken();
        sUserInfo.setUser_id(getUserID());
        sUserInfo.setAccess_token(getToken());
        sUserInfo.setExpires(Long.parseLong(getExpires()));
    }



    public static void logiOut() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    public UserToken getUserInfo() {
        UserToken info = new UserToken();
        info.setUser_id(getUserID());
        info.setAccess_token(getToken());
        info.setExpires(Long.parseLong(getExpires()));
        return info;
    }

    public void saveUserInfo(UserToken info) {
        sUserInfo = info;
        setExpires(String.valueOf(info.getExpires()));
        setUserID(info.getUser_id());
        setToken(info.getAccess_token());
    }

    public void setToken(String value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(TOKEN, value);
        edit.commit();
    }

    public String getToken() {
        return mSharedPreferences.getString(TOKEN, "");
    }

    public void setUserID(String userid) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(USERID, userid);
        edit.commit();
    }

    public String getUserID() {
        return mSharedPreferences.getString(USERID, "");
    }

    public void setExpires(String expires) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(EXPIRES, expires);
        edit.commit();
    }

    public String getExpires() {
        return mSharedPreferences.getString(EXPIRES, "0");
    }

    public void setTs(String ts) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(TS, ts);
        edit.commit();
    }

    public static void loginOut() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getTs() {
        return mSharedPreferences.getString(TS, "");
    }



    public Context getContext() {
        return mContext;
    }
}
