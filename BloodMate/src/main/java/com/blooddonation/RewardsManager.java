package com.blooddonation;

import java.time.LocalDate;
import java.util.*;

public class RewardsManager {
    private static class Reward {
        private final String name;
        private final String description;
        private final int pointsRequired;
        private final boolean isActive;

        public Reward(String name, String description, int pointsRequired) {
            this.name = name;
            this.description = description;
            this.pointsRequired = pointsRequired;
            this.isActive = true;
        }
    }

    private static class DonorPoints {
        private int totalPoints;
        private int donationCount;
        private final List<String> earnedRewards;
        private final Map<String, LocalDate> rewardHistory;

        public DonorPoints() {
            this.totalPoints = 0;
            this.donationCount = 0;
            this.earnedRewards = new ArrayList<>();
            this.rewardHistory = new HashMap<>();
        }
    }

    private final Map<String, DonorPoints> donorPoints;
    private final List<Reward> availableRewards;
    private final Map<String, Integer> pointsPerDonation;

    public RewardsManager() {
        donorPoints = new HashMap<>();
        availableRewards = new ArrayList<>();
        pointsPerDonation = new HashMap<>();
        
        // Initialize points per donation based on blood type
        pointsPerDonation.put("O-", 100); // Universal donor gets more points
        pointsPerDonation.put("O+", 80);
        pointsPerDonation.put("A-", 80);
        pointsPerDonation.put("A+", 70);
        pointsPerDonation.put("B-", 80);
        pointsPerDonation.put("B+", 70);
        pointsPerDonation.put("AB-", 70);
        pointsPerDonation.put("AB+", 60);

        // Initialize available rewards
        initializeRewards();
    }

    private void initializeRewards() {
        availableRewards.add(new Reward("Bronze Donor", "Certificate of Appreciation", 200));
        availableRewards.add(new Reward("Silver Donor", "Special Recognition Badge", 500));
        availableRewards.add(new Reward("Gold Donor", "VIP Donor Status", 1000));
        availableRewards.add(new Reward("Platinum Donor", "Lifetime Achievement Award", 2000));
        availableRewards.add(new Reward("Emergency Hero", "Special Recognition for Emergency Donations", 300));
    }

    public void addDonation(String donorId, String bloodGroup, boolean isEmergency) {
        DonorPoints points = donorPoints.computeIfAbsent(donorId, k -> new DonorPoints());
        points.donationCount++;
        
        // Calculate points
        int basePoints = pointsPerDonation.getOrDefault(bloodGroup, 50);
        int bonusPoints = isEmergency ? 50 : 0;
        int totalPoints = basePoints + bonusPoints;
        
        points.totalPoints += totalPoints;
        checkAndAwardRewards(donorId);
    }

    private void checkAndAwardRewards(String donorId) {
        DonorPoints points = donorPoints.get(donorId);
        if (points == null) return;

        for (Reward reward : availableRewards) {
            if (points.totalPoints >= reward.pointsRequired && 
                !points.earnedRewards.contains(reward.name)) {
                points.earnedRewards.add(reward.name);
                points.rewardHistory.put(reward.name, LocalDate.now());
            }
        }
    }

    public List<String> getAvailableRewards(String donorId) {
        DonorPoints points = donorPoints.get(donorId);
        if (points == null) return new ArrayList<>();

        return availableRewards.stream()
            .filter(reward -> points.totalPoints >= reward.pointsRequired)
            .map(reward -> reward.name)
            .toList();
    }

    public List<String> getEarnedRewards(String donorId) {
        DonorPoints points = donorPoints.get(donorId);
        return points != null ? new ArrayList<>(points.earnedRewards) : new ArrayList<>();
    }

    public Map<String, Object> getDonorStats(String donorId) {
        DonorPoints points = donorPoints.get(donorId);
        if (points == null) return null;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPoints", points.totalPoints);
        stats.put("donationCount", points.donationCount);
        stats.put("earnedRewards", points.earnedRewards);
        stats.put("rewardHistory", points.rewardHistory);
        
        return stats;
    }

    public void addCustomReward(String name, String description, int pointsRequired) {
        availableRewards.add(new Reward(name, description, pointsRequired));
    }

    public List<Map<String, Object>> getTopDonors(int count) {
        return donorPoints.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue().totalPoints, e1.getValue().totalPoints))
            .limit(count)
            .map(entry -> {
                Map<String, Object> donorInfo = new HashMap<>();
                donorInfo.put("donorId", entry.getKey());
                donorInfo.put("totalPoints", entry.getValue().totalPoints);
                donorInfo.put("donationCount", entry.getValue().donationCount);
                donorInfo.put("earnedRewards", entry.getValue().earnedRewards);
                return donorInfo;
            })
            .toList();
    }

    public void resetDonorPoints(String donorId) {
        donorPoints.remove(donorId);
    }
} 