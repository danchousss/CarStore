package com.example.databaseproject;

import com.example.databaseproject.Controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Загрузка FXML для hello-view.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/databaseproject/hello-view.fxml"));
            Parent root = fxmlLoader.load();

            // Загрузка FXML для user-view.fxml
            FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/com/example/databaseproject/user-view.fxml"));
            Parent userRoot = userLoader.load();

            // Получаем контроллер для user-view.fxml
            UserController userController = userLoader.getController();
            HelloController helloController = fxmlLoader.getController();

            // Передаем userController в HelloController, если это нужно
            helloController.setUserController(userController);

            // Устанавливаем сцену с hello-view
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Admin View");

            // Показываем главное окно
            primaryStage.show();

            // Проверка: можно ли загрузить и взаимодействовать с контроллером
            System.out.println("UserController: " + userController);
            System.out.println("carIdTextField: " + userController.getCarIdTextField());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Запускаем JavaFX приложение
    }
}

