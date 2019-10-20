package org.housework.server.controllers;

import org.apache.commons.codec.digest.DigestUtils;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@PostMapping("/api/user/login")
	public @ResponseBody boolean login(String login, String password) {
		User user = userRepository.findByLogin(login);
		
		String sha1Password = DigestUtils.sha1Hex(SALT + password);
				
		if("admin".equals(login) && user == null) {
			user = new User();
			user.setLogin(login);
			user.setPassword(sha1Password);
			userRepository.save(user);
			LOG.info("Create admin account");
		}
		
		LOG.info(user.getPassword());
		
		if(! sha1Password.equals(user.getPassword())) {
			return false;
		}
		
		LOG.info("Logged in " + user.getLogin());
		return true;
	}
	
	@GetMapping("/api/user/list")
	public @ResponseBody Iterable<User> list() {		
		return userRepository.findAll();
	}
}
