/*
 * Copyright 2016–2017 Nicolas Gnyra
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

package com.nicoco007.jeuxdelaesd.model;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.helper.TimeHelper;
import com.nicoco007.jeuxdelaesd.receiver.NotificationPublisher;
import com.nicoco007.jeuxdelaesd.helper.NotificationHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DayActivity {

    private static String TAG = "DayActivity";
    private static String PREFERENCES_FILE = "ACTIVITY_TIMERS";

    private Context context;
    private View view;

    private String text;
    private long startTime;
    private long endTime;
    private Coordinates marker;
    private PendingIntent pendingIntent;
    private Integer alarmDelay = null;

    public DayActivity(Context context, String text, String startTime, String endTime, MarkerInfo markerInfo) {

        this.context = context;
        this.text = text;
        this.marker = markerInfo.getCoordinates();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.CANADA_FRENCH);
            this.startTime = dateFormat.parse(startTime).getTime();
            this.endTime = dateFormat.parse(endTime).getTime();
        } catch (ParseException ex) {
            Log.i(TAG, "Failed to parse time: " + ex.getMessage());
        }

        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, 0);

        String key = Integer.toString(hashCode());

        if(settings.contains(key)) {

            Log.i(TAG, String.format("Loading alarm for '%s'...", text));

            int delay = settings.getInt(key, 0);

            if(delay > 0) {

                Log.i(TAG, "Refreshing alarm...");

                alarmDelay = delay;

                setupAlarm(delay);

            }

        }

    }

    public String getText() {

        return text;

    }

    public long getStartTime() {

        return startTime;

    }

    public long getEndTime() {

        return endTime;

    }

    public Coordinates getMarker() {

        return marker;

    }

    public int getRemindIndex() {

        if(alarmDelay == null) return 0;

        switch(alarmDelay) {
            case 5 * 60 * 1000:
                return 4;
            case 10 * 60 * 1000:
                return 3;
            case 15 * 60 * 1000:
                return 2;
            case 30 * 60 * 1000:
                return 1;
            default:
                return 0;

        }

    }

    public void setView(View view) {

        this.view = view;

        setAlarmEnabled(alarmDelay != null && alarmDelay > 0);

    }

    private void setAlarmEnabled(boolean enabled) {

        if(view instanceof ImageView) {

            ImageView imageView = (ImageView)view;

            imageView.setImageResource(enabled ? R.drawable.ic_alarm_black_36dp : R.drawable.ic_alarm_gray_36dp);

            imageView.invalidate();

        } else {

            Log.i(TAG, "Invalid view specified");

        }

    }

    public int hashCode() {

        return text.hashCode();

    }

    @SuppressLint("CommitPrefEdits")
    public void setNotification(int delay) {

        long timeDiff;

        if((timeDiff = setupAlarm(delay)) > 0) {

            SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Integer.toString(hashCode()), delay);
            editor.apply();

            setAlarmEnabled(true);

            Log.i(TAG, "Scheduled notification for " + timeDiff + " (in " + (timeDiff - SystemClock.elapsedRealtime()) + " ms)");

        }

    }

    private long setupAlarm(int delay) {

        Log.i(TAG, "Attempting to create notification and set up alarm for " + text);

        long time = this.startTime - delay;
        long timeDiff = SystemClock.elapsedRealtime() + TimeHelper.getUtcTimeDifference(time);

        if(TimeHelper.getUtcTimeDifference(time) < 0) {

            Log.i(TAG, String.format(Locale.CANADA_FRENCH, "Cannot set alarm: too early (%d ms ago)!", Math.abs(timeDiff - SystemClock.elapsedRealtime())));

            return 0;

        }

        Notification notification = NotificationHelper.createNotification(
                context,
                text + " va bientôt débuter!",
                String.format(Locale.CANADA_FRENCH, "L'activité va commencer dans %d minutes.", delay / 60 / 1000)
        );

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, hashCode());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        pendingIntent = PendingIntent.getBroadcast(context, hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeDiff, pendingIntent);

        alarmDelay = delay;

        return timeDiff;

    }

    @SuppressLint("CommitPrefEdits")
    public void clearNotification() {

        Log.i(TAG, "Clearing notification for " + text);

        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmDelay = null;

            SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(Integer.toString(hashCode()));
            editor.apply();

            setAlarmEnabled(false);
        }

    }

}
