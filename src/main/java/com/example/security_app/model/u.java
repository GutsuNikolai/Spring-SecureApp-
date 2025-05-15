package com.example.security_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class u {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Constructors, getters, setters
    public u() {}

    public u(User user, Role role) {
         this.user = user;
         this.role = role;
    }
}
