package com.superBoy.FoodieHub.Impl_Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.Model.Users;
import com.superBoy.FoodieHub.Repository.UserRepository;

@Service
public class CustomeUserDeatilsService implements UserDetailsService{

	private UserRepository userrepo;
	
	@Autowired
	public CustomeUserDeatilsService(UserRepository userrepo) {
		super();
		this.userrepo = userrepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users UserFromDb = userrepo.findByUserName(username)
			.orElseThrow(()->new UsernameNotFoundException(username+" UserNot Exist pls Register"));
		return new MyUserPrincipal(UserFromDb);
	}

}
