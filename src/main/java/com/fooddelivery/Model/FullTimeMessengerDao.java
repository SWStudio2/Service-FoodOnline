package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface FullTimeMessengerDao extends CrudRepository<FullTimeMessenger, Long> {
	
	@Query(value = "select distinct(full_bike_station_now) from fulltime_messenger", nativeQuery = true)
	public List<Integer> getAllStation();
	
}
