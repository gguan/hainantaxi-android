package com.hainantaxi.modle.entity;

import android.util.Pair;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.hainantaxi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by develop on 2017/5/26.
 */

public class TaxiCar {

    private long timestamp;

    private double latitude;
    private double longitude;
    private SmoothMoveMarker marker;
    private String name;
    private String id;


    public TaxiCar(SmoothMoveMarker marker) {
        this.marker = marker;
        marker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.map_car_point));
    }

    public TaxiCar moveToPoint(ArrayList<LatLng> points) {
        if (points == null || points.size() <= 0) {
            return this;
        }

        marker.setVisible(true);
        LatLng drivePoint = points.get(0);

        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置滑动的轨迹左边点
        marker.setPoints(subList);
        // 设置滑动的总时间
        marker.setTotalDuration(1);
        // 开始滑动
        marker.startSmoothMove();
        return this;
    }


    public void stopMove() {
        marker.setVisible(false);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public SmoothMoveMarker getMarker() {
        return marker;
    }

    public void setMarker(SmoothMoveMarker marker) {
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
