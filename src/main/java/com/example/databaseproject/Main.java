package com.example.databaseproject;

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
            // Загружаем FXML файл
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Hello-view.fxml"));
            Parent root = fxmlLoader.load();

            // Устанавливаем сцену с загруженным интерфейсом
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("User View");

            // Показываем главное окно
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Запускаем JavaFX приложение
    }
}

