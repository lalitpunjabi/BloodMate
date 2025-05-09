package com.blooddonation;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DonorManager {
    private List<Donor> donors = new ArrayList<>();
    private static final String FILE_NAME = "donors.txt";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DonorManager() {
        loadDonors();
    }

    public void addDonor(Donor donor) {
        // Check for duplicate donor by name
        if (donors.stream().anyMatch(d -> d.getName().equalsIgnoreCase(donor.getName()))) {
            throw new IllegalArgumentException("A donor with this name already exists.");
        }
        
        if (validateDonor(donor)) {
            donors.add(donor);
            saveDonors();
        }
    }

    public List<Donor> getAllDonors() {
        return new ArrayList<>(donors);
    }

    public Donor findDonor(String name) {
        return donors.stream()
            .filter(d -> d.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public void updateDonor(Donor oldDonor, Donor newDonor) {
        int index = donors.indexOf(oldDonor);
        if (index != -1 && validateDonor(newDonor)) {
            donors.set(index, newDonor);
            saveDonors();
        }
    }

    private boolean validateDonor(Donor donor) {
        try {
            // Validate date format
            LocalDate.parse(donor.getLastDonationDate(), dateFormatter);
            
            // Validate email format
            if (!donor.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Invalid email format");
                return false;
            }

            // Validate phone number (basic format)
            if (!donor.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
                System.out.println("Invalid phone number format");
                return false;
            }

            // Validate blood group format
            if (!donor.getBloodGroup().matches("^(A|B|AB|O)[+-]$")) {
                System.out.println("Invalid blood group format");
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD");
            return false;
        }
    }

    private void saveDonors() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Donor donor : donors) {
                writer.println(donor.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving donor data: " + e.getMessage());
        }
    }

    private void loadDonors() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    donors.add(Donor.fromString(line));
                } catch (Exception e) {
                    System.out.println("Error loading donor data: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // File might not exist yet — no problem
        }
    }

    public List<Donor> searchDonors(String query, String searchType) {
        return donors.stream()
            .filter(d -> {
                switch (searchType.toLowerCase()) {
                    case "name":
                        return d.getName().toLowerCase().contains(query.toLowerCase());
                    case "bloodgroup":
                        return d.getBloodGroup().equalsIgnoreCase(query);
                    case "location":
                        return d.getAddress().toLowerCase().contains(query.toLowerCase());
                    default:
                        return false;
                }
            })
            .toList();
    }

    public List<Donor> getEmergencyDonors(String bloodGroup) {
        return donors.stream()
            .filter(d -> d.isAvailableForEmergency() && 
                        d.getBloodGroup().equals(bloodGroup))
            .toList();
    }
}
