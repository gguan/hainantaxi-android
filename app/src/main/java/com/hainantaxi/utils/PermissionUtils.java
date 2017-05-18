package com.hainantaxi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by develop on 2017/3/3.
 */

public class PermissionUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_LOCATION = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static String[] CAERMA = {Manifest.permission.CAMERA};

    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void verifyStoragePermissionsCaerma(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    activity,
                    CAERMA,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void verifyLocationPermissions(Activity context) {
        int[] permission = new int[5];
        for (int i = 0, len = permission.length; i < len; i++) {
            permission[i] = ActivityCompat.checkSelfPermission(context, PERMISSIONS_LOCATION[i]);
            if (permission[i] != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{PERMISSIONS_LOCATION[i]}, REQUEST_LOCATION);
            }
        }
    }
}
