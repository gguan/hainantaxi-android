package com.hainantaxi.net;

import com.hainantaxi.modle.entity.Region;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by develop on 2017/5/17.
 */

public interface BaseDataSource {

    Observable<HTTPResponse<Region>> fetchRegion(double lat, double lng, int zoomDepth);
}
