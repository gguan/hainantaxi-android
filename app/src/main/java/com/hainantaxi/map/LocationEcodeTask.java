package com.hainantaxi.map;


import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hainantaxi.MyApplication;
import com.hainantaxi.modle.entity.Address;
import com.hainantaxi.utils.SubscriptionManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by develop on 2017/5/24.
 */

public class LocationEcodeTask extends SubscriptionManager implements GeocodeSearch.OnGeocodeSearchListener {

    public static String RE_GEOCODE = "re_geocode";
    public static String GEOCODE = "geocode";

    private final GeocodeSearch geocodeSearch;
    private Subscriber<Address> mAddressSubscriber;
    private Subscriber<GeocodeResult> mGeocodeResultSubscriber;


    private final Observable.OnSubscribe<GeocodeResult> mGeocodeResultOnSubscribe;
    private final Observable.OnSubscribe<Address> mAddressOnSubscribe;


    public LocationEcodeTask() {
        geocodeSearch = new GeocodeSearch(MyApplication.getContext());
        geocodeSearch.setOnGeocodeSearchListener(this);

        mGeocodeResultOnSubscribe = subscriber -> {
            mGeocodeResultSubscriber = (Subscriber<GeocodeResult>) subscriber;
        };

        mAddressOnSubscribe = subscriber -> {
            mAddressSubscriber = (Subscriber<Address>) subscriber;
        };
    }

    public Observable<GeocodeResult> loadGeoCodeResult(String cityName, String cityCode) {
        GeocodeQuery query = new GeocodeQuery(cityName, cityCode);
        geocodeSearch.getFromLocationNameAsyn(query);
        return Observable.create(mGeocodeResultOnSubscribe);
    }

    public Observable<Address> loadRegeocodeResult(LatLonPoint latLonPoint) {

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 1000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);

        return Observable.create(mAddressOnSubscribe);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        if (regeocodeResult != null) {
            mAddressSubscriber.onNext(new Address(regeocodeResult.getRegeocodeAddress()));
            mAddressSubscriber.onCompleted();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (geocodeResult != null) {
            mGeocodeResultSubscriber.onNext(geocodeResult);
            mGeocodeResultSubscriber.onCompleted();
        }
    }

    public void onComplete() {
        if (mAddressSubscriber != null && !mAddressSubscriber.isUnsubscribed()) {
            mAddressSubscriber.unsubscribe();
        }
        if (mGeocodeResultSubscriber != null && !mGeocodeResultSubscriber.isUnsubscribed()) {
            mGeocodeResultSubscriber.unsubscribe();
        }

        end();
    }

}
