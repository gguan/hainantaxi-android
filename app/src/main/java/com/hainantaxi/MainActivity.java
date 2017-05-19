package com.hainantaxi;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.mqtt.modle.Topic;
import com.hainantaxi.ui.BaseActivity;
import com.hainantaxi.utils.PermissionUtils;
import com.hainantaxi.utils.SignalUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.util.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.util.ActionSubscriber;

public class MainActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.tv_location)
    TextView mtvText;
    @BindView(R.id.tv_text)
    EditText mTvText;
    @BindView(R.id.tv_send)
    TextView mTvSend;

    AMapLocationClient mapLocationClient = null;
    AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.w("location", aMapLocation != null ? aMapLocation.toString() : "定位失败");
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMap aMap;
    private LatLng l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mapView == null) {
            mapView = (MapView) findViewById(R.id.map);
        }
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();//初始化AMapLocationClientOption对象
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);

        mapLocationClient.setLocationListener(mapLocationListener);
        mtvText.setOnClickListener(v -> {
            PermissionUtils.verifyLocationPermissions(MainActivity.this);
            mapLocationClient.startLocation();


        });
        mTvSend.setOnClickListener(v -> {
        });

//        l = new LatLng(39.610649, 116.714882);
//        // 获取轨迹坐标点
//        List<LatLng> points = new ArrayList<LatLng>();
//        for (int i = 0; i < 100; i++) {
//            points.add(new LatLng(39.610649 + i * 0.1, 116.714882 + i * 0.1));
//        }
//        LatLng drivePoint = points.get(0);
//        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
//        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
//
//        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
//        // 设置滑动的图标
//        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.map_mine_location));
//
//        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
//        points.set(pair.first, drivePoint);
//        List<LatLng> subList = points.subList(pair.first, points.size());
//
//        // 设置滑动的轨迹左边点
//        smoothMarker.setPoints(subList);
//        // 设置滑动的总时间
//        smoothMarker.setTotalDuration(40);
//        // 开始滑动
//        smoothMarker.startSmoothMove();
//        l = drivePoint;

        MQTTManager.getInstance().subscribe(Topic.createLocation()).subscribe(mqttMessage -> {
            String mQttStr = mqttMessage.toString();
            String[] strings = mQttStr.split("-");
            Log.e("m1tt", mQttStr);
            Double longitude = Double.parseDouble(strings[0]);
            double latitude = Double.parseDouble(strings[1]);

            // 获取轨迹坐标点
            List<LatLng> points = new ArrayList<LatLng>();
            LatLng drivePoint = new LatLng(latitude, longitude);
            points.add(l);
            points.add(drivePoint);
            LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

            SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
            // 设置滑动的图标
            smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.map_mine_location));

            Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
            points.set(pair.first, drivePoint);
            List<LatLng> subList = points.subList(pair.first, points.size());

            // 设置滑动的轨迹左边点
            smoothMarker.setPoints(subList);
            // 设置滑动的总时间
            smoothMarker.setTotalDuration(1);
            // 开始滑动
            smoothMarker.startSmoothMove();
            l = drivePoint;
        });
        aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
//                LatLng position = marker.getPosition();
//                double longitude = position.longitude;
//                double latitude = position.latitude;
//
//                sendMessage(longitude, latitude);
            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.setFlat(true);
                markerOptions.anchor(0.5f, 0.5f);
                markerOptions.position(new LatLng(0, 0));
                markerOptions
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),
                                        R.mipmap.map_mine_location)));
                Marker mPositionMark = aMap.addMarker(markerOptions);

                mPositionMark.setPositionByPixels(mapView.getWidth() / 2,
                        mapView.getHeight() / 2);
                AMapLocationClientOption option = new AMapLocationClientOption();
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                option.setOnceLocation(true);
                mapLocationClient.setLocationOption(option);

                mapLocationClient.startLocation();


            }
        });

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

            }
        });


        aMap.setAMapGestureListener(new AMapGestureListener() {
            @Override
            public void onDoubleTap(float v, float v1) {

            }

            @Override
            public void onSingleTap(float v, float v1) {

            }

            @Override
            public void onFling(float v, float v1) {

            }

            @Override
            public void onScroll(float v, float v1) {
//                LatLng latLng1 = aMap.getProjection().fromScreenLocation(new Point(mapView.getWidth() / 2, mapView.getHeight() / 2));
//                sendMessage(latLng1.longitude, latLng1.latitude);
            }

            @Override
            public void onLongPress(float v, float v1) {

            }

            @Override
            public void onDown(float v, float v1) {

            }

            @Override
            public void onUp(float v, float v1) {

            }

            @Override
            public void onMapStable() {

            }
        });

    }

    private void sendMessage(double longitude, double latitude) {
        String topic = Topic.createLocation();
        MqttTopic.validate(topic, false);
        int qos = 0;
        String payLoad = longitude + "-" + latitude;
        aMap.getMyLocation();
        boolean isRetained = false;

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payLoad.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(isRetained);
        MQTTManager.getInstance().publish(topic, mqttMessage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_LOCATION) {
            mapLocationClient.startLocation();
        }
    }
}
