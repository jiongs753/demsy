package com.kmetop.demsy.mvc.nutz;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.View;
import org.nutz.mvc.impl.processor.ViewProcessor;

import com.kmetop.demsy.lang.Ex;

public class DemsyFailProcessor extends ViewProcessor {

	@Override
	public void init(NutConfig config, ActionInfo ai) throws Throwable {
		view = evalView(config, ai, ai.getFailView());
	}

	@Override
	public void process(ActionContext ac) throws Throwable {
		Object re = ac.getMethodReturn();
		// Store object to request
		if (null != re)
			ac.getRequest().setAttribute(ViewProcessor.DEFAULT_ATTRIBUTE, re);
		Throwable err = Ex.root(ac.getError());
		if (re != null && re instanceof View) {
			((View) re).render(ac.getRequest(), ac.getResponse(), err);
		} else {
			view.render(ac.getRequest(), ac.getResponse(), null == re ? err : re);
		}
		doNext(ac);
	}
}