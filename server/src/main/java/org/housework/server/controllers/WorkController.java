package org.housework.server.controllers;

import java.util.Date;
import java.util.List;

import org.housework.server.UserSecurityService;
import org.housework.server.front.WorkForm;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.TaskType;
import org.housework.server.models.TaskTypeRepository;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.housework.server.models.Work;
import org.housework.server.models.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkController {
	@Autowired
	HouseRepository houseRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TaskTypeRepository taskTypeRepository;

	@Autowired
	WorkRepository workRepository;

	@Autowired
	UserSecurityService userSecurityService;

	
	@PostMapping("/api/work/create/{houseId}")
	public ResponseEntity<Work> insert(@PathVariable int houseId, @RequestBody WorkForm form) {
		return HouseRightUtils.checkRightOnHouse(houseRepository, houseId, (house)-> {
			User user = userRepository.findById(form.getWorkerId()).orElse(null);
			if(user == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			if(! house.accept(user)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			TaskType type = house.getTypes().stream().filter((t)->t.getId() == form.getTaskTypeId()).findFirst().orElse(null);
			if(type == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			Work work = new Work();
			work.setDate(new Date(form.getDate()));
			work.setType(type);
			work.setHouse(house);
			work.setWorker(user);
			work = workRepository.save(work);
			return new ResponseEntity<>(work, HttpStatus.OK);
		});
	}
	
	
	@GetMapping("/api/work/list/{houseId}/{start}/{end}")
	public ResponseEntity<List<Work>> insert(@PathVariable int houseId, @PathVariable long start, @PathVariable long end) {
		return HouseRightUtils.checkRightOnHouse(houseRepository, houseId, (house)-> {
			return new ResponseEntity<>(workRepository.findBy(new Date(start), new Date(end), house), HttpStatus.OK);
		});
	}
	
	@DeleteMapping("/api/work/remove/{houseId}/{workId}")
	public ResponseEntity<Boolean> insert(@PathVariable int houseId, @PathVariable int workId) {
		return HouseRightUtils.checkRightOnHouse(houseRepository, houseId, (house)-> {
			Work work = workRepository.findById(workId).orElse(null);
			if(work == null) {
				return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
			}
			
			if(work.getHouse().getId() != house.getId()) {
				return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
			}
			
			workRepository.delete(work);
			
			return new ResponseEntity<>(true, HttpStatus.OK); 
		});
	}
}