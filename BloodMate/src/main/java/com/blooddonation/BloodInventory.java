package com.blooddonation;

import java.time.LocalDate;
import java.util.*;

public class BloodInventory {
    private static class BloodUnit {
        private final String bloodGroup;
        private final LocalDate collectionDate;
        private final LocalDate expirationDate;
        private final String donorId;
        private boolean isReserved;

        public BloodUnit(String bloodGroup, String donorId) {
            this.bloodGroup = bloodGroup;
            this.donorId = donorId;
            this.collectionDate = LocalDate.now();
            this.expirationDate = collectionDate.plusDays(42); // Blood expires after 42 days
            this.isReserved = false;
        }
    }

    private final Map<String, List<BloodUnit>> inventory;
    private final Map<String, Integer> minimumStockLevels;
    private final List<String> lowStockAlerts;

    public BloodInventory() {
        inventory = new HashMap<>();
        minimumStockLevels = new HashMap<>();
        lowStockAlerts = new ArrayList<>();
        
        // Initialize inventory for all blood types
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String type : bloodTypes) {
            inventory.put(type, new ArrayList<>());
            minimumStockLevels.put(type, 10); // Set minimum stock level to 10 units
        }
    }

    public void addBloodUnit(String bloodGroup, String donorId) {
        BloodUnit unit = new BloodUnit(bloodGroup, donorId);
        inventory.get(bloodGroup).add(unit);
        checkStockLevels(bloodGroup);
    }

    public boolean reserveBloodUnit(String bloodGroup) {
        List<BloodUnit> units = inventory.get(bloodGroup);
        for (BloodUnit unit : units) {
            if (!unit.isReserved && !isExpired(unit)) {
                unit.isReserved = true;
                return true;
            }
        }
        return false;
    }

    public void removeExpiredBlood() {
        for (List<BloodUnit> units : inventory.values()) {
            units.removeIf(this::isExpired);
        }
    }

    public Map<String, Integer> getCurrentStockLevels() {
        Map<String, Integer> levels = new HashMap<>();
        for (Map.Entry<String, List<BloodUnit>> entry : inventory.entrySet()) {
            long validUnits = entry.getValue().stream()
                .filter(unit -> !isExpired(unit) && !unit.isReserved)
                .count();
            levels.put(entry.getKey(), (int) validUnits);
        }
        return levels;
    }

    public List<String> getLowStockAlerts() {
        lowStockAlerts.clear();
        for (Map.Entry<String, List<BloodUnit>> entry : inventory.entrySet()) {
            long validUnits = entry.getValue().stream()
                .filter(unit -> !isExpired(unit) && !unit.isReserved)
                .count();
            if (validUnits < minimumStockLevels.get(entry.getKey())) {
                lowStockAlerts.add(entry.getKey() + " blood is running low! Current stock: " + validUnits);
            }
        }
        return lowStockAlerts;
    }

    private boolean isExpired(BloodUnit unit) {
        return LocalDate.now().isAfter(unit.expirationDate);
    }

    private void checkStockLevels(String bloodGroup) {
        long validUnits = inventory.get(bloodGroup).stream()
            .filter(unit -> !isExpired(unit) && !unit.isReserved)
            .count();
        if (validUnits < minimumStockLevels.get(bloodGroup)) {
            lowStockAlerts.add(bloodGroup + " blood is running low! Current stock: " + validUnits);
        }
    }

    public void setMinimumStockLevel(String bloodGroup, int level) {
        minimumStockLevels.put(bloodGroup, level);
        checkStockLevels(bloodGroup);
    }

    public List<BloodUnit> getExpiringSoon(int daysThreshold) {
        List<BloodUnit> expiringSoon = new ArrayList<>();
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        
        for (List<BloodUnit> units : inventory.values()) {
            for (BloodUnit unit : units) {
                if (!unit.isReserved && !isExpired(unit) && 
                    unit.expirationDate.isBefore(thresholdDate)) {
                    expiringSoon.add(unit);
                }
            }
        }
        return expiringSoon;
    }
} 