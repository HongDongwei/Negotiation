package com.example.negotiation.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.csipsimple.service.SipService;
import com.example.negotiation.model.LoginReciverd;
import com.example.negotiation.socket.manager.ClientConnectManager;
import com.example.negotiation.socket.service.LongConnectService;
import com.example.negotiation.ui.UserActivity;

import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;

public class APP extends Application {
    public static final String TAG = "APP";

    public static int LOGINSTATE = 0;
    public static IoSession session = null;
    public static LoginReciverd loginReciverd = null;
    public static String USER = "";
    public static String PAD = "";
    public static String userName;
    public static String userPwd;
    public static String userConfCode;
    public static byte[] tellerId;
    private static Context context;
    public static Map<String, targetInfo> targetInfoList = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ClientConnectManager.getInstance().connect(this);
        startSipService();
        doStartService();
        targetInfoList.clear();
    }
    /**
     * 绑定服务
     */
    void doStartService() {
        Intent serviceIntent = new Intent(context, LongConnectService.class);
        startService(serviceIntent);
    }

    // Service monitoring stuff
    private void startSipService() {
        Thread t = new Thread("StartSip") {
            @Override
            public void run() {
                Intent serviceIntent = new Intent(context, SipService.class);
                serviceIntent.putExtra(com.csipsimple.api.SipManager.EXTRA_OUTGOING_ACTIVITY, new ComponentName(context, UserActivity.class));
                startService(serviceIntent);
            }
        };
        t.start();
    }
}
