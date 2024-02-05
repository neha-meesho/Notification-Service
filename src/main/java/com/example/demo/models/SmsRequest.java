package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SmsRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String message;
    private String status;
    private String failureCode;
    private String failureComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}