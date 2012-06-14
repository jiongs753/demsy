package com.kmetop.demsy.security.impl;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_ENCODER;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_SOFT;
import static com.kmetop.demsy.comlib.LibConst.F_CODE;
import static com.kmetop.demsy.comlib.LibConst.F_SOFT_ID;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.entity.IEncryption;
import com.kmetop.demsy.comlib.security.IAdminUser;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IPermission;
import com.kmetop.demsy.comlib.security.IRealm;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.engine.RootUserFactory;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.CndType;
import com.kmetop.demsy.orm.expr.CombCndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.expr.SimpleCndExpr;
import com.kmetop.demsy.security.ILogin;
import com.kmetop.demsy.security.IPasswordEncoder;
import com.kmetop.demsy.security.IRootUserFactory;
import com.kmetop.demsy.security.ISecurity;
import com.kmetop.demsy.security.SecurityException;
import com.kmetop.demsy.security.UnloginException;

public class Security implements ISecurity {
	protected static Log log = Logs.getLog(Security.class);

	// 依赖注入
	private IPasswordEncoder defaultPasswordEncoder;

	private IRootUserFactory rootusers = new RootUserFactory();

	// <softID,<moduleID, PermissionEntry>>
	private Map<Long, Map<Long, List<PermissionEntry>>> permissionItems = new HashMap();

	private Map<Long, Map<String, PermissionEntry>> dynamicPermissionItems = new HashMap();

	private IOrm orm() {
		return Demsy.orm();
	}

	public IPasswordEncoder getPwdEncoder(Long encoder, IPasswordEncoder defaultPE) {
		if (encoder == null || encoder <= 0) {
			return defaultPE;
		}
		synchronized (Security.class) {
			IPasswordEncoder pwdEncoder = null;
			Class<IPasswordEncoder> encoderClass = null;
			try {
				IEncryption strategy = (IEncryption) Demsy.orm().load(
						bizEngine.getStaticType(BIZSYS_DEMSY_LIB_ENCODER),
						Expr.eq(F_CODE, "" + encoder).or(Expr.eq(LibConst.F_ID, encoder)));
				if (strategy != null) {
					encoderClass = Cls.forName(strategy.getEncodeClass());
				}
			} catch (Throwable e) {
				log.errorf("加载密码加密器类出错! [encoder=%s]详细信息： %s", encoder, e);
			}
			if (encoderClass != null) {
				try {
					pwdEncoder = encoderClass.newInstance();
				} catch (Throwable e1) {
					log.errorf("创建密码加密器实例出错! 详细信息： %s", e1);
				}
			}
			return pwdEncoder;
		}
	}

	@Override
	public String encrypt(String username, String rawPwd, Long encoder) {
		return getPwdEncoder(encoder, defaultPasswordEncoder).encodePassword(rawPwd, username);
	}

	protected String genLoginKey(IDemsySoft app) {
		if (app == null) {
			return ILogin.SESSION_KEY_LOGIN_INFO;
		}
		return app.getId() + "." + ILogin.SESSION_KEY_LOGIN_INFO;
	}

	@Override
	public ILogin login(HttpServletRequest request, IDemsySoft app, String realm, String username, String password)
			throws SecurityException {
		HttpSession session = request.getSession();
		LoginImpl login = new LoginImpl(request, app, realm, username, password);
		session.setAttribute(genLoginKey(app), login);
		if (login.getRoleType() > 0) {
			session.setAttribute(ILogin.SESSION_KEY_USER_ROLE, "" + login.getRoleType());
		}

		return login;
	}

	@Override
	public ILogin login(HttpServletRequest request, IDemsySoft app) {
		return (ILogin) request.getSession().getAttribute(genLoginKey(app));
	}

	@Override
	public ILogin logout(HttpServletRequest request, IDemsySoft app) {
		HttpSession session = request.getSession();
		String key = genLoginKey(app);
		ILogin login = (ILogin) session.getAttribute(key);
		session.removeAttribute(key);

		return login;
	}

	public IUser getRootUser(String username) {
		return rootusers.getUser(username);
	}

	@Override
	public IUser checkUser(IDemsySoft soft, String realmCode, String username, String pwd) throws SecurityException {
		log.debugf("获取用户......[soft=%s, realm=%s, user=%s]", soft, realmCode, username);

		// 获取超级用户
		IUser user = null;
		if (Str.isEmpty(realmCode)) {
			user = getRootUser(username);
			if (user == null) {
				user = (IUser) orm().load(bizEngine.getStaticType(BIZSYS_DEMSY_SOFT), Expr.eq(F_CODE, username));
			}
		} else {
			IRealm realm = moduleEngine.getRealm(soft, realmCode);
			if (realm != null) {
				IBizSystem sys = moduleEngine.getSystem(realm.getUserModule());
				if (sys != null) {
					Class type = bizEngine.getType(sys);
					if (!IUser.class.isAssignableFrom(type)) {
						throw new SecurityException("安全策略中的用户模块非法! [%s]", realmCode);
					}
					user = (IUser) orm().load(type, Expr.eq(F_SOFT_ID, soft.getId()).and(Expr.eq(F_CODE, username)));
				}
			}
		}

		// 检查用户是否存在
		if (user == null) {
			throw new SecurityException("用户不存在! [%s]", username);
		}

		// 检查用户是否被禁用
		if (user.isDisabled()) {
			throw new SecurityException("用户不可用! [%s]", username);
		}

		// 检查用户是否被锁定
		if (user.isLocked()) {
			throw new SecurityException("用户已被锁定! [%s]", username);
		}

		// 检查帐号有效期
		Date now = new Date();
		Date from = user.getExpiredFrom();
		Date to = user.getExpiredTo();
		if ((from != null && now.getTime() < from.getTime()) || (to != null && now.getTime() > to.getTime())) {
			throw new SecurityException("用户有效期已过! [%s]", username);
		}

		// 检查用户密码
		IPasswordEncoder encoder = getPwdEncoder(user.getPwdEncoder(), defaultPasswordEncoder);
		if (!encoder.isValidPassword(user.getPassword(), pwd, username)) {
			throw new SecurityException("用户密码不正确! [%s]", username);
		}

		log.debugf("获取用户: 结束. [ user=%s]", username);

		return user;
	}

	public boolean isRootUser(String username) {
		return this.rootusers.getUser(username) != null;
	}

	// protected Object permitOne(Object obj) {
	// if (obj == null)
	// return null;
	//
	// // TODO: 检查权限许可
	//
	// return obj;
	// }

	// protected List permitList(List list) {
	// List ret = new LinkedList();
	// for (Object res : list) {
	// res = permitOne(res);
	// if (res != null) {
	// ret.add(res);
	// }
	// }
	//
	// return ret;
	// }
	//
	// protected Map permitMap(Map map) {
	// Map ret = new HashMap();
	// Iterator keys = map.keySet().iterator();
	// while (keys.hasNext()) {
	// Object key = keys.next();
	// Object value = map.get(key);
	// if (value != null) {
	// ret.put(key, value);
	// }
	// }
	//
	// return ret;
	// }

	// @Override
	// public <T> T checkPermission(T obj) {
	// if (obj instanceof List)
	// return (T) permitList((List) obj);
	// if (obj instanceof Map)
	// return (T) permitMap((Map) obj);
	//
	// return (T) this.permitOne(obj);
	// }

	@Override
	public void checkLogin(byte roleType) throws SecurityException {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null) {
			me.request().getSession().setAttribute("lasturi", Demsy.MvcUtil.requestURI(me.request()));
			throw new UnloginException("尚未登录或登录已过期，请先登录!");
		}
		if (login.getRoleType() < roleType)
			throw new SecurityException("你没有足够的权限执行该操作!");
	}

	public IRootUserFactory getRootUserFactory() {
		return rootusers;
	}

	protected class LoginImpl implements ILogin {
		private Map<String, Object> cachedData = new HashMap();

		private IUser user;

		private IDemsySoft soft;

		private long module;

		private String realm;

		private String username;

		private byte roleType;

		private double clientWidth = 1024.0;

		private double clientHeight = 768.0;

		LoginImpl(HttpServletRequest request, IDemsySoft app, String realm, String username, String password)
				throws SecurityException {
			log.debug("创建登录信息对象......");

			this.soft = app;
			this.realm = realm;
			this.username = username;

			if (!Str.isEmpty(realm)) {
				IRealm realmObj = moduleEngine.getRealm(soft, realm);
				if (realmObj != null && realmObj.getUserModule() != null)
					this.module = realmObj.getUserModule().getId();
			}

			if (Str.isEmpty(username)) {
				throw new SecurityException("检查登录用户失败! 未指定登录帐号.");
			} else {
				user = checkUser(soft, realm, username, password);

				this.initRole();

				log.debug("创建登录信息对象: 成功.");
			}

			//
			String sClientWidth = request.getParameter(PARAM_CLIENT_WIDTH);
			String sClientHeight = request.getParameter(PARAM_CLIENT_HEIGHT);
			if (!Str.isEmpty(sClientWidth)) {
				try {
					clientWidth = Double.parseDouble(sClientWidth);
				} catch (Throwable e) {

				}
			}
			if (!Str.isEmpty(sClientHeight)) {
				try {
					clientHeight = Double.parseDouble(sClientHeight);
				} catch (Throwable e) {

				}
			}
		}

		public LoginImpl(IDemsySoft app, IUser user) {
			this.soft = app;
			this.username = user.getUsername();

			this.user = user;
			this.initRole();
		}

		private void initRole() {
			if (isRootUser(username)) {
				roleType = IUserRole.ROLE_DEVELOPER;
			} else if (user instanceof IAdminUser) {
				IAdminUser admin = (IAdminUser) user;
				if (admin.getRole() != null)
					roleType = admin.getRole().getType();
				else
					roleType = IUserRole.ROLE_ADMIN_USER;
			} else if (user instanceof IDemsySoft) {
				roleType = IUserRole.ROLE_ADMIN_ROOT;
			}
		}

		public IUser getUser() {
			return user;
		}

		public IDemsySoft getApp() {
			return soft;
		}

		public String getRealm() {
			return realm;
		}

		public String getUsername() {
			return username;
		}

		@Override
		public Object get(String key) {
			return cachedData.get(key);
		}

		@Override
		public ILogin set(String key, Object value) {
			cachedData.put(key, value);
			return this;
		}

		public byte getRoleType() {
			return roleType;
		}

		public long getModule() {
			return module;
		}

		@Override
		public void setUser(IUser user) {
			this.user = user;
		}

		@Override
		public double getClientWidth() {
			return clientWidth;
		}

		@Override
		public double getClientHeight() {
			return clientHeight;
		}

		@Override
		public double getBodyWidth() {
			return clientWidth * 0.8;
		}

	}

	private class PermissionEntry {
		private long userModule;

		private CndExpr userExpr;

		private byte roleID = -1;

		private long dataModule;

		// private String[] actions;

		private CndExpr dataExpr;

		private Date expiredFrom;

		private Date expiredTo;

		private boolean denied;
	}

	public CndExpr getPermissionExpr(IModule module) {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null)
			return null;

		if (login.getRoleType() >= IUserRole.ROLE_ADMIN_ROOT) {
			return null;
		}

		List<CndExpr> exprs = new LinkedList();
		List<PermissionEntry> items = this.getModulePermissions(me.getSoft().getId(), module.getId());
		if (items != null) {
			for (PermissionEntry p : items) {
				long now = new Date().getTime();
				if (p.expiredFrom != null && now < p.expiredFrom.getTime())
					continue;
				if (p.expiredTo != null && now > p.expiredTo.getTime())
					continue;
				if (me.login().getModule() != p.userModule)
					continue;
				if (!match(login, p)) {
					continue;
				}

				if (p.dataExpr != null) {
					if (p.denied == false)
						exprs.add(p.dataExpr);
				}
			}
		}

		int len = exprs.size();
		if (len == 1) {
			return exprs.get(0);
		} else if (len == 0) {
			return null;
		}
		CndExpr expr = exprs.get(0);
		for (int i = 1; i < len; i++) {
			expr = expr.or(exprs.get(i));
		}
		return expr;
	}

	public CndExpr getPermissionFkExpr(IModule module, String fkField) {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null)
			return null;

		if (login.getRoleType() >= IUserRole.ROLE_ADMIN_ROOT) {
			return null;
		}

		List<CndExpr> exprs = new LinkedList();
		List<PermissionEntry> items = this.getModulePermissions(me.getSoft().getId(), module.getId());
		if (items != null) {
			for (PermissionEntry p : items) {
				long now = new Date().getTime();
				if (p.expiredFrom != null && now < p.expiredFrom.getTime())
					continue;
				if (p.expiredTo != null && now > p.expiredTo.getTime())
					continue;
				if (me.login().getModule() != p.userModule)
					continue;
				if (!match(login, p)) {
					continue;
				}
				addFkExpr(exprs, p.dataExpr, fkField);
			}
		}

		int len = exprs.size();
		if (len == 1) {
			return exprs.get(0);
		} else if (len == 0) {
			return null;
		}
		CndExpr expr = exprs.get(0);
		for (int i = 1; i < len; i++) {
			expr = expr.or(exprs.get(i));
		}
		return expr;
	}

	/**
	 * 解析权限限制的外键表达式
	 * 
	 * @param exprs
	 * @param expr
	 * @param fkField
	 */
	private void addFkExpr(List exprs, CndExpr expr, String fkField) {
		if (expr instanceof SimpleCndExpr) {
			SimpleCndExpr sexpr = (SimpleCndExpr) expr;
			if (sexpr != null) {
				String prop = sexpr.getProp();
				String fk = prop;
				int dot = prop.indexOf(".");
				if (dot > -1) {
					fk = prop.substring(0, dot);
				}
				if (fk.equals(fkField)) {
					prop = prop.substring(dot + 1);
					exprs.add(new SimpleCndExpr(prop, sexpr.getType(), sexpr.getValue()));
				}
			}
		} else if (expr instanceof CombCndExpr) {
			CombCndExpr cexpr = (CombCndExpr) expr;
			CndExpr expr1 = cexpr.getExpr();
			addFkExpr(exprs, expr1, fkField);
			CndExpr expr2 = cexpr.getExpr2();
			addFkExpr(exprs, expr2, fkField);
		}
	}

	public boolean visit(IModule module, boolean igloreDynamic) {
		Demsy me = Demsy.me();

		ILogin login = me.login();
		if (login != null) {
			if (login.getRoleType() >= IUserRole.ROLE_ADMIN_ROOT) {
				return true;
			}
		}
		boolean allow = false;
		boolean denied = false;

		// 动态内存授权
		if (!igloreDynamic) {
			Map dynitems = this.dynamicPermissionItems.get(me.getSoft().getId());
			if (dynitems != null) {
				Iterator<PermissionEntry> it = dynitems.values().iterator();
				while (it.hasNext()) {
					PermissionEntry p = it.next();
					if (module.getId().equals(p.dataModule) && match(login, p)) {
						return true;
					}
				}
			}
		}

		// 数据库授权
		List<PermissionEntry> items = this.getModulePermissions(me.getSoft().getId(), module.getId());
		if (items != null) {
			for (PermissionEntry p : items) {
				long now = new Date().getTime();
				if (p.expiredFrom != null && now < p.expiredFrom.getTime())
					continue;
				if (p.expiredTo != null && now > p.expiredTo.getTime())
					continue;
				if (me.login().getModule() != p.userModule)
					continue;

				if (!match(login, p)) {
					continue;
				}
				if (p.denied)
					denied = true;
				else
					allow = true;
			}
		}

		return allow && !denied;
	}

	private boolean match(ILogin login, PermissionEntry p) {
		if (p.roleID == IUserRole.ROLE_ANONYMOUS)
			return true;

		// 授权给匿名用户
		if (login != null) {
			IUser user = login.getUser();
			// 授权给登录用户
			if (p.roleID == IUserRole.ROLE_LOGIN_USER) {
				return true;
			}

			// 授权给用户角色
			if (p.roleID == login.getRoleType())
				return true;

			// 匹配用户表达式
			if (user != null && p.userExpr instanceof SimpleCndExpr) {
				SimpleCndExpr expr = (SimpleCndExpr) p.userExpr;

				String prop = expr.getProp();
				CndType type = expr.getType();
				Object value = expr.getValue();

				Object propValue = Obj.getValue(user, prop);
				if (type == CndType.in) {
					List list = (List) value;
					if (propValue != null && list.contains(propValue))
						return true;
				} else if (type == CndType.eq) {
					if (propValue != null && propValue.equals(value))
						return true;
				}
			}
		}

		return false;
	}

	private List<PermissionEntry> getModulePermissions(long softID, long module) {
		Map<Long, List<PermissionEntry>> modulePermisstions = getModulePermissions(softID);
		return modulePermisstions.get(module);
	}

	private Map<Long, List<PermissionEntry>> getModulePermissions(long softID) {
		Map<Long, List<PermissionEntry>> modulePermissions = permissionItems.get(softID);
		if (modulePermissions == null) {
			modulePermissions = this.loadPermissions(softID);
		}
		return modulePermissions;
	}

	private List<PermissionEntry> getPermissions(Map<Long, List<PermissionEntry>> map, Long module) {
		List<PermissionEntry> items = map.get(module);
		if (items == null) {
			items = new LinkedList();
			map.put(module, items);
		}
		return items;
	}

	private Map<Long, List<PermissionEntry>> loadPermissions(Long softID) {
		IOrm orm = orm();
		Demsy me = Demsy.me();

		IBizSystem sys = bizEngine.getSystem(LibConst.BIZSYS_ADMIN_PERMISSION);
		Class type = bizEngine.getType(sys);

		Map<Long, List<PermissionEntry>> map = this.permissionItems.get(softID);
		if (map == null) {
			map = new HashMap();
			permissionItems.put(softID, map);
		}

		List<IPermission> permissions = orm.query(type, Expr.eq(F_SOFT_ID, me.getSoft()));
		for (IPermission p : permissions) {
			if (p.isDisabled())
				continue;

			Dataset users = p.getUsers();
			Dataset datas = p.getDatas();
			if (users == null || datas == null)
				continue;

			PermissionEntry item = new PermissionEntry();
			item.expiredFrom = p.getExpiredFrom();
			item.expiredTo = p.getExpiredTo();
			item.denied = p.isDenied();
			if (users.getModule() != null)
				item.userModule = users.getModule().getId();
			if (datas.getModule() != null)
				item.dataModule = datas.getModule().getId();
			if (!Str.isEmpty(users.getRules()))
				item.userExpr = CndExpr.make(users.getRules());
			if (!Str.isEmpty(datas.getRules()))
				item.dataExpr = CndExpr.make(datas.getRules());

			getPermissions(map, item.dataModule).add(item);
		}

		return map;
	}

	@Override
	public void clearPermissions() {
		permissionItems.clear();
	}

	@Override
	public void addPermission(String key, byte roleID, long moduleID, String action) {
		Map<String, PermissionEntry> map = this.dynamicPermissionItems.get(Demsy.me().getSoft().getId());
		if (map == null) {
			map = new HashMap();
			dynamicPermissionItems.put(Demsy.me().getSoft().getId(), map);
		}
		String key1 = key + "." + action;
		// if (map.get(key1) != null) {
		// return;
		// }

		PermissionEntry item = new PermissionEntry();
		item.roleID = roleID;
		item.dataModule = moduleID;
		// item.actions = new String[] { action };

		map.put(key1, item);
	}
}
