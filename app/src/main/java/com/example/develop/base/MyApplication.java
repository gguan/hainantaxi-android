package com.example.develop.base;

import android.app.Application;

import com.example.develop.base.net.ApplicationModule;
import com.example.develop.base.net.BaseRepositoryComponent;
import com.example.develop.base.net.BasePreferenceServiceModule;
import com.example.develop.base.net.BaseRepositoryModule;
import com.example.develop.base.net.DaggerBaseRepositoryComponent;
import com.example.develop.base.net.service.PreferenceService;
import com.example.develop.base.utils.FrescoUtils;
import com.tencent.bugly.crashreport.CrashReport;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by develop on 2017/5/17.
 */

public class MyApplication extends Application {


    private BaseRepositoryComponent mBaseRepositoryComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        initRepositoryComponent();
//        initBugly();
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "", false);
    }


    private void initRepositoryComponent() {
        mBaseRepositoryComponent = DaggerBaseRepositoryComponent
                .builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .basePreferenceServiceModule(new BasePreferenceServiceModule(new PreferenceService(getApplicationContext())))
                .baseRepositoryModule(new BaseRepositoryModule()).build();

        if (mBaseRepositoryComponent != null && mBaseRepositoryComponent.getBaseRepository() != null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build();
            FrescoUtils.initFresco(this, okHttpClient);
        }
    }


}
