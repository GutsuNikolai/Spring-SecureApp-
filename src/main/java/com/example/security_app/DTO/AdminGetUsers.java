package com.example.security_app.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


public class AdminGetUsers {
    private String username;
    private List<String> roles;

    public AdminGetUsers(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}

