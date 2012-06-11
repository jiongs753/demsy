package com.kmetop.demsy.comlib.web;

import java.util.List;

public interface IResearchQuestion {

	public static final String SYS_CODE = "_web_research_question";

	Long getId();

	Long getResult();

	String getName();

	void setOptionsJson(String text);

	byte getType();

	void setName(String txt);

	void setSubject(IResearchSubject subject);

	void setOptions(List<IResearchOption> options);

	List<IResearchOption> getOptions();

	void setType(byte type);

	byte getMustable();

	void setMustable(byte mustable);

	IResearchSubject getSubject();
}
