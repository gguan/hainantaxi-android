package com.hainantaxi.map;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.annotation.NonNull;
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
import com.hainantaxi.MyApplication;
import com.hainantaxi.R;
import com.hainantaxi.modle.entity.Coordinate;
import com.hainantaxi.modle.entity.DeriverLocation;
import com.hainantaxi.mqtt.Variable;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.mqtt.modle.Topic;
import com.hainantaxi.utils.HNLog;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by develop on 2017/5/23.
 */

public class MapLocationTask implements LocationSource, AMapLocationListener, AMap.OnMapLoadedListener {

    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;


    boolean isMoveToLocation = true;

    AMap mAmap;
    MapView mapView;
    private SmoothMoveMarker moveMarker;

    LatLng fromLatLng;
    LatLng toLatLng;
    private Marker startMark;
    private Marker endMark;


    private Variable<Coordinate> coordinateVariable;


    public MapLocationTask(MapView mapView) {
        if (mapView == null) {
            return;
        }
        this.mapView = mapView;
        this.mAmap = mapView.getMap();
        moveMarker = new SmoothMoveMarker(mAmap);

        init();
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

    /**
     * 获取屏幕中间坐标
     *
     * @param aMap
     * @return
     */
    public LatLng getScreenMiduleLocation(AMap aMap) {
        return aMap.getProjection().fromScreenLocation(new Point(mapView.getWidth() / 2, mapView.getHeight() / 2));
    }


    /**
     * 订阅MQTT
     */
    public void subscribeMQTT() {
        HashMap<String, SmoothMoveMarker> map = new HashMap<>();
        Gson gson = new Gson();

        MQTTManager.getInstance().subscribe(Topic.createDriverLocation()).buffer(3, TimeUnit.SECONDS, 5)
                .subscribe(mqttMessageList -> {
                    for (MqttMessage mqttMessage : mqttMessageList) {
                        if (mqttMessage == null) return;
                        String mQttStr = mqttMessage.toString();
                        HNLog.println(mQttStr);
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
                    // 获取轨迹坐标点
                    List<LatLng> points = new ArrayList<LatLng>();
                    LatLng drivePoint = new LatLng(latitude, longitude);
                    points.add(drivePoint);

                    // 设置滑动的图标
                    moveMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.map_mine_location));
                    moveMarker.setPoints(points);
                    moveMarker.setTotalDuration(30);
                    moveMarker.startSmoothMove();

                    isMoveToLocation = false;
                }

                changeLocationSinal(latitude, longitude);

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();

                HNLog.i("AmapErr", errText);
            }
        }
    }

    /**
     * 定位地址改变
     *
     * @param latitude
     * @param longitude
     */
    private void changeLocationSinal(double latitude, double longitude) {
        Coordinate coordinate = new Coordinate();
        coordinate.setLongtidute(longitude);
        coordinate.setLatidute(latitude);
        if (coordinateVariable == null) {
            coordinateVariable = new Variable<>(coordinate);
        }

        coordinateVariable.setValue(coordinate);
    }

    @Override
    public void onMapLoaded() {
        if (mapView == null) return;
        addInstructMark();//第一次加载添加标记
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);

        mLocationClient.startLocation();
    }


    /**
     * 添加屏幕中间位置Marker
     */
    private void addInstructMark() {

        Marker mPositionMark = addMark(new LatLng(0, 0), R.mipmap.map_select_point);
        mPositionMark.setPositionByPixels(mapView.getWidth() / 2,
                mapView.getHeight() / 2);
    }

    public void setOnCameraChangeListener(AMap.OnCameraChangeListener mOnCameraChangeListener) {
        if (mAmap == null || mOnCameraChangeListener == null) return;
        mAmap.setOnCameraChangeListener(mOnCameraChangeListener);
    }


    public void setOnMapClickListener(AMap.OnMapClickListener clickListener) {
        if (mAmap == null || clickListener == null) return;
        mAmap.setOnMapClickListener(clickListener);
    }


    /**
     * 缩放地图  显示fromLatlng toLatlog坐标
     */
    public void zoom() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(getLatLngBounds(fromLatLng, toLatLng), 900, 900, 50);
        mAmap.moveCamera(cameraUpdate);

        addStartMarkAndEndMark();
    }


    /**
     * 添加起始点和 重点marker
     */
    public void addStartMarkAndEndMark() {
        if (startMark != null) {
            startMark.remove();
        }
        if (endMark != null) {
            endMark.remove();
        }
        startMark = addMark(fromLatLng, R.mipmap.map_start_point);
        endMark = addMark(toLatLng, R.mipmap.map_end_point);
    }

    /**
     * 添加坐标和图片资源  在地图上锚点
     *
     * @param latLng
     * @param iconResId
     * @return
     */
    private Marker addMark(LatLng latLng, int iconResId) {
        MarkerOptions options = new MarkerOptions();
        options.setFlat(true);
        options.anchor(0.5f, 0.5f);
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(mapView.getContext().getResources(),
                        iconResId)));
        return mAmap.addMarker(options);
    }

    /**
     * 获得两点之间 矩形区域
     *
     * @param fromLatLng
     * @param toLatLng
     * @return
     */
    @NonNull
    private LatLngBounds getLatLngBounds(LatLng fromLatLng, LatLng toLatLng) {
        double fLat = fromLatLng.latitude;
        double fLng = fromLatLng.longitude;
        double tLat = toLatLng.latitude;
        double tLng = toLatLng.longitude;

        return new LatLngBounds(new LatLng(Math.min(fLat, tLat), Math.min(fLng, tLng)), new LatLng(Math.max(fLat, tLat), Math.max(fLng, tLng)));
    }


    public Variable<Coordinate> getCoordinateVariable() {
        return coordinateVariable;
    }

    public void setCoordinateVariable(Variable<Coordinate> coordinateVariable) {
        this.coordinateVariable = coordinateVariable;
    }

    public void setFromLatLng(LatLng fromLatLng) {
        this.fromLatLng = fromLatLng;
    }

    public void setToLatLng(LatLng toLatLng) {
        this.toLatLng = toLatLng;
    }

    public LatLng getFromLatLng() {
        return fromLatLng;
    }

    public LatLng getToLatLng() {
        return toLatLng;
    }

}
