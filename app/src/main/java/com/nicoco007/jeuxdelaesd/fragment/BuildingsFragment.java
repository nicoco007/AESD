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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.adapter.BuildingsListAdapter;
import com.nicoco007.jeuxdelaesd.events.EventListener;
import com.nicoco007.jeuxdelaesd.events.LocationsUpdatedEventArgs;
import com.nicoco007.jeuxdelaesd.events.ShowMapCoordsEvent;
import com.nicoco007.jeuxdelaesd.helper.APICommunication;
import com.nicoco007.jeuxdelaesd.model.Location;
import com.nicoco007.jeuxdelaesd.model.LocationType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BuildingsFragment extends Fragment {
    private BuildingsListAdapter adapter;
    private EventBus eventBus = EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View self = inflater.inflate(R.layout.fragment_buildings, container, false);

        adapter = new BuildingsListAdapter(getContext(), new ArrayList<Location>());

        ListView buildingsListView = (ListView) self.findViewById(R.id.listview_buildings);

        buildingsListView.setAdapter(adapter);

        buildingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location item = adapter.getItem(position);

                eventBus.post(new ShowMapCoordsEvent(item.getLatitude(), item.getLongitude()));
            }
        });

        APICommunication.onLocationsUpdatedEventHandler.addListener(new EventListener<LocationsUpdatedEventArgs>() {
            @Override
            public void handle(LocationsUpdatedEventArgs result) {
                onLocationsUpdated(result);
            }
        });

        return self;
    }

    private void onLocationsUpdated(final LocationsUpdatedEventArgs result) {
        if (isAdded() && result.isSuccessful()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.clear();

                    ArrayList<Location> locations = new ArrayList<>();

                    for (Location location : result.getLocations())
                        if (location.getType() == LocationType.CAMP || location.getType() == LocationType.OTHER)
                            locations.add(location);

                    adapter.addAll(locations);
                }
            });
        }
    }
}
