package com.kmetop.demsy.biz;

import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.lang.DemsyException;

/**
 * 用于获取授权业务模块管理器。
 * 
 * @author yongshan.ji
 * 
 */
public interface IBizManagerFactory {

	/**
	 * 获取业务管理器。
	 * <UL>
	 * <LI>检查当前用户是否有权访问指定的模块；
	 * <LI>如果无权访问则抛出安全异常，否则返回模块管理器；
	 * </UL>
	 * 
	 * @param <X>
	 *            被管理的实体类型
	 * @param moduleCode
	 *            模块编号
	 * @return 业务管理器
	 * @throws DemsyException
	 *             如果无权访问指定模块则抛出安全异常。
	 */
	public <X> IBizManager<X> getManager(String moduleID) throws DemsyException;

	/**
	 * 获取业务管理器。
	 * <UL>
	 * <LI>无需检查当前用户是否有权访问指定的模块；
	 * </UL>
	 * 
	 * @param <X>
	 *            被管理的实体类型
	 * @param module
	 *            业务模块
	 * @return 业务管理器
	 * @throws DemsyException
	 */
	public <X> IBizManager<X> getManager(IModule module) throws DemsyException;

}
