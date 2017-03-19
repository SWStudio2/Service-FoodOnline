package com.fooddelivery.Model.Order;

public class SubOrder {
	private String menuid;
	private String menuAmount;
	private String remark;
	private String[] optioId;
	
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getMenuAmount() {
		return menuAmount;
	}
	public void setMenuAmount(String menuAmount) {
		this.menuAmount = menuAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String[] getOptioId() {
		return optioId;
	}
	public void setOptioId(String[] optioId) {
		this.optioId = optioId;
	}
	
}
