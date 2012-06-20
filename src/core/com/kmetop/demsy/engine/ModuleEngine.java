package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.appconfig;
import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.security;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZSYS;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_AUTO_MAKED_UPDATE_MENUS;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_SAVE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_DATASOURCE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_ACTION;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_SOFT;
import static com.kmetop.demsy.comlib.LibConst.F_BUILDIN;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Sqls;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.IModuleEngine;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizAction;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IBizEntity;
import com.kmetop.demsy.comlib.entity.IBizComponent;
import com.kmetop.demsy.comlib.entity.IDemsyCorp;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IRealm;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.config.IDataSource;
import com.kmetop.demsy.lang.Assert;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Option;
import com.kmetop.demsy.lang.Nodes.Node;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.nutz.EnMappingImpl;
import com.kmetop.demsy.orm.nutz.impl.OrmImpl;
import com.kmetop.demsy.util.sort.SortUtils;

public abstract class ModuleEngine implements IModuleEngine {
	protected Log log = Logs.getLog(this.getClass());

	protected Map<String, CacheCorp> corpCache;

	protected Map<String, CacheSoft> softCache;

	protected Map<Long, CacheSoft> softIdCache;

	protected Map<Long, IAction> actionLibCache;

	protected Map<Long, IDataSource> dataSourceCache;

	public ModuleEngine() {
		corpCache = new HashMap();
		softCache = new HashMap();
		softIdCache = new HashMap();
		actionLibCache = new HashMap();
	}

	public void clearCache() {
		synchronized (ModuleEngine.class) {
			corpCache.clear();
			softCache.clear();
			softIdCache.clear();
			actionLibCache.clear();
			dataSourceCache = null;
		}
	}

	// protected IOrm orm() {
	// return Demsy.orm();
	// }

	@Override
	public IBizSystem getSystem(IModule module) {
		if (module != null && module.getType() == IModule.TYPE_BIZ) {
			return bizEngine.getSystem(module.getRefID());
		}

		return null;
	}

	@Override
	public IDemsyCorp getCorpByDefault() {
		return getCorp(appconfig.getDefaultCorpCode());
	}

	@Override
	public IDemsyCorp getCorp(String code) {
		return corp(code).get();
	}

	CacheCorp corp(String code) {
		CacheCorp corp = corpCache.get(code);
		if (corp == null)
			corp = new CacheCorp(this, code);

		return corp;
	}

	@Override
	public IDemsySoft getSoftByDefault() {
		return soft().get();
	}

	@Override
	public IDemsySoft getSoft(String domainOrCode) {
		if (Str.isEmpty(domainOrCode)) {
			domainOrCode = appconfig.getDefaultSoftCode();
		}
		return soft(domainOrCode).get();
	}

	@Override
	public IDemsySoft getSoft(Long id) {
		return soft(id).get();
	}

	protected CacheSoft soft() {
		return soft(appconfig.getDefaultSoftCode());
	}

	protected CacheSoft soft(String domainOrCode) {
		CacheSoft ret = softCache.get(domainOrCode);
		if (ret == null)
			ret = new CacheSoft(this, domainOrCode);

		return ret;
	}

	protected CacheSoft soft(Long id) {
		CacheSoft ret = softIdCache.get(id);
		if (ret == null)
			ret = new CacheSoft(this, id);

		return ret;
	}

	public ISoftConfig getSoftConfig(String key) {
		return soft().configs().get(key);
	}

	@Override
	public IModule getModule(Long moduleID) {
		return soft().module(moduleID);
	}

	@Override
	public IModule getModule(String moduleGuid) {
		return soft().module(moduleGuid);
	}

	@Override
	public IModule getModule(IDemsySoft soft, IBizSystem system) {
		if (soft == null) {
			soft = Demsy.me().getSoft();
		}
		return this.getModule(soft.getId(), system);
	}

	public IModule getModule(Long soft, IBizSystem system) {
		if (system == null)
			return null;
		return soft(soft).bizModule(system.getId());
	}

	@Override
	public List<IModule> getSubModules(IModule module) {
		List<IModule> modules = new LinkedList();

		List<? extends IBizSystem> slaveSystems = bizEngine.getSystemsOfSlave(getSystem(module));
		for (IBizSystem sys : slaveSystems) {
			IModule slaveModule = this.getModule(module.getSoftID(), sys);
			if (slaveModule != null) {
				modules.add(slaveModule);
			}
		}

		return modules;
	}

	@Override
	public IAction getAction(IModule mdl, Serializable opID) {
		if (mdl.getType() == IModule.TYPE_BIZ) {
			if (opID instanceof String)
				return ((BizEngine) bizEngine).biz(getSystem(mdl).getId()).action((String) opID);
			else if (opID instanceof Number)
				return ((BizEngine) bizEngine).biz(getSystem(mdl).getId()).action(((Number) opID).longValue());
			else
				return null;
		}

		return null;

	}

	@Override
	public List<? extends IModule> getModules(IDemsySoft soft) {
		return soft(soft.getId()).modules();
	}

	@Override
	public Nodes makeNodesByModule(IDemsySoft soft) {
		return this.makeNodesByModule(soft, Demsy.me().login().getRoleType());
	}

	@Override
	public Nodes makeNodesByModule(IDemsySoft soft, byte role) {
		Assert.notNull(soft, "没有指定应用软件，不能获取模块功能菜单!");

		List<? extends IModule> modules = soft(soft.getId()).modules();

		Nodes root = Nodes.make();
		for (IModule module : modules) {
			if (module.isDisabled() || module.isHidden())
				continue;

			if (!security.visit(module, true))
				continue;

			makeNode(root, module);
		}

		SortUtils.sort(root.getChildren(), "order", true);

		// 非超级用户不能访问“平台管理”功能
		if (role != IUserRole.ROLE_DEVELOPER) {
			List<Node> list = root.getChildren();
			for (int i = list.size() - 1; i >= 0; i--) {
				Node node = list.get(i);
				String code = (String) node.getString("code");
				if (code != null)
					code = code.trim();
				if (LibConst.BIZCATA_DEMSY_ADMIN.equals(code)) {
					list.remove(i);
				}
			}

			root.optimize();
		}

		return root;
	}

	private void makeNode(Nodes root, IModule module) {
		if (module.isDisabled() || module.isHidden())
			return;

		Node node;
		if (module.getParent() == null) {
			node = root.addNode(null, module.getId());
		} else {
			makeNode(root, (IModule) module.getParent());

			node = root.addNode(((IBizComponent) module.getParent()).getId(), module.getId());
		}
		// node.setParams(module);
		node.setOrder(module.getOrderby());
		node.set("code", module.getCode());
		node.set("module", module);

		node.setName(module.getName());
		switch (module.getType()) {
		case IModule.TYPE_FOLDER:
			break;
		case IModule.TYPE_BIZ:
			node.setParams(MvcUtil.contextPath(MvcConst.URL_BZMAIN, module.getId()));
			break;
		case IModule.TYPE_STATIC:
			node.setParams(MvcUtil.contextPath(module.getPath(), module.getId()));
			break;
		case IModule.TYPE_WEB:
			break;
		}
	}

	@Override
	public Nodes makeNodesByAction(IModule mdl) {
		if (mdl.getType() == IModule.TYPE_BIZ) {
			IBizSystem sys = getSystem(mdl);
			Nodes root = makeActionNodes(mdl, sys);

			// List<Node> list = root.getChildren();
			// 移除从系统的新增操作
			// for (int i = list.size() - 1; i >= 0; i--) {
			// Node node = list.get(i);
			// if (node.getType().equals(TYPE_BZ_NEW) && bizEngine.isSlave(sys))
			// {
			// list.remove(i);
			// }
			// }

			Long softID = mdl.getSoftID();
			List<? extends IBizField> fieldsOfSlave = bizEngine.getFieldsOfSlave(sys);

			// 添加子系统“新增”操作到主系统操作菜单中
			// if (fields.size() > 0) {
			// for (IBizField f : fields) {
			// String param = bizEngine.getPropName(f);
			// if
			// (bizEngine.getType(f).equals(bizEngine.getType(f.getRefrenceSystem())))
			// {
			// param += ".id";
			// }
			//
			// IBizSystem subsys = f.getSystem();
			// IModule submdl = this.getModule(softID, subsys);
			// if (submdl == null) {
			// log.warnf("业务模块不存在! [system=%s]", subsys);
			// continue;
			// }
			//
			// Nodes submdlRoot = makeActionNodes(submdl, subsys);
			// List<Node> nodes = submdlRoot.getChildren();
			// for (Node node : nodes) {
			// if (node.getType().equals(TYPE_BZFORM_NEW)) {
			// node.setParams(param);
			// node.set("masterModuleID", mdl.getId());
			// root.addChild(node);
			// }
			// }
			// }
			// }

			List<Node> children = root.getChildren();

			// 自动生成字段字段批量修改菜单
			List<Node> items = this.filterNodes(children, TYPE_BZ_AUTO_MAKED_UPDATE_MENUS);// 过滤自动生成菜单项
			if (items.size() > 0) {
				Map<String, IBizField> fldsMap = bizEngine.getFieldsMap(bizEngine.getFieldsOfNavi(sys));
				Node unknownItem = null;

				List<String> props = new LinkedList();
				for (Node item : items) {
					if (Str.isEmpty((String) item.getParams())) {
						if (unknownItem != null) {
							children.remove(item);
						} else {
							unknownItem = item;
						}
						continue;
					}
					List<String> propNames = Str.toList((String) item.getParams(), ",");
					for (String prop : propNames) {
						if (props.contains(prop) || prop.equals(F_BUILDIN)) {
							continue;
						}
						props.add(prop);

						makeUpdateMenu(mdl, fldsMap.get(prop), root, item);

						fldsMap.remove(prop);
					}
					if (item.getSize() == 0) {
						children.remove(item);
					}
				}

				if (unknownItem != null) {
					int count = 1;

					Iterator<IBizField> it = fldsMap.values().iterator();
					while (it.hasNext()) {
						IBizField fld = it.next();

						if (fld.getPropName().equals(F_BUILDIN)) {
							continue;
						}

						Node item = root.addNode(unknownItem.getId(), unknownItem.getId() + "_" + (count++)).setName(unknownItem.getName() + fld.getName());
						item.set("mode", unknownItem.getString("mode"));

						if (!bizEngine.isSystemFK(fld)) {
							makeUpdateMenu(mdl, fld, root, item);
						}

						if (item.getSize() == 0) {
							unknownItem.getChildren().remove(item);
						}
					}
					if (unknownItem.getSize() == 1) {
						int index = children.indexOf(unknownItem);
						children.add(index, unknownItem.getChildren().get(0));
						children.remove(unknownItem);
					}
					if (unknownItem.getSize() == 0) {
						children.remove(unknownItem);
					}
				}
			}

			// 合并“新增”菜单组
			// items = this.filterNodes(children, TYPE_BZFORM_NEW);// 过滤新增菜单项
			// if (items.size() > 1 && children.size() > 20) {
			// List<Node> newnodes = root.getChildren();
			// Node newGroup = root.addNode(null, "new",
			// 0).setName("新增").setIcon(items.get(0).getIcon());
			// newnodes = newGroup.getChildren();
			// for (Node item : items) {
			// children.remove(item);
			// newnodes.add(item);
			// }
			// }

			// 生成子系统菜单组
			if (fieldsOfSlave.size() > 0) {
				String groupsSubMdlID = null;
				if (fieldsOfSlave.size() + root.getSize() > 20) {
					groupsSubMdlID = "grpssubmdl_" + mdl.getId();
					root.addNode(null, groupsSubMdlID).setName("明细数据");
				}
				for (IBizField f : fieldsOfSlave) {
					String param = bizEngine.getPropName(f) + ".id";

					IBizSystem subsys = f.getSystem();
					IModule submdl = this.getModule(softID, subsys);
					if (submdl == null || !security.visit(submdl, true)) {
						log.warnf("业务模块不存在! [system=%s]", subsys);
						continue;
					}

					// 子模块菜单组
					String groupSubMdlID = "grpsubmdl_" + submdl.getId();
					Node groupSubMdl = root.addNode(groupsSubMdlID, groupSubMdlID).setName(submdl.getName());

					// 子模块菜单项——“添加”
					Nodes submdlRoot = makeActionNodes(submdl, subsys);
					List<Node> subnodes = submdlRoot.getChildren();
					for (Node node : subnodes) {
						if (node.getType().equals(TYPE_BZFORM_NEW)) {
							node.setParams(param);
							node.set("masterModuleID", mdl.getId());
							groupSubMdl.getChildren().add(node);
						}
					}

					// 子模块菜单项——“查看”
					String nodeIdViewSubMdl = "viewsubmdl_" + submdl.getId();
					Node nodeViewSubMdl = root.addNode(groupSubMdlID, nodeIdViewSubMdl);
					nodeViewSubMdl.set("moduleID", submdl.getId());
					nodeViewSubMdl.set("masterModuleID", mdl.getId());
					nodeViewSubMdl.setName("查看明细");
					nodeViewSubMdl.setType(TYPE_BZSYS);
					nodeViewSubMdl.setParams(param);
				}
			}

			return root;
		}

		throw new java.lang.UnsupportedOperationException("不支持的模块类型!");
	}

	private void makeUpdateMenu(IModule mdl, IBizField fld, Nodes root, Node item) {
		String prop = bizEngine.getPropName(fld);
		if (fld == null) {
			return;
		}
		if (bizEngine.isSystemFK(fld)) {
			try {
				Class klass = bizEngine.getGenericType(fld);
				IBizSystem refSys = fld.getRefrenceSystem();
				if (!Cls.isEntityType(klass)) {
					klass = bizEngine.getType(refSys);
				}
				IOrm orm = Demsy.orm();
				Class type = bizEngine.getType(refSys);

				if (orm.count(type, null) < 10) {
					List<? extends IBizEntity> datas = orm.query(type, Cls.hasField(klass, F_ORDER_BY) ? CndExpr.asc(F_ORDER_BY) : null);
					for (IBizEntity data : datas) {
						Node node = root.addNode(item.getId(), item.getId() + "_" + fld.getId() + "_" + data.getId());
						node.setName(data.toString());
						node.setType(TYPE_BZ_SAVE);
						node.setParams(prop + ".id=" + data.getId());
						node.set("moduleID", mdl.getId());
						node.set("mode", item.getString("mode"));
					}
				}

			} catch (DemsyException e) {
				log.errorf("自动生成修改菜单出错! [%s(%s)] %s", fld.getName(), prop, e);
			}
		} else {
			String post = bizEngine.isV1Dic(fld) ? ".id" : "";
			Option[] options = bizEngine.getOptions(fld);
			for (Option option : options) {
				Node node = root.addNode(item.getId(), item.getId() + "_" + fld.getId() + "_" + option.getValue());
				node.setName(option.getText());
				node.setType(TYPE_BZ_SAVE);
				node.setParams(prop + post + "=" + option.getValue());
				node.set("moduleID", mdl.getId());
				node.set("mode", item.getString("mode"));
			}
		}
	}

	private List<Node> filterNodes(List<Node> list, int type) {
		List<Node> items = new LinkedList();
		for (Node node : list) {
			if (node.getType() != null && node.getType().equals(type)) {
				items.add(node);
			}
		}
		return items;
	}

	protected String makeParent(String parent, Long id) {
		if (Str.isEmpty(parent)) {
			return id + ".";
		}

		if (parent.endsWith("."))
			return parent + id + ".";
		else
			return parent + "." + id + ".";
	}

	public Nodes makeNodesByCurrentSoft() {

		Nodes root = Nodes.make();
		try {
			List<? extends IDemsySoft> list = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_DEMSY_SOFT));

			for (IDemsySoft ele : list) {
				IDemsyCorp corp = ele.getCorp();
				if (corp == null)
					continue;

				Node corpNode = root.addNode(null, "corp_" + corp.getId());
				corpNode.setName(corp.getName());

				Node softNode = root.addNode(corpNode.getId(), ele.getId());
				softNode.setName(ele.getName()).setParams(ele);
			}
		} catch (Throwable e) {
			log.warn(e);
		}
		return root;
	}

	public Nodes makeNodesByRealm(IDemsySoft soft) {
		return makeComNodes(this.getRealms(soft));
	}

	public Nodes makeComNodes(List<? extends IBizComponent> list) {
		Nodes root = Nodes.make();

		if (list != null)
			for (IBizComponent ele : list) {
				Node node = root.addNode(null, ele.getId());
				node.setName(ele.getName()).setParams(ele.getCode());
			}

		return root;
	}

	private Nodes makeActionNodes(IModule module, IBizSystem system) {
		List<? extends IBizAction> list = ((BizEngine) bizEngine).biz(system.getId()).actions();

		Nodes root = Nodes.make();

		// 添加业务操作菜单项到根节点F
		for (IBizAction action : list) {
			if (action.isDisabled() || Str.isEmpty(action.getName()))
				continue;

			IAction parent = action.getParentAction();

			Node node = root.addNode(parent == null ? "" : parent.getId(), action.getId());
			node.setName(action.getName());
			node.setDesc(action.getDesc());
			node.setType(action.getTypeCode());
			node.setParams(action.getParams());
			node.set("moduleID", module.getId());
			if (Str.isEmpty(action.getMode())) {
				node.set("mode", action.getId());
			} else {
				node.set("mode", action.getMode());
			}
			if (!Str.isEmpty(action.getTargetUrl())) {
				node.set("url", action.getTargetUrl());
			}
			if (!Str.isEmpty(action.getTargetWindow())) {
				node.set("target", action.getTargetWindow());
			}

			String logoPath = null;
			Upload logo = action.getLogo();
			if (logo == null || Str.isEmpty(logo.toString()))
				logoPath = Demsy.appconfig.get("imagepath.actionlib") + "/" + action.getMode() + ".gif";
			else
				logoPath = logo.toString();

			if (new File(Demsy.contextDir + logoPath).exists()) {
				node.setIcon(logoPath);
			}

		}

		return root;
	}

	public List<? extends IRealm> getRealms(IDemsySoft softObj) {
		if (softObj == null) {
			return null;
		}
		return this.soft(softObj.getId()).realms();
	}

	public IRealm getRealm(IDemsySoft softObj, String realmCode) {
		return this.soft(softObj.getId()).realm(realmCode);
	}

	public IAction getActionComponent(Long id) {
		if (this.actionLibCache.size() == 0) {
			List<IAction> list = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_DEMSY_LIB_ACTION));
			for (IAction ele : list) {
				actionLibCache.put(ele.getId(), ele);
			}
		}

		return this.actionLibCache.get(id);
	}

	public IDataSource getDataSource(Long dataSource) {
		if (this.dataSourceCache == null) {
			dataSourceCache = new HashMap();
			List<IBizComponent> list = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_DEMSY_DATASOURCE));
			for (IBizComponent ele : list) {
				dataSourceCache.put(ele.getId(), (IDataSource) ele);
			}
		}

		return this.dataSourceCache.get(dataSource);
	}

	public void increase(IOrm orm, Object obj, String field) {
		increase(orm, obj, field, 1);
	}

	public void increase(IOrm orm, Object obj, String field, int v) {
		EnMappingImpl en = (EnMappingImpl) orm.getEnMapping(obj.getClass());
		Serializable id = Obj.getId(en, obj);

		StringBuffer sb = new StringBuffer("update ");
		sb.append(en.getTableName()).append(" set ");
		String colname = en.getField(field).getColumnName();
		Number value = Obj.getValue(obj, field);
		if (value == null || value.intValue() == 0) {
			sb.append(colname).append("=").append(v);
		} else {
			sb.append(colname).append("=").append(colname).append("+").append(v);
		}
		sb.append(" where ").append(en.getIdentifiedField().getColumnName()).append("=").append(id);
		String sqlstr = sb.toString();

		((OrmImpl) orm).getDao().execute(Sqls.create(sqlstr));
	}

	public void decrease(IOrm orm, Object obj, String field) {
		decrease(orm, obj, field, 1);
	}

	public void decrease(IOrm orm, Object obj, String field, int v) {
		Number value = Obj.getValue(obj, field);
		if (value != null && value.intValue() > 0) {
			EnMappingImpl en = (EnMappingImpl) orm.getEnMapping(obj.getClass());
			Serializable id = Obj.getId(en, obj);

			StringBuffer sb = new StringBuffer("update ");
			sb.append(en.getTableName()).append(" set ");
			String colname = en.getField(field).getColumnName();
			sb.append(colname).append("=").append(colname).append("-").append(v);
			sb.append(" where ").append(en.getIdentifiedField().getColumnName()).append("=").append(id);
			String sqlstr = sb.toString();

			((OrmImpl) orm).getDao().execute(Sqls.create(sqlstr));
		}
	}
}
