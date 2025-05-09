package com.blooddonation;

import java.util.Scanner;
import java.util.List;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DonorManager donorManager = new DonorManager();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== BloodMate ====");
            System.out.println("1. Register New Donor");
            System.out.println("2. Check Donor Eligibility");
            System.out.println("3. Find Compatible Donors");
            System.out.println("4. View All Donors");
            System.out.println("5. Search Donors");
            System.out.println("6. Emergency Blood Request");
            System.out.println("7. Update Donor Information");
            System.out.println("8. View Donation Statistics");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> registerDonor();
                case 2 -> checkEligibility();
                case 3 -> findMatches();
                case 4 -> viewAllDonors();
                case 5 -> searchDonors();
                case 6 -> emergencyRequest();
                case 7 -> updateDonor();
                case 8 -> viewStatistics();
                case 9 -> {
                    System.out.println("Thank you for using BloodMate!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerDonor() {
        System.out.println("\n=== Register New Donor ===");
        
        System.out.print("Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Blood Group (e.g. A+, O-): ");
        String bloodGroup = scanner.nextLine().toUpperCase();

        System.out.print("Last Donation Date (YYYY-MM-DD): ");
        String lastDonation = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Weight (kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Do you have any chronic diseases? (yes/no): ");
        boolean hasChronicDisease = scanner.nextLine().toLowerCase().startsWith("y");

        System.out.print("Medical Conditions (if any): ");
        String medicalConditions = scanner.nextLine();

        System.out.print("Available for Emergency Donation? (yes/no): ");
        boolean emergencyAvailable = scanner.nextLine().toLowerCase().startsWith("y");

        System.out.print("Preferred Donation Center: ");
        String preferredCenter = scanner.nextLine();

        System.out.print("Emergency Contact Name: ");
        String emergencyContact = scanner.nextLine();

        System.out.print("Emergency Contact Phone: ");
        String emergencyPhone = scanner.nextLine();

        Donor donor = new Donor(name, age, bloodGroup, lastDonation, phone, email, 
                              address, weight, hasChronicDisease, medicalConditions,
                              emergencyAvailable, preferredCenter, emergencyContact, 
                              emergencyPhone);

        donorManager.addDonor(donor);
        System.out.println("\n✅ Donor registered successfully!");
    }

    private static void checkEligibility() {
        System.out.print("\nEnter donor name: ");
        String name = scanner.nextLine();

        for (Donor d : donorManager.getAllDonors()) {
            if (d.getName().equalsIgnoreCase(name)) {
                EligibilityChecker.EligibilityResult result = EligibilityChecker.checkEligibility(d);
                if (result.isEligible()) {
                    System.out.println("\n✅ " + name + " is eligible to donate blood.");
                } else {
                    System.out.println("\n❌ " + name + " is not eligible to donate now.");
                    System.out.println("Reasons:");
                    for (String reason : result.getReasons()) {
                        System.out.println("- " + reason);
                    }
                }
                return;
            }
        }
        System.out.println("Donor not found.");
    }

    private static void findMatches() {
        System.out.print("\nEnter recipient's blood group: ");
        String recipientGroup = scanner.nextLine().toUpperCase();

        List<Donor> matches = BloodMatcher.findMatches(recipientGroup, donorManager.getAllDonors());
        if (matches.isEmpty()) {
            System.out.println("No compatible donors found.");
        } else {
            System.out.println("\nCompatible donors:");
            for (Donor d : matches) {
                System.out.println("\nName: " + d.getName());
                System.out.println("Blood Group: " + d.getBloodGroup());
                System.out.println("Contact: " + d.getPhoneNumber());
                System.out.println("Location: " + d.getAddress());
                System.out.println("Emergency Available: " + (d.isAvailableForEmergency() ? "Yes" : "No"));
            }
        }
    }

    private static void viewAllDonors() {
        List<Donor> all = donorManager.getAllDonors();
        if (all.isEmpty()) {
            System.out.println("No donors registered yet.");
        } else {
            System.out.println("\n=== Registered Donors ===");
            for (Donor d : all) {
                System.out.println("\nName: " + d.getName());
                System.out.println("Age: " + d.getAge());
                System.out.println("Blood Group: " + d.getBloodGroup());
                System.out.println("Last Donation: " + d.getLastDonationDate());
                System.out.println("Contact: " + d.getPhoneNumber());
                System.out.println("Location: " + d.getAddress());
                System.out.println("Emergency Available: " + (d.isAvailableForEmergency() ? "Yes" : "No"));
                System.out.println("----------------------------------------");
            }
        }
    }

    private static void searchDonors() {
        System.out.println("\nSearch by:");
        System.out.println("1. Name");
        System.out.println("2. Blood Group");
        System.out.println("3. Location");
        System.out.print("Choose option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Donor> results = donorManager.getAllDonors().stream()
            .filter(d -> {
                switch (choice) {
                    case 1: return d.getName().toLowerCase().contains(searchTerm);
                    case 2: return d.getBloodGroup().toLowerCase().contains(searchTerm);
                    case 3: return d.getAddress().toLowerCase().contains(searchTerm);
                    default: return false;
                }
            })
            .toList();

        if (results.isEmpty()) {
            System.out.println("No matching donors found.");
        } else {
            System.out.println("\nSearch Results:");
            for (Donor d : results) {
                System.out.println("\nName: " + d.getName());
                System.out.println("Blood Group: " + d.getBloodGroup());
                System.out.println("Contact: " + d.getPhoneNumber());
                System.out.println("Location: " + d.getAddress());
            }
        }
    }

    private static void emergencyRequest() {
        System.out.println("\n=== Emergency Blood Request ===");
        System.out.print("Required Blood Group: ");
        String bloodGroup = scanner.nextLine().toUpperCase();

        List<Donor> emergencyDonors = donorManager.getAllDonors().stream()
            .filter(d -> d.isAvailableForEmergency() && 
                        BloodMatcher.findMatches(bloodGroup, List.of(d)).contains(d))
            .toList();

        if (emergencyDonors.isEmpty()) {
            System.out.println("No emergency donors available for this blood group.");
        } else {
            System.out.println("\nEmergency Donors Available:");
            for (Donor d : emergencyDonors) {
                System.out.println("\nName: " + d.getName());
                System.out.println("Blood Group: " + d.getBloodGroup());
                System.out.println("Contact: " + d.getPhoneNumber());
                System.out.println("Location: " + d.getAddress());
                System.out.println("Emergency Contact: " + d.getEmergencyContact() + 
                                 " (" + d.getEmergencyContactPhone() + ")");
            }
        }
    }

    private static void updateDonor() {
        System.out.print("\nEnter donor name to update: ");
        String name = scanner.nextLine();

        Donor donor = donorManager.findDonor(name);
        if (donor == null) {
            System.out.println("Donor not found.");
            return;
        }

        System.out.println("\nCurrent Information:");
        System.out.println("1. Name: " + donor.getName());
        System.out.println("2. Age: " + donor.getAge());
        System.out.println("3. Blood Group: " + donor.getBloodGroup());
        System.out.println("4. Last Donation Date: " + donor.getLastDonationDate());
        System.out.println("5. Phone Number: " + donor.getPhoneNumber());
        System.out.println("6. Email: " + donor.getEmail());
        System.out.println("7. Address: " + donor.getAddress());
        System.out.println("8. Weight: " + donor.getWeight());
        System.out.println("9. Medical Conditions: " + donor.getMedicalConditions());
        System.out.println("10. Emergency Availability: " + (donor.isAvailableForEmergency() ? "Yes" : "No"));
        System.out.println("11. Preferred Donation Center: " + donor.getPreferredDonationCenter());
        System.out.println("12. Emergency Contact: " + donor.getEmergencyContact());
        System.out.println("13. Emergency Contact Phone: " + donor.getEmergencyContactPhone());

        System.out.print("\nEnter the number of the field to update (1-13): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String newValue;
        switch (choice) {
            case 1 -> {
                System.out.print("New name: ");
                newValue = scanner.nextLine();
                donor = new Donor(newValue, donor.getAge(), donor.getBloodGroup(), 
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 2 -> {
                System.out.print("New age: ");
                int newAge = scanner.nextInt();
                scanner.nextLine();
                donor = new Donor(donor.getName(), newAge, donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 3 -> {
                System.out.print("New blood group: ");
                newValue = scanner.nextLine().toUpperCase();
                donor = new Donor(donor.getName(), donor.getAge(), newValue,
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 4 -> {
                System.out.print("New last donation date (YYYY-MM-DD): ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    newValue, donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 5 -> {
                System.out.print("New phone number: ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), newValue, donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 6 -> {
                System.out.print("New email: ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), newValue,
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 7 -> {
                System.out.print("New address: ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    newValue, donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 8 -> {
                System.out.print("New weight (kg): ");
                double newWeight = scanner.nextDouble();
                scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), newWeight, donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 9 -> {
                System.out.print("New medical conditions: ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    newValue, donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 10 -> {
                System.out.print("Available for emergency? (yes/no): ");
                boolean newEmergency = scanner.nextLine().toLowerCase().startsWith("y");
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), newEmergency,
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 11 -> {
                System.out.print("New preferred donation center: ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    newValue, donor.getEmergencyContact(),
                    donor.getEmergencyContactPhone());
            }
            case 12 -> {
                System.out.print("New emergency contact name: ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), newValue,
                    donor.getEmergencyContactPhone());
            }
            case 13 -> {
                System.out.print("New emergency contact phone: ");
                newValue = scanner.nextLine();
                donor = new Donor(donor.getName(), donor.getAge(), donor.getBloodGroup(),
                    donor.getLastDonationDate(), donor.getPhoneNumber(), donor.getEmail(),
                    donor.getAddress(), donor.getWeight(), donor.hasChronicDisease(),
                    donor.getMedicalConditions(), donor.isAvailableForEmergency(),
                    donor.getPreferredDonationCenter(), donor.getEmergencyContact(),
                    newValue);
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        donorManager.updateDonor(donorManager.findDonor(name), donor);
        System.out.println("Donor information updated successfully!");
    }

    private static void viewStatistics() {
        List<Donor> donors = donorManager.getAllDonors();
        
        System.out.println("\n=== Donation Statistics ===");
        System.out.println("Total Registered Donors: " + donors.size());
        
        // Blood group distribution
        System.out.println("\nBlood Group Distribution:");
        donors.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Donor::getBloodGroup,
                java.util.stream.Collectors.counting()))
            .forEach((group, count) -> 
                System.out.println(group + ": " + count + " donors"));

        // Emergency donors
        long emergencyDonors = donors.stream()
            .filter(Donor::isAvailableForEmergency)
            .count();
        System.out.println("\nEmergency Available Donors: " + emergencyDonors);
    }
}
