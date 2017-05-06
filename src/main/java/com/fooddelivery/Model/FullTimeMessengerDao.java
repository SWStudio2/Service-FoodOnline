package com.fooddelivery.Model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface FullTimeMessengerDao extends CrudRepository<FullTimeMessenger, Long> {

	@Query(value = "select distinct(full_bike_station_now) from fulltime_messenger", nativeQuery = true)
	public List<Integer> getAllStation();

	@Query(value = "select * "
			+ "from fulltime_messenger "
			+ "where full_email = :email and full_password = :pass"
			, nativeQuery = true)
	public List<FullTimeMessenger> findByFullEmail(@Param("email") String email , @Param("pass") String pass);

	@Query(value = "select * from fulltime_messenger "
			+ "where full_id = :full_id"
			, nativeQuery = true)
	public FullTimeMessenger findById(@Param("full_id") long fullId);

	@Query(value = "select * from fulltime_messenger "
			, nativeQuery = true)
	public List<FullTimeMessenger> findAll();

	//SQL B4
	@Modifying
	@Query(value="UPDATE fulltime_messenger" +
			" SET FULL_STATUS_ID = :status_id"
			+ " WHERE FULL_ID = :full_id",
			nativeQuery = true)
	public void updateFullTimeStatus(@Param("full_id") int full_id,
									 @Param("status_id") int status_id);

	@Query(value = "select FULL_BIKE_STATION_NOW, count(full_id) as count_fulltime "
			+ " from fulltime_messenger "
			+ " where FULL_STATUS_ID in (9,10) "
			+ " group by FULL_BIKE_STATION_NOW"
			, nativeQuery = true)
	public List<Object[]> getNumberOfMessengerInStation();


	@Query(value = "select * from fulltime_messenger where full_id = :fullId"
			, nativeQuery = true)
	public FullTimeMessenger getFullTimeByFullId(@Param("fullId") long fullId);

	@Query(value = "select f.FULL_ID from fulltime_messenger f"
			+ "inner join bike_station b "
			+ "on f.full_bike_station_now = b.bike_station_id "
			+ "WHERE full_status_id IN ( 9, 10 ) and b.bike_station_id = :stationId"
			, nativeQuery = true)
	public ArrayList<Integer> getFulltimeMessengerFreeByStationID(@Param("stationId") long stationId);

}
