package com.kmetop.demsy.mvc;

import static com.kmetop.demsy.Demsy.actionHandler;
import static com.kmetop.demsy.Demsy.appconfig;
import static com.kmetop.demsy.Demsy.initMe;
import static com.kmetop.demsy.Demsy.initSw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kmetop.demsy.Const;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.config.IAppConfig;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.ConfigException;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.servlet.StaticResourceFilter;
import com.kmetop.demsy.security.SecurityException;
import com.kmetop.demsy.security.UnloginException;
import com.kmetop.demsy.util.AntPathMatcher;
import com.kmetop.demsy.util.PathMatcher;

public class DemsyFilterProxy implements Filter, Const, MvcConst {
	protected transient static Log log = Logs.getLog(DemsyFilterProxy.class);

	private String encoding = "UTF-8";

	private static String REXP_IGNORE_RESOURCE = "^.+\\.(ico|java|jsp|jspx|js|css|jsf|" + appconfig.get(IAppConfig.UPLOAD_FILTER) + ")$";

	private static String REXP_STATIC_RESOURCE = "^/(scripts2|themes2)/*";

	private Pattern patternIgnoreResource;

	private Pattern patternStaticResource;

	private Pattern patternUploadResource;

	private StaticResourceFilter filterStaticResource;

	protected void doPostEncoding(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws UnsupportedEncodingException {
		req.setCharacterEncoding(encoding);
	}

	protected boolean doDemsyFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, String url) throws IOException, ServletException {
		Demsy me = null;
		try {
			me = initMe(req, resp);
			log.debugf(">>>>>>BEGIN>>>>>> %s", url);

			if (me.actionInvoker() != null && actionHandler.execute(req, resp)) {
				log.debugf("===END=== %s", url);

				return true;
			}

			log.debugf("===FAIL=== [%s]", url);

		} catch (SecurityException e) {
			log.errorf("===FAIL=== [%s] %s", url, e);
		} finally {
			if (me != null)
				me.release();
		}

		return false;
	}

	protected boolean doStaticFilters(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, String uri, String url) throws IOException, ServletException {
		if (patternUploadResource.matcher(uri).find()) {
			chain.doFilter(req, resp);
			return true;
		}
		if (patternStaticResource.matcher(uri).find()) {
			if (appconfig.isProductMode()) {
				chain.doFilter(req, resp);
				return true;
			}

			List<Filter> filters = new LinkedList();
			filters.add(filterStaticResource);

			try {

				FilterInvoker fi = new FilterInvoker(req, resp, chain);
				VirtualFilterChain virtualFilterChain = new VirtualFilterChain(fi, filters);
				virtualFilterChain.doFilter(fi.getRequest(), fi.getResponse());

				return true;
			} catch (Throwable e) {
				// log.warnf("处理静态资源出错! [%s] %s", url, e);
				// return true;
			}
		}
		if (patternIgnoreResource.matcher(uri).find()) {
			try {
				chain.doFilter(req, resp);
			} catch (Throwable e) {
				log.trace(e);
			}
			return true;
		}

		return false;
	}

	protected void initDemsy(FilterConfig filterConfig) throws ServletException {
		patternIgnoreResource = Pattern.compile(REXP_IGNORE_RESOURCE, Pattern.CASE_INSENSITIVE);
		patternStaticResource = Pattern.compile(REXP_STATIC_RESOURCE, Pattern.CASE_INSENSITIVE);
		patternUploadResource = Pattern.compile("^" + appconfig.getUploadPath() + "/*", Pattern.CASE_INSENSITIVE);
		filterStaticResource = new StaticResourceFilter();
		filterStaticResource.init(filterConfig);
	}

	protected void destroyDemsy() {
		filterStaticResource.destroy();
	}

	/*
	 * 接口实现：兼容 SFT 的 DEMSY 代理
	 */

	@SuppressWarnings("unused")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		doPostEncoding(req, resp, chain);

		initSw();

		String uri = MvcUtil.requestPath(req);
		String url = null;
		if (log.isDebugEnabled())
			url = MvcUtil.requestURL(req);

		if (!this.doStaticFilters(req, resp, chain, uri, url)) {

			if (!Const.isCompatibleSFT || !this.doSFTFilter(req, resp, chain, uri, url)) {
				try {
					if (!this.doDemsyFilter(req, resp, chain, url)) {
						chain.doFilter(req, resp);
					}
				} catch (ConfigException e) {
					resp.sendRedirect(MvcUtil.contextPath(URL_CONFIG));
					resp.flushBuffer();
				} catch (UnloginException e) {
					resp.sendRedirect(MvcUtil.contextPath(URL_SEC_LOGIN_FORM));
					resp.flushBuffer();
				}
			}
		}

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.initDemsy(filterConfig);

		if (Const.isCompatibleSFT) {
			initSFTFilters(filterConfig);
		}
	}

	public void destroy() {
		if (Const.isCompatibleSFT) {
			destroySFTFilters();
		}

		this.destroyDemsy();
	}

	// ============执行DEMSY业务逻辑===============

	/*
	 * 以下是兼容 SFT 的辅助函数
	 */

	private Map<String, Filter> filterMap;

	private Map<String, String[]> filterPattern;

	private String[] filterNames;

	private PathMatcher matcher;

	private boolean doSFTFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, String uri, String url) throws IOException, ServletException {
		List<Filter> filters = getSFTFilters(uri);

		if (filters == null || filters.size() == 0) {
			return false;
		}

		try {
			log.tracef("SFT Filter(s) 处理请求......[%s]", url);

			FilterInvoker fi = new FilterInvoker(req, resp, chain);
			VirtualFilterChain virtualFilterChain = new VirtualFilterChain(fi, filters);
			virtualFilterChain.doFilter(fi.getRequest(), fi.getResponse());

			log.tracef("SFT Filter(s)  处理请求: 结束. [%s]", url);
		} catch (Throwable e) {

			if (log.isDebugEnabled()) {
				log.errorf("SFT Filter(s) 处理请求: 出错! [" + url + "]", e);
			} else {
				if (url == null)
					url = MvcUtil.requestURL(req);

				log.errorf("SFT Filter(s) 处理请求: 出错! [%s]%s", url, e);
			}

			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Ex.msg(e));
		}

		return true;
	}

	private void initSFTFilters(FilterConfig fcfg) {
		log.info("初始化 SFT Filter(s)......");

		filterMap = new HashMap();
		filterPattern = new HashMap();
		filterNames = new String[] { "Encode", "SF", "IP", "URL", "Security", "Log", "CleanUp", "Page", "Struts2", "Servlet" };
		matcher = new AntPathMatcher();

		setupSFTFilters();
		log.debugf("加载 SF TFilter(s) 结束.[size=%s]", filterMap.size());

		Collection<Filter> filters = getSFTProxyFilters();
		for (Filter f : filters) {
			if (f != null) {
				try {

					f.init(fcfg);
					log.infof("初始化 SFT Filter 结束. [%s]", f.getClass().getSimpleName());

				} catch (Throwable e) {
					log.errorf("初始化 SFT Filter 出错! [%s] %s", f.getClass().getSimpleName(), Ex.msg(e));
				}
			}
		}

		log.infof("初始化 SFT Filter(s): 结束. [size=%s]", filters.size());
	}

	private void destroySFTFilters() {
		log.info("关闭 SFT Filter(s)......");

		Collection<Filter> filters = getSFTProxyFilters();

		for (Filter f : filters) {
			if (f != null) {
				try {
					f.destroy();
					log.infof("关闭 SFT Filter 结束. [%s]", f.getClass().getSimpleName());
				} catch (Throwable e) {
					log.errorf("关闭 SFT Filter 出错! [%s] %s ", f.getClass().getSimpleName(), Ex.msg(e));
				}
			}
		}

		this.filterMap.clear();
		this.filterMap = null;
		this.filterNames = null;
		this.filterPattern.clear();
		this.filterPattern = null;
		this.matcher = null;

		log.info("关闭 SFT Filter(s): 结束.");
	}

	private void setupSFTFilters() {
		// filterMap.put("JSI",
		// load("org.xidea.jsi.servlet.ClassPathResourceJSIFilter"));DemsyNutFilter
		filterMap.put("IP", loadSFTFilter("demsy.filter.IPFilter"));
		filterMap.put("Encode", loadSFTFilter("demsy.filter.EncodingFilter"));
		filterMap.put("SF", loadSFTFilter("demsy.filter.DemsySessionFilter"));
		// filterMap.put("Perf", load("demsy.filter.DemsyPerformanceFilter"));
		filterMap.put("Log", loadSFTFilter("demsy.filter.SFTServletContextListener"));
		filterMap.put("CleanUp", loadSFTFilter("demsy.filter.Struts2ClearFilter"));
		filterMap.put("Page", loadSFTFilter("demsy.filter.DemsyPageFilter"));
		filterMap.put("Struts2", loadSFTFilter("demsy.filter.Struts2Filter"));
		filterMap.put("Security", loadSFTFilter("demsy.filter.SecurityFilterProxy"));
		filterMap.put("URL", loadSFTFilter("demsy.filter.DemsyUrlRewriteFilter"));

		filterPattern.put("Encode", new String[] { "/**/*.action", "/**/*.do", "/**/*.jsp" });
		filterPattern.put("SF", new String[] { "/**/*.action", "/**/*.do", "/**/*.jsp", "/sft/admin/**", "/demsy/admin/**" });
		filterPattern.put("IP", new String[] { "/**/*.action", "/**/*.do" });
		// filterPattern.put("Perf", new String[] { "/**/*.action" });
		filterPattern.put("Log", new String[] { "/**/*.action" });
		filterPattern.put("CleanUp", new String[] { "/**/*.action", "/**/*.do" });
		filterPattern.put("Page", new String[] { "/**/*.action", "/**/*.do" });
		filterPattern.put("Struts2", new String[] { "/**/*.action", "/**/*.do" });
		filterPattern.put("Security", new String[] { "/sft/admin/**", "/demsy/admin/**" });
		filterPattern.put("URL", new String[] { "/**/*.html_demsy", "/**/*.html", "/blog/**", "/bbs/blog/**" });
		filterPattern.put("Servlet", new String[] { "/**/*.nut", "/dwr/**", "/servlet/lybbs_displayChart", "/servlet/lybbs_drawRand", "/servlet/bfapp/**", "/servlet/lybbs_rss",
				"/fckeditor/editor/filemanager/connectors/**", "/**/*.do" });
	}

	private Filter loadSFTFilter(String className) {
		try {
			Class<Filter> cls = Cls.forName((String) className);
			return cls.newInstance();
		} catch (Throwable e) {
			log.errorf("加载 SFT Filter 出错! [%s] %s", className, Ex.msg(e));
		}
		return null;
	}

	private List<Filter> getSFTFilters(String uri) {
		List<Filter> ret = new ArrayList();
		for (String name : filterNames) {
			String[] paths = this.filterPattern.get(name);
			if (paths == null) {
				continue;
			}
			for (String path : paths) {
				if (!matcher.match(path, uri)) {
					continue;
				}

				Filter filter = filterMap.get(name);

				if (filter != null) {
					ret.add(filter);
				} else {
					if (name.equals("Servlet")) {
						filterMap.put(name, DemsyServletProxy.filter);
						ret.add(DemsyServletProxy.filter);
					}
				}

				break;
			}
		}

		return ret;
	}

	protected Collection<Filter> getSFTProxyFilters() {
		return filterMap.values();
	}

	private static class VirtualFilterChain implements FilterChain {
		private FilterInvoker fi;

		private List<Filter> additionalFilters;

		private int currentPosition = 0;

		private int len = 0;

		private VirtualFilterChain(FilterInvoker filterInvocation, List<Filter> additionalFilters) {
			this.fi = filterInvocation;
			this.additionalFilters = additionalFilters;
			this.len = additionalFilters.size();
		}

		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			if (currentPosition == len) {
				fi.getChain().doFilter(request, response);
			} else {
				Filter nextFilter = additionalFilters.get(currentPosition++);

				if (log.isTraceEnabled()) {

					String url = MvcUtil.requestURL((HttpServletRequest) request);

					log.tracef("Filter<%s>处理请求...[%s]<%s/%s>...", nextFilter.getClass().getSimpleName(), url, currentPosition, len);

					nextFilter.doFilter(request, response, this);

					log.tracef("Filter<%s>处理请求: 结束. [%s]<%s/%s>...", nextFilter.getClass().getSimpleName(), url, currentPosition, len);

				} else {
					nextFilter.doFilter(request, response, this);
				}
			}
		}
	}

	private static class FilterInvoker {

		private FilterChain chain;

		private HttpServletRequest request;

		private HttpServletResponse response;

		private FilterInvoker(ServletRequest request, ServletResponse response, FilterChain chain) {
			if ((request == null) || (response == null) || (chain == null)) {
				throw new IllegalArgumentException("参数值不能为 null");
			}

			if (!(request instanceof HttpServletRequest)) {
				throw new IllegalArgumentException("参数值只能为HttpServletRequest对象");
			}

			if (!(response instanceof HttpServletResponse)) {
				throw new IllegalArgumentException("参数值只能为HttpServletResponse对象");
			}

			this.request = (HttpServletRequest) request;
			this.response = (HttpServletResponse) response;
			this.chain = chain;
		}

		public FilterChain getChain() {
			return chain;
		}

		public ServletRequest getRequest() {
			return request;
		}

		public ServletResponse getResponse() {
			return response;
		}
	}
}
