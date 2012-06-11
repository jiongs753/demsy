package com.kmetop.demsy.modules.sms;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

/**
 * 定时接收短信
 * 
 * @author yongshan.ji
 */
public class ReceiveSmsJob implements Job {
	public static Log log = Logs.getLog(ReceiveSmsJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debugf("接收短信......");
		try {
			ISmsEngineFactory factory = Demsy.bean("smsEngineFactory");
			ISmsEngine engine = factory.getSmsEngine();

			log.debugf("接收短信: 短信引擎[%s]", engine.getClass().getName());

			engine.receiveSms();

			log.debugf("接收短信: 结束.");
		} catch (Throwable e) {
			log.errorf("接收短信出错! %s", Ex.msg(e));
		}
	}

}
