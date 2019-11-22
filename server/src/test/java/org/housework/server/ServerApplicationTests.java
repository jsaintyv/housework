package org.housework.server;

import java.util.Date;
import java.util.List;

import org.housework.server.models.House;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.RoleAuthority;
import org.housework.server.models.TaskType;
import org.housework.server.models.TaskTypeRepository;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.housework.server.models.Work;
import org.housework.server.models.WorkRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HouseRepository houseRepository;

	@Autowired
	TaskTypeRepository taskTypeRepository;
	
	@Autowired
	WorkRepository workRepository;

	@Test
	void testUser() {
		final String email = "toto@gmail.com";
		User user = new User();
		user.setLogin(email);
		user.setPassword("toto");
		user.setEnabled(true);
		user.setRole(RoleAuthority.ADMIN);
		userRepository.save(user);
		
		int id = user.getId();
		
		User r = userRepository.findById(id).orElse(null);
		Assertions.assertEquals(r, user);
		user =r;
		
		final String homeName = "sweet";
		
		House house = new House();
		house.setName(homeName);
		house.setOwner(user);
		house = houseRepository.save(house);
		
	 	List<House> list = houseRepository.findByOwner(user);
	 	Assertions.assertEquals(list.size(), 1);
	 	Assertions.assertEquals(list.get(0).getName(), homeName);
	 	
	 	User userChild = new User();
	 	userChild.setLogin("marie");
	 	userChild.setPassword("marie");
	 	userChild.setEnabled(true);
	 	userChild.setRole(RoleAuthority.USER);
		userRepository.save(userChild);
		
		house.getUsers().add(userChild);
		houseRepository.save(house);
		
		house = houseRepository.findById(house.getId()).orElse(null);
		Assertions.assertEquals(house.getUsers().size(), 1);
	 	Assertions.assertTrue(house.getUsers().contains(userChild));
	 	
		
		list = houseRepository.findByUsers(userChild.getId()); 
	 	Assertions.assertEquals(list.size(), 1);
	 	Assertions.assertEquals(list.get(0).getName(), homeName);
	 	
	 	house.setOwner(user);
	 
	 	TaskType type = new TaskType();
	 	type.setName("Lave-vaisselle");
	 	type.setValue(1.0);
	 	taskTypeRepository.save(type);
	 	house.getTypes().add(type);
	 	
	 	type = new TaskType();
	 	type.setName("Mettre la table");
	 	type.setValue(1.0);
	 	type = taskTypeRepository.save(type);
	 	house.getTypes().add(type);
	 	
	 	houseRepository.save(house);
	 	
	 	house = houseRepository.findById(house.getId()).orElse(null);
		Assertions.assertEquals(house.getTypes().size(), 2);
		
		Work work = new Work();
		work.setHouse(house);
		work.setWorker(userChild);
		work.setDate(new Date());
		workRepository.save(work);
		
		List<Work> works = workRepository.findByUsers(userChild.getId());
		Assertions.assertEquals(works.size(), 1);
	}
}
