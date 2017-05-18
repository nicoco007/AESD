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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.adapter.ActivitiesListAdapter;
import com.nicoco007.jeuxdelaesd.adapter.SimpleSpinnerAdapter;
import com.nicoco007.jeuxdelaesd.events.EventListener;
import com.nicoco007.jeuxdelaesd.events.LocationsUpdatedEventArgs;
import com.nicoco007.jeuxdelaesd.helper.APICommunication;
import com.nicoco007.jeuxdelaesd.model.Activity;
import com.nicoco007.jeuxdelaesd.model.Location;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class ScheduleFragment extends Fragment {
    private static String TAG = "ScheduleFragment";

    private SwipeRefreshLayout refreshLayout;
    private ActivitiesListAdapter adapter;
    private Spinner sortSpinner;

    public ScheduleFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View self = inflater.inflate(R.layout.fragment_schedule, container, false);

        adapter = new ActivitiesListAdapter(this.getContext(), new ArrayList<Activity>());

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

        sortSpinner = (Spinner)self.findViewById(R.id.schedule_spinner_sort);

        ArrayList<String> items = new ArrayList<>();
        items.add("Heure de début/fin");
        items.add("Nom");

        sortSpinner.setAdapter(new SimpleSpinnerAdapter(getContext(), items));

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortActivities(position);
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

        APICommunication.onLocationsUpdatedEventHandler.addListener(new EventListener<LocationsUpdatedEventArgs>() {
            @Override
            public void handle(LocationsUpdatedEventArgs result) {
                onLocationsUpdated(result);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityDialogFragment dialog = new ActivityDialogFragment();
                dialog.setActivity(adapter.getItem(position));
                dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "ala_dialog_" + Long.toString(id));
            }
        });

        refreshLayout = (SwipeRefreshLayout) self.findViewById(R.id.swipe_container_activities);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                APICommunication.loadLocations(getContext(), true);
            }
        });

        return self;
    }

    public void onLocationsUpdated(final LocationsUpdatedEventArgs result) {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!result.isSuccessful()) {
                        // TODO: shove in helper class
                        Toast toast = Toast.makeText(getContext(), "Impossible de mettre à jour la liste d'activités. Veuillez vérifier votre connexion Internet puis réessayer.", Toast.LENGTH_LONG);
                        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);

                        if (textView != null)
                            textView.setGravity(Gravity.CENTER);

                        toast.show();

                        refreshLayout.setRefreshing(false);

                        return;
                    }

                    adapter.clear();

                    for (Location location : result.getLocations())
                        adapter.addAll(location.getActivities());

                    sortActivities(sortSpinner.getSelectedItemPosition());

                    adapter.notifyDataSetChanged();
                    adapter.notifyDataSetInvalidated();

                    refreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void sortActivities(int position) {
        switch (position) {
            case 0:
                adapter.sort(new Comparator<Activity>() {
                    @Override
                    public int compare(Activity a, Activity b) {
                        if (a.getStartTime() == b.getStartTime()) {
                            if (a.getEndTime() == b.getEndTime()) {
                                Collator collator = Collator.getInstance(Locale.CANADA_FRENCH);
                                collator.setStrength(Collator.PRIMARY);
                                return collator.compare(a.getName(), b.getName());
                            } else {
                                return (int) (a.getEndTime().getMillis() - b.getEndTime().getMillis());
                            }
                        } else {
                            return (int) (a.getStartTime().getMillis() - b.getStartTime().getMillis());
                        }
                    }
                });
            case 1:
                adapter.sort(new Comparator<Activity>() {
                    @Override
                    public int compare(Activity a, Activity b) {
                        Collator collator = Collator.getInstance(Locale.CANADA_FRENCH);
                        collator.setStrength(Collator.PRIMARY);
                        return collator.compare(a.getName(), b.getName());
                    }
                });
        }
    }
}
