package com.kmetop.demsy.comlib.entity;

import java.util.Date;

import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.config.IDataSource;

/**
 * DEMSY软件： 即在DEMSY平台上开发、运行的企业管理软件。
 * <p>
 * <ul>
 * <li>DEMSY平台上可以同时运行多套企业管理软件</li>
 * <li>每个企业可以拥有多套管理软件</li>
 * <li>DEMSY平台可以运行在客户自己的服务器环境上</li>
 * <li>DEMSY平台可以运行在托管的服务器环境上</li>
 * 
 * </ul>
 * 
 * @author yongshan.ji
 * 
 */
public interface IDemsySoft extends IUser {
	/**
	 * 获取应用系统所属企业用户， 一个企业用户可以拥有多个应用系统。
	 * 
	 * @return
	 */
	IDemsyCorp getCorp();

	/**
	 * 判断应用系统是否被锁定， 锁定的应用系统不能被访问。
	 * 
	 * @return
	 */
	boolean isLocked();

	/**
	 * 获取应用系统的有效起始时间
	 * 
	 * @return
	 */
	Date getExpiredFrom();

	/**
	 * 获取应用系统的有效截止时间
	 * 
	 * @return
	 */
	Date getExpiredTo();

	/**
	 * 获取应用系统域名， 可以通过域名判断访问的应用系统。
	 * 
	 * @return
	 */
	String getDomain();

	IDataSource getDataSource();

}
