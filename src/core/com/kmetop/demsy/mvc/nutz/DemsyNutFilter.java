package com.kmetop.demsy.mvc.nutz;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.security.SecurityException;

/**
 * @deprecated
 * @author yongshan.ji
 * 
 */
public class DemsyNutFilter implements Filter {

	public void init(FilterConfig conf) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Demsy me = null;
		try {
			me = Demsy.initMe(req, resp);

			if (Demsy.actionHandler.execute(req, resp)) {
				return;
			}

		} catch (SecurityException e) {
			if (e.getCode() > 0) {
				resp.setStatus(e.getCode());
			} else {
				throw new ServletException(Ex.msg(e));
			}
		} finally {
			if (me != null) {
				me.release();
			}
		}
		chain.doFilter(req, resp);
	}
}
