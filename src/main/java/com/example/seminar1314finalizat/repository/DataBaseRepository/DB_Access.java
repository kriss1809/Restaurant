package com.example.seminar1314finalizat.repository.DataBaseRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DB_Access {

    private Connection connection;
    private final String url, password, usernameDB;

    public DB_Access(String url, String usernameDB, String password) {
        this.url = url;
        this.usernameDB = usernameDB;
        this.password = password;
    }

    public void createConnection() throws SQLException
    {
        connection=DriverManager.getConnection(url,usernameDB,password);
    }
    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement createStatement(String statement) throws SQLException
    {
        return connection.prepareStatement(statement);
    }

}
