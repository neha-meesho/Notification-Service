package com.example.demo.rest.controllers;

import com.example.demo.models.BlacklistRequest;
import com.example.demo.rest.services.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/v1/blacklist")
public class BlacklistController {
    private final BlacklistService blacklistService;

    @Autowired
    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @GetMapping
    public ResponseEntity<Set<String>> getBlacklists() {
        Set<String> blacklistedNumbers = blacklistService.getBlacklist();
        return ResponseEntity.ok(blacklistedNumbers);
    }
    @PostMapping
    public ResponseEntity<String> addToBlacklist(@RequestBody @Valid BlacklistRequest request) {
        Set<String> phoneNumbers = request.getPhoneNumbers();
        blacklistService.addToBlacklist(phoneNumbers);
        return ResponseEntity.ok("Successfully blacklisted");
    }

    @DeleteMapping
    public ResponseEntity<String> removeFromBlacklist(@RequestBody BlacklistRequest request) {
        Set<String> phoneNumbers = request.getPhoneNumbers();

        // Remove from Redis and MySQL
        blacklistService.removeFromBlacklist(phoneNumbers);

        return ResponseEntity.ok("Successfully whitelisted");
    }

}
