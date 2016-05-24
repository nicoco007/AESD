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

package com.nicoco007.jeuxdelaesd.model;

import android.view.View;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class MarkerCollection implements Iterable<Marker> {

    private ArrayList<Marker> markers = new ArrayList<>();


    public MarkerCollection() {

    }

    public void addMarker(Marker marker) {
        markers.add(marker);
    }

    public void addMarker(double latitude, double longitude, View view) {
        markers.add(new Marker(latitude, longitude, view));
    }

    public Marker getMarkerAtCoords(double latitude, double longitude) {


        for(int i = 0; i < markers.size(); i++) {
            if(markers.get(i).latitude == latitude && markers.get(i).longitude == longitude) {
                return markers.get(i);
            }
        }

        return null;
    }

    public void clear() {

        markers.clear();

    }

    @Override
    public Iterator<Marker> iterator() {

        return markers.iterator();

    }
}
