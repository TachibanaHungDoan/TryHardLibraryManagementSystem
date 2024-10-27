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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    /*public void validateLogin(String username, String password){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = '" + usernameTextField.getText() + "'AND password = '" + passwordPassWordField.getText() + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while(queryResult.next()) {
                if(queryResult.getInt(1) == 1) {
                    loginMessageLabel.setText("Login successful");
                } else {
                    loginMessageLabel.setText("Login failed. Please try again");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }*/
    public boolean validateLogin(String username, String password) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        // Câu truy vấn SQL sử dụng dấu ? để truyền tham số an toàn
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
            Parent root = FXMLLoader.load(getClass().getResource("AdminView/dashboard-view.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            loginMessageLabel.setText("Wrong username or password");
        }
    }

    @FXML
    private void registerLabelClicked(MouseEvent event) throws IOException {
        try {
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("register-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
