package com.hainantaxi.ui.citysearchpoi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.hainantaxi.R;
import com.hainantaxi.map.LocationEcodeTask;
import com.hainantaxi.ui.BaseActivity;
import com.hainantaxi.ui.citysearchpoi.view.SearchTitle;
import com.hainantaxi.utils.HNLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by develop on 2017/5/24.
 */

public class CitySearchActivity extends BaseActivity {


    @BindView(R.id.st_title)
    SearchTitle mTitle;


    private LocationEcodeTask mLocationEcodeTask;

    @Override
    public void initView() {
        mTitle.setTitleRightClickListener(v -> {
        });
        mTitle.setTitleLeftClickListener(v -> {
            finish();
        });


    }

    @Override
    public void initData() {
        mLocationEcodeTask = new LocationEcodeTask();

        mLocationEcodeTask.addSubscription(LocationEcodeTask.GEOCODE, mLocationEcodeTask.loadGeoCodeResult("双井", "010").subscribe(geocodeResult -> {
            for (GeocodeAddress r : geocodeResult.getGeocodeAddressList()) {
                HNLog.i("mLocationEcodeTask", r.getFormatAddress());
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationEcodeTask != null) {
            mLocationEcodeTask.onComplete();
        }
    }

    @Override
    public void initBundleData() {
    }

    @Override
    public int getResId() {
        return R.layout.activity_city_search;
    }
}
