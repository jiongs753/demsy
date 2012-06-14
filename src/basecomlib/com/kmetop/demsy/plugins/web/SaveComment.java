package com.kmetop.demsy.plugins.web;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.sft.web.content.Comment;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveComment extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		Comment comment = (Comment) event.getEntity();

		// 回复
		if (comment.getId() != null && comment.getId() > 0) {

		} else {// 评论
			IOrm orm = event.getOrm();

			Object subject = orm.load(Demsy.bizEngine.getType(Demsy.moduleEngine.getSystem(comment.getModule())), comment.getSubjectID());
			comment.setName(subject.toString());
			IUser user = Demsy.me().loginUser();
			if (user != null) {
				if (!Str.isEmpty(user.getName()))
					comment.setCommenter(user.getName());
				else
					comment.setCommenter(user.getCode());
			}

			Demsy.moduleEngine.increase(orm, subject, "commentNum");
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}
