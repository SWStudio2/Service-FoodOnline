package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface BikePathDao extends CrudRepository<BikePath, Long> {
	final static String statusID = "9,10";
	@Query(value = "SELECT * FROM bike_station WHERE bike_station_id IN "
			+ "(SELECT DISTINCT full_bike_station_now FROM fulltime_messenger "
			+ "WHERE full_status_id IN (" +statusID+"))"
	  		, nativeQuery = true)
	 public List<BikePath> insertToBikePath();
	
	@Modifying
	@Query(value = "insert into bike_path (bike_path_id,bike_path_source_id,bike_path_destination_id,"
			+ "bike_path_duration,bike_path_distance,bike_path_type) "
			+ "values ("
			+ ":bike_path_id, :bike_path_source_id, :bike_path_destination_id,"
			+ ":bike_path_duration, :bike_path_distance, :bike_path_type)"
	  		, nativeQuery = true)
	 public void insertOrder(@Param("bike_path_id") long bike_path_id, 
			 @Param("bike_path_source_id") long bike_path_source_id,
			 @Param("bike_path_destination_id") long bike_path_destination_id, 
			 @Param("bike_path_duration") double bike_path_duration,
			 @Param("bike_path_distance") double bike_path_distance,
			 @Param("bike_path_type") String bike_path_type);	
	 
	
	@Query(value = "SELECT * FROM bike_path WHERE bike_path_source_id = :sID "
			+ "AND bike_path_destination_id = :dID AND bike_path_type = :type"
	  		, nativeQuery = true)
	public BikePath findBikePathFromId(@Param("sID") int sourceId,@Param("dID") int destId , @Param("type") String pathType );
	
	@Query(value = "SELECT * FROM bike_path WHERE bike_path_source_id in :sID "
			+ "AND bike_path_destination_id in :dID AND bike_path_type = :type"
	  		, nativeQuery = true)
	public BikePath[] findBikePathFromIdArray(@Param("sID") int[] sourceIdArray,@Param("dID") int[] destIdArray , @Param("type") String pathType );
}
