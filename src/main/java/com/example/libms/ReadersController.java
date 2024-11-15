package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ReadersController {
    @FXML
    private Button addReaderButton;

    @FXML
    private Button booksButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button clearInformationButton;

    @FXML
    private Button deleteReaderButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField readerEmailTextField;

    @FXML
    private ChoiceBox<Reader.ReaderGender> readerGenderChoiceBox;

    @FXML
    private TextField readerIDTextField;

    @FXML
    private TextField readerNameTextField;

    @FXML
    private TextField readerPhoneTextField;

    @FXML
    private Button readersButton;

    @FXML
    private TableView<Reader> readersTable;

    @FXML
    private TableColumn<Reader, String> emailColumn;

    @FXML
    private TableColumn<Reader, String> genderColumn;

    @FXML
    private TableColumn<Reader, Integer> phoneNumberColumn;

    @FXML
    private TableColumn<Reader, Integer> readerIDColumn;

    @FXML
    private TableColumn<Reader, String> readerNameColumn;

    private ObservableList<Reader> readersList = FXCollections.observableArrayList();

    @FXML
    private TextField searchBar;

    @FXML
    private Label timeLabel;

    @FXML
    private Button updateReaderButton;

    @FXML
    private Label usernameLabel;

    @FXML
    void initialize() {
        SceneController.setUpScene(usernameLabel, timeLabel);
        setReadersTable();
        searchBar.setOnKeyReleased(this::searchReaders);
        readerGenderChoiceBox.setItems(FXCollections.observableArrayList(Reader.ReaderGender.values()));
    }

    private void setReadersTable() {
        readerIDColumn.setCellValueFactory(new PropertyValueFactory<>("readerID"));
        readerNameColumn.setCellValueFactory(new PropertyValueFactory<>("readerName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));

        loadReadersDataFromDatabase();
    }

    private void loadReadersDataFromDatabase() {
        String query = "SELECT * FROM readers";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int readerID = resultSet.getInt("readerID");
                String readerName = resultSet.getString("readerName");
                String genderString = resultSet.getString("Gender");
                Reader.ReaderGender gender = Reader.ReaderGender.valueOf(genderString.toLowerCase());
                int phoneNumber = resultSet.getInt("PhoneNumber");
                String email = resultSet.getString("Email");

                Reader reader = new Reader(readerID, readerName, gender, phoneNumber, email);
                readersList.add(reader);
            }
            readersTable.setItems(readersList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void searchReaders(KeyEvent event) {
        String keyword = searchBar.getText().toLowerCase();

        List<Reader> filteredReaders = readersList.stream()
                .filter(reader -> reader.getReaderName().toLowerCase().contains(keyword) ||
                        String.valueOf(reader.getReaderID()).contains(keyword))
                .collect(Collectors.toList());
        readersTable.setItems(FXCollections.observableArrayList(filteredReaders));
    }

    @FXML
    void booksButtonClicked() throws IOException {
        SceneController.bookFlipSound();
        SceneController.switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws IOException {
        SceneController.playButtonClickSound1();
        SceneController.switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    @FXML
    void readersButtonClicked(ActionEvent event) throws IOException {
        SceneController.playButtonClickSound2();
        SceneController.switchScene("AdminView/readers-view.fxml", readersButton);
    }

    @FXML
    void borrowedBooksButtonClicked(ActionEvent event) throws IOException {
        SceneController.bookFlipSound();
        SceneController.switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        SceneController.switchSceneWithAlert("login-view.fxml", logOutButton
                , null, null
                ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        SceneController.logOutSound();
    }
    @FXML
    void addReaderButtonClicked() {
        // Get data from input fields
        String readerName = readerNameTextField.getText().trim();
        String email = readerEmailTextField.getText().trim();
        Reader.ReaderGender gender = readerGenderChoiceBox.getValue(); // Get the selected gender
        int phoneNumber;

        // Validate phone number input
        try {
            phoneNumber = Integer.parseInt(readerPhoneTextField.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText("Phone number must be a valid integer.");
            alert.showAndWait();
            return;
        }

        // Check if all fields are filled correctly
        if (readerName.isEmpty() || email.isEmpty() || gender == null || phoneNumber <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please fill all fields correctly.");
            alert.showAndWait();
            return;
        }

        // Insert new reader into the database
        String insertQuery = "INSERT INTO readers (readerName, Gender, PhoneNumber, Email) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            statement.setString(1, readerName);
            statement.setString(2, gender.name()); // Save the gender as a String
            statement.setInt(3, phoneNumber);
            statement.setString(4, email);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reader added successfully.");
                alert.showAndWait();
                loadReadersDataFromDatabase(); // Refresh the table
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Could not add reader to the database.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
