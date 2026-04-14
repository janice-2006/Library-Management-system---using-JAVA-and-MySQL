package com.library.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Problem Rectified: Added a private constructor to hide the implicit public
    // one
    private DBConnection() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() throws SQLException {
        // Note: In a real production app, move these to application.properties for
        // better security
        String url = "jdbc:mysql://localhost:3306/libraryDB";
        String user = "root";
        String pass = "Mayflower@#$123";

        return DriverManager.getConnection(url, user, pass);
    }
}