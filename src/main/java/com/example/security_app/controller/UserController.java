package com.example.security_app.controller;

import com.example.security_app.DTO.UserRequest;
import com.example.security_app.model.User;
import com.example.security_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController{

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createuser(@RequestBody UserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable UserRequest request){
        return userService.findByUsername(request.getUsername());

    }
}