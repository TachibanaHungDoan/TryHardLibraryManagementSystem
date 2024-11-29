package com.example.libms;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertShowing {
    protected void showAlert(String title, String headerText, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
