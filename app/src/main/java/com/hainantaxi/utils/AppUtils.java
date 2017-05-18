package com.hainantaxi.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @author feicien (ithcheng@gmail.com)
 * @since 2016-07-05 17:41
 */

public class AppUtils {

    private static final String version = "1.";


    public  static String getVersion(Context context) {
        String v =  getVersionName(context);
        if (v != null) {
            //PLLog.e("Version", v);
            return v;
        } else {
            return version;
        }
    }

    public  static String getUserAgent() {
        String os = Build.VERSION.RELEASE;
        String model = Build.MODEL;
        String brand = Build.BRAND;
        return "( com.playalot.Play ;" +
                "Android" + (os == null ? "" : os) + " ;" +
                (brand == null ? "" : brand) + " " + (model == null ? "" : model) + ");";
    }


    public static int getVersionCode(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return 0;
    }

    public static String getVersionName(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }

        return "";
    }
}
