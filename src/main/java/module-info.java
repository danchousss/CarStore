module com.example.databaseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.databaseproject.Controller;
    opens com.example.databaseproject to javafx.graphics,javafx.fxml;
}

