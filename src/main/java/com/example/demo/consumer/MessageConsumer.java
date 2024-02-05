package com.example.demo.consumer;

import com.example.demo.models.EsSmsRequest;
import com.example.demo.repository.EsRepository;
import com.example.demo.repository.SmsRequestRepository;
import com.example.demo.models.SmsRequest;
import com.example.demo.rest.exceptionHandling.ErrorResponse;
import com.example.demo.rest.exceptionHandling.ValidationException;
import com.example.demo.rest.services.BlacklistService;
import com.example.demo.rest.services.ImiService;
import com.example.demo.rest.services.SmsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Component
public class MessageConsumer {

    @Autowired
    private SmsRequestService smsRequestService;

    private final BlacklistService blacklistService;

    @Autowired
    private final ImiService imiService;
    @Autowired
    private SmsRequestRepository smsRequestRepository;

    @Autowired
    private EsRepository esRepository;

    @Autowired
    public MessageConsumer(BlacklistService blacklistService, ImiService imiService) {
        this.blacklistService = blacklistService;
        this.imiService = imiService;
    }

    @KafkaListener(topics = "send_sms", groupId = "neha")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        SmsRequest currentSms = smsRequestService.getSmsRequestById(Long.parseLong(message));
        String phoneNo = currentSms.getPhoneNumber();
        if(blacklistService.isBlacklisted(phoneNo)){
            System.out.println("the number is blacklisted");
            currentSms.setStatus("BAD_REQUEST");
            currentSms.setFailureCode("400");
            currentSms.setFailureComments("the number is blacklisted");
            smsRequestRepository.save(currentSms);
            throw new ValidationException("the number is blacklisted");

        }
        else{
            System.out.println("the number is not blacklisted");
            currentSms.setStatus("SENT");
            SmsRequest savedSms = smsRequestRepository.save(currentSms);
            EsSmsRequest newSms = new EsSmsRequest();
            newSms.setMessage(currentSms.getMessage());
            newSms.setPhoneNumber(currentSms.getPhoneNumber());
            newSms.setId(String.valueOf(savedSms.getId()));
            newSms.setCreatedAt(new Date().getTime());
            try{
                esRepository.save(newSms);
            }
            catch(Exception ignored){}

            System.out.println("sending sms!!!");
//            System.out.println(imiService.sendSms(savedSms));

        }



    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ValidationException exc){
        ErrorResponse error = new ErrorResponse();
        error.setMessage(exc.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

}
