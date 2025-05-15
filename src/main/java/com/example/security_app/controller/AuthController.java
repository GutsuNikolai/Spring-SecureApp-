package com.example.security_app.controller;

import com.example.security_app.DTO.LoginRequest;
import com.example.security_app.config.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.security_app.model.User;
import com.example.security_app.model.Role;
import com.example.security_app.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;  // Репозиторий для работы с пользователями



    // Эндпоинт для логина пользователя
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user != null && new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {

            // ⬇ Преобразуем Set<Role> → List<String>
            List<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();

            // ✅ Передаём список ролей в JwtUtil
            return JwtUtil.generateToken(user.getUsername(), roleNames);
        } else {
            // 🔐 Задержка 1 секунда при неправильном пароле
            try {
                Thread.sleep(5000); // задержка в миллисекундах
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Invalid username or password");
        }
    }

}
