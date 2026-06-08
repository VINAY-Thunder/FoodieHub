package com.superBoy.FoodieHub.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
	
	// Correct way to check if a user exists returning a boolean
    boolean existsByUserName(String username);
    
    // For your CustomUserDetailsService login lookup
    Optional<Users> findByUserName(String username);
    
    // For logging in with either Username or Email
    Optional<Users> findByUserNameOrEmail(String username, String email);
}
