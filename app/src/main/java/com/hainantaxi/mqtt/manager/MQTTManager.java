package com.hainantaxi.mqtt.manager;

import android.util.Log;

import com.hainantaxi.Config;
import com.hainantaxi.MyApplication;
import com.hainantaxi.mqtt.modle.Connection;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by develop on 2017/5/18.
 */

public class MQTTManager {

    private static MQTTManager instance;

    private static final int Qos = 1;
    private MqttAndroidClient mClient;
    private String mClientId = "001";

    private ArrayList<String> subscribes = new ArrayList<>();
    private HashMap<String, PublishSubject<MqttMessage>> subSinal = new HashMap<>();
    private Connection connection = new Connection();

    public static MQTTManager getInstance() {
        if (instance == null) {
            instance = new MQTTManager();
        }
        return instance;
    }

    private MQTTManager() {
        subscribes.clear();
        start();
    }

    private void start() {
        MqttConnectOptions options = connection.createOptions();
        if (mClient == null) {
            MqttClientPersistence mqttClientPersistence = connection.getMqttClientPersistence();
            String serverURI = connection.getServerURI();
            mClientId = connection.getClicntId();


            mClient = new MqttAndroidClient(MyApplication.getContext(), serverURI, mClientId, mqttClientPersistence);
            mClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    start();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //TODO 测试
                    if (!topic.isEmpty() && topic.startsWith("region") && topic.endsWith("driver")) {
                        subSinal.get(Config.User_sub_topic).onNext(message);
                    }

                    PublishSubject<MqttMessage> subject = subSinal.get(topic);
                    if(subject!=null){
                        subject.onNext(message);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
        }

        if (mClient != null && !mClient.isConnected()) {
            connection(options);
        }
    }

    public void reset(String mClientId) {
        stop();
        this.mClientId = mClientId;
        subscribes.clear();
        start();
    }


    public void stop() {
        if (mClient != null) {
            disconnectClient();
            mClient = null;
        }
    }

    private void resubscribe() {
        for (String k : subSinal.keySet()) {
            if (!subscribes.contains(k)) {
                subscribe(k);
            }
        }
    }

    private void connection(MqttConnectOptions option) {
        if (mClient != null) {
            if (!mClient.isConnected()) {
                try {
                    mClient.connect(option, "Connect", new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            //TODO 链接成功操作
                            Log.w("location", "connect_onSuccess-->" + asyncActionToken.toString() + "");
                            resubscribe();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.w("location", "onFailure-->" + exception.getMessage());
                            start();
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                    start();
                }
            }
        } else {
            start();
        }
    }

    public void disconnectClient() {
        if (mClient == null) {
            return;
        }

        if (mClient.isConnected()) {
            try {
                mClient.disconnect();
                mClient = null;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public Observable<MqttMessage> subscribe(String topic) {

        if (mClient == null) {
            start();
        } else {
            if (mClient.isConnected()) {
                if (!subscribes.contains(topic)) {
                    try {
                        mClient.subscribe(topic, Qos, "Subscribe", new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                //TODO 订阅成功操作
                                if (!topic.isEmpty() && !subscribes.contains(topic)) {
                                    subscribes.add(topic);
                                }

                                Log.w("location", "subscribe-->" + asyncActionToken.toString() + "");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                                Log.w("location", "subscribe_onFailure-->" + asyncActionToken.toString() + "");

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                connection(connection.createOptions());
            }
        }

        PublishSubject<MqttMessage> old = subSinal.get(topic);
        if (old != null) {
            return old.asObservable();
        }
        PublishSubject<MqttMessage> subject = PublishSubject.create();
        subSinal.put(topic, subject);

        return subject.asObservable();
    }

    private void saveSubscribe(String topic) {
        if (!topic.isEmpty() && !subscribes.contains(topic)) {
            subscribes.add(topic);
        }
    }


    public void unSubscribe(String topic) {
        if (mClient != null && mClient.isConnected()) {
            try {
                mClient.unsubscribe(topic, "Unsubscribe", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                        if (!topic.isEmpty() && subSinal.get(topic) != null) {
                            subSinal.get(topic).onCompleted();
                            subSinal.remove(topic);
                            subscribes.remove(topic);
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void publish(String topic, MqttMessage mq) {
        if (mClient == null) {
            start();
        } else {
            if (mClient.isConnected()) {
                try {
                    mClient.publish(topic, mq, "Publish", new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            //TODO 成功发布消息
                            Log.w("location", "publish_onSuccess-->" + asyncActionToken.toString() + "");

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            Log.w("location", "publish_onFailure-->" + asyncActionToken.toString() + "");

                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                connection(connection.createOptions());
            }
        }
    }


    public static void release() {
        if (instance != null) {
            instance.disconnectClient();
            instance = null;
        }
    }

}

