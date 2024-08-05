package com.example.bookstore_app.dto;

public class LoginResponse {
    private String jwt;

    public LoginResponse(String jwt) {
        this.jwt = jwt;
    }

    // Getter

    public String getJwt() {
        return jwt;
    }
}
