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

package com.nicoco007.jeuxdelaesd.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.adapter.SimpleSpinnerAdapter;
import com.nicoco007.jeuxdelaesd.events.ShowMapCoordsEvent;
import com.nicoco007.jeuxdelaesd.model.AesdDayActivity;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Locale;

public class ActivityDialogFragment extends DialogFragment {

    private AesdDayActivity item;

    private EventBus eventBus = EventBus.getDefault();

    public ActivityDialogFragment() {}

    public void setItem(AesdDayActivity item) {

        this.item = item;

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(item != null) {

            DateTimeZone timeZone = DateTimeZone.getDefault();
            DateTime localStartTime = new DateTime(item.getStartTime()).withZone(timeZone);
            DateTime localEndTime = new DateTime(item.getEndTime()).withZone(timeZone);

            View dialogView = View.inflate(getContext(), R.layout.dialog_activity, null);

            Button button = (Button)dialogView.findViewById(R.id.dialog_activity_button);

            if(item.getMarker() == null) {
                button.setEnabled(false);
                button.setText(getString(R.string.location_unavailable));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getMarker() != null) {
                        eventBus.post(new ShowMapCoordsEvent(item.getMarker().latitude, item.getMarker().longitude));
                        getDialog().dismiss();
                    }
                }
            });

            TextView text = (TextView)dialogView.findViewById(R.id.dialog_activity_time);
            text.setText(String.format(Locale.CANADA_FRENCH, getContext().getString(R.string.activity_time_text), localStartTime.getHourOfDay(), localStartTime.getMinuteOfHour(), localEndTime.getHourOfDay(), localEndTime.getMinuteOfHour()));

            Spinner spinner = (Spinner)dialogView.findViewById(R.id.dialog_activity_spinner);

            ArrayList<String> spinnerItems = new ArrayList<>();
            spinnerItems.add("Jamais");
            spinnerItems.add("30 minutes avant");
            spinnerItems.add("15 minutes avant");
            spinnerItems.add("10 minutes avant");
            spinnerItems.add("5 minutes avant");

            spinner.setAdapter(new SimpleSpinnerAdapter(getContext(), spinnerItems));

            spinner.setSelection(item.getRemindIndex());

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch(position) {
                        case 0:
                            item.clearNotification();
                            break;
                        case 1:
                            item.setNotification(30 * 60 * 1000);
                            break;
                        case 2:
                            item.setNotification(15 * 60 * 1000);
                            break;
                        case 3:
                            item.setNotification(10 * 60 * 1000);
                            break;
                        case 4:
                            item.setNotification(5 * 60 * 1000);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    item.clearNotification();

                }
            });

            builder.setView(dialogView);

            builder.setTitle(item.getText());

        } else {

            builder.setTitle("Woupelai");
            builder.setMessage("Une erreur s'est produite.");

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
