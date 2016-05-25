package com.nicoco007.jeuxdelaesd.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nicoco007.jeuxdelaesd.Markers;
import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.adapter.BuildingsListAdapter;
import com.nicoco007.jeuxdelaesd.model.MarkerInfo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class BuildingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View self = inflater.inflate(R.layout.fragment_buildings, container, false);

        ArrayList<MarkerInfo> buildings = new ArrayList<>();

        buildings.add(Markers.CAFETERIA);
        buildings.add(Markers.GENERAL_QUARTERS);
        buildings.add(Markers.ACADEMIE_CATHOLIQUE_MERE_TERESA);
        buildings.add(Markers.JEAN_VANIER);
        buildings.add(Markers.MONSEIGNEUR_DE_CHARBONNEL);
        buildings.add(Markers.NOUVELLE_ALLIANCE);
        buildings.add(Markers.PERE_RENE_DE_GALINEE);
        buildings.add(Markers.RENAISSANCE);
        buildings.add(Markers.SAINT_CHARLES_GARNIER);
        buildings.add(Markers.SAINT_FRERE_ANDRE);
        buildings.add(Markers.SAINTE_FAMILLE);
        buildings.add(Markers.SAINTE_TRINITE);
        buildings.add(Markers.CONVENIENCE_STORE);
        buildings.add(Markers.INFIRMARY);
        buildings.add(Markers.REFEREE_TENT);
        buildings.add(Markers.TEACHERS_LOUNGE);

        Collections.sort(buildings, new Comparator<MarkerInfo>() {
            @Override
            public int compare(MarkerInfo a, MarkerInfo b) {
                Collator collator = Collator.getInstance(Locale.CANADA_FRENCH);
                collator.setStrength(Collator.PRIMARY);
                return collator.compare(a.getName(), b.getName());
            }
        });

        BuildingsListAdapter listAdapter = new BuildingsListAdapter(getContext(), buildings);

        ListView buildingsListView = (ListView)self.findViewById(R.id.listview_buildings);

        buildingsListView.setAdapter(listAdapter);

        return self;

    }

}
