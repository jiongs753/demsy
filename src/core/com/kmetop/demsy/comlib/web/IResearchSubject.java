package com.kmetop.demsy.comlib.web;

import java.util.Date;
import java.util.List;

public interface IResearchSubject {

	public static final String SYS_CODE = "_web_research_subject";

	/**
	 * 禁止查看
	 */
	public static final byte VIEW_POLICY_DISABLED = 0;

	/**
	 * 允许查看
	 */
	public static final byte VIEW_POLICY_ENABLED = 1;

	// /**
	// * 登录查看
	// */
	public static final byte VIEW_POLICY_LOGIN = 2;

	/**
	 * 普通管理员查看
	 */
	public static final byte VIEW_POLICY_ADMIN_USER = 3;

	/**
	 * 超级管理员查看
	 */
	public static final byte VIEW_POLICY_ADMIN_ROOT = 4;

	/**
	 * 匿名参与
	 */
	public static final byte ENTRY_POLICY_ANONYMOUS = 0;

	/**
	 * 登录参与
	 */
	public static final byte ENTRY_POLICY_LOGIN_USER = 1;

	Long getId();

	Date getExpiredFrom();

	Date getExpiredTo();

	byte getEntryPolicy();

	byte getEntryTimes();

	Long getResult();

	String getQuestionsJson();

	void setQuestions(List<IResearchQuestion> questions);

	byte getViewPolicy();

}
