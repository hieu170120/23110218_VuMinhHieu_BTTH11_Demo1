package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.entity.UserInfo;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
public class UserInfoService implements UserDetailsService {
	@Autowired
	UserInfoRepository userInfoRepository;
	
	public UserInfoService(UserInfoRepository userInfoRepository) {
		this.userInfoRepository = userInfoRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userInfo = userInfoRepository.findByEmail(username);
		if (userInfo.isPresent()) {
			return new org.springframework.security.core.userdetails.User(
				userInfo.get().getEmail(),
				userInfo.get().getPassword(),
				AuthorityUtils.commaSeparatedStringToAuthorityList(userInfo.get().getRoles())
			);
		}
		return null;
	}
	
}
