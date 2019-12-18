package com.example.negotiation.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.negotiation.R;

/**
 *
 * 跳转时间倒计时页面
 * Created by Administrator on 2016/12/23.
 */

public class LeadActivity extends Activity {
    int time = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

        handler.postDelayed(runnable, 3000);

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            handler.postDelayed(this, 2000);

            if (time == 0) {
                Intent intent = new Intent(LeadActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } 
        }
    };
}
