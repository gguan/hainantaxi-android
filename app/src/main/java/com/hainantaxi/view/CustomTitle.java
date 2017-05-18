package com.hainantaxi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.hainantaxi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by develop on 2017/5/17.
 */

public class CustomTitle extends FrameLayout {

    private final int DEFAULT_RESID = 0;
    private final float DEFAULT_TEXT_SIZE = 18;
    private final int DEFAULT_TEXT_COLOR = Color.parseColor("#000000");

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

    @BindView(R.id.fl_center)
    FrameLayout mFlCenter;
    @BindView(R.id.fl_left)
    FrameLayout mFlLeft;
    @BindView(R.id.fl_right)
    FrameLayout mFlRight;

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

        if (mFlLeft == null) {
            mFlLeft = (FrameLayout) view.findViewById(R.id.fl_left);
        }

        if (mFlRight == null) {
            mFlRight = (FrameLayout) view.findViewById(R.id.fl_right);
        }

        if (mFlCenter == null) {
            mFlCenter = (FrameLayout) view.findViewById(R.id.fl_center);
        }

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
                mFlRight.removeAllViews();
                mFlRight.addView(textView);
                break;
            case IMAGE:
                ImageView imageView = createImageView();
                imageView.setImageResource(rightResId);
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
                mFlCenter.removeAllViews();
                mFlCenter.addView(textView);
                break;

            case LAYOUT:

                break;
        }
    }

    private void initLeft() {
        switch (leftType) {
            case TEXT:
                TextView textView = createTextView();
                textView.setText(leftText);
                textView.setTextSize(leftTextSize);
                textView.setTextColor(leftTextColor);
                mFlLeft.removeAllViews();
                mFlLeft.addView(textView);
                break;
            case IMAGE:
                ImageView imageView = createImageView();
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
