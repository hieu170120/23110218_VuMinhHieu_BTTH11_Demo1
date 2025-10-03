package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.RegisterUserModel;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Đăng ký người dùng mới
     */
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserModel registerUserModel) {
        // Kiểm tra nếu tên người dùng hoặc email đã tồn tại
        if (userService.findByUsername(registerUserModel.getUsername()).isPresent() ||
                userService.findByEmail(registerUserModel.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Username or email is already taken!");
        }

        // Tạo người dùng mới
        User user = new User();
        user.setUsername(registerUserModel.getUsername());
        user.setEmail(registerUserModel.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
        userService.registerUser(user);  // Lưu người dùng mới vào cơ sở dữ liệu

        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Đăng nhập người dùng và tạo JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterUserModel loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .collect(Collectors.toSet());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(userDetails.getUsername());
            response.setRoles(roles);
            
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException("Invalid credentials");
        }
    }

    /**
     * Lấy thông tin người dùng hiện tại
     */
    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(userDetails);
    }

    /**
     * Lấy tất cả người dùng
     */
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}
