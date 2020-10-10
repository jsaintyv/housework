package org.housework.server.controllers;

import org.housework.server.UserSecurityService;
import org.housework.server.front.TaskTypeForm;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.TaskType;
import org.housework.server.models.TaskTypeRepository;
import org.housework.server.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskTypeController {
	@Autowired
	HouseRepository houseRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TaskTypeRepository taskTypeRepository;

	@Autowired
	UserSecurityService userSecurityService;

	@PostMapping("/api/taskType/create/{houseId}")
	public ResponseEntity<TaskType> insert(@PathVariable int houseId, @RequestBody TaskTypeForm taskTypeForm) {
		return HouseRightUtils.checkIsAdminOnHouse(houseRepository, houseId, (house) -> {
			TaskType taskType = new TaskType();
			taskTypeForm.copyInto(taskType);
			taskTypeRepository.save(taskType);
			house.getTypes().add(taskType);
			houseRepository.save(house);
			return new ResponseEntity<>(taskType, HttpStatus.ACCEPTED);
		});
	}
	
	@PostMapping("/api/taskType/update/{houseId}/{taskTypeId}")
	public ResponseEntity<TaskType> update(@PathVariable Integer houseId, @PathVariable Integer taskTypeId, @RequestBody TaskTypeForm taskTypeForm) {
		return HouseRightUtils.checkIsAdminOnHouse(houseRepository, houseId, (house) -> {
			TaskType taskType = house.getTypes().stream().filter((t)->t.getId() == taskTypeId).findAny().orElse(null);
			if(taskType == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			taskTypeForm.copyInto(taskType);
			System.out.println(taskType.isReservedToAdmin());
			taskTypeRepository.save(taskType);
			house.getTypes().add(taskType);
			houseRepository.save(house);
			return new ResponseEntity<>(taskType, HttpStatus.ACCEPTED);
		});
	}
	
	@DeleteMapping("/api/taskType/delete/{houseId}/{taskTypeId}")
	public ResponseEntity<TaskType> delete(@PathVariable Integer houseId, @PathVariable Integer taskTypeId) {
		return HouseRightUtils.checkIsAdminOnHouse(houseRepository, houseId, (house) -> {
			TaskType taskType = house.getTypes().stream().filter((t)->t.getId() == taskTypeId).findAny().orElse(null);
			if(taskType == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		
			taskTypeRepository.delete(taskType);
			house.getTypes().remove(taskType);
			houseRepository.save(house);
			return new ResponseEntity<>(taskType, HttpStatus.ACCEPTED);
		});
	}	
}
