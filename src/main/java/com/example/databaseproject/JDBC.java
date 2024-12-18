package com.example.databaseproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;


public class JDBC {
    private static final String url = "jdbc:postgresql://localhost:5432/BookStore";

    private static final String username = "postgres";

    private static final String password = "Dzharkynbaev27";
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, username , password);
    }
    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback(); // Откат транзакции
            } catch (SQLException e) {
                System.out.println("Error while rolling back transaction: " + e.getMessage());
            }
        }
    }
}