package com.example.agilizapp.api.auth.service.controller.redis;

import com.example.agilizapp.api.auth.service.service.redis.RedisSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class RedisSessionController {

    @Autowired
    private RedisSessionService redisSessionService;

    @GetMapping("/api/sessions")
    public Set<String> getActiveSessions() {
        return redisSessionService.getActiveSessions();
    }

    @GetMapping("/api/sessions/{email}")
    public Map<Object, Object> getSessionData(@PathVariable String email) {
        return (Map<Object, Object>) redisSessionService.getSessionData(email);
    }
}
