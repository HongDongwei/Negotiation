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
import static com.example.negotiation.base.VTAState.TELLERID;
import static com.example.negotiation.base.VTAState.VERSION_LONG;

public class SipStatusSend extends Head {

    private byte[] flag = new byte[4];
    private byte[] tellerId = new byte[4];

    public SipStatusSend(byte[] version, int command, byte[] catrgory, byte[] receive, byte[] send, byte[] species, byte[] tellerId,byte[] flag) {
        this.version = version;
        this.command = command;
        this.catrgory = catrgory;
        this.receive = receive;
        this.send = send;
        this.species = species;
        this.tellerId = tellerId;
        this.flag = flag;
        contentLenth = HexUtils.IntToByteSmall(TELLERID + TELLERID);
        loginLenth = (short) (PROTOCOL_LONG + TELLERID + TELLERID);
        byteCommand = HexUtils.IntToByteSmall(this.command);
        byteLoginLenth = HexUtils.shortToByte(loginLenth);
    }


    public byte[] getByte() {
        byte[] statusSend = new byte[LOGIN_LONG + TELLERID + TELLERID];
        System.arraycopy(version, 0, statusSend, 0, VERSION_LONG);
        System.arraycopy(byteCommand, 0, statusSend, VERSION_LONG, COMMAND_TYPE_LONG);
        System.arraycopy(byteLoginLenth, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG, ALL_LONG);
        System.arraycopy(catrgory, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG, MESSAGE_CATRGORY);
        System.arraycopy(receive, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY, MESSAGE_RECEIVE);
        System.arraycopy(send, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE, MESSAGE_SEND);
        System.arraycopy(species, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND, MESSAGE_SPECIES);
        System.arraycopy(contentLenth, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES, MESSAGE_LONG);
        System.arraycopy(flag, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG, TELLERID);
        System.arraycopy(tellerId, 0, statusSend, VERSION_LONG + COMMAND_TYPE_LONG + ALL_LONG + MESSAGE_CATRGORY + MESSAGE_RECEIVE + MESSAGE_SEND + MESSAGE_SPECIES + MESSAGE_LONG + TELLERID, TELLERID);
        return statusSend;
    }


}
