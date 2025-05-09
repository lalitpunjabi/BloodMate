package com.blooddonation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsManager {
    private final DonorManager donorManager;

    public StatisticsManager(DonorManager donorManager) {
        this.donorManager = donorManager;
    }

    public Map<String, Object> getGeneralStatistics() {
        List<Donor> donors = donorManager.getAllDonors();
        Map<String, Object> stats = new HashMap<>();

        // Total donors
        stats.put("totalDonors", donors.size());

        // Blood group distribution
        Map<String, Long> bloodGroupDistribution = donors.stream()
            .collect(Collectors.groupingBy(
                Donor::getBloodGroup,
                Collectors.counting()
            ));
        stats.put("bloodGroupDistribution", bloodGroupDistribution);

        // Emergency donors
        long emergencyDonors = donors.stream()
            .filter(Donor::isAvailableForEmergency)
            .count();
        stats.put("emergencyDonors", emergencyDonors);

        // Age distribution
        Map<String, Long> ageDistribution = donors.stream()
            .collect(Collectors.groupingBy(
                d -> {
                    int age = d.getAge();
                    if (age < 25) return "18-24";
                    if (age < 35) return "25-34";
                    if (age < 45) return "35-44";
                    if (age < 55) return "45-54";
                    return "55+";
                },
                Collectors.counting()
            ));
        stats.put("ageDistribution", ageDistribution);

        // Donation frequency
        Map<String, Long> donationFrequency = donors.stream()
            .collect(Collectors.groupingBy(
                d -> {
                    LocalDate lastDonation = LocalDate.parse(d.getLastDonationDate());
                    long monthsSince = ChronoUnit.MONTHS.between(lastDonation, LocalDate.now());
                    if (monthsSince < 3) return "Recent (0-3 months)";
                    if (monthsSince < 6) return "Medium (3-6 months)";
                    return "Long ago (>6 months)";
                },
                Collectors.counting()
            ));
        stats.put("donationFrequency", donationFrequency);

        // Location-based statistics
        Map<String, Long> locationDistribution = donors.stream()
            .collect(Collectors.groupingBy(
                d -> d.getAddress().split(",")[0], // City level
                Collectors.counting()
            ));
        stats.put("locationDistribution", locationDistribution);

        return stats;
    }

    public List<Map<String, Object>> getDonationTrends() {
        List<Donor> donors = donorManager.getAllDonors();
        List<Map<String, Object>> trends = new ArrayList<>();

        // Monthly donation trends
        Map<String, Long> monthlyTrends = donors.stream()
            .collect(Collectors.groupingBy(
                d -> LocalDate.parse(d.getLastDonationDate()).getMonth().toString(),
                Collectors.counting()
            ));

        for (Map.Entry<String, Long> entry : monthlyTrends.entrySet()) {
            Map<String, Object> trend = new HashMap<>();
            trend.put("month", entry.getKey());
            trend.put("count", entry.getValue());
            trends.add(trend);
        }

        return trends;
    }

    public Map<String, Object> getEmergencyStatistics() {
        List<Donor> donors = donorManager.getAllDonors();
        Map<String, Object> stats = new HashMap<>();

        // Emergency donors by blood group
        Map<String, Long> emergencyByBloodGroup = donors.stream()
            .filter(Donor::isAvailableForEmergency)
            .collect(Collectors.groupingBy(
                Donor::getBloodGroup,
                Collectors.counting()
            ));
        stats.put("emergencyByBloodGroup", emergencyByBloodGroup);

        // Emergency donors by location
        Map<String, Long> emergencyByLocation = donors.stream()
            .filter(Donor::isAvailableForEmergency)
            .collect(Collectors.groupingBy(
                d -> d.getAddress().split(",")[0],
                Collectors.counting()
            ));
        stats.put("emergencyByLocation", emergencyByLocation);

        return stats;
    }

    public List<Map<String, Object>> getDonorRecommendations() {
        List<Donor> donors = donorManager.getAllDonors();
        List<Map<String, Object>> recommendations = new ArrayList<>();

        // Find donors who haven't donated in a while
        donors.stream()
            .filter(d -> {
                LocalDate lastDonation = LocalDate.parse(d.getLastDonationDate());
                return ChronoUnit.MONTHS.between(lastDonation, LocalDate.now()) >= 6;
            })
            .forEach(d -> {
                Map<String, Object> rec = new HashMap<>();
                rec.put("name", d.getName());
                rec.put("bloodGroup", d.getBloodGroup());
                rec.put("lastDonation", d.getLastDonationDate());
                rec.put("contact", d.getPhoneNumber());
                rec.put("type", "Inactive Donor");
                recommendations.add(rec);
            });

        // Find potential emergency donors
        donors.stream()
            .filter(d -> !d.isAvailableForEmergency() && !d.hasChronicDisease())
            .forEach(d -> {
                Map<String, Object> rec = new HashMap<>();
                rec.put("name", d.getName());
                rec.put("bloodGroup", d.getBloodGroup());
                rec.put("contact", d.getPhoneNumber());
                rec.put("type", "Potential Emergency Donor");
                recommendations.add(rec);
            });

        return recommendations;
    }
} 