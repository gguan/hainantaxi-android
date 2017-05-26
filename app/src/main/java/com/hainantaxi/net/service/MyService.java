package com.hainantaxi.net.service;

import com.hainantaxi.modle.entity.Region;
import com.hainantaxi.net.HTTPResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by develop on 2017/5/17.
 */

public interface MyService {


    @GET("region")
    Observable<HTTPResponse<Region>> fetchRegion(@Query("lat") double lat, @Query("lng") double lng, @Query("zoomDepth") int zoomDepth);
}
