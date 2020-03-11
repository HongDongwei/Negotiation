package com.example.negotiation.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.negotiation.R;
import com.example.negotiation.SipManager;
import com.example.negotiation.base.Const;
import com.example.negotiation.base.SharedPreferencesUtils;
import com.example.negotiation.model.LoginSend;
import com.example.negotiation.socket.manager.ClientConnectManager;
import com.example.negotiation.socket.manager.SessionManager;
import com.example.negotiation.utils.HexUtils;

//import static com.example.negotiation.base.APP.userName;
import static com.example.negotiation.base.APP.*;
import static com.example.negotiation.base.VTAState.A_VTA_LOGIN_REQ;


public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    private TextInputLayout inputUser;
    private TextInputLayout inputPassword;
    private TextInputLayout inputConfCode;
    private SipManager sipManager;
    private Handler handler;
    private byte[] loginbytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initVisible();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case Const.MsgType.MESSAGE_LOGIN_FAILED:
                        Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case Const.MsgType.SIP_STATUS:
                        Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case Const.MsgType.LOGIN_SUCCESS:
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        ClientConnectManager.getInstance().init(this, handler);
    }

    private void initVisible() {
        sipManager = new SipManager(this);
        TextView cvLogin = (TextView) findViewById(R.id.tv_login);
        inputUser = (TextInputLayout) findViewById(R.id.et_user);
        inputPassword = (TextInputLayout) findViewById(R.id.et_password);
        inputConfCode = (TextInputLayout) findViewById(R.id.et_confcode);
        final EditText etUser = inputUser.getEditText();
        final EditText etPwd = inputPassword.getEditText();
        final EditText etConfCode = inputConfCode.getEditText();

        Drawable iconUser = getResources().getDrawable(R.drawable.icon_user);
        iconUser.setBounds(0, 0, 80, 80);
        etUser.setCompoundDrawables(iconUser, null, null, null);

        Drawable iconPassword = getResources().getDrawable(R.drawable.icon_password);
        iconPassword.setBounds(0, 0, 80, 80);
        etPwd.setCompoundDrawables(iconPassword, null, null, null);

        Drawable iconConf = getResources().getDrawable(R.drawable.icon_user);
        iconConf.setBounds(0, 0, 80, 80);
        etConfCode.setCompoundDrawables(iconConf, null, null, null);

        etUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputUser.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etConfCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputConfCode.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                if (TextUtils.isEmpty(etUser.getText())) {
//                    inputUser.setError("用户账号不能为空");
//                    return;
//                }
//                if (TextUtils.isEmpty(etPwd.getText())) {
//                    inputPassword.setError("用户密码不能为空");
//                    return;
//                }
//                if (TextUtils.isEmpty(etConfCode.getText())) {
//                    inputConfCode.setError("用户会议码不能为空");
//                    return;
//                }
//                userName = etUser.getText().toString();
//                userPwd = etPwd.getText().toString();
//                userConfCode = etConfCode.getText().toString();
//                initLogin(userName, userPwd, userConfCode);
                userName = "qsgzz12";
                userPwd = "12345678";
                userConfCode = "123";
                initLogin(userName, userPwd, userConfCode);
                SessionManager.getInstance().writeBytesToServer(loginbytes);
            }
        });

        if ((boolean) SharedPreferencesUtils.getParam(this, Const.SharedPreferencesConst.REGISTER)) {
            initLogin( (String) SharedPreferencesUtils.getParam(this, Const.SharedPreferencesConst.USER_NAME),  (String) SharedPreferencesUtils.getParam(this, Const.SharedPreferencesConst.USER_PWD),   (String) SharedPreferencesUtils.getParam(this, Const.SharedPreferencesConst.USER_CONF));
            SessionManager.getInstance().writeBytesToServer(loginbytes);
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        }
    }

    private void initLogin(String username, String password, String confcode) {
        byte[] version = {0x01};
        int command = 1;
        byte[] text1 = {0x00, 0x00, 0x00, 0x00};
        byte[] text2 = {0x00, 0x00, (byte) 0xdc, 0x20};
        byte[] text3 = {0x00, 0x00, (byte) 0xdd, (byte) 0x80};
        byte[] text4 = HexUtils.IntToByteBig(A_VTA_LOGIN_REQ);
        LoginSend loginSend = new LoginSend(version, command, text1, text2, text3, text4, username, password, confcode);
        loginbytes = loginSend.getByte();
    }
}
