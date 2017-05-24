package com.hainantaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.hainantaxi.map.LocationEcodeTask;
import com.hainantaxi.map.MapLocationTask;
import com.hainantaxi.modle.entity.Coordinate;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.ui.BaseActivity;
import com.hainantaxi.ui.citysearchpoi.CitySearchActivity;
import com.hainantaxi.utils.PermissionUtils;
import com.hainantaxi.view.CustomTitle;


import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends BaseActivity implements AMap.OnCameraChangeListener {

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.tv_from)
    TextView mTvFrom;
    @BindView(R.id.tv_to)
    TextView mTvTo;
    @BindView(R.id.title)
    CustomTitle mTitle;


    private MapLocationTask mMapLocationTask;//地图显示相关功能
    private LocationEcodeTask mLocationEcodeTask;//地图编码 关键字提示功能

    private final int Request_Code_City_Search = 100;

    @Override
    public void setButterKnifeAfter(@Nullable Bundle savedInstanceState) {
        onMapStart(savedInstanceState);
        PermissionUtils.verifyLocationPermissions(MainActivity.this);
    }


    @Override
    public void initView() {
        mMapLocationTask = new MapLocationTask(mapView);//处理地图定位
        mMapLocationTask.setOnCameraChangeListener(this);

        mTvTo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CitySearchActivity.class);
            startActivityForResult(intent, Request_Code_City_Search);
            overridePendingTransition(R.anim.slide_in_bottom, 0);
        });

        mMapLocationTask.getCoordinateVariable().asObservable().distinctUntilChanged(new Func2<Coordinate, Coordinate, Boolean>() {
            @Override
            public Boolean call(Coordinate coordinate, Coordinate coordinate2) {
                return null;
            }
        }).flatMap(new Func1<Coordinate, Observable<Object>>() {
            @Override
            public Observable<Object> call(Coordinate coordinate) {
                return null;
            }
        }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                
            }
        });
    }

    @Override
    public void initData() {
        mLocationEcodeTask = new LocationEcodeTask();//处理数据搜索  地址编码
    }

    @Override
    public void initBundleData() {

    }

    @Override
    public int getResId() {
        return R.layout.activity_main;
    }

    private void onMapStart(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }


    @OnClick(R.id.rl_switch_to_location)
    void switchToLocation() {
        mMapLocationTask.startLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_LOCATION) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        MQTTManager.getInstance().disconnectClient();
        if (mLocationEcodeTask != null) {
            mLocationEcodeTask.onComplete();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;
        mMapLocationTask.setFromLatLng(target);
        if (target == null) return;
        mLocationEcodeTask.addSubscription(LocationEcodeTask.GEOCODE,
                mLocationEcodeTask.loadRegeocodeResult(new LatLonPoint(target.latitude, target.longitude))
                        .subscribe(regeocode -> {
                            if (regeocode == null) return;
                            mTvFrom.setText(regeocode.getAddress());
                        }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Code_City_Search && resultCode == RESULT_OK) {
            if (data != null) {
                Tip tip = data.getParcelableExtra(CitySearchActivity.Key_Tips);

                if (tip != null) {
                    LatLonPoint point = tip.getPoint();
                    if (point != null) {
                        mMapLocationTask.setToLatLng(new LatLng(point.getLatitude(), point.getLongitude()));
                    }
                    mLocationEcodeTask.addSubscription(LocationEcodeTask.GEOCODE, mLocationEcodeTask.loadGeoCodeResult(tip.getName(), tip.getAdcode()).subscribe(result -> {
                        if (result == null) return;
                        mTvTo.setText(result.getGeocodeQuery().getLocationName());
                    }));
                    mMapLocationTask.zoom();
                    Observable<Double> observable = Observable.create(new Observable.OnSubscribe<Double>() {
                        @Override
                        public void call(Subscriber<? super Double> subscriber) {
                            //计算距离

                        }
                    });

                }
            }
        }
    }
}
