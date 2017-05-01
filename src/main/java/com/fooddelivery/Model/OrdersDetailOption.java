package com.fooddelivery.Model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "orders_detail_option")
public class OrdersDetailOption {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "odo_id")
	private long odo_id;

	@NotNull
	@Column(name = "order_detail_id")
	private long order_detail_id;

	@NotNull
	@Column(name = "option_id")
	private long option_id;


	@ManyToOne
	@JoinColumn(name = "option_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private OptionsMenu optionsMenu;

	public OrdersDetailOption() {}

	public OrdersDetailOption(long orderDetailId) {
		this.order_detail_id = orderDetailId;
	}

	public OrdersDetailOption(
			long odoId,
			long orderDetailId,
			int optionId

	) {
		this.odo_id = odoId;
		this.order_detail_id = orderDetailId;
		this.option_id = optionId;
	}

	public void setOdoId(long odoId) {
		this.odo_id = odoId;
	}

	public long getOdoId() {
		return odo_id;
	}

	public void setOrderDetailId(long orderDetailId) {
		this.order_detail_id = orderDetailId;
	}

	public long getOrderDetailId() {
		return order_detail_id;
	}

	public void setOptionId(long optionId) {
		this.option_id = optionId;
	}

	public long getOptionId() {
		return option_id;
	}


	public OptionsMenu getOptionsMenu() {
		return optionsMenu;
	}

	public void setOptionsMenu(OptionsMenu optionsMenu) {
		this.optionsMenu = optionsMenu;
	}
}
