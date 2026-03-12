package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Friend;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FriendRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

import java.sql.SQLOutput;
import java.util.Optional;
@Controller
public class FriendController {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    // helper method to avoid repeating the auth logic
    private User getLoggedInUser() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String email = (String) principal.getAttributes().get("email");
        return userRepository.findUserByEmail(email).orElseThrow();
    }

    @PostMapping("/friends/add/{friendId}")
    public String addFriend(@PathVariable Long friendId) {

        User loggedInUser = getLoggedInUser();

        Long userId = loggedInUser.getId();
        if (userId.equals(friendId)) {
            return "redirect:/profile/" + friendId; // cannot add yourself
        }
        Long lower = Math.min(userId, friendId);
        Long higher = Math.max(userId, friendId);
        //checks if already in db

        try {
            if (!friendRepository.existsByUserIdAndFriendId(lower, higher)) {
                friendRepository.save(new Friend(lower, higher));
            }
        } catch (Exception e) {
            // handles race conditions if two requests insert at the same time
            System.out.println("Duplicate friendship prevented: " + lower + " <-> " + higher);
        }

        return "redirect:/profile/" + friendId;
    }
}

