package com.nicoco007.jeuxdelaesd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nicoco007.jeuxdelaesd.R;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, MapActivity.class);
                startActivity(i);
                finish();

            }

        }, 3000);

    }

}
