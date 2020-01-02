package com.example.negotiation.utils;

import com.csipsimple.api.SipProfile;
import com.csipsimple.api.SipUri;
import com.csipsimple.utils.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectUtils {

    private static final String TAG = "LongConnectService";
    public static final int REPEAT_TIME = 100;
    public static String HOST = "192.168.1.181";//��ʾIP��ַ
    public static final int PORT = 12006;//��ʾ�˿ں�
    public static final String DOMAIN = "192.168.1.181";
    public static final int TIMEOUT = 5;//�������ӳ�ʱʱ��,����5s��û�����ϱ��׳��쳣

    public static final int LOGIN = 1;
    public static final int UNLOGIN = 0;
    public static final int LOGINOUT = -1;
    public static final int LOGINWRONG = -2;


    /**
     * ��ȡ��ǰʱ��
     *
     * @return
     */
    public static String stringNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public SipProfile addAccount(SipProfile account, String accountDisplayName, String accountServer,
                                 String accountUserName, String accountPassword) {
        Log.d(TAG, "begin of save ....");
        account.display_name = accountDisplayName;

        String[] serverParts = new String[]{accountServer};
        account.acc_id = "<sip:" + SipUri.encodeUser(accountUserName) + "@" + serverParts[0].trim() + ">";

        String regUri = "sip:" + accountServer;
        account.reg_uri = regUri;
        account.proxies = new String[]{regUri};


        account.realm = "*";
        account.username = accountUserName;
        account.data = accountPassword;
        account.scheme = SipProfile.CRED_SCHEME_DIGEST;
        account.datatype = SipProfile.CRED_DATA_PLAIN_PASSWD;
        //By default auto transport
        account.transport = SipProfile.TRANSPORT_UDP;
        return account;
    }

}
