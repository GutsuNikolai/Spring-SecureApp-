package com.example.security_app.DTO;

public class LoginRequest {
    private String username;
    private String password;

    // Геттеры и сеттеры, ибо автогенерируемые не принимает, дает ошибку
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
