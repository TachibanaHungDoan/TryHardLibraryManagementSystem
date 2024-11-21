package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class RAllBooksController extends SceneController {

    @FXML
    private TableColumn<?, ?> ISBNColumn;

    @FXML
    private Button acquireButton;

    @FXML
    private Button addToCartButton;

    @FXML
    private Button allBooksButton;

    @FXML
    private Label allBooksLabel;

    @FXML
    private TableColumn<?, ?> authorColumn;

    @FXML
    private TableColumn<?, ?> bookIDColumn;

    @FXML
    private Button booksInventoryButton;

    @FXML
    private TableView<?> booksTable;

    @FXML
    private Button dashBoardButton;

    @FXML
    private TableColumn<?, ?> editionColumn;

    @FXML
    private Button gamesButton;

    @FXML
    private Button logOutButton;

    @FXML
    private TableColumn<?, ?> publishedDateColumn;

    @FXML
    private TableColumn<?, ?> publisherColumn;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private TableColumn<?, ?> remainingColumn;

    @FXML
    private TextField searchBar;

    @FXML
    private TableColumn<?, ?> stateColumn;

    @FXML
    private Label timeLabel;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button viewBookButton;


    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
    }

    @FXML
    void rBooksInventoryButtonClicked(ActionEvent event) throws IOException {
        switchScene("ReaderView/rBooksInventory-view.fxml", booksInventoryButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    @FXML
    void rHomeButtonClicked(ActionEvent event) throws IOException {
        switchScene("ReaderView/rDashBoard-view.fxml", dashBoardButton);
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null, "Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    @FXML
    void rViewBookButtonClicked(ActionEvent event) throws IOException {

    }

    @FXML
    void rAcquireButtonClicked(ActionEvent event) throws IOException {

    }
}
