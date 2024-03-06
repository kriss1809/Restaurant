package com.example.seminar1314finalizat.domain;

import java.util.Objects;

public class OrderItems extends Entity<Tuple<Long,Long>> {

    private Order order;
    private MenuItem menu_item;
    private Integer quantity;

    public OrderItems(Order order, MenuItem menu_item, Integer quantity) {
        this.order = order;
        this.menu_item = menu_item;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenu_item() {
        return menu_item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderItems that = (OrderItems) o;
        return Objects.equals(order, that.order) && Objects.equals(menu_item, that.menu_item) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), order, menu_item, quantity);
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "order=" + order +
                ", menu_item=" + menu_item +
                ", quantity=" + quantity +
                '}';
    }
}
