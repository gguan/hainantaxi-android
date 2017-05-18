package com.example.develop.base.net;

import com.example.develop.base.net.local.BaseLocalDataSource;
import com.example.develop.base.net.remote.BaseRemoteDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by develop on 2017/5/17.
 */

@Singleton
public class BaseRepository implements BaseDataSource {

    private final BaseLocalDataSource mBaseLocalDataSource;
    private final BaseRemoteDataSource mBaseRemoteDataSource;


    @Inject
    public BaseRepository(@Local BaseLocalDataSource mBaseLocalDataSource, @Remote BaseRemoteDataSource mBaseRemoteDataSource) {
        this.mBaseLocalDataSource = mBaseLocalDataSource;
        this.mBaseRemoteDataSource = mBaseRemoteDataSource;
    }
}
