package org.housework.server.models;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WorkRepository  extends CrudRepository<Work, Integer>{
	
	@Query("select w from Work w join w.worker u where u.id = ?1")
	List<Work> findByUsers(int userId);

}
