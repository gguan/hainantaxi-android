package com.hainantaxi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by develop on 2017/5/24.
 */

public class ComplexSubscription implements Subscription {

    private HashMap<String, Subscription> subscriptions;
    private volatile boolean unsubscribed;


    @Override
    public void unsubscribe() {
        if (!unsubscribed) {
            HashMap unsubscribe;
            synchronized (this) {
                if (unsubscribed) {
                    return;
                }
                unsubscribed = true;
                unsubscribe = subscriptions;
                subscriptions = null;
            }
            // we will only get here once
            unsubscribeFromAll(unsubscribe);
        }
    }

    @Override
    public boolean isUnsubscribed() {
        return unsubscribed;
    }


    public void add(String key, final Subscription s) {
        if (s == null) return;
        if (s.isUnsubscribed()) {
            return;
        }
        if (!unsubscribed) {
            synchronized (this) {
                if (!unsubscribed) {
                    if (subscriptions == null) {
                        subscriptions = new HashMap<>();
                    }
                    if (subscriptions.containsKey(key)) {
                        Subscription subscription = subscriptions.get(key);
                        subscription.unsubscribe();
                        subscriptions.remove(key);
                    }
                    subscriptions.put(key, s);
                    return;
                }
            }
        }
        // call after leaving the synchronized block so we're not holding a lock while executing this
        s.unsubscribe();
    }


    /**
     * Removes a {@link Subscription} from this {@code ComplexSubscription}, and unsubscribes the
     * {@link Subscription}.
     */
    public void unsubscribe(final String key) {
        if (!unsubscribed) {
            synchronized (this) {
                if (unsubscribed || subscriptions == null) {
                    return;
                }
                Subscription remove = subscriptions.remove(key);
                if (remove != null && !remove.isUnsubscribed()) {
                    remove.unsubscribe();
                }
            }
        }
    }


    private static void unsubscribeFromAll(HashMap<String, Subscription> subscriptions) {
        if (subscriptions == null) {
            return;
        }
        List<Throwable> es = null;
        for (Subscription s : subscriptions.values()) {
            try {
                s.unsubscribe();
            } catch (Throwable e) {
                if (es == null) {
                    es = new ArrayList<Throwable>();
                }
                es.add(e);
            }
        }
        Exceptions.throwIfAny(es);
    }

    /**
     * Returns true if this composite is not unsubscribed and contains subscriptions.
     *
     * @return {@code true} if this composite is not unsubscribed and contains subscriptions.
     * @since 1.0.7
     */
    public boolean hasSubscriptions() {
        if (!unsubscribed) {
            synchronized (this) {
                return !unsubscribed && subscriptions != null && !subscriptions.isEmpty();
            }
        }
        return false;
    }
}
