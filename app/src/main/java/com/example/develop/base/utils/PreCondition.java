package com.example.develop.base.utils;

/**
 * Created by develop on 2017/5/17.
 */

public class PreCondition {
    public static <T> T checkNotNull(T reference){
        if (reference==null){
            throw new NullPointerException();
        }

        return reference;
    }
}
