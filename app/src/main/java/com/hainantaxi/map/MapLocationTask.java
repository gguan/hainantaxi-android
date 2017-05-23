package com.hainantaxi.map;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hainantaxi.MyApplication;
import com.hainantaxi.R;
import com.hainantaxi.modle.entity.DeriverLocation;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.mqtt.modle.Topic;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by develop on 2017/5/23.
 */

public class MapLocationTask implements LocationSource, AMapLocationListener, AMap.OnMapLoadedListener {

    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;


    boolean isMoveToLocation = true;

    AMap mAmap;
    MapView mapView;
    private SmoothMoveMarker markerOption;

    public MapLocationTask(MapView mapView) {
        if (mapView == null) {
            return;
        }
        this.mapView = mapView;
        this.mAmap = mapView.getMap();
        markerOption = new SmoothMoveMarker(mAmap);
    }

    public void init() {

        mAmap.setLocationSource(this);
        mAmap.setMyLocationEnabled(true);
        mAmap.setRenderFps(60);
        UiSettings uiSettings = mAmap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);//定位按钮
        uiSettings.setScaleControlsEnabled(false);//比例尺
        uiSettings.setLogoPosition(AMapOptions.LOGO_MARGIN_LEFT);
        uiSettings.setCompassEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);//禁止旋转
        mAmap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        mAmap.setOnMapLoadedListener(this);

        subscribeMQTT();
    }


    public synchronized void startLocation() {
        isMoveToLocation = true;
        mLocationClient.startLocation();
    }

    //获取屏幕中间坐标
    public LatLng getScreenMiduleLocation(AMap aMap) {
        return aMap.getProjection().fromScreenLocation(new Point(mapView.getWidth() / 2, mapView.getHeight() / 2));
    }

    private void sendMessage(double longitude, double latitude) {
        String topic = Topic.createDriverLocation();
        MqttTopic.validate(topic, false);
        int qos = 0;
        String payLoad = longitude + "-" + latitude;
        mAmap.getMyLocation();
        boolean isRetained = false;

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payLoad.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(isRetained);
        MQTTManager.getInstance().publish(topic, mqttMessage);
    }

    public void subscribeMQTT() {
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(mAmap);
        HashMap<String, SmoothMoveMarker> map = new HashMap<>();
        Gson gson = new Gson();

        MQTTManager.getInstance().subscribe(Topic.createDriverLocation()).buffer(1, TimeUnit.SECONDS, 5)
                .subscribe(mqttMessageList -> {
                    for (MqttMessage mqttMessage : mqttMessageList) {
                        if (mqttMessage == null) return;
                        String mQttStr = mqttMessage.toString();
                        Log.e("m1tt", mQttStr);
                        Double longitude = 0d;
                        double latitude = 0d;
                        String id = "";
                        try {

                            DeriverLocation deriverLocation = gson.fromJson(mQttStr, DeriverLocation.class);
                            longitude = deriverLocation.getCoordinate().getLongtidute();
                            latitude = deriverLocation.getCoordinate().getLatidute();
                            id = deriverLocation.getId();

                        } catch (Exception ex) {
                            return;
                        }

                        if (!id.isEmpty()) {
                            LatLng drivePoint = new LatLng(latitude, longitude);
                            if (map.keySet().contains(id)) {
                                SmoothMoveMarker smoothMoveMarker = map.get(id);
                                //smoothMoveMarker.getPosition()
                                smoothMarkerMove(smoothMoveMarker, new ArrayList<LatLng>() {{
                                    add(drivePoint);
                                }});
                            } else {

                                map.put(id, smoothMarkerMove(new SmoothMoveMarker(mAmap), new ArrayList<LatLng>() {{
                                    add(drivePoint);
                                }}));
                            }
                        }
                    }
                });
    }

    private ArrayList<LatLng> buildList(LatLng from, LatLng to, int count) {
        double latStep = (to.latitude - from.latitude) / count;
        double lngStep = (to.longitude - from.longitude) / count;
        ArrayList<LatLng> latLngList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LatLng latLng = new LatLng(from.latitude + i * latStep, from.longitude + i * lngStep);
            latLngList.add(latLng);
        }
        return latLngList;
    }

    private SmoothMoveMarker smoothMarkerMove(SmoothMoveMarker smoothMoveMarker, ArrayList<LatLng> points) {
        // 获取轨迹坐标点

//        mAmap.animateCamera(CameraUpdateFactory.newLatLng(drivePoint));


        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < points.size(); i++) {
            b.include(points.get(i));
        }

        LatLngBounds bounds = b.build();
        // 设置滑动的图标
        LatLng drivePoint = points.get(0);
        smoothMoveMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.map_mine_location));

        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置滑动的轨迹左边点
        smoothMoveMarker.setPoints(subList);
        // 设置滑动的总时间
        smoothMoveMarker.setTotalDuration(1);
        // 开始滑动
        smoothMoveMarker.startSmoothMove();

        return smoothMoveMarker;
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(MyApplication.getContext());
            mLocationOption = new AMapLocationClientOption();

            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationOption.setWifiActiveScan(true);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setInterval(1);
            mLocationOption.setNeedAddress(true);
            mLocationOption.setLocationCacheEnable(false);

            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mLocationClient != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                String adCode = aMapLocation.getAdCode();
                String address = aMapLocation.getAddress();
                String aoiName = aMapLocation.getAoiName();
                String city = aMapLocation.getCity();
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                String cityCode = aMapLocation.getCityCode();
                String description = aMapLocation.getDescription();

                if (isMoveToLocation) {
                    // 改变地图中心点
                    CameraUpdate mCamerUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 17, 0, 0));
                    mAmap.animateCamera(mCamerUpdate);

//                    markerOption.position(new LatLng(latitude, longitude));
//                    markerOption.title("北京市").snippet("北京市：34.341568, 108.940174");
//
//                    markerOption.draggable(true);//设置Marker可拖动
//                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                            .decodeResource(MyApplication.getContext().getResources(), R.mipmap.map_mine_location)));
//                    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//                    markerOption.setFlat(true);//设置marker平贴地图效果
//                    mAmap.addMarker(markerOption);

                    // 获取轨迹坐标点
                    List<LatLng> points = new ArrayList<LatLng>();
                    LatLng drivePoint = new LatLng(latitude, longitude);
                    points.add(drivePoint);

                    // 设置滑动的图标
                    markerOption.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.map_mine_location));
                    markerOption.setPoints(points);
                    markerOption.setTotalDuration(30);
                    markerOption.startSmoothMove();

                    isMoveToLocation = false;
                }
                //
                Log.e("error", "adCode" + adCode + "\n address" + address + "\n aoiName" + aoiName + "\n city" + city + "\nlatitude" + latitude + "\nlongitude" + longitude + "\ncityCode" + cityCode + "\ndescription" + description);


            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onMapLoaded() {
        if (mapView == null) return;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(0, 0));
        markerOptions
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(mapView.getContext().getResources(),
                                R.mipmap.map_select_point)));
        Marker mPositionMark = mAmap.addMarker(markerOptions);

        mPositionMark.setPositionByPixels(mapView.getWidth() / 2,
                mapView.getHeight() / 2);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);

        mLocationClient.startLocation();
    }
}
