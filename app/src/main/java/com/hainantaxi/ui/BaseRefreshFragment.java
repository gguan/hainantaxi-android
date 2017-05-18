package com.hainantaxi.ui;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/**
 * Created by develop on 2017/5/17.
 */

public abstract class BaseRefreshFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @Override
    public abstract void onLoadMore();

    @Override
    public abstract void onRefresh();

    public void dismissRefresh(SwipeToLoadLayout swipeToLoadLayout) {
        if (swipeToLoadLayout != null && swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.post(() -> swipeToLoadLayout.setRefreshing(false));
        }
        resumeLoadImage();
    }

    public void dismissLoadMore(SwipeToLoadLayout swipeToLoadLayout) {
        if (swipeToLoadLayout != null && swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.post(() -> swipeToLoadLayout.setLoadingMore(false));
        }
        resumeLoadImage();
    }

    public void showRefresh(SwipeToLoadLayout swipeToLoadLayout) {
        if (swipeToLoadLayout != null && !swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.post(() -> swipeToLoadLayout.setRefreshing(true));
        }
    }

    public void showLoadMore(SwipeToLoadLayout swipeToLoadLayout) {
        if (swipeToLoadLayout != null && !swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.post(() -> swipeToLoadLayout.setLoadingMore(true));
        }
    }

}

