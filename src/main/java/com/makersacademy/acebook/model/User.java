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

    private boolean enabled;

    private String bio;
    private LocalDate birthday;

    @Column(name = "relationship_status")
    private String relationshipStatus;



    public User() {
        this.enabled = TRUE;
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
        this.enabled = TRUE;
    }
}