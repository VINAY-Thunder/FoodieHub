package com.superBoy.FoodieHub.WebConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.superBoy.FoodieHub.Impl_Service.CustomeUserDeatilsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private CustomeUserDeatilsService customeUserDeatilsService;
	
	@Autowired
	public SecurityConfig(CustomeUserDeatilsService customeUserDeatilsService) {
		super();
		this.customeUserDeatilsService = customeUserDeatilsService;
	}


	@Bean
	public SecurityFilterChain securityChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(crsf -> crsf.disable())
			.authorizeHttpRequests(auth -> auth
					.anyRequest().permitAll()
			)
			.httpBasic(Customizer.withDefaults())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
	
	@Bean
	public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
		org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
		configuration.setAllowedOriginPatterns(java.util.List.of("*"));
		configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(java.util.List.of("*"));
		configuration.setAllowCredentials(true);
		org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	
	@Bean
	public AuthenticationManager authManger(AuthenticationConfiguration config) throws Exception {
		return  config.getAuthenticationManager();
	}
	
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider Dao = new DaoAuthenticationProvider();
		Dao.setUserDetailsService(customeUserDeatilsService);
		Dao.setPasswordEncoder(new BCryptPasswordEncoder(12));
		return Dao;
	}
	
	
	@Bean
	public BCryptPasswordEncoder passordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
}
