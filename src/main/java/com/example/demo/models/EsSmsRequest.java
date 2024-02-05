package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "sms", type = "sms_request_type")

public class EsSmsRequest {
    @Id
    private String id;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String message;
    @NonNull
    private Long createdAt;
}

