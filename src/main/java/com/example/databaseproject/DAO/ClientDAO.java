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

        public static List<OrderDetail> getAllOrderDetails() throws SQLException {
            List<OrderDetail> orderDetailsList = new ArrayList<>();
            String sql = "SELECT order_id, order_details_id, car_id, quantity, customer_id FROM order_details";

            try (Connection connection = JDBC.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int orderId = resultSet.getInt("order_id");
                    int orderDetailsId = resultSet.getInt("order_details_id");
                    int carId = resultSet.getInt("car_id");
                    int quantity = resultSet.getInt("quantity");
                    int customerId = resultSet.getInt("customer_id"); // Получаем customer_id

                    // Добавляем запись в список
                    orderDetailsList.add(new OrderDetail(orderId, orderDetailsId, carId, quantity, customerId));
                }
            }
            return orderDetailsList;
        }

        public int getCustomerIdByLoginAndPassword(String login, String password) throws SQLException {
            String sql = "SELECT customer_id FROM customers WHERE login = ? AND password = ?";
            try (Connection connection = JDBC.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, login);
                statement.setString(2, password);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return rs.getInt("customer_id");
                }
            }
            return -1; // Если не найдено
        }

        public double getCarPriceById(int carId) throws SQLException {
            String sql = "SELECT price FROM cars WHERE car_id = ?";
            try (Connection connection = JDBC.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, carId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("price");
                }
            }
            return -1; // Если машина не найдена
        }

        public boolean addOrder(int customerId, int carId, double amount) throws SQLException {
            String sql = "INSERT INTO orders (customer_id, car_id, order_date, amount) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
            try (Connection connection = JDBC.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                statement.setInt(2, carId);
                statement.setDouble(3, amount);
                return statement.executeUpdate() > 0;
            }
        }

    public boolean updateOrderDetails(int customerId, int carId) throws SQLException {
        String checkOrderSql = "SELECT order_id FROM orders WHERE customer_id = ? AND car_id = ?";
        String checkDetailsSql = "SELECT quantity FROM order_details WHERE customer_id = ? AND car_id = ?";
        String updateDetailsSql = "UPDATE order_details SET quantity = quantity + 1 WHERE customer_id = ? AND car_id = ?";
        String insertDetailsSql = "INSERT INTO order_details (order_id, order_details_id, car_id, quantity, customer_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = JDBC.getConnection()) {
            connection.setAutoCommit(false);

            // Проверяем, есть ли заказ
            int orderId = -1;  // Для хранения order_id
            try (PreparedStatement checkOrderStmt = connection.prepareStatement(checkOrderSql)) {
                checkOrderStmt.setInt(1, customerId);
                checkOrderStmt.setInt(2, carId);
                ResultSet rsOrder = checkOrderStmt.executeQuery();

                if (!rsOrder.next()) {
                    // Если заказа нет, создаем новый заказ
                    int newOrderId = getNextOrderId();
                    double price = getCarPriceById(carId); // Получаем цену машины
                    if (addOrder(customerId, carId, price)) {
                        orderId = getNextOrderId();  // Получаем новый order_id
                    }
                } else {
                    orderId = rsOrder.getInt("order_id");  // Получаем существующий order_id
                }
            }

            // Проверяем, есть ли запись в order_details
            try (PreparedStatement checkStmt = connection.prepareStatement(checkDetailsSql)) {
                checkStmt.setInt(1, customerId);
                checkStmt.setInt(2, carId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Если запись существует, обновляем quantity
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateDetailsSql)) {
                        updateStmt.setInt(1, customerId);
                        updateStmt.setInt(2, carId);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Если записи нет, добавляем новую
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertDetailsSql)) {
                        insertStmt.setInt(1, orderId);  // Используем полученный orderId
                        insertStmt.setInt(2, getNextOrderDetailsId()); // Генерация нового order_details_id
                        insertStmt.setInt(3, carId);
                        insertStmt.setInt(4, 1); // quantity = 1
                        insertStmt.setInt(5, customerId);
                        insertStmt.executeUpdate();
                    }
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JDBC.rollback(JDBC.getConnection()); // Откатываем транзакцию с текущим соединением
            return false;
        }
    }

        private int getNextOrderId() throws SQLException {
            String sql = "SELECT MAX(order_id) AS max_id FROM orders";
            try (Connection connection = JDBC.getConnection();
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt("max_id") + 1;
                }
            }
            return 1; // Если таблица пустая
        }

        private int getNextOrderDetailsId() throws SQLException {
            String sql = "SELECT MAX(order_details_id) AS max_id FROM order_details";
            try (Connection connection = JDBC.getConnection();
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt("max_id") + 1;
                }
            }
            return 1; // Если таблица пустая
        }

        public List<OrderDetail> getOrderDetailsByCustomerId(int customerId) throws SQLException {
            String sql = "SELECT order_id, order_details_id, car_id, quantity, customer_id " +
                    "FROM order_details WHERE customer_id = ?";
            List<OrderDetail> orderDetailsList = new ArrayList<>();

            try (Connection connection = JDBC.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    OrderDetail orderDetail = new OrderDetail(
                            rs.getInt("order_id"),
                            rs.getInt("order_details_id"),
                            rs.getInt("car_id"),
                            rs.getInt("quantity"),
                            rs.getInt("customer_id")
                    );
                    orderDetailsList.add(orderDetail);
                }
            }
            return orderDetailsList;
        }
}






