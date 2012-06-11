package com.kmetop.demsy.biz.impl;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.security;

import java.util.ArrayList;
import java.util.List;

import org.nutz.lang.Mirror;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.IBizManager;
import com.kmetop.demsy.biz.IBizPlugin;
import com.kmetop.demsy.biz.IBizSession;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;

/**
 * 业务逻辑
 * <p>
 * 处理基础模块业务逻辑
 * 
 * @author yongshan.ji
 */
public class BizManagerImpl implements IBizManager {
	protected Log log = Logs.get();

	// 业务会话：可以切换ORM
	protected IBizSession bizSession;

	protected IModule bizModule;

	protected IBizSystem bizSystem;

	protected Class classOfEntity;

	public IBizSession bizSession() {
		return bizSession;
	}

	public IOrm orm() {
		return bizSession.orm();
	}

	BizManagerImpl(IBizSession bizSession, IModule module, IBizSystem system) throws DemsyException {
		this.bizSession = bizSession;
		this.bizModule = module;
		this.bizSystem = system;
		this.classOfEntity = bizEngine.getType(bizSystem);
	}

	BizManagerImpl(IBizSession bizSession, IBizSystem bizSystem) throws DemsyException {
		this.bizSession = bizSession;
		this.bizSystem = bizSystem;
		this.classOfEntity = bizEngine.getType(bizSystem);
	}

	BizManagerImpl(BizManagerImpl parent, IOrm orm) {
		this.bizSession = parent.bizSession.me(orm);
		this.bizModule = parent.bizModule;
		this.bizSystem = parent.bizSystem;
		this.classOfEntity = parent.classOfEntity;
	}

	@Override
	public IBizManager me(final IOrm orm) {
		class OrmBizManager extends BizManagerImpl {
			OrmBizManager() {
				super(BizManagerImpl.this, orm);
			}
		}
		return new OrmBizManager();
	}

	@Override
	public int save(Object entity, String actionID) throws DemsyException {
		this.checkPermission(entity, actionID);

		return bizSession.save(entity, this.buildCndExpr(null, actionID), loadPlugins(actionID));
	}

	@Override
	public int updateMore(Object obj, String actionID, CndExpr expr) throws DemsyException {
		this.checkPermission(obj, actionID);

		return bizSession.updateMore(obj, this.buildCndExpr(expr, actionID), loadPlugins(actionID));
	}

	public int delete(Object entity, String actionID) throws DemsyException {
		this.checkPermission(entity, actionID);

		return bizSession.delete(entity, loadPlugins(actionID));
	}

	@Override
	public int delete(Long id, String actionID) throws DemsyException {
		this.checkPermission(actionID);

		return bizSession.delete(id, loadPlugins(actionID));
	}

	@Override
	public int deleteMore(String actionID, CndExpr expr) throws DemsyException {
		this.checkPermission(actionID);

		return bizSession.deleteMore(classOfEntity, expr, loadPlugins(actionID));
	}

	@Override
	public Object load(Long id, String actionID) throws DemsyException {
		this.checkPermission(actionID);

		return bizSession.load(classOfEntity, id, this.buildCndExpr(null, actionID), loadPlugins(actionID));
	}

	public int count(String actionID, CndExpr expr) throws DemsyException {
		this.checkPermission(actionID);

		return bizSession.count(classOfEntity, this.buildCndExpr(expr, actionID), loadPlugins(actionID));
	}

	public List query(String actionID, CndExpr expr) throws DemsyException {
		this.checkPermission(actionID);

		return bizSession.query(classOfEntity, this.buildCndExpr(expr, actionID), loadPlugins(actionID));
	}

	@Override
	public List query(Pager pager, String actionID) throws DemsyException {
		this.checkPermission(actionID);

		pager.setQueryExpr(this.buildCndExpr(pager.getQueryExpr(), actionID));

		return bizSession.query(pager, loadPlugins(actionID));
	}

	@Override
	public Object run(Object obj, String actionID) throws DemsyException {
		return bizSession.run(obj, this.buildCndExpr(null, actionID), loadPlugins(actionID));
	}

	public void asynSave(Object entity, String actionID) throws DemsyException {
		this.checkPermission(entity, actionID);

		bizSession.asynSave(entity, buildCndExpr(null, actionID), loadPlugins(actionID));
	}

	@Override
	public void asynUpdateMore(Object obj, String actionID, CndExpr expr) throws DemsyException {
		this.checkPermission(obj, actionID);

		bizSession.asynUpdateMore(obj, expr, loadPlugins(actionID));
	}

	@Override
	public void asynDelete(Object obj, String actionID) throws DemsyException {
		this.checkPermission(obj, actionID);

		bizSession.asynDelete(obj, loadPlugins(actionID));
	}

	@Override
	public void asynDeleteMore(String actionID, CndExpr expr) throws DemsyException {
		this.checkPermission(actionID);

		bizSession.asynDeleteMore(classOfEntity, expr, loadPlugins(actionID));
	}

	@Override
	public void asynQuery(Pager pager, String actionID) throws DemsyException {
		this.checkPermission(actionID);

		pager.setQueryExpr(this.buildCndExpr(pager.getQueryExpr(), actionID));

		bizSession.asynQuery(pager, loadPlugins(actionID));
	}

	@Override
	public void asynRun(Object obj, String actionID) throws DemsyException {

		bizSession.asynRun(obj, buildCndExpr(null, actionID), loadPlugins(actionID));
	}

	public IModule getModule() {
		return bizModule;
	}

	public IBizSystem getSystem() {
		return bizSystem;
	}

	// ====================================================================
	// 权限控制
	// ====================================================================

	protected void checkPermission(String actionID) throws DemsyException {
	}

	protected void checkPermission(Object obj, String actionID) throws DemsyException {
		if (obj instanceof List) {
			for (Object ele : (List) obj) {
				this.checkPermission(ele, actionID);
			}
		} else {
			if (!Cls.getType(classOfEntity).isAssignableFrom(obj.getClass())) {
				throw new DemsyException("无权操作<%s>业务实体", obj.getClass().getSimpleName());
			}
			this.checkPermission(actionID);
		}
	}

	/**
	 * 创建查询条件、字段过滤、数据权限等条件表达式
	 * 
	 * @param expr
	 *            指定的条件表达式
	 * @param actionID
	 *            操作ID
	 * @return 新的条件表达式
	 */
	protected CndExpr buildCndExpr(CndExpr expr, String actionID) {
		// TODO: soft aware
		if (Demsy.security.isRootUser(Demsy.me().username())) {
			return expr;
		}

		// CndExpr softEq = Expr.eq(LibConst.F_SOFT_ID,
		// this.bizModule.getSoftID());
		// if (expr == null) {
		// return softEq;
		// }
		// return expr.and(softEq);

		CndExpr fkExpr = security.getPermissionExpr(bizModule);
		if (fkExpr != null) {
			if (expr == null)
				expr = fkExpr;
			else
				expr = expr.and(fkExpr);
		}

		return expr;
	}

	/**
	 * 加载模块业务插件
	 * 
	 * @return
	 */
	protected IBizPlugin[] loadPlugins(String actionID) {
		IAction action = Demsy.moduleEngine.getAction(bizModule, actionID);
		if (action == null)
			return null;

		String[] pluginArray = Str.toArray(action.getPlugin(), ",");
		List<IBizPlugin> plugins = new ArrayList(pluginArray.length);
		for (String pstr : pluginArray) {
			try {
				IBizPlugin plugin = (IBizPlugin) Mirror.me(Cls.forName(pstr)).born();
				plugins.add(plugin);
			} catch (Throwable e) {
				log.errorf("加载业务插件出错! [module=%s, action=%s] %s", bizModule, actionID, e);
			}
		}
		IBizPlugin[] ret = new IBizPlugin[plugins.size()];

		return (IBizPlugin[]) plugins.toArray(ret);
	}

	public Class getType() {
		return classOfEntity;
	}
}
