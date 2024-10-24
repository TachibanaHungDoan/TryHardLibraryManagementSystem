package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public static final String sampleUserName = "HungDoan";
    public static final String samplePassword = "31102005";
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPassWordField;

    public void loginButtonClicked(ActionEvent event) throws IOException {
        if (usernameTextField.getText().isBlank() || passwordPassWordField.getText().isBlank()) {
            loginMessageLabel.setText("Please enter your username and password");
        } else if (usernameTextField.getText().equals(sampleUserName) && passwordPassWordField.getText().equals(samplePassword)) {
            Parent root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            loginMessageLabel.setText("Wrong username or password");
        }
    }
}
