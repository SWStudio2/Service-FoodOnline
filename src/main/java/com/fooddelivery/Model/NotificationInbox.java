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
@Table(name = "notification_inbox")
public class NotificationInbox {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "noti_id")
	private int noti_id;
	
	@NotNull
	@Column(name = "noti_ref_id")
	private int noti_ref_id;
	
	@NotNull
	@Column(name = "noti_type")
	private String noti_type;

	@NotNull
	@Column(name = "noti_message_type")
	private String noti_message_type;

	@NotNull
	@Column(name = "noti_message_detail")
	private String noti_message_detail;

	@NotNull
	@Column(name = "noti_read_flag")
	private int noti_read_flag;

	
	public NotificationInbox() {}
	
	public NotificationInbox(
			int noti_id,
			int noti_ref_id,
			String noti_type,
			String noti_message_type,
			String noti_message_detail,
			int noti_read_flag
	) {
		this.noti_id = noti_id;
		this.noti_ref_id = noti_ref_id;
		this.noti_type = noti_type;
		this.noti_message_type = noti_message_type;
		this.noti_message_detail = noti_message_detail;
		this.noti_read_flag = noti_read_flag;
	}

	public int getNoti_id() {
		return noti_id;
	}

	public void setNoti_id(int noti_id) {
		this.noti_id = noti_id;
	}

	public int getNoti_ref_id() {
		return noti_ref_id;
	}

	public void setNoti_ref_id(int noti_ref_id) {
		this.noti_ref_id = noti_ref_id;
	}

	public String getNoti_type() {
		return noti_type;
	}

	public void setNoti_type(String noti_type) {
		this.noti_type = noti_type;
	}

	public String getNoti_message_type() {
		return noti_message_type;
	}

	public void setNoti_message_type(String noti_message_type) {
		this.noti_message_type = noti_message_type;
	}

	public String getNoti_message_detail() {
		return noti_message_detail;
	}

	public void setNoti_message_detail(String noti_message_detail) {
		this.noti_message_detail = noti_message_detail;
	}

	public int getNoti_read_flag() {
		return noti_read_flag;
	}

	public void setNoti_read_flag(int noti_read_flag) {
		this.noti_read_flag = noti_read_flag;
	}


	
}
