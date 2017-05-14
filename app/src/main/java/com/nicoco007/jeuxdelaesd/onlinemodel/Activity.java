package com.nicoco007.jeuxdelaesd.onlinemodel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity {
    private int id;
    private String name;
    private DateTime startTime;
    private DateTime endTime;
    private ArrayList<Result> results;

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
            activity = new Activity(
                    obj.getInt("id"),
                    obj.getString("name"),
                    new DateTime(obj.getString("start_time")).withZone(DateTimeZone.getDefault()),
                    new DateTime(obj.getString("end_time")).withZone(DateTimeZone.getDefault()),
                    new ArrayList<Result>()
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
}
