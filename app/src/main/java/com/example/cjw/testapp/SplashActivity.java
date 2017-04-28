package com.example.cjw.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    private Handler handler;
    private final int SPLASH_TIME = 3000;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        handler = new Handler();

        handler.postDelayed(runnable, SPLASH_TIME);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        handler.removeCallbacks(runnable);
//    }

}
