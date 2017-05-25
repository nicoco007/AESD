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
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.events.EventListener;
import com.nicoco007.jeuxdelaesd.events.LocationsUpdatedEventArgs;
import com.nicoco007.jeuxdelaesd.events.ShowMapCoordsEvent;
import com.nicoco007.jeuxdelaesd.helper.APICommunication;
import com.nicoco007.jeuxdelaesd.helper.NotificationHelper;
import com.nicoco007.jeuxdelaesd.model.Activity;
import com.nicoco007.jeuxdelaesd.model.Location;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements PermissionsListener {
    public static final String TAG = "MapFragment";

    private MapView mapView;
    private MapboxMap map;
    private OfflineManager offlineManager;

    // robin hood
    private static final double minLat = 43.92336814129122;
    private static final double maxLat = 43.92806636084152;
    private static final double minLong = -79.22172546386719;
    private static final double maxLong = -79.21623229980469;

    public static final Charset JSON_CHARSET = Charset.forName("UTF-8");
    public static final String JSON_FIELD_REGION_ID = "field_region_id";
    public static final String JSON_FIELD_REGION_NAME = "field_region_name";

    public MapFragment() {
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Subscribe
    public void onShowMapCoordsEvent(ShowMapCoordsEvent event) {
        if (map != null) {
            map.deselectMarkers();

            for (Marker marker : map.getMarkers()) {
                if (marker.getPosition().getLatitude() == event.getPosition().getLatitude() && marker.getPosition().getLongitude() == event.getPosition().getLongitude()) {
                    map.selectMarker(marker);

                    CameraPosition position = new CameraPosition.Builder()
                            .target(event.getPosition())
                            .zoom(16)
                            .build();

                    map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationHelper.loadNotifications(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // init with access token - we need to do this now or inflating will fail
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));

        // inflate view
        final View self = inflater.inflate(R.layout.fragment_map, container, false);

        // get location engine object for later use
        LocationEngine locationEngine = LocationSource.getLocationEngine(getContext());
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

                map.setLatLngBoundsForCameraTarget(new LatLngBounds.Builder().include(new LatLng(minLat, minLong)).include(new LatLng(maxLat, maxLong)).build());

                enableLocation();

                APICommunication.loadLocations(getContext());

                checkOfflineRegions();
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

    @SuppressWarnings("FieldCanBeLocal")
    private final String regionName = "Camp Robin Hood";
    private final String regionId = "Q2FtcCBSb2JpbiBIb29k";

    private void checkOfflineRegions() {
        // set up the offline manager
        offlineManager = OfflineManager.getInstance(getContext());

        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(OfflineRegion[] offlineRegions) {
                Log.i(TAG, "Got list of offline regions");

                boolean found = false;

                for (OfflineRegion offlineRegion : offlineRegions) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(offlineRegion.getMetadata(), JSON_CHARSET));

                        final String id = jsonObject.has(JSON_FIELD_REGION_ID) ? jsonObject.getString(JSON_FIELD_REGION_ID) : null;
                        final String name = jsonObject.has(JSON_FIELD_REGION_NAME) ? jsonObject.getString(JSON_FIELD_REGION_NAME) : null;

                        Log.i(TAG, String.format("Found region with ID \"%s\": %s", id, name));

                        if (id != null && id.equals(regionId)) {
                            found = true;
                            break;
                        } else {
                            offlineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                                @Override
                                public void onDelete() {
                                    Log.i(TAG, String.format("Successfully deleted region with ID \"%s\"", id));
                                }

                                @Override
                                public void onError(String error) {
                                    Log.e(TAG, String.format("Failed to delete region with ID \"%s\"", id));
                                }
                            });
                        }
                    } catch (JSONException ex) {
                        Log.w(TAG, "Failed to parse metadata as JSON");
                        ex.printStackTrace();
                    }
                }

                if (!found) {
                    Log.i(TAG, "Did not find required offline region");
                    downloadOfflineRegions();
                } else {
                    Log.i(TAG, "Found required offline region");
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Failed to list offline regions");
            }
        });
    }

    private void downloadOfflineRegions() {
        Log.i(TAG, "Downloading offline regions");

        // create a bounding box for the offline region
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(minLat, minLong))
                .include(new LatLng(maxLat, maxLong))
                .build();

        // define the offline region
        final OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                map.getStyleUrl(),
                latLngBounds,
                map.getMinZoomLevel(),
                map.getMaxZoomLevel(),
                getResources().getDisplayMetrics().density
        );

        // set the metadata
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(JSON_FIELD_REGION_ID, regionId);
            jsonObject.put(JSON_FIELD_REGION_NAME, regionName);

            String json = jsonObject.toString();
            byte[] metadata = json.getBytes(JSON_CHARSET);

            // create the region asynchronously
            offlineManager.createOfflineRegion(
                    definition,
                    metadata,
                    new OfflineManager.CreateOfflineRegionCallback() {
                        @Override
                        public void onCreate(OfflineRegion offlineRegion) {
                            offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

                            offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                                @Override
                                public void onStatusChanged(OfflineRegionStatus status) {
                                    double percentage = status.getRequiredResourceCount() >= 0 ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) : 0.0;

                                    if (status.isComplete()) {
                                        Log.i(TAG, "Region downloaded successfully.");
                                    } else if (status.isRequiredResourceCountPrecise()) {
                                        Log.v(TAG, String.format("Downloading region: %d/%d resources (%.2f%%)", status.getCompletedResourceCount(), status.getRequiredResourceCount(), percentage));
                                    }
                                }

                                @Override
                                public void onError(OfflineRegionError error) {
                                    Log.e(TAG, "onError reason: " + error.getReason());
                                    Log.e(TAG, "onError message: " + error.getMessage());
                                }

                                @Override
                                public void mapboxTileCountLimitExceeded(long limit) {
                                    Log.e(TAG, "Mapbox tile count limit exceeded: " + limit);
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, "Error: " + error);
                        }
                    }
            );
        } catch (JSONException ex) {
            Log.e(TAG, "Failed to encode metadata: " + ex.getMessage());
        }
    }

    private void onLocationsUpdated(final LocationsUpdatedEventArgs result) {
        // make sure we can actually modify the map
        if (result.isSuccessful() && map != null && isAdded() && getContext() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // remove all markers
                    map.clear();

                    // iterate through locations
                    for (Location location : result.getLocations()) {
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
            });
        }
    }

    @SuppressWarnings("MissingPermission")
    private void enableLocation() {
        if (getContext() != null) {
            PermissionsManager permissionsManager = new PermissionsManager(this);

            if (!PermissionsManager.areLocationPermissionsGranted(getContext())) {
                permissionsManager.requestLocationPermissions(getActivity());
            } else {
                map.setMyLocationEnabled(true);
            }
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
        NotificationHelper.saveNotifications(getContext());
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
