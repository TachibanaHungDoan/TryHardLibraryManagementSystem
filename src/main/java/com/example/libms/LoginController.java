package com.example.libms;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class LoginController extends SceneController {
    @FXML
    private Label changingLabel;
    @FXML
    private Button alrButton, sideSignUpButton;
    @FXML
    private Button signUpButton, signInButton;
    @FXML
    private PasswordField loginPassWordPassWordField;
    @FXML
    private PasswordField confirmPassWordPassWordField, signUpPassWordPassWordField;
    @FXML
    private TextField loginUsernameTextField, signUpUsernameTextField;
    @FXML
    private AnchorPane sideForm;

    private final String ADMIN_USERNAME = "Admin";
    private final String ADMIN_PASSWORD = "0";

    @FXML
    void initialize() {
    }

    @FXML
    void loginButtonClicked() throws IOException {
        String username = loginUsernameTextField.getText();
        String password = loginPassWordPassWordField.getText();

        if (username.isBlank() || password.isBlank()) {
            alertSoundPlay();
            showAlert(null,
                    null,
                    "Please enter your username and password",
                    Alert.AlertType.WARNING
            );
        } else if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            handleAdminLogin(username);
        } else if (validateLogin(username, password)) {
            handleReaderLogin(username);
        } else {
            alertSoundPlay();
            showAlert(null,
                    null,
                    "Invalid username or password",
                    Alert.AlertType.WARNING
            );
        }
    }

    private void handleAdminLogin(String username) throws IOException {
        playButtonClickSound1();
        setUserName(username);
        showAlert(null,
                null,
                "Admin login successful",
                Alert.AlertType.CONFIRMATION
        );
        switchScene("AdminView/dashBoard-view.fxml", signInButton);
    }

    private void handleReaderLogin(String username) throws IOException {
        playButtonClickSound1();
        setUserName(username);
        showAlert(null,
                null,
                "login successful",
                Alert.AlertType.CONFIRMATION
        );
        switchScene("ReaderView/rDashBoard-view.fxml", signInButton);
    }

    @FXML
    void registerButtonClicked() throws IOException {
        String username = signUpUsernameTextField.getText();
        String password = signUpPassWordPassWordField.getText();
        String confirmPassword = confirmPassWordPassWordField.getText();

        if (!validateRegistration(username, password, confirmPassword)) {
            return;
        }

        showInformationFormAndRegister(username, password);
    }

    private void showInformationFormAndRegister(String username, String password) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/informationFill-view.fxml"));
        DialogPane dialogPane = loader.load();

        RInformationFillController informationFillController = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Fill Information Form");

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (informationFillController.processForm(username, password)) {
                handleReaderRegistrationSuccessful(username);
            } else {
                alertSoundPlay();
                showAlert(null,
                        null,
                        "Registration failed. Try again!",
                        Alert.AlertType.WARNING
                );
            }
        } else {
            alertSoundPlay();
            showAlert(null,
                    null,
                    "Information form was canceled.",
                    Alert.AlertType.WARNING
            );
        }
    }

    private void handleReaderRegistrationSuccessful(String username) throws IOException {
        playButtonClickSound1();
        setUserName(username);
        showAlert(null,
                null,
                "Registration successful",
                Alert.AlertType.CONFIRMATION
        );
    }

    private boolean validateRegistration(String username, String password, String confirmPassword) {
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            alertSoundPlay();
            showAlert(null,
                    null,
                    "Please enter your username and password!",
                    Alert.AlertType.WARNING
            );
            return false;
        }
        if (!password.equals(confirmPassword)) {
            alertSoundPlay();
            showAlert(null,
                    null,
                    "Your passwords do not match!",
                    Alert.AlertType.WARNING
            );
            return false;
        }
        return true;
    }

    @FXML
    void switchForm(ActionEvent event) {
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
        PreparedStatement pstm = null;
        ResultSet queryResult = null;
        String verifyLogin = "SELECT readerID, readerName FROM readers WHERE userName = ? AND password = ?";

        try {
            pstm = connectDB.prepareStatement(verifyLogin);
            pstm.setString(1,username);
            pstm.setString(2,password);
            queryResult = pstm.executeQuery();
            if(queryResult.next()) {
                int readerID = queryResult.getInt("readerID");
                String readerName = queryResult.getString("readerName");
                LoggedInUser.setReaderID(readerID);
                LoggedInUser.setReaderName(readerName);
                return true;
                } else {
                    return false;
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
