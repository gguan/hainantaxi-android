package com.hainantaxi.ui;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by develop on 2017/5/17.
 */

public abstract class BaseObserver implements BasePresenter {

    private CompositeSubscription mCompositeSubscription;

    @Override
    public abstract void start();

    @Override
    public void end() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }


    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }
}
