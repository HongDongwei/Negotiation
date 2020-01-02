package com.example.negotiation.socket.client;


import android.util.Log;

import com.example.negotiation.socket.manager.ClientConnectManager;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class HeartBeatListener implements IoServiceListener {
    private final String TAG = "HeartBeatListener";
    public NioSocketConnector connector;

    public HeartBeatListener(NioSocketConnector connector) {
        this.connector = connector;
    }

    @Override
    public void serviceActivated(IoService arg0) throws Exception {
    }

    @Override
    public void serviceDeactivated(IoService arg0) throws Exception {
    }

    @Override
    public void serviceIdle(IoService arg0, IdleStatus arg1) throws Exception {
    }

    @Override
    public void sessionClosed(IoSession arg0) throws Exception {
        Log.d("", "hahahaha");
    }

    @Override
    public void sessionCreated(IoSession arg0) throws Exception {
    }

    @Override
    public void sessionDestroyed(IoSession arg0) {
        Log.i(TAG, "断线了");
        ClientConnectManager.getInstance().rePeatConnect();
    }
}
