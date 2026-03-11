package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = repository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/posts";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email = (String) principal.getAttributes().get("email");
        String username = email.substring(0, email.indexOf('@'));

        userRepository.findUserByEmail(email).ifPresent(post::setUser);

        repository.save(post);
        redirectAttributes.addFlashAttribute("message", "Post submitted!");
        return new RedirectView("/posts");
    }

    @GetMapping("/posts/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/posts/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        Post existingPost = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        existingPost.setContent(post.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        repository.save(existingPost);
        redirectAttributes.addFlashAttribute("message", "Post updated!");
        return "redirect:/";
    }

    @PostMapping("/posts/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        repository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Post deleted!");
        return "redirect:/posts";
    }
}
