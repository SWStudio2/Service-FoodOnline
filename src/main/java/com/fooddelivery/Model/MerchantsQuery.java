package com.fooddelivery.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public class MerchantsQuery {
	private static final Logger logger = LoggerFactory.getLogger(MerchantsQuery.class);
	  
	  public static Merchants[] queryMerChantByID(String mersIdString)
	  {
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&amp;characterEncoding=UTF-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";
			Merchants[] merList = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);
				
				if(connect != null){
					logger.info("Database Connected.");
				} else {
					logger.info("Database Connect Failed.");
				}
				s = connect.createStatement();
							
				String sql = "";
				
				StringBuffer sqlBuffer = new StringBuffer();

				sqlBuffer.append("SELECT MER_ID,MER_LATITUDE,MER_LONGTITUDE,MER_COOKTIME \n ");
				sqlBuffer.append("FROM merchants  \n ");
				sqlBuffer.append("Where mer_id in (" + mersIdString +") \n ");

				
				sql = sqlBuffer.toString();	
				
				ResultSet rec = s.executeQuery(sql);
				
				if(rec == null)
				{
					System.out.print(" record not found ");
				}
//				logger.info("" + sql);
				ArrayList<Merchants> arrMerchant = new ArrayList<Merchants>();
				while((rec!=null) && (rec.next()))
	            {
					Merchants tmpMerchant = new Merchants();
					tmpMerchant.setMerID(rec.getInt("MER_ID"));
					tmpMerchant.setMerLatitude(rec.getString("MER_LATITUDE"));
					tmpMerchant.setMerLongtitude(rec.getString("MER_LONGTITUDE"));
					tmpMerchant.setCookingTime(rec.getInt("MER_COOKTIME"));
					arrMerchant.add(tmpMerchant);
	            }
				
				merList = new Merchants[arrMerchant.size()];
				for(int i = 0;i<arrMerchant.size();i++)
				{
					merList[i] = (Merchants)arrMerchant.get(i);
				}
				// ADD THESE LINES
				rec.close(); rec = null;
				s.close(); s = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Close
			try {
				if(connect != null){
					connect.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				  
		  return merList;
	  }
}
