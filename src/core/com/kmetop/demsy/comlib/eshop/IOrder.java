package com.kmetop.demsy.comlib.eshop;

import java.util.Date;

import com.kmetop.demsy.comlib.entity.ITimeID;

public interface IOrder extends ITimeID {
	public static final String SYS_CODE = "_Order";

	public static final String PAYTYPE_DB = "0";// 担保交易

	// 0:等待买家付款,1:买家已付款,2:卖家已发货,3:交易成功,11:买家申请退款,12:卖家已退款,99:交易关闭
	public static final byte STATUS_WAIT_BUYER_PAY = 0;

	public static final byte STATUS_WAIT_SELLER_SEND_GOODS = 1;// 付款成功，但卖家没有发货

	public static final byte STATUS_WAIT_BUYER_CONFIRM_GOODS = 2;

	public static final byte STATUS_TRADE_SUCCESS = 3;

	public static final byte STATUS_WAIT_SALLER_REFUND = 4;

	public static final byte STATUS_WAIT_BUYER_CONFIRM_REFUND = 5;

	public static final byte STATUS_TRADE_FINISHED = 9;

	public static final byte STATUS_CLOSED = 19;

	public static String[] STATUS_TITLES = new String[] { "WAIT_BUYER_PAY", "WAIT_SELLER_SEND_GOODS",
			"WAIT_BUYER_CONFIRM_GOODS", "TRADE_SUCCESS", "STATUS_WAIT_SALLER_REFUND",
			"STATUS_WAIT_BUYER_CONFIRM_REFUND", "6", "7", "8", "TRADE_FINISHED", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19" };

	// 1:正常订单,2:立即购买,11:团购订单,21:秒杀订单
	public static final byte TYPE_CART = 1;

	public static final byte TYPE_DIRECT_BUY = 2;

	public static final byte TYPE_GROUP = 11;

	public static final byte TYPE_TIME = 21;

	/**
	 * 收货人姓名
	 */
	void setPersonName(String name);

	/**
	 * 联系电话
	 * 
	 * @param code
	 */
	void setTelcode(String code);

	void setAddress(String address);

	void setPostcode(String postcode);

	void setDesc(String note);

	void setPaytype(String type);

	void setLogisticsCost(Double cost);

	void setItemsCost(Double cost);

	void setItemsAmount(int amount);

	void setItemsCatalog(int size);

	void setTotalCost(Double cost);

	void setStatus(byte status);

	// void setDiscount(Double discount);

	void setType(byte type);

	void setSoftID(Long softID);

	byte getStatus();

	Double getLogisticsCost();

	// Double getDiscount();

	Double getItemsCost();

	Long getId();

	String getBuyerInfo();

	Double getTotalCost();

	String getPersonName();

	String getAddress();

	String getPostcode();

	String getTelcode();

	void setTradeID(String trade_no);

	public Integer getLogisticsType();

	public void setLogisticsType(Integer logisticsType);

	public String getLogisticsName();

	public void setLogisticsName(String logisticsName);

	public String getLogisticsID();

	public void setLogisticsID(String logisticsID);

	public String getLogisticsNote();

	public void setLogisticsNote(String logisticsNote);

	String getTradeID();

	public Date getPayTime();

	public void setPayTime(Date payTime);

	public Date getLogisticsTime();

	public void setLogisticsTime(Date logisticsTime);

	public String getOrderID();

	public void setOrderID(String orderID);

	void setLogisticsNum(int logisticsNum);

	String getBuyerHideInfo();

	Object getPaytype();

	int getLogisticsNum();

	Date getCreated();

	public String getNote();

	public void setNote(String note);
}
