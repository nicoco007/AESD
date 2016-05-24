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

package com.nicoco007.jeuxdelaesd.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.fragment.ActivityDialogFragment;
import com.nicoco007.jeuxdelaesd.model.AesdDayActivity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivitiesListAdapter extends ArrayAdapter<AesdDayActivity> implements Filterable {

    private List<AesdDayActivity> allItems;
    private List<AesdDayActivity> currentItems = new ArrayList<>();

    private final ActivitiesFilter filter = new ActivitiesFilter();

    public ActivitiesListAdapter(Context context, List<AesdDayActivity> list) {

        super(context, 0, list);
        this.allItems = list;
        this.currentItems.addAll(list);

    }

    @Override
    public int getCount() {

        return currentItems.size();

    }

    @Override
    public AesdDayActivity getItem(int location) {

        return currentItems.get(location);

    }

    public long getItemId(int location) {

        return currentItems.get(location).hashCode();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final AesdDayActivity item = getItem(position);

        View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_activities, parent, false);
        } else {
            view = convertView;
        }

        final int id = view.hashCode();

        TextView itemName = (TextView)view.findViewById(R.id.lvia_activity);
        TextView itemTime = (TextView)view.findViewById(R.id.lvia_time);

        itemName.setText(item.getText());

        DateTimeZone timeZone = DateTimeZone.getDefault();
        DateTime localStartTime = new DateTime(item.getStartTime()).withZone(timeZone);
        DateTime localEndTime = new DateTime(item.getEndTime()).withZone(timeZone);

        itemTime.setText(String.format(
                Locale.CANADA_FRENCH,
                getContext().getString(R.string.activity_time),
                localStartTime.getHourOfDay(),
                localStartTime.getMinuteOfHour(),
                localEndTime.getHourOfDay(),
                localEndTime.getMinuteOfHour()
        ));

        final RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.lvia_layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialogFragment dialog = new ActivityDialogFragment();
                dialog.setItem(item);
                dialog.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "ala_dialog_" + Integer.toString(id));
            }
        });

        item.setView(view.findViewById(R.id.lvia_image_timer));

        return view;

    }

    public Filter getFilter() {

        return filter;

    }

    private class ActivitiesFilter extends Filter {

        @Override
        @SuppressWarnings("unchecked")
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {

                results.values = allItems;
                results.count = allItems.size();

            } else {

                ArrayList<AesdDayActivity> filteredItems = new ArrayList<>();

                for(AesdDayActivity item : allItems) {

                    if(item.getText().toLowerCase().contains(constraint.toString().toLowerCase())) {

                        filteredItems.add(item);

                    }

                }

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
                    currentItems.addAll((ArrayList<AesdDayActivity>)results.values);

                notifyDataSetChanged();

            } else {

                notifyDataSetInvalidated();

            }

        }

    }

}
