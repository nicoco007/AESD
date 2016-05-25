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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.nicoco007.jeuxdelaesd.Activities;
import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.adapter.ActivitiesListAdapter;
import com.nicoco007.jeuxdelaesd.adapter.SimpleSpinnerAdapter;
import com.nicoco007.jeuxdelaesd.model.DayActivity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private static String TAG = "ScheduleFragment";

    public ScheduleFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View self = inflater.inflate(R.layout.fragment_schedule, container, false);

        final ActivitiesListAdapter adapter = new ActivitiesListAdapter(this.getContext(), Activities.get(getContext()));

        final ListView listView = (ListView)self.findViewById(R.id.listview_activities);

        listView.setAdapter(adapter);

        listView.setEmptyView(self.findViewById(R.id.empty));

        final EditText filterText = (EditText)self.findViewById(R.id.filter_activities);

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final Spinner sortSpinner = (Spinner)self.findViewById(R.id.schedule_spinner_sort);

        ArrayList<String> items = new ArrayList<>();
        items.add("Heure de début/fin");
        items.add("Nom");

        sortSpinner.setAdapter(new SimpleSpinnerAdapter(getContext(), items));

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        Log.i(TAG, "attempting to sort by time");
                        Collections.sort(Activities.get(getContext()), new Comparator<DayActivity>() {
                            @Override
                            public int compare(DayActivity a, DayActivity b) {
                                long startTimeSort = a.getStartTime() - b.getStartTime();
                                if(startTimeSort != 0) return (int)startTimeSort;

                                long endTimeSort = a.getEndTime() - b.getEndTime();
                                return (int)endTimeSort;
                            }
                        });
                        break;

                    case 1:
                        Collections.sort(Activities.get(getContext()), new Comparator<DayActivity>() {
                            @Override
                            public int compare(DayActivity a, DayActivity b) {
                                Collator collator = Collator.getInstance(Locale.CANADA_FRENCH);
                                collator.setStrength(Collator.PRIMARY);
                                return collator.compare(a.getText(), b.getText());
                            }
                        });
                        break;
                }

                adapter.getFilter().filter(filterText.getText());
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return self;
    }

}
