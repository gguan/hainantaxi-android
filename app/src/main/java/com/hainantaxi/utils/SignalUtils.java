package com.hainantaxi.utils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import rx.subjects.PublishSubject;

/**
 * Created by develop on 2017/5/19.
 */

public class SignalUtils {
    public static final Integer MQTT_MESSAGE_ARRIVED = 1;

    public static final PublishSubject<Integer> message = PublishSubject.create();

    public static final PublishSubject<MqttMessage> mMQTTmessage = PublishSubject.create();
}
