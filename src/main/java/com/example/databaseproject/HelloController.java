package com.example.databaseproject;

import com.example.databaseproject.Controller.UserController;
import com.example.databaseproject.DAO.ClientDAO;
import com.example.databaseproject.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.databaseproject.Model.Customer;
import com.example.databaseproject.DAO.ClientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class HelloController {

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private TextArea profileView;

    // Метод для обработки кнопки "Войти"
    @FXML
    private void handleLoginButton() {
        String login = loginField.getText();
        String password = passwordField.getText();

        String userType = ClientDAO.validateUser(login, password);

        if ("customer".equals(userType)) {
            try {
                // Получаем информацию о пользователе по логину и паролю
                Customer customer = ClientDAO.getCustomerByLoginAndPassword(login, password);

                if (customer != null) {
                    // Загружаем файл .fxml для профиля пользователя
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("user-view.fxml"));
                    Parent root = loader.load();

                    // Создаем новое окно (Stage)
                    Stage stage = new Stage();
                    stage.setTitle("Профиль пользователя");

                    // Устанавливаем сцену (Scene) с загруженным интерфейсом
                    stage.setScene(new Scene(root));

                    // Получаем контроллер UserController
                    UserController userController = loader.getController();
                    userController.setProfileData(customer);  // Передаем данные в UserController

                    // Показываем окно
                    stage.show();
                    UserSession.setUser(new User(Integer.parseInt(login), false));

                    // Закрываем текущее окно (если необходимо)
                    closeCurrentWindow();
                } else {
                    showErrorDialog();  // Показываем ошибку, если пользователь не найден
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("admin".equals(userType)) {
            try {
                // Загружаем файл .fxml для админского интерфейса
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin-view.fxml"));
                Parent root = loader.load();

                // Создаем новое окно (Stage)
                Stage stage = new Stage();
                stage.setTitle("Admin");

                // Устанавливаем сцену (Scene) с загруженным интерфейсом
                stage.setScene(new Scene(root));

                // Показываем окно
                stage.show();

                UserSession.setUser(new User(Integer.parseInt(login), true));

                // Закрываем текущее окно (если необходимо)
                closeCurrentWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog();  // Показываем ошибку, если логин/пароль неверные
        }
    }


    // Метод для закрытия текущего окна
    private void closeCurrentWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    // Метод для отображения ошибки при неверном логине или пароле
    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Неправильный логин или пароль", ButtonType.OK);
        alert.setTitle("Ошибка авторизации");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    private static UserController userController; // Ссылка на UserController

    public static void setUserController(UserController controller) {
        userController = controller; // Устанавливаем ссылку на UserController
    }
}
