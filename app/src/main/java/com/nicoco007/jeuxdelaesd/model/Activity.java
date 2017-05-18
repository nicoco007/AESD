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

package com.nicoco007.jeuxdelaesd.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity {
    private int id;
    private String name;
    private DateTime startTime;
    private DateTime endTime;
    private ArrayList<Result> results;
    private Location parentLocation;

    public Activity(int id, String name, DateTime startTime, DateTime endTime, ArrayList<Result> results) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.results = results;
    }

    public static Activity fromJson(JSONObject obj) {
        Activity activity = null;

        try {
            JSONArray resultsArray = obj.getJSONArray("results");
            ArrayList<Result> results = new ArrayList<>();

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject activityObject = resultsArray.getJSONObject(i);
                results.add(Result.fromJson(activityObject));
            }

            activity = new Activity(
                obj.getInt("id"),
                obj.getString("name"),
                new DateTime(obj.getString("start_time")).withZone(DateTimeZone.getDefault()),
                new DateTime(obj.getString("end_time")).withZone(DateTimeZone.getDefault()),
                results
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return activity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public Location getParentLocation() {
        return parentLocation;
    }

    public void setParentLocation(Location parentLocation) {
        this.parentLocation = parentLocation;
    }
}
