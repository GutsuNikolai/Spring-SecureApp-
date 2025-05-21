package com.example.security_app.controller;

import com.example.security_app.DTO.AdminGetUsers;
import com.example.security_app.DTO.UserRequest;
import com.example.security_app.model.Role;
import com.example.security_app.model.User;
import com.example.security_app.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/setRole")
    public User setRole(@RequestBody UserRequest request){
        return userService.setRole(request.getUsername(), request.getRoles());
    }

    @GetMapping("/allUsers")
    public List<AdminGetUsers> getAllUsers() {
        return userService.findAll().stream()
                .map(user -> new AdminGetUsers(
                        user.getUsername(),
                        user.getRoles().stream().map(Role::getName).toList()
                ))
                .toList();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        try{
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok("User " + username + " was deleted successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("User " + username + " not found");
        }
    }

}
