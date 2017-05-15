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
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.activity.MapActivity;
import com.nicoco007.jeuxdelaesd.events.EventListener;
import com.nicoco007.jeuxdelaesd.events.LocationsUpdatedEventArgs;
import com.nicoco007.jeuxdelaesd.helper.APICommunication;
import com.nicoco007.jeuxdelaesd.model.MarkerCollection;
import com.nicoco007.jeuxdelaesd.onlinemodel.Activity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements PermissionsListener {

    public static final int REQUEST_PERMISSION_GPS = 1;
    public static final String TAG = "MapFragment";

    private View self;                                          // save current view into variable
    private EventBus eventBus = EventBus.getDefault();          // eventbus for focusing a point
    private MarkerCollection markers = new MarkerCollection();  // tileview markers

    private MapView mapView;
    private MapboxMap map;

    private LocationEngine locationEngine;

    private static final int PERMISSIONS_LOCATION = 0;

    /**
     * school
     * 43.918942, -78.957837
     * 43.916957, -78.955833
     *
     * robinhood2
     * 43.927910, -79.221447
     * 43.923517, -79.216322
     */

    //private static final double minLat = 43.899141;
    //private static final double maxLat = 43.897313;
    //private static final double minLong = -78.956643;
    //private static final double maxLong = -78.954123;

    // robinhood2
    private static final double minLat = 43.92336814129122;
    private static final double maxLat = 43.92806636084152;
    private static final double minLong = -79.22172546386719;
    private static final double maxLong = -79.21623229980469;

    public MapFragment() {
        //eventBus.register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // init with access token - we need to do this now or inflating will fail
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));

        // inflate view
        self = inflater.inflate(R.layout.fragment_map, container, false);

        // get location engine object for later use
        locationEngine = LocationSource.getLocationEngine(getContext());
        locationEngine.activate();

        mapView = (MapView) self.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                Log.d(TAG, "onMapReady");

                map = mapboxMap;

                map.setCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng((minLat + maxLat) / 2, (minLong + maxLong) / 2))
                        .zoom(15)
                        .build());

                // doesnt work in options for some reason
                map.setMinZoomPreference(15);
                map.setMaxZoomPreference(18);
                map.setMyLocationEnabled(true);

                map.setLatLngBoundsForCameraTarget(new LatLngBounds.Builder().include(new LatLng(minLat, minLong)).include(new LatLng(maxLat, maxLong)).build());

                enableLocation();

                APICommunication.loadLocations(getContext());
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

    private void onLocationsUpdated(LocationsUpdatedEventArgs result) {
        if (!result.isSuccessful())
            return;

        // make sure we can actually modify the map
        if (map != null && isAdded()) {

            // remove all markers
            map.clear();

            // iterate through locations
            for (com.nicoco007.jeuxdelaesd.onlinemodel.Location location : result.getLocations()) {
                ArrayList<String> activityNames = new ArrayList<>();

                for (Activity activity : location.getActivities())
                    activityNames.add("\u2022 " + activity.getName());

                String snippet = TextUtils.join("\n", activityNames);

                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                String title = location.getName();

                MarkerOptions options = new MarkerOptions()
                        .position(pos)
                        .title(title)
                        .snippet(snippet);

                map.addMarker(options);
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void enableLocation() {
        PermissionsManager permissionsManager = new PermissionsManager(this);

        if (!PermissionsManager.areLocationPermissionsGranted(getContext())) {
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "Cette application nécessite votre position pour l'afficher sur la carte des lieux.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        } else {
            Toast.makeText(getContext(), "Vous avez refusé la demande d'accès à la position.", Toast.LENGTH_LONG).show();
        }
    }
}
