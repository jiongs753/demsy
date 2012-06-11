package com.kmetop.demsy.comlib.eshop;

import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.biz.field.Upload;

public interface IProduct {
	public static final String SYS_CODE = "_Product";

	Double getNowPrice();

	IProductCatalog getCatalog();

	IProductOperator getOperator();

	IProductDeliver getStorage();

	String getName();

	String getCode();

	Long getId();

	Upload getImage();

	Double getOldPrice();

	Double getBalance();

	Integer getStockNum();

	String getDesc();

	RichText getContent();

}
