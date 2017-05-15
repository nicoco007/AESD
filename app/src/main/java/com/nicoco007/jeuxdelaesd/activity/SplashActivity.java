package com.nicoco007.jeuxdelaesd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.helper.APICommunication;

public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load locations before we show anything
        APICommunication.loadLocations(this);

        // start main activity
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }
}
