package com.example.seminar1314finalizat;

import com.example.seminar1314finalizat.controller.TablesController;
import com.example.seminar1314finalizat.controller.WaitersController;
import com.example.seminar1314finalizat.repository.DataBaseRepository.*;
import com.example.seminar1314finalizat.service.Service;
import com.example.seminar1314finalizat.validators.Validator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class StartApplication extends Application {

    private DB_Access data;
    private WaiterRepoDB repowaiter;
    private OrderRepoDB repoorder;
    private MenuItemRepoDB repomenuitems;
    private TableRepoDB repotable;
    private OrderItemsRepoDB repoorderitems;
    public Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        String url="jdbc:postgresql://localhost:5432/Restaurant";
        String username="postgres";
        String password="18Kriss2003";

        this.data = new DB_Access(url, username, password);
        try {
            data.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Validator val = null;
        this.repowaiter = new WaiterRepoDB(val,data, "waiters");
        this.repotable = new TableRepoDB(val, data, "tables");
        this.repomenuitems = new MenuItemRepoDB(val, data, "menu_items");
        this.repoorder = new OrderRepoDB(val, data, "orders");
        this.repoorderitems = new OrderItemsRepoDB(val, data, "order_items");
        this.service = new Service(val, repowaiter, repotable, repomenuitems, repoorder, repoorderitems);

        FXMLLoader stageLoaderT = new FXMLLoader();
        stageLoaderT.setLocation(getClass().getResource("/com/example/seminar1314finalizat/tables-view.fxml"));
        TablesController ctrlT = stageLoaderT.getController();
        initTables(ctrlT);

        FXMLLoader stageLoaderW = new FXMLLoader();
        stageLoaderW.setLocation(getClass().getResource("/com/example/seminar1314finalizat/waiters-view.fxml"));
        WaitersController ctrlW = stageLoaderW.getController();
        initWaiters(ctrlW);

        //primaryStage.show();
    }

    private void initTables(TablesController controller) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/Restaurant";
        String jdbcUser = "postgres";
        String jdbcPassword = "18Kriss2003";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
            String sqlQuery = "SELECT * FROM public.tables";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int tableNumber = resultSet.getInt("number");
                        openTableWindow(controller, tableNumber);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void openTableWindow(TablesController controller, int tableNumber) throws IOException {
        FXMLLoader tableLoader = new FXMLLoader();
        tableLoader.setLocation(getClass().getResource("/com/example/seminar1314finalizat/tables-view.fxml"));
        AnchorPane tableLayout = tableLoader.load();

        Stage tableStage = new Stage();
        tableStage.setTitle("Table " + tableNumber);
        tableStage.setScene(new Scene(tableLayout));

        TablesController tableController = tableLoader.getController();
        tableController.setService(service, tableNumber);

        tableStage.show();
    }

    private void initWaiters(WaitersController controller) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/Restaurant";
        String jdbcUser = "postgres";
        String jdbcPassword = "18Kriss2003";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
            String sqlQuery = "SELECT * FROM public.waiters";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String nume = resultSet.getString("name");
                        openWaiterWindow(controller, nume);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void openWaiterWindow(WaitersController controller, String nume) throws IOException {
        FXMLLoader waiterLoader = new FXMLLoader();
        waiterLoader.setLocation(getClass().getResource("/com/example/seminar1314finalizat/waiters-view.fxml"));
        AnchorPane waiterLayout = waiterLoader.load();

        Stage waiterStage = new Stage();
        waiterStage.setTitle("Waiter " + nume);
        waiterStage.setScene(new Scene(waiterLayout));

        WaitersController waiterController = waiterLoader.getController();
        waiterController.setService(service, nume);

        waiterStage.show();
    }
}