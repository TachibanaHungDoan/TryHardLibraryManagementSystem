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

public class RegisterController {
    public static final String sampleRegisterUsername = "Avenchurin";
    public static final String sampleRegisterPassword = "1234569";
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passWordPassWordField;
    @FXML
    private PasswordField confirmPassWordPassWordField;
    @FXML
    private Label registerMessageLabel;

    public void registerButtonClicked(ActionEvent event) throws IOException {
        if (usernameTextField.getText().isBlank() || passWordPassWordField.getText().isBlank() || confirmPassWordPassWordField.getText().isBlank()) {
            registerMessageLabel.setText("Please enter your username and password!");
        } else if (!passWordPassWordField.getText().equals(confirmPassWordPassWordField.getText())) {
            registerMessageLabel.setText("Your passwords do not match!");
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
