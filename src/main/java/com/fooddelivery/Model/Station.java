package com.fooddelivery.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "bike_station")
public class Station {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "bike_station_id")
	private int stationId;
	
	@NotNull
	@Column(name = "bike_station_name")
	private String stationName;
	
	@NotNull
	@Column(name = "bike_station_latitude")
	private String stationLantitude;
	@NotNull
	@Column(name = "bike_station_longitude")
	private String stationLongtitude;
	
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public String getStationLantitude() {
		return stationLantitude;
	}
	public void setStationLantitude(String stationLantitude) {
		this.stationLantitude = stationLantitude;
	}
	public String getStationLongtitude() {
		return stationLongtitude;
	}
	public void setStationLongtitude(String stationLongtitude) {
		this.stationLongtitude = stationLongtitude;
	}


}
