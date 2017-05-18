package com.hainantaxi.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/**
 * Created by develop on 17-5-17.
 */
public class PoiSwipeToLoadLayout extends SwipeToLoadLayout {
    private static final String TAG = "PoiSwipeToLoadLayout";

    private boolean mIsScrollToTop = false;

    public PoiSwipeToLoadLayout(Context context) {
        super(context);
    }

    public PoiSwipeToLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PoiSwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actiov = MotionEventCompat.getActionMasked(ev);

        if (mIsScrollToTop) {
            switch (actiov) {
                case MotionEvent.ACTION_DOWN:
                    mIsScrollToTop = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    return onTouchEvent(ev);
                case MotionEvent.ACTION_UP:
                    mIsScrollToTop = false;
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public boolean isScrollToTop() {
        return mIsScrollToTop;
    }

    public void setScrollToTop(boolean scrollToTop) {
        mIsScrollToTop = scrollToTop;
    }


}
