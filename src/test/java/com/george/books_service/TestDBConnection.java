package com.george.books_service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:postgresql://localhost:5432/BooksServer"; // JDBC URL for PostgreSQL database
        String user = "postgres"; // Database username
        String password = "123456"; // Database password

        // Attempt to establish a connection to the database
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Check if the connection was successful
            if (conn != null) {
                System.out.println("Connected to the database!"); // Print success message
            } else {
                System.out.println("Failed to make connection!"); // Print failure message
            }
        } catch (SQLException e) {
            // Handle SQL exceptions and print error message
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
