package com.example.seminar1314finalizat.utils.observer;

import com.example.seminar1314finalizat.utils.events.Event;

public interface Observable <E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notify(E t);
}
