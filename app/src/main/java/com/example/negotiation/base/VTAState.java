package com.example.negotiation.base;

//
//VTA,AMS接口
//
public class VTAState {
    //柜员登陆请求
    public static final int A_VTA_LOGIN_REQ = 0x01100001;
    //柜员登陆响应
    public static final int A_VTA_LOGIN_RSP = 0x01100002;
    //柜员登出请求
    public static final int A_VTA_LOGOUT_REQ = 0x01100003;
    //柜员登出响应
    public static final int A_VTA_LOGOUT_RSP = 0x01100004;
    //柜员握手请求
    public static final int A_VTA_HANDSHAKE_REQ = 0x01100005;
    //柜员握手响应
    public static final int A_VTA_HANDSHAKE_RSP = 0x01100006;
    //柜员状态操作请求
    public static final int A_VTA_STATE_OPERATE_REQ = 0x01100007;
    //柜员状态操作响应
    public static final int A_VTA_STATE_OPERATE_RSP = 0x01100008;
    //柜员状态操作指示
    public static final int A_VTA_STATE_OPERATE_IND = 0x01100009;
    //柜员状态操作证实
    public static final int A_VTA_STATE_OPERATE_CNF = 0x0110000a;
    //柜员三方会话请求
    public static final int A_VTA_MULTI_SESS_REQ = 0x01100031;
    //柜员三方会话响应
    public static final int A_VTA_MULTI_SESS_RSP = 0x01100032;
    //加入会议指示
    //public static final int Sc_a_VTA_Conference_Ind = 0x01100033;
    //加入会议请求
    public static final int Sc_a_VTA_Conference_Cfg = 0x01100034;
    //柜员查询信息请求
    public static final int A_VTA_QUERY_INFO_REQ = 0x0110006e;
    //柜员查询信息响应
    public static final int A_VTA_QUERY_INFO_RSP = 0x0110006f;
    //质检员监听请求
    public static final int A_INSPECTOR_MONITOR_REQ = 0x011000a0;
    //质检员监听响应
    public static final int A_INSPECTOR_MONITOR_RSP = 0x011000a1;
    //质检员监听指示
    public static final int A_INSPECTOR_MONITOR_IND = 0x011000a2;
    //质检员监听证实
    public static final int A_INSPECTOR_MONITOR_IND_CNF = 0x011000a3;
    //VTA共享请求
    public static final int A_VTA_SHARE_REQ = 0x01100201;
    //VTA共享响应
    public static final int A_VTA_SHARE_RSP = 0x01100202;
    //VTA结束共享请求
    public static final int A_VTA_ENDSHARE_REQ = 0x01100203;
    //VTA结束共享响应
    public static final int A_VTA_ENDSHARE_RSP = 0x01100204;

    /* add 2019-12-31 for conference begin */
    //查询在线用户请求
    public static final int Sc_a_VTA_Query_Req   = 0x01100026;
    //查询在线用户响应
    public static final int Sc_a_VTA_Query_Rsp   = 0x01100027;
    //发起呼叫请求
    public static final int Sc_a_VTA_Call_Req    = 0x01100028;
    //发起呼叫响应
    public static final int Sc_a_VTA_Call_Rsp    = 0x01100029;
    //呼叫事件请求
    public static final int Sc_a_VTA_CallEvent_Req = 0x01100030;
    //呼叫事件响应
    public static final int Sc_a_VTA_CallEvent_Rsp = 0x01100031;
    //加入会议指示
    public static final int Sc_a_VTA_Conference_Ind = 0x01100033;
    //加入会议请求
    public static final int Sc_a_VTA_Conference_cfg = 0x01100034;
    //发起邀请请求
    public static final int Sc_a_VTA_Invite_Req = 0x01100052;//add 2019-12-10
    //邀请及响应广播
    public static final int Sc_a_VTA_Invite_Rsp = 0x01100053;//add 2019-12-10
    //被邀请指示
    public static final int Sc_a_VTA_Invite_Ind = 0x01100054;//add 2019-12-10
    //被邀请响应
    public static final int Sc_a_VTA_InviteInd_Rsp = 0x01100055;//add 2019-12-10
    //查询指示
    public static final int Sc_a_VTA_Query_Ind   = 0x01100056; //add 2019-12-11
    //禁言指示
    public static final int Sc_a_VTA_Mute_Ind   = 0x01100057; //
    //发送消息请求
    public static final int Sc_a_VTA_Msg_Req = 0x01100058; //add 2019-12-27
    //收到消息响应
    public static final int Sc_a_VTA_Msg_Rsp = 0x01100059; //add 2019-12-27
    /* add 2019-12-31 for conference end */

    //呼叫人信息
    //public static final int Sc_a_VTA_Call_Rsp = 0x01100029;
    //通话过程中的消息
    //public static final int Sc_a_VTA_RecvMsg = 0x01100040;
    //sip状态检测
    public static final int Sc_a_VTA_SipStatus_Req = 0x01100061;
    //sip状态回复
    public static final int Sc_a_VTA_SipStatus_Rsp = 0x01100062;


    //版本号长度
    public static final int VERSION_LONG = 1;
    //命令类型长度
    public static final int COMMAND_TYPE_LONG = 4;
    //消息长度
    public static final int ALL_LONG = 2;
    //消息总长度
    public static final int LOGIN_LONG = 27;
    //协议长度
    public static final int PROTOCOL_LONG = 20;

    //携带消息

    //消息类别码
    public static final int MESSAGE_CATRGORY = 4;
    //消息接收者标识
    public static final int MESSAGE_RECEIVE = 4;
    //消息发送者标识
    public static final int MESSAGE_SEND = 4;
    //消息种别码
    public static final int MESSAGE_SPECIES = 4;
    //携带消息长度
    public static final int MESSAGE_LONG = 4;
    //字符串长度
    public static final int STRING_LONG = 1;
    //登录结果
    public static final int LOGINRESULT = 4;
    //用户id长度
    public static final int TELLERID = 4;
    //用户类型长度
    public static final int TELLERTYPE = 2;
    //是否启用stun/turn标识长度
    public static final int IDENTIFICATION = 2;
    //StunServerIp长度
    public static final int STUNSERVERIP = 4;
    //StunServerPort长度
    public static final int STUNSERVERPORT = 2;
    //TurnServerPort长度
    public static final int TURNSERVERIP = 4;
    //TurnServerPort长度
    public static final int TURNSERVERPORT = 2;
    //JsmpegServerIp长度
    public static final int JSMPEGSERVERIP = 4;
    //JsmpegServerPort长度
    public static final int JSMPEGSERVERPORT = 2;

    /* add 2020-01-02, 消息字段类型 */
    public static final int BYTE = 1;
    public static final int WORD = 2;
    public static final int DWORD = 4;

    //登录类别码
    public static final byte[] LOGIN_SPECIES = {2, 0, 16, 1};
    //登出类别码
    public static final byte[] LOGOUT_SPECIES = {2, 0, 16, 1};
}
