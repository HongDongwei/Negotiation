package com.example.negotiation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.csipsimple.api.SipProfile;
import com.csipsimple.db.DBProvider;
import com.csipsimple.models.Filter;
import com.csipsimple.utils.PreferencesWrapper;
import com.csipsimple.utils.video.VideoUtilsWrapper;
import com.csipsimple.wizards.WizardUtils;
import com.csipsimple.wizards.impl.Basic;

import java.util.List;

import static com.csipsimple.api.SipConfigManager.STUN_SERVER;
import static com.csipsimple.api.SipConfigManager.TURN_PASSWORD;
import static com.csipsimple.api.SipConfigManager.TURN_SERVER;
import static com.csipsimple.api.SipConfigManager.TURN_USERNAME;
import static com.csipsimple.api.SipConfigManager.VIDEO_CAPTURE_SIZE;

/**
 * Des:hybirdbase - com.codvision.dld.utils.sip
 * 统一管理 Sip通信相关的逻辑
 *
 * @author xujichang、洪东伟
 * @date 2019-04-29 - 20:55
 * <p>
 * modify:
 */
public class SipManager {
    public static final String TAG = "SipManager";
    private String wizardId = "";
    private Basic wizard = null;
    private SipProfile account = null;
    private Context context;
    private PreferencesWrapper preferencesWrapper;
    public static final int ACTIVITY = 1;
    public static final int INACTIVITY = 0;
    public static final int UNREGISTERED = -1;
    private int which = 0;
    public CharSequence[] entries;
    public CharSequence[] values;
    public String[] array;
    private String oldCaptureSize;
    public static boolean getAccount = false;
    private int i = 1;

    public SipManager(Context context) {
        this.context = context;
        preferencesWrapper = new PreferencesWrapper(context);
    }

    public void initCaptureSize() {
        VideoUtilsWrapper vuw = VideoUtilsWrapper.getInstance();
        List<VideoUtilsWrapper.VideoCaptureDeviceInfo> capt = vuw.getVideoCaptureDevices(context);
        int size = capt.get(capt.size() - 1).capabilities.size();
        entries = new CharSequence[size + 1];
        values = new CharSequence[size + 1];
        int i = 0;
        entries[0] = context.getText(me.xujichang.lib.csipsimple.R.string.auto);
        values[0] = "";
        i++;
        //for(VideoCaptureDeviceInfo vcdi : capt) {
        VideoUtilsWrapper.VideoCaptureDeviceInfo vcdi = capt.get(capt.size() - 1);
        for (VideoUtilsWrapper.VideoCaptureCapability cap : vcdi.capabilities) {
            entries[i] = cap.toPreferenceDisplay();
            values[i] = cap.toPreferenceValue();
            Log.i(TAG, "afterBuildPrefsForType: entries=" + entries[i] + " values=" + values[i]);
            i++;
        }
        array = new String[i];
        for (int a = 0; a < i; a++) {
            array[a] = entries[a] + "";
        }
    }

    public int getVideoWhich() {
        oldCaptureSize = getVelue(VIDEO_CAPTURE_SIZE);
        for (int i = 0; i < array.length; i++) {
            if (oldCaptureSize.equals(values[i] + "")) {
                which = i;
            }
        }
        return which;
    }


    public void setVelue(String key, String velue) {
        preferencesWrapper.setPreferenceStringValue(key, velue);
    }

    public String getVelue(String key) {
        return preferencesWrapper.getPreferenceStringValue(key);
    }

    public void register(String displayName, String userName, String pwd) {
        Log.i(TAG, "userName=" + displayName + "userName=" + userName + "pwd=" + pwd);
        if (checkStatus(displayName) == ACTIVITY && checkStatus(displayName) == INACTIVITY) {
            Log.i(TAG, "register: 已注册");
            setAccount(displayName, true);
        } else {
            Log.i(TAG, "register: 未注册");
            //buildAccount(displayName, userName, pwd);
        }
    }

    /**
     * 删除储存的SIP账号
     */
    public void initDeleteAll() {
        SipProfile account;
        Cursor cursor = context.getContentResolver().query(SipProfile.ACCOUNT_URI, new String[]{
                SipProfile.FIELD_ID + " AS " + BaseColumns._ID,
                SipProfile.FIELD_ID,
                SipProfile.FIELD_DISPLAY_NAME,
                SipProfile.FIELD_WIZARD,
                SipProfile.FIELD_ACTIVE,
        }, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                account = new SipProfile(cursor);
                context.getContentResolver().delete(ContentUris.withAppendedId(SipProfile.ACCOUNT_ID_URI_BASE, account.id), null, null);
            }
        }
        context.sendBroadcast(new Intent(com.csipsimple.api.SipManager.ACTION_SIP_REQUEST_RESTART));
    }

    public String[] getAllCount(String userName) {
        int count = 0;
        String[] name = new String[]{"", ""};
        Cursor cursor = context.getContentResolver().query(SipProfile.ACCOUNT_URI, new String[]{
                SipProfile.FIELD_ID + " AS " + BaseColumns._ID,
                SipProfile.FIELD_ID,
                SipProfile.FIELD_DISPLAY_NAME,
                SipProfile.FIELD_WIZARD,
                SipProfile.FIELD_ACTIVE,
                SipProfile.FIELD_USERNAME,
                SipProfile.FIELD_DATA,
        }, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    account = new SipProfile(cursor);
                    if (account.active) {
                        count++;
                        name[1] = account.username;
                    }
                } while (cursor.moveToNext());
                name[0] = count + "";
                return name;
            }
        }
        return name;
    }

    //更具name获得id
    public int getId(String userName) {
        SipProfile account;
        Cursor cursor = context.getContentResolver().query(SipProfile.ACCOUNT_URI, new String[]{
                SipProfile.FIELD_ID + " AS " + BaseColumns._ID,
                SipProfile.FIELD_ID,
                SipProfile.FIELD_DISPLAY_NAME,
                SipProfile.FIELD_WIZARD,
                SipProfile.FIELD_ACTIVE,
                SipProfile.FIELD_USERNAME,
                SipProfile.FIELD_DATA,
        }, SipProfile.FIELD_DISPLAY_NAME + "=?", new String[]{userName}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                account = new SipProfile(cursor);
                Log.i(TAG, "getId: " + account.id);
                return (int) account.id;
            }
        }
        return 0;
    }

    //检查账号状态
    public int checkStatus(String userName) {
        SipProfile account;
        Cursor cursor = context.getContentResolver().query(SipProfile.ACCOUNT_URI, new String[]{
                SipProfile.FIELD_ID + " AS " + BaseColumns._ID,
                SipProfile.FIELD_ID,
                SipProfile.FIELD_DISPLAY_NAME,
                SipProfile.FIELD_WIZARD,
                SipProfile.FIELD_ACTIVE,
                SipProfile.FIELD_USERNAME,
                SipProfile.FIELD_DATA,
        }, SipProfile.FIELD_DISPLAY_NAME + "=?", new String[]{userName}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                account = new SipProfile(cursor);
                if (account.active) {
                    return ACTIVITY;
                } else {
                    return INACTIVITY;
                }
            }
        }
        return UNREGISTERED;
    }

    //设置账号状态
    public void setAccount(String userName, boolean state) {
        Log.i(TAG, "SIP设置了: " + i++ + "次");
        ContentValues cv = new ContentValues();
        cv.put(SipProfile.FIELD_ACTIVE, state);
        int i = getId(userName);
        Log.i(TAG, "userName=" + userName + "setAccount: i=" + i + "state=" + state);
        context.getContentResolver().update(ContentUris.withAppendedId(SipProfile.ACCOUNT_ID_URI_BASE, i), cv, null, null);
    }

//    //注册账号
//    public void buildAccount(String displayName, String userName, String pwd) {
//        setWizardId("BASIC");
//        account = SipProfile.getProfileFromDbId(context, SipProfile.INVALID_ID, DBProvider.ACCOUNT_FULL_PROJECTION);//6384
//        PreferencesWrapper prefs = new PreferencesWrapper(context.getApplicationContext());
//        //account = wizard.buildAccount1(account, displayName, "218.108.119.242", userName, pwd);
//        account = wizard.buildAccount1(account, displayName, DOMAIN, userName, pwd);
//        Log.i(TAG, "buildAccount: " + userName);
//        account.wizard = wizardId;
//        if (account.id == SipProfile.INVALID_ID) {
//            // This account does not exists yet
//            prefs.startEditing();
//            wizard.setDefaultParams(prefs);
//            prefs.endEditing();
//            Uri uri = context.getContentResolver().insert(SipProfile.ACCOUNT_URI, account.getDbContentValues());
//
//            // After insert, add filters for this wizard
//            account.id = ContentUris.parseId(uri);
//            List<Filter> filters = wizard.getDefaultFilters(account);
//            if (filters != null) {
//                for (Filter filter : filters) {
//                    // Ensure the correct id if not done by the wizard
//                    filter.account = (int) account.id;
//                    context.getContentResolver().insert(com.csipsimple.api.SipManager.FILTER_URI, filter.getDbContentValues());
//                }
//            }
//            // Check if we have to restart
//        }
//    }

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

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        //TODO  暂时
        return true;
    }
}
