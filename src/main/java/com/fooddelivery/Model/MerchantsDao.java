package com.fooddelivery.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface MerchantsDao extends CrudRepository<Merchants, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
//	  @Query("select m.mer_name from Merchants m where m.mer_id = :merId")
//	  public String findNameByMerchantId(@Param("mer_id") String merId);
		
	
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
	  public List<Merchants> findMerchantsByStatus(@Param("date") String date, @Param("time") String time);
	  
	  
	  @Query(value = "SELECT * FROM merchants "
		  		+ "WHERE mer_open_status = 'open' "
		  		+ "and mer_id not in ( SELECT mer_id FROM holidays_merchant WHERE HOL_DATE = CURDATE() ) "
		  		+ "and mer_id in ( select mer_id from working_day_merchant where work_day = DAYNAME(:date) "
		  		+ "and work_open_time < :time "
		  		+ "and work_close_time > :time "
		  		+ "and work_day_status = 'open' )"
		  		+ "and mer_name like concat('%',:mername,'%') " 
		  		, nativeQuery = true)
	  public List<Merchants> findMerchantsByMerName(@Param("mername") String mername , @Param("date") String date, @Param("time") String time);
	  

	  
//	  public static Merchants[] queryMerChantByID(String[] merId)
//	  {
//		  Connection connect = null;
//			Statement s = null;
//			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
//			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&amp;characterEncoding=UTF-8";
//			String USER = "ba8167e655c97d";
//			String PASSWORD = "fcc5664d";
//			
//			String merchantIdForQuery = "";
//			Merchants[] merList = null;
//			for(int i = 0;i<merId.length;i++)
//			{
//				if(i == 0)
//				{
//					merchantIdForQuery += merId[i];
//				}
//				else
//				{
//					merchantIdForQuery += "," + merId[i];
//				}
//			}
//			
//
//
//			// Close
//
//			
//			try {
//				Class.forName("com.mysql.jdbc.Driver");
//				connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);
//				
//				if(connect != null){
//					System.out.println("Database Connected.");
//				} else {
//					System.out.println("Database Connect Failed.");
//				}
//				s = connect.createStatement();
//							
//				String sql = "";
//				
//				StringBuffer sqlBuffer = new StringBuffer();
//
//				sqlBuffer.append("SELECT MER_ID,MER_LATITUDE,MER_LONGTITUDE,MER_COOKTIME \n ");
//				sqlBuffer.append("FROM merchants  \n ");
//				sqlBuffer.append("Where mer_id in (merchantList) \n ");
//
//				
//				sql = sqlBuffer.toString();	
//				
//				ResultSet rec = s.executeQuery(sql);
//				
//				if(rec == null)
//				{
//					System.out.print(" record not found ");
//				}
////				System.out.println("" + sql);
//				ArrayList<Merchants> arrMerchant = new ArrayList<Merchants>();
//				while((rec!=null) && (rec.next()))
//	            {
//					Merchants tmpMerchant = new Merchants();
//					tmpMerchant.setMerID(Long.parseLong(rec.getString("MER_ID")));
//					tmpMerchant.setMerLatitude(rec.getString("MER_LATITUDE"));
//					tmpMerchant.setMerLongtitude(rec.getString("MER_LONGTITUDE"));
//					tmpMerchant.setCookingTime(rec.getInt("MER_COOKTIME"));
//					arrMerchant.add(tmpMerchant);
//	            }
//				
//				merList = new Merchants[arrMerchant.size()];
//				for(int i = 0;i<arrMerchant.size();i++)
//				{
//					merList[i] = (Merchants)arrMerchant.get(i);
//				}
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			// Close
//			try {
//				if(connect != null){
//					connect.close();
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}				  
//		  return merList;
//	  }
}
