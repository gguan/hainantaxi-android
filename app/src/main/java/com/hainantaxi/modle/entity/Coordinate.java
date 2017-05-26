package com.hainantaxi.modle.entity;

/**
 * Created by develop on 2017/5/23.
 */

public class Coordinate {
    private double latidute;
    private double longtidute;


    public Coordinate(double latidute, double longtidute) {
        this.latidute = latidute;
        this.longtidute = longtidute;
    }

    public Coordinate() {
    }

    public double getLatidute() {
        return latidute;
    }

    public void setLatidute(double latidute) {
        this.latidute = latidute;
    }

    public double getLongtidute() {
        return longtidute;
    }

    public void setLongtidute(double longtidute) {
        this.longtidute = longtidute;
    }
}
