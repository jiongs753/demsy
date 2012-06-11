package com.kmetop.demsy.comlib.web;

public interface IResearchOption {

	public static final String SYS_CODE = "_web_research_answer";

	IResearchQuestion getQuestion();

	IResearchSubject getSubject();

	Long getId();

	Long getResult();

	String getName();

	void setSubject(IResearchSubject subject);

	void setQuestion(IResearchQuestion question);

	void setName(String name);

	byte getType();

	void setType(byte type);

}
