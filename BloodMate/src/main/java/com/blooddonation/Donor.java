package com.blooddonation;

import java.util.UUID;

public class Donor {
    private final String id;
    private String name;
    private int age;
    private String bloodGroup;
    private String lastDonationDate; // format: YYYY-MM-DD
    private String phoneNumber;
    private String email;
    private String address;
    private double weight;
    private boolean hasChronicDisease;
    private String medicalConditions;
    private boolean isAvailableForEmergency;
    private String preferredDonationCenter;
    private String emergencyContact;
    private String emergencyContactPhone;

    public Donor(String name, int age, String bloodGroup, String lastDonationDate, 
                String phoneNumber, String email, String address, double weight,
                boolean hasChronicDisease, String medicalConditions, 
                boolean isAvailableForEmergency, String preferredDonationCenter,
                String emergencyContact, String emergencyContactPhone) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.lastDonationDate = lastDonationDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.weight = weight;
        this.hasChronicDisease = hasChronicDisease;
        this.medicalConditions = medicalConditions;
        this.isAvailableForEmergency = isAvailableForEmergency;
        this.preferredDonationCenter = preferredDonationCenter;
        this.emergencyContact = emergencyContact;
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getLastDonationDate() {
        return lastDonationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public double getWeight() {
        return weight;
    }

    public boolean hasChronicDisease() {
        return hasChronicDisease;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public boolean isAvailableForEmergency() {
        return isAvailableForEmergency;
    }

    public String getPreferredDonationCenter() {
        return preferredDonationCenter;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    @Override
    public String toString() {
        return String.join(",", 
            id, name, String.valueOf(age), bloodGroup, lastDonationDate,
            phoneNumber, email, address, String.valueOf(weight),
            String.valueOf(hasChronicDisease), medicalConditions,
            String.valueOf(isAvailableForEmergency), preferredDonationCenter,
            emergencyContact, emergencyContactPhone);
    }

    public static Donor fromString(String line) {
        String[] parts = line.split(",");
        Donor donor = new Donor(
            parts[1],                    // name
            Integer.parseInt(parts[2]),  // age
            parts[3],                    // bloodGroup
            parts[4],                    // lastDonationDate
            parts[5],                    // phoneNumber
            parts[6],                    // email
            parts[7],                    // address
            Double.parseDouble(parts[8]),// weight
            Boolean.parseBoolean(parts[9]), // hasChronicDisease
            parts[10],                   // medicalConditions
            Boolean.parseBoolean(parts[11]), // isAvailableForEmergency
            parts[12],                   // preferredDonationCenter
            parts[13],                   // emergencyContact
            parts[14]                    // emergencyContactPhone
        );
        return donor;
    }
}
