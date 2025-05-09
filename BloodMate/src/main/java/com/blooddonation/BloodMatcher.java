package com.blooddonation;

import java.util.*;

public class BloodMatcher {
    private static final Map<String, List<String>> compatibilityMap = new HashMap<>();

    static {
        // O- can donate to everyone (universal donor)
        compatibilityMap.put("O-", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        
        // O+ can donate to A+, B+, AB+, and O+
        compatibilityMap.put("O+", Arrays.asList("A+", "B+", "AB+", "O+"));
        
        // A- can donate to A+, A-, AB+, and AB-
        compatibilityMap.put("A-", Arrays.asList("A+", "A-", "AB+", "AB-"));
        
        // A+ can donate to A+ and AB+
        compatibilityMap.put("A+", Arrays.asList("A+", "AB+"));
        
        // B- can donate to B+, B-, AB+, and AB-
        compatibilityMap.put("B-", Arrays.asList("B+", "B-", "AB+", "AB-"));
        
        // B+ can donate to B+ and AB+
        compatibilityMap.put("B+", Arrays.asList("B+", "AB+"));
        
        // AB- can donate to AB+ and AB-
        compatibilityMap.put("AB-", Arrays.asList("AB+", "AB-"));
        
        // AB+ can only donate to AB+ (universal recipient)
        compatibilityMap.put("AB+", Arrays.asList("AB+"));
    }

    public static List<Donor> findMatches(String recipientBloodGroup, List<Donor> donors) {
        List<String> compatible = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : compatibilityMap.entrySet()) {
            if (entry.getValue().contains(recipientBloodGroup)) {
                compatible.add(entry.getKey());
            }
        }

        List<Donor> matches = new ArrayList<>();
        for (Donor d : donors) {
            if (compatible.contains(d.getBloodGroup())) {
                matches.add(d);
            }
        }
        return matches;
    }
}
