package org.housework.server.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WorkRepository  extends CrudRepository<Work, Integer>{
	
	@Query("select w from Work w join w.worker u where u.id = ?1")
	List<Work> findByUsers(int userId);

	@Query("select w from Work w where ?1 <= w.date and w.date < ?2 and w.house = ?3")
	List<Work> findBy(Date start, Date end, House  house);
	
	@Query("select w from Work w where w.house = ?1")
	List<Work> findBy(House  house);
}
