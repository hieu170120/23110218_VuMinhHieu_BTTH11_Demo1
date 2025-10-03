package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.service.CustomUserDetailsService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl(); // Custom implementation for user authentication
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding configuration
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
        configurers.add(new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(AuthenticationManagerBuilder auth) throws Exception {
                // Uncomment and configure if needed
                // auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
            }
        });
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                   .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/").hasAnyAuthority("USER", "ADMIN", "EDITOR", "CREATOR") // Access to root
                       .requestMatchers("/new").hasAnyAuthority("ADMIN", "CREATOR") // Access to /new for specific roles
                       .requestMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR") // Access to /edit for admin or editor
                       .requestMatchers("/delete/**").hasAuthority("ADMIN") // Only admin can delete
                       .requestMatchers(HttpMethod.GET, "/api/**").permitAll() // API endpoints open for all GET requests
                       .requestMatchers("/api/**").permitAll() // Open all /api/** requests
                       .anyRequest().authenticated() // Other requests need authentication
                   )
                   .httpBasic(withDefaults())
                   .formLogin(login -> login.loginPage("/login").permitAll())
                   .logout(logout -> logout.permitAll())
                   .exceptionHandling(handling -> handling.accessDeniedPage("/403"))
                   .build();
    }
}
