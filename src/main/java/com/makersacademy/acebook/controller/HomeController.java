package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class HomeController {

	@Autowired
	private PostRepository postRepository;

	@GetMapping("/")
	public ModelAndView IndexPage() {
		ModelAndView modelAndView = new ModelAndView("posts/homefeed");
		List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
		modelAndView.addObject("posts", posts);
		return modelAndView;
	}
}
