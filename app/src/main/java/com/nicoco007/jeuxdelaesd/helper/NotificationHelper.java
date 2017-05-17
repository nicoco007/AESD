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

package com.nicoco007.jeuxdelaesd.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.receiver.NotificationPublisher;

import org.joda.time.DateTime;

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";

    public static Notification createNotification(Context context, String title, String content) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher) // TODO: dedicated notification icon
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(0xFF0B2F7C, 3000, 3000)
                .setSound(alarmSound);

        return builder.build();
    }

    public static Notification getNotification(Context context, int notificationId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);

        PendingIntent intent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_NO_CREATE);

        Log.i(TAG, "Intent exists: " + (intent != null));
        Log.i(TAG, "Intent: " + intent);

        return new Notification();
    }

    public static void scheduleNotification(Context context, Notification notification, int notificationId, long triggerAtMillis) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);
    }
}
