package com.example.negotiation.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android Studio.
 * User: hongdw
 * Date: 2019/12/19
 * Time: 16:48
 */
public class SharedPreferencesUtils {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "user_msg";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        switch (key) {
            case Const.SharedPreferencesConst.USER_NAME:
                editor.putString(key, (String) object);
                break;
            case Const.SharedPreferencesConst.USER_PWD:
                editor.putString(key, (String) object);
                break;
            case Const.SharedPreferencesConst.USER_CONF:
                editor.putString(key, (String) object);
                break;
            case Const.SharedPreferencesConst.REGISTER:
                editor.putBoolean(key, (boolean) object);
                break;
            default:
                break;
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getParam(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        switch (key) {
            case Const.SharedPreferencesConst.USER_NAME:
                return sp.getString(key, "");
            case Const.SharedPreferencesConst.USER_PWD:
                return sp.getString(key, "");
            case Const.SharedPreferencesConst.USER_CONF:
                return sp.getString(key, "");
            case Const.SharedPreferencesConst.REGISTER:
                return sp.getBoolean(key, false);
            default:
                break;
        }

        return null;
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    /**
     * 清除指定数据
     *
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("定义的键名");
        editor.commit();
    }

}

