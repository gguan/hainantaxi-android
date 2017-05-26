package com.hainantaxi;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.hainantaxi.mqtt.Variable;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.net.ApplicationModule;
import com.hainantaxi.net.BaseRepositoryComponent;
import com.hainantaxi.net.BasePreferenceServiceModule;
import com.hainantaxi.net.BaseRepositoryModule;
import com.hainantaxi.net.DaggerBaseRepositoryComponent;
import com.hainantaxi.net.service.PreferenceService;
import com.hainantaxi.utils.FrescoUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.android.dex.Dex;


import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import cn.smssdk.SMSSDK;
import dalvik.system.DexFile;
import okhttp3.OkHttpClient;
import rx.functions.Action1;


/**
 * Created by develop on 2017/5/17.
 */

public class MyApplication extends Application {


    private BaseRepositoryComponent mBaseRepositoryComponent;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        initRepositoryComponent();
//        initBugly();
//        initSMS();
    }

    public static Context getContext() {
        return context;
    }

    private void initSMS() {
        SMSSDK.initSDK(this, "您的appkey", "您的appsecret");
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


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MQTTManager.release();
    }

    public BaseRepositoryComponent getmBaseRepositoryComponent() {
        return mBaseRepositoryComponent;
    }
}
