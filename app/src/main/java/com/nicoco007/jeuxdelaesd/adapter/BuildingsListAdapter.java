package com.nicoco007.jeuxdelaesd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.events.ShowMapCoordsEvent;
import com.nicoco007.jeuxdelaesd.model.MarkerInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BuildingsListAdapter extends ArrayAdapter<MarkerInfo> {
    private List<MarkerInfo> buildings;
    private EventBus eventBus = EventBus.getDefault();

    public BuildingsListAdapter(Context context, List<MarkerInfo> list) {
        super(context, 0, list);
        this.buildings = list;
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public MarkerInfo getItem(int location) {
        return buildings.get(location);
    }

    public long getItemId(int location) {
        return buildings.get(location).hashCode();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final MarkerInfo item = getItem(position);

        View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_buildings, parent, false);
        } else {
            view = convertView;
        }

        TextView textView = (TextView)view.findViewById(R.id.lvib_text);

        textView.setText(item.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new ShowMapCoordsEvent(item.getCoordinates()));
            }
        });

        return view;
    }
}
