
package com.train.reservation;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        // Create database tables
        DatabaseSetup.createTables();

        // Start login screen
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}


