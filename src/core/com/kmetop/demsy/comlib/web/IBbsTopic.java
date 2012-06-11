package com.kmetop.demsy.comlib.web;

import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.security.IUser;

public interface IBbsTopic {

	public static final String SYS_CODE = "LybbsPosttopic";

	public Integer getId();

	public IBbsForum getForum();

	public String getCreatedBy();

	public void setAuthor(IUser user);

	public IUser getAuthor();

	public byte getStatus();

	public void setContent(RichText string);

	/**
	 * 获取警告信息：如果存在警告信息 则不显示帖子内容，否则显示帖子内容
	 * 
	 * @return
	 */
	public String getWarning();

	/**
	 * 设置警告信息：如果存在警告信息 则不显示帖子内容，否则显示帖子内容
	 * 
	 * @return
	 */
	public void setWarning(String warning);

	public byte getViewMode();

	public String getViewUsers();

	public String getName();

	String getDesc();
}
