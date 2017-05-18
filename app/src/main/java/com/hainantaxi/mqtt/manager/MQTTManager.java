package com.hainantaxi.mqtt.manager;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

/**
 * Created by develop on 2017/5/18.
 */

public class MQTTManager {

    private static MQTTManager instance;
    private MqttAndroidClient mClient;
    private Context mContext;

    public static MQTTManager getInstance(Context context) {
        if (instance == null) {
            instance = new MQTTManager(context);
        }
        return instance;
    }

    private MQTTManager(Context context) {
        mContext = context;
        createClient();
    }

    private void createClient() {
        MqttConnectOptions option = createOption();
        if (mClient == null) {
            MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
            String serverURI = "tcp://45.63.126.236:1883";
            String clicntId = "Android-1"+ UUID.randomUUID().toString();


            mClient = new MqttAndroidClient(mContext, serverURI, clicntId, mqttClientPersistence);
            mClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.w("location", "__connectionLost-->" + cause.getMessage() + "");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //TODO 消息成功到达
                    Log.w("location", "messageArrived-->" + message.getPayload().toString() + "");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    try {
                        Log.w("location", "deliveryComplete-->" + token.getMessage());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (!mClient.isConnected()) {
            try {
                mClient.connect(option, "Connect", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //TODO 链接成功操作
                            Log.w("location", "connect_onSuccess-->" + asyncActionToken.toString() + "");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.w("location", "onFailure-->" + exception.getMessage());
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public void disconnectClient() {
        if (mClient != null && mClient.isConnected()) {
            try {
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public void subscribe(String topic, int qos) {
        if (mClient != null && mClient.isConnected()) {
            try {
                mClient.subscribe(topic, qos, "Subscribe", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //TODO 订阅成功操作

                            Log.w("location", "subscribe-->" +  asyncActionToken.toString() + "");

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
    }


    public void unSubscribe(String topic) {
        if (mClient != null && mClient.isConnected()) {
            try {
                mClient.unsubscribe(topic, "Unsubscribe", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //TODO 取消订阅成功操作
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
        if (mClient != null && mClient.isConnected()) {
            try {
                mClient.publish(topic, mq, "Publish", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //TODO 成功发布消息

                            Log.w("location", "publish_onSuccess-->" +  asyncActionToken.toString() + "");

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            Log.w("location", "publish_onFailure-->" +  asyncActionToken.toString()+ "");

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    private MqttConnectOptions createOption() {
        MqttConnectOptions options = new MqttConnectOptions();
        int timeout = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
        int keepAlive = MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT;
        boolean cleanSession = false;
        String username = "司机";
        String password = "123456";
        String lwtTopic = "";
        String lwtPayload = "";
        int lwtQos = 0;
        boolean lwtRetained = false;


        options.setCleanSession(cleanSession);
        options.setConnectionTimeout(timeout);
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(keepAlive);
        if (!username.isEmpty()) {
            options.setUserName(username);
        }
        if (!password.isEmpty()) {
            options.setPassword(password.toCharArray());
        }

        if (!lwtTopic.isEmpty() && !lwtPayload.isEmpty()) {
            options.setWill(lwtTopic, lwtPayload.getBytes(), lwtQos, lwtRetained);
        }
        return options;
    }


    public static void release() {
        if (instance != null) {
            instance.disconnectClient();
            instance = null;
        }
    }


}

