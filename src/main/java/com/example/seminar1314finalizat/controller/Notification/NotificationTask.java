package com.example.seminar1314finalizat.controller.Notification;

import com.example.seminar1314finalizat.domain.Order;
import com.example.seminar1314finalizat.service.Service;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class NotificationTask extends Task<Void> {

    private final Order order;
    private Service srv;

    public NotificationTask(Order order, Service service) {
        this.order = order;
        this.srv = service;
    }

    @Override
    protected Void call() throws Exception {
        try {
            // Așteaptă timpul estimat de pregătire
            Thread.sleep(srv.getEstimatedTime(order) * 1000L);

            // Notifică chelnerul că ordinea este pregătită
            Platform.runLater(() -> {
                NotificationManager.showNotification("Order is ready for Table " + order.getTable().getNumber());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

