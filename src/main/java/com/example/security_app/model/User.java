package com.example.security_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Имя пользователя не может быть null")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles;

    public User(){}

    public User(String username,String password, Set<Role> roles ) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Автогенерируемые не принимает, дает ошибку
    public Set<Role> getRoles(){return roles;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}

}
