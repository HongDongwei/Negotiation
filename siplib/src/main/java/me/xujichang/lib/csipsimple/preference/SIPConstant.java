package me.xujichang.lib.csipsimple.preference;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;

import net.grandcentrix.tray.AppPreferences;

import org.webrtc.videoengine.VideoCaptureAndroid;

import java.net.PortUnreachableException;


/**
 * Created by sxy on 2019/5/14 10:19
 */
public class SIPConstant {
    //front与back
    public static final int FRONT = 1;
    public static final int BACK = 0;
    public static final String Prefs_Camera_face = "cmaera_face";
    public static final String CAMERA_ROTATION = "camera_rotation";
    public static AppPreferences sPreferences;
    public static String cameraFace = "back";

    public static void setCameraLocation(Context context, int index) {

        AppPreferences vAppPreferences = getPreference(context);
        vAppPreferences.put(Prefs_Camera_face, index);
        if (index == FRONT) {
            cameraFace = "front";
        } else {
            cameraFace = "back";
        }
//        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(Prefs_Camera_face, index).apply();
    }

    private static AppPreferences getPreference(Context context) {
        if (null == sPreferences) {
            sPreferences = new AppPreferences(context);
        }
        return sPreferences;
    }

    public static int getCameraLocation(Context pContext) {
        AppPreferences vAppPreferences = getPreference(pContext);
        return vAppPreferences.getInt(Prefs_Camera_face, FRONT);
//        return PreferenceManager.getDefaultSharedPreferences(pContext).getInt(Prefs_Camera_face, FRONT);
    }

    public static String getCameraLocationStr(Context pApplicationContext) {
        int face = getCameraLocation(pApplicationContext);

        if (face == FRONT) {
            return "前置摄像头";
        } else {
            return "后置摄像头";
        }
    }

    public static String getCameraFlag2Str(Context pContext) {
        return getCameraLocation(pContext) == FRONT ? "front" : "back";
    }
}
