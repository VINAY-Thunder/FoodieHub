package com.superBoy.FoodieHub.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superBoy.FoodieHub.I_Service.IUserService;
import com.superBoy.FoodieHub.Request.DTOs.LoginRequestDto;
import com.superBoy.FoodieHub.Request.DTOs.RegisterUserDto;
import com.superBoy.FoodieHub.Response.DTOs.LoginResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "User and Admin registration and login")
public class AuthController {

	private IUserService userservice;

	@Autowired
	public AuthController(IUserService userservice) {
		super();
		this.userservice = userservice;
	}

	@Operation(summary = "Register a new customer user account")
	@PostMapping("/user")
	public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserDto user) {
		return new ResponseEntity<String>(userservice.registerUser(user), HttpStatus.CREATED);
	}

	@Operation(summary = "Register a new admin user account")
	@PostMapping("/admin")
	public ResponseEntity<String> registerAdmin(@RequestBody @Valid RegisterUserDto admin) {
		return new ResponseEntity<String>(userservice.registerAdmin(admin), HttpStatus.CREATED);
	}

	@Operation(summary = "Login — validate credentials and return role + userId")
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDto loginRequest) {
		try {
			LoginResponseDTO response = userservice.login(loginRequest);
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginResponseDTO(null, null, null, e.getMessage()));
		}
	}
}
