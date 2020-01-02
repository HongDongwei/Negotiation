package com.example.negotiation.model;

import java.util.HashMap;
import java.util.Map;

import com.csipsimple.utils.Log;
import com.example.negotiation.base.targetInfo;
import com.example.negotiation.utils.HexUtils;

import static com.example.negotiation.base.VTAState.*;

public class QueryReciverd extends Head {
    public int resultNum;
    public Map<String, targetInfo> targetInfoList = new HashMap<>();

    public byte[] byteByte=new byte[1];
    public byte[] byteWord=new byte[2];
    public byte[] byteDword=new byte[4];
    public byte[] byteString;

    private String tmpRealName[] = new String[8];
    private String tmpLoginName[] = new String[8];
    private int tmpID[] = new int[8];
    private short tmpType[] = new short[8];
    private short tmpStatus[] = new short[8];

    public QueryReciverd(byte[] messge) {
        int cur_read = 0;
        targetInfoList.clear();
        //版本号
        System.arraycopy(messge, cur_read, version, 0, VERSION_LONG);
        cur_read += VERSION_LONG;
        //命令类型
        System.arraycopy(messge, cur_read, byteCommand, 0, COMMAND_TYPE_LONG);
        cur_read += COMMAND_TYPE_LONG;
        //消息长度
        System.arraycopy(messge, cur_read, byteLoginLenth, 0, ALL_LONG);
        cur_read += ALL_LONG;
        //消息类别码
        System.arraycopy(messge, cur_read, catrgory, 0, MESSAGE_CATRGORY);
        cur_read += MESSAGE_CATRGORY;
        //消息接收者标识
        System.arraycopy(messge, cur_read, receive, 0, MESSAGE_RECEIVE);
        cur_read += MESSAGE_RECEIVE;
        //消息发送者标识
        System.arraycopy(messge, cur_read, send, 0, MESSAGE_SEND);
        cur_read += MESSAGE_SEND;
        //消息种别码
        System.arraycopy(messge, cur_read, species, 0, MESSAGE_SPECIES);
        cur_read += MESSAGE_SPECIES;
        //携带消息长度
        System.arraycopy(messge, cur_read, contentLenth, 0, MESSAGE_LONG);
        cur_read += MESSAGE_LONG;
        //查询结果
        System.arraycopy(messge, cur_read, byteDword, 0, DWORD);
        this.resultNum = HexUtils.bytesToIntBig(byteDword);
        cur_read += DWORD;

        //realname
        for(int i = 0; i < resultNum; i++)
        {
            System.arraycopy(messge, cur_read, byteByte, 0, BYTE); cur_read += BYTE;
            byteString = new byte[byteByte[0]];
            System.arraycopy(messge, cur_read, byteString, 0, byteByte[0]); cur_read += byteByte[0];
            this.tmpRealName[i] = new String(byteString);
        }
        //id
        for(int i = 0; i < resultNum; i++)
        {
            System.arraycopy(messge, cur_read, byteDword, 0, DWORD); cur_read += DWORD;
            this.tmpID[i] = HexUtils.bytesToIntBig(byteDword);
        }
        //name
        for(int i = 0; i < resultNum; i++)
        {
            System.arraycopy(messge, cur_read, byteByte, 0, BYTE); cur_read += BYTE;
            byteString = new byte[byteByte[0]];
            System.arraycopy(messge, cur_read, byteString, 0, byteByte[0]); cur_read += byteByte[0];
            this.tmpLoginName[i] = new String(byteString);
        }
        //type
        for(int i = 0; i < resultNum; i++)
        {
            System.arraycopy(messge, cur_read, byteWord, 0, WORD); cur_read += WORD;
            this.tmpType[i] = HexUtils.bytesToIntShortBig(byteWord);
        }
        //status
        for(int i = 0; i < resultNum; i++)
        {
            System.arraycopy(messge, cur_read, byteWord, 0, WORD); cur_read += WORD;
            this.tmpStatus[i] = HexUtils.bytesToIntShortBig(byteWord);
        }

        for(int i = 0; i < resultNum; i++)
        {
            targetInfo tmpInfo = new targetInfo();
            tmpInfo.setLoginName(tmpLoginName[i]);
            tmpInfo.setRealName(tmpRealName[i]);
            tmpInfo.setTargetID(tmpID[i]);
            tmpInfo.setTargetType(tmpType[i]);
            tmpInfo.setCallStatus(tmpStatus[i]);
            targetInfoList.put(tmpLoginName[i], tmpInfo);
        }

    }
}
