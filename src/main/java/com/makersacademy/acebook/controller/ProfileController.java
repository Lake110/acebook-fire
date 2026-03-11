package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/profile")
    public ModelAndView showProfile() {
        ModelAndView modelAndView = new ModelAndView("posts/profile");

        //gets Autho0 user currently logged in
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        //get email
        String email = (String) principal.getAttributes().get("email");
        User user = userRepository.findUserByEmail(email).orElseThrow();  //throwe xception if not found

        modelAndView.addObject("posts", postRepository.findByUser(user));
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
