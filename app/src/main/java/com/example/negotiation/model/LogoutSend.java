package com.example.negotiation.model;

import com.example.negotiation.api.StateApplication;
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
import static com.example.negotiation.api.VTAState.TELLERID;
import static com.example.negotiation.api.VTAState.VERSION_LONG;

public class LogoutSend extends Head {

    private byte[] amsPid = new byte[4];
    private byte[] tellerId = new byte[4];

    public LogoutSend(byte[] version, int command, byte[] catrgory, byte[] receive, byte[] send, byte[] species) {
        this.version = version;
        this.command = command;
        this.catrgory = catrgory;
        this.receive = receive;
        this.send = send;
        this.species = species;
        this.amsPid = new byte[]{0, 0, 0, 1};
        this.tellerId = StateApplication.loginReciverd.getTellerId();
        contentLenth = HexUtils.IntToByteSmall(TELLERID + TELLERID);
        loginLenth = (short) (PROTOCOL_LONG + TELLERID + TELLERID);
        byteCommand = HexUtils.IntToByteSmall(this.command);
        byteLoginLenth = HexUtils.shortToByte(loginLenth);
    }


    public byte[] getByte() {
        byte[] logout = new byte[LOGIN_LONG + TELLERID + TELLERID];
        System.arraycopy(version, 0, logout, 0, VERSION_LONG);
        System.arraycopy(byteCommand, 0, logout, VERSION_LONG, COMMAND_TYPE_LONG);
        System.arraycopy(byteLoginLenth, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG, ALL_LONG);
        System.arraycopy(catrgory, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG, MESSAGE_CATRGORY);
        System.arraycopy(receive, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY, MESSAGE_RECEIVE);
        System.arraycopy(send, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE, MESSAGE_SEND);
        System.arraycopy(species, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND, MESSAGE_SPECIES);
        System.arraycopy(contentLenth, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES, MESSAGE_LONG);
        System.arraycopy(amsPid, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG, TELLERID);
        System.arraycopy(tellerId, 0, logout, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + TELLERID, TELLERID);
        return logout;
    }


}
