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
import com.hainantaxi.map.LocationEcodeTask;
import com.hainantaxi.map.MapLocationTask;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.ui.BaseActivity;
import com.hainantaxi.ui.citysearchpoi.CitySearchActivity;
import com.hainantaxi.utils.PermissionUtils;
import com.hainantaxi.view.CustomTitle;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AMap.OnCameraChangeListener, AMap.OnMapClickListener {

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.tv_from)
    TextView mTvFrom;
    @BindView(R.id.tv_to)
    TextView mTvTo;
    @BindView(R.id.title)
    CustomTitle mTitle;

    private MapLocationTask mMapLocationTask;
    private LocationEcodeTask mLocationEcodeTask;

    @Override
    public void setButterKnifeAfter(@Nullable Bundle savedInstanceState) {
        onMapStart(savedInstanceState);
        PermissionUtils.verifyLocationPermissions(MainActivity.this);
    }


    @Override
    public void initView() {
        mMapLocationTask = new MapLocationTask(mapView);//处理地图定位
        mMapLocationTask.setOnCameraChangeListener(this);
        mMapLocationTask.setOnMapClickListener(this);

        mTvTo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CitySearchActivity.class);
            startActivity(intent);
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
        if (target == null) return;
        mLocationEcodeTask.addSubscription(LocationEcodeTask.GEOCODE,
                mLocationEcodeTask.loadRegeocodeResult(new LatLonPoint(target.latitude, target.longitude))
                        .subscribe(regeocode -> {
                            if (regeocode == null) return;
                            mTvFrom.setText(regeocode.getAddress());
                        }));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (latLng == null) return;
        mLocationEcodeTask.addSubscription(LocationEcodeTask.RE_GEOCODE,
                mLocationEcodeTask.loadRegeocodeResult(new LatLonPoint(latLng.latitude, latLng.longitude))
                        .subscribe(regeocode -> {

                            if (regeocode == null) return;

                            mTvTo.setText(regeocode.getAddress());
                        }));
    }


}
