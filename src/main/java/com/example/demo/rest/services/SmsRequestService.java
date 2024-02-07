package com.example.demo.rest.services;


import com.example.demo.kafka.producer.MessageProducer;
import com.example.demo.repository.SmsRequestRepository;
import com.example.demo.models.SmsRequest;
import com.example.demo.rest.exceptionHandling.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsRequestService {

    @Autowired
    private SmsRequestRepository smsRequestRepository;

    @Autowired
    private  MessageProducer messageProducer;

    public List<SmsRequest> getAllSmsRequests() {
        return smsRequestRepository.findAll();
    }

    public SmsRequest getSmsRequestById(Long id) {
        return smsRequestRepository.findById(id).orElseThrow(() -> new ValidationException("Sms with requested id not found"));
    }

    public SmsRequest addSmsRequest(SmsRequest smsRequest) {

        System.out.println(smsRequest.toString());
        smsRequest.setCreatedAt(LocalDateTime.now());
        smsRequest.setUpdatedAt(LocalDateTime.now());
        SmsRequest savedSms = smsRequestRepository.save(smsRequest);
        messageProducer.sendMessage("send_sms",String.valueOf(savedSms.getId()));
        return smsRequestRepository.save(smsRequest);
    }

    public void deleteSmsRequest(Long id) {
        smsRequestRepository.deleteById(id);
    }
}

