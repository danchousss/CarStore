package com.example.databaseproject.Controller;

import com.example.databaseproject.DAO.AdminDAO;
import com.example.databaseproject.DAO.ClientDAO;
import com.example.databaseproject.Model.Car;
import com.example.databaseproject.Model.Customer;
import com.example.databaseproject.Model.OrderDetail;
import com.example.databaseproject.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserController {
    @FXML
    private TextArea orderDetailsView;

    private ClientDAO clientDAO;

    public void initialize() {
        clientDAO = new ClientDAO();
        loadOrderDetailsData();  // Один метод для загрузки данных
        loadCarsData();
        loadOrderDetailsData();
    }

    // Метод для загрузки данных о заказах в TextArea
    private void loadOrderDetailsData() {
        try {
            // Получаем список всех деталей заказов из DAO
            List<OrderDetail> orderDetailsList = ClientDAO.getAllOrderDetails();

            // Проверяем, что список не пуст
            if (orderDetailsList == null || orderDetailsList.isEmpty()) {
                orderDetailsView.setText("No data available.");
                return; // Выход, если данных нет
            }

            // Используем StringBuilder для формирования строки для TextArea
            StringBuilder sb = new StringBuilder();
            for (OrderDetail orderDetail : orderDetailsList) {
                sb.append("Order ID: ").append(orderDetail.getOrderId())
                        .append(", Order Details ID: ").append(orderDetail.getOrderDetailsId())
                        .append(", Car ID: ").append(orderDetail.getCarId())
                        .append(", Quantity: ").append(orderDetail.getQuantity())
                        .append(", Customer ID: ").append(orderDetail.getCustomerId()) // Добавлено поле customer_id
                        .append("\n");
            }

            // Устанавливаем текст в TextArea
            orderDetailsView.setText(sb.toString());
        } catch (SQLException e) {
            // Обрабатываем ошибку при загрузке данных
            orderDetailsView.setText("Error loading data: " + e.getMessage());
            System.err.println("Error loading order details: " + e.getMessage());
        }
    }

    @FXML
    private TextArea profileView;

    // Метод для установки данных пользователя в TextArea
    public void setProfileData(Customer customer) {
        profileView.setText("Customer ID: " + customer.getCustomerId() + "\n" +
                "Name: " + customer.getName() + "\n" +
                "Login: " + customer.getLogin() + "\n" +
                "Phone Number: " + customer.getPhoneNumber());
    }
    @FXML
    private TextArea tablecarview;


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
            tablecarview.setText(sb.toString());
        } catch (SQLException e) {
            // Отображаем ошибку, если загрузка данных не удалась
            tablecarview.setText("Error loading data: " + e.getMessage());
        } catch (NullPointerException e) {
            // Обрабатываем случай, если carTextArea не инициализирована
            System.err.println("Error: TextArea is not initialized. Check your FXML file.");
        }
    }
    @FXML
    private Button logoutButton;               // Кнопка ADD Customer
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
    @FXML
    private TextField carIdTextField;

    @FXML
    private Button buyCarButton;

    @FXML
    private TextField passwordField;
    @FXML
    private TextField loginField;
    @FXML
    private void handleBuyCarButtonClick() {
        try {
            int carId = Integer.parseInt(carIdTextField.getText());

//            System.out.println("Login: " + login + ", Password: " + password);
//            System.out.println("Car ID entered: " + carId);
//
//            if (login.isEmpty() || password.isEmpty() || carIdTextField.getText().isEmpty()) {
//                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields.");
//                return;
//            }

//            int customerId = clientDAO.getCustomerIdByLoginAndPassword(login, password);
            int customerId = UserSession.getUser().getUserId();
            System.out.println("Customer ID: " + customerId);

            if (customerId == -1) {
                showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid login or password.");
                return;
            }

            double carPrice = clientDAO.getCarPriceById(carId);
            System.out.println("Car Price: " + carPrice);

            if (carPrice == -1) {
                showAlert(Alert.AlertType.ERROR, "Car Not Found", "Invalid car ID.");
                return;
            }

            boolean orderAdded = clientDAO.addOrder(customerId, carId, carPrice);
            System.out.println("Order added: " + orderAdded);

            if (!orderAdded) {
                showAlert(Alert.AlertType.ERROR, "Order Failed", "Could not place the order.");
                return;
            }

            boolean orderDetailsUpdated = clientDAO.updateOrderDetails(customerId, carId);
            System.out.println("Order details updated: " + orderDetailsUpdated);

            if (!orderDetailsUpdated) {
                showAlert(Alert.AlertType.WARNING, "Order Details Issue", "Order placed, but details could not be updated.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Car purchased successfully!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Car ID must be a number.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String invalidInput, String s) {
    }
    private UserController userController;

    // Метод для установки UserController
    public void setUserController(UserController userController) {
        this.userController = userController;
    }


    public TextField getCarIdTextField() {
        return carIdTextField;
    }


}
