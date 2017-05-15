package com.nicoco007.jeuxdelaesd.events;

import com.nicoco007.jeuxdelaesd.onlinemodel.Location;

import java.util.ArrayList;

public class LocationsUpdatedEventArgs {
    private boolean successful;
    private ArrayList<Location> locations;

    public LocationsUpdatedEventArgs(boolean successful, ArrayList<Location> locations) {
        this.successful = successful;
        this.locations = locations;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }
}
