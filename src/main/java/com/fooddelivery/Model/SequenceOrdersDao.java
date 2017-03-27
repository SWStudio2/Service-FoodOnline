package com.fooddelivery.Model;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import ch.qos.logback.classic.Logger;

@Transactional
public interface SequenceOrdersDao extends CrudRepository<SequenceOrders, Long> {
	
	Logger logger = (Logger) LoggerFactory.getLogger(SequenceOrdersDao.class);

	/*
	 * "insert into commit_activity_link (commit_id, activity_id) VALUES (?1, ?2)"*/
	@Modifying
	@Query(value = "insert into sequence_orders (seqor_mer_id, seqor_sort, "
			+ "seqor_receive_status, seqor_mess_id, seqor_type_mess,"
			+ "seqor_order_id, seqor_cook_status) values ("
			+ ":seqor_mer_id, :seqor_sort, "
			+ ":seqor_receive_status, :seqor_mess_id, :seqor_type_mess,"
			+ ":seqor_order_id, :seqor_cook_status)"
	  		, nativeQuery = true)
	 public void insertOrder(
			 @Param("seqor_mer_id") String seqor_mer_id,
			 @Param("seqor_sort") String seqor_sort, 
			 @Param("seqor_receive_status") String seqor_receive_status,
			 @Param("seqor_mess_id") String seqor_mess_id,
			 @Param("seqor_type_mess") String seqor_type_mess,
			 @Param("seqor_order_id") String seqor_order_id,
			 @Param("seqor_cook_status") String seqor_cook_status);
	 
	
}
