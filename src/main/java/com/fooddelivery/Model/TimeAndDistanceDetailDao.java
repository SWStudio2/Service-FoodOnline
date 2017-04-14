package com.fooddelivery.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
public class TimeAndDistanceDetailDao {
	private static final Logger logger = LoggerFactory.getLogger(TimeAndDistanceDetailDao.class);

	TimeAndDistanceDetail timeAndDistance;
	final static String statusID = "9,10";
	
	public static TimeAndDistanceDetail[] getTimeAndDistanceDetail(String merchantIdForQuery)
	{
		Connection connect = null;
		Statement s = null;
		String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
		URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&characterEncoding=utf-8";
		String USER = "ba8167e655c97d";
		String PASSWORD = "fcc5664d";
		

		TimeAndDistanceDetail[] detailListReturn = null;

		// Close

		
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

			sqlBuffer.append("(\n ");
			sqlBuffer.append("SELECT \n ");
			sqlBuffer.append("bp.bike_path_source_id,\n ");
			sqlBuffer.append("bp.bike_path_destination_id,\n ");
			sqlBuffer.append("bp.bike_path_duration,\n ");
			sqlBuffer.append("bp.bike_path_distance,\n ");
			sqlBuffer.append("bp.bike_path_type\n ");
			sqlBuffer.append("FROM bike_path bp\n ");
			sqlBuffer.append("WHERE \n ");
			sqlBuffer.append("bike_path_source_id IN ("+ merchantIdForQuery +") \n ");
			sqlBuffer.append("AND bike_path_destination_id IN ("+ merchantIdForQuery+") \n ");
			sqlBuffer.append("AND bike_path_type = 'merchant'\n ");
			sqlBuffer.append(")\n ");
			sqlBuffer.append("UNION\n ");
			sqlBuffer.append("(\n ");
			sqlBuffer.append("SELECT \n ");
			sqlBuffer.append("bp.bike_path_source_id,\n ");
			sqlBuffer.append("bp.bike_path_destination_id,\n ");
			sqlBuffer.append("bp.bike_path_duration,\n ");
			sqlBuffer.append("bp.bike_path_distance,\n ");
			sqlBuffer.append("bp.bike_path_type\n ");
			sqlBuffer.append("FROM bike_path bp\n ");
			sqlBuffer.append("WHERE \n ");
			sqlBuffer.append("bike_path_source_id IN \n ");
			sqlBuffer.append("(\n ");
			sqlBuffer.append("SELECT DISTINCT full_biKe_station_now\n ");
			sqlBuffer.append("  FROM fulltime_messenger \n ");
			sqlBuffer.append("  WHERE full_status_id IN ("+ statusID +")\n ");
			sqlBuffer.append(")  \n ");
			sqlBuffer.append("AND bike_path_destination_id IN ("+ merchantIdForQuery +")\n ");
			sqlBuffer.append("AND  bike_path_type = 'bike'\n ");
			sqlBuffer.append(")\n ");



			
			sql = sqlBuffer.toString();	
			
			ResultSet rec = s.executeQuery(sql);
			
			if(rec == null)
			{
				System.out.print(" record not found ");
			}
			
			ArrayList<TimeAndDistanceDetail> arrTimeDetail = new ArrayList<TimeAndDistanceDetail>();
			while((rec!=null) && (rec.next()))
            {
                TimeAndDistanceDetail tmpDetail = new TimeAndDistanceDetail();
                tmpDetail.setSourceId(rec.getInt("bike_path_source_id"));
                tmpDetail.setDestinationId(rec.getInt("bike_path_destination_id"));
                tmpDetail.setDistance(rec.getString("bike_path_distance"));
                tmpDetail.setDuration(rec.getString("bike_path_duration"));
                tmpDetail.setPathType(rec.getString("bike_path_type"));
                arrTimeDetail.add(tmpDetail);
            }
			
			detailListReturn = new TimeAndDistanceDetail[arrTimeDetail.size()];
			for(int i = 0;i<arrTimeDetail.size();i++)
			{
				detailListReturn[i] = (TimeAndDistanceDetail)arrTimeDetail.get(i);
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
		
		return detailListReturn;
	}	
}
