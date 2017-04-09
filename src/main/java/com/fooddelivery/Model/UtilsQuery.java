package com.fooddelivery.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class UtilsQuery {

	public static String checkRecallOrder(int orderID)
	{
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&characterEncoding=utf-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";

			String resultRecall = "N";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);
				
				if(connect != null){
					System.out.println("Database Connected.");
				} else {
					System.out.println("Database Connect Failed.");
				}
				s = connect.createStatement();
							
				String sql = "";
				
				StringBuffer sqlBuffer = new StringBuffer();
							  
				sqlBuffer.append("select IF(\n ");
				sqlBuffer.append("(\n ");
				sqlBuffer.append("select count(seqor_id)  \n ");
				sqlBuffer.append("from sequence_orders\n ");
				sqlBuffer.append("where seqor_order_id = "+ orderID+"\n ");
				sqlBuffer.append(") = (\n ");
				sqlBuffer.append("select count(seqor_id) \n ");
				sqlBuffer.append("from sequence_orders\n ");
				sqlBuffer.append("where seqor_order_id = "+ orderID+"\n ");
				sqlBuffer.append("and seqor_cook_status in ('ยืนยันการรับออร์เดอร์','ไม่รับออร์เดอร์')\n ");
				sqlBuffer.append("),'Y','N') As CHK_RECALL\n ");
		
				sql = sqlBuffer.toString();	
				System.out.println(sql);
				ResultSet rec = s.executeQuery(sql);

				if(rec == null)
				{
					System.out.print(" record not found ");
				}

				while((rec!=null) && (rec.next()))
	            {
					resultRecall = rec.getString("CHK_RECALL");
					System.out.println(rec.getString("CHK_RECALL"));
	            }
				
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
		  return resultRecall;
	}	
	
	public static ArrayList getMerchantAndOrderSeqByOrderId(int orderID)
	{
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&characterEncoding=utf-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";

			ArrayList<HashMap<String, Object>> arrResult = new ArrayList<HashMap<String, Object>>();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);
				
				if(connect != null){
					System.out.println("Database Connected.");
				} else {
					System.out.println("Database Connect Failed.");
				}
				s = connect.createStatement();
							
				String sql = "";
				
				StringBuffer sqlBuffer = new StringBuffer();
							  
				sqlBuffer.append("SELECT   \n ");
				sqlBuffer.append("  so.SEQOR_ID,  \n ");
				sqlBuffer.append("  so.SEQOR_MER_ID,  \n ");
				sqlBuffer.append("  so.SEQOR_COOK_STATUS,  \n ");
				sqlBuffer.append("  so.SEQOR_COOK_TIME,  \n ");
				sqlBuffer.append("  m.MER_LATITUDE,  \n ");
				sqlBuffer.append("  m.MER_LONGTITUDE   \n ");
				sqlBuffer.append("FROM  \n ");
				sqlBuffer.append("  sequence_orders so   \n ");
				sqlBuffer.append("  INNER JOIN merchants m   \n ");
				sqlBuffer.append("    ON m.mer_id = so.SEQOR_MER_ID   \n ");
				sqlBuffer.append("WHERE seqor_order_id = " + orderID + "    \n ");
				sqlBuffer.append("  AND seqor_cook_status = 'ยืนยันการรับออร์เดอร์'  \n ");


				
				sql = sqlBuffer.toString();	
				
				ResultSet rec = s.executeQuery(sql);

				if(rec == null)
				{
					System.out.print(" record not found ");
				}
//				System.out.println("" + sql);
				
				while((rec!=null) && (rec.next()))
	            {
					HashMap<String, Object> tmpHash = new HashMap<String, Object>();
					tmpHash.put("SEQOR_ID", rec.getInt("SEQOR_ID"));
					tmpHash.put("SEQOR_MER_ID", rec.getInt("SEQOR_MER_ID"));
					tmpHash.put("SEQOR_COOK_STATUS", rec.getString("SEQOR_COOK_STATUS"));
					tmpHash.put("SEQOR_COOK_TIME", rec.getDouble("SEQOR_COOK_TIME"));
					tmpHash.put("MER_LATITUDE", rec.getString("MER_LATITUDE"));
					tmpHash.put("MER_LONGTITUDE", rec.getString("MER_LONGTITUDE"));
					arrResult.add(tmpHash);
	            }
				
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
		  return arrResult;
	}
	
	public static HashMap<String, String> getLatitudeAndLongtitudeByOrderId(int orderID)
	{
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&characterEncoding=utf-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";

			HashMap<String, String> tmpHash = new HashMap<String, String>();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);
				
				if(connect != null){
					System.out.println("Database Connected.");
				} else {
					System.out.println("Database Connect Failed.");
				}
				s = connect.createStatement();
							
				String sql = "";
				
				StringBuffer sqlBuffer = new StringBuffer();
				  
				sqlBuffer.append("(SELECT    \n ");
				sqlBuffer.append("  ORDER_ADDRESS_LATITUDE,  \n ");
				sqlBuffer.append("  ORDER_ADDRESS_LONGTITUDE   \n ");
				sqlBuffer.append("  FROM  \n ");
				sqlBuffer.append("  orders  \n ");
				sqlBuffer.append("  WHERE order_id = " +orderID +")   \n ");

				sql = sqlBuffer.toString();	
				
				ResultSet rec = s.executeQuery(sql);

				if(rec == null)
				{
					System.out.print(" record not found ");
				}
//				System.out.println("" + sql);
				
				while((rec!=null) && (rec.next()))
	            {
					tmpHash.put("ORDER_ADDRESS_LATITUDE", rec.getString("ORDER_ADDRESS_LATITUDE"));
					tmpHash.put("ORDER_ADDRESS_LONGTITUDE", rec.getString("ORDER_ADDRESS_LONGTITUDE"));
	            }
				
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
		  return tmpHash;
	}	
}
