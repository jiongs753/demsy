package com.kmetop.demsy.plugins.web;

import static com.kmetop.demsy.Demsy.moduleEngine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.web.IResearchOption;
import com.kmetop.demsy.comlib.web.IResearchQuestion;
import com.kmetop.demsy.comlib.web.IResearchResult;
import com.kmetop.demsy.comlib.web.IResearchSubject;
import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveResearchResult extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		IOrm orm = event.getOrm();
		Demsy me = Demsy.me();
		IResearchSubject subject = null;

//		Enumeration names = Demsy.me().request().getParameterNames();
//		while (names.hasMoreElements()) {
//			String name = (String) names.nextElement();
//			String value = Demsy.me().request().getParameter(name);
//			System.out.println(name + " = " + value);
//		}

		Class questionType = Demsy.bizEngine.getType(IResearchQuestion.SYS_CODE);
		Map<Long, IResearchQuestion> mapMustableQuestions = new HashMap();

		Object obj = event.getEntity();
		List<IResearchResult> list = new ArrayList();

		if (obj instanceof List) {
			list = (List<IResearchResult>) obj;
		} else if (obj instanceof IResearchResult) {
			list.add((IResearchResult) obj);
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			IResearchResult result = list.get(i);

			IResearchOption option = result.getOption();
			IResearchQuestion question = result.getQuestion();
			if (option != null) {
				option = (IResearchOption) orm.load(option.getClass(), option.getId());
				question = option.getQuestion();
			} else if (question != null) {
				question = (IResearchQuestion) orm.load(question.getClass(), question.getId());
			}
			if (option == null && question == null) {
				list.remove(i);
				continue;
			}

			if (subject == null) {
				subject = question.getSubject();

				List<IResearchQuestion> mustableQuestions = orm.query(questionType, Expr.eq("mustable", 1).and(Expr.eq("subject", subject)));
				for (IResearchQuestion q : mustableQuestions) {
					mapMustableQuestions.put(q.getId(), q);
				}

				// 校验：有效期
				Date from = subject.getExpiredFrom();
				Date to = subject.getExpiredTo();
				Date now = new Date();
				if ((from != null && now.getTime() < from.getTime()) || (to != null && now.getTime() > to.getTime())) {
					throw new DemsyException("调查期限从 %s 到 %s ！", Dates.formatDate(from), Dates.formatDate(to));
				}
				// 校验：登录参与
				if (subject.getEntryPolicy() == 1) {
					if (Str.isEmpty(me.username())) {
						throw new DemsyException("请先登录后再参与调查！");
					}
				}
				// 限制参与次数
				byte times = subject.getEntryTimes();
				if (times > 0) {
					int count = 0;
					if (subject.getEntryPolicy() == 1) {
						count = orm.count(result.getClass(), Expr.eq("subject", subject).and(Expr.eq(LibConst.F_CREATED_BY, me.username())));
					} else {
						count = orm.count(result.getClass(), Expr.eq("subject", subject).and(Expr.eq(LibConst.F_CREATED_IP, me.request().getRemoteAddr())));
					}
					if (count >= times) {
						throw new DemsyException("你已经参与过本次调查，不允许重复参与！");
					}
				}
			}

			byte qtype = question.getType();
			if (qtype == 2 && Str.isEmpty(result.getAnswerText())) {
				list.remove(i);
				continue;
			}

			if (option != null) {
				byte otype = option.getType();
				if (otype == 1 && Str.isEmpty(result.getAnswerText())) {
					throw new DemsyException("【%s】必须填写！", option.getName());
				}
			}
			mapMustableQuestions.remove(question.getId());

			if (option != null) {
				moduleEngine.increase(orm, option, "result");
			}
			moduleEngine.increase(orm, question, "result");
			moduleEngine.increase(orm, subject, "result");

			result.setQuestion(question);
			result.setSubject(subject);
		}

		StringBuffer sbMust = new StringBuffer();
		Iterator<Long> it = mapMustableQuestions.keySet().iterator();
		while (it.hasNext()) {
			Long key = it.next();
			IResearchQuestion q = mapMustableQuestions.get(key);
			sbMust.append("[" + q.getName() + "]");
		}
		if (sbMust.length() > 0) {
			throw new DemsyException("%s 必须选择或填写！", sbMust);
		}
	}
}
