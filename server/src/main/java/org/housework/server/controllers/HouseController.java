package org.housework.server.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.housework.server.UserSecurityService;
import org.housework.server.front.HouseForm;
import org.housework.server.mail.MailService;
import org.housework.server.models.House;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.PendingRegistration;
import org.housework.server.models.PendingRegistrationRepository;
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
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HouseController {	
	@Autowired
	HouseRepository houseRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PendingRegistrationRepository pendingRegistrationRepository;
	
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
		Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if((auth instanceof User)) {
			User user = (User) auth;		
			return ResponseEntity.ok(houseRepository.findByOwner(user));
		}
		
		return ResponseEntity.ok(new ArrayList<House>());
		
	}
	
	@GetMapping("/api/house/listAvailables")
	public ResponseEntity<List<House>>  listAvailables() {		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<House> houses = new HashSet<>(houseRepository.findByUsers(user.getId()));
		houses.addAll(houseRepository.findByOwner(user));
		return ResponseEntity.ok(new ArrayList<House>(houses));
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
			
			if(house.accept(newUser)) {
				// Something wrong, this user was already accepted.
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			PendingRegistration registration = new PendingRegistration();
			registration.setTarget(house);
			registration.setWorker(newUser);
			registration.setSecret(UserController.getNextRandomLong());
			registration = pendingRegistrationRepository.save(registration);
			
			MailService.instance().sendRegistrationConfirmationMail(registration);
			
			return new ResponseEntity<>(house, HttpStatus.ACCEPTED);
		});
	}
	
	@GetMapping("/confirmRegisteringhouse/{registrationId}/{workerId}/{secret}")
	public RedirectView  register(@PathVariable Integer registrationId, @PathVariable Integer workerId, @PathVariable Long secret) {
		System.out.println(registrationId);
		System.out.println(workerId);
		System.out.println(secret);
		PendingRegistration registration =  pendingRegistrationRepository.findById(registrationId).orElse(null);
		if(registration == null) {
			return new RedirectView("/registerFailed.html");
		}
		
		System.out.println("Secret:" + registration.getSecret()); 
		 
		if(registration.getSecret().longValue() != secret.longValue()) {
			return new RedirectView("/registerFailed.html");
		}

		System.out.println("Same secret"); 
		
		registration.getTarget().getUsers().add(registration.getWorker());
		houseRepository.save(registration.getTarget());
		
		return new RedirectView("/registerCompleted.html");
	}
}
