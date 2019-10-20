package org.housework.server.models;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer>{
	User findByLogin(@Param("login") String login);

}
