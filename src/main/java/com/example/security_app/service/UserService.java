package com.example.security_app.service;

import com.example.security_app.DTO.UserRequest;
import com.example.security_app.model.User;
import com.example.security_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserRequest request) {
        User user = new User(request.getUsername(), request.getPassword());
//        user.setPassword("testPass");
        return userRepository.save(user);
    }

    public Optional<User> findByUsername (String username) {
        return userRepository.findByUsername(username);
    }

}
