package com.fooddelivery.Model;


import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface StationDao extends CrudRepository<Station, Long>{
	
	@Query("from Station s where s.stationId = :staID")
	public Station findByID(@Param("staID") Integer staID);
	
}
