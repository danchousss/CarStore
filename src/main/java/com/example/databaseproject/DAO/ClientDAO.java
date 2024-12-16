package com.example.databaseproject.DAO;
import com.example.databaseproject.JDBC;
import com.example.databaseproject.Model.Customer;
import com.example.databaseproject.Model.OrderDetail;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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

        // Метод для получения всех деталей заказов
        public static List<OrderDetail> getAllOrderDetails() throws SQLException {
            List<OrderDetail> orderDetails = new ArrayList<>();

            // Получаем соединение через класс JDBC
            try (Connection connection = JDBC.getConnection()) {
                // SQL-запрос для получения данных из таблицы order_details
                String query = """
                SELECT order_id, order_details_id, car_id, quantity
                FROM order_details;
            """;

                // Выполняем запрос
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                    // Перебираем все строки результата
                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        int orderDetailsId = rs.getInt("order_details_id");
                        int carId = rs.getInt("car_id");
                        int quantity = rs.getInt("quantity");

                        // Создаем объект OrderDetail и добавляем в список
                        orderDetails.add(new OrderDetail(orderId, orderDetailsId, carId, quantity));
                    }
                }
            } catch (SQLException e) {
                // Подробное сообщение об ошибке
                throw new SQLException("Error while retrieving data from order_details table: " + e.getMessage(), e);
            }

            return orderDetails;
        }
    public static Customer getCustomerByLoginAndPassword(String login, String password) throws SQLException {
        Customer customer = null;

        // Получаем соединение через класс JDBC
        try (Connection connection = JDBC.getConnection()) {
            // SQL-запрос для получения данных из таблицы customers по логину и паролю
            String query = """
        SELECT customer_id, name, login, phone_number, password
        FROM customers
        WHERE login = ? AND password = ?;
        """;

            // Выполняем запрос
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                // Устанавливаем параметры в запрос
                stmt.setString(1, login);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    // Если пользователь найден, создаем объект Customer
                    if (rs.next()) {
                        int customerId = rs.getInt("customer_id");
                        String name = rs.getString("name");
                        String userLogin = rs.getString("login");
                        String phoneNumber = rs.getString("phone_number");
                        String userPassword = rs.getString("password");

                        // Создаем объект Customer
                        customer = new Customer(customerId, name, userLogin, phoneNumber, userPassword);
                    }
                }
            }
        } catch (SQLException e) {
            // Подробное сообщение об ошибке
            throw new SQLException("Error while retrieving customer data: " + e.getMessage(), e);
        }

        return customer; // Вернем null, если пользователь не найден
    }

}




