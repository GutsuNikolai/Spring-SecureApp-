package com.example.security_app.DTO;

import com.example.security_app.model.Role;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public class UserRequest {
    private String username;
    private String password;
    private Set<String> roles;

    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public Set<String> getRoles(){return roles;}

    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
    public void setRoles(Set<String> roles){this.roles = roles;}
}
