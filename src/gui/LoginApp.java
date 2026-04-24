package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.SocketClient;

public class LoginApp extends Application {
    private Stage primaryStage;
    private int currentUserId = -1;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }
    
    private void showLoginScreen() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        
        Label titleLabel = new Label("Booking System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        
        loginBtn.setOnAction(e -> {
            String response = SocketClient.sendCommand("LOGIN " + usernameField.getText() + " " + passwordField.getText());
            if (response.startsWith("SUCCESS")) {
                currentUserId = Integer.parseInt(response.split(" ")[1]);
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Login successful! User ID: " + currentUserId);
                showBookingScreen();
            } else {
                messageLabel.setText(response);
            }
        });
        
        registerBtn.setOnAction(e -> {
            String response = SocketClient.sendCommand("REGISTER " + usernameField.getText() + " " + passwordField.getText());
            if (response.startsWith("SUCCESS")) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(response);
            } else {
                messageLabel.setText(response);
            }
        });
        
        grid.add(titleLabel, 0, 0, 2, 1);
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginBtn, 0, 3);
        grid.add(registerBtn, 1, 3);
        grid.add(messageLabel, 0, 4, 2, 1);
        
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Login - Booking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showBookingScreen() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Create Booking");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label hotelLabel = new Label("Hotel:");
        TextField hotelField = new TextField();
        hotelField.setPromptText("e.g., Hilton");
        
        Label dateLabel = new Label("Date (YYYY-MM-DD):");
        TextField dateField = new TextField();
        dateField.setPromptText("2026-05-01");
        
        Button bookBtn = new Button("Book Now");
        Button viewBookingsBtn = new Button("View My Bookings");
        Button logoutBtn = new Button("Logout");
        
        Label messageLabel = new Label();
        
        bookBtn.setOnAction(e -> {
            String response = SocketClient.sendCommand("BOOK " + currentUserId + " " + hotelField.getText() + " " + dateField.getText());
            if (response.startsWith("SUCCESS")) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("✓ " + response);
                hotelField.clear();
                dateField.clear();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(response);
            }
        });
        
        viewBookingsBtn.setOnAction(e -> showBookingsScreen());
        
        logoutBtn.setOnAction(e -> {
            currentUserId = -1;
            showLoginScreen();
        });
        
        vbox.getChildren().addAll(titleLabel, hotelLabel, hotelField, dateLabel, dateField, 
                                   bookBtn, viewBookingsBtn, logoutBtn, messageLabel);
        
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setTitle("Booking - Booking System");
        primaryStage.setScene(scene);
    }
    
    private void showBookingsScreen() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        
        Label titleLabel = new Label("My Bookings");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TextArea bookingsArea = new TextArea();
        bookingsArea.setEditable(false);
        bookingsArea.setPrefHeight(300);
        
        Button refreshBtn = new Button("Refresh");
        Button backBtn = new Button("Back to Booking");
        
        refreshBtn.setOnAction(e -> {
            String response = SocketClient.sendCommand("GET_BOOKINGS " + currentUserId);
            bookingsArea.setText(response);
        });
        
        backBtn.setOnAction(e -> showBookingScreen());
        
        vbox.getChildren().addAll(titleLabel, bookingsArea, refreshBtn, backBtn);
        
        Scene scene = new Scene(vbox, 500, 450);
        primaryStage.setTitle("My Bookings - Booking System");
        primaryStage.setScene(scene);
        
        // Auto refresh on load
        refreshBtn.fire();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}