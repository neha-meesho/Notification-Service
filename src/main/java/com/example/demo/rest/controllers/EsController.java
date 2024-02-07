package com.example.demo.rest.controllers;

import com.example.demo.models.DateField;
import com.example.demo.models.EsSmsRequest;


import com.example.demo.repository.EsRepository;
import com.example.demo.rest.services.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/elastic")
public class EsController {

    @Autowired
    EsService esService;

    @PostMapping
    public ResponseEntity<String> sendSms(@RequestBody @Valid EsSmsRequest esModel){
        long date = new Date().getTime();
        esModel.setCreatedAt(date);

        return ResponseEntity.ok("sms saved in es");
    }

    @GetMapping
    public ResponseEntity<Page<EsSmsRequest>> getAllSms(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<EsSmsRequest> result = esService.getAllSms(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }


    @GetMapping("/byPhoneNumberAndTimeRange")
    public ResponseEntity<Page<EsSmsRequest>> getSmsByPhoneNumberAndTimeRange(
            @RequestParam String phoneNumber,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
           long start = Timestamp.valueOf(startDate).getTime();
           long end =  Timestamp.valueOf(endDate).getTime();

        Page<EsSmsRequest> result = esService.getSmsByPhoneNumberAndTimeRange(phoneNumber, start, end, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byText")
    public ResponseEntity<Page<EsSmsRequest>> getSmsContainingText(
            @RequestParam String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<EsSmsRequest> result = esService.getSmsContainingMessage(searchText, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }



}