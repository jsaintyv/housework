package org.housework.server.controllers;

import java.util.List;
import java.util.function.Function;

import org.housework.server.UserSecurityService;
import org.housework.server.front.HouseForm;
import org.housework.server.models.House;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HouseController {
	@Autowired
	HouseRepository houseRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserSecurityService userSecurityService;
			
	public HouseController() {		
	}
	
	
	
	@PostMapping("/api/house/create")
	public ResponseEntity<House>  insert(HouseForm houseForm) {					
		House house = new House();
		houseForm.copyInto(house);
		house.setOwner(userSecurityService.getCurrentUser());					
		houseRepository.save(house);		
		return new ResponseEntity<>(house, HttpStatus.CREATED);
	}
	
	@GetMapping("/api/house/retrieve/{id}")
	public ResponseEntity<House>  retrieve(Integer id) {		
		return HouseRightUtils.checkRightOnHouse(houseRepository, id, (house)-> new ResponseEntity<>(house, HttpStatus.FOUND));
	}
	
	@GetMapping("/api/house/listOwned")
	public ResponseEntity<List<House>>  listOwned() {		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return ResponseEntity.ok(houseRepository.findByOwner(user));
	}
	
	@GetMapping("/api/house/listAvailables")
	public ResponseEntity<List<House>>  listAvailables() {		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(houseRepository.findByUsers(user.getId()));
	}
		
	@PostMapping("/api/house/update/{id}")
	public ResponseEntity<House>  update(Integer id, HouseForm form) {
		return HouseRightUtils.checkIsAdminOnHouse(houseRepository, id, (house)-> {
			form.copyInto(house);					
			houseRepository.save(house);
			return new ResponseEntity<>(house, HttpStatus.ACCEPTED);
		});	
	}
	
	
	@DeleteMapping("/api/house/delete/{id}")
	public ResponseEntity<Boolean>  delete(@PathVariable("id")  Integer id) {
		return HouseRightUtils.checkIsAdminOnHouse(houseRepository, id, (house)-> {						
			houseRepository.delete(house);
			return ResponseEntity.ok(Boolean.TRUE);
		});
	}
	
	@PostMapping("/api/house/register/{id}")
	public ResponseEntity<House>  register(@PathVariable("id") Integer id, String login) {
		return HouseRightUtils.checkIsAdminOnHouse(houseRepository, id, (house)-> {
			User newUser = userRepository.findByLogin(login);
			if(newUser == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			house.getUsers().add(newUser);		
			houseRepository.save(house);
			
			return new ResponseEntity<>(house, HttpStatus.ACCEPTED);
		});
	}
}
