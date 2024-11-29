package com.example.libms;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RInformationFillController {
    @FXML
    private TextField readerNameTextField, readerPhoneTextField, readerEmailTextField;
    @FXML
    private ChoiceBox<Reader.ReaderGender> readerGenderChoiceBox;

    private SoundButtonController soundButtonController = SoundButtonController.getInstance();
    private AlertShowing alertShowing = new AlertShowing();

    @FXML
    void initialize() {
        // Populate the gender ChoiceBox
        readerGenderChoiceBox.getItems().setAll(Reader.ReaderGender.values());
        readerGenderChoiceBox.setValue(Reader.ReaderGender.male); // Default value
    }

    public boolean processForm(String username, String password) {
        // Validate inputs
        String name = readerNameTextField.getText();
        Reader.ReaderGender gender = readerGenderChoiceBox.getValue();
        String phone = readerPhoneTextField.getText();
        String email = readerEmailTextField.getText();

        if (name.isBlank() || phone.isBlank() || email.isBlank() || gender == null) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert(null, null, "Please fill in all fields!", Alert.AlertType.WARNING);
            return false;
        }

        try {
            int phoneNumber = Integer.parseInt(phone);
            int newReaderID = getNextReaderID();

            // Insert into the readers table
            String insertReaderQuery = "INSERT INTO readers (readerID, username, password, readerName, gender, phoneNumber, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertReaderQuery)) {

                preparedStatement.setInt(1, newReaderID);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, name);
                preparedStatement.setString(5, gender.toString());
                preparedStatement.setInt(6, phoneNumber);
                preparedStatement.setString(7, email);

                int result = preparedStatement.executeUpdate();
                return result > 0;
            }

        } catch (NumberFormatException e) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert(null, null, "Phone number must be numeric!", Alert.AlertType.WARNING);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert(null, null, "An error occurred while saving the data.", Alert.AlertType.ERROR);
            return false;
        }
    }

    private int getNextReaderID() {
        String query = "SELECT MAX(readerID) FROM readers";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int maxID = resultSet.getInt(1);
                return maxID + 1; // Increment by 1
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1; // Default to 1 if table is empty
    }
}