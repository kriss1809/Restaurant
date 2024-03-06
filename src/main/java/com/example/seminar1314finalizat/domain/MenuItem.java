package com.example.seminar1314finalizat.domain;

import java.util.Objects;

public class MenuItem extends Entity<Long> {

    private String name;
    private float price;
    private short preparationTimeInMinutes;

    public MenuItem(String name, float price, short preparationTimeInMinutes) {
        this.name = name;
        this.price = price;
        this.preparationTimeInMinutes = preparationTimeInMinutes;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public short getPreparationTimeInMinutes() {
        return preparationTimeInMinutes;
    }

    public void setPreparationTimeInMinutes(short preparationTimeInMinutes) {
        this.preparationTimeInMinutes = preparationTimeInMinutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MenuItem menuItem = (MenuItem) o;
        return Float.compare(price, menuItem.price) == 0 && preparationTimeInMinutes == menuItem.preparationTimeInMinutes && Objects.equals(name, menuItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, price, preparationTimeInMinutes);
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                ", price=" + price +
                ", preparationTimeInMinutes=" + preparationTimeInMinutes + "\n";
    }
}
