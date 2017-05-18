package com.hainantaxi.net;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by develop on 2017/5/17.
 */

@Singleton
@Component(modules = {BaseRepositoryModule.class, BasePreferenceServiceModule.class, ApplicationModule.class})
public interface BaseRepositoryComponent {

    BaseRepository getBaseRepository();
}
