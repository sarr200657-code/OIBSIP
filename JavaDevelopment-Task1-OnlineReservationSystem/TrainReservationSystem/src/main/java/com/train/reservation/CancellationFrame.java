
package com.train.reservation;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class CancellationFrame extends JFrame {

    private JTextField pnrField;

    private JTextArea detailsArea;

    private JButton fetchButton;
    private JButton cancelButton;

    private long currentPNR = -1;

    public CancellationFrame() {

        setTitle("Train Reservation - Cancellation");
        setSize(600, 500);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();

        JLabel pnrLabel =
                new JLabel("Enter PNR:");

        pnrField =
                new JTextField(15);

        fetchButton =
                new JButton("FETCH");

        topPanel.add(pnrLabel);
        topPanel.add(pnrField);
        topPanel.add(fetchButton);

        detailsArea =
                new JTextArea();

        detailsArea.setEditable(false);

        detailsArea.setFont(
                new Font("Monospaced", Font.PLAIN, 14)
        );

        JScrollPane scrollPane =
                new JScrollPane(detailsArea);

        cancelButton =
                new JButton("CANCEL BOOKING");

        cancelButton.setEnabled(false);

        JButton backButton =
                new JButton("Back to Reservation");

        JPanel bottomPanel =
                new JPanel();

        bottomPanel.add(cancelButton);
        bottomPanel.add(backButton);

        fetchButton.addActionListener(
                e -> fetchBooking()
        );

        cancelButton.addActionListener(
                e -> cancelBooking()
        );

        backButton.addActionListener(e -> {

            dispose();

            new ReservationFrame();
        });

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void fetchBooking() {

        String pnrText =
                pnrField.getText().trim();

        if (pnrText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter PNR number."
            );

            return;
        }

        long pnr;

        try {

            pnr =
                    Long.parseLong(pnrText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "PNR must be numeric."
            );

            return;
        }

        String sql =
                """
                SELECT r.*, t.train_name
                FROM reservations r
                JOIN trains t
                ON r.train_number = t.train_number
                WHERE r.pnr = ?
                """;

        try (Connection con =
                     DBConnection.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setLong(1, pnr);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                currentPNR = pnr;

                String details =
                        "PNR: " +
                        rs.getLong("pnr") +
                        "\n\n" +

                        "Passenger Name: " +
                        rs.getString("passenger_name") +
                        "\n\n" +

                        "Train Number: " +
                        rs.getInt("train_number") +
                        "\n\n" +

                        "Train Name: " +
                        rs.getString("train_name") +
                        "\n\n" +

                        "Class: " +
                        rs.getString("class_type") +
                        "\n\n" +

                        "Journey Date: " +
                        rs.getString("journey_date") +
                        "\n\n" +

                        "Source: " +
                        rs.getString("source") +
                        "\n\n" +

                        "Destination: " +
                        rs.getString("destination");

                detailsArea.setText(details);

                cancelButton.setEnabled(true);

            } else {

                currentPNR = -1;

                detailsArea.setText("");

                cancelButton.setEnabled(false);

                JOptionPane.showMessageDialog(
                        this,
                        "No booking found for this PNR."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void cancelBooking() {

        if (currentPNR == -1) {
            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this booking?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM reservations WHERE pnr = ?";

        try (Connection con =
                     DBConnection.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setLong(1, currentPNR);

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Booking cancelled successfully!"
                );

                detailsArea.setText("");
                pnrField.setText("");

                currentPNR = -1;

                cancelButton.setEnabled(false);
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Cancellation failed!"
            );
        }
    }
}

