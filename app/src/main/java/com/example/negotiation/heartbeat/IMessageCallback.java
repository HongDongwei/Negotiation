package com.example.negotiation.heartbeat;

import com.csipsimple.api.SipProfileState;

public interface IMessageCallback {
    void onLogin();

    void onLogout();

    void onLoginWrong();

    SipProfileState onGotSipState();
}
