package com.example.negotiation.model;

import com.example.negotiation.utils.HexUtils;

import static com.example.negotiation.api.VTAState.ALL_LONG;
import static com.example.negotiation.api.VTAState.COMMAND_TYPE_LONG;
import static com.example.negotiation.api.VTAState.LOGIN_LONG;
import static com.example.negotiation.api.VTAState.MESSAGE_CATRGORY;
import static com.example.negotiation.api.VTAState.MESSAGE_LONG;
import static com.example.negotiation.api.VTAState.MESSAGE_RECEIVE;
import static com.example.negotiation.api.VTAState.MESSAGE_SEND;
import static com.example.negotiation.api.VTAState.MESSAGE_SPECIES;
import static com.example.negotiation.api.VTAState.PROTOCOL_LONG;
import static com.example.negotiation.api.VTAState.STRING_LONG;
import static com.example.negotiation.api.VTAState.VERSION_LONG;

/**
 * Created by Android Studio.
 * User: hongdw
 * Date: 2019/12/18
 * Time: 16:20
 */
public class AddMeet extends Head {


    //用户名字长度
    private byte[] msgLenth = new byte[1];
    //用户名字
    private String msg;
    //用户名字字节数组
    private byte[] byteMsg;


    public byte[] getMsgLenth() {
        return msgLenth;
    }

    public void setMsgLenth(byte[] msgLenth) {
        this.msgLenth = msgLenth;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public byte[] getByteMsg() {
        return byteMsg;
    }

    public void setByteMsg(byte[] byteMsg) {
        this.byteMsg = byteMsg;
    }


    public AddMeet(byte[] version, int command, byte[] catrgory, byte[] receive, byte[] send, byte[] species, String msg) {
        this.version = version;
        this.command = command;
        this.catrgory = catrgory;
        this.receive = receive;
        this.send = send;
        this.species = species;
        this.msg = msg;
        byteMsg = msg.getBytes();
        contentLenth = HexUtils.IntToByteSmall(byteMsg.length + 2);
        loginLenth = (short) (PROTOCOL_LONG + byteMsg.length + 2);
        byteCommand = HexUtils.IntToByteSmall(this.command);
        byteLoginLenth = HexUtils.shortToByte(loginLenth);
        msgLenth[0] = HexUtils.IntToByteSmall(byteMsg.length)[0];
    }

    public byte[] getByte() {
        byte[] addMeet = new byte[LOGIN_LONG + byteMsg.length + 2];
        System.arraycopy(version, 0, addMeet, 0, VERSION_LONG);
        System.arraycopy(byteCommand, 0, addMeet, VERSION_LONG, COMMAND_TYPE_LONG);
        System.arraycopy(byteLoginLenth, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG, ALL_LONG);
        System.arraycopy(catrgory, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG, MESSAGE_CATRGORY);
        System.arraycopy(receive, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY, MESSAGE_RECEIVE);
        System.arraycopy(send, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE, MESSAGE_SEND);
        System.arraycopy(species, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND, MESSAGE_SPECIES);
        System.arraycopy(contentLenth, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES, MESSAGE_LONG);
        System.arraycopy(msgLenth, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG, STRING_LONG);
        System.arraycopy(byteMsg, 0, addMeet, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + STRING_LONG, byteMsg.length);
        return addMeet;
    }
}
