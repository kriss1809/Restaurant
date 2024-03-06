package com.example.seminar1314finalizat.controller;

import com.example.seminar1314finalizat.controller.Notification.NotificationTask;
import com.example.seminar1314finalizat.domain.MenuItem;
import com.example.seminar1314finalizat.domain.Order;
import com.example.seminar1314finalizat.domain.Table;
import com.example.seminar1314finalizat.service.Service;
import com.example.seminar1314finalizat.utils.events.ChangeEvent;
import com.example.seminar1314finalizat.utils.observer.Observer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WaitersController implements Observer<ChangeEvent> {

    Service service;
    String nume;
    @FXML
    private Label label_nume;
    @FXML
    private TableView<Order> tbl_orders;
    @FXML
    private TableColumn<Order, String> tbl_col_nr;
    @FXML
    private TableColumn<Order, String> tbl_col_actions;
    public ObservableList<Order> ordersModel = FXCollections.observableArrayList();
    @FXML
    private Button raport_comenzi;
    @FXML
    private DatePicker data_aleasa;

    public void setService(Service service, String nume) {
        this.service = service;
        this.nume = nume;
        this.label_nume.setText("Waiter: " + nume);
        service.addObserver(this);
        initordersModel();
    }

    @FXML
    public void initialize()
    {
        initializeTableOrders();
    }

    private void initializeTableOrders()
    {
        tbl_col_nr.setCellValueFactory(order -> new SimpleStringProperty( order.getValue().getTable().getNumber() + ""));

        tbl_col_actions.setCellValueFactory(order -> new SimpleStringProperty( order.getValue().getTable().getNumber() + ""));
        tbl_col_actions.setCellFactory(param -> new TableCell<>() {
            private final Button acceptButton = new Button("Accept");

            {
                acceptButton.setStyle("-fx-background-color: #00FF00;");
                acceptButton.setOnAction(event -> handleAcceptButton(getIndex()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(5, acceptButton);
                    buttonBox.setAlignment(Pos.CENTER); // Center the buttons horizontally
                    setGraphic(buttonBox);
                }
            }
        });
        tbl_orders.setItems(ordersModel);
    }

    private void handleAcceptButton(int index) {
       Order selectedItem = ordersModel.get(index);
       service.updateOrder(selectedItem, nume);
       initordersModel();

       // thread uri pentru numararea timpului
        NotificationTask notificationTask = new NotificationTask(selectedItem, service);
        Thread thread = new Thread(notificationTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void initordersModel() {
        Iterable<Order> orders = service.getAllOrdersWaiterNull();
        List<Order> ordersList = StreamSupport.stream(orders.spliterator(),false).collect(Collectors.toList());
        ordersModel.setAll(ordersList);
    }

    @FXML
    private void handleUserPanelClicks(ActionEvent event)
    {
        if(event.getSource()==raport_comenzi)
            gui_raport_comenzi();

    }

    private void gui_raport_comenzi() {
        if(data_aleasa.getValue() == null) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Raport", "Nu ati ales ziua!");
        }
        else {
            LocalDateTime data = LocalDateTime.of(data_aleasa.getValue(), LocalTime.MIDNIGHT);
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Raport", service.getRaport(data, nume).toString());
        }
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        initordersModel();
    }


}
