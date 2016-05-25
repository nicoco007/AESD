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

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicoco007.jeuxdelaesd.Activities;
import com.nicoco007.jeuxdelaesd.Markers;
import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.events.ShowMapCoordsEvent;
import com.nicoco007.jeuxdelaesd.model.AesdDayActivity;
import com.nicoco007.jeuxdelaesd.model.Callout;
import com.nicoco007.jeuxdelaesd.model.CalloutInfo;
import com.nicoco007.jeuxdelaesd.model.Coordinates;
import com.nicoco007.jeuxdelaesd.model.Marker;
import com.nicoco007.jeuxdelaesd.model.MarkerCollection;
import com.nicoco007.jeuxdelaesd.model.PointD;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.paths.CompositePathView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {

    public static final int REQUEST_PERMISSION_GPS = 1;
    public static final String TAG = "MapFragment";

    private View self;                                          // save current view into variable
    private EventBus eventBus = EventBus.getDefault();          // eventbus for focusing a point
    private TileView tileView;                                  // global tileview instance
    private MarkerCollection markers = new MarkerCollection();  // tileview markers
    private ImageView currentLocationMarker;                    // current location marker

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

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        self = inflater.inflate(R.layout.fragment_map, container, false);

        eventBus.register(this);

        RelativeLayout relativeLayout = (RelativeLayout)self.findViewById(R.id.map_activity_layout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tileView = new TileView(getContext());

        tileView.setSize(2048, 2432);
        tileView.addDetailLevel(1f, "robinhood/19/tile-%d_%d.jpg");
        tileView.addDetailLevel(2f, "robinhood/20/tile-%d_%d.jpg");
        tileView.setScaleLimits(1f, 3f);
        tileView.setShouldRenderWhilePanning(true);
        tileView.defineBounds(minLong, maxLat, maxLong, minLat);

        tileView.setMarkerTapListener(new MarkerLayout.MarkerTapListener() {
            @Override
            public void onMarkerTap(View view, int x, int y) {

                Callout callout = new Callout(view.getContext());

                CalloutInfo calloutInfo = (CalloutInfo)view.getTag();
                PointD position = calloutInfo.getPosition();

                if(tileView.getScale() < 2f) {
                    tileView.slideToAndCenterWithScale(position.x, position.y, 2f);
                } else {
                    tileView.slideToAndCenter(position.x, position.y);
                }

                tileView.addCallout(callout, position.x, position.y, -0.5f, -1.0f);

                if(calloutInfo.getTitle() == null) {

                    ArrayList<String> activityNames = new ArrayList<>();

                    for(AesdDayActivity activity : Activities.get(getContext())) {

                        if(activity.getMarker().latitude == position.y && activity.getMarker().longitude == position.x) {

                            activityNames.add(activity.getText());

                        }

                    }

                    if(activityNames.size() > 0) {

                        String concat = activityNames.get(0);

                        for(int i = 1; i < activityNames.size(); i++) {
                            concat += ",\n" + activityNames.get(i);
                        }

                        callout.setTitle(concat);

                    } else {

                        callout.setTitle("(rien)");

                    }

                } else {

                    callout.setTitle(calloutInfo.getTitle());

                }

                callout.transitionIn();

            }
        });

        currentLocationMarker = new ImageView(self.getContext());
        currentLocationMarker.setImageResource(R.drawable.ic_my_location_blue_36dp);
        currentLocationMarker.setVisibility(View.INVISIBLE);
        tileView.addMarker(currentLocationMarker, 0.5d, 0.5d, -0.5f, -0.5f);

        addPin(Markers.ARCHERY);
        addPin(Markers.BASEBALL_EAST,               "Terrain de balle-molle 2");
        addPin(Markers.BASEBALL_SOUTH);
        addPin(Markers.BASEBALL_WEST,               "Terrain de balle-molle 1");
        addPin(Markers.BASKETBALL_DOUBLE,           "Terrains de basketball 1 et 2");
        addPin(Markers.BASKETBALL_SINGLE,           "Terrain de basketball 3");
        addPin(Markers.BUILDING_CLUMP_EAST);
        addPin(Markers.CAFETERIA,                   "Cafétéria");
        addPin(Markers.COVERED_DINING_ROOM);
        addPin(Markers.GENERAL_QUARTERS,            "Quartier général de l'AESDCCS");
        addPin(Markers.IS_THIS_EVEN_A_FIELD);
        addPin(Markers.MINI_PUTT_BUILDING);
        addPin(Markers.NORTH_EAST_DOME);
        addPin(Markers.OUTSIDE_STAGE);
        addPin(Markers.POOL);
        addPin(Markers.SMALL_SHITTY_BUILDING);
        addPin(Markers.SOCCER_FIELDS,               "Terrains de soccer");
        addPin(Markers.SOUTH_EAST_PORTABLE);
        //addPin(Markers.SOUTH_WEST_BUILDING);
        addPin(Markers.SOUTH_WEST_DOME);
        addPin(Markers.SOUTH_WEST_PORTABLE_BOTTOM);
        addPin(Markers.SOUTH_WEST_PORTABLE_TOP);
        addPin(Markers.STAGE_DOME,                  "Tennis de table\nDanse\nCérémonie de clôture");
        addPin(Markers.TENNIS_COURTS,               "Tennis\nBallon-chasseur");
        addPin(Markers.VOLLEYBALL);

        layoutParams.addRule(RelativeLayout.BELOW, R.id.info_layout);
        layoutParams.addRule(RelativeLayout.ABOVE, R.id.incompetence);
        relativeLayout.addView(tileView, layoutParams);

        if (ContextCompat.checkSelfPermission(self.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION_GPS);
        } else {
            requestLocation();
        }

        Button resetButton = (Button)self.findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Marker marker : markers)
                    marker.view.setVisibility(View.VISIBLE);

                v.setVisibility(View.GONE);
            }
        });

        return self;
    }

    @Subscribe
    public void onEvent(ShowMapCoordsEvent e) {

        /*for(Marker marker : markers)
            if(marker.view instanceof ImageView)
                ((ImageView)marker.view).setImageResource(R.drawable.ic_place_grey_36px);*/

        self.findViewById(R.id.reset_button).setVisibility(View.VISIBLE);

        for(Marker marker : markers)
            marker.view.setVisibility(View.GONE);

        tileView.scrollToAndCenter(e.longitude, e.latitude);

        View view = markers.getMarkerAtCoords(e.latitude, e.longitude).view;
        view.setVisibility(View.VISIBLE);

        /*if(view instanceof ImageView)
            ((ImageView)view).setImageResource(R.drawable.ic_place_red_36px);*/

        TranslateAnimation anim = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, -0.5f
        );

        view.invalidate();
        view.clearAnimation();
        view.invalidate();
        anim.setDuration(300);
        anim.setRepeatCount(5);
        anim.setRepeatMode(TranslateAnimation.REVERSE);
        anim.setInterpolator(new DecelerateInterpolator(1.5f));
        view.startAnimation(anim);
        view.invalidate();

    }

    private void setCurrentLocation(double latitude, double longitude, int accuracy) {

        currentLocationMarker.setVisibility(View.VISIBLE);
        tileView.moveMarker(currentLocationMarker, longitude, latitude);
        TextView textView = (TextView)self.findViewById(R.id.coordinates);
        textView.setText(String.format(Locale.CANADA, "Latitude: %f; Longitude: %f ±%dm", latitude, longitude, accuracy));

    }

    private View addPin(Coordinates location) {

        return addPin(location, null);

    }

    private View addPin(Coordinates location, String title) {

        ImageView imageView = new ImageView(self.getContext());
        imageView.setImageResource(R.drawable.ic_place_red_36px);
        View view = tileView.addMarker(imageView, location.longitude, location.latitude, -0.5f, -1.0f);

        view.setTag(new CalloutInfo(title, location.longitude, location.latitude));

        markers.addMarker(location.latitude, location.longitude, view);

        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    requestLocation();

                break;
            default:
                break;
        }
    }

    private void requestLocation() {

        Log.i(TAG, "Attempting to request location...");

        final TextView coordsTextView = (TextView) self.findViewById(R.id.coordinates);
        final TextView statusTextView = (TextView) self.findViewById(R.id.status);

        if(isAdded()) coordsTextView.setText(getString(R.string.attempting_load_gps));

        if (ContextCompat.checkSelfPermission(self.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            final LocationManager location = (LocationManager) self.getContext().getSystemService(Context.LOCATION_SERVICE);

            GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int status) {

                    int satellites = 0;
                    int satellitesInFix = 0;

                    for (GpsSatellite sat : location.getGpsStatus(null).getSatellites()) {
                        if (sat.usedInFix()) {
                            satellitesInFix++;
                        }
                        satellites++;
                    }

                    if(isAdded()) statusTextView.setText(String.format(getString(R.string.satellite_amount), satellitesInFix, satellites));

                }
            };

            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    if(isAdded()) self.findViewById(R.id.info_layout).setVisibility(View.GONE);

                    Log.d(TAG, "Location changed");

                    setCurrentLocation(location.getLatitude(), location.getLongitude(), (int)location.getAccuracy());

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                    Log.i(TAG, "Location listener status changed");

                    switch (status) {

                        case LocationProvider.AVAILABLE:
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        case LocationProvider.OUT_OF_SERVICE:
                            if(isAdded()) self.findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
                            if(isAdded()) coordsTextView.setText(getString(R.string.loading_gps));
                            break;

                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.i(TAG, "Location provider enabled");
                    if(isAdded()) self.findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.i(TAG, "Location provider disabled");
                    if(isAdded()) self.findViewById(R.id.info_layout).setVisibility(View.GONE);
                }
            };

            location.addGpsStatusListener(gpsListener);
            location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

            if(isAdded()) self.findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
            if(isAdded()) coordsTextView.setText(getString(R.string.loading_gps));

        } else {

            coordsTextView.setText(R.string.permission_failed);

            if(isAdded()) self.findViewById(R.id.info_layout).setVisibility(View.GONE);

        }

    }

}
