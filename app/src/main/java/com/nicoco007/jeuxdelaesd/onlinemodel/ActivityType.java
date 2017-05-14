package com.nicoco007.jeuxdelaesd.onlinemodel;

public enum ActivityType {
    ACTIVITY (0), CAMP (1), OTHER (2);

    private final int id;

    ActivityType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
