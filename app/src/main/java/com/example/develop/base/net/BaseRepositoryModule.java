package com.example.develop.base.net;

import android.content.Context;

import com.example.develop.base.net.local.BaseLocalDataSource;
import com.example.develop.base.net.remote.BaseRemoteDataSource;
import com.example.develop.base.net.service.PreferenceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by develop on 2017/5/17.
 */

@Module
public class BaseRepositoryModule {

    @Singleton
    @Provides
    @Remote
    BaseRemoteDataSource provideBaseRemoteDataSource(Context context, PreferenceService preferenceService) {
        return new BaseRemoteDataSource(context, preferenceService);
    }

    @Singleton
    @Provides
    @Local
    BaseLocalDataSource provideBaseLocalDataSource(Context context, PreferenceService preferenceService) {
        return new BaseLocalDataSource(context, preferenceService);
    }
}
