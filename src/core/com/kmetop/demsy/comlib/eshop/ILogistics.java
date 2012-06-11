package com.kmetop.demsy.comlib.eshop;

import java.util.Date;

public interface ILogistics {
	public static final String SYS_CODE = "_eshop_Logistics";

	/**
	 * 获取收货人姓名
	 * 
	 * @return
	 */
	public String getPersonName();

	public void setPersonName(String name);

	public void setDeliver(IProductDeliver storage);

	public IProductOperator getOperator();

	public void setOperator(IProductOperator operator);

	public void setOrderID(String timeID);

	public void setAddress(String address);

	public void setPostcode(String postcode);

	public void setTelcode(String telcode);

	public void setItemsCatalog(int size);

	public void setItemsAmount(int amount);

	public void setSoftID(Long id);

	public void setOrderDate(Date payTime);

	public void setTotalCost(Double totalCast);

	public void setDesc(String string);

	public void setNote(String note);
}
