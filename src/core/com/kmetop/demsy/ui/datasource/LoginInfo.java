package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI_BLOCK;

import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.security.ILogin;

public class LoginInfo extends UiBaseDataSource {

	public Map process(UIBlockContext parser) {

		Map data = new HashMap();

		ILogin login = Demsy.me().login();
		if (login == null) {
			data.put("realmNodes", moduleEngine.makeNodesByRealm(Demsy.me().getSoft()));
		} else {
			data.put("login", login);
			data.put("user", Demsy.me().loginUser());
		}
		data.put("loadBlockUrl", MvcUtil.contextPath(URL_UI_BLOCK, ""));
		IPage link = parser.getBlock().getLink();
		if (link != null) {
			String url = MvcUtil.contextPath(URL_UI, link.getId());
			data.put("registerUrl", url);
			data.put("itemUrl", url);
		}

		IPage tlink = parser.getBlock().getTitleLink();
		if (tlink != null) {
			String url = MvcUtil.contextPath(URL_UI, tlink.getId());
			data.put("myhomeUrl", url);
			data.put("titleUrl", url);
		}

		return data;
	}
}
