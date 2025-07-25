package com.blooddonation.webapi;

import com.blooddonation.CampaignManager;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignManager campaignManager = new CampaignManager();

    @GetMapping
    public List<Map<String, Object>> getAllCampaigns() {
        return campaignManager.getAllCampaigns();
    }

    @PostMapping
    public void createCampaign(@RequestBody Map<String, Object> campaignData) {
        campaignManager.createCampaign(campaignData);
    }
}
