package com.hainantaxi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyCharacterMap.UnavailableException;

import java.util.Map;
import java.util.Set;

/**
 * preference基础类
 * <p/>
 * 建议：
 * 1.如果存在同一程序的多个进程之间的数据贡献，在保存数据的时候的模式请传入Context.MODE_MULTI_PROCESS，
 * 否则数据传递不会及时更新（注意，这个是有代价的，存取效率会降低）
 *
 * @author ls
 */
public class BasePreference {

    /**
     * 保存数据：String
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveString(Context context, String preferenceName, String key, String value) {
        saveString(context, preferenceName, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据：String
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static void saveString(Context context, String preferenceName, String key, String value, int mode) {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取数据:String
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：null
     *
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static String getString(Context context, String preferenceName, String key) {
        return getString(context, preferenceName, key, null);
    }

    /**
     * 获取数据:String
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultStr
     */
    protected static String getString(Context context, String preferenceName, String key, String defaultStr) {
        return getString(context, preferenceName, key, defaultStr, Context.MODE_PRIVATE);
    }

    /**
     * 获取数据:String
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultStr
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static String getString(Context context, String preferenceName, String key, String defaultStr, int mode) {
        String result = null;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        result = preference.getString(key, defaultStr);

        return result;
    }

    /**
     * 保存数据：long
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveLong(Context context, String preferenceName, String key, long value) {
        saveLong(context, preferenceName, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据：long
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static void saveLong(Context context, String preferenceName, String key, long value, int mode) {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取数据:long
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     *
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static long getLong(Context context, String preferenceName, String key) {
        return getLong(context, preferenceName, key, 0);
    }

    /**
     * 获取数据:long
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defValue
     */
    protected static long getLong(Context context, String preferenceName, String key, long defValue) {
        return getLong(context, preferenceName, key, defValue, Context.MODE_PRIVATE);
    }

    /**
     * 获取数据:long
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defValue
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static long getLong(Context context, String preferenceName, String key, long defValue, int mode) {
        long result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        result = preference.getLong(key, defValue);

        return result;
    }

    /**
     * 保存数据：int
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveInt(Context context, String preferenceName, String key, int value) {
        saveInt(context, preferenceName, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据：int
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static void saveInt(Context context, String preferenceName, String key, int value, int mode) {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取数据:int
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     *
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static int getInt(Context context, String preferenceName, String key) {
        return getInt(context, preferenceName, key, 0);
    }

    /**
     * 获取数据:int
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static int getInt(Context context, String preferenceName, String key, int defValue) {
        return getInt(context, preferenceName, key, defValue, Context.MODE_PRIVATE);
    }

    /**
     * 获取数据:int
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static int getInt(Context context, String preferenceName, String key, int defValue, int mode) {
        int result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        result = preference.getInt(key, defValue);

        return result;
    }

    /**
     * 保存数据：boolean
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveBoolean(Context context, String preferenceName, String key, boolean value) {
        saveBoolean(context, preferenceName, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据：boolean
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static void saveBoolean(Context context, String preferenceName, String key, boolean value, int mode) {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取数据:boolean
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：false
     *
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static boolean getBoolean(Context context, String preferenceName, String key) {
        return getBoolean(context, preferenceName, key, false);
    }

    /**
     * 获取数据:boolean
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @return
     */
    protected static boolean getBoolean(Context context, String preferenceName, String key, boolean defaultValue) {
        return getBoolean(context, preferenceName, key, defaultValue, Context.MODE_PRIVATE);
    }

    /**
     * 获取数据:boolean
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @param mode           访问模式，见Context.MODE_XXXX
     * @return
     */
    protected static boolean getBoolean(Context context, String preferenceName, String key, boolean defaultValue, int mode) {
        boolean result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        result = preference.getBoolean(key, defaultValue);

        return result;
    }

    /**
     * 保存数据：float
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveFloat(Context context, String preferenceName, String key, float value) {
        saveFloat(context, preferenceName, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据：float
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static void saveFloat(Context context, String preferenceName, String key, float value, int mode) {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        SharedPreferences.Editor editor = preference.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 获取数据:float
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     *
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static float getFloat(Context context, String preferenceName, String key) {
        return getFloat(context, preferenceName, key, 0);
    }

    /**
     * 获取数据:float
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @return
     */
    protected static float getFloat(Context context, String preferenceName, String key, float defaultValue) {
        return getFloat(context, preferenceName, key, defaultValue, Context.MODE_PRIVATE);
    }

    /**
     * 获取数据:float
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @param mode           访问模式，见Context.MODE_XXXX
     * @return
     */
    protected static float getFloat(Context context, String preferenceName, String key, float defaultValue, int mode) {
        float result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        result = preference.getFloat(key, defaultValue);

        return result;
    }

    /**
     * 保存数据：StringSet
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveStringSet(Context context, String preferenceName, String key, Set<String> value) {
        saveStringSet(context, preferenceName, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据：StringSet
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     * @param mode           访问模式，见Context.MODE_XXXX
     */
    protected static void saveStringSet(Context context, String preferenceName, String key, Set<String> value, int mode) {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        SharedPreferences.Editor editor = preference.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    /**
     * 获取数据:StringSet
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     *
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static Set<String> getStringSet(Context context, String preferenceName, String key) {
        return getStringSet(context, preferenceName, key, null);
    }

    /**
     * 获取数据:StringSet
     * 默认模式: Context.MODE_PRIVATE
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @return
     */
    protected static Set<String> getStringSet(Context context, String preferenceName, String key, Set<String> defaultValue) {
        return getStringSet(context, preferenceName, key, defaultValue, Context.MODE_PRIVATE);
    }

    /**
     * 获取数据:StringSet
     *
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @param mode           访问模式，见Context.MODE_XXXX
     * @return
     */
    protected static Set<String> getStringSet(Context context, String preferenceName, String key, Set<String> defaultValue, int mode) {
        Set<String> result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        result = preference.getStringSet(key, defaultValue);

        return result;
    }

    /**
     * @param context
     * @param preferenceName
     * @param keyValue
     * @param mode
     */
    @SuppressWarnings("unchecked")
    protected static void saveMulValue(Context context, String preferenceName, Map<String, Object> keyValue, int mode) throws UnavailableException {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, mode);
        SharedPreferences.Editor editor = preference.edit();

        for (String key : keyValue.keySet()) {
            Object value = keyValue.get(key);

            if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Set<?>) {
                editor.putStringSet(key, (Set<String>) value);
            } else {
                throw new UnavailableException("unvalid param:keyValue");
            }
        }

        editor.commit();
    }

}
