package com.fooddelivery.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "fulltime_messenger")
public class FullTimeMessenger {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "full_id")
	private long full_id;

	@NotNull
	@Column(name = "full_name")
	private String full_name;

	@NotNull
	@Column(name = "full_contact_number")
	private String full_contact_number;

	@NotNull
	@Column(name = "full_email")
	private String full_email;

	@NotNull
	@Column(name = "full_password")
	private String full_password;

	@NotNull
	@Column(name = "full_status_id")
	private int full_status_id;

	@Column(name = "full_lastest_lattitude")
	private String full_lastest_lattitude;

	@Column(name = "full_lastest_longtitude")
	private String full_lastest_longtitude;

	@Column(name = "full_recommend_lattitude")
	private String full_recommend_lattitude;

	@Column(name = "full_recommend_longtitude")
	private String full_recommend_longtitude;

	@Column(name = "full_order_id")
	private String full_order_id;

	@Column(name = "full_bike_station_now")
	private String full_bike_station_now;

	//--------------RELATION----------------
	@ManyToOne
	@JoinColumn(name = "full_order_id", referencedColumnName="order_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private Orders order;

	@ManyToOne
	@JoinColumn(name = "full_status_id", referencedColumnName="status_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private StatusConfig statusConfig;

	@ManyToOne
	@JoinColumn(name = "full_bike_station_now", referencedColumnName="bike_station_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private BikeStation bikeStation;



//	@OneToMany(mappedBy="fullTimeMessenger")
//	private List<SequenceOrders> sequenceOrders;

	public FullTimeMessenger() { }

	public FullTimeMessenger(long fullId) {
		this.full_id = fullId;
	}

	public FullTimeMessenger
			(
					String fullName,
					String fullContactNumber,
					String fullEmail,
					String fullPassword,
					int fullStatus,
					String fullLastestLattitude,
					String fullLastestLongtitude,
					String fullRecommendLattitude,
					String fullRecommendLongtitude
			) {
		this.full_name = fullName;
		this.full_contact_number = fullContactNumber;
		this.full_email = fullEmail;
		this.full_password = fullPassword;
		this.full_status_id = fullStatus;
		this.full_lastest_lattitude = fullLastestLattitude;
		this.full_lastest_longtitude = fullLastestLongtitude;
		this.full_recommend_lattitude = fullRecommendLattitude;
		this.full_recommend_longtitude = fullRecommendLongtitude;

	}

	public long getFullId() {
		return full_id;
	}

	public void setFullId(long fullId) {
		this.full_id = fullId;
	}

	public String getFullName() {
		return full_name;
	}

	public void setFullName(String fullName) {
		this.full_name = fullName;
	}

	public String getFullContactNumber() {
		return full_contact_number;
	}

	public void setFullContactNumber(String fullContactNumber) {
		this.full_contact_number = fullContactNumber;
	}

	public String getFullEmail() {
		return full_email;
	}

	public void setFullEmail(String fullEmail) {
		this.full_email = fullEmail;
	}

	public String getFullPassword() {
		return full_password;
	}

	public void setFullPassword(String fullPassword) {
		this.full_password = fullPassword;
	}

	public int getFullStatusId() {
		return full_status_id;
	}

	public void setFullStatusId(int fullStatus) {
		this.full_status_id = fullStatus;
	}

	public String getFullLatestLattitude() {
		return full_lastest_lattitude;
	}

	public void setFullLastestLattitude(String fullLastestLongtitude) {
		this.full_lastest_lattitude = fullLastestLongtitude;
	}

	public String getFullLatestLongtitude() {
		return full_lastest_longtitude;
	}

	public void setFullLastestLongtitude(String fullLastestLongtitude) {
		this.full_lastest_longtitude = fullLastestLongtitude;
	}

	public String getFullRecommendLattitude() {
		return full_recommend_lattitude;
	}

	public void setFullRecommendLattitude(String fullRecommendLongtitude) {
		this.full_recommend_lattitude = fullRecommendLongtitude;
	}

	public String getFullRecommendLongtitude() {
		return full_recommend_longtitude;
	}

	public void setFullRecommendLongtitude(String fullRecommendLongtitude) {
		this.full_recommend_longtitude = fullRecommendLongtitude;
	}


	public String getFullOrderId() {
		return full_order_id;
	}

	public void setFullOrderId(String fullOrderId) {
		this.full_order_id = fullOrderId;
	}

	public String getFullBikeStationNow() {
		return full_bike_station_now;
	}

	public void setFull_bike_station_now(String fullBikeStationNow) {
		this.full_bike_station_now = fullBikeStationNow;
	}

	public BikeStation getBikeStation() {
		return bikeStation;
	}

	public void setBikeStation(BikeStation bikeStation) {
		this.bikeStation = bikeStation;
	}


	//--------------RELATION----------------
//	public List<SequenceOrders> getSequenceOrders() {
//		return sequenceOrders;
//	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public StatusConfig getStatusConfig() {
		return statusConfig;
	}

	public void setStatusConfig(StatusConfig statusConfig) {
		this.statusConfig = statusConfig;
	}


}
