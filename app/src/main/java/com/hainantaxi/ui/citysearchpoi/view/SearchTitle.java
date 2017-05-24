package com.hainantaxi.ui.citysearchpoi.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hainantaxi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by develop on 2017/5/24.
 */

public class SearchTitle extends FrameLayout {


    @BindView(R.id.tv_city)
    TextView mTvCity;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_cancle)
    TextView mTvCancle;

    public SearchTitle(@NonNull Context context) {
        this(context, null);
    }

    public SearchTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchTitle(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_search_title, this);
        ButterKnife.bind(this, view);


    }


    public void setTitleLeftClickListener(View.OnClickListener leftClickListener) {
        if (mTvCity == null || leftClickListener == null) {
            return;
        }

        mTvCity.setOnClickListener(leftClickListener);
    }


    public void setTitleRightClickListener(View.OnClickListener titleRightClickListener) {
        if (mTvCancle == null || titleRightClickListener == null) {
            return;
        }

        mTvCancle.setOnClickListener(titleRightClickListener);
    }

    public String getInput() {
        if (mEtInput == null || mEtInput.getText().toString().isEmpty()) return "北京";
        return mEtInput.getText().toString().trim();
    }


    public void addTextChangedListener(TextWatcher watcher) {
        if (watcher == null || mEtInput == null) return;
        mEtInput.addTextChangedListener(watcher);
    }
}
