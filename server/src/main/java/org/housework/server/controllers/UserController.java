package org.housework.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.housework.server.front.UserFront;
import org.housework.server.front.UserUpdateForm;
import org.housework.server.mail.MailService;
import org.housework.server.models.RoleAuthority;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	private static final Random random = new Random();
	
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
	
	@PostMapping("/register")
	public ResponseEntity<Boolean>  register(HttpServletRequest request, String login, String password) {
		if(!(login.contains("@") && login.contains("."))) {
			return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
		
		User user = new User();
		user.setLogin(login);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		user.setEnabled(false);
		user.setRole(RoleAuthority.USER);
		user.setRegisteringCode(getNextRandomLong());
		userRepository.save(user);
				
		MailService.instance().sendRegistrationUserConfirmation(user);		
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/api/user/update/{userId}")
	public ResponseEntity<Boolean>  update(@PathVariable Integer userId, @RequestBody UserUpdateForm form) {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userId == null) {
			return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
		
		if(userId != user.getId()) {
			return new ResponseEntity<>(Boolean.FALSE, HttpStatus.FORBIDDEN);
		}			
		form.copyInto(user);		
		userRepository.save(user);
				
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.ACCEPTED);
	}

	public static long getNextRandomLong() {
		return random.nextLong() ^ (System.nanoTime()%((long)1e6));
	}

	@GetMapping("/confirmRegister")
	public  RedirectView  register(String login, long registeringCode) {
		
		if(!(login.contains("@") && login.contains("."))) {
			return new RedirectView("/registerFailed.html");
		}
		
		User user = userRepository.findByLogin(login);
		if(user == null || user.getRegisteringCode() != registeringCode) {
			return new RedirectView("/registerFailed.html");
		}
		
		user.setEnabled(true);
		userRepository.save(user);
		
		LOG.info(login + " is now Ok !");


		return new RedirectView("/registerCompleted.html");
	}
}
