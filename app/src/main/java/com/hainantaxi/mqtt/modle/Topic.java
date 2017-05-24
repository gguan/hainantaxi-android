package com.hainantaxi.mqtt.modle;

import com.hainantaxi.Config;

/**
 * Created by develop on 2017/5/18.
 */
public class Topic {


    //TODO 处理其他操作

    public static String createDriverLocation() {
        return Config.User_sub_topic;
    }

    public static String createDriverLocation(String params) {
        return Config.Driver_sub_topic + params;
    }
}
