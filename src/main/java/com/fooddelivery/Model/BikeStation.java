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
public class BikeStation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "bike_station_id")
	private long bike_station_id;
	
	@NotNull
	@Column(name = "bike_station_name")
	private String bike_station_name;
	
	@NotNull
	@Column(name = "bike_station_latitude")
	private String bike_station_latitude;
	
	@NotNull
	@Column(name = "bike_station_longitude")
	private String bike_station_longitude;
	
	public BikeStation() {}
	
	public BikeStation(long bikeStationId) {
		this.bike_station_id = bikeStationId;
	}
	
	public BikeStation(
			String bikeStationName,
			String bikeStationLatitude,
			String bikeStationLongitude
	) {
		this.bike_station_name = bikeStationName;
		this.bike_station_latitude = bikeStationLatitude;
		this.bike_station_longitude = bikeStationLongitude;
	}
	
	public long getBikeStationId() {
		return bike_station_id;
	}
	
	public void setBikeStationId(long bikeStationId) {
		this.bike_station_id = bikeStationId;
	}
	
	public String getBikeStationName() {
		return bike_station_name;
	}
	
	public void setBikeStationName(String bikeStationName) {
		this.bike_station_name = bikeStationName;
	}
	
	public String getBikeStationLatitude() {
		return bike_station_latitude;
	}
	
	public void setBikeStationLatitude(String bikeStationLatitude) {
		this.bike_station_latitude = bikeStationLatitude;
	}
	
	public String getBikeStationLongitude() {
		return bike_station_longitude;
	}
	
	public void setBikeStationLongitude(String bikeStationLongitude) {
		this.bike_station_longitude = bikeStationLongitude;
	}

}
