package com.hainantaxi.modle.entity;

import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

/**
 * Created by develop on 2017/5/19.
 */

public class Connection {
    private String serverURI = "tcp://45.63.126.236:1883";
    private String clicntId = "Android-1" + UUID.randomUUID().toString();
    private MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
    private int timeout = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
    private int keepAlive = MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT;
    private boolean cleanSession = false;
    private String username = "司机";
    private String password = "123456";
    private String lwtTopic = "";
    private String lwtPayload = "";
    private int lwtQos = 0;
    private boolean lwtRetained = false;

    public String getServerURI() {
        return serverURI;
    }

    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
    }

    public String getClicntId() {
        return clicntId;
    }

    public void setClicntId(String clicntId) {
        this.clicntId = clicntId;
    }

    public MqttClientPersistence getMqttClientPersistence() {
        return mqttClientPersistence;
    }

    public void setMqttClientPersistence(MqttClientPersistence mqttClientPersistence) {
        this.mqttClientPersistence = mqttClientPersistence;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLwtTopic() {
        return lwtTopic;
    }

    public void setLwtTopic(String lwtTopic) {
        this.lwtTopic = lwtTopic;
    }

    public String getLwtPayload() {
        return lwtPayload;
    }

    public void setLwtPayload(String lwtPayload) {
        this.lwtPayload = lwtPayload;
    }

    public int getLwtQos() {
        return lwtQos;
    }

    public void setLwtQos(int lwtQos) {
        this.lwtQos = lwtQos;
    }

    public boolean isLwtRetained() {
        return lwtRetained;
    }

    public void setLwtRetained(boolean lwtRetained) {
        this.lwtRetained = lwtRetained;
    }

    public MqttClientPersistence createMqttClientPersitstence() {
        return new MemoryPersistence();
    }

    public MqttConnectOptions createOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
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
}
