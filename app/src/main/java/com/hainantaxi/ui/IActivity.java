package com.hainantaxi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by develop on 2017/5/24.
 */

public interface IActivity {


    public void setButterKnifeAfter(@Nullable Bundle savedInstanceState);

    public void setContentViewBefore();

    public void initView();

    public void initData();

    public void initBundleData();

    public int getResId();
}
