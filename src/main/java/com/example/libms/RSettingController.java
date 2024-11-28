package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RSettingController extends SceneController {
    @FXML
    private Button confirmButton, cancelButton;
    @FXML
    private TextField currentPassWordTextField, newPassWordTextField, confirmNewPassWordTextField;

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
        String querry = "SELECT * FROM readers WHERE password = ?";
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
        String username = LoggedInUser.getUsername();
        String updateQuery = "UPDATE readers SET password = ? WHERE username = ? AND password = ?";

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Password Change");
        confirmationAlert.setContentText("Are you sure you want to change your password?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                    preparedStatement.setString(1, newPassWord);
                    preparedStatement.setString(2, username);
                    preparedStatement.setString(3, currentPassWord);

                    int rowsUpdated = preparedStatement.executeUpdate();

                    if (rowsUpdated > 0) {
                        playButtonClickSound2();
                        showAlert(null, "Password changed successfully!", null, Alert.AlertType.INFORMATION);
                    } else {
                        alertSoundPlay();
                        showAlert(null, "Failed to change password. Please check your current password.", null, Alert.AlertType.WARNING);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    alertSoundPlay();
                    showAlert(null, "An error occurred while changing the password. Please try again later.", null, Alert.AlertType.ERROR);
                }
            } else {
                alertSoundPlay();
                showAlert(null, "Password change canceled.", null, Alert.AlertType.INFORMATION);
            }
        });
    }
}
