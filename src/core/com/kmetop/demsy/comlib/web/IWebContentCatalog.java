package com.kmetop.demsy.comlib.web;

import com.kmetop.demsy.comlib.biz.IRuntimeConfigable;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IBizComponent;

/**
 * 网站栏目
 * 
 * @author yongshan.ji
 * 
 */
public interface IWebContentCatalog extends IBizComponent, IRuntimeConfigable {
	public static final int TYPE_INFO = 0;

	public static final int TYPE_REF = 1;

	public static final int TYPE_FOLDER = 99;

	static final String SYS_CODE = "WebContentCategory";

	Long getCatalogID();

	String getCatalogName();

	/**
	 * 0:信息栏目,1:引用栏目,99:栏目分类
	 * 
	 * @return
	 */
	Integer getType();

	IWebContentCatalog getRefrence();

	boolean isInfoRequiredImage();

	Upload getImage();
}
