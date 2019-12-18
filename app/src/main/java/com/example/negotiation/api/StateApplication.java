package com.example.negotiation.api;

import android.app.Application;

import com.example.negotiation.model.LoginReciverd;

import org.apache.mina.core.session.IoSession;

public class StateApplication extends Application {
    public static final String TAG = "StateApplication";

    public static int LOGINSTATE = 0;
    public static IoSession session = null;
    public static LoginReciverd loginReciverd = null;
    public static String USER = "";
    public static String PAD = "";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
