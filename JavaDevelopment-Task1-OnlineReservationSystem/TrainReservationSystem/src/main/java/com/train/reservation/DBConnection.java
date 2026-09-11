package com.train.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:sqlite:train_reservation.db";

    public static Connection getConnection() {

        try {
            return DriverManager.getConnection(URL);

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}

