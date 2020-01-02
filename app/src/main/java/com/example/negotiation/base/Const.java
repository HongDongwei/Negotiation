package com.example.negotiation.base;

/**
 * Created by Android Studio.
 * User: hongdw
 * Date: 2019/12/19
 * Time: 16:48
 */
public class Const {

    public static class SharedPreferencesConst {
        public static final String USER_NAME = "user_name";
        public static final String USER_PWD = "user_pwd";
        public static final String USER_CONF = "user_confcode";
        public static final String REGISTER = "register";
    }

    public static final class MsgType {
        public static final int MESSAGE_LOGIN_FAILED = 1;
        public static final int MESSAGE_CALL_NAME = 2;
        public static final int SIP_STATUS = 3;
        public static final int LOGIN_SUCCESS = 4;
    }
}
