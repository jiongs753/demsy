package com.kmetop.demsy.security;

import javax.servlet.http.HttpServletRequest;

import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.orm.expr.CndExpr;

/**
 * DEMSY安全管理器接口
 * 
 * @author yongshan.ji
 */
public interface ISecurity {
	boolean isRootUser(String username);

	IRootUserFactory getRootUserFactory();

	boolean visit(IModule module,boolean igloreDynamic);

	void checkLogin(byte roleType) throws SecurityException;

	/**
	 * 对原始密文加密
	 * 
	 * @param salt
	 * 
	 * @param rawpwd
	 *            原始密码
	 * @param encoder
	 *            加密器ID，null——表示使用默认加密器
	 * @return 返回加密后的密文
	 */
	String encrypt(String salt, String rawpwd, Long encoder);

	// ***登录 API***
	/**
	 * 登录应用系统： 检查登录账号和密码是否匹配，如果匹配则将用户账户对象保存在HTTP SESSION中。
	 * <p>
	 * 已经登录的账户会被先注销然后再登录
	 * 
	 * @param app
	 *            应用系统
	 * @param realm
	 *            用户类型
	 * @param username
	 *            用户账号
	 * @param password
	 *            用户密码
	 * @return 返回登录信息
	 * @throws SecurityException
	 *             将会抛出安全异常。如用户名不存在；用户名密码不匹配；用户已被锁定等
	 */
	ILogin login(HttpServletRequest request, IDemsySoft app, String realm, String username, String password) throws SecurityException;

	/**
	 * 获取登录信息
	 * 
	 * @param app
	 *            应用系统
	 * @return 返回Session中的登录信息
	 */
	ILogin login(HttpServletRequest request, IDemsySoft app);

	/**
	 * 注销已经登录的用户会话对象
	 * 
	 * @param app
	 *            应用系统
	 * @return 返回登录信息
	 */
	ILogin logout(HttpServletRequest request, IDemsySoft app);

	// ***用户 API***

	/**
	 * 获取应用中帐号和密码匹配的用户
	 * <UL>
	 * <LI>应用系统——表示要检查是哪个应用系统中的用户？(DEMSY平台中可同时运行多个应用系统)
	 * <LI>登录类型——表示实现ILogin接口的业务系统编号。(应用系统支持有多种用户类型如：员工、会员、管理员等)
	 * </UL>
	 * 
	 * @param soft
	 *            应用系统。
	 * @param realm
	 *            用户类型
	 * @param username
	 *            用户账号
	 * @param password
	 *            用户密码
	 * @return 用户对象
	 * @throws SecurityException
	 *             将会抛出安全异常。如用户名不存在；用户名密码不匹配；用户已被锁定等
	 */
	IUser checkUser(IDemsySoft soft, String realm, String username, String password) throws SecurityException;

	CndExpr getPermissionFkExpr(IModule module, String fkField);

	CndExpr getPermissionExpr(IModule module);

	/**
	 * 动态授权用户角色可以访问的模块操作
	 * 
	 * @param roleID
	 * @param moduleID
	 * @param actions
	 */
	void addPermission(String key, byte roleID, long moduleID, String action);

	void clearPermissions();

	// ***检查资源许可***

	// /**
	// * 检查当前用户是否有权访问指定的资源
	// * <UL>
	// * <LI>如果自愿是Collection/Map等，则将逐项检查，并忽略无权访问的项。
	// * </UL>
	// *
	// * @param <T>
	// * 资源类型
	// * @param resource
	// * 资源
	// * @return 返回有权访问的资源
	// */
	// <T> T checkPermission(T resource);

	// /**
	// * 检查当前用户是否有权访问指定的资源列表，无权访问的资源将被过滤。
	// *
	// * @param <T>
	// * 资源类型
	// * @param list
	// * 资源列表
	// * @return 返回过滤后有权访问的资源列表
	// */
	// <T> List<? extends T> checkPermission(List<? extends T> list);

	// ***模块 API***
	//
	// /**
	// * 检查登录用户是否有权访问指定的功能模块？
	// *
	// * @param module
	// * 功能模块
	// * @return 返回null表示无权访问指定模块
	// */
	// IModule checkModule(IModule module);
	//
	// /**
	// * 获取功能模块，如果用户有权访问则返回模块对象，否则抛出安全异常。
	// *
	// * @param moduleID
	// * 功能模块ID：可以是String型的模块编号；也可以是Long型的模块id
	// * @return 功能模块
	// * @throws SecurityException
	// * 如果用户无权访问指定的功能模块则抛出异常
	// */
	// IModule getModule(Serializable moduleID) throws SecurityException;
	//
	// /**
	// * 检查当前用户有权访问的功能模块，无权访问的模块将被过滤
	// *
	// * @return 返回权限范围内的功能模块列表
	// */
	// List<? extends IModule> checkModules(List<? extends IModule> list);
	//
	// /**
	// * 获取登录用户有权访问的所有功能模块
	// *
	// * @return 返回权限范围内的功能模块列表
	// */
	// List<? extends IModule> getModules();
	//
	// /**
	// * 获取登录用户有权访问的下级功能模块
	// * <p>
	// * 父模块：null——表示获取根模块
	// *
	// * @param parent
	// * 父模块
	// * @return 获取权限范围内的子模块列表
	// */
	// List<? extends IModule> getSubModules(IModule parent);
	//
	// // ***操作 API***
	// /**
	// * 检查登录用户是否有权访问指定的操作？
	// *
	// * @param op
	// * 操作
	// * @return 返回null表示无权访问指定操作
	// */
	// IOperation checkOperation(IOperation op);
	//
	// /**
	// * 获取操作，如果用户有权访问则返回操作，否则抛出安全异常。
	// *
	// * @param opID
	// * 操作编号: 可以是String型的操作编号；也可以是Long型的操作id
	// * @return 返回权限范围内的操作
	// * @throws SecurityException
	// * 如果用户无权访问则抛出异常
	// */
	// IOperation getOperation(IModule module, Serializable opID) throws
	// SecurityException;
	//
	// /**
	// * 检查当前用户有权访问的操作，无权访问的操作将被过滤
	// *
	// * @return 返回权限范围内的操作列表
	// */
	// List<? extends IOperation> checkOperations(List<? extends IOperation>
	// list);
	//
	// /**
	// * 获取登录用户有权访问的所有模块操作。
	// *
	// * @param module
	// * 功能模块
	// * @return 返回权限范围内的模块操作列表
	// */
	// List<? extends IOperation> getOperations(IModule module);
	//
	// /**
	// * 获取登录用户有权访问的模块子操作。
	// * <P>
	// * 父操作：null——表示获取模块根操作，否则获取模块子操作
	// *
	// * @param module
	// * 功能模块
	// * @param parent
	// * 父操作
	// * @return 返回权限范围内的子操作列表
	// */
	// List<? extends IOperation> getSubOperations(IModule module, IOperation
	// parent);

}
