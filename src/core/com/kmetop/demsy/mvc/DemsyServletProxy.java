package com.kmetop.demsy.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kmetop.demsy.Const;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.plugin.Plugins;
import com.kmetop.demsy.util.AntPathMatcher;
import com.kmetop.demsy.util.PathMatcher;

@SuppressWarnings("serial")
public class DemsyServletProxy extends HttpServlet implements Filter, Const, MvcConst {
	protected static transient Log log = Logs.getLog(DemsyServletProxy.class);

	protected boolean doDemsyFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
		return false;
	}

	protected void initDemsy(ServletConfig config) throws ServletException {
		log.info("启动 DEMSY Plugin(s)...");
		Plugins.startContextPlugins();
		log.info("启动 DEMSY Plugin(s): 结束.");

		// 如下日志如 DemsyListenerProxy.contextInitialized 开头部分对应
		log.info("启动DEMSY软件: 结束. <正在运行DEMSY软件.....>");
		System.out.println("启动DEMSY软件: 结束. <正在运行DEMSY软件.....>");
	}

	public void destroyDemsy() {

	}

	/*
	 * 接口实现：兼容 SFT 的 DEMSY 代理
	 */
	static DemsyServletProxy filter;

	@SuppressWarnings("unused")
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

		if (!Const.isCompatibleSFT || !doSFTFilter((HttpServletRequest) req, (HttpServletResponse) resp, chain)) {

			if (!this.doDemsyFilter((HttpServletRequest) req, (HttpServletResponse) resp, chain)) {
				chain.doFilter(req, resp);
			}
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		if (Const.isCompatibleSFT) {
			initSFT(config);
		}

		this.initDemsy(config);
	}

	@Override
	public void destroy() {
		if (Const.isCompatibleSFT) {
			destroySFT();
		}

		this.destroyDemsy();
	}

	/*
	 * 以下是兼容 SFT 的辅助函数
	 */

	private PathMatcher matcher = new AntPathMatcher();

	private Map<String, HttpServlet> servletMap = new HashMap();

	// <servlet-name, url-pattern>
	private Map<String, String[]> servletPattern = new HashMap();

	private String[] servletNames = new String[] { "dwr", "chart", "dr", "bfapp", "rss", "ckfinder", "struts1" };

	private boolean doSFTFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
		String url = null;
		if (log.isDebugEnabled())
			url = MvcUtil.requestURL(req);

		String uri = MvcUtil.requestPath(req);
		List<HttpServlet> servlets = getSFTServlets(uri);
		int len = servlets.size();
		if (len == 0) {
			return false;
		}

		try {
			log.debugf("SFT Servlet(s) 处理请求......[%s]", url);

			if (log.isTraceEnabled()) {
				int count = 0;
				for (HttpServlet s : servlets) {
					count++;

					log.tracef("SFT Servlet<%s>处理请求...[%s]<%s/%s>...", s.getClass().getSimpleName(), url, count, len);

					s.service(req, resp);

					log.tracef("SFT Servlet<%s>处理请求: 结束. [%s]<%s/%s>...", s.getClass().getSimpleName(), url, count, len);

				}
			} else {
				for (HttpServlet s : servlets) {
					s.service(req, resp);
				}
			}

			log.debugf("SFT Servlet(s) 处理请求: 结束. [%s]", url);

		} catch (Throwable e) {

			if (log.isDebugEnabled()) {
				log.errorf("SFT Servlet(s) 处理请求: 出错! [" + url + "]", e);
			} else {
				if (url == null)
					url = MvcUtil.requestURL(req);

				log.errorf("SFT Servlet(s) 处理请求: 出错! [%s] %s", url, e);
			}

		}

		return true;
	}

	private void initSFT(ServletConfig config) throws ServletException {
		log.info("初始化 SFT Servlet(s)......");

		setupSFTServlets();
		log.debugf("加载 SFT Servlet(s) 结束. [size=%s]", servletMap.size());

		Collection<HttpServlet> ss = getSFTProxyServlets();
		for (HttpServlet s : ss) {
			if (s != null) {
				try {
					s.init(config);
					log.infof("初始化SFT Servlet(s) 结束. [%s]", s.getClass().getName());
				} catch (Throwable e) {
					log.errorf("初始化SFT Servlet(s) 出错! [%s] %s ", s.getClass().getName(), Ex.msg(e));
				}
			}
		}

		filter = this;

		log.infof("初始化SFT Servlet(s): 结束. [size=%s]", ss.size());

	}

	private void destroySFT() {

		log.info("关闭SFT Servlet(s)......");

		Collection<HttpServlet> ss = getSFTProxyServlets();
		for (HttpServlet s : ss) {
			if (s != null) {
				try {
					s.destroy();

					log.infof("关闭 SFT Servlet(s) 结束. [%s]", s.getClass().getSimpleName());
				} catch (Throwable e) {
					log.errorf("关闭 SFT Servlet(s) 出错! [%s] %s ", s.getClass().getSimpleName(), Ex.msg(e));
				}
			}
		}

		this.servletMap.clear();
		this.servletMap = null;
		this.servletNames = null;
		this.servletPattern.clear();
		this.servletPattern = null;
		this.matcher = null;

		log.info("关闭 SFT Servlet(s): 结束.");

	}

	private void setupSFTServlets() {
		HttpServlet s;

		s = loadSFTServlet("uk.ltd.getahead.dwr.DWRServlet");
		if (s != null)
			servletMap.put("dwr", s);

		s = loadSFTServlet("net.lybbs.stat.servlet.DisplayChart");
		if (s != null)
			servletMap.put("chart", s);

		s = loadSFTServlet("net.lycommon.user.servlet.DrawRandServlet");
		if (s != null)
			servletMap.put("dr", s);

		s = loadSFTServlet("net.buffalo.web.servlet.ApplicationServlet");
		// s = load("demsy.filter.BuffaloServlet");
		if (s != null)
			servletMap.put("bfapp", s);

		s = loadSFTServlet("net.lybbs.rss.servlet.FeedServlet");
		if (s != null)
			servletMap.put("rss", s);

		s = loadSFTServlet("com.ckfinder.connector.ConnectorServlet");
		if (s != null)
			servletMap.put("ckfinder", s);

		s = loadSFTServlet("demsy.filter.Struts1Servlet");
		if (s != null)
			servletMap.put("struts1", s);

		servletPattern.put("dwr", new String[] { "/dwr/**" });
		servletPattern.put("chart", new String[] { "/servlet/lybbs_displayChart" });
		servletPattern.put("dr", new String[] { "/servlet/lybbs_drawRand" });
		// servletPattern.put("bfapp", new String[] { "/servlet/bfapp/**" });
		servletPattern.put("rss", new String[] { "/servlet/lybbs_rss" });
		servletPattern.put("ckfinder", new String[] { "/ckfinder/core/connector/java/connector.java" });
		servletPattern.put("struts1", new String[] { "/**/*.do" });
	}

	private HttpServlet loadSFTServlet(String className) {
		try {
			Class<HttpServlet> cls = Cls.forName((String) className);
			return cls.newInstance();
		} catch (Throwable e) {
			log.errorf("加载 SFT Servlet 出错! [%s] %s", className, Ex.msg(e));
		}
		return null;
	}

	private List<HttpServlet> getSFTServlets(String uri) {
		List<HttpServlet> ret = new ArrayList();
		for (String name : servletNames) {
			String[] paths = this.servletPattern.get(name);
			if (paths == null) {
				continue;
			}
			for (String path : paths) {
				if (!matcher.match(path, uri)) {
					continue;
				}
				HttpServlet servlet = servletMap.get(name);

				if (servlet != null) {
					ret.add(servlet);
				}
			}
		}

		return ret;
	}

	private Collection<HttpServlet> getSFTProxyServlets() {
		return servletMap.values();
	}

}
