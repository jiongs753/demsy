package com.kmetop.demsy.plugins.web;

import java.util.LinkedList;
import java.util.List;

import org.nutz.json.Json;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.web.IResearchOption;
import com.kmetop.demsy.comlib.web.IResearchQuestion;
import com.kmetop.demsy.comlib.web.IResearchResult;
import com.kmetop.demsy.comlib.web.IResearchSubject;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;

public class SaveResearchSubject extends LoadResearchSubject {

	@Override
	public void before(BizEvent event) {
		IOrm orm = event.getOrm();

		Class questionType = Demsy.bizEngine.getType(IResearchQuestion.SYS_CODE);
		Class optionType = Demsy.bizEngine.getType(IResearchOption.SYS_CODE);
		Class resultType = Demsy.bizEngine.getType(IResearchResult.SYS_CODE);

		IResearchSubject subject = (IResearchSubject) event.getEntity();
		if (Obj.getId(subject) == null) {
			orm.save(subject);
		}

		try {
			String questionsText = subject.getQuestionsJson();
			if (!Str.isEmpty(questionsText)) {
				String className = questionType.getName() + "[]";
				IResearchQuestion[] array = (IResearchQuestion[]) Json.fromJson(Cls.forName(className), questionsText);
				List<IResearchQuestion> oldQuestions = orm.query(questionType, Expr.eq("subject", subject));
				List<IResearchQuestion> questions = new LinkedList();
				for (IResearchQuestion q : array) {
					IResearchQuestion question = q;
					if (q.getId() != null) {
						question = (IResearchQuestion) orm.load(q.getClass(), q.getId());
						question.setName(q.getName());
						question.setType(q.getType());
						question.setMustable(q.getMustable());
					}
					question.setSubject(subject);
					questions.add(question);
					if (Str.isEmpty(question.getName())) {
						throw new DemsyException("标题不能为空！");
					}

					orm.save(question);

					List<IResearchOption> oldOptions = orm.query(optionType, Expr.eq("question", question));
					List<IResearchOption> options = q.getOptions();
					for (IResearchOption o : options) {
						IResearchOption option = o;
						if (o.getId() != null) {
							option = (IResearchOption) orm.load(o.getClass(), o.getId());
							option.setName(o.getName());
							option.setType(o.getType());
						}
						option.setQuestion(question);
						option.setSubject(subject);
						if (Str.isEmpty(option.getName())) {
							throw new DemsyException("标题不能为空！");
						}

						orm.save(option);
					}
					for (IResearchOption option : oldOptions) {
						if (!options.contains(option)) {
							List<IResearchResult> results = orm.query(resultType, Expr.eq("option", option));
							for (IResearchResult result : results) {
								orm.delete(result);
							}
							orm.delete(option);
						}
					}
				}
				for (IResearchQuestion question : oldQuestions) {
					if (!questions.contains(question)) {
						orm.delete(question);
					}
				}
			}
		} catch (Throwable e) {
			throw new DemsyException(e);
		}
	}

}
