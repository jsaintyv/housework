package org.housework.server.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HouseRepository extends CrudRepository<House, Integer>{

}
