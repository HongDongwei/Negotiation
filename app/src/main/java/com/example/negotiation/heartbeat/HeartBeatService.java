package com.example.negotiation.heartbeat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.negotiation.api.StateApplication;
import com.example.negotiation.filter.ByteCodecFactory;
import com.example.negotiation.model.HeartSend;
import com.example.negotiation.model.LoginSend;
import com.example.negotiation.utils.ConnectUtils;
import com.example.negotiation.utils.HexUtils;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.negotiation.api.VTAState.A_VTA_LOGIN_REQ;

public class HeartBeatService extends Service {

    private static final String TAG = "HeartBeatService";
    private NioSocketConnector connector = null;
    private HeartBeatHandler handler = new HeartBeatHandler();//创建handler对象，用于业务逻辑处理
    private HeartBeatThread thread = new HeartBeatThread();
    private IoSession session = null;
    private SocketBinder sockerBinder = new SocketBinder();
    private Timer timer = new Timer();
    private TimerTask task;
    private ConnectFuture future = null;
    private byte[] loginbytes;
    private boolean SIPLOGINSTATE = false;
    private int cout = 0;

    public void setMessageCallback(IMessageCallback messageCallback) {
        handler.setCallback(messageCallback);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind: ");
        if (!thread.isInterrupted()) {
            thread.start();
        }
        return sockerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        stopTimer();
        return super.onUnbind(intent);
    }

    public class SocketBinder extends Binder {

        /*返回SocketService 在需要的地方可以通过ServiceConnection获取到SocketService  */
        public HeartBeatService getService() {
            return HeartBeatService.this;
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "onStart: ");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        //开启单独的线程，因为Service是位于主线程的，为了避免主线程被阻塞
        if (!thread.isInterrupted()) {
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopTimer();
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    class HeartBeatThread extends Thread {
        @Override
        public void run() {
            initClientMina(ConnectUtils.HOST, ConnectUtils.PORT);
        }
    }


    /**
     * 初始化客户端MINA
     *
     * @param host
     * @param port
     */
    public void initClientMina(String host, int port) {

        try {
            connector = new NioSocketConnector();
            connector.setHandler(handler);
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ByteCodecFactory()));//添加Filter对象
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.toString());
        }
        connector.setConnectTimeout(ConnectUtils.TIMEOUT);//设置连接超时时间
        int count = 0;//记录连接次数
        while (true) {
            try {
                count++;
                //执行到这里表示客户端刚刚启动需要连接服务器,第一次连接服务器的话是没有尝试次数限制的，但是随后的断线重连就有次数限制了
                future = connector.connect(new InetSocketAddress(host, port));
                future.awaitUninterruptibly();//一直阻塞,直到连接建立

                session = future.getSession();//获取Session对象
                if (session.isConnected()) {
                    initLogin();
                    StateApplication.session = session;
                    session.write(IoBuffer.wrap(loginbytes));
                    Log.i(TAG, "账号已发出 " + StateApplication.USER + StateApplication.PAD);
                    sendBeatData();
                    break;
                }
            } catch (RuntimeIoException e) {
                System.out.println(ConnectUtils.stringNowTime() + " : 第" + count + "次客户端连接服务器失败，因为" + ConnectUtils.TIMEOUT + "s没有连接成功");
                try {
                    Thread.sleep(2000);//如果本次连接服务器失败，则间隔2s后进行重连操作
                    System.out.println(ConnectUtils.stringNowTime() + " : 开始第" + (count + 1) + "次连接服务器");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        //为MINA客户端添加监听器，当Session会话关闭的时候，进行自动重连
        connector.addListener(new HeartBeatListener(connector));
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
                        HeartSend heartSend = new HeartSend();
                        session.write(IoBuffer.wrap(heartSend.getHeartSend()));
//                        SipProfileState pjSipService = PjSipService.getProfileState(SipProfile.getProfileFromDbId(getBaseContext(), 1, DBProvider.ACCOUNT_FULL_PROJECTION));
////                        Log.i(TAG, String.valueOf(pjSipService.active));
//                        SipProfileState state = handler.onGotState();
//                        if (null != state) {
//                            Log.i(TAG, String.valueOf(state.isActive()));
//                        }

                    } catch (Exception e) {
                        /*发送失败说明socket断开了或者出现了其他错误*/
                        System.out.println("连接断开，正在重连");
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

    private void initLogin() {
        byte[] version = {0x01};
        int command = 1;
        byte[] text1 = {0x00, 0x00, 0x00, 0x00};
        byte[] text2 = {0x00, 0x00, (byte) 0xdc, 0x20};
        byte[] text3 = {0x00, 0x00, (byte) 0xdd, (byte) 0x80};
        byte[] text4 = HexUtils.IntToByteBig(A_VTA_LOGIN_REQ);
        LoginSend loginSend = new LoginSend(version, command, text1, text2, text3, text4, StateApplication.USER, StateApplication.PAD);
        loginbytes = loginSend.getByte();
    }
}
