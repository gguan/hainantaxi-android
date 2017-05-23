package com.hainantaxi;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.hainantaxi.map.MapLocationTask;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.ui.BaseActivity;
import com.hainantaxi.utils.PermissionUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mapView;

    private MapLocationTask mMapLocationTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onMapStart(savedInstanceState);

        PermissionUtils.verifyLocationPermissions(MainActivity.this);
        mMapLocationTask = new MapLocationTask(mapView);
        mMapLocationTask.init();

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
}
