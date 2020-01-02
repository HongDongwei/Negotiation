package com.example.negotiation.socket.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.negotiation.model.HeartSend;
import com.example.negotiation.socket.manager.SessionManager;

import java.util.Timer;
import java.util.TimerTask;


public class LongConnectService extends Service {

    private final String TAG = "LongConnectService";

    // 端口号，要求客户端与服务器端一致
    private static int PORT = 12006;
    private HeartSend heartSend;
    private Timer timer = new Timer();
    private TimerTask task;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        heartSend = new HeartSend();
        sendBeatData();
        super.onCreate();
    }


    //定时发送数据
    private void sendBeatData() {

        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        SessionManager.getInstance().writeBytesToServer(heartSend.getHeartSend());
                    } catch (Exception e) {
                        /*发送失败说明socket断开了或者出现了其他错误*/
                        Log.i(TAG, "run: 正在重连");
                        /*重连*/
                        e.printStackTrace();
                    }
                }
            };
        }
        timer.schedule(task, 0, 1000);
    }

    //关闭定时发送数据
    private void stopTimer() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
    }

}
