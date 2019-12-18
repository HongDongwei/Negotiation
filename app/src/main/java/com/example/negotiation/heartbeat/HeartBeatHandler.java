package com.example.negotiation.heartbeat;

import com.example.negotiation.api.StateApplication;
import com.example.negotiation.model.LoginReciverd;
import com.example.negotiation.utils.ConnectUtils;
import com.example.negotiation.utils.HexUtils;
import com.csipsimple.api.SipProfileState;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.net.URLDecoder;

import static com.example.negotiation.api.VTAState.A_VTA_LOGIN_RSP;
import static com.example.negotiation.api.VTAState.A_VTA_LOGOUT_RSP;
import static com.example.negotiation.api.VTAState.LOGINRESULT;
import static com.example.negotiation.utils.ConnectUtils.LOGIN;
import static com.example.negotiation.utils.ConnectUtils.LOGINOUT;
import static com.example.negotiation.utils.ConnectUtils.LOGINWRONG;

public class HeartBeatHandler extends IoHandlerAdapter {
    private IMessageCallback messageCallback;

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        System.out.println(ConnectUtils.stringNowTime() + " : Use:exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        String result = URLDecoder.decode(message.toString(), "UTF-8");
        byte[] data = HexUtils.hexStringToBytes(result);
        if (data != null) {
            byte[] species = new byte[4];
            byte[] byteloginResult = new byte[4];
            System.arraycopy(data, 19, species, 0, LOGINRESULT);
            if (HexUtils.byteEquals(species, HexUtils.IntToByteBig(A_VTA_LOGIN_RSP))) {
                System.arraycopy(data, 27, byteloginResult, 0, LOGINRESULT);
                if (HexUtils.bytesToIntBig(byteloginResult) == 0) {
                    StateApplication.LOGINSTATE = LOGIN;
                    StateApplication.loginReciverd = new LoginReciverd(data);
                    if (null != messageCallback) {
                        messageCallback.onLogin();
                    }
                } else {
                    StateApplication.LOGINSTATE = LOGINWRONG;
                    if (null != messageCallback) {
                        messageCallback.onLoginWrong();
                    }
                }
            } else if (HexUtils.byteEquals(species, HexUtils.IntToByteBig(A_VTA_LOGOUT_RSP))) {
                StateApplication.LOGINSTATE = LOGINOUT;
                if (null != messageCallback) {
                    messageCallback.onLogout();
                }
            }
        }

        System.out.println(ConnectUtils.stringNowTime() + " : Use:messageReceived" + result);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        String result = URLDecoder.decode(message.toString(), "UTF-8");
        System.out.println(ConnectUtils.stringNowTime() + " : Use:messageSent" + result);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        //System.out.println(ConnectUtils.stringNowTime()+" : 客户端调用sessionClosed");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println(ConnectUtils.stringNowTime() + " : Use:sessionCreated");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println(ConnectUtils.stringNowTime() + " : Use:sessionIdle");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println(ConnectUtils.stringNowTime() + " : Use:sessionOpened");
    }

    public void setCallback(IMessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public SipProfileState onGotState() {
        if (null == messageCallback) {
            return null;
        }
        return messageCallback.onGotSipState();
    }
}
