package org.housework.server;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.efficientolap.OlapMember;
import org.housework.server.models.House;
import org.housework.server.models.HouseRepository;
import org.housework.server.models.RoleAuthority;
import org.housework.server.models.TaskType;
import org.housework.server.models.TaskTypeRepository;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.housework.server.models.Work;
import org.housework.server.models.WorkRepository;
import org.housework.server.olap.DataMartHouse;
import org.housework.server.utils.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerApplicationTests {
	private static final String TASK_METTRE_LA_TABLE = "Mettre la table";
	private static final String TASK_LAVE_VAISSELLE = "Lave-vaisselle";
	private static final String WORKER1 = "worker1";
	private static final String WORKER2 = "worker2";

	@Autowired
	UserRepository userRepository;

	@Autowired
	HouseRepository houseRepository;

	@Autowired
	TaskTypeRepository taskTypeRepository;

	@Autowired
	WorkRepository workRepository;

	@Autowired
	DataSource dataSource;

	House house;

	@Test
	void testScenario() {
		prepareDataModel();
		olapQuery();
	}

	private void prepareDataModel() {
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
		user = r;

		final String homeName = "sweet";

		house = new House();
		house.setName(homeName);
		house.setOwner(user);
		house = houseRepository.save(house);
		final User worker1 = buildChild(WORKER1);
		final User worker2 = buildChild(WORKER2);

		List<House> list = houseRepository.findByOwner(user);
		Assertions.assertEquals(list.size(), 1);
		Assertions.assertEquals(list.get(0).getName(), homeName);

		house = houseRepository.findById(house.getId()).orElse(null);
		Assertions.assertEquals(house.getUsers().size(), 2);
		Assertions.assertTrue(house.getUsers().contains(worker1));

		list = houseRepository.findByUsers(worker1.getId());
		Assertions.assertEquals(list.size(), 1);
		Assertions.assertEquals(list.get(0).getName(), homeName);

		final TaskType typeLv = new TaskType();
		typeLv.setName(TASK_LAVE_VAISSELLE);
		typeLv.setValue(1.0);
		taskTypeRepository.save(typeLv);
		house.getTypes().add(typeLv);

		final TaskType typeMt = new TaskType();
		typeMt.setName(TASK_METTRE_LA_TABLE);
		typeMt.setValue(1.0);
		taskTypeRepository.save(typeMt);
		house.getTypes().add(typeMt);

		houseRepository.save(house);

		house = houseRepository.findById(house.getId()).orElse(null);
		Assertions.assertEquals(house.getTypes().size(), 2);

		Work work = buildWork(house, worker1, DateUtils.buildDate(2020, 9, 1), typeLv);
		workRepository.save(work);

		work = buildWork(house, worker1, DateUtils.buildDate(2020, 10, 2), typeMt);
		workRepository.save(work);

		work = buildWork(house, worker1, DateUtils.buildDate(2020, 10, 1), typeLv);
		workRepository.save(work);

		work = buildWork(house, worker2, DateUtils.buildDate(2020, 10, 2), typeMt);
		workRepository.save(work);

		List<Work> works = workRepository.findByUsers(worker1.getId());
		Assertions.assertEquals(3, works.size());

		works = workRepository.findByUsers(worker2.getId());
		Assertions.assertEquals(1, works.size());
	}

	private void olapQuery() {
		DataMartHouse dataMart = DataMartHouse.getDatamart(house);
		dataMart.update(house, workRepository);

		Assertions.assertEquals(2, dataMart.userDimension.list().size()); // 2 worker registerd
		Assertions.assertEquals(2, dataMart.typeDimension.list().size());

		OlapMember typeMbr = dataMart.typeDimension.list().stream()
				.filter((m) -> TASK_LAVE_VAISSELLE.equals(m.getCode())).findFirst().orElse(null);

		Assertions.assertEquals(2, dataMart.scoreMeasure.getValue(dataMart.userDimension.any, typeMbr,
				dataMart.weekDimension.any, dataMart.workIdDimension.any));

		Assertions.assertEquals(1, dataMart.scoreMeasure.getValue(dataMart.userDimension.any, typeMbr,
				dataMart.weekDimension.getMember("2020_40"), dataMart.workIdDimension.any));

		OlapMember typeMt = dataMart.typeDimension.list().stream()
				.filter((m) -> TASK_METTRE_LA_TABLE.equals(m.getCode())).findFirst().orElse(null);
		Assertions.assertEquals(2, dataMart.scoreMeasure.getValue(dataMart.userDimension.any, typeMt,
				dataMart.weekDimension.any, dataMart.workIdDimension.any));
		
	
		Map<String, Double> expectedValue = new HashMap<>();
		expectedValue.put(String.join(";", WORKER1, TASK_LAVE_VAISSELLE, "2020_40"), 1.0);
		dataMart.scoreMeasure.forEachCell((members, value) -> {
			String key = String.join(";", members[0].getCode(), members[1].getCode(), members[2].getCode());
			if (value.equals(expectedValue.get(key))) {
				expectedValue.remove(key);
			}
		}, dataMart.userDimension.any, typeMbr, dataMart.weekDimension.getMember("2020_40"), dataMart.workIdDimension.any);
		
	
		Assertions.assertEquals(0, expectedValue.size());
	}
	
	  

	private User buildChild(String name) {
		User userChild = new User();
		userChild.setLogin(name);
		userChild.setName(name);
		userChild.setPassword(name);
		userChild.setEnabled(true);
		userChild.setRole(RoleAuthority.USER);

		userChild = userRepository.save(userChild);
		house.getUsers().add(userChild);
		houseRepository.save(house);

		// To avoid contrainst violation we reload else hibernate will try to register
		// second time the previous added.
		house = houseRepository.findById(house.getId()).orElse(null);

		return userChild;
	}

	private Work buildWork(House house, User userChild, Date workDay, TaskType taskType) {
		Work work = new Work();
		work.setHouse(house);
		work.setWorker(userChild);
		work.setDate(workDay);
		work.setType(taskType);
		return work;
	}

}
