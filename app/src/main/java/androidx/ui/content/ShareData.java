package androidx.ui.content;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

/**
 * 分享数据
 */
public class ShareData {

    private static final String SHARE_PREFERENCE_NAME = "_SP_DATA";
    private static final String TAG = ShareData.class.getSimpleName();

    /**
     * 数据存储构造函数
     */
    private ShareData() {

    }

    /**
     * 获取数据保存对象
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            return null;
        }
        String PACKAGE_NAME = context.getApplicationContext().getPackageName().replace(".", "_").toUpperCase();
        String name = PACKAGE_NAME +"_"+ SHARE_PREFERENCE_NAME;
        Log.i(TAG, "->name = " + name);
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 保存字符串
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 保存int
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 保存long
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 保存boolean
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 保存float
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 保存 Set<String>
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String key, Set<String> value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * 获取字符串
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        return getSharedPreferences(context).getString(key, defValue);
    }

    /**
     * 获取int
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    /**
     * 获取long
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        return getSharedPreferences(context).getLong(key, defValue);
    }

    /**
     * 获取boolean
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    /**
     * 获取Set
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return
     */
    public static Set<String> getStringSet(Context context, String key, Set defValue) {
        return getSharedPreferences(context).getStringSet(key, defValue);
    }

}
