package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RSettingController extends SceneController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField confirmNewPassWordTextField;

    @FXML
    private TextField currentPassWordTextField;

    @FXML
    private TextField newPassWordTextField;


    @FXML
    void cancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmButtonClicked(ActionEvent event) {
        String currentPassWord = currentPassWordTextField.getText();
        String newPassWord = newPassWordTextField.getText();
        String confirmNewPassWord = confirmNewPassWordTextField.getText();

        if (currentPassWord.isEmpty() || newPassWord.isEmpty() || confirmNewPassWord.isEmpty()) {
            alertSoundPlay();
            showAlert(null, "All fields must be filled!", null, Alert.AlertType.WARNING);
            return;
        }

        if (!newPassWord.equals(confirmNewPassWord)) {
            alertSoundPlay();
            showAlert(null, "Passwords do not match!", null, Alert.AlertType.WARNING);
            return;
        }

        if (isCurrentPassWordValid(currentPassWord)) {
            changePassWord(currentPassWord, newPassWord);
        } else {
            alertSoundPlay();
            showAlert(null, "Your current password is not right!", null, Alert.AlertType.WARNING);
        }
    }

    private boolean isCurrentPassWordValid(String currentPassWord) {
        String querry = "SELECT * FROM user WHERE password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(querry)) {
            preparedStatement.setString(1, currentPassWord);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void changePassWord(String currentPassWord,String newPassWord) {
    }
}
