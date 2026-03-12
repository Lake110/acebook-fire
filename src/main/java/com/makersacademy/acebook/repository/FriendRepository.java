package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
    default boolean areFriends(Long userId, Long friendId)  {
        Long lower = Math.min(userId, friendId);
        Long higher = Math.max(userId, friendId);
        return existsByUserIdAndFriendId(lower, higher);
    }
    @Query("""
    SELECT COUNT(f)
    FROM Friend f
    WHERE f.userId = :userId OR f.friendId = :userId
""")
    long countFriends(@Param("userId") Long userId);
}