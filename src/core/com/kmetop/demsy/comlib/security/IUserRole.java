package com.kmetop.demsy.comlib.security;

import com.kmetop.demsy.comlib.entity.IBizComponent;

public interface IUserRole extends IBizComponent {

	public static final byte ROLE_ANONYMOUS = 0;

	public static final byte ROLE_LOGIN_USER = 1;

	/**
	 * 普通管理员
	 */
	public static final byte ROLE_ADMIN_USER = 90;

	/**
	 * 超级管理员
	 */
	public static final byte ROLE_ADMIN_ROOT = 100;

	public static final byte ROLE_DEVELOPER = 127;

	byte getType();

}
