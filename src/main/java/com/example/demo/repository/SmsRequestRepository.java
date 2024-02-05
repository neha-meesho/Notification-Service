package com.example.demo.repository;

import com.example.demo.models.SmsRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRequestRepository extends JpaRepository<SmsRequest,Long> {
}
