package com.example.demo.service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.model.LoginUserModel;
import com.example.demo.model.RegisterUserModel;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
@Service

public class AuthenticationService {
	 private final UserRepository userRepository;
	    
	    private final PasswordEncoder passwordEncoder;
	    
	    private final AuthenticationManager authenticationManager;

	    public AuthenticationService(
	        UserRepository userRepository,
	        AuthenticationManager authenticationManager,
	        PasswordEncoder passwordEncoder
	    ) {
	        this.authenticationManager = authenticationManager;
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public User signup(RegisterUserModel input) {
	        User user = new User();
	        user.setName(input.getName());
	        user.setEmail(input.getEmail());
	        user.setPassword(passwordEncoder.encode(input.getPassword()));
	        user.setImages("/images/default-avatar.png");

	        return userRepository.save(user);
	    }

	    public User authenticate(LoginUserModel input) {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        input.getEmail(),
	                        input.getPassword()
	                )
	        );

	        return userRepository.findByEmail(input.getEmail())
	                .orElseThrow();
	    }
}
