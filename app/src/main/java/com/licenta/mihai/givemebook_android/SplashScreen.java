package com.licenta.mihai.givemebook_android;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.licenta.mihai.givemebook_android.Utils.AppSettings;
import com.licenta.mihai.givemebook_android.Utils.Util;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.openActivityClosingParent(SplashScreen.this, LandingActivity.class);
            }
        }, AppSettings.SPLASH_DISPLAY_LENGTH);
    }
}
