package com.fooddelivery.Model;

public class TimeAndDistanceDetail {
	
	private int sourceId;
	private int destinationId;
	private String distance;
	private String duration;
	private String pathType;
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public int getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getPathType() {
		return pathType;
	}
	public void setPathType(String pathType) {
		this.pathType = pathType;
	}
	
	public static String[] getTimeAndDuration(TimeAndDistanceDetail[] tmpDetail,int source,int destination,String pathType)
	{
		String[] valueList = new String[2];
		for(int i = 0;i<tmpDetail.length;i++)
		{
			if(tmpDetail[i].getSourceId() == source && tmpDetail[i].getDestinationId() == destination && tmpDetail[i].getPathType().equals(pathType))
			{
				valueList[0] = tmpDetail[i].getDuration();
				valueList[1] = tmpDetail[i].getDistance();
			}
		}
		return valueList;
	}
}
