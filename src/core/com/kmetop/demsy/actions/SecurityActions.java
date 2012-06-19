package com.kmetop.demsy.actions;

import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.Demsy.security;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.globalVariables;

import java.util.HashMap;
import java.util.Map;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Status;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.security.ILogin;
import com.kmetop.demsy.security.IRootUserFactory;
import com.kmetop.demsy.security.SecurityException;

@Ok("json")
public class SecurityActions extends ModuleActions {
	protected static Log log = Logs.getLog(SecurityActions.class);

	@At(URL_SEC_LOGIN_FORM)
	@Ok("st:admin/login")
	public Map loginForm() {
		log.debug("访问后台登录页面...");

		Map context = new HashMap();
		context.putAll(globalVariables);
		Demsy ctx = Demsy.me();

		IDemsySoft soft = ctx.getSoft();

		if (soft != null) {
			context.put("realmNodes", moduleEngine.makeNodesByRealm(soft));
			context.put("title", soft.getName() + "——登录");
		} else {
			context.put("title", "登录");
		}
		context.put("loginBodyBg", MvcUtil.contextPath("/themes2/images/login_body_bg.jpg"));
		context.put("loginBoxBg", MvcUtil.contextPath("/themes2/images/login_box_bg.jpg"));
		context.put("loginBtn", MvcUtil.contextPath("/themes2/images/login_btn.jpg"));

		context.put("user", ctx.loginUser());

		log.debug("访问后台登录成功.");
		return context;
	}

	@At(URL_SEC_LOGIN)
	public Status login(@Param("_loginrealm_") String realm) throws SecurityException {
		log.debugf("登录...[%s]", realm);

		Demsy ctx = Demsy.me();
		String user = ctx.request().getParameter(ILogin.PARAM_USER);
		String pwd = ctx.request().getParameter(ILogin.PARAM_PWD);
		try {
			ILogin login = security.login(ctx.request(), ctx.getSoft(), realm, user, pwd);

			String uri = (String) ctx.request().getSession().getAttribute("lasturi");
			ctx.request().getSession().removeAttribute("lasturi");

			log.debugf("登录成功. [%s]", login);
			return new Status(true, "登录成功.", uri, login);
		} catch (Throwable e) {
			String info = Ex.msg(e);
			log.debugf("登录失败! %s", info);

			return new Status(false, "登录失败: " + info);
		}
	}

	@At(URL_SEC_LOGOUT)
	public Status logout() {
		log.debugf("注销...");

		Demsy ctx = Demsy.me();
		ILogin login = security.login(ctx.request(), ctx.getSoft());

		security.logout(ctx.request(), ctx.getSoft());

		log.debugf("注销成功. [%s]", login);
		return new Status(true, "注销成功.", null, login);
	}

	@At(URL_ADMIN_CHGPWDFORM)
	@Ok("st:admin/chgpwd")
	public Map chgpwdForm() {
		Map context = new HashMap();
		context.putAll(globalVariables);
		Demsy ctx = Demsy.me();

		IDemsySoft soft = ctx.getSoft();

		if (soft != null) {
			context.put("realmNodes", moduleEngine.makeNodesByRealm(soft));
			context.put("title", soft.getName() + "——修改密码");
		} else {
			context.put("title", "修改密码");
		}

		context.put("user", ctx.loginUser());

		return context;
	}

	@At(URL_ADMIN_CHGPWD)
	public Status chgpwd(@Param("_loginpwd_") String oldpwd, @Param("rawPassword") String pwd1, @Param("rawPassword2") String pwd2) throws SecurityException {
		Demsy ctx = Demsy.me();
		String username = ctx.username();
		try {

			if (Str.isEmpty(pwd1) || Str.isEmpty(pwd2)) {
				throw new DemsyException("请输入新密码!");
			}

			IUser user = ctx.loginUser();
			if (user == null) {
				throw new DemsyException("尚未登录，请先登录!");
			}

			security.login(ctx.request(), ctx.getSoft(), ctx.login().getRealm(), username, oldpwd);

			if (security.isRootUser(username)) {
				IRootUserFactory f = security.getRootUserFactory();
				user = f.getUser(username);
				user.setRawPassword("");
				user.setRawPassword2("");
				user.setRawPassword(pwd1);
				user.setRawPassword2(pwd2);
				f.saveUser(user);
			} else {
				IOrm orm = Demsy.orm();
				user = (IUser) orm.load(user.getClass(), Expr.eq(LibConst.F_SOFT_ID, ctx.getSoft()).and(Expr.eq(LibConst.F_CODE, username)));
				if (user == null) {
					throw new DemsyException("登录用户不存在!");
				}
				user.setRawPassword("");
				user.setRawPassword2("");
				user.setRawPassword(pwd1);
				user.setRawPassword2(pwd2);
				orm.save(user);
			}

			return new Status(true, "修改密码成功.");
		} catch (Throwable e) {
			String info = Ex.msg(e);
			log.debugf("修改密码失败! %s", info);

			return new Status(false, "修改密码失败: " + info);
		}
	}
}
