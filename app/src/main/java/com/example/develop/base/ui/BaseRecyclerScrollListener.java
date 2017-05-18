package com.example.develop.base.ui;

import android.support.v7.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by developer on 17-5-17.
 */
public class BaseRecyclerScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            Fresco.getImagePipeline().resume();
        } else {
            Fresco.getImagePipeline().pause();
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }
}
