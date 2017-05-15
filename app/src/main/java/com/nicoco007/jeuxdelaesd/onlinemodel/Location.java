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

package com.nicoco007.jeuxdelaesd.onlinemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Location {
    private int id;
    private String name;
    private LocationType type;
    private double latitude;
    private double longitude;
    private ArrayList<Activity> activities;

    public Location(int id, String name, LocationType type, double latitude, double longitude, ArrayList<Activity> activities) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.activities = activities;
    }

    public static Location fromJson(JSONObject obj) {
        Location location = null;

        try {
            JSONArray activitiesArray = obj.getJSONArray("activities");
            ArrayList<Activity> activities = new ArrayList<>();

            for (int i = 0; i < activitiesArray.length(); i++) {
                JSONObject activityObject = activitiesArray.getJSONObject(i);
                activities.add(Activity.fromJson(activityObject));
            }

            location = new Location(
                    obj.getInt("id"),
                    obj.getString("name"),
                    LocationType.values()[obj.getInt("type")],
                    obj.getDouble("latitude"),
                    obj.getDouble("longitude"),
                    activities
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocationType getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }
}
