module com.example.libms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.libms to javafx.fxml;
    exports com.example.libms;
}