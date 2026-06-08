package com.superBoy.FoodieHub.I_Service;

import com.superBoy.FoodieHub.Request.DTOs.LoginRequestDto;
import com.superBoy.FoodieHub.Request.DTOs.RegisterUserDto;
import com.superBoy.FoodieHub.Response.DTOs.LoginResponseDTO;

public interface IUserService {
	public String registerUser(RegisterUserDto user);
	
	public String registerAdmin(RegisterUserDto admin);

	public LoginResponseDTO login(LoginRequestDto loginRequest);
}
