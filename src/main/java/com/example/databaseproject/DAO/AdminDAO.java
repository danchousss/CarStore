package com.example.databaseproject.DAO;

import com.example.databaseproject.JDBC;
import com.example.databaseproject.Model.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    // Метод для получения всех данных из таблицы cars
    public static List<Car> getAllCars() throws SQLException {
        List<Car> cars = new ArrayList<>();

        // Получаем соединение через класс JDBC
        try (Connection connection = JDBC.getConnection()) {
            // Изменяем SQL-запрос для выбора названия компании из таблицы companies
            String query = "SELECT cars.car_id, cars.year, cars.model, cars.company_id, companies.companies_name AS company_name, cars.price\n" +
                    "FROM cars" +
                    "JOIN companies ON cars.company_id = companies.company_id;\n";

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int carId = rs.getInt("car_id");
                    int year = rs.getInt("year");
                    String model = rs.getString("model");
                    int companyId = rs.getInt("company_id");
                    String companyName = rs.getString("company_name");
                    int price = rs.getInt("price");


                    cars.add(new Car(carId, year, model, companyId, companyName, price));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving data from cars table", e);
        }

        return cars;
    }
}
