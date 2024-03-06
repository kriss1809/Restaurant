package com.example.seminar1314finalizat.domain;

import java.util.Objects;

public class Waiter extends Entity<Long>{

    private String name;

    public Waiter(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Waiter waiter = (Waiter) o;
        return Objects.equals(name, waiter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "Waiter{" +
                "name='" + name + '\'' +
                '}';
    }
}
