package com.example.security_app.service;

import com.example.security_app.model.Role;
import com.example.security_app.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private RoleService (RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Метод для добавления роли
    public Role createRole(String name) {
        Role role = new Role(name);
        return roleRepository.save(role);
    }

    // Метод для проверкки существования роли
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
