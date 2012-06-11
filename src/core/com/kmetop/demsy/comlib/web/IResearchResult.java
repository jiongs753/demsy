package com.kmetop.demsy.comlib.web;

public interface IResearchResult {

	public static final String SYS_CODE = "_web_research_result";

	Long getId();

	IResearchOption getOption();

	void setQuestion(IResearchQuestion rq);

	void setSubject(IResearchSubject rs);

	IResearchQuestion getQuestion();

	Long getResult();

	String getAnswerText();

}
