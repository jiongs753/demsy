package com.kmetop.demsy.mvc.view;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.ConfigException;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.ui.IUIView;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;
import com.kmetop.demsy.mvc.ui.widget.UIPageView;

public class BizView extends AbstractView {
	private static Log log = Logs.getLog(BizView.class);

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
		if (obj == null) {
			log.warn("未知业务窗体!");
			return;
		}

		Map context = this.initContext(req, resp);

		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", -1);

		Writer out = null;
		try {
			out = resp.getWriter();

			if (obj instanceof UIPageView) {
				UIPageView ui = (UIPageView) obj;
				resp.setContentType(MvcUtil.renderBizModel.getContentType(ui));
				MvcUtil.renderBizModel.render(out, ui, context);
			} else if (obj instanceof UIWidgetModel) {
				UIWidgetModel dataModel = (UIWidgetModel) obj;

				if (dataModel.isAjaxData()) {
					resp.setContentType(MvcUtil.renderBizData.getContentType(dataModel));
					MvcUtil.renderBizData.render(out, dataModel, context);
				} else {
					IUIView ui = (IUIView) obj;
					String dacorator = (String) dataModel.getDacorator();
					// 装饰器为空串——表示不使用装饰器
					if (dacorator == null || dacorator.trim().length() > 0) {
						UIPageView pageUI = Demsy.uiEngine.makePageView(dacorator);
						// pageUI.setPlaceholder(dataModel);
						pageUI.addBlock("", dataModel);
						pageUI.set("title", dataModel.getName());

						ui = pageUI;
					}

					resp.setContentType(MvcUtil.renderBizModel.getContentType(ui));
					MvcUtil.renderBizModel.render(out, ui, context);
				}
			} else if (obj instanceof ConfigException) {
				String path = ((ConfigException) obj).getRedirect();

				if (!Str.isEmpty(path))
					context.put("redirect", path);
				else
					context.put("redirect", MvcUtil.contextPath(URL_CONFIG));

				MvcUtil.renderBizModel.render(out, (Throwable) obj, context);
			} else if (obj instanceof Throwable) {
				MvcUtil.renderBizModel.render(out, (Throwable) obj, context);
			} else {
				String msg = String.format("业务视图不支持<%s>的呈现! ", obj);
				log.error(msg);

				throw new DemsyException(msg);
			}
		} finally {
			resp.flushBuffer();
			try {
				if (out != null)
					out.close();
			} catch (Throwable iglore) {
			}
		}
	}
}
