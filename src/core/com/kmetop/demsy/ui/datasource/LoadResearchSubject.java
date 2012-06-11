package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.web.IResearchOption;
import com.kmetop.demsy.comlib.web.IResearchQuestion;
import com.kmetop.demsy.comlib.web.IResearchResult;
import com.kmetop.demsy.comlib.web.IResearchSubject;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.security.ILogin;

public class LoadResearchSubject extends UiBaseDataSource {

	@Override
	public Object process(UIBlockContext maker) {
		Map ctx = new HashMap();

		List list = maker.query();
		IResearchSubject subject = null;
		if (list != null && list.size() > 0)
			subject = (IResearchSubject) list.get(0);
		if (subject != null) {
			IOrm orm = Demsy.orm();

			UIBlockDataModel subjectNode = maker.makeDataModel(subject);

			ILogin login = Demsy.me().login();
			byte view = subject.getViewPolicy();
			boolean allowView = false;
			switch (view) {
			case IResearchSubject.VIEW_POLICY_DISABLED:
				break;
			case IResearchSubject.VIEW_POLICY_ENABLED:
				allowView = true;
				break;
			case IResearchSubject.VIEW_POLICY_LOGIN:
				if (login != null && login.getRoleType() >= IUserRole.ROLE_LOGIN_USER) {
					allowView = true;
				}
				break;
			case IResearchSubject.VIEW_POLICY_ADMIN_USER:
				if (login != null && login.getRoleType() >= IUserRole.ROLE_ADMIN_USER) {
					allowView = true;
				}
				break;
			case IResearchSubject.VIEW_POLICY_ADMIN_ROOT:
				if (login != null && login.getRoleType() >= IUserRole.ROLE_ADMIN_ROOT) {
					allowView = true;
				}
				break;
			}
			if (allowView) {
				subjectNode.set("view", "1");
			}

			if (allowView) {
				subjectNode.set("resultSumValue", getResultSumValue(orm, subject));
			}

			// IUiPageBlock block = maker.getBlock();

			Class questionType = Demsy.bizEngine.getType(IResearchQuestion.SYS_CODE);
			Class answerType = Demsy.bizEngine.getType(IResearchOption.SYS_CODE);

			List questions = orm.query(questionType, Expr.eq("subject", subject));
			for (Object q : questions) {
				UIBlockDataModel qustionNode = maker.makeDataModel(q);
				subjectNode.addItem(qustionNode);

				List<IResearchOption> answers = orm.query(answerType, Expr.eq("question", q));
				for (IResearchOption a : answers) {
					UIBlockDataModel answerNode = maker.makeDataModel(a);
					if (allowView) {
						answerNode.set("resultPercentage", getResultPercentage(orm, a));
						answerNode.set("resultSumValue", getResultSumValue(orm, a));
					}
					qustionNode.addItem(answerNode);
				}
			}

			ctx.put("data", subjectNode);

			IModule resultMdl = moduleEngine.getModule(Demsy.me().getSoft(), bizEngine.getSystem(IResearchResult.SYS_CODE));
			ctx.put("saveUrl", MvcUtil.contextPath(MvcConst.URL_BZ_SAVE, resultMdl.getId() + ":", "c_n", Demsy.me().addToken()));
			Demsy.security.addPermission("block" + maker.getBlock().getId(), IUserRole.ROLE_ANONYMOUS, resultMdl.getId(), "c_n");
		}

		return ctx;
	}

	public Long getResultSumValue(IOrm orm, IResearchSubject subject) {
		Long times = subject.getResult();
		if (times != null)
			return times;

		Class type = Demsy.bizEngine.getType(IResearchResult.SYS_CODE);
		Long sum = 0l;
		List<IResearchResult> results = orm.query(type, Expr.eq("subject", subject).setFieldRexpr("result$", true));
		if (results != null) {
			for (IResearchResult r : results) {
				Long v = r.getResult();
				if (v == null || v <= 0) {
					v = 1l;
				}
				sum += v;
			}
		}
		return sum;
	}

	private static Long getResultSumValue(IOrm orm, IResearchQuestion question) {
		Long times = question.getResult();
		if (times != null)
			return times;

		Class type = Demsy.bizEngine.getType(IResearchResult.SYS_CODE);
		Long sum = 0l;
		List<IResearchResult> results = orm.query(type, Expr.eq("question", question).setFieldRexpr("result$", true));
		if (results != null) {
			for (IResearchResult r : results) {
				Long v = r.getResult();
				if (v == null || v <= 0) {
					v = 1l;
				}
				sum += v;
			}
		}
		return sum;
	}

	private static Long getResultSumValue(IOrm orm, IResearchOption answer) {
		Long times = answer.getResult();
		if (times != null)
			return times;

		Class type = Demsy.bizEngine.getType(IResearchResult.SYS_CODE);
		Long sum = 0l;
		List<IResearchResult> results = orm.query(type, Expr.eq("option", answer).setFieldRexpr("result$", true));
		if (results != null) {
			for (IResearchResult r : results) {
				Long v = r.getResult();
				if (v == null || v <= 0) {
					v = 1l;
				}
				sum += v;
			}
		}
		return sum;
	}

	private static String getResultPercentage(IOrm orm, IResearchOption answer) {
		double p = 0.0;
		double optionValue = getResultSumValue(orm, answer).doubleValue();
		double questionValue = getResultSumValue(orm, answer.getQuestion()).doubleValue();
		if (questionValue != 0) {
			p = optionValue / questionValue;
		}
		p = p * 100;
		return new DecimalFormat("#,##0.0").format(p);
	}
}
