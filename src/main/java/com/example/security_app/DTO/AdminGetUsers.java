package com.example.security_app.DTO;

import java.util.List;

public record AdminGetUsers(String username, List<String> roles) {
}


