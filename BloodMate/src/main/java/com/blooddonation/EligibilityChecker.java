package com.blooddonation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EligibilityChecker {
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 65;
    private static final double MIN_WEIGHT = 50.0; // kg
    private static final int MIN_MONTHS_BETWEEN_DONATIONS = 3;

    public static class EligibilityResult {
        private final boolean eligible;
        private final List<String> reasons;

        public EligibilityResult(boolean eligible, List<String> reasons) {
            this.eligible = eligible;
            this.reasons = reasons;
        }

        public boolean isEligible() { return eligible; }
        public List<String> getReasons() { return reasons; }
    }

    public static EligibilityResult checkEligibility(Donor donor) {
        List<String> reasons = new ArrayList<>();
        boolean eligible = true;

        // Age check
        if (donor.getAge() < MIN_AGE) {
            eligible = false;
            reasons.add("Donor must be at least " + MIN_AGE + " years old");
        } else if (donor.getAge() > MAX_AGE) {
            eligible = false;
            reasons.add("Donor must be under " + MAX_AGE + " years old");
        }

        // Weight check
        if (donor.getWeight() < MIN_WEIGHT) {
            eligible = false;
            reasons.add("Donor must weigh at least " + MIN_WEIGHT + " kg");
        }

        // Time since last donation
        LocalDate lastDate = LocalDate.parse(donor.getLastDonationDate());
        long monthsSince = ChronoUnit.MONTHS.between(lastDate, LocalDate.now());
        if (monthsSince < MIN_MONTHS_BETWEEN_DONATIONS) {
            eligible = false;
            reasons.add("Must wait " + MIN_MONTHS_BETWEEN_DONATIONS + " months between donations");
        }

        // Medical conditions
        if (donor.hasChronicDisease()) {
            eligible = false;
            reasons.add("Chronic disease: " + donor.getMedicalConditions());
        }

        // Additional checks for specific medical conditions
        String conditions = donor.getMedicalConditions().toLowerCase();
        if (conditions.contains("hiv") || conditions.contains("aids")) {
            eligible = false;
            reasons.add("HIV/AIDS positive");
        }
        if (conditions.contains("hepatitis")) {
            eligible = false;
            reasons.add("Hepatitis positive");
        }
        if (conditions.contains("malaria")) {
            eligible = false;
            reasons.add("Recent malaria infection");
        }

        return new EligibilityResult(eligible, reasons);
    }

    public static boolean isEligible(Donor donor) {
        return checkEligibility(donor).isEligible();
    }
}
