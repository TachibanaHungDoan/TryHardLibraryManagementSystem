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

public class ReadersController extends AdminTemplateController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button homeButton, booksButton, borrowedBooksButton, logOutButton;
    @FXML
    private Button updateReaderButton, clearInformationButton, deleteReaderButton;
    @FXML
    private TextField searchBar;
    @FXML
    private Label readerIDLabel;
    @FXML
    private TextField readerNameTextField,readerPhoneTextField, readerEmailTextField;
    @FXML
    private ChoiceBox<Reader.ReaderGender> readerGenderChoiceBox;
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

    private final ReaderDAO readerDAO = new ReaderDAO();
    private ObservableList<Reader> readersList = FXCollections.observableArrayList();
    private Reader selectedReader;

    private SoundButtonController soundButtonController = SoundButtonController.getInstance();
    private AlertShowing alertShowing = new AlertShowing();

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        setReadersTable();
        searchBar.setOnKeyReleased(this::searchReaders);
        readerGenderChoiceBox.setItems(FXCollections.observableArrayList(Reader.ReaderGender.values()));
        readersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedReader = newValue;
            if (selectedReader != null) {
                setReaderTextField();
            }
        });
        clearInformationButton.setOnAction(event -> clearInformationButtonClicked());
    }

    @FXML
    void booksButtonClicked() throws IOException {
        switchToBooksView(booksButton);
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws IOException {
       switchToDashboardView(homeButton);
    }

    @FXML
    void borrowedBooksButtonClicked(ActionEvent event) throws IOException {
        switchToBorrowedBooksView(borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        switchToLoginView(logOutButton);
    }

    @FXML
    void clearInformationButtonClicked() {
        soundButtonController.playButtonClickSound2();
        readerIDLabel.setText("");
        readerNameTextField.clear();
        readerGenderChoiceBox.setValue(null);
        readerPhoneTextField.clear();
        readerEmailTextField.clear();
    }

    @FXML
    void updateReaderButtonClicked() {
        // Lấy dữ liệu từ các trường nhập
        String readerIDText = readerIDLabel.getText().trim();
        String readerName = readerNameTextField.getText().trim();
        String email = readerEmailTextField.getText().trim();
        Reader.ReaderGender gender = readerGenderChoiceBox.getValue();
        int phoneNumber;

        // Kiểm tra xem Reader ID có hợp lệ không
        int readerID;
        try {
            readerID = Integer.parseInt(readerIDText);
        } catch (NumberFormatException e) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert(null, "Input Error", "Reader ID must be a valid integer.", Alert.AlertType.WARNING);
            return;
        }

        // Kiểm tra số điện thoại có hợp lệ không
        try {
            phoneNumber = Integer.parseInt(readerPhoneTextField.getText().trim());
        } catch (NumberFormatException e) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert(null, "Input Error", "Phone number must be a valid integer.", Alert.AlertType.WARNING);
            return;
        }

        if (readerName.isEmpty() || email.isEmpty() || gender == null || phoneNumber <= 0) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert(null, "Input Error", "Please fill all fields correctly.", Alert.AlertType.WARNING);
            return;
        }

        Reader readerToUpdate = new Reader(readerID, readerName, gender, phoneNumber, email);
        int updateResult = readerDAO.update(readerToUpdate);
            if (updateResult > 0) {
                soundButtonController.playButtonClickSound2();
                alertShowing.showAlert(null, "Success", "Reader updated successfully.", Alert.AlertType.INFORMATION);
                refreshTableData();
                clearInformationButtonClicked();
            } else {
                soundButtonController.alertSoundPlay();
                alertShowing.showAlert(null, "Update Failed", "No reader found with the given ID.", Alert.AlertType.WARNING);
            }
        }

    @FXML
    void deleteReaderButtonClicked() {
        // Lấy Reader được chọn trong bảng
        Reader selectedReader = readersTable.getSelectionModel().getSelectedItem();

        // Kiểm tra nếu không có dòng nào được chọn
        if (selectedReader == null) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert(null, "No Selection", "Please select a reader to delete.", Alert.AlertType.WARNING);
            return;
        }

        // Hiển thị xác nhận trước khi xóa
        soundButtonController.deleteConfirm();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this reader?");
        confirmationAlert.setContentText("Reader Name: " + selectedReader.getReaderName());

        // Chờ người dùng xác nhận
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Xóa Reader trong cơ sở dữ liệu
            int deleteResult = readerDAO.delete(selectedReader);
            if (deleteResult > 0) {
                // Xóa thành công, hiển thị thông báo
                soundButtonController.playButtonClickSound2();
                alertShowing.showAlert(null, "Success", "Reader deleted successfully.", Alert.AlertType.INFORMATION);
                // Xóa Reader khỏi danh sách hiển thị và cập nhật bảng
                readersList.remove(selectedReader);
                readersTable.refresh();
            } else {
                soundButtonController.alertSoundPlay();
                alertShowing.showAlert(null, "Error", "Failed to delete the selected reader.", Alert.AlertType.ERROR);
            }
        }
    }

    private void setReaderTextField() {
        readerIDLabel.setText(String.valueOf(selectedReader.getReaderID()));
        readerNameTextField.setText(String.valueOf(selectedReader.getReaderName()));
        readerGenderChoiceBox.setValue(selectedReader.getGender());
        readerPhoneTextField.setText(String.valueOf(selectedReader.getPhoneNumber()));
        readerEmailTextField.setText(String.valueOf(selectedReader.getEmail()));
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
        readersList.clear();
        List<Reader> getReaderList = readerDAO.getAllItems();
        readersList = FXCollections.observableArrayList(getReaderList);
        readersTable.setItems(readersList);
    }

    private void searchReaders(KeyEvent event) {
        String keyword = searchBar.getText().toLowerCase();

        List<Reader> filteredReaders = readersList.stream()
                .filter(reader -> reader.getReaderName().toLowerCase().contains(keyword) ||
                        String.valueOf(reader.getReaderID()).contains(keyword))
                .collect(Collectors.toList());
        readersTable.setItems(FXCollections.observableArrayList(filteredReaders));
    }

    // Làm mới dữ liệu hiển thị trên bảng
    private void refreshTableData() {
        readersList.clear(); // Xóa danh sách hiện tại
        loadReadersDataFromDatabase(); // Tải dữ liệu mới từ cơ sở dữ liệu
    }
}
