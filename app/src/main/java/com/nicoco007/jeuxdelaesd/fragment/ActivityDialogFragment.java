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

package com.nicoco007.jeuxdelaesd.fragment;

import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.adapter.ResultsListAdapter;
import com.nicoco007.jeuxdelaesd.adapter.SimpleSpinnerAdapter;
import com.nicoco007.jeuxdelaesd.events.ShowMapCoordsEvent;
import com.nicoco007.jeuxdelaesd.helper.NotificationHelper;
import com.nicoco007.jeuxdelaesd.model.Activity;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Locale;

public class ActivityDialogFragment extends DialogFragment {
    private static final String TAG = "ActivityDialogFragment";
    private Activity item;

    private EventBus eventBus = EventBus.getDefault();

    public ActivityDialogFragment() {}

    public void setActivity(Activity item) {
        this.item = item;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(item != null) {
            View dialogView = View.inflate(getContext(), R.layout.dialog_activity, null);

            Button button = (Button)dialogView.findViewById(R.id.dialog_activity_button);

            if(item.getParentLocation() == null) {
                button.setEnabled(false);
                button.setText(getString(R.string.location_unavailable));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getParentLocation() != null) {
                        eventBus.post(new ShowMapCoordsEvent(item.getParentLocation().getLatitude(), item.getParentLocation().getLongitude()));
                        getDialog().dismiss();
                    }
                }
            });

            TextView text = (TextView)dialogView.findViewById(R.id.dialog_activity_time);
            text.setText(getContext().getString(
                    R.string.activity_time_text,
                    item.getStartTime().getHourOfDay(),
                    item.getStartTime().getMinuteOfHour(),
                    item.getEndTime().getHourOfDay(),
                    item.getEndTime().getMinuteOfHour()
            ));

            Spinner spinner = (Spinner)dialogView.findViewById(R.id.dialog_activity_spinner);

            ArrayList<String> spinnerItems = new ArrayList<>();
            spinnerItems.add("Jamais");
            spinnerItems.add("30 minutes avant");
            spinnerItems.add("15 minutes avant");
            spinnerItems.add("10 minutes avant");
            spinnerItems.add("5 minutes avant");

            spinner.setAdapter(new SimpleSpinnerAdapter(getContext(), spinnerItems));

            DateTime notificationTime = NotificationHelper.getNotificationDateTime(item.getName().hashCode());

            if (notificationTime != null) {
                if (notificationTime.isBefore(item.getStartTime()) && notificationTime.isAfter(DateTime.now())) {
                    int definedTimeDiff = (int) new Interval(notificationTime, item.getStartTime()).toDuration().getStandardMinutes();
                    Log.i(TAG, String.valueOf(definedTimeDiff));

                    switch (definedTimeDiff) {
                        case 30:
                            spinner.setSelection(1);
                            break;
                        case 15:
                            spinner.setSelection(2);
                            break;
                        case 10:
                            spinner.setSelection(3);
                            break;
                        case 5:
                            spinner.setSelection(4);
                            break;
                    }
                } else {
                    Log.w(TAG, "Invalid alarm defined, cancelling");
                    NotificationHelper.cancelNotification(getContext(), item.getName().hashCode());
                }
            } else {
                Log.i(TAG, "No defined alarm");
            }

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int notifId = item.getName().hashCode();
                    Integer timeDiff = null;

                    switch(position) {
                        case 1:
                            timeDiff = 30;
                            break;
                        case 2:
                            timeDiff = 15;
                            break;
                        case 3:
                            timeDiff = 10;
                            break;
                        case 4:
                            timeDiff = 5;
                            break;
                    }

                    if (timeDiff != null) {
                        Notification notification = NotificationHelper.createNotification(getContext(), "Activité imminente!", String.format(Locale.CANADA_FRENCH, "%s commence dans %d minutes!", item.getName(), timeDiff));
                        NotificationHelper.scheduleNotification(getContext(), notification, notifId, item.getStartTime().minusMinutes(timeDiff).getMillis());
                    } else {
                        NotificationHelper.cancelNotification(getContext(), notifId);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    NotificationHelper.cancelNotification(getContext(), item.getName().hashCode());
                }
            });

            ResultsListAdapter adapter = new ResultsListAdapter(getContext(), item.getResults());

            ListView resultsListView = (ListView) dialogView.findViewById(R.id.dialog_list_view);

            resultsListView.setAdapter(adapter);

            resultsListView.setEmptyView(dialogView.findViewById(R.id.dialog_list_view_empty));

            builder.setView(dialogView);

            builder.setTitle(item.getName());
        } else {
            builder.setTitle("Woupelai");
            builder.setMessage("Une erreur s'est produite. Veuillez réessayer.");
        }

        builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
