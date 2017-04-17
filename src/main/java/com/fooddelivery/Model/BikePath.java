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
@Table(name = "bike_path")
public class BikePath {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "bike_path_id")
	private long bike_path_id;
	
	@NotNull
	@Column(name = "bike_path_source_id")
	private long bike_path_source_id;
	
	@NotNull
	@Column(name = "bike_path_destination_id")
	private long bike_path_destination_id;

	@NotNull
	@Column(name = "bike_path_duration")
	private double bike_path_duration;
	
	@NotNull
	@Column(name = "bike_path_distance")
	private double bike_path_distance;
	
	@NotNull
	@Column(name = "bike_path_type")
	private String bike_path_type;
	
	public BikePath() {}
	
	public BikePath(long bike_path_id) {
		this.bike_path_id = bike_path_id;
	}
	
	public BikePath(
			long bike_path_source_id,
			long bike_path_destination_id,
			double bike_path_duration,
			double bike_path_distance,
			String bike_path_type
	) {
		this.bike_path_source_id = bike_path_source_id;
		this.bike_path_destination_id = bike_path_destination_id;
		this.bike_path_duration = bike_path_duration;
		this.bike_path_distance = bike_path_distance;
		this.bike_path_type = bike_path_type;
	}
	

}
