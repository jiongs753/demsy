package com.kmetop.demsy.comlib.security;

public interface IAdminUser extends IUser {
	IUserRole getRole();

	IGroup getGroup();

	String getLatestUrl();

	void setLatestUrl(String url);
}
