package com.example.databaseproject.Controller;
import com.example.databaseproject.DAO.AdminDAO;
import com.example.databaseproject.Model.Car;

import com.example.databaseproject.Model.Customer;
import com.example.databaseproject.Model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXML;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminController {

    @FXML
    private TextArea carTextArea;

    @FXML
    public void initialize() {
        // Загружаем данные при инициализации
        loadCarsData();
        loadCustomersData();
        loadOrdersData();
        loadAdminData();
    }


    private void loadCarsData() {
        try {
            // Получаем список всех автомобилей из DAO
            List<Car> carList = AdminDAO.getAllCars();

            // Используем StringBuilder для формирования строки
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

            // Устанавливаем текст в TextArea
            carTextArea.setText(sb.toString());
        } catch (SQLException e) {
            // Отображаем ошибку, если загрузка данных не удалась
            carTextArea.setText("Error loading data: " + e.getMessage());
        } catch (NullPointerException e) {
            // Обрабатываем случай, если carTextArea не инициализирована
            System.err.println("Error: TextArea is not initialized. Check your FXML file.");
        }
    }
    @FXML
    private TextArea customerTextArea;


    private void loadCustomersData() {
        try {
            List<Customer> customerList = AdminDAO.getAllCustomers();
            StringBuilder sb = new StringBuilder();

            for (Customer customer : customerList) {
                sb.append("ID: ").append(customer.getCustomerId())
                        .append(", Name: ").append(customer.getName())
                        .append(", Login: ").append(customer.getLogin())
                        .append(", Phone: ").append(customer.getPhoneNumber())
                        .append(", Password: ").append(customer.getPassword())
                        .append("\n");
            }

            customerTextArea.setText(sb.toString());
        } catch (SQLException e) {
            customerTextArea.setText("Error while retrieving data from customers table: " + e.getMessage());
        }
    }
    @FXML
    private TextArea orderTextArea;


    private void loadOrdersData() {
        try {
            List<Order> orderList = AdminDAO.getAllOrders(); // Получаем данные из базы
            StringBuilder sb = new StringBuilder(); // Строка для вывода

            for (Order order : orderList) {
                sb.append("Order ID: ").append(order.getOrderId())
                        .append(", Customer ID: ").append(order.getCustomerId())
                        .append(", Amount: ").append(order.getAmount())
                        .append(", Order Date: ").append(order.getOrderDate())
                        .append(", Car ID: ").append(order.getCarId())
                        .append("\n");
            }

            // Выводим результат в TextArea
            orderTextArea.setText(sb.toString());
        } catch (SQLException e) {
            // В случае ошибки выводим текст ошибки
            orderTextArea.setText("Error while retrieving data from orders table: " + e.getMessage());
            e.printStackTrace(); // Вывод полного стека ошибок в консоль
        }
    }



        @FXML
        private TextArea profile;  // Привязка к TextArea

        // Метод для загрузки данных для администратора
        private void loadAdminData() {
            try {
                // Получаем данные из базы
                List<String> adminData = AdminDAO.getAdminData();

                // Создаем строку для вывода в TextArea
                StringBuilder sb = new StringBuilder();

                // Добавляем данные в строку
                for (String data : adminData) {
                    sb.append(data).append("\n");
                }

                // Устанавливаем текст в TextArea
                profile.setText(sb.toString());

            } catch (SQLException e) {
                // В случае ошибки выводим сообщение об ошибке в TextArea
                profile.setText("Error while retrieving admin data: " + e.getMessage());
            }
        }


    @FXML
    private TextField searchCustomerField11;  // Для customer_id
    @FXML
    private TextField searchCustomerField111; // Для name
    @FXML
    private TextField searchCustomerField112; // Для login
    @FXML
    private TextField searchCustomerField113; // Для phone_number
    @FXML
    private TextField searchCustomerField1121; // Для password
    @FXML
    private Button logoutButton;
    @FXML
    private void handleLogoutButton(ActionEvent event) {
        try {
            // Загружаем FXML файл для главного окна
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/databaseproject/hello-view.fxml"));
            Parent root = fxmlLoader.load();

            // Создаем и показываем новое окно
            Stage stage = new Stage();
            stage.setTitle("Welcome");
            stage.setScene(new Scene(root));
            stage.show();

            // Закрываем текущее окно
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку, если что-то пошло не так
        }
    }



    // Методы для отображения сообщений
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private TextField carIdField;

    @FXML
    private TextField companyIdField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField companiesNameField;

    @FXML
    private TextField priceField;

    private final AdminDAO adminDAO = new AdminDAO();

    @FXML
    private void handleAddMachineButtonClick() {
        try {
            // Получение данных из полей ввода
            int carId = Integer.parseInt(carIdField.getText());
            int companyId = Integer.parseInt(companyIdField.getText());
            int year = Integer.parseInt(yearField.getText());
            String model = modelField.getText();
            String companiesName = companiesNameField.getText();
            double price = Double.parseDouble(priceField.getText());

            // Вызов DAO для добавления машины
            boolean success = adminDAO.addCar(carId, companyId, year, model, companiesName, price);

            // Уведомление пользователя об успехе или ошибке
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Car added successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add car.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please check your input data.");
        }
    }

    private void clearFields() {
        carIdField.clear();
        companyIdField.clear();
        yearField.clear();
        modelField.clear();
        companiesNameField.clear();
        priceField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private TextField searchCarField1; // Поле для ввода ID машины

    @FXML
    private void handleDeleteCarButton(ActionEvent event) {
        try {
            // Получаем ID из текстового поля
            String carIdText = searchCarField1.getText();
            if (carIdText == null || carIdText.trim().isEmpty()) {
                showAlert("Ошибка", "Поле ID машины пустое.", Alert.AlertType.ERROR);
                return;
            }

            int carId = Integer.parseInt(carIdText.trim()); // Преобразуем текст в число

            AdminDAO adminDAO = new AdminDAO();
            boolean isDeleted = adminDAO.deleteCarById(carId); // Вызываем метод DAO для удаления

            if (isDeleted) {
                showAlert("Успех", "Машина успешно удалена.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Ошибка", "Машина с указанным ID не найдена.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "ID машины должно быть числом.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при удалении машины.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




}










