package com.example.demo.rest.services;

import com.example.demo.models.SmsRequest;
import com.example.demo.models.imiconnect.Channels;
import com.example.demo.models.imiconnect.Destination;
import com.example.demo.models.imiconnect.Sms;
import com.example.demo.models.imiconnect.SmsModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ImiService {

    @Value("${api.key}")
    private String apiKey;

    private static final String API_ENDPOINT = "https://api.imiconnect.in/resources/v1/messaging";

    private final RestTemplate restTemplate;

    public ImiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendSms(SmsRequest savedSms) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);


        SmsModel smsModel= new SmsModel();
        smsModel.setDeliverychannel("sms");
        Channels smsChannel = new Channels();
        Sms sms = new Sms();
        sms.setText(savedSms.getMessage());
        smsChannel.setSms(sms);
        smsModel.setChannels(smsChannel);
        Destination smsDestination = new Destination();
        smsDestination.setMsisdn(List.of(savedSms.getPhoneNumber()));
        smsDestination.setCorrelationid(String.valueOf(savedSms.getId()));
        smsModel.setDestination(List.of(smsDestination));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(Collections.singletonList(smsModel));
            System.out.println(requestBody);
            HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(API_ENDPOINT, httpEntity, String.class);
            return response.getBody();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }


    }
}
