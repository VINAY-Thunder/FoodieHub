package com.superBoy.FoodieHub.Response.DTOs;

public class LoginResponseDTO {

    private Long userId;
    private String role;
    private String userName;
    private String message;

    public LoginResponseDTO() {}

    public LoginResponseDTO(Long userId, String role, String userName, String message) {
        this.userId = userId;
        this.role = role;
        this.userName = userName;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
