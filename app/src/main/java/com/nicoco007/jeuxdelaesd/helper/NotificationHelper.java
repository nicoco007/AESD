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

package com.nicoco007.jeuxdelaesd.helper;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.nicoco007.jeuxdelaesd.R;

public class NotificationHelper {

    public static Notification createNotification(Context context, String title, String content) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_scg);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setLights(0xFFFFFFFF, 3000, 3000);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        return builder.build();

    }

}
