package com.hainantaxi.net.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hainantaxi.modle.entity.response.UserData;
import com.hainantaxi.modle.entity.response.UserToken;
import com.hainantaxi.net.service.PreferenceService;

import javax.inject.Singleton;

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
