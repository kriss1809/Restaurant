package com.example.seminar1314finalizat.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Order extends Entity<Long>{

    private LocalDateTime date;
    private Map<MenuItem, Integer> items;
    private Waiter waiter;
    private Table table;

    public Order(LocalDateTime date, Map<MenuItem, Integer> items, Waiter waiter, Table table) {
        this.date = date;
        this.items = items;
        this.waiter = waiter;
        this.table = table;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(date, order.date) && Objects.equals(items, order.items) && Objects.equals(waiter, order.waiter) && Objects.equals(table, order.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date, items, waiter, table);
    }

    @Override
    public String toString() {
        return "Order{" +
                "date=" + date +
                ", items=" + items +
                ", waiter=" + waiter +
                ", table=" + table +
                '}';
    }
}
