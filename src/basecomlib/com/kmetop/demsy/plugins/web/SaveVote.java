package com.kmetop.demsy.plugins.web;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.base.web.Vote;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveVote extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		Vote vote = (Vote) event.getEntity();

		// 回复
		if (vote.getId() != null && vote.getId() > 0) {

		} else {// 评论
			IOrm orm = event.getOrm();

			IModule module = Demsy.moduleEngine.getModule(vote.getModuleID());
			Object subject = orm.load(Demsy.bizEngine.getType(Demsy.moduleEngine.getSystem(module)), vote.getSubjectID());
			// vote.setName(subject.toString());
			// IUser user = Demsy.me().loginUser();

			if (vote.getType() == 1)
				Demsy.moduleEngine.increase(orm, subject, "voteAgreeNum");
			else
				Demsy.moduleEngine.increase(orm, subject, "voteOpposeNum");
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}
