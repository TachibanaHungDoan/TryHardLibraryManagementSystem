package com.example.libms;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class LoginController extends SceneController {

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

    @FXML
    void initialize() {
        //SceneController.playBackGroundMusic();

    }

    /*public void loginButtonClicked() throws IOException {
        if (loginUsernameTextField.getText().isBlank() || loginPassWordPassWordField.getText().isBlank()) {
            alertSoundPlay();
            showAlert(null,null,"Please enter your username and password", Alert.AlertType.WARNING);
        } else if (validateLogin(loginUsernameTextField.getText(), loginPassWordPassWordField.getText())) {
            playButtonClickSound1();
            setUsername(loginUsernameTextField.getText());
            showAlert(null, null, "Login successfully", Alert.AlertType.CONFIRMATION);
            switchScene("AdminView/dashBoard-view.fxml", signInButton);
        } else {
            alertSoundPlay();
            showAlert(null, null, "Invalid username or password", Alert.AlertType.WARNING);
        }
    }*/
    public void loginButtonClicked() throws IOException {
        String username = loginUsernameTextField.getText();
        String password = loginPassWordPassWordField.getText();

        if (username.isBlank() || password.isBlank()) {
            alertSoundPlay();
            showAlert(null, null, "Please enter your username and password", Alert.AlertType.WARNING);
        } else if (username.equals("admin") && password.equals("0")) {
            // Admin credentials
            playButtonClickSound1();
            setUsername(username); // Set username globally if needed
            showAlert(null, null, "Admin login successful", Alert.AlertType.CONFIRMATION);
            switchScene("AdminView/dashBoard-view.fxml", signInButton);
        } else if (validateLogin(username, password)) {
            // Regular user credentials
            playButtonClickSound1();
            setUsername(username); // Set username globally if needed
            showAlert(null, null, "Login successful", Alert.AlertType.CONFIRMATION);
            switchScene("ReaderView/rDashBoard-view.fxml", signInButton);
        } else {
            // Invalid credentials
            alertSoundPlay();
            showAlert(null, null, "Invalid username or password", Alert.AlertType.WARNING);
        }
    }

    /*public void registerButtonClicked(ActionEvent event) throws IOException {
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
                SceneController.playButtonClickSound1();
                SceneController.showAlert("Registration Successful"
                        ,null
                        ,"You have registered successfully!"
                        ,Alert.AlertType.INFORMATION);
            } else {
                SceneController.alertSoundPlay();
                SceneController.showAlert(null, null, "Registration failed. Try again!", Alert.AlertType.WARNING);
            }
        }

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/informationFill-view.fxml"));
        DialogPane dialogPane = loader.load();

        RInformationFillController controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Fill Information Form");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            dialog.close();
        } else if (result.isPresent() && result.get() == ButtonType.OK) {
            switchScene("ReaderView/rDashBoard-view.fxml", signUpButton);
        }
    }*/
    public void registerButtonClicked(ActionEvent event) throws IOException {
        String username = signUpUsernameTextField.getText();
        String password = signUpPassWordPassWordField.getText();
        String confirmPassword = confirmPassWordPassWordField.getText();

        // Validate username and password fields
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            SceneController.alertSoundPlay();
            SceneController.showAlert(null, null, "Please enter your username and password!", Alert.AlertType.WARNING);
            return;
        }

        if (!password.equals(confirmPassword)) {
            SceneController.alertSoundPlay();
            SceneController.showAlert(null, null, "Your passwords do not match!", Alert.AlertType.WARNING);
            return;
        }

        // Load information fill form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/informationFill-view.fxml"));
        DialogPane dialogPane = loader.load();

        RInformationFillController controller = loader.getController();


        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Fill Information Form");

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Process the information form
            if (controller.processForm()) {
                // If both registration and information fill are successful, add the user credentials to the database
                if (registerUser(username, password)) {
                    SceneController.playButtonClickSound1();
                    SceneController.showAlert(
                            "Registration Successful",
                            null,
                            "You have registered successfully!",
                            Alert.AlertType.INFORMATION
                    );
                    // Optionally switch to a different view, like the Reader dashboard
                    switchScene("ReaderView/rDashBoard-view.fxml", signUpButton);
                } else {
                    SceneController.alertSoundPlay();
                    SceneController.showAlert(null, null, "Registration failed. Try again!", Alert.AlertType.WARNING);
                }
            } else {
                SceneController.alertSoundPlay();
                SceneController.showAlert(null, null, "Failed to fill out the information form.", Alert.AlertType.WARNING);
            }
        } else {
            SceneController.alertSoundPlay();
            SceneController.showAlert(null, null, "Information form was canceled.", Alert.AlertType.WARNING);
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
            playButtonClickSound1();
            slider.setNode(sideForm);
            slider.setToX(350);
            slider.setDuration(Duration.seconds(.5));
            changingLabel.setText("Already have account?");
            slider.setOnFinished((ActionEvent e) -> {
                alrButton.setVisible(true);
                sideSignUpButton.setVisible(false);
            });
            slider.play();
        } else if (event.getSource() == alrButton) {
           playButtonClickSound1();
            slider.setNode(sideForm);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            changingLabel.setText("New to our platform?");
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
