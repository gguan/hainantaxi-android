package com.hainantaxi.utils;

import com.hainantaxi.net.HTTPResponse;
import com.hainantaxi.net.Remote;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by develop on 17-5-16.
 */
public class RxUtil {
    static final int SUCCESS_CODE = 1;

    @Remote
    public static <T> Subscription subscribeRemoteable(Observable<T> observable, Observer<T> observer) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Remote
    public static <T> Observable<T> transferRemoteObservable(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Remote
    public static <T> Subscription requestSubscribeRemoteable(Observable<HTTPResponse<T>> observable, Observer<T> observer) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(thttpRespone -> {
                    if (thttpRespone == null) {
                        return Observable.error(new Throwable("No respone"));
                    }
                    String msg = thttpRespone.getMessage();
                    if (thttpRespone.getCode() != SUCCESS_CODE) {
                        if (msg == null) {
                            return Observable.error(new Throwable("Error Code"));
                        } else {
                            return Observable.error(new Throwable(msg));
                        }
                    }
                    if (thttpRespone.getData() != null) {
                        return Observable.just(thttpRespone.getData());
                    } else {
                        return Observable.error(new Throwable("Data is nill"));
                    }
                })
                .subscribe(observer);
    }


    @Remote
    public static <T> Observable<T> netRequest(Observable<HTTPResponse<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(thttpRespone -> {
                    if (thttpRespone == null) {
                        return Observable.error(new Throwable("No respone"));
                    }
                    String msg = thttpRespone.getMessage();
                    if (thttpRespone.getCode() != SUCCESS_CODE) {
                        if (msg == null) {
                            return Observable.error(new Throwable("Error Code"));
                        } else {
                            return Observable.error(new Throwable(msg));
                        }
                    }
                    if (thttpRespone.getData() != null) {
                        return Observable.just(thttpRespone.getData());
                    } else {
                        return Observable.error(new Throwable("Data is nill"));
                    }
                });

    }

}
