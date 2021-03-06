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

package com.nicoco007.jeuxdelaesd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.model.Location;

import java.util.Collection;
import java.util.List;

public class BuildingsListAdapter extends ArrayAdapter<Location> {
    private List<Location> buildings;

    public BuildingsListAdapter(Context context, List<Location> list) {
        super(context, 0, list);
        this.buildings = list;
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public Location getItem(int location) {
        return buildings.get(location);
    }

    public long getItemId(int location) {
        return buildings.get(location).hashCode();
    }

    @Override
    public void add(@Nullable Location object) {
        buildings.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Location> collection) {
        buildings.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        buildings.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Location item = getItem(position);

        View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_buildings, parent, false);
        } else {
            view = convertView;
        }

        TextView textView = (TextView)view.findViewById(R.id.lvib_text);

        textView.setText(item.getName());

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new ShowMapCoordsEvent(item.getLatitude(), item.getLongitude()));
            }
        });*/

        return view;
    }
}
