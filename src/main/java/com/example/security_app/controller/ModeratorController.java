package com.example.security_app.controller;

import com.example.security_app.DTO.UserReview;
import com.example.security_app.DTO.Views;
import com.example.security_app.repository.UserRepository;
import com.example.security_app.model.Role;
import com.example.security_app.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.security_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/moderator")
public class ModeratorController {

    private final UserService userService;
    public ModeratorController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete/{postId}")
    public void deletePost(@PathVariable int postId){
        System.out.println("Post with id = " + postId + " was deleted successfully");
    }

    @GetMapping("/review")
    @JsonView(Views.ModeratorView.class)
    public List<UserReview> reviewUsers() {
        return userService.getAllUsers().stream()
                .map(user -> new UserReview(
                        user.getUsername(),
                        user.getRoles().stream().map(Role::getName).toList()
                ))
                .toList();
    }

    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }


}
