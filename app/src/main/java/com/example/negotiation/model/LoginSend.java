package com.example.negotiation.model;

import com.example.negotiation.utils.HexUtils;

import static com.example.negotiation.base.VTAState.ALL_LONG;
import static com.example.negotiation.base.VTAState.COMMAND_TYPE_LONG;
import static com.example.negotiation.base.VTAState.LOGIN_LONG;
import static com.example.negotiation.base.VTAState.MESSAGE_CATRGORY;
import static com.example.negotiation.base.VTAState.MESSAGE_LONG;
import static com.example.negotiation.base.VTAState.MESSAGE_RECEIVE;
import static com.example.negotiation.base.VTAState.MESSAGE_SEND;
import static com.example.negotiation.base.VTAState.MESSAGE_SPECIES;
import static com.example.negotiation.base.VTAState.PROTOCOL_LONG;
import static com.example.negotiation.base.VTAState.STRING_LONG;
import static com.example.negotiation.base.VTAState.VERSION_LONG;


public class LoginSend extends Head {

    //用户名字长度
    private byte[] userLenth = new byte[1];
    //用户名字
    private String user;
    //用户名字字节数组
    private byte[] byteUser;
    //用户密码长度
    private byte[] passwordLenth = new byte[1];
    //用户密码
    private String password;
    //用户密码字节数组
    private byte[] bytePassword;
    //用户会议码长度
    private byte[] confcodeLenth = new byte[1];
    //用户会议码
    private String confcode;
    //用户会议码字节数组
    private byte[] byteConfcode;

    public byte[] getUserLenth() {
        return userLenth;
    }

    public void setUserLenth(byte[] userLenth) {
        this.userLenth = userLenth;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public byte[] getByteUser() {
        return byteUser;
    }

    public void setByteUser(byte[] byteUser) {
        this.byteUser = byteUser;
    }

    public byte[] getPasswordLenth() {
        return passwordLenth;
    }

    public void setPasswordLenth(byte[] passwordLenth) {
        this.passwordLenth = passwordLenth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getBytePassword() {
        return bytePassword;
    }

    public void setBytePassword(byte[] bytePassword) {
        this.bytePassword = bytePassword;
    }

    public byte[] getConfcodeLenth() {
        return confcodeLenth;
    }

    public void setConfcodeLenth(byte[] ConfcodeLenth) { this.confcodeLenth = ConfcodeLenth; }

    public String getConfcode() {
        return confcode;
    }

    public void setConfcode(String Confcode) {
        this.confcode = Confcode;
    }

    public byte[] getByteConfcode() {
        return byteConfcode;
    }

    public void setByteConfcode(byte[] byteConfCode) {
        this.byteConfcode = byteConfCode;
    }

    public LoginSend(byte[] version, int command, byte[] catrgory, byte[] receive, byte[] send, byte[] species, String user, String password, String confcode) {
        this.version = version;
        this.command = command;
        this.catrgory = catrgory;
        this.receive = receive;
        this.send = send;
        this.species = species;
        this.user = user;
        this.password = password;
        this.confcode = confcode;
        byteUser=user.getBytes();
        bytePassword=password.getBytes();
        byteConfcode=confcode.getBytes();
        contentLenth = HexUtils.IntToByteSmall(byteUser.length + bytePassword.length + byteConfcode.length + 3);
        loginLenth = (short) (PROTOCOL_LONG + byteUser.length + bytePassword.length + byteConfcode.length + 3);
        byteCommand = HexUtils.IntToByteSmall(this.command);
        byteLoginLenth = HexUtils.shortToByte(loginLenth);
        userLenth[0] = HexUtils.IntToByteSmall(byteUser.length)[0];
        passwordLenth[0] = HexUtils.IntToByteSmall(bytePassword.length)[0];
        confcodeLenth[0] = HexUtils.IntToByteSmall(byteConfcode.length)[0];
    }

    public byte[] getByte() {
        byte[] login = new byte[LOGIN_LONG + byteUser.length + bytePassword.length + byteConfcode.length + 3];
        System.arraycopy(version, 0, login, 0, VERSION_LONG);
        System.arraycopy(byteCommand, 0, login, VERSION_LONG, COMMAND_TYPE_LONG);
        System.arraycopy(byteLoginLenth, 0, login, VERSION_LONG + COMMAND_TYPE_LONG, ALL_LONG);
        System.arraycopy(catrgory, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG, MESSAGE_CATRGORY);
        System.arraycopy(receive, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY, MESSAGE_RECEIVE);
        System.arraycopy(send, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE, MESSAGE_SEND);
        System.arraycopy(species, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND, MESSAGE_SPECIES);
        System.arraycopy(contentLenth, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES, MESSAGE_LONG);
        System.arraycopy(userLenth, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG, STRING_LONG);
        System.arraycopy(byteUser, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + STRING_LONG, byteUser.length);
        System.arraycopy(passwordLenth, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + STRING_LONG + byteUser.length, STRING_LONG);
        System.arraycopy(password.getBytes(), 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + STRING_LONG + byteUser.length + STRING_LONG,bytePassword.length);
        System.arraycopy(confcodeLenth, 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + STRING_LONG + byteUser.length + STRING_LONG + bytePassword.length, STRING_LONG);
        System.arraycopy(confcode.getBytes(), 0, login, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + STRING_LONG + byteUser.length + STRING_LONG + bytePassword.length + STRING_LONG,byteConfcode.length);
        return login;
    }


}
