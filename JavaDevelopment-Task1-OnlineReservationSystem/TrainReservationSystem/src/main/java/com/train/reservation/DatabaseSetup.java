
package com.train.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void createTables() {

        try (Connection con = DBConnection.getConnection()) {

            // Users table
            String usersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    )
                    """;

            // Trains table
            String trainsTable = """
                    CREATE TABLE IF NOT EXISTS trains (
                        train_number INTEGER PRIMARY KEY,
                        train_name TEXT NOT NULL
                    )
                    """;

            // Reservations table
            String reservationsTable = """
                    CREATE TABLE IF NOT EXISTS reservations (
                        pnr INTEGER PRIMARY KEY,
                        passenger_name TEXT NOT NULL,
                        train_number INTEGER NOT NULL,
                        class_type TEXT NOT NULL,
                        journey_date TEXT NOT NULL,
                        source TEXT NOT NULL,
                        destination TEXT NOT NULL
                    )
                    """;

            con.createStatement().execute(usersTable);
            con.createStatement().execute(trainsTable);
            con.createStatement().execute(reservationsTable);

            // Insert default login
            String userSQL =
                    "INSERT OR IGNORE INTO users(username, password) VALUES (?, ?)";

            try (PreparedStatement ps = con.prepareStatement(userSQL)) {

                ps.setString(1, "admin");
                ps.setString(2, "1234");

                ps.executeUpdate();
            }

            // Insert sample trains
            String trainSQL =
                    "INSERT OR IGNORE INTO trains(train_number, train_name) VALUES (?, ?)";

            try (PreparedStatement ps = con.prepareStatement(trainSQL)) {

                addTrain(ps, 12001, "Shatabdi Express");
                addTrain(ps, 12002, "New Delhi Shatabdi");
                addTrain(ps, 12501, "Pushpak Express");
                addTrain(ps, 12555, "Gorakhdham Express");
                addTrain(ps, 12423, "Dibrugarh Rajdhani");

            }

            System.out.println("Database ready!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTrain(
            PreparedStatement ps,
            int number,
            String name) throws SQLException {

        ps.setInt(1, number);
        ps.setString(2, name);
        ps.executeUpdate();
    }
}


