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
public interface OrdersDetailDao extends CrudRepository<OrderDetail, Long> {
	
	Logger logger = (Logger) LoggerFactory.getLogger(OrdersDetailDao.class);

	/*
	 * "insert into commit_activity_link (commit_id, activity_id) VALUES (?1, ?2)"*/
	@Modifying
	@Query(value = "insert into order_detail (order_id, order_detail_amount, "
			+ "order_remark, menu_id, mer_id) values ("
			+ ":order_id, :order_detail_amount, "
			+ ":order_remark, :menu_id, :mer_id)"
	  		, nativeQuery = true)
	 public void insertOrder(
			 @Param("order_id") String order_id,
			 @Param("order_detail_amount") String order_detail_amount, 
			 @Param("order_remark") String order_remark,
			 @Param("menu_id") String menu_id,
			 @Param("mer_id") String mer_id);
	 
	
}
