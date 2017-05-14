package com.nicoco007.jeuxdelaesd.events;

import java.util.ArrayList;
import java.util.logging.Handler;

public class EventHandler<T> {
    private ArrayList<EventListener<T>> listeners = new ArrayList<>();

    public void addListener(EventListener<T> listener) {
        listeners.add(listener);
    }

    public void raise(T result) {
        for (EventListener<T> listener : listeners) {
            listener.handle(result);
        }
    }
}
