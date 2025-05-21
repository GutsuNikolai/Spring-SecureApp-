package com.example.security_app.service;

import com.example.security_app.DTO.UserRequest;
import com.example.security_app.config.SecurityConfig;
import com.example.security_app.exception.ConflictException;
import com.example.security_app.model.Role;
import com.example.security_app.model.User;
import com.example.security_app.repository.RoleRepository;
import com.example.security_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = securityConfig.passwordEncoder();
    }

    public User createUser(UserRequest request) {

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ConflictException("Username is already taken");
        }
        // ШИФРУЕМ ПАРОЛЬ перед сохранением
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), hashedPassword, roles);
        return userRepository.save(user);
    }

    @Transactional
    public User setRole(String username, Set <String> roleNames){
        // Получаю пользователя из бд по имени (проверяю если есть)
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Поиск ролей и добавление
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.getRoles().addAll(roles);

        return userRepository.save(user);
    }

    public Optional<User> findByUsername (String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUserByUsername(String username ){
        User user = userRepository.findByUsername((username)).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
