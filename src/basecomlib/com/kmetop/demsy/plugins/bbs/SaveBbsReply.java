package com.kmetop.demsy.plugins.bbs;

import java.util.Date;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.sft.lybbs.LybbsDb;
import com.kmetop.demsy.comlib.impl.sft.lybbs.LybbsPostreply;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveBbsReply extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		LybbsPostreply obj = (LybbsPostreply) event.getEntity();

		// 回复
		if (obj.getId() != null && obj.getId() > 0) {

		} else {// 论坛
			Demsy ctx = Demsy.me();
			obj.setCreatedIP(ctx.request().getRemoteAddr());
			obj.setCreated(new Date());
			LybbsDb forum = obj.getTopic().getForum();
			forum = (LybbsDb) Demsy.orm().load(LybbsDb.class, forum.getId());

			obj.setForum(forum);
			obj.setName("回贴:" + obj.getTopic().getName());

			if ((obj.getCatalog() == null || Str.isEmpty(obj.getCatalog().toString()))//
					&& (obj.getContent() == null || Str.isEmpty(obj.getContent().toString()))) {
				throw new DemsyException("回复内容不能为空!");
			}

			// 检查：正规论坛只允许登录用户发帖
			if (ctx.loginUser() != null) {
				obj.setCreatedBy(ctx.username());
			} else {
				String type = forum.getType();
				if ("yes".equals(type)) {
					throw new DemsyException("正规论坛只允许登录用户发帖, 请先登录!");
				}
			}

			// 查看方式为“指定用户”，则指定用户必须填写
			if (obj.getViewMode() == 4) {
				if (Str.isEmpty(obj.getViewUsers())) {
					throw new DemsyException("必需填写指定用户!");
				}
			}

			IOrm orm = event.getOrm();
			Demsy.moduleEngine.increase(orm, obj.getForum(), "replynumber");
			Demsy.moduleEngine.increase(orm, obj.getTopic(), "commentNum");
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}
