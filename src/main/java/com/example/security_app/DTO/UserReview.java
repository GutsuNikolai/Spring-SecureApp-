package com.example.security_app.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;

@Data
public class UserReview {
    @JsonView(Views.UserView.class)
    private String username;

    @JsonView(Views.ModeratorView.class)
    private List<String> roles;

    public UserReview(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}
