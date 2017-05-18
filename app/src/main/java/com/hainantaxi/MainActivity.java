package com.hainantaxi;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.hainantaxi.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(mapView==null){
            mapView= (MapView) findViewById(R.id.map);
        }
        mapView.onCreate(savedInstanceState);
        AMap aMap = mapView.getMap();
    }
}
