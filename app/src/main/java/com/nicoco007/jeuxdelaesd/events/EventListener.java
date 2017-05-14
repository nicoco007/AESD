package com.nicoco007.jeuxdelaesd.events;

public interface EventListener<T> {
    void handle(T result);
}
