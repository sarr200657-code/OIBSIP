
package com.train.reservation;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Train Reservation System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel title = new JLabel(
                "TRAIN RESERVATION SYSTEM",
                SwingConstants.CENTER
        );

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("LOGIN");

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        panel.add(usernameLabel);
        panel.add(usernameField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(new JLabel(""));
        panel.add(loginButton);

        loginButton.addActionListener(e -> login());

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(
                passwordField.getPassword()
        );

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );

            return;
        }

        String sql =
                "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login successful!"
                );

                dispose();

                new ReservationFrame();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password!",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

