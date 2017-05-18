package com.example.develop.base.ui;

/**
 * Created by developer on 17-5-17.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);

    void handleError(Throwable throwable);

}
