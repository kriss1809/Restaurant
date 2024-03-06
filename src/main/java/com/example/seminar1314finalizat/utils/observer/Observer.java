package com.example.seminar1314finalizat.utils.observer;


import com.example.seminar1314finalizat.utils.events.Event;

public interface Observer <E extends Event> {
    void update(E e);
}