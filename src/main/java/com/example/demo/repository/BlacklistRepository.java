package com.example.demo.repository;

import com.example.demo.models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    void deleteByPhoneNumber(String phoneNumber);

    // Add custom queries if needed
}

