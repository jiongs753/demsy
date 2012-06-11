package com.kmetop.demsy.comlib.eshop;

public interface ILogisticsItem {

	public static final String SYS_CODE = "_eshop_LogisticsItem";

	void setLogistics(ILogistics logistics);

	void setAmount(int amount);

	void setName(String name);

	void setCode(String code);

	void setSoftID(Long id);

	void setOrderID(String orderID);

	void setOrderItemID(Long id);

	void setProductID(Long id);

	void setPrice(Double price);

	void setSubtotal(Double subtotal);
}
