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

public class ReadersController extends SceneController {
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
        setUpScene(usernameLabel, timeLabel);
        setReadersTable();
        searchBar.setOnKeyReleased(this::searchReaders);
        readerGenderChoiceBox.setItems(FXCollections.observableArrayList(Reader.ReaderGender.values()));
        readersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                readerIDTextField.setText(String.valueOf(newValue.getReaderID()));
                readerNameTextField.setText(String.valueOf(newValue.getReaderName()));
                readerGenderChoiceBox.setValue(newValue.getGender());
                readerPhoneTextField.setText(String.valueOf(newValue.getPhoneNumber()));
                readerEmailTextField.setText(String.valueOf(newValue.getEmail()));
            }
        });
        clearInformationButton.setOnAction(event -> clearInformationButtonClicked());
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

            readersList.clear();

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
        bookFlipSound();
        switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound1();
        switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    @FXML
    void borrowedBooksButtonClicked(ActionEvent event) throws IOException {
        bookFlipSound();
        switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton
                , null, null
                ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    @FXML
    void clearInformationButtonClicked() {
        playButtonClickSound2();
        readerIDTextField.clear();
        readerNameTextField.clear();
        readerGenderChoiceBox.setValue(null);
        readerPhoneTextField.clear();
        readerEmailTextField.clear();
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
            alertSoundPlay();
            showAlert("Input Error", "Phone number must be a valid integer.", null, Alert.AlertType.WARNING);
            return;
        }

        // Check if all fields are filled correctly
        if (readerName.isEmpty() || email.isEmpty() || gender == null || phoneNumber <= 0) {
            alertSoundPlay();
            showAlert("Input Error", "Please fill all fields correctly", null, Alert.AlertType.WARNING);
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
                playButtonClickSound2();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reader added successfully.");
                alert.showAndWait();
                loadReadersDataFromDatabase(); // Refresh the table
            }

        } catch (SQLException e) {
            alertSoundPlay();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Could not add reader to the database.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void updateReaderButtonClicked() {

        // Lấy dữ liệu từ các trường nhập
        String readerIDText = readerIDTextField.getText().trim();
        String readerName = readerNameTextField.getText().trim();
        String email = readerEmailTextField.getText().trim();
        Reader.ReaderGender gender = readerGenderChoiceBox.getValue();
        int phoneNumber;

        // Kiểm tra xem Reader ID có hợp lệ không
        int readerID;
        try {
            readerID = Integer.parseInt(readerIDText);
        } catch (NumberFormatException e) {
            alertSoundPlay();
            showAlert(null, "Input Error", "Reader ID must be a valid integer.", Alert.AlertType.WARNING);
            return;
        }

        // Kiểm tra số điện thoại có hợp lệ không
        try {
            phoneNumber = Integer.parseInt(readerPhoneTextField.getText().trim());
        } catch (NumberFormatException e) {
            alertSoundPlay();
            showAlert(null, "Input Error", "Phone number must be a valid integer.", Alert.AlertType.WARNING);
            return;
        }

        // Kiểm tra xem các trường có đầy đủ dữ liệu không
        if (readerName.isEmpty() || email.isEmpty() || gender == null || phoneNumber <= 0) {
            alertSoundPlay();
            showAlert(null, "Input Error", "Please fill all fields correctly.", Alert.AlertType.WARNING);
            return;
        }

        // Tạo câu truy vấn để cập nhật dữ liệu
        String updateQuery = "UPDATE readers SET readerName = ?, Gender = ?, PhoneNumber = ?, Email = ? WHERE readerID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            // Truyền giá trị vào câu lệnh SQL
            statement.setString(1, readerName);
            statement.setString(2, gender.name());
            statement.setInt(3, phoneNumber);
            statement.setString(4, email);
            statement.setInt(5, readerID);

            // Thực thi cập nhật
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                playButtonClickSound2();
                showAlert(null, "Success", "Reader updated successfully.", Alert.AlertType.INFORMATION);
                refreshTableData();
                clearInformationButtonClicked();
            } else {
                alertSoundPlay();
                showAlert(null, "Update Failed", "No reader found with the given ID.", Alert.AlertType.WARNING);
            }

        } catch (SQLException e) {
            alertSoundPlay();
            showAlert(null, "Database Error", "Could not update the database. \n" + e.getMessage(), Alert.AlertType.ERROR);

        }
    }

    // Làm mới dữ liệu hiển thị trên bảng
    private void refreshTableData() {
        readersList.clear(); // Xóa danh sách hiện tại
        loadReadersDataFromDatabase(); // Tải dữ liệu mới từ cơ sở dữ liệu
    }
    @FXML
    void deleteReaderButtonClicked() {
        // Lấy Reader được chọn trong bảng
        Reader selectedReader = readersTable.getSelectionModel().getSelectedItem();

        // Kiểm tra nếu không có dòng nào được chọn
        if (selectedReader == null) {
            alertSoundPlay();
            showAlert(null, "No Selection", "Please select a reader to delete.", Alert.AlertType.WARNING);
            return;
        }

        // Hiển thị xác nhận trước khi xóa
        deleteConfirm();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this reader?");
        confirmationAlert.setContentText("Reader Name: " + selectedReader.getReaderName());

        // Chờ người dùng xác nhận
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Xóa Reader trong cơ sở dữ liệu
            String deleteQuery = "DELETE FROM readers WHERE readerID = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

                statement.setInt(1, selectedReader.getReaderID()); // Sử dụng ID để xác định dòng cần xóa
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Xóa thành công, hiển thị thông báo
                    playButtonClickSound2();
                    showAlert(null, "Success", "Reader deleted successfully.", Alert.AlertType.INFORMATION);
                    // Xóa Reader khỏi danh sách hiển thị và cập nhật bảng
                    readersList.remove(selectedReader);
                    readersTable.refresh();
                } else {
                    alertSoundPlay();
                    showAlert(null, "Error", "Failed to delete the selected reader.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                alertSoundPlay();
                showAlert(null, "Database Error", "Could not delete the database. \n" + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
