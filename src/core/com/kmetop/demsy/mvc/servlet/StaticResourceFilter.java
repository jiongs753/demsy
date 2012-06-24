package com.kmetop.demsy.mvc.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Files;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.mvc.MvcConst;

/**
 * @author yongshan.ji
 * 
 */
public class StaticResourceFilter implements Filter, MvcConst {

	public static String classPathForDir;

	public void init(FilterConfig conf) throws ServletException {
		classPathForDir = System.getProperty("user.dir") + File.separator + "work" + File.separator + "classes";
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", -1);

		String path = MvcUtil.requestPath(req);
		String filename = Demsy.appconfig.getClassDir() + path;
		if (!new File(filename).exists() && !Demsy.appconfig.isProductMode()) {
			filename = classPathForDir + path;
		}

		InputStream fis = Files.findFileAsStream(filename);

		if (fis == null) {
			throw new IOException("resource not existed. [" + filename + "]");
		}

		Writer out = null;
		InputStream is = null;
		try {
			out = resp.getWriter();
			is = new BufferedInputStream(fis);

			int b;
			while (-1 != (b = is.read())) {
				out.write(b);
			}

		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Throwable e) {
			}
			try {
				if (is != null)
					is.close();
			} catch (Throwable e) {
			}
			try {
				if (fis != null)
					fis.close();
			} catch (Throwable e) {
			}
			resp.flushBuffer();
		}

	}
}
