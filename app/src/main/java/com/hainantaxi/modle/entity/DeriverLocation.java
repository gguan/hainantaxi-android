package com.hainantaxi.modle.entity;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by develop on 2017/5/23.
 */

public class DeriverLocation {
    //{"id":"bot-10-0-2298-Grandfather's Profile","timestamp":1495526048517,"position":[39.859009,116.446823]}
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
            coordinate.setLatidute(position.get(0));
            coordinate.setLongtidute(position.get(1));
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
                    position.set(0, coordinate.getLatidute());
                    position.set(1, coordinate.getLongtidute());
                }
            }
        }
        this.coordinate = coordinate;
    }
}
