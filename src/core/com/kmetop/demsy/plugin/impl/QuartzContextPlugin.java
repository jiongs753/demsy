package com.kmetop.demsy.plugin.impl;

import static com.kmetop.demsy.Demsy.servletContext;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.kmetop.demsy.plugin.IContextPlugin;

public class QuartzContextPlugin implements IContextPlugin {
	public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";

	private static Scheduler scheduler = null;

	public String getName() {
		return "定时任务调度程序";
	}

	public void start() throws SchedulerException {
		if (servletContext.getAttribute(QUARTZ_FACTORY_KEY) != null) {
			throw new SchedulerException("已启动!");
		}

		StdSchedulerFactory factory = new StdSchedulerFactory();

		scheduler = factory.getScheduler();

		scheduler.start();

		servletContext.setAttribute(QUARTZ_FACTORY_KEY, factory);
	}

	public void close() throws SchedulerException {
		if (scheduler != null)
			scheduler.shutdown();
	}

	@Override
	public boolean support() {
		return servletContext != null;
	}

}
