package com.example.libms;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterController {
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
    @FXML
    private ChoiceBox<String> roleChoiceBox;

    public void registerButtonClicked(ActionEvent event) throws IOException {
        String username = usernameTextField.getText();
        String password = passWordPassWordField.getText();
        String confirmPassword = confirmPassWordPassWordField.getText();

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            registerMessageLabel.setText("Please enter your username and password!");
        } else if (!password.equals(confirmPassword)) {
            registerMessageLabel.setText("Your passwords do not match!");
        } else {
            if (registerUser(username, password)) {
                SceneController.showAlert("Registration Successful"
                                                ,null
                                                ,"You have registered successfully!"
                                                ,Alert.AlertType.INFORMATION);

                SceneController.switchScene("login-view.fxml", registerButton);

            } else {
                registerMessageLabel.setText("Registration failed. Try again.");
            }
        }
    }

    private boolean registerUser(String username, String password) {
        String insertQuery = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int result = preparedStatement.executeUpdate();

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void initialize() {
        String str [] ={"Reader", "Admin"};
        roleChoiceBox.setItems(FXCollections.observableArrayList(str));
    }

    public void ClickChoiceBox(MouseEvent mouseEvent) {
        roleChoiceBox.show();
    }

}
