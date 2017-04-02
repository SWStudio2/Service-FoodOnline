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
public interface OrdersDao extends CrudRepository<Orders, Long> {
	
	Logger logger = (Logger) LoggerFactory.getLogger(OrdersDao.class);

	/*
	 * "insert into commit_activity_link (commit_id, activity_id) VALUES (?1, ?2)"*/
	@Modifying
	@Query(value = "insert into orders (order_cus_id, order_address, order_address_latitude,"
			+ "order_address_longtitude, order_datetime, order_datetime_delivery, "
			+ "order_delivery_rate, order_price, order_distance, order_status) values ("
			+ ":order_cus_id, :order_address, :order_address_latitude, :order_address_longtitude, "
			+ ":order_datetime, :order_datetime_delivery, :order_delivery_rate, "
			+ ":order_price, :order_distance, :order_status)"
	  		, nativeQuery = true)
	 public void insertOrder(@Param("order_cus_id") long order_cus_id, 
			 @Param("order_address") String order_address,
			 @Param("order_address_latitude") String order_address_latitude, 
			 @Param("order_address_longtitude") String order_address_longtitude,
			 @Param("order_datetime") String order_datetime,
			 @Param("order_datetime_delivery") String order_datetime_delivery,
			 @Param("order_delivery_rate") int order_delivery_rate, 
			 @Param("order_price") double order_price,
			 @Param("order_distance") double order_distance,
			 @Param("order_status") String order_status);
	 
	
}