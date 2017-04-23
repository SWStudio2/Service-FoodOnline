package com.fooddelivery.Model;

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
	
	 @Modifying
	 @Query(value="UPDATE fulltime_messenger" +
	"SET FULL_STATUS_ID = :status_id"
	+ "WHERE FULL_ID = :full_id", 
	nativeQuery = true)
	 public void updateFullTimeStatus(@Param("full_id") long full_id,
			 @Param("status_id") int status_id); 	

}
