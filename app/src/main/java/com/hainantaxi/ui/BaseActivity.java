package com.hainantaxi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by develop on 2017/5/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewBefore();
        setContentView(getResId());
        ButterKnife.bind(this);
        setButterKnifeAfter(savedInstanceState);
        initBundleData();
        initData();
        initView();
    }

    
    @Override
    public void setContentViewBefore() {
    }

    @Override
    public void setButterKnifeAfter(@Nullable Bundle savedInstanceState) {
    }


}
