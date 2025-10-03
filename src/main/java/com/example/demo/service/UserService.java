package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;


@Service
public class UserService {
	private final UserRepository userRepository;
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	public List<User> allUsers() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}
	public Object findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	public Object findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
	public void registerUser(User user) {
		// TODO Auto-generated method stub
		
	}
	public Object findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	public void save(User user) {
		// TODO Auto-generated method stub
		
	}
}