package com.example.security_app.DTO;

import com.example.security_app.model.Role;

import java.util.List;
import java.util.Set;

public class UserRequest {
    private String username;
    private String password;
//    private Set<Role> roles;

    public String getUsername(){return username;}
    public String getPassword(){return password;}
//    public Set<Role> getRoles(){return roles;}

    public void setUserName(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
//    public void setRoles(Set<Role> roles){this.roles = roles;}
}
