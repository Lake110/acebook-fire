package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.*;

@Data
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "post_likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}