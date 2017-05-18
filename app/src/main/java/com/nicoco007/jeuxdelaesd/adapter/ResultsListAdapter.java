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
import com.nicoco007.jeuxdelaesd.model.Result;

import java.util.List;

public class ResultsListAdapter extends ArrayAdapter<Result> {
    public ResultsListAdapter(@NonNull Context context, @NonNull List<Result> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Result result = getItem(position);

        View view;

        if (convertView == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_results, parent, false);
        else
            view = convertView;

        TextView key = (TextView) view.findViewById(R.id.lvir_key);
        TextView value = (TextView) view.findViewById(R.id.lvir_value);

        key.setText(result.getType().getName());
        value.setText(result.getSchool().getName());

        return view;
    }
}
