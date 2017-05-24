package com.hainantaxi.modle.entity;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by develop on 2017/5/23.
 */

public class DeriverLocation {

    private final int COUNT = 2;//数组数量
    private final int LAT = 1;//纬度位置
    private final int LNG = 0;// 经度位置

    private String id;
    private long timestamp;
    private ArrayList<Double> position;

    private Coordinate coordinate;

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

    public ArrayList<Double> getPosition() {
        return position;
    }

    public void setPosition(ArrayList<Double> position) {
        this.position = position;
    }

    public Coordinate getCoordinate() {
        if (position != null && position.size() == 2) {
            Coordinate coordinate = new Coordinate();
            coordinate.setLatidute(position.get(LAT));
            coordinate.setLongtidute(position.get(LNG));
            return coordinate;
        }
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        if (coordinate != null) {
            if (position == null) {
                position = new ArrayList<>();
                position.add(coordinate.getLatidute());
                position.add(coordinate.getLongtidute());
            } else {
                if (position.size() == 2) {
                    position.set(LAT, coordinate.getLatidute());
                    position.set(LNG, coordinate.getLongtidute());
                }
            }
        }
        this.coordinate = coordinate;
    }
}
