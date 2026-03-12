package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // Check if user has already liked a post
    boolean existsByUserAndPost(User user, Post post);

    // Add/ Remove a like
    @Transactional
    void deleteByUserAndPost(User user, Post post);

    // Count no. likes
    long countByPost(Post post);
}
