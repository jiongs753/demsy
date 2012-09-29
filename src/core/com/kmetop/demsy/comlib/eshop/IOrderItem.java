package com.kmetop.demsy.comlib.eshop;

public interface IOrderItem {
	/**
	 * 购物车：未下单
	 */
	public static final byte STATUS_CART = 0;

	/**
	 * 已下单：备货中...
	 */
	public static final byte STATUS_PREPARING = 2;

	/**
	 * 已备货
	 */
	public static final byte STATUS_PREPARED = 3;

	public static final byte STATUS_CANCEL = 9;

	public static final String SYS_CODE = "_OrderItem";

	void setPrice(double price);

	void setProduct(IProduct product);

	void setProductCatalog(IProductCatalog catalog);

	void setProductOperator(IProductOperator vender);

	void setAmount(int amount);

	void setSubtotal(double d);

	void setStatus(byte status);

	void setCreatedIP(String remoteAddr);

	void setName(String name);

	void setCode(String code);

	IProduct getProduct();

	int getAmount();

	double getSubtotal();

	void setSoftID(Long id);

	void setOrder(IOrder order);

	byte getStatus();

	IOrder getOrder();

	Double getPrice();

	Double getDiscount();

	String getName();

	String getCode();

	Long getId();

	void setDiscount(Double discount);
}
