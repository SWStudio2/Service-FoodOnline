package com.fooddelivery.Model;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;


import ch.qos.logback.classic.Logger;

@Transactional
public interface OrdersDetailOptionDao extends CrudRepository<OrdersDetailOption, Long> {
	
	Logger logger = (Logger) LoggerFactory.getLogger(OrdersDetailOptionDao.class);

	/*
	 * "insert into commit_activity_link (commit_id, activity_id) VALUES (?1, ?2)"*/
	/*@Modifying
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
			 @Param("mer_id") String mer_id);*/
	 
	
}
