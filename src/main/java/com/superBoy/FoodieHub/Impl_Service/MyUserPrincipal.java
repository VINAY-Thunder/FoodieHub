package com.superBoy.FoodieHub.Impl_Service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.Model.Users;

public class MyUserPrincipal implements UserDetails{

	private Users users;
	
	public MyUserPrincipal(Users user) {
		super();
		this.users = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(
	            new SimpleGrantedAuthority("ROLE_" + users.getRole())
	    );
	}

	@Override
	public String getPassword() {
		return users.getPassword();
	}

	@Override
	public String getUsername() {
		return users.getUserName();
	}
}
