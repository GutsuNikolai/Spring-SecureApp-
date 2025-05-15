package com.example.security_app.controller;

import com.example.security_app.DTO.UserRequest;
import com.example.security_app.DTO.UserReview;
import com.example.security_app.DTO.Views;
import com.example.security_app.model.User;
import com.example.security_app.model.Role;
import com.example.security_app.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController{

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createuser(@RequestBody UserRequest request){
        return userService.createUser(request);
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        User user = userService.getCurrentUser();

        Map<String, Object> profile = new HashMap<>();
        profile.put("username", user.getUsername());
        //profile.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return ResponseEntity.ok(profile);
    }
}