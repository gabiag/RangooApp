package com.example.rangoo.Activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rangoo.R;
import com.example.rangoo.Utils.GoTo;

public class SplashActivity extends AppCompatActivity {

    private static final int _DELAY_INTENT = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //GoTo.signInView(SplashActivity.this);
                GoTo.profileView(SplashActivity.this);
            }
        }, _DELAY_INTENT);

    }
}