package com.example.security_app.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {

    @NotNull(message = "Имя пользователя не может быть null")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    private Set<String> roles;

    // Геттеры и сеттеры, ибо автогенерируемые не принимает, дает ошибку
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public Set<String> getRoles(){return roles;}

    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
    public void setRoles(Set<String> roles){this.roles = roles;}
}
