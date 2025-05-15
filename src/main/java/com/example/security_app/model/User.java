package com.example.security_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.Set;

//@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "users")

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

    public Set<Role> getRoles(){return roles;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}

//    public void setUsername(String username) { this.username = username; }
//    public void setPassword(String password) { this.password = password; }
//    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
