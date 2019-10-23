package org.housework.server.models;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface HouseRepository extends CrudRepository<House, Integer>{

	@Query("select h from House h where h.owner = ?1")
	List<House> findByOwner(User user);
	
	@Query("select h from House h join h.users u where u.id = ?1")
	List<House> findByUsers(int userId);
}
