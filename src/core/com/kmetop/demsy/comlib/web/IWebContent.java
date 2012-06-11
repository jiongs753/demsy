package com.kmetop.demsy.comlib.web;

import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.biz.field.Upload;

/**
 * 网站信息
 * 
 * @author yongshan.ji
 * 
 */
public interface IWebContent {
	static final String SYS_CODE = "WebContent";

	public static final int TYPE_EDITOR = 0;

	public static final int TYPE_LINK = 1;

	public static final int TYPE_REFER = 99;

	IWebContentCatalog getInfoCatalog();

	IWebContent getRefrence();

	Long getInfoID();

	String getInfoName();

	String getInfoAuthor();

	String getInfoOrigin();

	RichText getInfoContent();

	String getInfoLinkPath();

	// String getInfoLinkTarget();

	String getInfoLogo();

	String getInfoImage();

	String getInfoDate();

	Integer getTypeCode();

	String getName();

	Upload getImage();

	String getInfoDesc();

}
