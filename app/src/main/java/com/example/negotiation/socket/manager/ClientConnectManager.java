package com.example.negotiation.socket.manager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.csipsimple.utils.PreferencesWrapper;
import com.example.negotiation.base.Const;
import com.example.negotiation.model.HeartSend;
import com.example.negotiation.model.LoginSend;
import com.example.negotiation.socket.client.ByteCodecFactory;
import com.example.negotiation.socket.client.HeartBeatHandler;
import com.example.negotiation.socket.client.HeartBeatListener;
import com.example.negotiation.socket.client.HeartBeatMessageFactory;
import com.example.negotiation.utils.ConnectUtils;
import com.example.negotiation.utils.HexUtils;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.negotiation.base.VTAState.A_VTA_LOGIN_REQ;

import static com.csipsimple.api.SipConfigManager.TURN_SERVER;
import static com.example.negotiation.utils.ConnectUtils.HOST;

public class ClientConnectManager {
    public static final String TAG = "ClientConnectManager";
    private static ClientConnectManager instance;
    private Handler handler;
    private Timer timer = new Timer();
    private TimerTask task;
    private byte[] loginbytes;
    private PreferencesWrapper preferencesWrapper;
    private int count;

    public static ClientConnectManager getInstance() {
        if (null == instance) {
            instance = new ClientConnectManager();
        }
        return instance;
    }

    private ClientConnectManager() {


    }

    private Context context;

    public void init(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        preferencesWrapper = new PreferencesWrapper(context);
    }

    public void init(Context context) {
        this.context = context;
    }

    public void connect(final Context context) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                //HOST = parseHostGetIPAddress(ConnectUtils.DOMAIN);
                //preferencesWrapper.setPreferenceStringValue(TURN_SERVER, HOST + ":3478");
                Log.i(TAG, "ConnectUtils.HOST in connect: " + ConnectUtils.HOST);
                NioSocketConnector mSocketConnector = new NioSocketConnector();
                //设置协议封装解析处理
                mSocketConnector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new ByteCodecFactory()));
                //设置心跳包
                KeepAliveFilter heartFilter = new KeepAliveFilter(new HeartBeatMessageFactory());
                //每 5 分钟发送一个心跳包
                heartFilter.setRequestInterval(5 * 60);
                //心跳包超时时间 10s
                heartFilter.setRequestTimeout(10);
                // 获取过滤器链
                DefaultIoFilterChainBuilder filterChain = mSocketConnector.getFilterChain();
                filterChain.addLast("encoder", new ProtocolCodecFilter(new ByteCodecFactory()));
                // 添加编码过滤器 处理乱码、编码问题
                filterChain.addLast("decoder", new ProtocolCodecFilter(new ByteCodecFactory()));
                mSocketConnector.getFilterChain().addLast("heartbeat", heartFilter);
                //设置 handler 处理业务逻辑
                mSocketConnector.setHandler(new HeartBeatHandler(context));
                mSocketConnector.addListener(new HeartBeatListener(mSocketConnector));
                //配置服务器地址
                InetSocketAddress mSocketAddress = new InetSocketAddress(HOST, ConnectUtils.PORT);
                //发起连接
                ConnectFuture mFuture = mSocketConnector.connect(mSocketAddress);
                mFuture.awaitUninterruptibly();
                IoSession mSession = mFuture.getSession();
                Log.d("", "======连接成功" + mSession.toString());
                e.onNext(mSession);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull Object o) {
                IoSession mSession = (IoSession) o;
                Log.i(TAG, "连接:" + mSession.isConnected());
                SessionManager.getInstance().setSeesion(mSession);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
//                Log.i(TAG, "onComplete: " + SharedPreferenceUtils.getUserName(context));
//                if (!"".equals(SharedPreferenceUtils.getUserName(context))) {
//                    initLogin(SharedPreferenceUtils.getUserName(context));
//                    SessionManager.getInstance().writeBytesToServer(loginbytes);
//                    Log.i(TAG, "onGotRefreshToken: 已经登陆AMS");
//                }
            }
        });
    }


    /**
     * 解析域名获取IP数组
     *
     * @param host
     * @return
     */
    public String parseHostGetIPAddress(String host) {
        try {
            InetAddress[] x = InetAddress.getAllByName(host);
            return x[0].getHostAddress();
            //得到字符串形式的ip地址
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.d("putoutmsg", "域名解析出错");
        }
        Toast.makeText(context, "域名解析出错", Toast.LENGTH_LONG).show();
        return "60.191.75.54";
    }


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

    public void ToastErr(String msg) {
        Log.i(TAG, "MESSAGE_LOGIN_FAILED: " + msg);
        handler.obtainMessage(Const.MsgType.MESSAGE_LOGIN_FAILED, msg).sendToTarget();
    }

    public void ShowDialog(String msg) {
        Log.i(TAG, "SIP_STATUS: " + msg);
        handler.obtainMessage(Const.MsgType.SIP_STATUS, msg).sendToTarget();
    }

    public void loginSucess(String msg) {
        Log.i(TAG, "LOGIN_SUCCESS: " + msg);
        handler.obtainMessage(Const.MsgType.LOGIN_SUCCESS, msg).sendToTarget();
    }
    public void rePeatConnect() {
        final boolean[] isRepeat = {false};
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                // 执行到这里表示Session会话关闭了，需要进行重连,我们设置每隔3s重连一次,如果尝试重连5次都没成功的话,就认为服务器端出现问题,不再进行重连操作
                count = 0;// 记录尝试重连的次数
                while (!isRepeat[0] && count < 100) {
                    try {
                        //HOST = parseHostGetIPAddress(ConnectUtils.DOMAIN);
                        //preferencesWrapper.setPreferenceStringValue(TURN_SERVER, HOST + ":3478");
                        Log.i(TAG, "ConnectUtils.HOST in rePeatConnect: " + ConnectUtils.HOST);
                        count++;
                        NioSocketConnector mSocketConnector = new NioSocketConnector();
                        //设置协议封装解析处理
                        mSocketConnector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new ByteCodecFactory()));
                        //设置心跳包
                        KeepAliveFilter heartFilter = new KeepAliveFilter(new HeartBeatMessageFactory());
                        //每 5 分钟发送一个心跳包
                        heartFilter.setRequestInterval(5 * 60);
                        //心跳包超时时间 10s
                        heartFilter.setRequestTimeout(10);
                        // 获取过滤器链
                        DefaultIoFilterChainBuilder filterChain = mSocketConnector.getFilterChain();
                        filterChain.addLast("encoder", new ProtocolCodecFilter(new ByteCodecFactory()));
                        // 添加编码过滤器 处理乱码、编码问题
                        filterChain.addLast("decoder", new ProtocolCodecFilter(new ByteCodecFactory()));
                        mSocketConnector.getFilterChain().addLast("heartbeat", heartFilter);
                        //设置 handler 处理业务逻辑
                        mSocketConnector.setHandler(new HeartBeatHandler(context));
                        mSocketConnector.addListener(new HeartBeatListener(mSocketConnector));
                        //配置服务器地址
                        InetSocketAddress mSocketAddress = new InetSocketAddress(HOST, ConnectUtils.PORT);
                        //发起连接
                        ConnectFuture mFuture = mSocketConnector.connect(mSocketAddress);
                        mFuture.awaitUninterruptibly();
                        IoSession mSession = mFuture.getSession();
                        if (mSession.isConnected()) {
                            isRepeat[0] = true;
                            Log.i(TAG, "======连接成功" + mSession.toString());
                            e.onNext(mSession);
                            e.onComplete();
                            break;
                        }
                    } catch (Exception e1) {
                        if (count == ConnectUtils.REPEAT_TIME) {
                            System.out.println(ConnectUtils.stringNowTime() + " : 断线重连"
                                    + ConnectUtils.REPEAT_TIME + "次之后仍然未成功,结束重连.....");
                            break;
                        } else {
                            System.out.println(ConnectUtils.stringNowTime() + " : 本次断线重连失败,3s后进行第" + (count + 1) + "次重连.....");
                            try {
                                Thread.sleep(3000);
                                System.out.println(ConnectUtils.stringNowTime() + " : 开始第" + (count + 1) + "次重连.....");
                            } catch (InterruptedException e12) {
                                Log.e("rePeatConnect ", "rePeatConnect e12" + e12);
                            }
                        }
                    }

                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull Object o) {
                IoSession mSession = (IoSession) o;
                Log.i(TAG, "连接(RE)：" + mSession.isConnected());
                SessionManager.getInstance().setSeesion(mSession);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                ClientConnectManager.getInstance().rePeatConnect();
                Log.i(TAG, "onError(RE): " + e.getMessage());
            }

            @Override
            public void onComplete() {
//                Log.i(TAG, "onComplete(RE): " + SharedPreferenceUtils.getUserName(context));
//                if (!"".equals(SharedPreferenceUtils.getUserName(context))) {
//                    initLogin(SharedPreferenceUtils.getUserName(context));
//                    SessionManager.getInstance().writeBytesToServer(loginbytes);
//                    Log.i(TAG, "onGotRefreshToken: 已经登陆AMS，账号为" + SharedPreferenceUtils.getUserName(context));
//                }
            }
        });
    }

    private void initLogin(String username) {
        byte[] version = {0x01};
        int command = 1;
        byte[] text1 = {0x00, 0x00, 0x00, 0x00};
        byte[] text2 = {0x00, 0x00, (byte) 0xdc, 0x20};
        byte[] text3 = {0x00, 0x00, (byte) 0xdd, (byte) 0x80};
        byte[] text4 = HexUtils.IntToByteBig(A_VTA_LOGIN_REQ);
        LoginSend loginSend = new LoginSend(version, command, text1, text2, text3, text4, username, "12345678","123");
        loginbytes = loginSend.getByte();
    }
}
