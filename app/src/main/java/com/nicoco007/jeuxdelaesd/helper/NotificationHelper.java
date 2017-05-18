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

import android.annotation.SuppressLint;
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
import com.nicoco007.jeuxdelaesd.activity.MapActivity;
import com.nicoco007.jeuxdelaesd.receiver.NotificationPublisher;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";
    private static final String NOTIFICATIONS_DATA_FILE_NAME = "notifications.dat";

    @SuppressLint("UseSparseArrays")
    private static HashMap<Integer, Long> notifications = new HashMap<>();

    public static Notification createNotification(Context context, String title, String content) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, MapActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_notify)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(0xFF0B2F7C, 3000, 3000)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);

        return builder.build();
    }

    public static void scheduleNotification(Context context, Notification notification, int notificationId, long triggerAtMillis) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);

        notificationIntent.setAction(Intent.ACTION_VIEW);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

        notifications.put(notificationId, triggerAtMillis);

        Log.i(TAG, "Scheduled notification with ID " + notificationId + " at " + new DateTime(triggerAtMillis).withZone(DateTimeZone.getDefault()).toString());
    }

    public static void cancelNotification(Context context, int id) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        notifications.remove(id);

        Log.i(TAG, "Cancelled notification with ID " + id);
    }

    public static DateTime getNotificationDateTime(int id) {
        for (Map.Entry<Integer, Long> entry : notifications.entrySet()) {
            if (entry.getKey() == id) {
                return new DateTime(entry.getValue()).withZone(DateTimeZone.getDefault());
            }
        }

        return null;
    }

    public static void saveNotifications(Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(NOTIFICATIONS_DATA_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(notifications);

            objectOutputStream.close();
            outputStream.close();

            Log.i(TAG, "Successfully saved notifications.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to save notifications.dat: " + e.getMessage());
        }
    }

    public static void loadNotifications(Context context) {
        try {
            FileInputStream inputStream = context.openFileInput(NOTIFICATIONS_DATA_FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            notifications = (HashMap<Integer, Long>) objectInputStream.readObject();

            // use iterator to prevent ConcurrentModificationException
            Iterator iterator = notifications.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Integer, Long> entry = (Map.Entry<Integer, Long>) iterator.next();

                if (entry.getValue() < DateTime.now().getMillis())
                    iterator.remove();
            }

            objectInputStream.close();
            inputStream.close();

            Log.i(TAG, "Successfully loaded notifications.");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            Log.e(TAG, "Failed to load notifications.dat: " + e.getMessage());
        }
    }
}
