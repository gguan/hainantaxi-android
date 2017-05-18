package com.example.develop.base.net.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.develop.base.modle.entity.response.UserData;
import com.example.develop.base.modle.entity.response.UserToken;
import com.example.develop.base.net.service.PreferenceService;

import java.util.ArrayList;

import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by develop on 2017/5/17.
 */

@Singleton
public class BaseLocalDataSource {

    private PreferenceService mPreferenceService;
    public UserData mUserData;


    public BaseLocalDataSource(@NonNull Context context, PreferenceService preferenceService) {
        mPreferenceService = preferenceService;
    }

    public UserToken getUserInfo() {
        return mPreferenceService.getUserInfo();
    }

}
