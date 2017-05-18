package com.example.develop.base;

import android.os.Bundle;

import com.example.develop.base.ui.BaseActivity;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
