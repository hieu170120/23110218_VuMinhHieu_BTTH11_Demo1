package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Customer;

@RestController
@EnableMethodSecurity
public class CustomerController {
	final private List<Customer> customers = List.of(
			Customer.builder().id("1").name("John Doe").phoneNumber("123-456-7890").email("john@gmail.com").build(),
			Customer.builder().id("2").name("Jane Smith").phoneNumber("987-654-3210").email("jane@gmail.com").build());
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello, World!");
	}
	@GetMapping("/customer/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Chỉ cho phép truy cập nếu có vai trò ADMIN
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = this.customers;
		return ResponseEntity.ok(customers);
	}
	@GetMapping("/customer/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER')")
	public ResponseEntity<Customer> getCustomerList(@PathVariable("id") String id) {
		List<Customer> customers = this.customers.stream()
				.filter(c -> c.getId().equals(id)).toList();
		return ResponseEntity.ok(customers.get(0));
		
	}
}
