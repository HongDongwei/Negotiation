package com.example.negotiation.base;

public class targetInfo {
    private short m_nTargetType;
    //private short m_nCallType;
    private short m_CallStatus;
    private int m_nTargetId;
    //private String m_CallId;
    private String m_LoginName;
    private String m_RealName;

    public void targetInfo(){
        this.m_nTargetType = 1;
        this.m_CallStatus = 6;
        this.m_nTargetId = 0;
        this.m_LoginName = "";
        this.m_RealName = "";
    }

    public void setTargetType(short type){this.m_nTargetType = type;};
    public short getTargetType(){return this.m_nTargetType;};

    public void setCallStatus(short status){this.m_CallStatus = status;};
    public short getCallStatus(){return this.m_CallStatus;};

    public void setTargetID(int id){this.m_nTargetId = id;};
    public int getTargetID(){return this.m_nTargetId;};

    public void setLoginName(String name){this.m_LoginName = name;};
    public String getLoginName(){return this.m_LoginName;};

    public void setRealName(String name){this.m_RealName = name;};
    public String getRealName(){return this.m_RealName;};
}
