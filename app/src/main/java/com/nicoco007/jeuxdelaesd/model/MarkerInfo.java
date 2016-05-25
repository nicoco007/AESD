package com.nicoco007.jeuxdelaesd.model;

import com.nicoco007.jeuxdelaesd.model.Coordinates;

public class MarkerInfo {

    private Coordinates coordinates;
    private String name;

    public MarkerInfo(double latitude, double longitude) {

        this(latitude, longitude, null);

    }

    public MarkerInfo(double latitude, double longitude, String name) {

        this.coordinates = new Coordinates(latitude, longitude);
        this.name = name;

    }

    public Coordinates getCoordinates() {

        return coordinates;

    }

    public String getName() {

        return name;

    }

}
