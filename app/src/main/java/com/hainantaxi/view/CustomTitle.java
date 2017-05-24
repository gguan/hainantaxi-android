package com.hainantaxi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.hainantaxi.R;
import com.hainantaxi.utils.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by develop on 2017/5/17.
 */

public class CustomTitle extends FrameLayout {

    private final int DEFAULT_RESID = 0;
    private final float DEFAULT_TEXT_SIZE = 18;
    private final int DEFAULT_TEXT_COLOR = Color.parseColor("#000000");
    private final int defaultWidth = DeviceUtil.dip2px(getContext(), 20);

    private final int TEXT = 1;
    private final int IMAGE = 2;
    private final int LAYOUT = 3;

    private int leftType = TEXT;
    private int centerType = TEXT;
    private int rightType = TEXT;

    private String leftText = "";
    private String centerText = "";
    private String rightText = "";

    private float leftTextSize = DEFAULT_TEXT_SIZE;
    private float centerTextSize = DEFAULT_TEXT_SIZE;
    private float rightTextSize = DEFAULT_TEXT_SIZE;

    private int leftTextColor = DEFAULT_TEXT_COLOR;
    private int centerTextColor = DEFAULT_TEXT_COLOR;
    private int rightTextColor = DEFAULT_TEXT_COLOR;

    private int leftResId = DEFAULT_RESID;
    private int rightResId = DEFAULT_RESID;
    private int centerResId = DEFAULT_RESID;

    @BindView(R.id.fl_center)
    FrameLayout mFlCenter;
    @BindView(R.id.fl_left)
    FrameLayout mFlLeft;
    @BindView(R.id.fl_right)
    FrameLayout mFlRight;
    private FrameLayout.LayoutParams mImageDefaultParams;

    public CustomTitle(@NonNull Context context) {
        this(context, null);
    }

    public CustomTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitle(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_title, this);
        ButterKnife.bind(this, view);
        mImageDefaultParams = new FrameLayout.LayoutParams(defaultWidth, defaultWidth, Gravity.CENTER);
        mImageDefaultParams.gravity = Gravity.CENTER;


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTitle);

        leftType = a.getInt(R.styleable.CustomTitle_title_left_type, TEXT);
        centerType = a.getInt(R.styleable.CustomTitle_title_center_type, TEXT);
        rightType = a.getInt(R.styleable.CustomTitle_title_right_type, TEXT);

        leftText = a.getString(R.styleable.CustomTitle_title_left_text);
        centerText = a.getString(R.styleable.CustomTitle_title_center_text);
        rightText = a.getString(R.styleable.CustomTitle_title_right_text);

        leftTextSize = a.getDimension(R.styleable.CustomTitle_title_left_text_size, DEFAULT_TEXT_SIZE);
        centerTextSize = a.getDimension(R.styleable.CustomTitle_title_center_text_size, DEFAULT_TEXT_SIZE);
        rightTextSize = a.getDimension(R.styleable.CustomTitle_title_right_text_size, DEFAULT_TEXT_SIZE);

        leftTextColor = a.getColor(R.styleable.CustomTitle_title_left_text_color, DEFAULT_TEXT_COLOR);
        centerTextColor = a.getColor(R.styleable.CustomTitle_title_center_text_color, DEFAULT_TEXT_COLOR);
        rightTextColor = a.getColor(R.styleable.CustomTitle_title_right_text_color, DEFAULT_TEXT_COLOR);

        leftResId = a.getResourceId(R.styleable.CustomTitle_title_left_image_src, DEFAULT_RESID);
        rightResId = a.getResourceId(R.styleable.CustomTitle_title_right_image_src, DEFAULT_RESID);
        centerResId = a.getResourceId(R.styleable.CustomTitle_title_center_image_src, DEFAULT_RESID);


        a.recycle();

        initLeft();
        initCenter();
        initRight();

        initEventListener();
    }

    private void initEventListener() {
        mFlLeft.setOnClickListener(v -> {
            if (mOnLeftOnclicListener != null) {
                mOnLeftOnclicListener.leftOnclic();
            }
        });

        mFlRight.setOnClickListener(v -> {
            if (mOnRightOnclicListener != null) {
                mOnRightOnclicListener.leftOnclic();
            }
        });

    }

    private void initRight() {
        switch (rightType) {
            case TEXT:
                TextView textView = createTextView();
                textView.setText(rightText);
                textView.setTextSize(rightTextSize);
                textView.setTextColor(rightTextColor);
                textView.setGravity(Gravity.CENTER);
                mFlRight.removeAllViews();
                mFlRight.addView(textView);
                break;
            case IMAGE:
                ImageView imageView = createImageView();
                imageView.setImageResource(rightResId);
                imageView.setLayoutParams(mImageDefaultParams);
                mFlRight.removeAllViews();
                mFlRight.addView(imageView);
                break;
            case LAYOUT:

                break;
        }
    }

    private void initCenter() {
        switch (centerType) {
            case TEXT:
                TextView textView = createTextView();
                textView.setText(centerText);
                textView.setTextColor(centerTextColor);
                textView.setTextSize(centerTextSize);
                textView.setGravity(Gravity.CENTER);
                mFlCenter.removeAllViews();
                mFlCenter.addView(textView);
                break;

            case IMAGE:
                ImageView imageView = createImageView();
                imageView.setImageResource(centerResId);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(params);
                mFlCenter.removeAllViews();
                mFlCenter.addView(imageView);
                break;
            case LAYOUT:

                break;
        }
    }

    public CustomTitle addLeftView(View view) {

        return this;
    }

    private void initLeft() {
        switch (leftType) {
            case TEXT:
                TextView textView = createTextView();
                textView.setText(leftText);
                textView.setTextSize(leftTextSize);
                textView.setTextColor(leftTextColor);
                textView.setGravity(Gravity.CENTER);
                mFlLeft.removeAllViews();
                mFlLeft.addView(textView);
                break;
            case IMAGE:
                ImageView imageView = createImageView();
                imageView.setLayoutParams(mImageDefaultParams);
                imageView.setImageResource(leftResId);
                mFlLeft.removeAllViews();
                mFlLeft.addView(imageView);
                break;
            case LAYOUT:

                break;
        }
    }

    private TextView createTextView() {
        TextView textView = (TextView) inflate(getContext(), R.layout.layout_custom_title_text, null);
        return textView;
    }

    private ImageView createImageView() {
        ImageView imageView = (ImageView) inflate(getContext(), R.layout.layout_custom_title_image, null);
        return imageView;
    }


    public void setOnLeftOnclicListener(OnLeftOnclicListener mOnLeftOnclicListener) {
        this.mOnLeftOnclicListener = mOnLeftOnclicListener;
    }

    public void setOnRightOnclicListener(OnRightOnclicListener mOnRightOnclicListener) {
        this.mOnRightOnclicListener = mOnRightOnclicListener;
    }

    private OnRightOnclicListener mOnRightOnclicListener;
    private OnLeftOnclicListener mOnLeftOnclicListener;

    public interface OnLeftOnclicListener {
        void leftOnclic();
    }

    public interface OnRightOnclicListener {
        void leftOnclic();
    }
}
