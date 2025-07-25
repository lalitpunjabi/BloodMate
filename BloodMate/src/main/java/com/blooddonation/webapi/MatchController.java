package com.blooddonation.webapi;

import com.blooddonation.BloodMatcher;
import com.blooddonation.Donor;
import com.blooddonation.DonorManager;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {
    private final DonorManager donorManager = new DonorManager();

    @GetMapping
    public List<Donor> findMatches(@RequestParam String bloodGroup) {
        return BloodMatcher.findMatches(bloodGroup, donorManager.getAllDonors());
    }
}
