package com.fooddelivery.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StationQuery {
	private static final Logger logger = LoggerFactory.getLogger(StationQuery.class);
	final static String statusID = "9,10";
	public static Station[] getStationAvailable()
	{
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&amp;characterEncoding=UTF-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";
			Station[] stationList = null;
			
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
							  
				sqlBuffer.append("SELECT bike_station_id,bike_station_latitude,bike_station_longitude  \n ");
				sqlBuffer.append("FROM bike_station WHERE  bike_station_id IN (SELECT DISTINCT full_biKe_station_now  \n ");
				sqlBuffer.append("FROM fulltime_messenger  \n ");
				sqlBuffer.append("WHERE full_status_id IN (" + statusID + "))  \n ");
				
				sql = sqlBuffer.toString();	
				
				ResultSet rec = s.executeQuery(sql);
				
				if(rec == null)
				{
					System.out.print(" record not found ");
				}
//				logger.info("" + sql);
				ArrayList<Station> arrStation = new ArrayList<Station>();
				while((rec!=null) && (rec.next()))
	            {
					Station tmpStation = new Station();
					tmpStation.setStationId(rec.getInt("bike_station_id"));
					tmpStation.setStationLantitude(rec.getString("bike_station_latitude"));
					tmpStation.setStationLongtitude(rec.getString("bike_station_longitude"));
					arrStation.add(tmpStation);
	            }
				
				stationList = new Station[arrStation.size()];
				for(int i = 0;i<arrStation.size();i++)
				{
					stationList[i] = (Station)arrStation.get(i);
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
		  return stationList;
	}
}
