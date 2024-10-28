package com.example.libms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPassWordField;
    @FXML
    private Label registerLabel;

    public static String userName;

    public boolean validateLogin(String username, String password) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "SELECT count(1) FROM user WHERE username = '" + usernameTextField.getText() + "'AND password = '" + passwordPassWordField.getText() + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while(queryResult.next()) {
                if(queryResult.getInt(1) == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public void loginButtonClicked() throws IOException {
        if (usernameTextField.getText().isBlank() || passwordPassWordField.getText().isBlank()) {
            loginMessageLabel.setText("Please enter your username and password");
        } else if (validateLogin(usernameTextField.getText(), passwordPassWordField.getText())) {
            userName = usernameTextField.getText();
            SceneController.switchScene("AdminView/dashBoard-view.fxml", loginButton);
        } else {
            loginMessageLabel.setText("Wrong username or password");
        }
    }

    @FXML
    private void registerLabelClicked(MouseEvent event) throws IOException {
        try {
            SceneController.switchScene("register-view.fxml", registerLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
