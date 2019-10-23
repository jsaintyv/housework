package org.housework.server.controllers;

import java.util.List;

import org.housework.server.UserSecurityService;
import org.housework.server.front.HouseForm;
import org.housework.server.models.House;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HouseController {
	private static final Logger LOG = LoggerFactory.getLogger(HouseController.class);
	
	@Autowired
	HouseRepository houseRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserSecurityService userSecurityService;
			
	public HouseController() {		
	}
	
	@PostMapping("/api/house/create")
	public ResponseEntity<House>  insert(HouseForm form) {					
		House house = new House();
		form.copyInto(house);
		house.setOwner(userSecurityService.getCurrentUser());					
		houseRepository.save(house);		
		return new ResponseEntity<>(house, HttpStatus.CREATED);
	}
	
	@GetMapping("/api/house/retrieve/{id}")
	public ResponseEntity<House>  retrieve(Integer id) {		
		House house = houseRepository.findById(id).orElse(null);
		if(house == null) {
			return new ResponseEntity<House>(HttpStatus.NOT_FOUND);
		}
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(! house.accept(user)) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<House>(HttpStatus.FORBIDDEN);
		}
								
		return new ResponseEntity<>(house, HttpStatus.FOUND);
	}
	
	@GetMapping("/api/house/listOwned")
	public ResponseEntity<List<House>>  listOwned() {		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user == null) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<List<House>>(HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<>(houseRepository.findByOwner(user), HttpStatus.FOUND);
	}
	
	@GetMapping("/api/house/listAvailables")
	public ResponseEntity<List<House>>  listAvailables() {		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user == null) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<List<House>>(HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<>(houseRepository.findByUsers(user.getId()), HttpStatus.FOUND);
	}
		
	@PostMapping("/api/house/update/{id}")
	public ResponseEntity<House>  update(Integer id, HouseForm form) {
		House house = houseRepository.findById(id).orElse(null);
		if(house == null) {
			return new ResponseEntity<House>(HttpStatus.NOT_FOUND);
		}
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(! house.accept(user)) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<House>(HttpStatus.FORBIDDEN);
		}
		
		
		form.copyInto(house);
		house.setOwner(userSecurityService.getCurrentUser());					
		houseRepository.save(house);
		
	
		return new ResponseEntity<>(house, HttpStatus.ACCEPTED);
	}
	
	
	@DeleteMapping("/api/house/delete/{id}")
	public ResponseEntity<House>  delete(Integer id) {
		House house = houseRepository.findById(id).orElse(null);
		if(house == null) {
			return new ResponseEntity<House>(HttpStatus.NOT_FOUND);
		}
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(! house.accept(user)) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<House>(HttpStatus.FORBIDDEN);
		}
									
		houseRepository.delete(house);	
		return new ResponseEntity<>(house, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/api/house/register/{id}")
	public ResponseEntity<House>  register(Integer id, String login) {
		House house = houseRepository.findById(id).orElse(null);
		if(house == null) {
			return new ResponseEntity<House>(HttpStatus.NOT_FOUND);
		}
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(! house.accept(user)) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<House>(HttpStatus.FORBIDDEN);
		}
		
		User newUser = userRepository.findByLogin(login);
		if(newUser == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		house.getUsers().add(newUser);		
		houseRepository.save(house);
		
		return new ResponseEntity<>(house, HttpStatus.ACCEPTED);
	}
	
	
}
