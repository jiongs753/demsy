package com.kmetop.demsy.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

import com.kmetop.demsy.Const;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class DemsyListenerProxy implements ServletContextListener, HttpSessionActivationListener {
	public static final Log log = Logs.getLog(DemsyListenerProxy.class);

	protected void demsyContextInitialized(ServletContextEvent event) {
		Demsy.initSw();

		// 如下日志如 DemsyServletProxy.init 结束分对应
		System.out.println("启动DEMSY软件.....");
		log.info("启动DEMSY软件.....");

		Demsy.init(event.getServletContext());
	}

	protected void demsyContextDestroyed(ServletContextEvent event) {
		Demsy.close();
	}

	/*
	 * 接口实现：兼容 SFT 的 DEMSY 代理
	 */

	@Override
	public void contextInitialized(ServletContextEvent event) {
		this.demsyContextInitialized(event);

		if (Const.isCompatibleSFT)
			initedSFTContext(event);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		this.demsyContextDestroyed(event);

		if (Const.isCompatibleSFT)
			destroyedSFTContext(event);
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent event) {

		if (Const.isCompatibleSFT)
			activateSFTSession(event);
	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent event) {

		if (Const.isCompatibleSFT)
			passivateSFTSession(event);
	}

	/*
	 * 以下是兼容 SFT 的辅助函数
	 */

	private static List<ServletContextListener> contextListeners = new ArrayList();

	private static List<HttpSessionActivationListener> sessionListeners = new ArrayList();

	private void activateSFTSession(HttpSessionEvent event) {
		for (HttpSessionActivationListener l : sessionListeners) {
			l.sessionDidActivate(event);
		}
	}

	private void passivateSFTSession(HttpSessionEvent event) {
		for (HttpSessionActivationListener l : sessionListeners) {
			l.sessionWillPassivate(event);
		}
	}

	private void destroyedSFTContext(ServletContextEvent event) {
		log.info("关闭  SFT Listener(s)......");

		for (ServletContextListener l : contextListeners) {
			try {
				l.contextDestroyed(event);
				log.infof("关闭  SFT Listener 结束. [%s]", l.getClass().getSimpleName());
			} catch (Throwable e) {
				log.errorf("关闭  SFT Listener 出错! [%s] %s", l.getClass().getSimpleName(), Ex.msg(e));
			}
		}

		contextListeners.clear();
		contextListeners = null;
		sessionListeners.clear();
		sessionListeners = null;

		log.info("关闭  SFT Listener(s): 结束.");
	}

	private void initedSFTContext(ServletContextEvent event) {
		log.infof("初始化  SFT Listener(s)......");

		setupSFTListeners(event.getServletContext());
		log.debugf("加载  SFT Listener(s)结束.[size=%s]", contextListeners.size());

		for (ServletContextListener l : contextListeners) {
			try {
				if (l != null) {
					l.contextInitialized(event);
					log.infof("初始化   SFT Listener 结束. [%s]", l.getClass().getSimpleName());
				}
			} catch (Throwable e) {
				log.errorf("初始化  SFT Listener 出错! [%s] %s", l.getClass().getSimpleName(), Ex.msg(e));
			}
		}

		log.infof("初始化  SFT Listener(s): 结束. [size=%s]", contextListeners.size());
	}

	private void setupSFTListeners(ServletContext context) {
		// 加载 ServletContextListener...

		// contextListeners.add(load("demsy.filter.DemsyLog4jConfigListener"));
		contextListeners.add(loadSFTContextListener(context, "demsy.filter.SFTServletContextListener"));
		contextListeners.add(loadSFTContextListener(context, "demsy.filter.DemsyContextLoaderListener"));
		contextListeners.add(loadSFTContextListener(context, "org.springframework.web.util.IntrospectorCleanupListener"));
		contextListeners.add(loadSFTContextListener(context, "net.lybbs.common.servlet.ContextLoaderListener"));

		// 加载 HttpSessionActivationListener...
		sessionListeners.add(loadSFTSessionListener(context, "demsy.filter.SFTServletContextListener"));
	}

	private ServletContextListener loadSFTContextListener(ServletContext context, String className) {
		try {
			Class<ServletContextListener> cls = Cls.forName((String) className);
			return cls.newInstance();
		} catch (Throwable e) {
			log.errorf("加载  SFT Listener 出错! [%s] %s", className, Ex.msg(e));
		}
		return null;
	}

	private HttpSessionActivationListener loadSFTSessionListener(ServletContext context, String className) {
		try {
			Class<HttpSessionActivationListener> cls = Cls.forName((String) className);
			return cls.newInstance();
		} catch (Throwable e) {
			log.errorf("加载   SFT Listener 出错! [%s] %s", className, e);
		}
		return null;
	}

}
