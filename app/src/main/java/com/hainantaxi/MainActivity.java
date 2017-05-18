package com.hainantaxi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.hainantaxi.mqtt.manager.MQTTManager;
import com.hainantaxi.mqtt.modle.Topic;
import com.hainantaxi.ui.BaseActivity;
import com.hainantaxi.utils.PermissionUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.tv_location)
    TextView mtvText;

    AMapLocationClient mapLocationClient = null;
    AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.w("location", aMapLocation != null ? aMapLocation.toString() : "定位失败");
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mapView == null) {
            mapView = (MapView) findViewById(R.id.map);
        }
        mapView.onCreate(savedInstanceState);
        AMap aMap = mapView.getMap();
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();//初始化AMapLocationClientOption对象
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);

        mapLocationClient.setLocationListener(mapLocationListener);
        mtvText.setOnClickListener(v -> {
            PermissionUtils.verifyLocationPermissions(MainActivity.this);
            mapLocationClient.startLocation();

            //test
//            MQTTManager.getInstance(getApplicationContext()).subscribe(Topic.createLocation(), 0);
            sendMessage();
        });

    }

    private void sendMessage() {
        String topic = Topic.createLocation();
        MqttTopic.validate(topic, false);
        int qos = 0;
        String payLoad = "Number" + Math.random() * 10;
        boolean isRetained = false;

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payLoad.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(isRetained);
        MQTTManager.getInstance(getApplicationContext()).publish(topic, mqttMessage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_LOCATION) {
            mapLocationClient.startLocation();
        }
    }
}
