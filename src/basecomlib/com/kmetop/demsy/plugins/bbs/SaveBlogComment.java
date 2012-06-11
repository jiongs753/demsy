package com.kmetop.demsy.plugins.bbs;

import java.util.Date;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.sft.lybbs.LyblogComment;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveBlogComment extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		LyblogComment obj = (LyblogComment) event.getEntity();

		// 回复
		if (obj.getId() != null && obj.getId() > 0) {

		} else {// 论坛
			Demsy ctx = Demsy.me();
			obj.setCreatedIP(ctx.request().getRemoteAddr());
			obj.setCreated(new Date());

			IOrm orm = event.getOrm();
			Demsy.moduleEngine.increase(orm, obj.getPost(), "commentNum");
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}
