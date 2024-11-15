package com.example.libms;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {

    @FXML
    private Label changingLabel;
    @FXML
    private Button alrButton;

    @FXML
    private PasswordField confirmPassWordPassWordField;

    @FXML
    private PasswordField loginPassWordPassWordField;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private ChoiceBox roleChoiceBox;

    @FXML
    private AnchorPane sideForm;

    @FXML
    private Button sideSignUpButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private PasswordField signUpPassWordPassWordField;

    @FXML
    private TextField signUpUsernameTextField;

    public static String userName;

    @FXML
    void initialize() {
        //SceneController.playBackGroundMusic();
        roleChoiceBox.setItems(FXCollections.observableArrayList(new String[] { "Admin", "Reader" }));
    }

    public void ClickChoiceBox(MouseEvent mouseEvent) {
        roleChoiceBox.show();
    }

    public void loginButtonClicked() throws IOException {
        if (loginUsernameTextField.getText().isBlank() || loginPassWordPassWordField.getText().isBlank()) {
            SceneController.alertSoundPlay();
            SceneController.showAlert(null,null,"Please enter your username and password", Alert.AlertType.WARNING);
        } else if (validateLogin(loginUsernameTextField.getText(), loginPassWordPassWordField.getText())) {
            SceneController.playButtonClickSound();
            userName = loginUsernameTextField.getText();
            SceneController.showAlert(null, null, "Login successfully", Alert.AlertType.CONFIRMATION);
            SceneController.switchScene("AdminView/dashBoard-view.fxml", signInButton);
        } else {
            SceneController.alertSoundPlay();
            SceneController.showAlert(null, null, "Invalid username or password", Alert.AlertType.WARNING);
        }
    }

    public void registerButtonClicked(ActionEvent event) throws IOException {
        String username = signUpUsernameTextField.getText();
        String password = signUpPassWordPassWordField.getText();
        String confirmPassword = confirmPassWordPassWordField.getText();

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            SceneController.alertSoundPlay();
            SceneController.showAlert(null, null, "Please enter your username and password!", Alert.AlertType.WARNING);
        } else if (!password.equals(confirmPassword)) {
            SceneController.alertSoundPlay();
            SceneController.showAlert(null, null, "Your passwords do not match!", Alert.AlertType.WARNING);
        } else {
            if (registerUser(username, password)) {
                SceneController.playButtonClickSound();
                SceneController.showAlert("Registration Successful"
                        ,null
                        ,"You have registered successfully!"
                        ,Alert.AlertType.INFORMATION);
            } else {
                SceneController.alertSoundPlay();
                SceneController.showAlert(null, null, "Registration failed. Try again!", Alert.AlertType.WARNING);
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

    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        if (event.getSource() == sideSignUpButton) {
            SceneController.playButtonClickSound();
            slider.setNode(sideForm);
            slider.setToX(350);
            slider.setDuration(Duration.seconds(.5));
            changingLabel.setText("Already have account? Sign in now");
            slider.setOnFinished((ActionEvent e) -> {
                alrButton.setVisible(true);
                sideSignUpButton.setVisible(false);
            });
            slider.play();
        } else if (event.getSource() == alrButton) {
            SceneController.playButtonClickSound();
            slider.setNode(sideForm);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            changingLabel.setText("New to our platform? Sign up now");
            slider.setOnFinished((ActionEvent e) -> {
                alrButton.setVisible(false);
                sideSignUpButton.setVisible(true);
            });
            slider.play();
        }
    }

    public boolean validateLogin(String username, String password) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "SELECT count(1) FROM user WHERE username = '" + loginUsernameTextField.getText() + "'AND password = '" + loginPassWordPassWordField.getText() + "'";
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

}
