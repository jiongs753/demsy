package com.kmetop.demsy.mvc.view;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FreemarkerView extends AbstractView {

	public FreemarkerView(String path) {
		super(path, TPL_FTL);
	}

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
		Map ctx = this.initContext(req, resp);

		resp.setHeader("Cache-Control", "no-cache");
		resp.setContentType("text/plain");

		Writer out = null;
		try {
			out = resp.getWriter();
			MvcUtil.tplEngineFTL.render(path, ctx, out);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Throwable iglore) {
			}
			resp.flushBuffer();
		}
	}

}
