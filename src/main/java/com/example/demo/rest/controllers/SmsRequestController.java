package com.example.demo.rest.controllers;

import com.example.demo.models.SmsRequest;
import com.example.demo.kafka.producer.MessageProducer;
import com.example.demo.rest.exceptionHandling.ErrorResponse;
import com.example.demo.rest.services.SmsRequestService;
import com.example.demo.rest.exceptionHandling.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/sms")
public class SmsRequestController {


    @Autowired
    private SmsRequestService smsRequestService;


    @GetMapping
    public List<SmsRequest> getAllSmsRequests() {
        return smsRequestService.getAllSmsRequests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getSmsRequestById(@PathVariable Long id) {
        SmsRequest smsRequest = smsRequestService.getSmsRequestById(id);
        return ResponseEntity.ok(smsRequest);
    }


    @Autowired
    private MessageProducer messageProducer;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> addSmsRequest(@RequestBody  @Valid SmsRequest smsRequest){

        if(smsRequest.getMessage().trim().isEmpty()){
            throw new ValidationException("SMS message is mandatory");
        }
        if (smsRequest.getPhoneNumber().trim().isEmpty()) {
            throw new ValidationException("phone_number is mandatory");
        }
        SmsRequest newSms = smsRequestService.addSmsRequest(smsRequest);
//
        return ResponseEntity.ok(Map.of("data", Map.of("requestId", newSms.getId(), "comments", "Successfully Sent")));


    }

    @DeleteMapping("/{id}")
    public void deleteSmsRequest(@PathVariable Long id) {
        smsRequestService.deleteSmsRequest(id);
    }



    // Exception handler for validation errors
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ValidationException exc){
        ErrorResponse error = new ErrorResponse();
        error.setMessage(exc.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }


}

