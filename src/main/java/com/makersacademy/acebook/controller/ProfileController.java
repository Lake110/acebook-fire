package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.AvatarOptions;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLOutput;


@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    // helper method to avoid repeating the auth logic
    private User getLoggedInUser() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String email = (String) principal.getAttributes().get("email");
        return userRepository.findUserByEmail(email).orElseThrow();
    }

    // navbar profile button redirects to logged in user's profile
    @GetMapping("/profile")
    public String redirectToOwnProfile() {
        User loggedInUser = getLoggedInUser();
        return "redirect:/profile/" + loggedInUser.getId();
    }

    @GetMapping("/profile/{id}")
    public ModelAndView showProfile(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("posts/profile");

        User user = userRepository.findById(id).orElseThrow();
        User loggedInUser = getLoggedInUser();

        modelAndView.addObject("posts", postRepository.findByUser(user));
        modelAndView.addObject("user", user);
        modelAndView.addObject("isOwnProfile", loggedInUser.getId().equals(user.getId()));
        modelAndView.addObject("avatarStyles", AvatarOptions.STYLES);
        return modelAndView;
    }

    @PostMapping("/profile/{id}/avatar")
    public String updateAvatar(@PathVariable Long id, @RequestParam String avatarStyle) {
        User loggedInUser = getLoggedInUser();
        User user = userRepository.findById(id).orElseThrow();

        // Only the owner can update their avatar
        if (!loggedInUser.getId().equals(user.getId())) {
            return "redirect:/profile/" + id + "?error=unauthorized";
        }

        // Ensure the style is one of the allowed options
        if (!AvatarOptions.STYLES.contains(avatarStyle)) {
            return "redirect:/profile/" + id + "?error=invalid";
        }

        user.setAvatarStyle(avatarStyle);
        userRepository.save(user);
        return "redirect:/profile/" + id;
    }

    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam(required = false) String bio,
                              @RequestParam(required = false) String relationshipStatus) {
        User user = getLoggedInUser();
        user.setBio(bio);
        user.setRelationshipStatus(relationshipStatus);
        userRepository.save(user);
        return "redirect:/profile/" + user.getId();
    }
}