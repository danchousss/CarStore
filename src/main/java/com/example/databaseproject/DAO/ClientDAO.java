package com.example.databaseproject.DAO;
import com.example.databaseproject.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ClientDAO {


        public static String validateUser(String login, String password) {
            String sqlCustomer = "SELECT COUNT(*) FROM customers WHERE login = ? AND password = ?";
            String sqlAdmin = "SELECT COUNT(*) FROM login_password_admin WHERE login = ? AND password = ?";

            try (Connection connection = JDBC.getConnection()) {
                // Проверка в таблице customers
                try (PreparedStatement statement = connection.prepareStatement(sqlCustomer)) {
                    statement.setString(1, login);
                    statement.setString(2, password);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return "customer"; // Пользователь найден в таблице customers
                    }
                }

                // Проверка в таблице login_password_admin
                try (PreparedStatement statement = connection.prepareStatement(sqlAdmin)) {
                    statement.setString(1, login);
                    statement.setString(2, password);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return "admin"; // Пользователь найден в таблице admin
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null; // Пользователь не найден
        }
}
