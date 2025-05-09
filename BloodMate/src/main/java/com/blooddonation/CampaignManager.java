package com.blooddonation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CampaignManager {
    private static class Campaign {
        private final String name;
        private final String location;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final String organizer;
        private final String contactInfo;
        private final List<String> targetBloodGroups;
        private final int targetDonors;
        private int registeredDonors;
        private final List<String> registeredDonorIds;

        public Campaign(String name, String location, LocalDateTime startTime, 
                       LocalDateTime endTime, String organizer, String contactInfo,
                       List<String> targetBloodGroups, int targetDonors) {
            this.name = name;
            this.location = location;
            this.startTime = startTime;
            this.endTime = endTime;
            this.organizer = organizer;
            this.contactInfo = contactInfo;
            this.targetBloodGroups = targetBloodGroups;
            this.targetDonors = targetDonors;
            this.registeredDonors = 0;
            this.registeredDonorIds = new ArrayList<>();
        }
    }

    private final List<Campaign> activeCampaigns;
    private final List<Campaign> pastCampaigns;
    private final Map<String, List<String>> donorCampaignHistory;

    public CampaignManager() {
        activeCampaigns = new ArrayList<>();
        pastCampaigns = new ArrayList<>();
        donorCampaignHistory = new HashMap<>();
    }

    public void createCampaign(String name, String location, LocalDateTime startTime,
                             LocalDateTime endTime, String organizer, String contactInfo,
                             List<String> targetBloodGroups, int targetDonors) {
        Campaign campaign = new Campaign(name, location, startTime, endTime, organizer,
                                       contactInfo, targetBloodGroups, targetDonors);
        activeCampaigns.add(campaign);
    }

    public boolean registerDonorForCampaign(String campaignName, String donorId) {
        Campaign campaign = findCampaign(campaignName);
        if (campaign != null && !campaign.registeredDonorIds.contains(donorId)) {
            campaign.registeredDonorIds.add(donorId);
            campaign.registeredDonors++;
            
            // Update donor's campaign history
            donorCampaignHistory.computeIfAbsent(donorId, k -> new ArrayList<>())
                .add(campaignName);
            
            return true;
        }
        return false;
    }

    public void updateCampaignStatus() {
        LocalDateTime now = LocalDateTime.now();
        Iterator<Campaign> iterator = activeCampaigns.iterator();
        
        while (iterator.hasNext()) {
            Campaign campaign = iterator.next();
            if (now.isAfter(campaign.endTime)) {
                pastCampaigns.add(campaign);
                iterator.remove();
            }
        }
    }

    public List<Campaign> getActiveCampaigns() {
        return new ArrayList<>(activeCampaigns);
    }

    public List<Campaign> getPastCampaigns() {
        return new ArrayList<>(pastCampaigns);
    }

    public List<String> getDonorCampaignHistory(String donorId) {
        return donorCampaignHistory.getOrDefault(donorId, new ArrayList<>());
    }

    public List<Campaign> getUpcomingCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        return activeCampaigns.stream()
            .filter(campaign -> campaign.startTime.isAfter(now))
            .toList();
    }

    public List<Campaign> getCampaignsByLocation(String location) {
        return activeCampaigns.stream()
            .filter(campaign -> campaign.location.equalsIgnoreCase(location))
            .toList();
    }

    public List<Campaign> getCampaignsByBloodGroup(String bloodGroup) {
        return activeCampaigns.stream()
            .filter(campaign -> campaign.targetBloodGroups.contains(bloodGroup))
            .toList();
    }

    private Campaign findCampaign(String name) {
        return activeCampaigns.stream()
            .filter(campaign -> campaign.name.equals(name))
            .findFirst()
            .orElse(null);
    }

    public Map<String, Object> getCampaignStatistics(String campaignName) {
        Campaign campaign = findCampaign(campaignName);
        if (campaign == null) {
            return null;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("name", campaign.name);
        stats.put("location", campaign.location);
        stats.put("targetDonors", campaign.targetDonors);
        stats.put("registeredDonors", campaign.registeredDonors);
        stats.put("completionPercentage", 
            (double) campaign.registeredDonors / campaign.targetDonors * 100);
        stats.put("startTime", campaign.startTime);
        stats.put("endTime", campaign.endTime);
        stats.put("targetBloodGroups", campaign.targetBloodGroups);
        
        return stats;
    }
} 