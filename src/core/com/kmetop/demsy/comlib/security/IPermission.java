package com.kmetop.demsy.comlib.security;

import java.util.Date;

import com.kmetop.demsy.comlib.biz.field.Dataset;

/**
 * <b>权限许可：</b>即“允许”或“拒绝”主体对象在有效期内访问系统资源做什么事情？
 * 
 * @author yongshan.ji
 */
public interface IPermission {
	boolean isDenied();

	boolean isDisabled();

	Date getExpiredFrom();

	Date getExpiredTo();

	Dataset getUsers();

	Dataset getDatas();
}
