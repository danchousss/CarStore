package com.example.databaseproject.Controller;
import com.example.databaseproject.DAO.AdminDAO;
import com.example.databaseproject.Model.Car;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.util.List;

public class AdminController {

    @FXML
    private TextArea carTextArea;

    @FXML
    public void initialize() {
        loadCarsData();
    }

    private void loadCarsData() {
        try {
            List<Car> carList = AdminDAO.getAllCars();
            StringBuilder sb = new StringBuilder();

            for (Car car : carList) {
                sb.append("ID: ").append(car.getCarId())
                        .append(", Year: ").append(car.getYear())
                        .append(", Model: ").append(car.getModel())
                        .append(", Company ID: ").append(car.getCompanyId())
                        .append(", Company Name: ").append(car.getCompanyName())
                        .append(", Price: ").append(car.getPrice())
                        .append("\n");
            }

            carTextArea.setText(sb.toString());
        } catch (SQLException e) {
            carTextArea.setText("Error loading data: " + e.getMessage());
        }
    }

}

