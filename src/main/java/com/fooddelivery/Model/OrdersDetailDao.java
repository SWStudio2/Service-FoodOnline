package com.fooddelivery.Model;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fooddelivery.Model.Order.OrderHeaderAndDetail;

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
	 
	
	 
	 @Query(value="select distinct "	  
		      +"o.*,"
		      +"m.menu_id,"
		      +"m.menu_name,"
		      +"m.menu_price,"
		      +"od.order_detail_amount,"
		      +"od.order_remark,"
		       +"IFNULL(sop.sum_option,0) as option_total_price,"
		      +"((m.menu_price * od.order_detail_amount) +  IFNULL(sop.sum_option,0) ) as menu_total_price"
		    +" from order_detail od"
		      +" inner join menu m on m.menu_id = od.menu_id"
		    +" inner join orders o on od.order_id = o.order_id" 
		    +" Left  join ( "
		  		+" select odp.order_detail_id,sum(om.option_price) as sum_option" 
		  		+" from order_detail od"
		  		+" inner join orders_detail_option odp  ON odp.order_detail_id = od.order_detail_id"
		  		+" inner join options_menu om on om.option_id = odp.option_id"
		  		+" where od.order_id = :order_id"
		  		+" group by odp.order_detail_id) sop on sop.order_detail_id = od.order_detail_id"
		    +" where od.order_id = :order_id" , nativeQuery = true)
	 public List<Object[]> findByOrderId(@Param("order_id") Long order_id );
	 
	 
	 @Query(value="select distinct " 	  
          +"m.menu_id,m.menu_name,"
          +"m.menu_price,"
          +"od.order_detail_id,"
          +"od.order_detail_amount,"
          +"od.order_remark, "
          +"((m.menu_price * od.order_detail_amount) +  IFNULL(sop.sum_option,0) ) as menu_total_price"
          +" from order_detail od"
          +" inner join menu m on m.menu_id = od.menu_id"
          +" Left join ( select odp.order_detail_id,sum(om.option_price) as sum_option"
          +" from order_detail od"
  			+" inner join orders_detail_option odp  ON odp.order_detail_id = od.order_detail_id "
  				+" inner join options_menu om on om.option_id = odp.option_id where od.order_id = :order_id"
  				+" group by odp.order_detail_id) sop on sop.order_detail_id = od.order_detail_id where od.order_id = :order_id"
        +" and od.order_id = :order_id and od.MER_ID = :mer_id" , nativeQuery = true)
	 public List<Object[]> findByOrderIdAndMerId(@Param("order_id") Long order_id , @Param("mer_id") Long mer_id);
}
