package com.superBoy.FoodieHub.Impl_Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.Enums.AdminRole;
import com.superBoy.FoodieHub.I_Service.IUserService;
import com.superBoy.FoodieHub.Model.Users;
import com.superBoy.FoodieHub.Repository.UserRepository;
import com.superBoy.FoodieHub.Request.DTOs.LoginRequestDto;
import com.superBoy.FoodieHub.Request.DTOs.RegisterUserDto;
import com.superBoy.FoodieHub.Response.DTOs.LoginResponseDTO;

@Service
public class UserService implements IUserService {

	private UserRepository userrepo;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userrepo, BCryptPasswordEncoder encoder) {
		super();
		this.userrepo = userrepo;
		this.passwordEncoder = encoder;
	}

	// creating the ROLE_USER
	@Override
	public String registerUser(RegisterUserDto user) {
		if (user != null) {
			Users NEWuser = new Users();
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			NEWuser.setUserName(user.getUserName());
			NEWuser.setPassword(encodedPassword);
			NEWuser.setRole(AdminRole.USER);
			NEWuser.setEmail(user.getEmail());
			userrepo.save(NEWuser);
			return "Successfully Register";
		}
		return "User is null";
	}

	@Override
	public String registerAdmin(RegisterUserDto admin) {
		if (admin != null) {
			Users NEWuser = new Users();
			String encodedPassword = passwordEncoder.encode(admin.getPassword());
			NEWuser.setUserName(admin.getUserName());
			NEWuser.setPassword(encodedPassword);
			NEWuser.setRole(AdminRole.ADMIN);
			NEWuser.setEmail(admin.getEmail());
			userrepo.save(NEWuser);
			return " Admin_Register Successfully";
		}
		return " Admin is Null";
	}

	@Override
	public LoginResponseDTO login(LoginRequestDto loginRequest) {
		Users user = userrepo.findByUserNameOrEmail(loginRequest.getUserName(), loginRequest.getUserName())
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid username or password");
		}

		return new LoginResponseDTO(
				user.getUserId().longValue(),
				user.getRole().name(),
				user.getUserName(),
				"Login successful"
		);
	}

}
