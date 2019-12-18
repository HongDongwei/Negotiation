package com.example.negotiation.ui;


import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.csipsimple.api.SipProfile;
import com.example.negotiation.R;
import com.example.negotiation.api.StateApplication;
import com.example.negotiation.heartbeat.HeartBeatService;
import com.example.negotiation.heartbeat.IMessageCallback;
import com.example.negotiation.model.LogoutSend;
import com.example.negotiation.utils.HexUtils;
import com.csipsimple.api.SipManager;
import com.csipsimple.api.SipProfileState;
import com.csipsimple.db.DBProvider;
import com.csipsimple.models.Filter;
import com.csipsimple.service.SipService;
import com.csipsimple.utils.PreferencesWrapper;
import com.csipsimple.wizards.WizardUtils;
import com.csipsimple.wizards.impl.Basic;

import org.apache.mina.core.buffer.IoBuffer;

import java.util.List;

import static com.example.negotiation.api.StateApplication.LOGINSTATE;
import static com.example.negotiation.api.VTAState.A_VTA_LOGOUT_REQ;

public class MainActivity extends Activity implements Handler.Callback {
    public static final String TAG = "MainActivity";
    private TextInputLayout inputUser;
    private TextInputLayout inputPassword;
    private byte[] logoutbytes;
    protected SipProfile account = null;
    private Basic wizard = null;
    private String wizardId = "";
    SharedPreferences sp = null;
    private Handler handler;
    //sip service 连接回调

    private SipService sipService;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
//            sipService = ((SipService.ProxyBinder) arg1).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };
    private HeartBeatService heartBeatService;
    private boolean mIsBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setWizardId("BASIC");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new Handler(this);
        initVisible();

    }

    @Override
    protected void onStart() {
        initDeleteData();
        super.onStart();

    }

    @Override
    protected void onResume() {
        if (StateApplication.session != null && StateApplication.loginReciverd != null) {
            initlogout();
            StateApplication.session.write(IoBuffer.wrap(logoutbytes));
        }
        super.onResume();
    }

    private void initVisible() {
        bindService(new Intent(this, SipService.class), connection, Context.BIND_AUTO_CREATE);

        TextView cvLogin = (TextView) findViewById(R.id.tv_login);
        inputUser = (TextInputLayout) findViewById(R.id.et_user);
        inputPassword = (TextInputLayout) findViewById(R.id.et_password);

        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        inputUser.getEditText().setText(sp.getString("uname", null));
        inputPassword.getEditText().setText(sp.getString("upswd", null));

        final EditText etUser = inputUser.getEditText();
        final EditText etPwd = inputPassword.getEditText();
        Drawable iconUser = getResources().getDrawable(R.drawable.icon_user);
        iconUser.setBounds(0, 0, 80, 80);
        etUser.setCompoundDrawables(iconUser, null, null, null);
        Drawable iconPassword = getResources().getDrawable(R.drawable.icon_password);
        iconPassword.setBounds(0, 0, 80, 80);
        etPwd.setCompoundDrawables(iconPassword, null, null, null);
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
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartBeatService != null) {
                    unbindService(mConnection);
                }
                if (TextUtils.isEmpty(etUser.getText())) {
                    inputUser.setError("用户账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etPwd.getText())) {
                    inputPassword.setError("用户密码不能为空");
                    return;
                }
                StateApplication.USER = etUser.getText().toString().trim();
                StateApplication.PAD = etPwd.getText().toString().trim();
                SharedPreferences.Editor editor = sp.edit();
                Log.d(TAG, "USER:" + StateApplication.USER + ";PAD:" + StateApplication.PAD);
                editor.putString("uname", StateApplication.USER);
                editor.putString("upswd", StateApplication.PAD);
                editor.commit();
                doBindService();
            }
        });
    }

    /**
     * 登出字节组
     */
    private void initlogout() {
        byte[] version = {0x01};
        int command = 1;
        byte[] text1 = {0x00, 0x00, 0x00, 0x00};
        byte[] text2 = {0x00, 0x00, (byte) 0xdc, 0x20};
        byte[] text3 = {0x00, 0x00, (byte) 0xdd, (byte) 0x80};
        byte[] text4 = HexUtils.IntToByteBig(A_VTA_LOGOUT_REQ);
        LogoutSend logoutSend = new LogoutSend(version, command, text1, text2, text3, text4);
        logoutbytes = logoutSend.getByte();
    }

    /**
     * SIP账号注册
     */
    public void register() {
        String displayName = String.valueOf(StateApplication.loginReciverd.getTellerIdNum());
        String username = String.valueOf(StateApplication.loginReciverd.getTellerIdNum());
        String password = String.valueOf(StateApplication.loginReciverd.getTellerIdNum());
        String server = "192.168.1.181";
        account = SipProfile.getProfileFromDbId(this, SipProfile.INVALID_ID, DBProvider.ACCOUNT_FULL_PROJECTION);//6384
        PreferencesWrapper prefs = new PreferencesWrapper(getApplicationContext());
        account = wizard.buildAccount1(account, displayName, server, username, password);
        account.wizard = wizardId;
        if (account.id == SipProfile.INVALID_ID) {
            // This account does not exists yet
            prefs.startEditing();
            wizard.setDefaultParams(prefs);
            prefs.endEditing();
            Uri uri = getContentResolver().insert(SipProfile.ACCOUNT_URI, account.getDbContentValues());

            // After insert, add filters for this wizard
            account.id = ContentUris.parseId(uri);
            List<Filter> filters = wizard.getDefaultFilters(account);
            if (filters != null) {
                for (Filter filter : filters) {
                    // Ensure the correct id if not done by the wizard
                    filter.account = (int) account.id;
                    getContentResolver().insert(SipManager.FILTER_URI, filter.getDbContentValues());
                }
            }
            // Check if we have to restart
        }

    }

    /**
     * 删除储存的SIP账号
     */
    private void initDeleteData() {
        Cursor cursor = getContentResolver().query(SipProfile.ACCOUNT_URI, new String[]{
                SipProfile.FIELD_ID + " AS " + BaseColumns._ID,
                SipProfile.FIELD_ID,
                SipProfile.FIELD_DISPLAY_NAME,
                SipProfile.FIELD_WIZARD,
                SipProfile.FIELD_ACTIVE
        }, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MainActivity.this.getContentResolver().delete(ContentUris.withAppendedId(SipProfile.ACCOUNT_ID_URI_BASE, new SipProfile(cursor).id), null, null);
            }
        }
        sendBroadcast(new Intent(SipManager.ACTION_SIP_REQUEST_RESTART));
    }

    //
    private boolean setWizardId(String wId) {
        if (wizardId == null) {
            return setWizardId(WizardUtils.EXPERT_WIZARD_TAG);
        }

        WizardUtils.WizardInfo wizardInfo = WizardUtils.getWizardClass(wId);
        if (wizardInfo == null) {
            if (!wizardId.equals(WizardUtils.EXPERT_WIZARD_TAG)) {
                return setWizardId(WizardUtils.EXPERT_WIZARD_TAG);
            }
            return false;
        }

        try {
            wizard = (Basic) wizardInfo.classObject.newInstance();
        } catch (IllegalAccessException e) {
            if (!wizardId.equals(WizardUtils.EXPERT_WIZARD_TAG)) {
                return setWizardId(WizardUtils.EXPERT_WIZARD_TAG);
            }
            return false;
        } catch (InstantiationException e) {
            if (!wizardId.equals(WizardUtils.EXPERT_WIZARD_TAG)) {
                return setWizardId(WizardUtils.EXPERT_WIZARD_TAG);
            }
            return false;
        }
        wizardId = wId;
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        unbindService(connection);
    }

    /**
     * 长链接回调
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            ;
            heartBeatService = ((HeartBeatService.SocketBinder) service).getService();

            //回调监听
            heartBeatService.setMessageCallback(new IMessageCallback() {
                @Override
                public void onLogin() {
                    handler.obtainMessage(LOGINSTATE).sendToTarget();
                }

                @Override
                public void onLoginWrong() {
                    handler.obtainMessage(LOGINSTATE).sendToTarget();
                }

                @Override
                public void onLogout() {
                    handler.obtainMessage(LOGINSTATE).sendToTarget();
                }


                @Override
                public SipProfileState onGotSipState() {
                    SipProfileState state = sipService.getSipProfileState(1);
                    return state;
                }
            });
        }

        public void onServiceDisconnected(ComponentName className) {
            heartBeatService = null;
        }
    };

    /**
     * 绑定服务
     */
    void doBindService() {
        bindService(new Intent(MainActivity.this, HeartBeatService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /**
     * 解除绑定
     */
    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 1:
                register();
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                break;
            case -1:
               // doUnbindService();
                Log.i(TAG, "handleMessage:doUnbindService");
                break;
            case -2:
                inputUser.setError("用户密码错误");
                break;
            default:
                break;
        }
        return true;
    }

}
