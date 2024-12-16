package com.example.databaseproject.DAO;

import com.example.databaseproject.JDBC;
import com.example.databaseproject.Model.Car;
import com.example.databaseproject.Model.Customer;
import com.example.databaseproject.Model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    // Метод для получения всех данных из таблицы cars
    public static List<Car> getAllCars() throws SQLException {
        List<Car> cars = new ArrayList<>();

        // Получаем соединение через класс JDBC
        try (Connection connection = JDBC.getConnection()) {
            // Исправленный SQL-запрос
            String query = """
                    SELECT cars.car_id, cars.year, cars.model, cars.company_id, 
                           companies.companies_name AS company_name, cars.price
                    FROM cars
                    JOIN companies ON cars.company_id = companies.company_id;
                    """;

            // Выполняем запрос
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int carId = rs.getInt("car_id");
                    int year = rs.getInt("year");
                    String model = rs.getString("model");
                    int companyId = rs.getInt("company_id");
                    String companyName = rs.getString("company_name");
                    int price = rs.getInt("price");

                    // Добавляем объект Car в список
                    cars.add(new Car(carId, year, model, companyId, companyName, price));
                }
            }
        } catch (SQLException e) {
            // Подробное сообщение об ошибке
            throw new SQLException("Error while retrieving data from cars table: " + e.getMessage(), e);
        }

        return cars;
    }
    public static List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        // Получаем соединение через класс JDBC
        try (Connection connection = JDBC.getConnection()) {
            // SQL-запрос для получения данных из таблицы customers
            String query = """
                SELECT customer_id, name, login, phone_number, password
                FROM customers;
                """;

            // Выполняем запрос
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    String name = rs.getString("name");
                    String login = rs.getString("login");
                    String phoneNumber = rs.getString("phone_number");
                    String password = rs.getString("password");

                    // Добавляем объект Customer в список
                    customers.add(new Customer(customerId, name, login, phoneNumber, password));
                }
            }
        } catch (SQLException e) {
            // Подробное сообщение об ошибке
            throw new SQLException("Error while retrieving data from customers table: " + e.getMessage(), e);
        }

        return customers;
    }
    public static List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();

        // Получаем соединение через класс JDBC
        try (Connection connection = JDBC.getConnection()) {
            // SQL-запрос для получения данных из таблицы orders
            String query = """
            SELECT order_id, customer_id, amount, order_date, car_id
            FROM orders;
            """;

            // Выполняем запрос
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Обрабатываем результат
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int customerId = rs.getInt("customer_id");
                    double amount = rs.getDouble("amount");
                    Date orderDate = rs.getDate("order_date");
                    int carId = rs.getInt("car_id");

                    // Создаем объект Order и добавляем его в список
                    orders.add(new Order(orderId, customerId, amount, orderDate, carId));
                }
            }
        } catch (SQLException e) {
            // Выбрасываем исключение с подробным сообщением
            throw new SQLException("Error while retrieving data from orders table: " + e.getMessage(), e);
        }

        return orders;
    }
    public static List<String> getAdminData() throws SQLException {
        List<String> data = new ArrayList<>();

        // Пример подключения к базе данных
        try (Connection connection = JDBC.getConnection()) {
            // SQL-запрос для получения данных из таблицы login_password_admin
            String query = "SELECT login, password, name, surname FROM login_password_admin";

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Обрабатываем результат запроса
                while (rs.next()) {
                    String login = rs.getString("login");
                    String password = rs.getString("password");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");

                    // Добавляем данные в список
                    data.add("Login: " + login + ", Password: " + password + ", Name: " + name + ", Surname: " + surname);
                }
            }
        } catch (SQLException e) {
            // Пробрасываем исключение, чтобы его можно было обработать в контроллере
            throw new SQLException("Error while retrieving admin data: " + e.getMessage(), e);
        }

        return data;
    }
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, name, login, phone_number, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем параметры для запроса
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getLogin());
            preparedStatement.setString(4, customer.getPhoneNumber());
            preparedStatement.setString(5, customer.getPassword());

            // Выполняем запрос
            int rowsAffected = preparedStatement.executeUpdate();

            // Возвращаем true, если добавлено успешно
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addCar(int carId, int companyId, int year, String model, String companiesName, double price) {
        String sql = "INSERT INTO cars (car_id, company_id, year, model, companies_name, price) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем параметры для вставки
            preparedStatement.setInt(1, carId);
            preparedStatement.setInt(2, companyId);
            preparedStatement.setInt(3, year);
            preparedStatement.setString(4, model);
            preparedStatement.setString(5, companiesName);
            preparedStatement.setDouble(6, price);

            // Выполняем запрос
            int rowsAffected = preparedStatement.executeUpdate();

            // Успешное выполнение, если затронуто более 0 строк
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteCarById(int carId) {
        String sql = "DELETE FROM cars WHERE car_id = ?";

        try (Connection connection = JDBC.getConnection(); // Метод для получения соединения с базой
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, carId); // Устанавливаем значение ID
            int rowsAffected = preparedStatement.executeUpdate(); // Выполняем запрос

            return rowsAffected > 0; // Возвращаем true, если хотя бы одна строка была удалена
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Если произошла ошибка
        }
    }


}
