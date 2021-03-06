package com.hainantaxi;

/**
 * Created by develop on 2017/5/17.
 */

public class Config {
    public static final String BASE_URL = "http://106.14.202.84:4400/v1/";
    public static final String USER_AGENT_HEAD = "Play/";
    public static final String APP_VERSION_HEAD = "Android/";
    public static final long NET_TIME_OUT = 1000;

    public static final String KEY_TOKEN_HEAD = "X-Auth-Token";
    public static final String KEY_AGENT = "User-Agent";
    public static final String KEY_PLAY_VERSION = "Play-Version";
    public static final String KEY_TIME_OUT = "Timestamp";


    public static final String User_sub_topic = "region/+/driver";
    public static final String Driver_sub_topic = "drivers/location/";
}
