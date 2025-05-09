package com.blooddonation;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.blooddonation.EligibilityChecker.EligibilityResult;

public class BloodDonationGUI extends Application {
    private DonorManager donorManager;
    private RewardsManager rewardsManager;
    private VBox mainContent;
    private StackPane root;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        donorManager = new DonorManager();
        rewardsManager = new RewardsManager();
        
        // Create the main layout
        root = new StackPane();
        scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Create the main content area
        mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Create the header
        HBox header = createHeader();
        mainContent.getChildren().add(header);

        // Create the main menu
        GridPane menuGrid = createMainMenu();
        mainContent.getChildren().add(menuGrid);

        root.getChildren().add(mainContent);

        // Set up the stage
        primaryStage.setTitle("BloodMate");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #c62828;");

        // Add logo
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/blood_drop.svg")));
        logo.setFitHeight(60);
        logo.setFitWidth(60);

        // Add title
        Label title = new Label("BloodMate");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        header.getChildren().addAll(logo, title);
        return header;
    }

    private GridPane createMainMenu() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Create menu buttons
        Button[] buttons = {
            createMenuButton("Register Donor", "register-donor"),
            createMenuButton("Check Eligibility", "check-eligibility"),
            createMenuButton("Find Matches", "find-matches"),
            createMenuButton("View All Donors", "view-donors"),
            createMenuButton("Search Donors", "search-donors"),
            createMenuButton("Emergency Request", "emergency-request"),
            createMenuButton("Update Donor", "update-donor"),
            createMenuButton("Statistics", "statistics"),
            createMenuButton("Donor Rewards", "donor-rewards")
        };

        // Add buttons to grid
        for (int i = 0; i < buttons.length; i++) {
            grid.add(buttons[i], i % 4, i / 4);
        }

        return grid;
    }

    private Button createMenuButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        button.setPrefSize(250, 100);
        button.setStyle("-fx-font-size: 16px; -fx-background-color: #f5f5f5;");
        
        button.setOnAction(e -> handleButtonClick(id));
        
        return button;
    }

    private void handleButtonClick(String buttonId) {
        switch (buttonId) {
            case "register-donor" -> showRegisterDonorForm();
            case "check-eligibility" -> showEligibilityCheck();
            case "find-matches" -> showFindMatches();
            case "view-donors" -> showAllDonors();
            case "search-donors" -> showSearchDonors();
            case "emergency-request" -> showEmergencyRequest();
            case "update-donor" -> showUpdateDonor();
            case "statistics" -> showStatistics();
            case "donor-rewards" -> showRewardsPanel();
        }
    }

    private void showRegisterDonorForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setMaxWidth(600);

        // Create form fields
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        DatePicker lastDonationPicker = new DatePicker();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        TextField addressField = new TextField();
        TextField weightField = new TextField();
        CheckBox chronicDiseaseCheck = new CheckBox("Has Chronic Disease");
        TextArea medicalConditionsArea = new TextArea();
        CheckBox emergencyAvailableCheck = new CheckBox("Available for Emergency");
        TextField preferredCenterField = new TextField();
        TextField emergencyContactField = new TextField();
        TextField emergencyPhoneField = new TextField();

        // Add form fields to layout
        form.getChildren().addAll(
            createFormField("Full Name:", nameField),
            createFormField("Age:", ageField),
            createFormField("Blood Group:", bloodGroupCombo),
            createFormField("Last Donation Date:", lastDonationPicker),
            createFormField("Phone Number:", phoneField),
            createFormField("Email:", emailField),
            createFormField("Address:", addressField),
            createFormField("Weight (kg):", weightField),
            chronicDiseaseCheck,
            createFormField("Medical Conditions:", medicalConditionsArea),
            emergencyAvailableCheck,
            createFormField("Preferred Donation Center:", preferredCenterField),
            createFormField("Emergency Contact:", emergencyContactField),
            createFormField("Emergency Contact Phone:", emergencyPhoneField)
        );

        // Add submit button
        Button submitButton = new Button("Register Donor");
        submitButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            // Handle form submission
            try {
                Donor donor = new Donor(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    bloodGroupCombo.getValue(),
                    lastDonationPicker.getValue().toString(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    Double.parseDouble(weightField.getText()),
                    chronicDiseaseCheck.isSelected(),
                    medicalConditionsArea.getText(),
                    emergencyAvailableCheck.isSelected(),
                    preferredCenterField.getText(),
                    emergencyContactField.getText(),
                    emergencyPhoneField.getText()
                );
                donorManager.addDonor(donor);
                // Add initial points for registration
                rewardsManager.addDonation(donor.getId(), donor.getBloodGroup(), false);
                showAlert("Success", "Donor registered successfully!", Alert.AlertType.INFORMATION);
                clearForm(form);
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error", "Please fill all fields correctly.", Alert.AlertType.ERROR);
            }
        });

        form.getChildren().add(submitButton);
        showContent(form);
    }

    private HBox createFormField(String label, Control field) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label labelNode = new Label(label);
        labelNode.setPrefWidth(200);
        hbox.getChildren().addAll(labelNode, field);
        return hbox;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearForm(VBox form) {
        form.getChildren().stream()
            .filter(node -> node instanceof Control)
            .forEach(node -> {
                if (node instanceof TextField) {
                    ((TextField) node).clear();
                } else if (node instanceof TextArea) {
                    ((TextArea) node).clear();
                } else if (node instanceof CheckBox) {
                    ((CheckBox) node).setSelected(false);
                } else if (node instanceof ComboBox) {
                    ((ComboBox<?>) node).getSelectionModel().clearSelection();
                } else if (node instanceof DatePicker) {
                    ((DatePicker) node).setValue(null);
                }
            });
    }

    private void showContent(VBox content) {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(createHeader());
        
        // Add back button
        Button backButton = new Button("← Back to Main Menu");
        backButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            mainContent.getChildren().clear();
            mainContent.getChildren().add(createHeader());
            mainContent.getChildren().add(createMainMenu());
        });
        
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.getChildren().add(backButton);
        
        mainContent.getChildren().add(topBar);
        mainContent.getChildren().add(content);
    }

    private void showEligibilityCheck() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setMaxWidth(600);

        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField weightField = new TextField();
        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        DatePicker lastDonationPicker = new DatePicker();
        CheckBox chronicDiseaseCheck = new CheckBox("Has Chronic Disease");
        TextArea medicalConditionsArea = new TextArea();

        form.getChildren().addAll(
            createFormField("Name:", nameField),
            createFormField("Age:", ageField),
            createFormField("Weight (kg):", weightField),
            createFormField("Blood Group:", bloodGroupCombo),
            createFormField("Last Donation Date:", lastDonationPicker),
            chronicDiseaseCheck,
            createFormField("Medical Conditions:", medicalConditionsArea)
        );

        Button checkButton = new Button("Check Eligibility");
        checkButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        checkButton.setOnAction(e -> {
            try {
                Donor tempDonor = new Donor(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    bloodGroupCombo.getValue(),
                    lastDonationPicker.getValue().toString(),
                    "", // phone
                    "", // email
                    "", // address
                    Double.parseDouble(weightField.getText()),
                    chronicDiseaseCheck.isSelected(),
                    medicalConditionsArea.getText(),
                    false, // emergency available
                    "", // preferred center
                    "", // emergency contact
                    "" // emergency phone
                );
                
                EligibilityResult result = EligibilityChecker.checkEligibility(tempDonor);
                String message = result.isEligible() ? 
                    "You are eligible to donate blood!" : 
                    "You are not eligible to donate blood.\nReasons:\n" + String.join("\n", result.getReasons());
                
                showAlert(
                    "Eligibility Result",
                    message,
                    Alert.AlertType.INFORMATION
                );
            } catch (Exception ex) {
                showAlert("Error", "Please fill all required fields correctly.", Alert.AlertType.ERROR);
            }
        });

        form.getChildren().add(checkButton);
        showContent(form);
    }

    private void showFindMatches() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setMaxWidth(600);

        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        TextField locationField = new TextField();

        form.getChildren().addAll(
            createFormField("Required Blood Group:", bloodGroupCombo),
            createFormField("Location:", locationField)
        );

        Button findButton = new Button("Find Compatible Donors");
        findButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        findButton.setOnAction(e -> {
            List<Donor> allDonors = donorManager.getAllDonors();
            List<Donor> matches = BloodMatcher.findMatches(bloodGroupCombo.getValue(), allDonors);
            
            // Filter by location if provided
            if (locationField.getText() != null && !locationField.getText().trim().isEmpty()) {
                String location = locationField.getText().toLowerCase().trim();
                matches = matches.stream()
                    .filter(d -> d.getAddress().toLowerCase().contains(location))
                    .collect(Collectors.toList());
            }
            
            VBox resultsBox = new VBox(10);
            resultsBox.setPadding(new Insets(20));
            
            if (matches.isEmpty()) {
                resultsBox.getChildren().add(new Label("No compatible donors found."));
            } else {
                resultsBox.getChildren().add(new Label("Compatible Donors:"));
                for (Donor donor : matches) {
                    HBox donorInfo = new HBox(10);
                    donorInfo.getChildren().addAll(
                        new Label(donor.getName()),
                        new Label(donor.getBloodGroup()),
                        new Label(donor.getPhoneNumber())
                    );
                    resultsBox.getChildren().add(donorInfo);
                }
            }
            
            showContent(resultsBox);
        });

        form.getChildren().add(findButton);
        showContent(form);
    }

    private void showAllDonors() {
        VBox donorsBox = new VBox(10);
        donorsBox.setPadding(new Insets(20));
        
        List<Donor> donors = donorManager.getAllDonors();
        if (donors.isEmpty()) {
            donorsBox.getChildren().add(new Label("No donors registered."));
        } else {
            donorsBox.getChildren().add(new Label("Registered Donors:"));
            for (Donor donor : donors) {
                HBox donorInfo = new HBox(10);
                donorInfo.getChildren().addAll(
                    new Label(donor.getName()),
                    new Label(donor.getBloodGroup()),
                    new Label(donor.getPhoneNumber())
                );
                donorsBox.getChildren().add(donorInfo);
            }
        }
        
        showContent(donorsBox);
    }

    private void showSearchDonors() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setMaxWidth(600);

        TextField searchField = new TextField();
        ComboBox<String> searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("Name", "Blood Group", "Location");
        searchTypeCombo.setValue("Name");

        form.getChildren().addAll(
            createFormField("Search Type:", searchTypeCombo),
            createFormField("Search Term:", searchField)
        );

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        searchButton.setOnAction(e -> {
            List<Donor> results = donorManager.searchDonors(
                searchField.getText(),
                searchTypeCombo.getValue()
            );
            
            VBox resultsBox = new VBox(10);
            resultsBox.setPadding(new Insets(20));
            
            if (results.isEmpty()) {
                resultsBox.getChildren().add(new Label("No donors found."));
            } else {
                resultsBox.getChildren().add(new Label("Search Results:"));
                for (Donor donor : results) {
                    HBox donorInfo = new HBox(10);
                    donorInfo.getChildren().addAll(
                        new Label(donor.getName()),
                        new Label(donor.getBloodGroup()),
                        new Label(donor.getPhoneNumber())
                    );
                    resultsBox.getChildren().add(donorInfo);
                }
            }
            
            showContent(resultsBox);
        });

        form.getChildren().add(searchButton);
        showContent(form);
    }

    private void showEmergencyRequest() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setMaxWidth(600);

        // Create form fields
        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        TextField unitsField = new TextField();
        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(3);

        // Add form fields to layout
        form.getChildren().addAll(
            createFormField("Required Blood Group:", bloodGroupCombo),
            createFormField("Number of Units:", unitsField),
            createFormField("Additional Notes:", notesArea)
        );

        // Add submit button
        Button submitButton = new Button("Submit Emergency Request");
        submitButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            try {
                String bloodGroup = bloodGroupCombo.getValue();
                int units = Integer.parseInt(unitsField.getText());
                String notes = notesArea.getText();

                if (bloodGroup == null || bloodGroup.isEmpty()) {
                    showAlert("Error", "Please select a blood group", Alert.AlertType.ERROR);
                    return;
                }

                List<Donor> emergencyDonors = donorManager.getEmergencyDonors(bloodGroup);
                
                if (emergencyDonors.isEmpty()) {
                    showAlert("No Donors Available", 
                        "No emergency donors available for blood group " + bloodGroup, 
                        Alert.AlertType.WARNING);
                    return;
                }

                // Create a dialog to show available donors
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Available Emergency Donors");
                dialog.setHeaderText(String.format("The following donors are available for %d units of %s blood:%s", 
                    units, bloodGroup, notes.isEmpty() ? "" : "\n\nNotes: " + notes));

                VBox content = new VBox(10);
                content.setPadding(new Insets(10));

                for (Donor donor : emergencyDonors) {
                    HBox donorInfo = new HBox(10);
                    donorInfo.getChildren().addAll(
                        new Label(donor.getName()),
                        new Label("Phone: " + donor.getPhoneNumber()),
                        new Label("Location: " + donor.getPreferredDonationCenter())
                    );
                    content.getChildren().add(donorInfo);

                    // Add points for emergency availability
                    rewardsManager.addDonation(donor.getId(), donor.getBloodGroup(), true);
                }

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setPrefHeight(300);
                dialog.getDialogPane().setContent(scrollPane);

                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(closeButton);

                dialog.showAndWait();
                clearForm(form);

            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid number of units", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error", "An error occurred: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        form.getChildren().add(submitButton);
        showContent(form);
    }

    private void showUpdateDonor() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setMaxWidth(600);

        TextField searchField = new TextField();
        Button searchButton = new Button("Search Donor");
        searchButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        
        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, searchButton);
        
        form.getChildren().add(searchBox);
        showContent(form);
    }

    private void showStatistics() {
        VBox statsBox = new VBox(10);
        statsBox.setPadding(new Insets(20));
        
        // Create StatisticsManager instance
        StatisticsManager statsManager = new StatisticsManager(donorManager);
        
        // Get statistics
        Map<String, Object> stats = statsManager.getGeneralStatistics();
        
        // Display statistics
        statsBox.getChildren().add(new Label("Blood Donation Statistics"));
        statsBox.getChildren().add(new Label("Total Donors: " + stats.get("totalDonors")));
        statsBox.getChildren().add(new Label("Blood Group Distribution:"));
        
        @SuppressWarnings("unchecked")
        Map<String, Long> bloodGroupStats = (Map<String, Long>) stats.get("bloodGroupDistribution");
        if (bloodGroupStats != null) {
            for (Map.Entry<String, Long> entry : bloodGroupStats.entrySet()) {
                statsBox.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue()));
            }
        }
        
        showContent(statsBox);
    }

    private void showRewardsPanel() {
        RewardsPanel rewardsPanel = new RewardsPanel(rewardsManager);
        showContent(rewardsPanel);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 