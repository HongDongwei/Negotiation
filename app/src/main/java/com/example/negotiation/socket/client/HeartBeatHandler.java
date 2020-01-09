package com.example.negotiation.socket.client;

import android.content.Context;
import android.content.Intent;

import com.csipsimple.utils.Log;
import com.example.negotiation.SipManager;
import com.example.negotiation.base.APP;
import com.example.negotiation.base.Const;
import com.example.negotiation.base.SharedPreferencesUtils;
import com.example.negotiation.model.LoginReciverd;
import com.example.negotiation.model.QueryReciverd;
import com.example.negotiation.model.SipStatusSend;
import com.example.negotiation.socket.manager.ClientConnectManager;
import com.example.negotiation.socket.manager.SessionManager;
import com.example.negotiation.utils.ConnectUtils;
import com.example.negotiation.utils.HexUtils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.net.URLDecoder;

import static com.example.negotiation.base.VTAState.*;
//import static com.example.negotiation.base.VTAState.A_VTA_LOGIN_RSP;
//import static com.example.negotiation.base.VTAState.A_VTA_LOGOUT_RSP;
//import static com.example.negotiation.base.VTAState.LOGINRESULT;
//import static com.example.negotiation.base.VTAState.Sc_a_VTA_Call_Rsp;
//import static com.example.negotiation.base.VTAState.Sc_a_VTA_RecvMsg;
//import static com.example.negotiation.base.VTAState.Sc_a_VTA_SipStatus_Req;
//import static com.example.negotiation.base.VTAState.Sc_a_VTA_SipStatus_Rsp;

public class HeartBeatHandler extends IoHandlerAdapter {

    private final String TAG = "HeartBeatHandler";
    private SipManager sipManager;
    public static final String BROADCAST_ACTION = "com.commonlibrary.mina.broadcast";
    public static final String MESSAGE = "message";
    private Context mContext;
    private int i = 0;
    private byte[] sipStatusbytes;
    private boolean isCheck = false;
    public static byte[] socketFd = new byte[4];

    public HeartBeatHandler(Context context) {
        this.mContext = context;
        sipManager = new SipManager(context);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        Log.i(TAG, ConnectUtils.stringNowTime() + " : 客户端调用exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        Log.i(TAG, ConnectUtils.stringNowTime() + " : 客户端调用messageReceived：" + message.toString());
        String result = URLDecoder.decode(message.toString(), "UTF-8");
        byte[] data = HexUtils.hexStringToBytes(result);
        if (data != null) {
            byte[] species = new byte[4]; //消息种别码
            System.arraycopy(data, 19, species, 0, LOGINRESULT);
            if (HexUtils.byteEquals(species, HexUtils.IntToByteBig(A_VTA_LOGIN_RSP))) {
                byte[] byteloginResult = new byte[4];
                System.arraycopy(data, 27, byteloginResult, 0, LOGINRESULT);
                if (HexUtils.bytesToIntBig(byteloginResult) == 0) {
                    //登录成功
                    Log.i(TAG, "SIP注册了" + i++ + "次");
                    LoginReciverd loginParse = new LoginReciverd(data);
                    APP.tellerId = loginParse.getTellerId();
                    if (!(boolean) SharedPreferencesUtils.getParam(mContext, Const.SharedPreferencesConst.REGISTER)) {
                        android.util.Log.i(TAG, "messageReceived: " + "true");
                        SharedPreferencesUtils.setParam(mContext, Const.SharedPreferencesConst.REGISTER, true);
                        SharedPreferencesUtils.setParam(mContext, Const.SharedPreferencesConst.USER_NAME, APP.userName);
                        SharedPreferencesUtils.setParam(mContext, Const.SharedPreferencesConst.USER_PWD, APP.userPwd);
                        SharedPreferencesUtils.setParam(mContext, Const.SharedPreferencesConst.USER_CONF, APP.userConfCode);
                        sipManager.register((String) SharedPreferencesUtils.getParam(mContext, Const.SharedPreferencesConst.USER_NAME), String.valueOf(loginParse.getTellerIdNum()), String.valueOf(loginParse.getTellerIdNum()));
                        sipManager.initSipSet(loginParse);
                        ClientConnectManager.getInstance().loginSucess("登陆成功");
                    }
                } else {
                    Log.i(TAG, "messageReceived: 该账号没有通话权限");
                    ClientConnectManager.getInstance().ToastErr("该账号没有通话权限");
                }
            }
            else if (HexUtils.byteEquals(species, HexUtils.IntToByteBig(A_VTA_LOGOUT_RSP))) {
                //登出指令
            }
            else if (HexUtils.byteEquals(species, HexUtils.IntToByteSmall(Sc_a_VTA_SipStatus_Rsp))) {
                //sip状态
                byte[] flag = new byte[4];
                byte[] status = new byte[4];
                System.arraycopy(data, 27, flag, 0, 4);
                System.arraycopy(data, 31, status, 0, 4);
                int FLAG = HexUtils.bytesToIntSmall(flag);
                int STATUS = HexUtils.bytesToIntSmall(status);
                if (STATUS == 1) {
                    if (FLAG == 0) {
                        Intent intent = new Intent();
                        intent.setAction("Receive");
                        intent.putExtra("type", "status");
                        intent.putExtra("status", "false");
                        mContext.sendBroadcast(intent);

                        Intent close = new Intent();
                        close.setAction("SendNotify");
                        close.putExtra("status", "false");
                        mContext.sendBroadcast(close);
                    } else {
                        if (!isCheck) {
                            isCheck = true;
                            //ClientConnectManager.getInstance().ShowDialog("sip账号重启中,15秒后再次检测。");
                            sipManager.initDeleteAll();
                            sipManager.buildAccount((String)SharedPreferencesUtils.getParam(mContext, Const.SharedPreferencesConst.USER_NAME), String.valueOf(HexUtils.bytesToIntBig(APP.tellerId)), String.valueOf(HexUtils.bytesToIntBig(APP.tellerId)));
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Thread.sleep(15000);
                                        initStatus();
                                        SessionManager.getInstance().writeBytesToServer(sipStatusbytes);
                                        isCheck = false;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    /**
                                     * 要执行的操作
                                     */
                                }
                            }.start();
                        }

                    }
                }
            }
            else if (HexUtils.byteEquals(species, HexUtils.IntToByteBig(Sc_a_VTA_Msg_Rsp))) {
                //通话过程中的消息
            }
            else if (HexUtils.byteEquals(species, HexUtils.IntToByteSmall(Sc_a_VTA_Call_Rsp))) {
                //呼叫人信息
                byte[] lenB = new byte[4];
                System.arraycopy(data, 27, lenB, 0, 4);
                int lenI = HexUtils.bytesToIntBig(lenB);
                byte[] nameB = new byte[lenI];
                System.arraycopy(data, 31, nameB, 0, lenI);
                socketFd = new byte[4];
                System.arraycopy(data, 31 + lenI, socketFd, 0, 4);
                final String str = new String(nameB, "utf-8");
                final Intent intent = new Intent();
                intent.setAction("Receive");
                intent.putExtra("type", "name");
                intent.putExtra("name", str);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);//休眠3秒
                            mContext.sendBroadcast(intent);
                            android.util.Log.i(TAG, "sendBroadcast: " + str);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /**
                         * 要执行的操作
                         */
                    }
                }.start();

            }
            else if (HexUtils.byteEquals(species, HexUtils.IntToByteBig(Sc_a_VTA_Query_Rsp))) {
                //更新在线成员列表
                QueryReciverd queryParse = new QueryReciverd(data);
                APP.targetInfoList = queryParse.targetInfoList;
            }
        }

        System.out.println(ConnectUtils.stringNowTime() + " : Use:messageReceived  " + result);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        //Log.i(TAG, ConnectUtils.stringNowTime() + " : 客户端调用messageSent");
        //        session.close(true);//加上这句话实现短连接的效果，向客户端成功发送数据后断开连接
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.i(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionClosed");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        Log.i(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionCreated");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        Log.i(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionIdle");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        Log.i(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionOpened");
    }


    /**
     * 状态检测字节组
     */
    private void initStatus() {
        byte[] version = {0x01};
        int command = 1;
        byte[] catrgory = {0x00, 0x00, 0x00, 0x00};
        byte[] receive = {0x00, 0x00, (byte) 0xdc, 0x20};
        byte[] send = {0x00, 0x00, (byte) 0xdd, (byte) 0x80};
        byte[] species = HexUtils.IntToByteSmall(Sc_a_VTA_SipStatus_Req);
        SipStatusSend sipStatusSend = new SipStatusSend(version, command, catrgory, receive, send, species, APP.tellerId, new byte[]{0, 0, 0, 1});
        System.arraycopy(sipStatusSend.getByte(), 0, sipStatusbytes, 0, 35);
        System.arraycopy(socketFd, 0, sipStatusbytes, 35, 4);
        sipStatusbytes = sipStatusSend.getByte();
    }
}
