package com.example.seminar1314finalizat.domain;

import java.util.Objects;

public class Table extends Entity<Long>{

    private int number;

    public Table(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Table table = (Table) o;
        return number == table.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number);
    }

    @Override
    public String toString() {
        return "Table{" +
                "number=" + number +
                '}';
    }
}
