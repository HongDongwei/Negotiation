package com.example.negotiation.socket.manager;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class SessionManager {

    private static SessionManager instance;

    private IoSession mSession;

    public static boolean LOGINSTATE = false;
    private volatile static Object bytes = new Object();

    public static SessionManager getInstance() {
        if (null == instance) {
            instance = new SessionManager();
        }
        return instance;
    }

    private SessionManager() {
    }

    public void setSeesion(IoSession session) {
        this.mSession = session;
    }

    public void writeToServer(Object msg) {
        if (mSession != null) {
            mSession.write(msg);
        }
    }

    public void writeBytesToServer(byte[] msg) {
        if (mSession != null && mSession.isConnected()) {
            mSession.write(IoBuffer.wrap(msg));
        }
    }

    public void closeSession() {
        if (mSession != null) {
            mSession.closeOnFlush();
        }
    }

    public void removeSession() {
        this.mSession = null;
    }
}
