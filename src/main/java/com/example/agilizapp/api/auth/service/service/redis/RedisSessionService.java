package com.example.agilizapp.api.auth.service.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisSessionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Set<String> getActiveSessions() {
        return redisTemplate.keys("session:*");
    }

    public Object getSessionData(String email) {
        String sessionKey = "session:" + email;
        return redisTemplate.opsForHash().entries(sessionKey);
    }
}
