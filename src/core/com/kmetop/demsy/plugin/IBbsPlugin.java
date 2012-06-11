package com.kmetop.demsy.plugin;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

public interface IBbsPlugin {
	String getUserImageUrl(String username, String imageUrl);

	Object getUserGrade(String userName);

	<T> T loginBBS(HttpServletRequest request, String username);

	<T> T createBBSUser(HttpServletRequest request, String username);

	boolean forceLogin(HttpServletRequest request, String username);

	void setUserMode(Object user);

	String getLoginFullName(String code);

	void mark(String username, String ruleCode, int quantity, String relatedInfo);

	String html2Text(String inputString);

	String getMarkRuleBbsPost();

	boolean login(HttpServletRequest request, String username, String password);

	<T> T getBbsUser(HttpServletRequest request);

	String getMemberID(String username);

	InputStream getDbConfigAsStream();

	StringBuffer getTypeAdTag();

	boolean isTypeAdTagEnabled(String key);

	boolean isOverrideOnline();

	void writeOnline(StringBuffer sb, HttpServletRequest request, int forumID);

	boolean isBBSHomePageGameEnabled();

	boolean isBBSHomePageStyleEnabled();

	boolean isWelcomeInfoEnabled();

	boolean isBBSHomePageLoginEnabled();

	boolean isBBSHomePageRegisterEnabled();

	boolean isWhosOnlineEnabled();

	boolean isSearchMenuEnabled();

	boolean isBBSHomePageHelpEnabled();

	boolean isBBSHomePageStatEnabled();

	boolean isBBSHomePageBlogEnabled();

	boolean isControlPanelEnabled();

	boolean isAdminApplyManagerEnabled();

	boolean isBBSHomePageLogoutEnabled();

	boolean isAdminCenterEnabled();

	StringBuffer getWebTag(String string);

	boolean isWebTagEnabled(String string);

	boolean isBbsEnabled(String string);

	boolean isOverrideAlliance();

	void writeAlliance();

	boolean isOverrideHomePageStarUser();

	void writeHomePageStarUser();

	boolean isOverrideHomePageBirthdayUser();

	void writeHomePageBirthdayUser();

	boolean isOverrideHomePageNewTopic();

	void writeHomePageNewTopic();

	boolean isOverrideUserNewAgreement();

	void writeUserNewAgreement(String agreement);

	String getUserShowUrl();

	String getMarkRuleBbsPostRecommended();

	String getMarkRuleComment();

	String getMarkRuleBbsPostCommented();

	void cancelMark(String username, String ruleCode, String relatedInfo);

	String getMarkRuleBbsPostClicked();
}
