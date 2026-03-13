package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.makersacademy.acebook.repository.CommentRepository;

@Controller
public class UsersController extends BaseController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email = (String) principal.getAttributes().get("email");
        String username = email.substring(0, email.indexOf('@'));

        userRepository.findUserByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(username);
            return userRepository.save(newUser);
        });

        return new RedirectView("/posts");
    }

    @GetMapping("/settings")
    public ModelAndView settings() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String email = (String) principal.getAttributes().get("email");

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ModelAndView modelAndView = new ModelAndView("users/settings");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/users/settings/username")
    public RedirectView updateUsername(@RequestParam String username, RedirectAttributes redirectAttributes) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String email = (String) principal.getAttributes().get("email");

        userRepository.findUserByEmail(email).ifPresent(user -> {
            user.setUsername(username);
            userRepository.save(user);
        });
        redirectAttributes.addFlashAttribute("message", "Username Updated!");
        return new RedirectView("/settings");
    }

    @Transactional
    @PostMapping("/users/settings/delete")
    public RedirectView deleteAccount(HttpSession session) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = (String) principal.getAttributes().get("email");

        userRepository.findUserByEmail(email).ifPresent(user -> {
            commentRepository.deleteAllByUser(user);
            postRepository.deleteAllByUser(user);
            userRepository.delete(user);
        });

        session.invalidate();
        return new RedirectView("/");
    }

    @PostMapping("/users/settings/profile")
    public RedirectView updateProfile(
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String birthday,
            @RequestParam(required = false) String relationshipStatus,
            RedirectAttributes redirectAttributes) {

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email = (String) principal.getAttributes().get("email");

        userRepository.findUserByEmail(email).ifPresent(user -> {
            user.setBio(bio);

            if (birthday != null && !birthday.isEmpty()) {
                user.setBirthday(java.time.LocalDate.parse(birthday));
            }

            user.setRelationshipStatus(relationshipStatus);

            userRepository.save(user);
        });
        redirectAttributes.addFlashAttribute("message2", "Profile Updated!");
        return new RedirectView("/settings");
    }

}