package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtRepo jwtRepo;

    public String generateToken(UserDetails userDetails) {
        String token = jwtUtil.generateToken(userDetails.getUsername());
        jwtRepo.saveToken(userDetails.getUsername(), token);
        return token;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        boolean valid = jwtUtil.validateToken(token, userDetails.getUsername());
        return valid && jwtRepo.existsByToken(token);
    }

    public void removeToken(String username) {
        jwtRepo.removeToken(username);
    }

    public boolean isTokenStored(String token) {
        return jwtRepo.existsByToken(token);
    }
}
