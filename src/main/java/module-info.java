module com.example.databaseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.datatransfer;

    exports com.example.databaseproject.Controller;
    opens com.example.databaseproject to javafx.graphics,javafx.fxml;
    opens com.example.databaseproject.Controller to javafx.fxml;
}

