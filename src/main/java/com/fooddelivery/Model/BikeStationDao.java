package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface BikeStationDao extends CrudRepository<BikeStation, Long> {
	final static String statusID = "9,10";
	@Query(value = "SELECT * FROM bike_station WHERE bike_station_id IN "
			+ "(SELECT DISTINCT full_bike_station_now FROM fulltime_messenger "
			+ "WHERE full_status_id IN (" +statusID+"))"
	  		, nativeQuery = true)
	 public List<BikeStation> getBikeStationAvailable();
	
	
	@Query(value = "SELECT * FROM bike_station "
	  		, nativeQuery = true)
	 public List<BikeStation> getBikeStationAll();	
	 
}
