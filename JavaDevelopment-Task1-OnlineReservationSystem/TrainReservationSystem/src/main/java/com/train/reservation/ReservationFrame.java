
package com.train.reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationFrame extends JFrame {

    private JTextField passengerField;
    private JTextField trainNumberField;
    private JTextField trainNameField;
    private JTextField dateField;
    private JTextField sourceField;
    private JTextField destinationField;

    private JComboBox<String> classBox;

    public ReservationFrame() {

        setTitle("Train Reservation - Book Ticket");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel passengerLabel = new JLabel("Passenger Name:");
        JLabel trainNumberLabel = new JLabel("Train Number:");
        JLabel trainNameLabel = new JLabel("Train Name:");
        JLabel classLabel = new JLabel("Class Type:");
        JLabel dateLabel = new JLabel("Journey Date:");
        JLabel sourceLabel = new JLabel("Source Station:");
        JLabel destinationLabel = new JLabel("Destination:");

        passengerField = new JTextField();
        trainNumberField = new JTextField();
        trainNameField = new JTextField();

        // User cannot manually change train name
        trainNameField.setEditable(false);

        dateField = new JTextField();
        sourceField = new JTextField();
        destinationField = new JTextField();

        String[] classes = {
                "AC First Class",
                "AC 2 Tier",
                "AC 3 Tier",
                "Sleeper",
                "General"
        };

        classBox = new JComboBox<>(classes);

        JButton searchTrainButton = new JButton("Find Train");
        JButton bookButton = new JButton("BOOK TICKET");
        JButton cancelButton = new JButton("Cancellation");

        panel.add(passengerLabel);
        panel.add(passengerField);

        panel.add(trainNumberLabel);

        JPanel trainPanel = new JPanel(new BorderLayout());

        trainPanel.add(trainNumberField, BorderLayout.CENTER);
        trainPanel.add(searchTrainButton, BorderLayout.EAST);

        panel.add(trainPanel);

        panel.add(trainNameLabel);
        panel.add(trainNameField);

        panel.add(classLabel);
        panel.add(classBox);

        panel.add(dateLabel);
        panel.add(dateField);

        panel.add(sourceLabel);
        panel.add(sourceField);

        panel.add(destinationLabel);
        panel.add(destinationField);

        panel.add(bookButton);
        panel.add(cancelButton);

        searchTrainButton.addActionListener(
                e -> findTrain()
        );

        bookButton.addActionListener(
                e -> bookTicket()
        );

        cancelButton.addActionListener(e -> {

            dispose();

            new CancellationFrame();
        });

        add(panel);

        setVisible(true);
    }

    private void findTrain() {

        String trainNumber = trainNumberField.getText().trim();

        if (trainNumber.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter train number."
            );

            return;
        }

        try {

            int number = Integer.parseInt(trainNumber);

            String sql =
                    "SELECT train_name FROM trains WHERE train_number = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, number);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    trainNameField.setText(
                            rs.getString("train_name")
                    );

                } else {

                    trainNameField.setText("");

                    JOptionPane.showMessageDialog(
                            this,
                            "Train not found!"
                    );
                }
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Train number must be numeric."
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookTicket() {

        String passenger = passengerField.getText().trim();
        String trainNumberText =
                trainNumberField.getText().trim();

        String trainName =
                trainNameField.getText().trim();

        String classType =
                classBox.getSelectedItem().toString();

        String date =
                dateField.getText().trim();

        String source =
                sourceField.getText().trim();

        String destination =
                destinationField.getText().trim();

        // Empty field validation
        if (passenger.isEmpty()
                || trainNumberText.isEmpty()
                || trainName.isEmpty()
                || date.isEmpty()
                || source.isEmpty()
                || destination.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields."
            );

            return;
        }

        // Validate train number
        int trainNumber;

        try {

            trainNumber =
                    Integer.parseInt(trainNumberText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Train number must be numeric."
            );

            return;
        }

        // Validate date
        if (!isValidDate(date)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date! Use format DD-MM-YYYY."
            );

            return;
        }

        // Generate PNR
        long pnr =
                1000000000L +
                (long)(Math.random() * 9000000000L);

        String sql =
                """
                INSERT INTO reservations
                (pnr, passenger_name, train_number,
                 class_type, journey_date, source, destination)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setLong(1, pnr);
            ps.setString(2, passenger);
            ps.setInt(3, trainNumber);
            ps.setString(4, classType);
            ps.setString(5, date);
            ps.setString(6, source);
            ps.setString(7, destination);

            ps.executeUpdate();

            String message =
                    "BOOKING SUCCESSFUL!\n\n" +
                    "PNR: " + pnr + "\n" +
                    "Passenger: " + passenger + "\n" +
                    "Train: " + trainNumber +
                    " - " + trainName + "\n" +
                    "Class: " + classType + "\n" +
                    "Journey Date: " + date + "\n" +
                    "From: " + source + "\n" +
                    "To: " + destination;

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Booking Confirmation",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Booking failed!"
            );
        }
    }

    private boolean isValidDate(String date) {

        try {

            SimpleDateFormat format =
                    new SimpleDateFormat("dd-MM-yyyy");

            format.setLenient(false);

            Date parsedDate =
                    format.parse(date);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private void clearFields() {

        passengerField.setText("");
        trainNumberField.setText("");
        trainNameField.setText("");
        dateField.setText("");
        sourceField.setText("");
        destinationField.setText("");

        classBox.setSelectedIndex(0);
    }
}


