package com.example.negotiation.model;


import com.example.negotiation.utils.HexUtils;

import static com.example.negotiation.base.VTAState.ALL_LONG;
import static com.example.negotiation.base.VTAState.COMMAND_TYPE_LONG;
import static com.example.negotiation.base.VTAState.IDENTIFICATION;
import static com.example.negotiation.base.VTAState.JSMPEGSERVERIP;
import static com.example.negotiation.base.VTAState.JSMPEGSERVERPORT;
import static com.example.negotiation.base.VTAState.LOGINRESULT;
import static com.example.negotiation.base.VTAState.MESSAGE_CATRGORY;
import static com.example.negotiation.base.VTAState.MESSAGE_LONG;
import static com.example.negotiation.base.VTAState.MESSAGE_RECEIVE;
import static com.example.negotiation.base.VTAState.MESSAGE_SEND;
import static com.example.negotiation.base.VTAState.MESSAGE_SPECIES;
import static com.example.negotiation.base.VTAState.STRING_LONG;
import static com.example.negotiation.base.VTAState.STUNSERVERIP;
import static com.example.negotiation.base.VTAState.STUNSERVERPORT;
import static com.example.negotiation.base.VTAState.TELLERID;
import static com.example.negotiation.base.VTAState.TELLERTYPE;
import static com.example.negotiation.base.VTAState.TURNSERVERIP;
import static com.example.negotiation.base.VTAState.TURNSERVERPORT;
import static com.example.negotiation.base.VTAState.VERSION_LONG;

public class LoginReciverd extends Head {

    //登录结果
    private int loginResult;
    //登录结果字节数组
    private byte[] byteloginResult = new byte[4];
    //用户ID
    private byte[] tellerId = new byte[4];
    //用户IDnum
    private int tellerIdNum;
    //用户类型
    private byte[] tellerType = new byte[2];
    //是否启用stun/turn标识
    private byte[] identification = new byte[2];
    //StunServerIp
    private byte[] stunServerIp = new byte[4];
    //StunServerPort
    private byte[] stunServerPort = new byte[2];

    public String getStunServer() {
        return stunServer;
    }

    public void setStunServer(String stunServer) {
        this.stunServer = stunServer;
    }

    //StunServer
    private String stunServer;
    //StunUserName
    private String stunUserName;
    //StunUserName字节数组
    private byte[] byteStunUserName;
    //StunUserName长度
    private byte[] stunUserNameLenth = new byte[1];
    //StunPwd
    private String stunPwd;
    //StunPwd字节数组
    private byte[] byteStunPwd;
    //StunPwd长度
    private byte[] stunPwdLenth = new byte[1];
    //TurnServerIp
    private byte[] turnServerIp = new byte[4];
    //TurnServerPort
    private byte[] turnServerPort = new byte[2];

    public String getTurnServer() {
        return turnServer;
    }

    public void setTurnServer(String turnServer) {
        this.turnServer = turnServer;
    }

    //TurnServer
    private String turnServer;
    //TurnUserName
    private String turnUserName;
    //TurnUserName字节数组
    private byte[] byteTurnUserName;
    //TurnUserName长度
    private byte[] turnUserNameLenth = new byte[1];
    //TurnPwd
    private String turnPwd;
    //TurnPwd字节数组
    private byte[] byteTurnPwd;
    //TurnPwd长度
    private byte[] turnPwdLenth = new byte[1];
//    //JsmpegServerIp
//    private byte[] jsmpegServerIp = new byte[4];
//    //JsmpegServerPort
//    private byte[] jsmpegServerPort = new byte[2];

    public int getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(int loginResult) {
        this.loginResult = loginResult;
    }

    public byte[] getByteloginResult() {
        return byteloginResult;
    }

    public void setByteloginResult(byte[] byteloginResult) {
        this.byteloginResult = byteloginResult;
    }

    public byte[] getTellerId() {
        return tellerId;
    }

    public void setTellerId(byte[] tellerId) {
        this.tellerId = tellerId;
    }

    public byte[] getTellerType() {
        return tellerType;
    }

    public void setTellerType(byte[] tellerType) {
        this.tellerType = tellerType;
    }

    public byte[] getIdentification() {
        return identification;
    }

    public void setIdentification(byte[] identification) {
        this.identification = identification;
    }

    public byte[] getStunServerIp() {
        return stunServerIp;
    }

    public void setStunServerIp(byte[] stunServerIp) {
        this.stunServerIp = stunServerIp;
    }

    public byte[] getStunServerPort() {
        return stunServerPort;
    }

    public void setStunServerPort(byte[] stunServerPort) {
        this.stunServerPort = stunServerPort;
    }

    public String getStunUserName() {
        return stunUserName;
    }

    public int getTellerIdNum() {
        return tellerIdNum;
    }

    public void setTellerIdNum(int tellerIdNum) {
        this.tellerIdNum = tellerIdNum;
    }

    public void setStunUserName(String stunUserName) {
        this.stunUserName = stunUserName;
    }

    public byte[] getByteStunUserName() {
        return byteStunUserName;
    }

    public void setByteStunUserName(byte[] byteStunUserName) {
        this.byteStunUserName = byteStunUserName;
    }

    public byte[] getStunUserNameLenth() {
        return stunUserNameLenth;
    }

    public void setStunUserNameLenth(byte[] stunUserNameLenth) {
        this.stunUserNameLenth = stunUserNameLenth;
    }

    public String getStunPwd() {
        return stunPwd;
    }

    public void setStunPwd(String stunPwd) {
        this.stunPwd = stunPwd;
    }

    public byte[] getByteStunPwd() {
        return byteStunPwd;
    }

    public void setByteStunPwd(byte[] byteStunPwd) {
        this.byteStunPwd = byteStunPwd;
    }

    public byte[] getStunPwdLenth() {
        return stunPwdLenth;
    }

    public void setStunPwdLenth(byte[] stunPwdLenth) {
        this.stunPwdLenth = stunPwdLenth;
    }

    public byte[] getTurnServerIp() {
        return turnServerIp;
    }

    public void setTurnServerIp(byte[] turnServerIp) {
        this.turnServerIp = turnServerIp;
    }

    public byte[] getTurnServerPort() {
        return turnServerPort;
    }

    public void setTurnServerPort(byte[] turnServerPort) {
        this.turnServerPort = turnServerPort;
    }

    public String getTurnUserName() {
        return turnUserName;
    }

    public void setTurnUserName(String turnUserName) {
        this.turnUserName = turnUserName;
    }

    public byte[] getByteTurnUserName() {
        return byteTurnUserName;
    }

    public void setByteTurnUserName(byte[] byteTurnUserName) {
        this.byteTurnUserName = byteTurnUserName;
    }

    public byte[] getTurnUserNameLenth() {
        return turnUserNameLenth;
    }

    public void setTurnUserNameLenth(byte[] turnUserNameLenth) {
        this.turnUserNameLenth = turnUserNameLenth;
    }

    public String getTurnPwd() {
        return turnPwd;
    }

    public void setTurnPwd(String turnPwd) {
        this.turnPwd = turnPwd;
    }

    public byte[] getByteTurnPwd() {
        return byteTurnPwd;
    }

    public void setByteTurnPwd(byte[] byteTurnPwd) {
        this.byteTurnPwd = byteTurnPwd;
    }

    public byte[] getTurnPwdLenth() {
        return turnPwdLenth;
    }

    public void setTurnPwdLenth(byte[] turnPwdLenth) {
        this.turnPwdLenth = turnPwdLenth;
    }

//    public byte[] getJsmpegServerIp() {
//        return jsmpegServerIp;
//    }
//
//    public void setJsmpegServerIp(byte[] jsmpegServerIp) {
//        this.jsmpegServerIp = jsmpegServerIp;
//    }
//
//    public byte[] getJsmpegServerPort() {
//        return jsmpegServerPort;
//    }
//
//    public void setJsmpegServerPort(byte[] jsmpegServerPort) {
//        this.jsmpegServerPort = jsmpegServerPort;
//    }


    public LoginReciverd(byte[] messge) {
        //版本号
        System.arraycopy(messge, 0, version, 0, VERSION_LONG);
        //命令类型
        System.arraycopy(messge, VERSION_LONG, byteCommand, 0, COMMAND_TYPE_LONG);
        //消息长度
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG, byteLoginLenth, 0, ALL_LONG);
        //消息类别码
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG, catrgory, 0, MESSAGE_CATRGORY);
        //消息接收者标识
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY, receive, 0, MESSAGE_RECEIVE);
        //消息发送者标识
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE, send, 0, MESSAGE_SEND);
        //消息种别码
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND, species, 0, MESSAGE_SPECIES);
        //携带消息长度
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES, contentLenth, 0, MESSAGE_LONG);
        //登录结果
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG, byteloginResult, 0, LOGINRESULT);
        //用户ID
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT, tellerId, 0, TELLERID);
        //用户类型
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID, tellerType, 0, TELLERTYPE);
        //是否启用stun/turn标识
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE, identification, 0, IDENTIFICATION);
        //StunServerIp
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION, stunServerIp, 0, STUNSERVERIP);
        //StunServerPort
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP, stunServerPort, 0, STUNSERVERPORT);
        //StunUserName长度
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT, stunUserNameLenth, 0, STRING_LONG);
        byteStunUserName = new byte[stunUserNameLenth[0]];
        //StunUserName
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG, byteStunUserName, 0, stunUserNameLenth[0]);
        //StunPwd长度
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0], stunPwdLenth, 0, STRING_LONG);
        byteStunPwd = new byte[stunPwdLenth[0]];
        //StunPwd
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG, byteStunPwd, 0, stunPwdLenth[0]);
        //TurnServerIp
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0], turnServerIp, 0, TURNSERVERIP);
        //TurnServerPort
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0] + TURNSERVERIP, turnServerPort, 0, TURNSERVERPORT);
        //TurnUserName长度
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0] + TURNSERVERIP + TURNSERVERPORT, turnUserNameLenth, 0, STRING_LONG);
        byteTurnUserName = new byte[turnUserNameLenth[0]];
        //TurnUserName
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0] + TURNSERVERIP + TURNSERVERPORT + STRING_LONG, byteTurnUserName, 0, turnUserNameLenth[0]);
        //TurnPwd长度
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0] + TURNSERVERIP + TURNSERVERPORT + STRING_LONG + turnUserNameLenth[0], turnPwdLenth, 0, STRING_LONG);
        byteTurnPwd = new byte[turnPwdLenth[0]];
        //TurnPwd
        System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0] + TURNSERVERIP + TURNSERVERPORT + STRING_LONG + turnUserNameLenth[0] + STRING_LONG, byteTurnPwd, 0, turnPwdLenth[0]);
        //JsmpegServerIp
     //   System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0] + TURNSERVERIP + TURNSERVERPORT + STRING_LONG + turnUserNameLenth[0] + STRING_LONG + turnPwdLenth[0], jsmpegServerIp, 0, JSMPEGSERVERIP);
        //jsmpegServerPort
   //     System.arraycopy(messge, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + LOGINRESULT + TELLERID + TELLERTYPE + IDENTIFICATION + STUNSERVERIP + STUNSERVERPORT + STRING_LONG + stunUserNameLenth[0] + STRING_LONG + stunPwdLenth[0] + TURNSERVERIP + TURNSERVERPORT + STRING_LONG + turnUserNameLenth[0] + STRING_LONG + turnPwdLenth[0] + JSMPEGSERVERIP, jsmpegServerPort, 0, JSMPEGSERVERPORT);
        this.command = HexUtils.bytesToIntBig(byteCommand);
        this.loginLenth = HexUtils.bytesToIntShortSmall(byteLoginLenth);
        this.command = HexUtils.bytesToIntBig(byteCommand);
        this.loginResult = HexUtils.bytesToIntBig(byteloginResult);
        this.tellerIdNum= HexUtils.bytesToIntBig(tellerId);
        this.stunUserName = new String(byteStunUserName);
        this.stunPwd = new String(byteStunPwd);
        this.turnPwd = new String(byteTurnPwd);
        this.turnUserName = new String(byteTurnUserName);
//        int stunServerIp1=stunServerIp[0]& 0xFF;
//        int stunServerIp2=stunServerIp[1]& 0xFF;
//        int stunServerIp3=stunServerIp[2]& 0xFF;
//        int stunServerIp4=stunServerIp[3]& 0xFF;
//        short stunServerPortNum=HexUtils.bytesToIntShortBig(stunServerPort) ;
//        stunServer= String.valueOf(stunServerIp1)+"."+ String.valueOf(stunServerIp2)+"."+ String.valueOf(stunServerIp3)+"."+ String.valueOf(stunServerIp4)+":"+ String.valueOf(stunServerPortNum);
//        int turnServerIp1=turnServerIp[0]& 0xFF;
//        int turnServerIp2=turnServerIp[1]& 0xFF;
//        int turnServerIp3=turnServerIp[2]& 0xFF;
//        int turnServerIp4=turnServerIp[3]& 0xFF;
//        short turnServerPortNum=HexUtils.bytesToIntShortBig(turnServerPort) ;
//        turnServer= String.valueOf(turnServerIp1)+"."+ String.valueOf(turnServerIp2)+"."+ String.valueOf(turnServerIp3)+"."+ String.valueOf(turnServerIp4)+":"+ String.valueOf(turnServerPortNum);
//
    }
}
