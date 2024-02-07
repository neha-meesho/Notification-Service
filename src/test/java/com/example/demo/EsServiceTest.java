package com.example.demo;

import com.example.demo.models.EsSmsRequest;
import com.example.demo.repository.EsRepository;
import com.example.demo.rest.services.EsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EsServiceTest {

    @Mock
    private EsRepository esRepository;

    @InjectMocks
    private EsService esService;

    @Test
    void saveShouldCallRepositorySave() {
        // Arrange
        EsSmsRequest esModel = new EsSmsRequest();

        // Act
        esService.save(esModel);

        // Assert
        verify(esRepository, times(1)).save(esModel);
    }

    @Test
    void getSmsByPhoneNumberAndTimeRangeShouldCallRepositoryMethod() {
        // Arrange
        String phoneNumber = "1234567890";
        long start = 0L;
        long end = Instant.now().toEpochMilli();
        PageRequest pageable = PageRequest.of(0, 10);

        // Mocking behavior for repository method
        Page<EsSmsRequest> expectedPage = mock(Page.class);
        when(esRepository.findByPhoneNumberAndCreatedAtBetween(phoneNumber, start, end, pageable))
                .thenReturn(expectedPage);

        // Act
        Page<EsSmsRequest> resultPage = esService.getSmsByPhoneNumberAndTimeRange(phoneNumber, start, end, pageable);

        // Assert
        assertEquals(expectedPage, resultPage);
    }

    @Test
    void getSmsContainingMessageShouldCallRepositoryMethod() {
        // Arrange
        String searchText = "test";
        PageRequest pageable = PageRequest.of(0, 10);

        // Mocking behavior for repository method
        Page<EsSmsRequest> expectedPage = mock(Page.class);
        when(esRepository.findByMessageContaining(searchText, pageable))
                .thenReturn(expectedPage);

        // Act
        Page<EsSmsRequest> resultPage = esService.getSmsContainingMessage(searchText, pageable);

        // Assert
        assertEquals(expectedPage, resultPage);
    }

    @Test
    void getAllSmsShouldCallRepositoryMethod() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);

        // Mocking behavior for repository method
        Page<EsSmsRequest> expectedPage = mock(Page.class);
        when(esRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<EsSmsRequest> resultPage = esService.getAllSms(pageable);

        // Assert
        assertEquals(expectedPage, resultPage);
    }
}
