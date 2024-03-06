package com.example.seminar1314finalizat.service;

import com.example.seminar1314finalizat.domain.*;
import com.example.seminar1314finalizat.repository.DataBaseRepository.*;
import com.example.seminar1314finalizat.utils.events.ChangeEvent;
import com.example.seminar1314finalizat.utils.events.ChangeEventType;
import com.example.seminar1314finalizat.utils.observer.Observable;
import com.example.seminar1314finalizat.utils.observer.Observer;
import com.example.seminar1314finalizat.validators.Validator;

import java.time.LocalDateTime;
import java.util.*;

public class Service implements Observable<ChangeEvent> {

    protected Validator validator;
    private WaiterRepoDB waiterRepo;
    private TableRepoDB tableRepo;
    private MenuItemRepoDB menuitemRepo;
    private OrderRepoDB orderRepo;
    private OrderItemsRepoDB orderitemsRepo;

    public Service(Validator validator, WaiterRepoDB waiterRepo, TableRepoDB tableRepo, MenuItemRepoDB menuitemRepo, OrderRepoDB orderRepo, OrderItemsRepoDB oiRepo) {
        this.validator = validator;
        this.waiterRepo = waiterRepo;
        this.tableRepo = tableRepo;
        this.menuitemRepo = menuitemRepo;
        this.orderRepo = orderRepo;
        this.orderitemsRepo = oiRepo;
    }

    public Iterable<MenuItem> getAllMenuItems() {
        return menuitemRepo.findAll();
    }
    public Iterable<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Iterable<Order> getAllOrdersWaiterNull() {
        return orderRepo.findAllWaiterNull();
    }

    public Iterable<MenuItem> getTop5()
    {
        return menuitemRepo.top5();
    }

    public Float getBill(Integer nr)
    {
        return orderRepo.getBill(nr);
    }

    public int getEstimatedTime(Order o){
        int max=0;
        for(MenuItem item : o.getItems().keySet())
            if(max < item.getPreparationTimeInMinutes())
                max = item.getPreparationTimeInMinutes();
        return max;
    }


    public Iterable<Order> getRaport(LocalDateTime data, String nume){
        Optional<Waiter> w = waiterRepo.findByName(nume);
        if(w.isPresent()) {
            return orderRepo.raport(data,w.get());
        }
        return null;
    }

    public void addOrder(Order entity) {
       orderRepo.save(entity);
       notify(new ChangeEvent(ChangeEventType.ADD, entity));

       Optional<Order> savedOrder = orderRepo.findMostRecent();


        if (savedOrder.isPresent()) {
            Order orderWithId = savedOrder.get();
            for (Map.Entry<MenuItem, Integer> entry : entity.getItems().entrySet()) {
                MenuItem menuItem = entry.getKey();
                Integer quantity = entry.getValue();

                OrderItems orderItem = new OrderItems(orderWithId, menuItem, quantity);
                orderitemsRepo.save(orderItem);
            }

            notify(new ChangeEvent(ChangeEventType.ADD, orderWithId));
        }
        else
            System.out.println("eroare");
    }



    public void updateOrder(Order order, String nume_waiter)
    {
        Optional<Waiter> w = waiterRepo.findByName(nume_waiter);
        if(w.isPresent()) {
            orderRepo.updateWaiter(order, w.get());
            notify(new ChangeEvent(ChangeEventType.UPDATE, order));
        }
    }

    public Optional<Table> findTable(int nr)
    {
        return tableRepo.findOneByNr(nr);
    }

    private List<Observer<ChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<ChangeEvent> e) {
        observers.add(e);
    }
    @Override
    public void removeObserver(Observer<ChangeEvent> e) {
        observers.remove(e);
    }
    @Override
    public void notify(ChangeEvent t) {
        observers.forEach(observer -> observer.update(t));
    }
}
