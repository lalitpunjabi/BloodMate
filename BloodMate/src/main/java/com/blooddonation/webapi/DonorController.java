package com.blooddonation.webapi;

import com.blooddonation.Donor;
import com.blooddonation.DonorManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {
    private final DonorManager donorManager = new DonorManager();

    @GetMapping
    public List<Donor> getAllDonors() {
        return donorManager.getAllDonors();
    }

    @PostMapping
    public ResponseEntity<String> registerDonor(@RequestBody Donor donor) {
        boolean success = donorManager.registerDonor(donor);
        if (success) {
            return ResponseEntity.ok("Donor registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }

    // Add more endpoints as needed (update, search, etc.)
}
