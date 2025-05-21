package com.example.security_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Setter
@Getter
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    // многие ко многим с пользователями
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {};
    public Role(String name) {
        this.name = name;
    }

    // Автогенерируемый не принимает, дает ошибку
    public String getName() {
        return name;
    }

}
