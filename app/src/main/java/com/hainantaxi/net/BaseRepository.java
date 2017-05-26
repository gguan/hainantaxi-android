package com.hainantaxi.net;

import com.hainantaxi.modle.entity.Region;
import com.hainantaxi.net.local.BaseLocalDataSource;
import com.hainantaxi.net.remote.BaseRemoteDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

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

    @Override
    public Observable<HTTPResponse<Region>> fetchRegion(double lat, double lng, int zoomDepth) {
        return mBaseRemoteDataSource.fetchRegion(lat, lng, zoomDepth);
    }
}
