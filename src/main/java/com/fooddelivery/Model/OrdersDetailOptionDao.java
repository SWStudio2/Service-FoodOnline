package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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
	 
//	@Query(nativeQuery=true, 
//			value="select * "
//	 		+ "from orders_detail_option odopt "
//	 		+ "where odopt.order_detail_id=:od_id")
//	public List<OrdersDetailOption> findByOrderId(@Param("od_id") Long od_id);
	

	@Query(value="SELECT distinct odp.order_detail_id,om.option_name,om.option_price"
         +" FROM orders_detail_option odp "
         +" INNER JOIN options_menu om ON om.option_id = odp.option_id"
         +" WHERE odp.order_detail_id = :order_detail_id" , nativeQuery = true)
	 public List<Object[]> findByOrderId(@Param("order_detail_id") Long order_detail_id);
	
}
