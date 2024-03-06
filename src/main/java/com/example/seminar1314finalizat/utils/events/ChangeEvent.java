package com.example.seminar1314finalizat.utils.events;


import com.example.seminar1314finalizat.domain.Entity;

public class ChangeEvent<E extends Entity> implements Event {
    private ChangeEventType type;
    private E data,oldData;

    public ChangeEvent(ChangeEventType type, E data) {
        this.type = type;
        this.data = data;
    }

}