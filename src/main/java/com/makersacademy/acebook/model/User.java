package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import static java.lang.Boolean.TRUE;

@Data
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(nullable = false)
    private String avatarStyle = "thumbs";

    private boolean enabled;
    private String bio;
    private LocalDate birthday;
    private String relationshipStatus;

    public User() {
        this.enabled = TRUE;
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
        this.enabled = TRUE;
    }

    public String getAvatarUrl() {
        return "https://api.dicebear.com/9.x/" + avatarStyle + "/svg?seed=" + this.username;
    }

    public String getAvatarStyle() {
        return avatarStyle;
    }

    public void setAvatarStyle(String avatarStyle) {
        this.avatarStyle = avatarStyle;
    }
}