package com.kmetop.demsy.biz.impl;

import static com.kmetop.demsy.Demsy.*;
import static com.kmetop.demsy.Demsy.moduleEngine;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.IBizManager;
import com.kmetop.demsy.biz.IBizManagerFactory;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.config.IDataSource;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

/**
 * 业务管理器工厂：用来创建业务管理器
 * 
 * @author yongshan.ji
 */
public class BizManagerFactory implements IBizManagerFactory {
	protected static Log log = Logs.getLog(BizManagerFactory.class);

	@Override
	public <X> IBizManager<X> getManager(String moduleID) throws DemsyException {
		log.debugf("获取业务管理器......[moduleID=%s]", moduleID);

		IModule module = null;
		try {
			module = moduleEngine.getModule(Long.parseLong(moduleID));
		} catch (Throwable e) {
			module = moduleEngine.getModule(moduleID);
		}

		if (module != null && module.getType() == IModule.TYPE_BIZ) {
			return getManager(module);
		} else
			throw new DemsyException("业务模块不存在! [moduleID=%s]", moduleID);
	}

	@Override
	public <X> IBizManager<X> getManager(IModule module) throws DemsyException {
		if (module == null) {
			return null;
		}
		if (module.getType() != IModule.TYPE_BIZ)
			throw new DemsyException("业务模块不存在! [module=%s]", module);

		IBizSystem system = moduleEngine.getSystem(module);

		if (!security.visit(module, false)) {
			throw new DemsyException("无权执行该操作!");
		}

		IDataSource ds = module.getDataSource();
		if (ds != null && !module.isBuildin()) {
			return new BizManagerImpl(bizSession.me(Demsy.orm(ds)), module, system);
		} else {
			return new BizManagerImpl(bizSession, module, system);
		}
	}

}
