package com.blooddonation.webapi;

import com.blooddonation.StatisticsManager;
import com.blooddonation.DonorManager;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final StatisticsManager statsManager = new StatisticsManager(new DonorManager());

    @GetMapping
    public Map<String, Object> getGeneralStats() {
        return statsManager.getGeneralStatistics();
    }
}
