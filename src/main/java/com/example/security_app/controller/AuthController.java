package com.example.security_app.controller;

import com.example.security_app.DTO.LoginRequest;
import com.example.security_app.DTO.UserRequest;
import com.example.security_app.config.JwtUtil;
import com.example.security_app.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.security_app.model.User;
import com.example.security_app.model.Role;
import com.example.security_app.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;  // Репозиторий для работы с пользователями
    @Autowired
    private UserService userService;

    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> lockoutUntil = new ConcurrentHashMap<>();

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 1 * 60 * 1000; // 3 минуты
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        logger.info("🔐 Попытка входа: " + loginRequest.getUsername() + " [" + new Date() + "]" );
        String username = loginRequest.getUsername();

        // 1. Проверяем — не заблокирован ли пользователь
        if (lockoutUntil.containsKey(username)) {
            long unlockTime = lockoutUntil.get(username);
            if (System.currentTimeMillis() < unlockTime) {
                throw new BadCredentialsException("Account temporarily locked. Try again later.");
            } else {
                // Разблокировка
                lockoutUntil.remove(username);
                loginAttempts.remove(username);
            }
        }

        // 2. Поиск пользователя
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        boolean correctPassword = new BCryptPasswordEncoder()
                .matches(loginRequest.getPassword(), user.getPassword());

        if (correctPassword) {
            loginAttempts.remove(username);
            // ⬇ Преобразуем Set<Role> → List<String>
            List<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();

            //  Передаём список ролей в JwtUtil
            logger.info("✅ Успешный вход: " + user.getUsername() + " [" + new Date() + "]" + "\n");

            return JwtUtil.generateToken(user.getUsername(), roleNames);
        } else {
            logger.info("❌ Ошибка входа: " + loginRequest.getUsername() + " [" + new Date() + "]" + "\n");
            loginAttempts.put(username, loginAttempts.getOrDefault(username, 0) + 1);
            //Задержка
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Если превысили лимит
            if (loginAttempts.get(username) >= MAX_ATTEMPTS) {
                logger.info("⛔ Пользователь " + loginRequest.getUsername() + " временно заблокирован на " +  LOCKOUT_DURATION + "\n");
                lockoutUntil.put(username, System.currentTimeMillis() + LOCKOUT_DURATION);
                throw new BadCredentialsException("Too many failed attempts. Account locked for 3 minutes.");
            }

            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public User createuser(@RequestBody @Valid UserRequest request){
        return userService.createUser(request);
    }

}
