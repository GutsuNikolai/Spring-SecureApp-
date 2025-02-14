package com.example.security_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "roles")  // Таблица в БД
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Автоинкремент ID
    private Long id;

    @Column(name = "name", unique = true, nullable = false)  // Уникальное и обязательное поле
    private String name;

    // Связь многие ко многим с пользователями
    @ManyToMany(mappedBy = "roles")  // Указывает, что связь с пользователями уже настроена в сущности User
    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }
}
