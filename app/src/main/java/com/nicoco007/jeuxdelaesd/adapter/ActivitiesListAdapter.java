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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.onlinemodel.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ActivitiesListAdapter extends ArrayAdapter<Activity> implements Filterable {
    private static final String TAG = "ActivitiesListAdapter";

    private List<Activity> allItems;
    private List<Activity> currentItems = new ArrayList<>();

    private final ActivitiesFilter filter = new ActivitiesFilter();

    public ActivitiesListAdapter(Context context, List<Activity> list) {
        super(context, 0, list);

        this.allItems = list;
        this.currentItems.addAll(list);
    }

    @Override
    public int getCount() {
        return currentItems.size();
    }

    @Override
    public Activity getItem(int location) {
        return currentItems.get(location);
    }

    public long getItemId(int location) {
        return currentItems.get(location).hashCode();
    }

    @Override
    public void add(@Nullable Activity object) {
        allItems.add(object);
        filter.refresh();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Activity> collection) {
        allItems.addAll(collection);
        filter.refresh();
    }

    @Override
    public void clear() {
        allItems.clear();
        filter.refresh();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Activity item = getItem(position);

        View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_activities, parent, false);
        } else {
            view = convertView;
        }

        TextView itemName = (TextView)view.findViewById(R.id.lvia_activity);
        TextView itemTime = (TextView)view.findViewById(R.id.lvia_time);

        itemName.setText(item.getName());

        itemTime.setText(String.format(
                Locale.CANADA_FRENCH,
                getContext().getString(R.string.activity_time),
                item.getStartTime().getHourOfDay(),
                item.getStartTime().getMinuteOfHour(),
                item.getEndTime().getHourOfDay(),
                item.getEndTime().getMinuteOfHour()
        ));

        return view;
    }

    @NonNull
    public Filter getFilter() {
        return filter;
    }

    private class ActivitiesFilter extends Filter {
        private CharSequence prevConstraint;

        protected void refresh() {
            filter(prevConstraint);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected FilterResults performFiltering(CharSequence constraint) {
            prevConstraint = constraint;

            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = allItems;
                results.count = allItems.size();
            } else {
                ArrayList<Activity> filteredItems = new ArrayList<>();

                for(Activity item : allItems)
                    if(item.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                        filteredItems.add(item);

                results.values = filteredItems;
                results.count = filteredItems.size();
            }

            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            currentItems.clear();

            if(results.count > 0) {
                if(results.values instanceof ArrayList<?>)
                    currentItems.addAll((ArrayList<Activity>) results.values);

                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
