package com.example.demo;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JwtRepo {
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public void saveToken(String username, String token) {
        tokenStore.put(username, token);
    }

    public String getToken(String username) {
        return tokenStore.get(username);
    }

    public void removeToken(String username) {
        tokenStore.remove(username);
    }

    public boolean existsByToken(String token) {
        return tokenStore.containsValue(token);
    }
}
