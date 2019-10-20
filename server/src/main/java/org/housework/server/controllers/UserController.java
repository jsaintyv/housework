package org.housework.server.controllers;

import java.util.ArrayList;
import java.util.List;

import org.housework.server.front.UserFront;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	private static final String SALT = "ad2azp5oa";
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	public UserController() {
	}

	@Autowired
	UserRepository userRepository;

	@GetMapping("/api/user/current")
	public @ResponseBody UserFront current() {		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LOG.info("Current user:" + principal);
		return new UserFront((User) principal);
	}
	
	@GetMapping("/api/user/list")
	public @ResponseBody List<UserFront> list() {	
		List<UserFront> users = new ArrayList<UserFront>();
		for(User u:  userRepository.findAll()) {
			users.add(new UserFront(u));
		}
		return users;
	}
	
	@GetMapping("/rejected")
	public @ResponseBody boolean rejected() {			
		return false;
	}
}
