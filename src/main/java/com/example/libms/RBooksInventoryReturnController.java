package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.*;

public class RBooksInventoryReturnController extends ReaderTemplateController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button dashBoardButton, allBooksButton, gamesButton, logOutButton;
    @FXML
    private Button borrowedBooksScreenSwitchButton;
    @FXML
    private TableView<ReturnedBook> returnedBooksTable;
    @FXML
    private TableColumn<ReturnedBook, Integer> bookIDColumn;
    @FXML
    private TableColumn<ReturnedBook, String> titleColumn;
    @FXML
    private TableColumn<ReturnedBook, String> authorColumn;
    @FXML
    private TableColumn<ReturnedBook, String> publisherColumn;
    @FXML
    private TableColumn<ReturnedBook, String> isbnColumn;
    @FXML
    private TableColumn<ReturnedBook, Date> returnedDateColumn;
    @FXML
    private TableColumn<ReturnedBook, Integer> lateFeeColumn;

    private final ObservableList<ReturnedBook> returnedBooksList = FXCollections.observableArrayList();
    private SoundButtonController soundButtonController = SoundButtonController.getInstance();
    private AlertShowing alertShowing = new AlertShowing();

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        returnedDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        lateFeeColumn.setCellValueFactory(new PropertyValueFactory<>("lateFee"));
        loadReturnedBooksDataFromDatabase();
    }

    private void loadReturnedBooksDataFromDatabase() {
        String query = "SELECT r.rtBooksID, r.title, r.isbn, "
                + "r.returnedDate, r.borrowedDay, r.lateFee, r.readerID, b.author, b.publisher "
                + "FROM readerReturnedBooks r " + "JOIN books b ON r.isbn = b.isbn " + "WHERE r.readerID = "
                + "'" + LoggedInUser.getReaderID() + "'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            returnedBooksList.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt("rtBooksID");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                String isbn = resultSet.getString("isbn");
                Date returnedDate = resultSet.getDate("returnedDate");
                int lateFee = resultSet.getInt("lateFee");
                ReturnedBook rbook = new ReturnedBook(id,title ,author, publisher,isbn, returnedDate, lateFee);
                returnedBooksList.add(rbook);
            }
            returnedBooksTable.setItems(returnedBooksList);
    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void borrowedBooksSwitchScenehButtonClicked(ActionEvent event) throws IOException {
        soundButtonController.playButtonClickSound2();
        switchScene("ReaderView/rBooksInventoryBorrow-view.fxml", borrowedBooksScreenSwitchButton);
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) throws IOException {
        switchToLoginView(logOutButton);
    }

    @FXML
    void rAllBooksButtonClicked(ActionEvent event) throws IOException {
        switchToAllBooksView(allBooksButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        switchToGameView(gamesButton);
    }

    @FXML
    void rHomeButtonClicked(ActionEvent event) throws IOException {
        switchToDashBoardView(dashBoardButton);
    }
}
