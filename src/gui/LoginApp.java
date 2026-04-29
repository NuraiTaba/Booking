package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.SocketClient;

public class LoginApp extends Application {
    private static final String APP_BG = "-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);";
    private static final String PANEL_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.95); -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-radius: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 5);";
    private static final String PRIMARY_BUTTON = "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12 24; -fx-font-size: 14px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 2);";
    private static final String SECONDARY_BUTTON = "-fx-background-color: rgba(255, 255, 255, 0.95); -fx-text-fill: #2f3e55; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 20; -fx-border-color: rgba(102, 126, 234, 0.3); -fx-border-radius: 25; -fx-border-width: 1;";
    private static final String DANGER_BUTTON = "-fx-background-color: linear-gradient(to right, #ff6b6b, #ee5a52); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 20;";
    private static final String INPUT_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: rgba(255, 255, 255, 0.5); -fx-border-radius: 25; -fx-background-radius: 25; -fx-padding: 10 15; -fx-font-size: 14px; -fx-prompt-text-fill: #999;";
    private static final String TEXT_MUTED = "-fx-text-fill: #666666;";

    private Stage primaryStage;
    private int currentUserId = -1;
    private int currentUserRole = 0; // 0 - guest, 1 - owner, 2 - admin
    private ListView<HotelItem> hotelListView;
    private TextArea hotelDetailsArea;
    private ImageView hotelImageView;
    private Label statusLabel;
    private TextField searchField;
    private ListView<String> suggestionsListView;
    private ListView<String> recentSearchesListView;
    private ListView<String> popularDestinationsListView;
    private Pane mapPane;
    private TextArea recommendationsArea;
    private Spinner<Integer> minPriceSpinner;
    private Spinner<Integer> maxPriceSpinner;
    private Spinner<Integer> starsSpinner;
    private ComboBox<String> propertyTypeBox;
    private ComboBox<String> sortBox;
    private TextField districtField;
    private CheckBox wifiCheckBox;
    private CheckBox parkingCheckBox;
    private CheckBox poolCheckBox;
    private CheckBox breakfastCheckBox;
    private CheckBox freeCancellationCheckBox;
    private CheckBox availableOnlyCheckBox;
    private DatePicker checkInPicker;
    private DatePicker checkOutPicker;
    private Spinner<Integer> guestsSpinner;
    private Spinner<Integer> roomsSpinner;
    private TextField guestNameField;
    private TextField guestEmailField;
    private TextField guestPhoneField;
    private ComboBox<String> tripTypeBox;
    private TextArea bookingsArea;
    private TextArea ownerHotelsArea;
    private TextArea ownerBookingsArea;
    private TextArea wishlistArea;
    private TextField wishlistCollectionField;
    private TextField bookingIdField;
    private DatePicker changeCheckInPicker;
    private DatePicker changeCheckOutPicker;
    private TextField profileNameField;
    private TextField profileEmailField;
    private TextField profilePhoneField;
    private ComboBox<String> reviewSortBox;
    private Spinner<Integer> reviewMinRatingSpinner;
    private Spinner<Integer> reviewCleanlinessSpinner;
    private Spinner<Integer> reviewComfortSpinner;
    private Spinner<Integer> reviewStaffSpinner;
    private TextField reviewPhotoField;
    private TextField contactMessageField;
    private int currentPage = 0;
    private final int pageSize = 5;
    private List<HotelItem> currentHotels = new ArrayList<>();
    private HotelItem selectedHotel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    private void showLoginScreen() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle(APP_BG);

        // Иконка или заголовок
        Label titleLabel = new Label("🏨 Booking System");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 5, 0, 0, 2);");

        // Панель логина
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(PANEL_STYLE);
        grid.setMaxWidth(400);

        Label usernameLabel = new Label("👤 Username");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        styleInput(usernameField);

        Label passwordLabel = new Label("🔒 Password");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        styleInput(passwordField);

        Button loginBtn = new Button("🚀 Login");
        Button registerBtn = new Button("📝 Register");
        stylePrimary(loginBtn);
        styleSecondary(registerBtn);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");

        loginBtn.setOnAction(e -> {
            String response = send("LOGIN", usernameField.getText(), passwordField.getText());
            if (response.startsWith("SUCCESS")) {
                String[] parts = response.split(" ");
                currentUserId = Integer.parseInt(parts[1]);
                currentUserRole = Integer.parseInt(parts[2]); // Сервер должен вернуть роль
                showMainScreen();
            } else {
                messageLabel.setText("❌ " + response);
            }
        });

        registerBtn.setOnAction(e -> {
            String response = send("REGISTER", usernameField.getText(), passwordField.getText());
            messageLabel.setStyle(response.startsWith("SUCCESS") ? "-fx-text-fill: #4CAF50;" : "-fx-text-fill: #ff6b6b;");
            messageLabel.setText(response.startsWith("SUCCESS") ? "✅ " + response : "❌ " + response);
        });

        HBox buttons = new HBox(15, loginBtn, registerBtn);
        buttons.setAlignment(Pos.CENTER);

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 0, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 0, 3);
        grid.add(buttons, 0, 4);
        grid.add(messageLabel, 0, 5);

        mainContainer.getChildren().addAll(titleLabel, grid);

        primaryStage.setTitle("Login - Booking System");
        primaryStage.setScene(new Scene(mainContainer, 600, 500));
        primaryStage.show();
    }

    private void showMainScreen() {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(createSearchTab());
        tabs.getTabs().add(createBookingsTab());
        tabs.getTabs().add(createWishlistTab());
        tabs.getTabs().add(createProfileTab());
        if (currentUserRole >= 1) { // Owner or Admin
            tabs.getTabs().add(createOwnerTab());
        }
        if (currentUserRole == 2) { // Admin only
            tabs.getTabs().add(createAdminTab());
        }

        // Стиль для вкладок
        tabs.setStyle("-fx-tab-min-width: 120px; -fx-tab-max-width: 120px; -fx-tab-min-height: 40px;");

        Button logoutBtn = new Button("🚪 Logout");
        styleSecondary(logoutBtn);
        logoutBtn.setOnAction(e -> {
            currentUserId = -1;
            currentUserRole = 0;
            showLoginScreen();
        });

        Label userLabel = new Label("👤 User ID: " + currentUserId + " (Role: " + (currentUserRole == 0 ? "Guest" : currentUserRole == 1 ? "Owner" : "Admin") + ")");
        userLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        statusLabel = new Label("✅ Ready");
        statusLabel.setStyle("-fx-text-fill: #e8f5e8; -fx-font-size: 12px;");
        HBox topBar = new HBox(20, userLabel, statusLabel);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 2);");

        HBox topRight = new HBox(logoutBtn);
        topRight.setPadding(new Insets(15, 20, 15, 20));
        topRight.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(topBar);
        topPane.setRight(topRight);
        topPane.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2);");

        BorderPane root = new BorderPane();
        root.setStyle(APP_BG);
        root.setTop(topPane);
        root.setCenter(tabs);

        primaryStage.setTitle("🏨 Booking System");
        primaryStage.setScene(new Scene(root, 1300, 900));
        loadHotels();
    }

    private Tab createSearchTab() {
        searchField = new TextField();
        searchField.setPromptText("City, country, address or hotel");
        styleInput(searchField);
        suggestionsListView = new ListView<>();
        suggestionsListView.setPrefHeight(90);
        styleList(suggestionsListView);
        recentSearchesListView = new ListView<>();
        recentSearchesListView.setPrefHeight(120);
        styleList(recentSearchesListView);
        popularDestinationsListView = new ListView<>();
        popularDestinationsListView.setPrefHeight(120);
        styleList(popularDestinationsListView);
        recommendationsArea = new TextArea();
        recommendationsArea.setEditable(false);
        recommendationsArea.setPrefHeight(90);
        styleArea(recommendationsArea);
        mapPane = new Pane();
        mapPane.setPrefSize(520, 180);
        mapPane.setStyle("-fx-background-color: #eef6ff; -fx-border-color: #b7cbe6; -fx-border-radius: 8; -fx-background-radius: 8;");

        searchField.textProperty().addListener((obs, oldValue, newValue) -> loadAutocomplete(newValue));
        suggestionsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                searchField.setText(newValue);
                searchHotels();
            }
        });
        recentSearchesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("EMPTY")) {
                searchField.setText(newValue);
                searchHotels();
            }
        });
        popularDestinationsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("EMPTY")) {
                searchField.setText(newValue.split("\\|")[0]);
                searchHotels();
            }
        });

        checkInPicker = new DatePicker();
        checkOutPicker = new DatePicker();
        styleInput(checkInPicker);
        styleInput(checkOutPicker);
        guestsSpinner = new Spinner<>(1, 12, 2);
        roomsSpinner = new Spinner<>(1, 6, 1);
        styleInput(guestsSpinner);
        styleInput(roomsSpinner);
        guestNameField = new TextField();
        guestNameField.setPromptText("Full name");
        styleInput(guestNameField);
        guestEmailField = new TextField();
        guestEmailField.setPromptText("Email");
        styleInput(guestEmailField);
        guestPhoneField = new TextField();
        guestPhoneField.setPromptText("Phone");
        styleInput(guestPhoneField);
        tripTypeBox = new ComboBox<>();
        tripTypeBox.getItems().addAll("Leisure", "Business");
        tripTypeBox.getSelectionModel().selectFirst();
        styleInput(tripTypeBox);

        minPriceSpinner = new Spinner<>(0, 1000000, 0, 5000);
        maxPriceSpinner = new Spinner<>(0, 1000000, 1000000, 5000);
        starsSpinner = new Spinner<>(1, 5, 1);
        styleInput(minPriceSpinner);
        styleInput(maxPriceSpinner);
        styleInput(starsSpinner);
        propertyTypeBox = new ComboBox<>();
        propertyTypeBox.getItems().addAll("Any", "Hotel", "Apartment", "Hostel");
        propertyTypeBox.getSelectionModel().selectFirst();
        styleInput(propertyTypeBox);
        sortBox = new ComboBox<>();
        sortBox.getItems().addAll("rating", "price", "popularity");
        sortBox.getSelectionModel().selectFirst();
        styleInput(sortBox);
        districtField = new TextField();
        districtField.setPromptText("District");
        styleInput(districtField);
        wifiCheckBox = new CheckBox("Wi-Fi");
        parkingCheckBox = new CheckBox("Parking");
        poolCheckBox = new CheckBox("Pool");
        breakfastCheckBox = new CheckBox("Breakfast");
        freeCancellationCheckBox = new CheckBox("Free cancellation");
        availableOnlyCheckBox = new CheckBox("Available rooms");

        Button searchBtn = new Button("Search");
        Button filterBtn = new Button("Apply filters");
        Button allBtn = new Button("All hotels");
        Button bookBtn = new Button("Book selected");
        Button wishlistBtn = new Button("Save to wishlist");
        Button prevPageBtn = new Button("Prev");
        Button nextPageBtn = new Button("Next");
        Button recommendBtn = new Button("For you");
        Button contactBtn = new Button("Contact hotel");
        Button reviewsBtn = new Button("Load reviews");
        stylePrimary(searchBtn);
        styleSecondary(filterBtn, allBtn, wishlistBtn, prevPageBtn, nextPageBtn, recommendBtn, reviewsBtn, contactBtn);
        stylePrimary(bookBtn);
        contactMessageField = new TextField();
        contactMessageField.setPromptText("Message to hotel");
        styleInput(contactMessageField);
        Spinner<Integer> reviewRatingSpinner = new Spinner<>(1, 5, 5);
        reviewCleanlinessSpinner = new Spinner<>(1, 5, 5);
        reviewComfortSpinner = new Spinner<>(1, 5, 5);
        reviewStaffSpinner = new Spinner<>(1, 5, 5);
        styleInput(reviewRatingSpinner);
        styleInput(reviewCleanlinessSpinner);
        styleInput(reviewComfortSpinner);
        styleInput(reviewStaffSpinner);
        reviewSortBox = new ComboBox<>();
        reviewSortBox.getItems().addAll("date", "rating", "cleanliness", "comfort", "staff");
        reviewSortBox.getSelectionModel().selectFirst();
        styleInput(reviewSortBox);
        reviewMinRatingSpinner = new Spinner<>(1, 5, 1);
        styleInput(reviewMinRatingSpinner);
        reviewPhotoField = new TextField();
        reviewPhotoField.setPromptText("Review photo URL");
        styleInput(reviewPhotoField);
        TextField reviewCommentField = new TextField();
        reviewCommentField.setPromptText("Write your review");
        styleInput(reviewCommentField);
        Button addReviewBtn = new Button("Add review");
        styleSecondary(addReviewBtn);

        searchBtn.setOnAction(e -> searchHotels());
        filterBtn.setOnAction(e -> filterHotels());
        allBtn.setOnAction(e -> loadHotels());
        bookBtn.setOnAction(e -> bookSelectedHotel());
        wishlistBtn.setOnAction(e -> saveSelectedHotelToWishlist());
        reviewsBtn.setOnAction(e -> loadReviewsForSelectedHotel());
        addReviewBtn.setOnAction(e -> addReview(reviewRatingSpinner.getValue(), reviewCommentField));
        prevPageBtn.setOnAction(e -> showPage(currentPage - 1));
        nextPageBtn.setOnAction(e -> showPage(currentPage + 1));
        recommendBtn.setOnAction(e -> loadRecommendations());
        contactBtn.setOnAction(e -> contactSelectedHotel());

        GridPane searchPanel = new GridPane();
        searchPanel.setHgap(10);
        searchPanel.setVgap(10);
        searchPanel.setPadding(new Insets(16));
        searchPanel.setStyle(PANEL_STYLE + " -fx-padding: 16;");
        searchPanel.setMaxWidth(1080);
        searchPanel.add(new Label("Destination:"), 0, 0);
        searchPanel.add(searchField, 1, 0, 4, 1);
        Button refreshDiscoveryBtn = new Button("Discovery");
        styleSecondary(refreshDiscoveryBtn);
        refreshDiscoveryBtn.setOnAction(e -> loadDiscoveryPanels());
        searchPanel.add(refreshDiscoveryBtn, 5, 0);
        searchPanel.add(new Label("Check-in:"), 0, 1);
        searchPanel.add(checkInPicker, 1, 1);
        searchPanel.add(new Label("Check-out:"), 2, 1);
        searchPanel.add(checkOutPicker, 3, 1);
        searchPanel.add(new Label("Guests:"), 4, 1);
        searchPanel.add(guestsSpinner, 5, 1);
        searchPanel.add(new Label("Rooms:"), 0, 2);
        searchPanel.add(roomsSpinner, 1, 2);
        searchPanel.add(new Label("Trip:"), 2, 2);
        searchPanel.add(tripTypeBox, 3, 2);
        searchPanel.add(new Label("Min price:"), 0, 3);
        searchPanel.add(minPriceSpinner, 1, 3);
        searchPanel.add(new Label("Max price:"), 2, 3);
        searchPanel.add(maxPriceSpinner, 3, 3);
        searchPanel.add(new Label("Stars from:"), 4, 3);
        searchPanel.add(starsSpinner, 5, 3);
        searchPanel.add(new Label("Type:"), 0, 4);
        searchPanel.add(propertyTypeBox, 1, 4);
        searchPanel.add(new Label("District:"), 2, 4);
        searchPanel.add(districtField, 3, 4);
        searchPanel.add(new Label("Sort:"), 4, 4);
        searchPanel.add(sortBox, 5, 4);
        searchPanel.add(new HBox(8, wifiCheckBox, parkingCheckBox, poolCheckBox), 1, 5, 3, 1);
        searchPanel.add(new HBox(8, breakfastCheckBox, freeCancellationCheckBox, availableOnlyCheckBox), 4, 5, 2, 1);
        searchPanel.add(new Label("Guest:"), 0, 6);
        searchPanel.add(guestNameField, 1, 6, 2, 1);
        searchPanel.add(guestEmailField, 3, 6);
        searchPanel.add(guestPhoneField, 4, 6);
        searchPanel.add(new HBox(8, bookBtn, wishlistBtn), 5, 6);
        searchPanel.add(new HBox(8, searchBtn, filterBtn, allBtn), 1, 7, 3, 1);
        searchPanel.add(new HBox(8, prevPageBtn, nextPageBtn, recommendBtn, reviewsBtn), 1, 8, 4, 1);
        searchPanel.add(new Label("Review:"), 0, 9);
        searchPanel.add(reviewRatingSpinner, 1, 9);
        searchPanel.add(new HBox(6, new Label("Clean"), reviewCleanlinessSpinner, new Label("Comfort"), reviewComfortSpinner, new Label("Staff"), reviewStaffSpinner), 2, 9, 3, 1);
        searchPanel.add(addReviewBtn, 5, 9);
        searchPanel.add(new Label("Comment:"), 0, 10);
        searchPanel.add(reviewCommentField, 1, 10, 2, 1);
        searchPanel.add(reviewPhotoField, 3, 10, 2, 1);
        searchPanel.add(new HBox(6, new Label("Sort reviews"), reviewSortBox, new Label("Min"), reviewMinRatingSpinner), 5, 10);
        searchPanel.add(new Label("Contact:"), 0, 11);
        searchPanel.add(contactMessageField, 1, 11, 4, 1);
        searchPanel.add(contactBtn, 5, 11);

        hotelListView = new ListView<>();
        hotelListView.setPrefWidth(420);
        styleList(hotelListView);
        hotelListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            selectedHotel = newValue;
            showHotelDetails(newValue);
        });

        hotelImageView = new ImageView();
        hotelImageView.setFitWidth(520);
        hotelImageView.setFitHeight(260);
        hotelImageView.setPreserveRatio(true);
        hotelImageView.setSmooth(true);

        hotelDetailsArea = new TextArea();
        hotelDetailsArea.setEditable(false);
        hotelDetailsArea.setWrapText(true);
        styleArea(hotelDetailsArea);

        VBox discoveryBox = new VBox(8,
                new Label("Suggestions"), suggestionsListView,
                new Label("Recent searches"), recentSearchesListView,
                new Label("Popular destinations"), popularDestinationsListView,
                new Label("For you"), recommendationsArea,
                new Label("Map"), mapPane);
        discoveryBox.setPrefWidth(240);
        discoveryBox.setPadding(new Insets(10));
        discoveryBox.setStyle(PANEL_STYLE);

        VBox detailsBox = new VBox(10, hotelImageView, hotelDetailsArea);
        detailsBox.setPadding(new Insets(10));
        detailsBox.setStyle(PANEL_STYLE);
        VBox.setVgrow(hotelDetailsArea, Priority.ALWAYS);

        HBox content = new HBox(12, discoveryBox, hotelListView, detailsBox);
        content.setPadding(new Insets(12));
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        ScrollPane searchScroll = new ScrollPane(searchPanel);
        searchScroll.setFitToWidth(true);
        searchScroll.setPrefViewportHeight(260);
        searchScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox root = new VBox(searchScroll, content);
        root.setStyle(APP_BG);
        VBox.setVgrow(content, Priority.ALWAYS);

        Tab tab = new Tab("Search & Hotels", root);
        tab.setClosable(false);
        return tab;
    }

    private Tab createBookingsTab() {
        bookingsArea = new TextArea();
        bookingsArea.setEditable(false);
        styleArea(bookingsArea);

        Button refreshBtn = new Button("Refresh bookings");
        styleSecondary(refreshBtn);
        refreshBtn.setOnAction(e -> loadBookings());
        bookingIdField = new TextField();
        bookingIdField.setPromptText("Booking ID");
        styleInput(bookingIdField);
        changeCheckInPicker = new DatePicker();
        changeCheckOutPicker = new DatePicker();
        styleInput(changeCheckInPicker);
        styleInput(changeCheckOutPicker);
        Button cancelBtn = new Button("Cancel booking");
        Button changeDatesBtn = new Button("Change dates");
        styleDanger(cancelBtn);
        styleSecondary(changeDatesBtn);
        cancelBtn.setOnAction(e -> cancelBooking());
        changeDatesBtn.setOnAction(e -> changeBookingDates());

        HBox controls = new HBox(8,
                refreshBtn,
                bookingIdField,
                new Label("New check-in:"),
                changeCheckInPicker,
                new Label("New check-out:"),
                changeCheckOutPicker,
                changeDatesBtn,
                cancelBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(10, controls, bookingsArea);
        root.setPadding(new Insets(12));
        root.setStyle(APP_BG);
        VBox.setVgrow(bookingsArea, Priority.ALWAYS);

        Tab tab = new Tab("My bookings", root);
        tab.setClosable(false);
        tab.setOnSelectionChanged(e -> {
            if (tab.isSelected()) {
                loadBookings();
            }
        });
        return tab;
    }

    private Tab createWishlistTab() {
        wishlistArea = new TextArea();
        wishlistArea.setEditable(false);
        styleArea(wishlistArea);
        wishlistCollectionField = new TextField("Favorites");
        wishlistCollectionField.setPromptText("Collection");
        styleInput(wishlistCollectionField);
        TextField removeHotelIdField = new TextField();
        removeHotelIdField.setPromptText("Hotel ID");
        styleInput(removeHotelIdField);

        Button refreshBtn = new Button("Refresh wishlist");
        Button saveSelectedBtn = new Button("Save selected hotel");
        Button removeBtn = new Button("Remove by hotel ID");
        styleSecondary(refreshBtn, saveSelectedBtn);
        styleDanger(removeBtn);
        refreshBtn.setOnAction(e -> loadWishlist());
        saveSelectedBtn.setOnAction(e -> saveSelectedHotelToWishlist());
        removeBtn.setOnAction(e -> {
            String response = send("REMOVE_WISHLIST", String.valueOf(currentUserId), removeHotelIdField.getText());
            setStatus(response);
            loadWishlist();
        });

        HBox controls = new HBox(8, new Label("Collection:"), wishlistCollectionField, saveSelectedBtn, refreshBtn, removeHotelIdField, removeBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(10, controls, wishlistArea);
        root.setPadding(new Insets(12));
        root.setStyle(APP_BG);
        VBox.setVgrow(wishlistArea, Priority.ALWAYS);

        Tab tab = new Tab("Wishlist", root);
        tab.setClosable(false);
        tab.setOnSelectionChanged(e -> {
            if (tab.isSelected()) {
                loadWishlist();
            }
        });
        return tab;
    }

    private Tab createProfileTab() {
        profileNameField = new TextField();
        profileEmailField = new TextField();
        profilePhoneField = new TextField();
        styleInput(profileNameField);
        styleInput(profileEmailField);
        styleInput(profilePhoneField);

        Button loadBtn = new Button("Load profile");
        Button saveBtn = new Button("Save profile");
        styleSecondary(loadBtn);
        stylePrimary(saveBtn);
        loadBtn.setOnAction(e -> loadProfile());
        saveBtn.setOnAction(e -> saveProfile());

        GridPane form = new GridPane();
        form.setPadding(new Insets(12));
        form.setHgap(8);
        form.setVgap(8);
        form.setStyle(PANEL_STYLE + " -fx-padding: 18;");
        form.add(new Label("Full name:"), 0, 0);
        form.add(profileNameField, 1, 0);
        form.add(new Label("Email:"), 0, 1);
        form.add(profileEmailField, 1, 1);
        form.add(new Label("Phone:"), 0, 2);
        form.add(profilePhoneField, 1, 2);
        form.add(new HBox(8, loadBtn, saveBtn), 1, 3);

        BorderPane root = new BorderPane(form);
        root.setPadding(new Insets(12));
        root.setStyle(APP_BG);

        Tab tab = new Tab("Profile", root);
        tab.setClosable(false);
        tab.setOnSelectionChanged(e -> {
            if (tab.isSelected()) {
                loadProfile();
            }
        });
        return tab;
    }

    private Tab createOwnerTab() {
        TextField nameField = new TextField();
        TextField cityField = new TextField();
        TextField addressField = new TextField();
        TextField priceField = new TextField();
        TextField imageUrlField = new TextField();
        styleInput(nameField);
        styleInput(cityField);
        styleInput(addressField);
        styleInput(priceField);
        styleInput(imageUrlField);
        imageUrlField.setPromptText("https://...");
        ComboBox<String> ownerTypeBox = new ComboBox<>();
        ownerTypeBox.getItems().addAll("Hotel", "Apartment", "Hostel");
        ownerTypeBox.getSelectionModel().selectFirst();
        styleInput(ownerTypeBox);
        TextField ownerDistrictField = new TextField();
        ownerDistrictField.setPromptText("District");
        styleInput(ownerDistrictField);
        TextField ownerDistanceField = new TextField("1.0");
        styleInput(ownerDistanceField);
        Spinner<Integer> ownerRoomsField = new Spinner<>(1, 100, 5);
        styleInput(ownerRoomsField);
        CheckBox ownerWifiBox = new CheckBox("Wi-Fi");
        ownerWifiBox.setSelected(true);
        CheckBox ownerParkingBox = new CheckBox("Parking");
        CheckBox ownerPoolBox = new CheckBox("Pool");
        CheckBox ownerBreakfastBox = new CheckBox("Breakfast");
        CheckBox ownerCancellationBox = new CheckBox("Free cancellation");
        ownerCancellationBox.setSelected(true);
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        styleArea(descriptionArea);
        Spinner<Integer> starsField = new Spinner<>(1, 5, 3);
        styleInput(starsField);

        Button addHotelBtn = new Button("Add property");
        stylePrimary(addHotelBtn);
        addHotelBtn.setOnAction(e -> {
            try {
                String response = send("ADD_HOTEL",
                        nameField.getText(),
                        cityField.getText(),
                        addressField.getText(),
                        String.valueOf(starsField.getValue()),
                        priceField.getText(),
                        descriptionArea.getText(),
                        imageUrlField.getText(),
                        String.valueOf(currentUserId),
                        ownerTypeBox.getValue(),
                        ownerDistrictField.getText(),
                        ownerDistanceField.getText(),
                        String.valueOf(ownerRoomsField.getValue()),
                        String.valueOf(ownerWifiBox.isSelected()),
                        String.valueOf(ownerParkingBox.isSelected()),
                        String.valueOf(ownerPoolBox.isSelected()),
                        String.valueOf(ownerBreakfastBox.isSelected()),
                        String.valueOf(ownerCancellationBox.isSelected()));
                setStatus(response);
                if (response.startsWith("SUCCESS")) {
                    nameField.clear();
                    cityField.clear();
                    addressField.clear();
                    priceField.clear();
                    imageUrlField.clear();
                    ownerDistrictField.clear();
                    ownerDistanceField.setText("1.0");
                    descriptionArea.clear();
                    loadOwnerData();
                    loadHotels();
                }
            } catch (Exception ex) {
                setStatus("ERROR Invalid property data");
            }
        });

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.setStyle(PANEL_STYLE + " -fx-padding: 16;");
        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("City:"), 0, 1);
        form.add(cityField, 1, 1);
        form.add(new Label("Address:"), 0, 2);
        form.add(addressField, 1, 2);
        form.add(new Label("Stars:"), 0, 3);
        form.add(starsField, 1, 3);
        form.add(new Label("Price:"), 0, 4);
        form.add(priceField, 1, 4);
        form.add(new Label("Photo URL:"), 0, 5);
        form.add(imageUrlField, 1, 5);
        form.add(new Label("Type:"), 0, 6);
        form.add(ownerTypeBox, 1, 6);
        form.add(new Label("District:"), 0, 7);
        form.add(ownerDistrictField, 1, 7);
        form.add(new Label("Distance km:"), 0, 8);
        form.add(ownerDistanceField, 1, 8);
        form.add(new Label("Rooms:"), 0, 9);
        form.add(ownerRoomsField, 1, 9);
        form.add(new HBox(8, ownerWifiBox, ownerParkingBox, ownerPoolBox), 1, 10);
        form.add(new HBox(8, ownerBreakfastBox, ownerCancellationBox), 1, 11);
        form.add(new Label("Description:"), 0, 12);
        form.add(descriptionArea, 1, 12);
        form.add(addHotelBtn, 1, 13);

        ownerHotelsArea = new TextArea();
        ownerHotelsArea.setEditable(false);
        styleArea(ownerHotelsArea);
        ownerBookingsArea = new TextArea();
        ownerBookingsArea.setEditable(false);
        styleArea(ownerBookingsArea);

        Button refreshBtn = new Button("Refresh owner data");
        styleSecondary(refreshBtn);
        refreshBtn.setOnAction(e -> loadOwnerData());

        HBox lists = new HBox(10,
                labelledArea("My properties", ownerHotelsArea),
                labelledArea("Bookings for my properties", ownerBookingsArea));
        HBox.setHgrow(lists, Priority.ALWAYS);

        VBox root = new VBox(12, form, refreshBtn, lists);
        root.setPadding(new Insets(12));
        root.setStyle(APP_BG);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Tab tab = new Tab("Owner dashboard", scrollPane);
        tab.setClosable(false);
        tab.setOnSelectionChanged(e -> {
            if (tab.isSelected()) {
                loadOwnerData();
            }
        });
        return tab;
    }

    private Tab createAdminTab() {
        TextArea usersArea = new TextArea();
        usersArea.setEditable(false);
        styleArea(usersArea);

        TextArea hotelsArea = new TextArea();
        hotelsArea.setEditable(false);
        styleArea(hotelsArea);

        TextArea statsArea = new TextArea();
        statsArea.setEditable(false);
        styleArea(statsArea);

        Button refreshBtn = new Button("📊 Refresh admin data");
        stylePrimary(refreshBtn);
        refreshBtn.setOnAction(e -> {
            // Load users, hotels, stats
            String users = send("GET_ALL_USERS");
            usersArea.setText(users);
            String hotels = send("GET_ALL_HOTELS");
            hotelsArea.setText(hotels);
            String stats = send("GET_STATS");
            statsArea.setText(stats);
        });

        VBox root = new VBox(12,
                new Label("👥 All Users:"), usersArea,
                new Label("🏨 All Hotels:"), hotelsArea,
                new Label("📈 Statistics:"), statsArea,
                refreshBtn);
        root.setPadding(new Insets(12));
        root.setStyle(APP_BG);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Tab tab = new Tab("Admin panel", scrollPane);
        tab.setClosable(false);
        return tab;
    }

    private VBox labelledArea(String title, TextArea area) {
        VBox box = new VBox(6, new Label(title), area);
        VBox.setVgrow(area, Priority.ALWAYS);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private void loadHotels() {
        String response = send("GET_HOTELS", getSortValue());
        updateHotelList(response);
        loadDiscoveryPanels();
    }

    private void searchHotels() {
        if (searchField.getText().trim().isEmpty()) {
            loadHotels();
            return;
        }
        String response = send("SEARCH", searchField.getText().trim());
        send("SAVE_SEARCH", String.valueOf(currentUserId), searchField.getText().trim());
        updateHotelList(response);
        loadRecentSearches();
    }

    private void filterHotels() {
        String response = send("FILTER",
                String.valueOf(minPriceSpinner.getValue()),
                String.valueOf(maxPriceSpinner.getValue()),
                String.valueOf(starsSpinner.getValue()),
                propertyTypeBox.getValue(),
                String.valueOf(wifiCheckBox.isSelected()),
                String.valueOf(parkingCheckBox.isSelected()),
                String.valueOf(poolCheckBox.isSelected()),
                String.valueOf(breakfastCheckBox.isSelected()),
                String.valueOf(freeCancellationCheckBox.isSelected()),
                districtField.getText(),
                String.valueOf(availableOnlyCheckBox.isSelected()),
                getSortValue());
        updateHotelList(response);
    }

    private void updateHotelList(String response) {
        hotelListView.getItems().clear();
        currentHotels = parseHotels(response);
        currentPage = 0;
        showPage(0);
        if (currentHotels.isEmpty()) {
            hotelDetailsArea.setText(response);
        }
        renderMap(currentHotels);
    }

    private void showPage(int page) {
        if (currentHotels == null || currentHotels.isEmpty()) {
            hotelListView.getItems().clear();
            return;
        }
        int maxPage = (currentHotels.size() - 1) / pageSize;
        currentPage = Math.max(0, Math.min(page, maxPage));
        int from = currentPage * pageSize;
        int to = Math.min(from + pageSize, currentHotels.size());
        hotelListView.getItems().setAll(currentHotels.subList(from, to));
        setStatus("Hotels " + (from + 1) + "-" + to + " of " + currentHotels.size());
        hotelListView.getSelectionModel().selectFirst();
    }

    private List<HotelItem> parseHotels(String response) {
        List<HotelItem> hotels = new ArrayList<>();
        if (response == null || response.startsWith("ERROR") || response.startsWith("EMPTY")) {
            return hotels;
        }
        String[] lines = response.split("\\R");
        for (String line : lines) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 6) {
                try {
                    hotels.add(new HotelItem(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4]),
                            Double.parseDouble(parts[5])));
                } catch (NumberFormatException ignored) {
                    setStatus("Skipped invalid hotel row: " + line);
                }
            }
        }
        return hotels;
    }

    private void showHotelDetails(HotelItem hotel) {
        if (hotel == null) {
            hotelImageView.setImage(null);
            hotelDetailsArea.clear();
            return;
        }
        String response = send("HOTEL_DETAIL", String.valueOf(hotel.id));
        String[] parts = response.split("\\|", -1);
        if (parts.length >= 8) {
            String imageUrl = parts.length >= 9 ? parts[8] : "";
            showHotelImage(imageUrl);
            String reviews = getFilteredReviews(hotel.id);
            String gallery = parts.length >= 20 ? parts[19] : "";
            String policies = parts.length >= 21 ? parts[20] : "";
            String rooms = parts.length >= 22 ? parts[21] : "";
            String contact = parts.length >= 23 ? parts[22] : "";
            String extras = "";
            if (parts.length >= 19) {
                extras = "Type: " + parts[9] + "\n" +
                         "District: " + parts[10] + "\n" +
                         "Distance to center: " + parts[11] + " km\n" +
                         "Popularity: " + parts[12] + "\n" +
                         "Available rooms: " + parts[13] + "\n" +
                         "Amenities: " + formatAmenities(parts) + "\n" +
                         "Cancellation: " + ("true".equalsIgnoreCase(parts[18]) ? "Free" : "Non-refundable") + "\n\n";
            }
            hotelDetailsArea.setText(
                    "Name: " + parts[1] + "\n" +
                    "City: " + parts[2] + "\n" +
                    "Address: " + parts[3] + "\n" +
                    "Stars: " + parts[4] + "\n" +
                    "Price per night: " + parts[5] + "\n" +
                    "Rating: " + parts[6] + "\n\n" +
                    extras +
                    "Gallery:\n" + gallery.replace(",", "\n") + "\n\n" +
                    "Policies:\n" + policies + "\n\n" +
                    "Available rooms:\n" + rooms.replace(";", "\n") + "\n\n" +
                    "Contact: " + contact + "\n\n" +
                    "Description:\n" + parts[7] + "\n\n" +
                    "Reviews:\n" + reviews + "\n\n" +
                    "Choose dates and press Book selected to create a reservation.");
        } else {
            hotelImageView.setImage(null);
            hotelDetailsArea.setText(response);
        }
    }

    private void showHotelImage(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty() || imageUrl.equalsIgnoreCase("null")) {
            hotelImageView.setImage(null);
            return;
        }
        try {
            hotelImageView.setImage(new Image(imageUrl, true));
        } catch (IllegalArgumentException ex) {
            hotelImageView.setImage(null);
            setStatus("Could not load hotel image");
        }
    }

    private void loadAutocomplete(String query) {
        if (suggestionsListView == null || query == null || query.trim().length() < 2) {
            if (suggestionsListView != null) {
                suggestionsListView.getItems().clear();
            }
            return;
        }
        String response = send("AUTOCOMPLETE", query.trim());
        suggestionsListView.getItems().setAll(splitLines(response));
    }

    private void loadDiscoveryPanels() {
        loadRecentSearches();
        if (popularDestinationsListView != null) {
            popularDestinationsListView.getItems().setAll(splitLines(send("POPULAR_DESTINATIONS")));
        }
    }

    private void loadRecentSearches() {
        if (recentSearchesListView != null) {
            recentSearchesListView.getItems().setAll(splitLines(send("RECENT_SEARCHES", String.valueOf(currentUserId))));
        }
    }

    private List<String> splitLines(String response) {
        List<String> lines = new ArrayList<>();
        if (response == null || response.trim().isEmpty()) {
            return lines;
        }
        for (String line : response.split("\\R")) {
            if (!line.trim().isEmpty()) {
                lines.add(line);
            }
        }
        return lines;
    }

    private void renderMap(List<HotelItem> hotels) {
        if (mapPane == null) {
            return;
        }
        mapPane.getChildren().clear();
        Text title = new Text(12, 20, "Result map");
        title.setFill(Color.web("#27527a"));
        mapPane.getChildren().add(title);
        if (hotels.isEmpty()) {
            mapPane.getChildren().add(new Text(12, 55, "No hotels to show"));
            return;
        }
        int index = 0;
        for (HotelItem hotel : hotels) {
            double x = 35 + (index % 4) * 115 + Math.min(40, hotel.rating * 6);
            double y = 55 + (index / 4) * 55 + Math.min(20, hotel.stars * 3);
            Circle marker = new Circle(x, y, 9, Color.web("#006ce4"));
            marker.setStroke(Color.WHITE);
            marker.setStrokeWidth(2);
            Text label = new Text(x + 12, y + 4, hotel.name.length() > 16 ? hotel.name.substring(0, 16) : hotel.name);
            marker.setOnMouseClicked(e -> {
                hotelListView.getSelectionModel().select(hotel);
                hotelListView.scrollTo(hotel);
            });
            label.setOnMouseClicked(e -> {
                hotelListView.getSelectionModel().select(hotel);
                hotelListView.scrollTo(hotel);
            });
            mapPane.getChildren().addAll(marker, label);
            index++;
        }
    }

    private void loadReviewsForSelectedHotel() {
        if (selectedHotel == null) {
            setStatus("Select a hotel first");
            return;
        }
        String response = getFilteredReviews(selectedHotel.id);
        hotelDetailsArea.appendText("\n\nReviews:\n" + response);
    }

    private String getFilteredReviews(int hotelId) {
        String sort = reviewSortBox == null || reviewSortBox.getValue() == null ? "date" : reviewSortBox.getValue();
        int minRating = reviewMinRatingSpinner == null ? 1 : reviewMinRatingSpinner.getValue();
        return send("GET_REVIEWS", String.valueOf(hotelId), sort, String.valueOf(minRating));
    }

    private void addReview(int rating, TextField commentField) {
        if (selectedHotel == null) {
            setStatus("Select a hotel first");
            return;
        }
        String comment = commentField.getText().trim();
        if (comment.isEmpty()) {
            setStatus("Write a review comment");
            return;
        }
        String response = send("ADD_REVIEW",
                String.valueOf(selectedHotel.id),
                String.valueOf(currentUserId),
                "0",
                String.valueOf(rating),
                String.valueOf(reviewCleanlinessSpinner.getValue()),
                String.valueOf(reviewComfortSpinner.getValue()),
                String.valueOf(reviewStaffSpinner.getValue()),
                comment,
                reviewPhotoField.getText());
        setStatus(response);
        if (response.startsWith("SUCCESS")) {
            commentField.clear();
            reviewPhotoField.clear();
            showHotelDetails(selectedHotel);
        }
    }

    private void loadRecommendations() {
        String response = send("RECOMMENDATIONS", String.valueOf(currentUserId));
        if (recommendationsArea != null) {
            recommendationsArea.setText(response);
        }
        updateHotelList(response);
    }

    private void contactSelectedHotel() {
        if (selectedHotel == null) {
            setStatus("Select a hotel first");
            return;
        }
        String message = contactMessageField.getText().trim();
        if (message.isEmpty()) {
            setStatus("Write a message to hotel");
            return;
        }
        String response = send("CONTACT_HOTEL", String.valueOf(currentUserId), String.valueOf(selectedHotel.id), message);
        setStatus(response);
        contactMessageField.clear();
    }

    private void bookSelectedHotel() {
        if (selectedHotel == null) {
            setStatus("Select a hotel first");
            return;
        }
        if (checkInPicker.getValue() == null) {
            setStatus("Select check-in date");
            return;
        }
        if (checkOutPicker.getValue() == null) {
            setStatus("Select check-out date");
            return;
        }
        if (guestNameField.getText().trim().isEmpty() || guestEmailField.getText().trim().isEmpty()
                || guestPhoneField.getText().trim().isEmpty()) {
            setStatus("Enter guest name, email and phone");
            return;
        }
        String response = send("BOOK_FULL",
                String.valueOf(currentUserId),
                selectedHotel.name,
                checkInPicker.getValue().toString(),
                checkOutPicker.getValue().toString(),
                String.valueOf(guestsSpinner.getValue()),
                String.valueOf(roomsSpinner.getValue()),
                guestNameField.getText(),
                guestEmailField.getText(),
                guestPhoneField.getText());
        setStatus(response);
        if (response.startsWith("SUCCESS")) {
            guestNameField.clear();
            guestEmailField.clear();
            guestPhoneField.clear();
            loadBookings();
            loadHotels();
        }
    }

    private void loadBookings() {
        if (bookingsArea != null) {
            bookingsArea.setText(send("GET_BOOKINGS", String.valueOf(currentUserId)));
        }
    }

    private void cancelBooking() {
        String bookingId = bookingIdField.getText().trim();
        if (bookingId.isEmpty()) {
            setStatus("Enter booking ID");
            return;
        }
        String response = send("CANCEL_BOOKING", String.valueOf(currentUserId), bookingId);
        setStatus(response);
        loadBookings();
    }

    private void changeBookingDates() {
        String bookingId = bookingIdField.getText().trim();
        if (bookingId.isEmpty() || changeCheckInPicker.getValue() == null || changeCheckOutPicker.getValue() == null) {
            setStatus("Enter booking ID and new dates");
            return;
        }
        String response = send("CHANGE_BOOKING_DATES",
                String.valueOf(currentUserId),
                bookingId,
                changeCheckInPicker.getValue().toString(),
                changeCheckOutPicker.getValue().toString());
        setStatus(response);
        loadBookings();
    }

    private void saveSelectedHotelToWishlist() {
        if (selectedHotel == null) {
            setStatus("Select a hotel first");
            return;
        }
        String collection = wishlistCollectionField == null ? "Favorites" : wishlistCollectionField.getText();
        String response = send("ADD_WISHLIST",
                String.valueOf(currentUserId),
                String.valueOf(selectedHotel.id),
                collection);
        setStatus(response);
        loadWishlist();
    }

    private void loadWishlist() {
        if (wishlistArea != null) {
            wishlistArea.setText(send("GET_WISHLIST", String.valueOf(currentUserId)));
        }
    }

    private void loadProfile() {
        if (profileNameField == null) {
            return;
        }
        String response = send("GET_PROFILE", String.valueOf(currentUserId));
        if (response.startsWith("ERROR")) {
            setStatus(response);
            return;
        }
        String[] parts = response.split("\\|", -1);
        if (parts.length >= 4) {
            profileNameField.setText(parts[1]);
            profileEmailField.setText(parts[2]);
            profilePhoneField.setText(parts[3]);
            setStatus("Profile loaded for " + parts[0]);
        }
    }

    private void saveProfile() {
        String response = send("UPDATE_PROFILE",
                String.valueOf(currentUserId),
                profileNameField.getText(),
                profileEmailField.getText(),
                profilePhoneField.getText());
        setStatus(response);
    }

    private void loadOwnerData() {
        if (ownerHotelsArea != null) {
            ownerHotelsArea.setText(send("MY_HOTELS", String.valueOf(currentUserId)));
        }
        if (ownerBookingsArea != null) {
            ownerBookingsArea.setText(send("OWNER_BOOKINGS", String.valueOf(currentUserId)));
        }
    }

    private String send(String command, String... values) {
        StringBuilder sb = new StringBuilder(command);
        for (String value : values) {
            String safeValue = value == null ? "" : value.replace("|", "/").replace("\r", " ").replace("\n", " ");
            sb.append("|").append(safeValue);
        }
        return SocketClient.sendCommand(sb.toString());
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private String getSortValue() {
        return sortBox == null || sortBox.getValue() == null ? "rating" : sortBox.getValue();
    }

    private String formatAmenities(String[] parts) {
        List<String> amenities = new ArrayList<>();
        if ("true".equalsIgnoreCase(parts[14])) {
            amenities.add("Wi-Fi");
        }
        if ("true".equalsIgnoreCase(parts[15])) {
            amenities.add("parking");
        }
        if ("true".equalsIgnoreCase(parts[16])) {
            amenities.add("pool");
        }
        if ("true".equalsIgnoreCase(parts[17])) {
            amenities.add("breakfast included");
        }
        return amenities.isEmpty() ? "No selected amenities" : String.join(", ", amenities);
    }

    private void stylePrimary(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle(PRIMARY_BUTTON);
            button.setMinWidth(120);
            button.setPrefHeight(42);
        }
    }

    private void styleSecondary(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle(SECONDARY_BUTTON);
            button.setMinWidth(120);
            button.setPrefHeight(42);
        }
    }

    private void styleDanger(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle(DANGER_BUTTON);
            button.setMinWidth(120);
            button.setPrefHeight(42);
        }
    }

    private void styleInput(Control control) {
        control.setStyle(INPUT_STYLE);
    }

    private void styleArea(TextArea area) {
        area.setStyle(INPUT_STYLE + " -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;");
    }

    private void styleList(ListView<?> listView) {
        listView.setStyle("-fx-background-color: white; -fx-border-color: #d8e1ef; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    private static class HotelItem {
        private final int id;
        private final String name;
        private final String city;
        private final int stars;
        private final int price;
        private final double rating;

        private HotelItem(int id, String name, String city, int stars, int price, double rating) {
            this.id = id;
            this.name = name;
            this.city = city;
            this.stars = stars;
            this.price = price;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return name + " | " + city + " | " + stars + " stars | " + price + " KZT | rating " + rating;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
