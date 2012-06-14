package com.kmetop.demsy.actions;

import static com.kmetop.demsy.Demsy.me;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.Demsy.security;
import static com.kmetop.demsy.Demsy.uiEngine;
import static com.kmetop.demsy.mvc.MvcConst.VW_BIZ;

import java.util.HashMap;
import java.util.Map;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IAdminUser;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.config.SoftConfigManager;
import com.kmetop.demsy.lang.ConfigException;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ui.IUIView;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;
import com.kmetop.demsy.mvc.ui.widget.UIPageView;

@Ok(VW_BIZ)
@Fail(VW_BIZ)
public class IndexActions extends ModuleActions implements MvcConst {

	@At(URL_INDEX)
	public IUIView index() throws DemsyException {
		log.debug("访问主页...");

		IPage indexPage = Demsy.uiEngine.loadIndexPage();
		if (indexPage == null)
			throw new DemsyException("网站开发中...");

		UIPageView ui = (UIPageView) uiEngine.makePageView(indexPage, null, null, null);
		ui.set("loadBlockUrl", MvcUtil.contextPath(URL_UI_BLOCK, ""));

		log.debugf("访问主页结束. %s", ui);

		return ui;
	}

	/**
	 * 后台首页
	 * 
	 * @return
	 */
	@At(URL_ADMIN)
	@Ok("st:admin/index")
	@Fail("redirect:" + URL_SEC_LOGIN_FORM)
	public Map admin() {
		log.debug("访问后台主页...");

		security.checkLogin(IUserRole.ROLE_ADMIN_USER);

		Map context = new HashMap();
		context.putAll(MvcUtil.globalVariables);

		IDemsySoft soft = me().getSoft();

		context.put("title", soft.getName() + "——后台管理系统");
		context.put("topUrl", MvcUtil.contextPath(URL_ADMIN_TOP, ""));
		context.put("leftUrl", MvcUtil.contextPath(URL_ADMIN_MENU, ""));

		String rightUrl = "#";
		IUser user = Demsy.me().loginUser();
		if (user instanceof IAdminUser) {
			rightUrl = ((IAdminUser) user).getLatestUrl();
		}
		context.put("rightUrl", Str.isEmpty(rightUrl) ? "#" : rightUrl);

		SoftConfigManager config = SoftConfigManager.me();
		context.put("topHeight", config.getInt(SoftConfigManager.ADMIN_UI_TOP_HEIGHT, 95) + 33);
		context.put("leftWidth", config.get(SoftConfigManager.ADMIN_UI_LEFT_WIDTH, "20%"));

		log.debug("访问后台主页成功.");

		return context;
	}

	@At(URL_ADMIN_TOP)
	@Ok("st:admin/top")
	@Fail("redirect:" + URL_SEC_LOGIN_FORM)
	public Map top() throws DemsyException {
		log.debug("访问后台顶部...");

		security.checkLogin(IUserRole.ROLE_ADMIN_USER);

		Map context = new HashMap();
		context.putAll(MvcUtil.globalVariables);

		IDemsySoft soft = me().getSoft();
		Upload logo = soft.getLogo();
		context.put(
				"logo",
				MvcUtil.contextPath((logo == null || Str.isEmpty(logo.toString())) ? "/themes2/images/index_top_logo.jpg"
						: logo.toString()));

		String rightUrl = "#";
		IUser user = Demsy.me().loginUser();
		if (user instanceof IAdminUser) {
			rightUrl = ((IAdminUser) user).getLatestUrl();
		}
		context.put("rightUrl", Str.isEmpty(rightUrl) ? "#" : rightUrl);

		SoftConfigManager config = SoftConfigManager.me();
		context.put("topHeight", config.get(SoftConfigManager.ADMIN_UI_TOP_HEIGHT, "95"));

		context.put("user", me().loginUser());

		log.debug("访问后台顶部成功.");
		return context;
	}

	@At(URL_ADMIN_MENU)
	public UIWidgetModel menu() throws DemsyException {
		String title = "访问后台功能菜单";
		log.debugf("%s....", title);

		try {
			security.checkLogin(IUserRole.ROLE_ADMIN_USER);

			UIWidgetModel modelUI = uiEngine.makeFunctionMenuView(me().getSoft());
			modelUI.setDacorator(null);

			modelUI.setData(moduleEngine.makeNodesByModule(me().getSoft()));
			modelUI.set("target", "body");

			log.debugf("%s成功.", title);
			return modelUI;
		} catch (ConfigException e) {
			throw e;
		} catch (SecurityException e) {
			throw e;
		} catch (Throwable e) {
			String msg = String.format("%s出错! %s", title, Ex.msg(e));
			log.error(msg, e);

			throw new DemsyException(msg);
		}
	}
}
