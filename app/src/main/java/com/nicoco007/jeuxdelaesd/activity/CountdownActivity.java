/*
 * Copyright © 2016 Nicolas Gnyra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicoco007.jeuxdelaesd.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.helper.TimeHelper;

import java.util.Locale;

public class CountdownActivity extends AesdActivity {

    private CountDownTimer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        super.initDrawer();

        long diff = 0;

        try {
            diff = TimeHelper.getUtcTimeDifference("2016-05-26 10:00:00 GMT-04:00");
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

        if(timer == null) {
            timer = new CountDownTimer(diff, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    TextView textDays = (TextView)findViewById(R.id.days);
                    TextView textHours = (TextView)findViewById(R.id.hours);
                    TextView textMinutes = (TextView)findViewById(R.id.minutes);
                    TextView textSeconds = (TextView)findViewById(R.id.seconds);

                    long seconds = millisUntilFinished / 1000 % 60;
                    long minutes = millisUntilFinished / (1000 * 60) % 60;
                    long hours = millisUntilFinished / (1000 * 60 * 60) % 24;
                    long days = millisUntilFinished / (1000 * 60 * 60 * 24);

                    textDays.setText(String.format(Locale.CANADA_FRENCH, "%02d", days));
                    textHours.setText(String.format(Locale.CANADA_FRENCH, "%02d", hours));
                    textMinutes.setText(String.format(Locale.CANADA_FRENCH, "%02d", minutes));
                    textSeconds.setText(String.format(Locale.CANADA_FRENCH, "%02d", seconds));
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }
}
