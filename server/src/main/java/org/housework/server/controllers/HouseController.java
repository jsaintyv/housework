package org.housework.server.controllers;

import org.housework.server.models.House;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HouseController {
	private static final Logger LOG = LoggerFactory.getLogger(HouseController.class);
	
	@Autowired
	HouseRepository houseRepository;
	
	public HouseController() {		
	}
	
	@GetMapping("/api/house/retrieve")
	public ResponseEntity<House>  retrieve(int id) {		
		House house = houseRepository.findById(id).orElse(null);
		if(house == null) {
			return new ResponseEntity<House>(HttpStatus.NOT_FOUND);
		}
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user == null) {
			return new ResponseEntity<House>(HttpStatus.FORBIDDEN);
		}
		
		if(! (house.getOwner().equals(user) 
				&& house.getUsers().contains(user))
	    ) {
			return new ResponseEntity<House>(HttpStatus.FORBIDDEN); 
		}
		
		
		return new ResponseEntity<>(house, HttpStatus.FOUND);
	}
	
	@PostMapping("/api/house/update")
	public ResponseEntity<House>  update(House house) {		
		
		return new ResponseEntity<>(house, HttpStatus.FOUND);
	}
	
}
