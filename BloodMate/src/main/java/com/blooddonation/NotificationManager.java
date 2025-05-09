package com.blooddonation;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationManager {
    private final DonorManager donorManager;
    private final Map<String, List<Notification>> notifications;
    private final List<EmergencyRequest> activeEmergencyRequests;

    public NotificationManager(DonorManager donorManager) {
        this.donorManager = donorManager;
        this.notifications = new ConcurrentHashMap<>();
        this.activeEmergencyRequests = new ArrayList<>();
    }

    public void createEmergencyRequest(String bloodGroup, String location, String hospital, String contactPerson, String contactPhone) {
        EmergencyRequest request = new EmergencyRequest(
            bloodGroup, location, hospital, contactPerson, contactPhone, LocalDateTime.now()
        );
        activeEmergencyRequests.add(request);

        // Notify matching donors
        List<Donor> matchingDonors = donorManager.getEmergencyDonors(bloodGroup);
        for (Donor donor : matchingDonors) {
            addNotification(donor.getPhoneNumber(), new Notification(
                "EMERGENCY BLOOD REQUEST",
                String.format("Urgent need for %s blood at %s. Please contact %s at %s if available.",
                    bloodGroup, hospital, contactPerson, contactPhone),
                NotificationType.EMERGENCY
            ));
        }
    }

    public void addNotification(String recipientId, Notification notification) {
        notifications.computeIfAbsent(recipientId, key -> new ArrayList<>())
            .add(notification);
    }

    public List<Notification> getNotifications(String recipientId) {
        return notifications.getOrDefault(recipientId, new ArrayList<>());
    }

    public void markNotificationAsRead(String recipientId, Notification notification) {
        List<Notification> userNotifications = notifications.get(recipientId);
        if (userNotifications != null) {
            userNotifications.remove(notification);
        }
    }

    public List<EmergencyRequest> getActiveEmergencyRequests() {
        return new ArrayList<>(activeEmergencyRequests);
    }

    public void resolveEmergencyRequest(EmergencyRequest request) {
        activeEmergencyRequests.remove(request);
    }

    public static class Notification {
        private final String title;
        private final String message;
        private final NotificationType type;
        private final LocalDateTime timestamp;

        public Notification(String title, String message, NotificationType type) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.timestamp = LocalDateTime.now();
        }

        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public NotificationType getType() { return type; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    public static class EmergencyRequest {
        private final String bloodGroup;
        private final String location;
        private final String hospital;
        private final String contactPerson;
        private final String contactPhone;
        private final LocalDateTime timestamp;
        private boolean resolved;

        public EmergencyRequest(String bloodGroup, String location, String hospital,
                              String contactPerson, String contactPhone, LocalDateTime timestamp) {
            this.bloodGroup = bloodGroup;
            this.location = location;
            this.hospital = hospital;
            this.contactPerson = contactPerson;
            this.contactPhone = contactPhone;
            this.timestamp = timestamp;
            this.resolved = false;
        }

        public String getBloodGroup() { return bloodGroup; }
        public String getLocation() { return location; }
        public String getHospital() { return hospital; }
        public String getContactPerson() { return contactPerson; }
        public String getContactPhone() { return contactPhone; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public boolean isResolved() { return resolved; }
        public void setResolved(boolean resolved) { this.resolved = resolved; }
    }

    public enum NotificationType {
        EMERGENCY,
        REMINDER,
        UPDATE,
        SYSTEM
    }
} 