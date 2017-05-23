package com.hainantaxi.mqtt.modle;

/**
 * Created by develop on 2017/5/18.
 */
public class Topic {


    public static String createDriverLocation() {
        return "region/test/driver";
    }

    public static String createDriverLocation(String params) {
        return "drivers/location/" + params;
    }
}
