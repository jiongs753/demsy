package com.kmetop.demsy.security;

import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IUser;

/**
 * 登录信息： 描述客户端用户登录信息
 * 
 * @author yongshan.ji
 */
public interface ILogin {
	public static final String SESSION_KEY_USER_ROLE = "UserRole";

	public static final String SESSION_KEY_LOGIN_INFO = "Login";

	/**
	 * 参数用户类型KEY：用于从HTTP请求中获取参数用户类型(
	 * {@link com.kmetop.demsy.security.Demsy#realm()})
	 */
	public static final String PARAM_REALM = "_loginrealm_";

	/**
	 * 参数用户帐号KEY：用于从HTTP请求中获取参数用户帐号(
	 * {@link com.kmetop.demsy.security.Demsy#username()})
	 */
	public static final String PARAM_USER = "_loginuser_";

	/**
	 * 参数用户密码KEY：用于从HTTP请求中获取参数用户密码(
	 * {@link com.kmetop.demsy.security.Demsy#password()})
	 */
	public static final String PARAM_PWD = "_loginpwd_";
	public static final String PARAM_CLIENT_WIDTH = "clientWidth";
	public static final String PARAM_CLIENT_HEIGHT = "clientHeight";

	long getModule();

	/**
	 * 获取登录信息中验证成功的用户实体
	 * 
	 * @return 用户实体对象，不能返回空值。
	 */
	IUser getUser();

	void setUser(IUser user);

	/**
	 * 获取客户端所登录的目标应用系统
	 * <OL>
	 * <LI>【DEMSY平台】单应用环境：返回值永远是空。
	 * <LI>【DEMSY平台】多应用环境：表示客户端登录到该目标应用系统
	 * <LI>【DEMSY平台】多应用环境：返回空值——表示超级用户访问【DEMSY平台】
	 * </OL>
	 * 
	 * @return
	 */
	IDemsySoft getApp();

	/**
	 * 获取登录信息中的用户类型
	 * 
	 * @return
	 */
	String getRealm();

	/**
	 * 获取登录信息中的用户帐号
	 * 
	 * @return
	 */
	String getUsername();

	/**
	 * 获取登录信息中的用户缓存对象
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 */
	<T> T get(String key);

	/**
	 * 设置用户缓存对象到登录信息中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	ILogin set(String key, Object value);

	byte getRoleType();

	/**
	 * 获取客户端浏览器宽度
	 * 
	 * @return
	 */
	double getClientWidth();

	/**
	 * 获取客户端浏览器高度
	 * 
	 * @return
	 */
	double getClientHeight();

	/**
	 * 获取浏览器右边——内容区域的宽度
	 * 
	 * @return
	 */
	double getBodyWidth();

}
