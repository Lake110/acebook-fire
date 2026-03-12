package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "FRIENDS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"friender_user_id", "friendee_user_id"})
)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "friender_user_id")
    private Long userId;

    @Column(name = "friendee_user_id")
    private Long friendId;

    public Friend() {}

    public Friend(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}