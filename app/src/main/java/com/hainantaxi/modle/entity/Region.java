package com.hainantaxi.modle.entity;

import java.util.List;

/**
 * Created by develop on 2017/5/26.
 */

public class Region {
    private String regionId;
    private double distance;
    private List<String> subRegions;


    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<String> getSubRegions() {
        return subRegions;
    }

    public void setSubRegions(List<String> subRegions) {
        this.subRegions = subRegions;
    }
}
