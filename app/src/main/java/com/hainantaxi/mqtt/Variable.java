package com.hainantaxi.mqtt;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by develop on 2017/5/19.
 */

public class Variable<T> {

    private BehaviorSubject<T> subject = BehaviorSubject.create();

    private T value;

    public Variable(T value) {
        this.value = value;
    }

    public synchronized T getValue() {
        return value;
    }

    public synchronized void setValue(T value) {
        this.value = value;
        subject.onNext(value);
    }

    public Observable<T> asObservable() {
        return subject;
    }

    public void destory() {
        subject.onCompleted();
    }
}
