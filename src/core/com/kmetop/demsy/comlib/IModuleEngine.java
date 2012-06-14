package com.kmetop.demsy.comlib;

import java.io.Serializable;
import java.util.List;

import com.kmetop.demsy.comlib.biz.IBizCatalog;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.entity.IDemsyCorp;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IRealm;
import com.kmetop.demsy.config.IDataSource;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.orm.IOrm;

/**
 * 组件库：用于获取经过安全认证后的组件。
 * 
 * @author yongshan.ji
 * 
 */
public interface IModuleEngine {
	public void clearCache();

	Nodes makeNodesByCurrentSoft();

	/**
	 * 获取平台默认企业用户
	 * 
	 * @return
	 */
	IDemsyCorp getCorpByDefault();

	IDemsyCorp getCorp(String code);

	/**
	 * 获取平台默认应用软件
	 * 
	 * @return
	 */
	IDemsySoft getSoftByDefault();

	IDemsySoft getSoft(String domainOrCode);

	IDemsySoft getSoft(Long id);

	List<? extends IModule> getModules(IDemsySoft soft);

	/**
	 * 根据功能模块ID获取功能模块
	 */
	IModule getModule(Long moduleID);

	/**
	 * 根据功能模块GUID获取功能模块
	 * 
	 * @param moduleGuid
	 * @return
	 */
	IModule getModule(String moduleGuid);

	/**
	 * 获取软件业务模块
	 * 
	 * @param soft
	 * @param system
	 * @return
	 */
	IModule getModule(IDemsySoft soft, IBizSystem system);

	/**
	 * 获取从属模块
	 * 
	 * @param module
	 * @return
	 */
	List<IModule> getSubModules(IModule module);

	/**
	 * 获取模块操作
	 * 
	 * @param module
	 * @param opID
	 * @return
	 */
	IAction getAction(IModule module, Serializable opID);

	/**
	 * 创建软件功能模块菜单
	 * 
	 * @param soft
	 * @param optimize
	 * @return
	 */
	Nodes makeNodesByModule(IDemsySoft soft);
	
	Nodes makeNodesByModule(IDemsySoft soft, byte role);

	/**
	 * 创建模块操作菜单
	 * 
	 * @param module
	 * @return
	 */
	Nodes makeNodesByAction(IModule module);

	/**
	 * 获取模块业务系统
	 * 
	 * @param module
	 * @return
	 */
	IBizSystem getSystem(IModule module);

	/**
	 * 升级软件功能模块
	 * 
	 * @param soft
	 */
	void upgradeModules(IDemsySoft soft);

	void upgradeWebContent(IDemsySoft soft);

	/**
	 * 安装软件功能模块
	 * 
	 * @param soft
	 */
	void setupDemsy();

	ISoftConfig getSoftConfig(String key);

	Nodes makeNodesByRealm(IDemsySoft soft);

	List<? extends IRealm> getRealms(IDemsySoft softObj);

	IRealm getRealm(IDemsySoft soft, String realmCode);

	IModule getModule(Long softID, IBizSystem refrenceSystem);

	IAction getActionComponent(Long actionLib);

	IDataSource getDataSource(Long dataSource);

	IModule makeModule(IOrm orm, IDemsySoft soft, IBizCatalog catalog);

	IModule makeModule(IOrm orm, IDemsySoft soft, IBizSystem system);

	void increase(IOrm orm, Object obj, String field);

	void decrease(IOrm orm, Object obj, String field);

	void increase(IOrm orm, Object obj, String field, int value);

	void decrease(IOrm orm, Object obj, String field, int value);
}
