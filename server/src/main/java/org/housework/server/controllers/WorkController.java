package org.housework.server.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.efficientolap.OlapStreamQueryAsJson;
import org.housework.server.UserSecurityService;
import org.housework.server.front.WorkForm;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.TaskType;
import org.housework.server.models.TaskTypeRepository;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.housework.server.models.ValueByUser;
import org.housework.server.models.ValueByUserService;
import org.housework.server.models.Work;
import org.housework.server.models.WorkRepository;
import org.housework.server.olap.DataMartHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
	
	@Autowired
	ValueByUserService valueByUserService;

	
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
						
			User connected = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			if(connected.getId() != house.getOwner().getId()) {
				if(Boolean.TRUE.equals(type.isReservedToAdmin())) {
					System.out.println("reject: reserved to admin");
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
				
				Calendar cal  = Calendar.getInstance();
				cal.set(Calendar.HOUR, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.add(Calendar.DAY_OF_MONTH,-2);
				if(work.getDate().getTime() < cal.getTimeInMillis()) {
					System.out.println("reject: too old");
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
			}
			
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
	
	@GetMapping("/api/work/scoresByUser/{houseId}")
	public ResponseEntity<List<ValueByUser>> scoreByUser(@PathVariable int houseId) {
		return HouseRightUtils.checkRightOnHouse(houseRepository, houseId, (house)-> {
			return new ResponseEntity<>(valueByUserService.list(houseId), HttpStatus.OK);
		});
	}
	
	
	@GetMapping("/api/olap/worksByWeek/{houseId}")
	public ResponseEntity<StreamingResponseBody> worksByWeek(@PathVariable int houseId, HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		return HouseRightUtils.checkRightOnHouse(houseRepository, houseId, (house)-> {			
			DataMartHouse dataMart = DataMartHouse.getDatamart(house);
			dataMart.update(house, workRepository);			
			StreamingResponseBody result = new StreamingResponseBody() {
				@Override
				public void writeTo(OutputStream out) throws IOException {
					
					try(OlapStreamQueryAsJson jsonStream = new OlapStreamQueryAsJson(out) ) {
						dataMart.scoreMeasure.forEachCell(jsonStream, dataMart.userDimension.any, dataMart.typeDimension.any, dataMart.weekDimension.any, dataMart.workIdDimension.any);					
					}
					out.flush();
				}
			};
			ResponseEntity<StreamingResponseBody> r = new ResponseEntity<StreamingResponseBody>(result, HttpStatus.OK);
			
			return r;					
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
			
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(user.getId() != house.getOwner().getId()) {
				if(Boolean.TRUE.equals(work.getType().isReservedToAdmin())) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
				
				if(work.getWorker().getId() != user.getId()) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
				
				Calendar cal  = Calendar.getInstance();
				cal.set(Calendar.HOUR, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.add(Calendar.DAY_OF_MONTH,-2);
				if(work.getDate().getTime() < cal.getTimeInMillis()) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
			}
			
			workRepository.delete(work);
			
			return new ResponseEntity<>(true, HttpStatus.OK); 
		});
	}
}
