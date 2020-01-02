package com.example.negotiation.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.negotiation.R;
import com.example.negotiation.SipManager;
import com.example.negotiation.base.APP;
import com.csipsimple.api.SipConfigManager;
import com.csipsimple.service.SipService;
import com.csipsimple.utils.CustomDistribution;
import com.csipsimple.utils.PreferencesProviderWrapper;
import com.csipsimple.utils.PreferencesWrapper;
import com.csipsimple.utils.backup.BackupWrapper;
import com.example.negotiation.base.SharedPreferencesUtils;
import com.example.negotiation.model.AddMeet;
import com.example.negotiation.socket.manager.SessionManager;
import com.example.negotiation.utils.HexUtils;
import com.example.negotiation.utils.PhoneCode;

import static com.csipsimple.api.SipConfigManager.STUN_SERVER;
import static com.csipsimple.api.SipConfigManager.TURN_PASSWORD;
import static com.csipsimple.api.SipConfigManager.TURN_SERVER;
import static com.csipsimple.api.SipConfigManager.TURN_USERNAME;
import static com.csipsimple.api.SipManager.ACTION_OUTGOING_UNREGISTER;
import static com.csipsimple.api.SipManager.ACTION_SIP_REQUEST_RESTART;
import static com.csipsimple.api.SipManager.ACTION_UI_PREFS_FAST;
import static com.csipsimple.api.SipManager.ACTION_UI_PREFS_GLOBAL;
import static com.csipsimple.api.SipManager.EXTRA_OUTGOING_ACTIVITY;
import static com.example.negotiation.base.VTAState.A_VTA_LOGIN_REQ;
import static com.example.negotiation.base.VTAState.Sc_a_VTA_Conference_Cfg;

public class UserActivity extends Activity {

    private TextView tvLogout;

    //    private TextView tvUserName;
//    private TextView tvSipName;
//    private TextView tvLoginState;
    private ImageView ivText;

    private ImageButton ibSet;
    private final static int CHANGE_PREFS = 1;
    private PreferencesProviderWrapper prefProviderWrapper;
    private boolean hasTriedOnceActivateAcc = false;
    private SipManager sipManager;
    PreferencesWrapper preferencesWrapper;
    private PhoneCode pcCode;
    private byte[] addMeet;

    //add 2019-12-31
    private Button button_inConf;
    private Button button_outConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //prefWrapper = new PreferencesWrapper(this);
        prefProviderWrapper = new PreferencesProviderWrapper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initVisible();
        initEvent();
        sendBroadcast(new Intent(ACTION_SIP_REQUEST_RESTART));

        pcCode.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                showToast(code);
                initAdd(code);
                SessionManager.getInstance().writeBytesToServer(addMeet);
            }

            @Override
            public void onInput() {

            }
        });

        button_inConf = findViewById(R.id.button_inConf);
        button_inConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加入会议
            }
        });

        button_outConf = findViewById(R.id.button_outConf);
        button_outConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出会议
            }
        });
    }

    private void initAdd(String code) {
        byte[] version = {0x01};
        int command = 1;
        byte[] text1 = {0x00, 0x00, 0x00, 0x00};
        byte[] text2 = {0x00, 0x00, (byte) 0xdc, 0x20};
        byte[] text3 = {0x00, 0x00, (byte) 0xdd, (byte) 0x80};
        byte[] text4 = HexUtils.IntToByteBig(Sc_a_VTA_Conference_Cfg);
        AddMeet addMeet = new AddMeet(version, command, text1, text2, text3, text4, code);
        this.addMeet = addMeet.getByte();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initVisible() {
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        ivText = (ImageView) findViewById(R.id.iv_text);
        pcCode = (PhoneCode) findViewById(R.id.pc_code);
        sipManager = new SipManager(this);
        ibSet = (ImageButton) findViewById(R.id.ib_set);
        hasTriedOnceActivateAcc = false;
        if (!prefProviderWrapper.getPreferenceBooleanValue(SipConfigManager.PREVENT_SCREEN_ROTATION)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    private void initEvent() {
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        ibSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ACTION_UI_PREFS_GLOBAL), CHANGE_PREFS);
            }
        });
    }

    private void disconnect(boolean quit) {
        Intent intent = new Intent(ACTION_OUTGOING_UNREGISTER);
        intent.putExtra(EXTRA_OUTGOING_ACTIVITY, new ComponentName(this, UserActivity.class));
        sendBroadcast(intent);
        if (quit) {
            finish();
        }
    }

    /**
     * 返回键处理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_PREFS) {
            sendBroadcast(new Intent(ACTION_SIP_REQUEST_RESTART));
            BackupWrapper.getInstance(this).dataChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("请确认是否退出登录？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sipManager.initDeleteAll();
                SharedPreferencesUtils.clearAll(UserActivity.this);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Service monitoring stuff
    private void startSipService() {
        Thread t = new Thread("StartSip") {
            public void run() {
                Intent serviceIntent = new Intent(UserActivity.this, SipService.class);
                serviceIntent.putExtra(EXTRA_OUTGOING_ACTIVITY, new ComponentName(UserActivity.this, UserActivity.class));
                startService(serviceIntent);
                // postStartSipService();
            }

            ;
        };
        t.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        prefProviderWrapper.setPreferenceBooleanValue(PreferencesWrapper.HAS_BEEN_QUIT, false);
        startSipService();
    }

    @Override
    protected void onDestroy() {
        disconnect(false);
        super.onDestroy();
    }

    private void postStartSipService() {
        // If we have never set fast settings
        if (CustomDistribution.showFirstSettingScreen()) {
            if (!prefProviderWrapper.getPreferenceBooleanValue(PreferencesWrapper.HAS_ALREADY_SETUP, false)) {
                Intent prefsIntent = new Intent(ACTION_UI_PREFS_FAST);
                prefsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(prefsIntent);
                return;
            }
        } else {
            boolean doFirstParams = !prefProviderWrapper.getPreferenceBooleanValue(PreferencesWrapper.HAS_ALREADY_SETUP, false);
            prefProviderWrapper.setPreferenceBooleanValue(PreferencesWrapper.HAS_ALREADY_SETUP, true);
            if (doFirstParams) {
                prefProviderWrapper.resetAllDefaultValues();
            }
        }

        // If we have no account yet, open account panel,
        if (!hasTriedOnceActivateAcc) {

            hasTriedOnceActivateAcc = true;
        }
    }
}