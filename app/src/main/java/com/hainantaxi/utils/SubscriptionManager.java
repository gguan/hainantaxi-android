package com.hainantaxi.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by develop on 2017/5/24.
 */

public abstract class SubscriptionManager {

    private ComplexSubscription mComplexSubscription;

    public void end() {
        if (mComplexSubscription != null) {
            mComplexSubscription.unsubscribe();
        }
    }

    public ComplexSubscription getCompositeSubscription() {
        if (this.mComplexSubscription == null) {
            this.mComplexSubscription = new ComplexSubscription();
        }

        return this.mComplexSubscription;
    }


    public void addSubscription(String key, Subscription s) {
        if (this.mComplexSubscription == null) {
            this.mComplexSubscription = new ComplexSubscription();
        }

        this.mComplexSubscription.add(key, s);
    }

    public void removeSubscription(String key){
        if(this.mComplexSubscription==null){
            this.mComplexSubscription = new ComplexSubscription();
        }
        this.mComplexSubscription.unsubscribe(key);
    }
}
