package com.blooddonation.webapi;

import com.blooddonation.RewardsManager;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {
    private final RewardsManager rewardsManager = new RewardsManager();

    @GetMapping("/top-donors")
    public List<Map<String, Object>> getTopDonors(@RequestParam(defaultValue = "10") int count) {
        return rewardsManager.getTopDonors(count);
    }

    @GetMapping
    public List<Map<String, Object>> getAllRewards() {
        return rewardsManager.getAllRewards();
    }
}
