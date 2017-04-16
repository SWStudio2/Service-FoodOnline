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
	private Long bike_path_id;
	
	@NotNull
	@Column(name = "bike_path_source_id")
	private Integer bike_path_source_id;
	
	@NotNull
	@Column(name = "bike_path_destination_id")
	private Integer bike_path_destination_id;
	
	@NotNull
	@Column(name = "bike_path_duration")
	private Double bike_path_duration;
	
	@NotNull
	@Column(name = "bike_path_distance")
	private Double bike_path_distance;
	
	@NotNull
	@Column(name = "bike_path_type")
	private String bike_path_type;
	
	public BikePath() {}

	
	public BikePath(long bike_path_id) {
		this.bike_path_id = bike_path_id;
	}
	
	public BikePath(
			Integer bike_path_source_id,
			Integer bike_path_destination_id,
			Double bike_path_duration,
			Double bike_path_distance,
			String bike_path_type
	) {
		this.bike_path_source_id = bike_path_source_id;
		this.bike_path_destination_id = bike_path_destination_id;
		this.bike_path_duration = bike_path_duration;
		this.bike_path_distance = bike_path_distance;
		this.bike_path_type = bike_path_type;
	}


	public Long getBike_path_id() {
		return bike_path_id;
	}

	public void setBike_path_id(Long bike_path_id) {
		this.bike_path_id = bike_path_id;
	}

	public int getBike_path_source_id() {
		return bike_path_source_id;
	}

	public void setBike_path_source_id(int bike_path_source_id) {
		this.bike_path_source_id = bike_path_source_id;
	}

	public int getBike_path_destination_id() {
		return bike_path_destination_id;
	}

	public void setBike_path_destination_id(int bike_path_destination_id) {
		this.bike_path_destination_id = bike_path_destination_id;
	}

	public double getBike_path_duration() {
		return bike_path_duration;
	}

	public void setBike_path_duration(double bike_path_duration) {
		this.bike_path_duration = bike_path_duration;
	}

	public double getBike_path_distance() {
		return bike_path_distance;
	}

	public void setBike_path_distance(double bike_path_distance) {
		this.bike_path_distance = bike_path_distance;
	}

	public String getBike_path_type() {
		return bike_path_type;
	}

	public void setBike_path_type(String bike_path_type) {
		this.bike_path_type = bike_path_type;
	}
	
}
