package com.fooddelivery.Model;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fooddelivery.Model.Order.OrderMerchant;

@Transactional
public interface MerchantsDao extends CrudRepository<Merchants, Long> {

	//	SELECT * 
	//	FROM merchants 
	//	WHERE 
	//	mer_open_status = 'open' 
	//		and mer_id not in (
	//			SELECT mer_id 
	//			FROM holidays_merchant 
	//			WHERE HOL_DATE = CURDATE()
	//		) 
	//		and mer_id in (
	//			select mer_id from working_day_merchant 
	//			where work_day = DAYNAME('$date')
	//			and work_open_time < '$time'
	//			and work_close_time > '$time'
	//			and work_day_status = 'open’
	//		)
	//		and mer_name like '%merName%'

	  @Query(value = "SELECT * FROM merchants "
	  		+ "WHERE mer_open_status = 'open' "
	  		+ "and mer_id not in ( SELECT mer_id FROM holidays_merchant WHERE HOL_DATE = CURDATE() ) "
	  		+ "and mer_id in ( select mer_id from working_day_merchant where work_day = DAYNAME(:date) "
	  		+ "and work_open_time < :time "
	  		+ "and work_close_time > :time "
	  		+ "and work_day_status = 'open' )" 
	  		, nativeQuery = true)
	  public List<Merchants> findByStatus(@Param("date") String date, @Param("time") String time);
	  
	  
	  @Query(value = "SELECT * FROM merchants "
		  		+ "WHERE mer_open_status = 'open' "
		  		+ "and mer_id not in ( SELECT mer_id FROM holidays_merchant WHERE HOL_DATE = CURDATE() ) "
		  		+ "and mer_id in ( select mer_id from working_day_merchant where work_day = DAYNAME(:date) "
		  		+ "and work_open_time < :time "
		  		+ "and work_close_time > :time "
		  		+ "and work_day_status = 'open' )"
		  		+ "and mer_name like concat('%',:mername,'%') " 
		  		, nativeQuery = true)
	  public List<Merchants> findByMerName(@Param("mername") String mername , @Param("date") String date, @Param("time") String time);
	  
	  @Query(value = "select * from merchants" , nativeQuery = true )
	  public List<Merchants> findAllMerchants();
	  
	  @Query(value = "select * from merchants where mer_id in ( :merIds )" , nativeQuery = true )
	  public List<Merchants> getMerchantsByMerIds(@Param("merIds") List<Integer> mersIds);
	  

//	 @Query(nativeQuery=true, 
//			value="select m.* "
//	 		+ "from merchants m "
//	 		+ "inner join order_detail od on m.mer_id=od.mer_id "
//	 		+ "where od.order_id=:order_id")
//	 public List<Merchants> findByOrderId(@Param("order_id") Long order_id);
	 
	  
//	    SELECT m.MER_NAME,
//	      m.MER_LATITUDE,
//	      m.MER_LONGTITUDE,
//	      so.SEQOR_MER_DISTANCE,
//	      (so.SEQOR_MER_DISTANCE * (
//	       SELECT ORDER_DELIVERY_RATE FROM orders WHERE ORDER_ID = orderID)
//	      ) AS DELIVERY_PRICE,
//	        sm.FOOD_PRICE
//	    FROM sequence_orders so
//	    INNER JOIN merchants m ON m.MER_ID = so.SEQOR_MER_ID
//	    INNER JOIN (
//	      SELECT od.MER_ID,
//	        SUM((m.menu_price * od.order_detail_amount) +   IFNULL(sop.sum_option,0)) AS FOOD_PRICE
//	         FROM order_detail od
//	         INNER JOIN menu m ON m.MENU_ID = od.MENU_ID
//	        LEFT JOIN ( 
//	          SELECT odp.order_detail_id,SUM(om.option_price) AS sum_option 
//	           FROM order_detail od
//	           INNER JOIN orders_detail_option odp  ON odp.order_detail_id = od.order_detail_id
//	           INNER JOIN options_menu om ON om.option_id = odp.option_id
//	           WHERE od.order_id = orderID
//	           GROUP BY odp.order_detail_id) sop ON sop.order_detail_id = od.order_detail_id
//	         
//	         WHERE od.ORDER_ID = orderID
//	         GROUP BY od.MER_ID
//	           ) sm ON sm.MER_ID = so.SEQOR_MER_ID
//	    WHERE SEQOR_ORDER_ID = orderID
	 
	 @Query(value = "SELECT m.MER_NAME,"
	      +"m.MER_LATITUDE,"
	      +"m.MER_LONGTITUDE,"
	      +"so.SEQOR_MER_DISTANCE,"
	      +"(so.SEQOR_MER_DISTANCE * ("
	       +"SELECT ORDER_DELIVERY_RATE FROM orders WHERE ORDER_ID = :order_id)"
	       +") AS DELIVERY_PRICE,"
	        +"sm.FOOD_PRICE"
	    +" FROM sequence_orders so"
	    +" INNER JOIN merchants m ON m.MER_ID = so.SEQOR_MER_ID"
	    +" INNER JOIN ("
	      +"SELECT od.MER_ID,"
	        +"SUM((m.menu_price * od.order_detail_amount) +   IFNULL(sop.sum_option,0)) AS FOOD_PRICE"
	         +" FROM order_detail od"
	         +" INNER JOIN menu m ON m.MENU_ID = od.MENU_ID"
	        +" LEFT JOIN ( "
	          +"SELECT odp.order_detail_id,SUM(om.option_price) AS sum_option "
	           +" FROM order_detail od"
	           +" INNER JOIN orders_detail_option odp  ON odp.order_detail_id = od.order_detail_id"
	           +" INNER JOIN options_menu om ON om.option_id = odp.option_id"
	           +" WHERE od.order_id = :order_id"
	           +" GROUP BY odp.order_detail_id) sop ON sop.order_detail_id = od.order_detail_id"
	         +" WHERE od.ORDER_ID = :order_id"
	         +" GROUP BY od.MER_ID"
	         +") sm ON sm.MER_ID = so.SEQOR_MER_ID"
	    +" WHERE SEQOR_ORDER_ID = :order_id" , nativeQuery = true )
	 public List<Merchants> findByOrderId(@Param("order_id") Long order_id);
	 
	 
	@Query(value = "SELECT m.MER_NAME,"
      +"m.MER_LATITUDE,"
      +"m.MER_LONGTITUDE,"
      +"so.SEQOR_MER_DISTANCE,"
      +"(so.SEQOR_MER_DISTANCE * ("
      +" SELECT ORDER_DELIVERY_RATE FROM orders WHERE ORDER_ID = orderID)"
      +") AS DELIVERY_PRICE,"
      +"  sm.FOOD_PRICE"
    +"FROM sequence_orders so"
    +"INNER JOIN merchants m ON m.MER_ID = so.SEQOR_MER_ID"
    +"INNER JOIN ("
      +"SELECT od.MER_ID,"
        +"SUM((m.menu_price * od.order_detail_amount) +   IFNULL(sop.sum_option,0)) AS FOOD_PRICE"
         +"FROM order_detail od"
         +"INNER JOIN menu m ON m.MENU_ID = od.MENU_ID"
        +"LEFT JOIN ( "
         +" SELECT odp.order_detail_id,SUM(om.option_price) AS sum_option "
          +" FROM order_detail od"
          +" INNER JOIN orders_detail_option odp  ON odp.order_detail_id = od.order_detail_id"
          +" INNER JOIN options_menu om ON om.option_id = odp.option_id"
          +" WHERE od.order_id = orderID"
          +" GROUP BY odp.order_detail_id) sop ON sop.order_detail_id = od.order_detail_id"
         
         +"WHERE od.ORDER_ID = orderID"
         +"GROUP BY od.MER_ID"
         +" ) sm ON sm.MER_ID = so.SEQOR_MER_ID"
    +"WHERE SEQOR_ORDER_ID = orderID" , nativeQuery = true )
	public List<Object[]> findSpecialByOrderId(@Param("order_id") Long order_id);
}
