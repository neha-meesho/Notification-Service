package com.example.demo.rest.services;

import com.example.demo.repository.BlacklistRepository;
import com.example.demo.models.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service

public class BlacklistService {
    private final BlacklistRepository blacklistRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository, RedisTemplate<String, String> redisTemplate) {
        this.blacklistRepository = blacklistRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void addToBlacklist(Set<String> phoneNumbers) {
        // Add phone numbers to Redis set
        redisTemplate.opsForSet().add("blacklist", phoneNumbers.toArray(new String[0]));
        for (String phoneNumber : phoneNumbers) {
            Blacklist entity = new Blacklist();
            entity.setPhoneNumber(phoneNumber);
            blacklistRepository.save(entity);
        }
    }


    public Set<String> getBlacklist() {
        // Get all phone numbers from Redis set
        return redisTemplate.opsForSet().members("blacklist");
    }

    @Transactional
    public void removeFromBlacklist(Set<String> phoneNumbers) {
        // Remove from Redis
        redisTemplate.opsForSet().remove("blacklist", phoneNumbers.toArray(new String[0]));

        // Remove from MySQL
        for (String phoneNumber : phoneNumbers) {
            blacklistRepository.deleteByPhoneNumber(phoneNumber);
        }

        // Additional logic if needed
    }

    public boolean isBlacklisted(String phoneNumber) {
        return redisTemplate.opsForSet().isMember("blacklist", phoneNumber);
    }
}
