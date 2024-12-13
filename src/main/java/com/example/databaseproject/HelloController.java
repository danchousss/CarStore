package com.example.databaseproject;

import com.example.databaseproject.DAO.ClientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButton() {


        String login = loginField.getText();
        String password = passwordField.getText();

        String userType = ClientDAO.validateUser(login, password);

        if ("customer".equals(userType)) {
            try {
                // Загружаем файл .fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("user-view.fxml"));
                Parent root = loader.load();

                // Создаём новое окно (Stage)
                Stage stage = new Stage();
                stage.setTitle("new");

                // Устанавливаем сцену (Scene) с загруженным интерфейсом
                stage.setScene(new Scene(root));

                // Показываем окно
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeCurrentWindow();
        } else if ("admin".equals(userType)) {
            try {
                // Загружаем файл .fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
                Parent root = loader.load();

                // Создаём новое окно (Stage)
                Stage stage = new Stage();
                stage.setTitle("new");

                // Устанавливаем сцену (Scene) с загруженным интерфейсом
                stage.setScene(new Scene(root));

                // Показываем окно
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog();
        }
        closeCurrentWindow();


    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Неправильный логин или пароль", ButtonType.OK);
        alert.setTitle("Ошибка авторизации");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}