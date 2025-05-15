package com.example.security_app.service;

import com.example.security_app.model.Role;
import com.example.security_app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

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
