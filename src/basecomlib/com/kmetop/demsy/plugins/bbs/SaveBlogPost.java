package com.kmetop.demsy.plugins.bbs;

import java.util.Date;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.sft.lybbs.LyblogPosts;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveBlogPost extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		LyblogPosts obj = (LyblogPosts) event.getEntity();

		// 回复
		if (obj.getId() != null && obj.getId() > 0) {

		} else {// 论坛
			Demsy ctx = Demsy.me();

			if (ctx.login() == null) {
				throw new DemsyException("你尚未登录，请先登录!");
			}
			obj.setCreatedIP(ctx.request().getRemoteAddr());
			obj.setCreated(new Date());
			obj.setCreatedBy(ctx.username());
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}
