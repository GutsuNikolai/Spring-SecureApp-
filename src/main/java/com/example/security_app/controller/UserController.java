package com.example.security_app.controller;

import com.example.security_app.model.User;
import com.example.security_app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController{

    private final UserService userService;
    private UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        User user = userService.getCurrentUser();

        Map<String, Object> profile = new HashMap<>();
        profile.put("username", user.getUsername());

        return ResponseEntity.ok(profile);
    }
}