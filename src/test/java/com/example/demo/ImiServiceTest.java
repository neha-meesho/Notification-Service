package com.example.demo;

import com.example.demo.models.SmsRequest;
import com.example.demo.rest.services.ImiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    @InjectMocks
    private ImiService imiService;

    @Test
    void sendSmsShouldReturnResponse() {
        // Arrange
        SmsRequest savedSms = new SmsRequest();
        savedSms.setId(1L);
        savedSms.setPhoneNumber("1234567890");
        savedSms.setMessage("Test message");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Mocking behavior for RestTemplate
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("Response from ImiConnect"));

        // Act
        String response = imiService.sendSms(savedSms);

        // Assert
        assertNotNull(response);
        assertEquals("Response from ImiConnect", response);

        // Verify that the RestTemplate was called with the expected parameters
        ArgumentCaptor<HttpEntity<String>> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate, times(1)).postForEntity(eq("https://api.imiconnect.in/resources/v1/messaging"),
                httpEntityCaptor.capture(),
                eq(String.class));

        HttpEntity<String> capturedHttpEntity = httpEntityCaptor.getValue();
        assertNotNull(capturedHttpEntity);
        assertEquals(headers, capturedHttpEntity.getHeaders());
        // You may need to deserialize the request body and verify its contents as well
    }
}
