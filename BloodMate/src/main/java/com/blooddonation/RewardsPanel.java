package com.blooddonation;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import java.util.Map;

public class RewardsPanel extends VBox {
    private final RewardsManager rewardsManager;
    private final TextField donorIdField;
    private final Label pointsLabel;
    private final Label rewardsLabel;
    private final ListView<String> rewardsList;
    private final ListView<Map<String, Object>> topDonorsList;

    public RewardsPanel(RewardsManager rewardsManager) {
        this.rewardsManager = rewardsManager;
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_CENTER);

        // Title
        Label titleLabel = new Label("Donor Rewards System");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        getChildren().add(titleLabel);

        // Donor ID Input
        HBox donorInputBox = new HBox(10);
        donorInputBox.setAlignment(Pos.CENTER);
        Label donorIdLabel = new Label("Donor ID:");
        donorIdField = new TextField();
        Button checkButton = new Button("Check Rewards");
        donorInputBox.getChildren().addAll(donorIdLabel, donorIdField, checkButton);
        getChildren().add(donorInputBox);

        // Points Display
        pointsLabel = new Label("Total Points: 0");
        pointsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        getChildren().add(pointsLabel);

        // Rewards Display
        rewardsLabel = new Label("Earned Rewards:");
        rewardsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        getChildren().add(rewardsLabel);

        rewardsList = new ListView<>();
        rewardsList.setPrefHeight(150);
        getChildren().add(rewardsList);

        // Top Donors Section
        Label topDonorsLabel = new Label("Top Donors");
        topDonorsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        getChildren().add(topDonorsLabel);

        topDonorsList = new ListView<>();
        topDonorsList.setPrefHeight(200);
        getChildren().add(topDonorsList);

        // Refresh Top Donors Button
        Button refreshButton = new Button("Refresh Top Donors");
        getChildren().add(refreshButton);

        // Event Handlers
        checkButton.setOnAction(e -> checkDonorRewards());
        refreshButton.setOnAction(e -> refreshTopDonors());

        // Initial load of top donors
        refreshTopDonors();
    }

    private void checkDonorRewards() {
        String donorId = donorIdField.getText().trim();
        if (donorId.isEmpty()) {
            showAlert("Error", "Please enter a donor ID");
            return;
        }

        Map<String, Object> stats = rewardsManager.getDonorStats(donorId);
        if (stats == null) {
            showAlert("Not Found", "No records found for this donor ID");
            return;
        }

        // Update points display
        pointsLabel.setText(String.format("Total Points: %d", stats.get("totalPoints")));

        // Update rewards list
        List<String> earnedRewards = (List<String>) stats.get("earnedRewards");
        rewardsList.getItems().clear();
        rewardsList.getItems().addAll(earnedRewards);
    }

    private void refreshTopDonors() {
        List<Map<String, Object>> topDonors = rewardsManager.getTopDonors(5);
        topDonorsList.getItems().clear();
        
        for (Map<String, Object> donor : topDonors) {
            String displayText = String.format("Donor ID: %s\nPoints: %d\nDonations: %d\nRewards: %s",
                donor.get("donorId"),
                donor.get("totalPoints"),
                donor.get("donationCount"),
                String.join(", ", (List<String>) donor.get("earnedRewards")));
            
            topDonorsList.getItems().add(Map.of("display", displayText));
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 