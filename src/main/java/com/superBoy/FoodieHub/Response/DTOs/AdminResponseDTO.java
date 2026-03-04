package com.superBoy.FoodieHub.Response.DTOs;

import java.time.LocalDateTime;

import com.superBoy.FoodieHub.Enums.AdminRole;

public class AdminResponseDTO {
	private Long adminId;
	private String name;
	private String email;
	private String phone;
	private AdminRole role;
	private LocalDateTime createdAt;

	public AdminResponseDTO() {
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public AdminRole getRole() {
		return role;
	}

	public void setRole(AdminRole role) {
		this.role = role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
