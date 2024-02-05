package com.example.demo;

import com.example.demo.models.SmsRequest;
import com.example.demo.producer.MessageProducer;
import com.example.demo.repository.SmsRequestRepository;
import com.example.demo.rest.exceptionHandling.ValidationException;
import com.example.demo.rest.services.SmsRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsRequestServiceTest {
    @Mock
    private SmsRequestRepository smsRequestRepository;
    @Mock
    private MessageProducer messageProducer;


    @InjectMocks
    private SmsRequestService smsRequestService;


    List<SmsRequest> mockSmsRequests = Arrays.asList(
            new SmsRequest(1L,"9182469635","hi","SENT","","", LocalDateTime.now(),LocalDateTime.now()),
            new SmsRequest(2L,"9676258010","hi from meesho","REJECT","400","Blacklisted number", LocalDateTime.now(),LocalDateTime.now())
    );

    @Test
    void getAllSmsRequestsShouldReturnListOfSmsRequests() {
        when(smsRequestRepository.findAll()).thenReturn(mockSmsRequests);
        List<SmsRequest> result = smsRequestService.getAllSmsRequests();
        assertEquals(mockSmsRequests, result);
    }

    @Test
    void getSmsRequestByIdShouldReturnSmsRequestIfExists() {
        Long id = 1L;
        when(smsRequestRepository.findById(id)).thenReturn(Optional.of(mockSmsRequests.get(0)));
        SmsRequest result = smsRequestService.getSmsRequestById(id);
        assertEquals(mockSmsRequests.get(0), result);
    }

    @Test
    void getSmsRequestByIdShouldThrowValidationExceptionIfNotExists() {
        Long id = 1L;
        when(smsRequestRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> smsRequestService.getSmsRequestById(id));
    }

    @Test
    void addSmsRequestShouldReturnSavedSmsRequest() {
        SmsRequest smsRequestToSave = new SmsRequest(null,"9182469635", "hi",null,null,null,null,null);
        when(smsRequestRepository.save(smsRequestToSave)).thenReturn(mockSmsRequests.get(0));
        doNothing().when(messageProducer).sendMessage(anyString(), anyString());
        SmsRequest result = smsRequestService.addSmsRequest(smsRequestToSave);
        assertEquals(mockSmsRequests.get(0), result);
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        //check if msg producer sending the saved sms id
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageProducer, times(1)).sendMessage(eq("send_sms"), argumentCaptor.capture());
        assertEquals(String.valueOf(result.getId()), argumentCaptor.getValue());


    }


}
