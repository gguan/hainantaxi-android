package com.hainantaxi.ui.citysearchpoi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.help.Tip;
import com.hainantaxi.R;
import com.hainantaxi.map.LocationEcodeTask;
import com.hainantaxi.map.PoiSearchTask;
import com.hainantaxi.ui.BaseActivity;
import com.hainantaxi.ui.citysearchpoi.adapter.CitySearchAdapter;
import com.hainantaxi.ui.citysearchpoi.view.SearchTitle;
import com.hainantaxi.utils.HNLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by develop on 2017/5/24.
 */

public class CitySearchActivity extends BaseActivity {


    @BindView(R.id.st_title)
    SearchTitle mTitle;
    @BindView(R.id.rv_list)
    RecyclerView mRvInputTipsList;

    public static final String Key_Tips = "tips";


    private LocationEcodeTask mLocationEcodeTask;
    private PoiSearchTask poiSearchTask;
    private CitySearchAdapter mAdapter;


    @Override
    public int getResId() {
        return R.layout.activity_city_search;
    }

    @Override
    public void initView() {
        mTitle.setTitleRightClickListener(v -> {
            onBackPressed();
        });
        mTitle.setTitleLeftClickListener(v -> {

        });

        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchInputTips();
            }
        });

        mAdapter = new CitySearchAdapter();
        mRvInputTipsList.setAdapter(mAdapter);
        mRvInputTipsList.setLayoutManager(new LinearLayoutManager(this));


        mAdapter.setOnItemClickListener(this::onItemClickListener);


        View view = getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void onItemClickListener(Tip tip, int position) {
        Intent intent = new Intent();
        intent.putExtra(Key_Tips, tip);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    private void searchInputTips() {
        poiSearchTask.addSubscription(PoiSearchTask.Input_tips, poiSearchTask.loadImputtips(mTitle.getInput(), "").subscribe(tips -> {
            mAdapter.refreshData(tips);
        }));
    }

    @Override
    public void initData() {
        mLocationEcodeTask = new LocationEcodeTask();
        poiSearchTask = new PoiSearchTask();

        mLocationEcodeTask.addSubscription(LocationEcodeTask.GEOCODE, mLocationEcodeTask.loadGeoCodeResult("双井", "010").subscribe(geocodeResult -> {
            for (GeocodeAddress r : geocodeResult.getGeocodeAddressList()) {

            }
        }));
        searchInputTips();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    public void setContentViewBefore() {
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shap_white_bg));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationEcodeTask != null) {
            mLocationEcodeTask.onComplete();
        }

        if (poiSearchTask != null) {
            poiSearchTask.end();
        }
    }

    @Override
    public void initBundleData() {
    }

}
