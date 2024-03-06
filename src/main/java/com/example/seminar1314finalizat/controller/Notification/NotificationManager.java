package com.example.seminar1314finalizat.controller.Notification;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationManager {
    public static void showNotification(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}