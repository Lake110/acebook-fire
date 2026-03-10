package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Getter @Setter
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime updatedAt;

    public Post() {}

    public Post(String content) {
        this.content = content;
    }

}
