package com.example.demo.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

@Service
public record UserService (UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
	public UserInfo saveUser(UserInfo userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		return userInfoRepository.save(userInfo);
	}
	public String addUser(UserInfo userInfo) {
		if (userInfoRepository.findByEmail(userInfo.getEmail()).isPresent()) {
			return "User already exists";
		}
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		userInfoRepository.save(userInfo);
		return "User added successfully";
	}
	
}
