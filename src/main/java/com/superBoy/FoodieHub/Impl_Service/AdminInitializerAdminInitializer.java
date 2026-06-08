package com.superBoy.FoodieHub.Impl_Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.superBoy.FoodieHub.Enums.AdminRole;
import com.superBoy.FoodieHub.Model.Users;
import com.superBoy.FoodieHub.Repository.UserRepository;

@Component
public class AdminInitializerAdminInitializer implements CommandLineRunner {

	private BCryptPasswordEncoder encodePassword;
	private UserRepository userRepo;

	@Autowired
	public AdminInitializerAdminInitializer(BCryptPasswordEncoder encodePassword, UserRepository userRepo) {
		super();
		this.encodePassword = encodePassword;
		this.userRepo = userRepo;
	}

	@Override
	public void run(String... args) throws Exception {

		if (userRepo.findByUserName("superadmin").isEmpty()) {
			Users adminUsers = new Users();
			adminUsers.setEmail("superadmin@gmail.com");
			adminUsers.setPassword(encodePassword.encode("admin2026"));
			adminUsers.setUserName("superadmin");
			adminUsers.setRole(AdminRole.ADMIN);
			userRepo.save(adminUsers);
			System.out.println(">> [FoodieHub] First Admin account ('foodieadmin') created successfully!");
		} else {
			System.out.println(">> [FoodieHub] Admin already exists. Skipping database initialization.");
		}
	}

}
