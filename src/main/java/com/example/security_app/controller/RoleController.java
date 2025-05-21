package com.example.security_app.controller;

import com.example.security_app.model.Role;
import com.example.security_app.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RequestMapping("/roles")
@RestController
public class RoleController {

    private final RoleService roleService;
    private RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public Role createRole(@RequestParam String name){
        return roleService.createRole(name);
    }

    @GetMapping("/{name}")
    public Optional<Role> getRoleByName(@PathVariable String name ){
        return roleService.findByName(name);
    }

}
