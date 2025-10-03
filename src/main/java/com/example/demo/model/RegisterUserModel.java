package com.example.demo.model;

import lombok.Data;

@Data
public class RegisterUserModel {
    private String username;
    private String password;
    private String name;
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
