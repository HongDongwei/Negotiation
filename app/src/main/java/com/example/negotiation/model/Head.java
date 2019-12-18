package com.example.negotiation.model;

public class Head {
    //版本号
    public byte[] version=new byte[1];
    //命令类型
    public int command;
    //命令类型字节数组
    public byte[]byteCommand=new byte[4];
    //消息长度
    public short loginLenth;
    //总长度
    public byte[] byteLoginLenth=new byte[2];
    //消息类别码
    public byte[] catrgory=new byte[4];
    //消息接收者标识
    public byte[] receive=new byte[4];
    //消息发送者标识
    public byte[] send=new byte[4];
    //消息种别码
    public byte[] species=new byte[4];
    //携带消息长度
    public byte[] contentLenth=new byte[4];

}
