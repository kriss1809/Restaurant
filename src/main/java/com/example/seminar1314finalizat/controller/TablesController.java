package com.example.seminar1314finalizat.controller;

import com.example.seminar1314finalizat.domain.MenuItem;
import com.example.seminar1314finalizat.domain.Order;
import com.example.seminar1314finalizat.domain.OrderItems;
import com.example.seminar1314finalizat.domain.Table;
import com.example.seminar1314finalizat.service.Service;
import com.example.seminar1314finalizat.utils.events.ChangeEvent;
import com.example.seminar1314finalizat.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TablesController implements Observer<ChangeEvent> {

    Service service;
    Integer table_number;
    @FXML
    private Button btn_check_bill;
    @FXML
    private Button btn_top5;
    @FXML
    private Button btn_order;
    @FXML
    private Label label_nr;
    @FXML
    private TableView<MenuItem> tableview_meniu;
    @FXML
    private TableColumn<MenuItem, Float> tbl_col_price;
    @FXML
    private TableColumn<MenuItem, String> tbl_col_item;
    public ObservableList<MenuItem> menuModel = FXCollections.observableArrayList();

    public void setService(Service service, Integer nr) {
        this.service = service;
        this.table_number = nr;
        this.label_nr.setText("Masa cu numarul: " + nr);
        service.addObserver(this);
        initMenuModel();
    }

    @FXML
    public void initialize()
    {
        initializeTableMenuItems();
    }

    private void initializeTableMenuItems()
    {
        tbl_col_item.setCellValueFactory(new PropertyValueFactory<MenuItem,String>("name"));
        tbl_col_price.setCellValueFactory(new PropertyValueFactory<MenuItem,Float>("price"));

        tableview_meniu.setItems(menuModel);
        tableview_meniu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initMenuModel() {
        Iterable<MenuItem> menu = service.getAllMenuItems();
        List<MenuItem> menuList = StreamSupport.stream(menu.spliterator(),false).collect(Collectors.toList());
        menuModel.setAll(menuList);
    }

    @FXML
    private void handleUserPanelClicks(ActionEvent event)
    {
        if(event.getSource()==btn_order)
            gui_order();
        if(event.getSource()==btn_top5)
            gui_top5();
        if(event.getSource()==btn_check_bill)
            gui_check_bill();
    }

    private void gui_check_bill() {
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Check Bill", service.getBill(table_number).toString());
    }

    private void gui_top5() {
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "TOP 5", service.getTop5().toString());
    }

    private void gui_order() {

        ObservableList<MenuItem> selected = tableview_meniu.getSelectionModel().getSelectedItems();

        Map<MenuItem, Integer> items = new HashMap<>();
        for (MenuItem menuItem : selected) {
            items.put(menuItem, 1);
        }
        Table table = service.findTable(table_number).get();
        Order o = new Order(LocalDateTime.now(), items,null, table);
        service.addOrder(o);
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Timp estimat", String.valueOf(service.getEstimatedTime(o)));

    }

    @Override
    public void update(ChangeEvent changeEvent) { initMenuModel();}

}
