package com.nicoco007.jeuxdelaesd.onlinemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Location {
    private int id;
    private String name;
    private ActivityType type;
    private double latitude;
    private double longitude;
    private ArrayList<Activity> activities;

    public Location(int id, String name, ActivityType type, double latitude, double longitude, ArrayList<Activity> activities) {
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
                    ActivityType.values()[obj.getInt("type")],
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

    public ActivityType getType() {
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
