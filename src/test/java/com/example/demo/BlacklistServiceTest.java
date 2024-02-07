package com.example.demo;

import com.example.demo.models.Blacklist;
import com.example.demo.repository.BlacklistRepository;
import com.example.demo.rest.services.BlacklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceTest {

    @Mock
    private BlacklistRepository blacklistRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private SetOperations setOperations;

    @InjectMocks
    private BlacklistService blacklistService;

    @Test
    void addToBlacklistShouldAddToRedisAndDatabase() {
        // Arrange
        Set<String> phoneNumbers = new HashSet<>(Arrays.asList("1234567890", "9876543210"));
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(setOperations.add(anyString(), any(String[].class))).thenReturn(2L);

        blacklistService.addToBlacklist(phoneNumbers);

        verify(redisTemplate.opsForSet(), times(1)).add(anyString(), any(String[].class));
        verify(blacklistRepository, times(2)).save(any(Blacklist.class));
    }

    @Test
    void getBlacklistShouldReturnSetFromRedis() {
        Set<String> expectedBlacklist = new HashSet<>(Arrays.asList("1234567890", "9876543210"));
         when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(setOperations.members(anyString())).thenReturn(expectedBlacklist);

        Set<String> actualBlacklist = blacklistService.getBlacklist();

        assertEquals(expectedBlacklist, actualBlacklist);
    }

    @Test
    void removeFromBlacklistShouldRemoveFromRedisAndDatabase() {
        Set<String> phoneNumbers = new HashSet<>(Arrays.asList("1234567890", "9876543210"));

        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(setOperations.remove(anyString(),any(String[].class))).thenReturn(2L);

        blacklistService.removeFromBlacklist(phoneNumbers);

        verify(redisTemplate.opsForSet(), times(1)).remove(anyString(), any(String[].class));
        verify(blacklistRepository, times(2)).deleteByPhoneNumber(anyString());
    }

    @Test
    void isBlacklistedShouldCheckIfNumberIsInRedis() {
        // Arrange
        String blacklistedNumber = "1234567890";
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(redisTemplate.opsForSet().isMember(anyString(), anyString()  )).thenReturn(true);

        // Act
        boolean result = blacklistService.isBlacklisted(blacklistedNumber);

        // Assert
        assertTrue(result);
    }
}
