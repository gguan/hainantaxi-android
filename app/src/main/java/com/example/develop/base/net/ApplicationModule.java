package com.example.develop.base.net;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by develop on 2017/5/17.
 */

@Module
public class ApplicationModule {
    private final Context mContext;

    public ApplicationModule(Context context) {
        this.mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
