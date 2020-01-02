package com.example.negotiation.model;


import com.example.negotiation.utils.HexUtils;

public class HeartSend {
    private byte[] version = {0x01};
    private byte[] type = HexUtils.IntToByteSmall(2);
    private byte[] len = HexUtils.shortToByte((short) 0);
    private byte[] heart = new byte[7];

    public byte[] getHeartSend() {
        System.arraycopy(version, 0, heart, 0, 1);
        System.arraycopy(type, 0, heart, 1, 4);
        System.arraycopy(len, 0, heart, 5, 2);
        return heart;
    }

}
