package com.fooddelivery.Model;
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
	TimeAndDistanceDetail timeAndDistance;
	
	public static TimeAndDistanceDetail[] getTimeAndDistanceDetail(String[] merchantIdList)
	{
		Connection connect = null;
		Statement s = null;
		String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
		URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&amp;characterEncoding=UTF-8";
		String USER = "ba8167e655c97d";
		String PASSWORD = "fcc5664d";
		
		String merchantIdForQuery = "";
		TimeAndDistanceDetail[] detailListReturn = null;
		for(int i = 0;i<merchantIdList.length;i++)
		{
			if(i == 0)
			{
				merchantIdForQuery += merchantIdList[i];
			}
			else
			{
				merchantIdForQuery += "," + merchantIdList[i];
			}
		}
		


		// Close

		
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

			sqlBuffer.append("(\n ");
			sqlBuffer.append("select \n ");
			sqlBuffer.append("bp.bike_path_source_id,\n ");
			sqlBuffer.append("bp.bike_path_destination_id,\n ");
			sqlBuffer.append("bp.bike_path_duration,\n ");
			sqlBuffer.append("bp.bike_path_distance,\n ");
			sqlBuffer.append("bp.bike_path_type\n ");
			sqlBuffer.append("from bike_path bp\n ");
			sqlBuffer.append("where \n ");
			sqlBuffer.append("bike_path_source_id in (" + merchantIdForQuery + ") \n ");
			sqlBuffer.append("and bike_path_destination_id in (" + merchantIdForQuery + ") \n ");
			sqlBuffer.append("and bike_path_type = 'merchant'\n ");
			sqlBuffer.append(")\n ");
			sqlBuffer.append("union\n ");
			sqlBuffer.append("(\n ");
			sqlBuffer.append("select \n ");
			sqlBuffer.append("bp.bike_path_source_id,\n ");
			sqlBuffer.append("bp.bike_path_destination_id,\n ");
			sqlBuffer.append("bp.bike_path_duration,\n ");
			sqlBuffer.append("bp.bike_path_distance,\n ");
			sqlBuffer.append("bp.bike_path_type\n ");
			sqlBuffer.append("from bike_path bp\n ");
			sqlBuffer.append("where \n ");
			sqlBuffer.append("bike_path_source_id in \n ");
			sqlBuffer.append("(\n ");
			sqlBuffer.append("select distinct full_biKe_station_now\n ");
			sqlBuffer.append("  from fulltime_messenger \n ");
			sqlBuffer.append("  where full_status in ('stand by','back to station')\n ");
			sqlBuffer.append(")  \n ");
			sqlBuffer.append("and bike_path_destination_id in (" + merchantIdForQuery + ")\n ");
			sqlBuffer.append("and  bike_path_type = 'bike'\n ");
			sqlBuffer.append(")\n ");
			sqlBuffer.append("\n ");


			
			sql = sqlBuffer.toString();	
			
			ResultSet rec = s.executeQuery(sql);
			
			if(rec == null)
			{
				System.out.print(" record not found ");
			}
			System.out.println("" + sql);
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
