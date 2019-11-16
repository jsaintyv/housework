package org.housework.server.controllers;

import java.util.function.Function;

import org.housework.server.models.House;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class HouseRightUtils {
	private HouseRightUtils() {
	}
	
	public static <T> ResponseEntity<T> checkRightOnHouse(HouseRepository houseRepository, int id, Function<House, ResponseEntity<T>> actionOnHouse) {
		House house = houseRepository.findById(id).orElse(null);
		if(house == null) {
			return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		}
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(! house.accept(user)) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<T>(HttpStatus.FORBIDDEN);
		}
		
		return actionOnHouse.apply(house);
	}
	
	public static <T> ResponseEntity<T> checkIsAdminOnHouse(HouseRepository houseRepository, int id, Function<House, ResponseEntity<T>> actionOnHouse) {
		House house = houseRepository.findById(id).orElse(null);
		if(house == null) {
			return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		}
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(! house.isAdmin(user)) {
			// Not found to avoid a scan of house.id
			return new ResponseEntity<T>(HttpStatus.FORBIDDEN);
		}
		
		return actionOnHouse.apply(house);
	}
}
