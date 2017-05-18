package com.example.develop.base.net;

import com.example.develop.base.net.service.PreferenceService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by develop on 2017/5/17.
 */
@Module
public class BasePreferenceServiceModule {

    private final PreferenceService mPreferenceService;

    public BasePreferenceServiceModule(PreferenceService preferenceService) {
        mPreferenceService = preferenceService;
    }

    @Provides
    PreferenceService providePreferenceService() {
        return mPreferenceService;
    }
}
