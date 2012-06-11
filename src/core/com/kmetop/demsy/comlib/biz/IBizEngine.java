package com.kmetop.demsy.comlib.biz;

import java.util.List;
import java.util.Map;

import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Option;
import com.kmetop.demsy.orm.IOrm;

/**
 * 业务系统引擎
 * 
 * @author yongshan.ji
 */
public interface IBizEngine {

	public IBizEngine me(IOrm orm);

	public void validateSystems() throws DemsyException;

	/**
	 * 将已存在的数据库表解析成业务系统对象
	 * 
	 * @param tableName
	 *            数据库表名
	 * @return 业务系统
	 */
	public IBizSystem parseSystem(String tableName);

	/**
	 * 将指定的实体类转换成业务系统对象
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @return 业务系统相关实体列表
	 * @throws DemsyException
	 * @throws ClassNotFoundException
	 *             指定的类没有找到则抛出该异常
	 */
	public IBizSystem parseSystem(Class classOfEntity) throws DemsyException;

	public List<IBizSystem> parseStaticSystems() throws DemsyException;

	public void setupComlib(Class<?> type);

	public void setupComlibs();

	// IBizSystem Class of Entity ============================================

	/**
	 * 只编译业务系统，但不 copy 类文件到/WEB-INF/classes下
	 * 
	 * @param system
	 * @return
	 * @throws DemsyException
	 */
	public List<String> compileSystem(IBizSystem system) throws DemsyException;

	/**
	 * 获取业务系统实体类
	 * 
	 * @param system
	 *            业务系统对象
	 * @return 实体类
	 * @throws DemsyException
	 *             编译业务实体类出错将抛出编译错误异常
	 */
	public Class getType(IBizSystem system) throws DemsyException;

	public Class getStaticType(String system);

	/**
	 * 获取业务系统自身引用的递归树节点属性。
	 * 
	 * @param system
	 *            业务系统
	 * @return 属性名称
	 */
	public String getTreeField(IBizSystem system);

	/**
	 * 获取系统字段：不受安全系统的限制。
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统字段列表
	 */
	public List<? extends IBizField> getFields(IBizSystem system);

	public List<? extends IBizField> getFields(IBizFieldGroup group);

	public List<? extends IBizField> getFieldsOfEnabled(IBizSystem system);

	public List<? extends IBizField> getFieldsOfEnabled(IBizFieldGroup group);

	public List<? extends IBizField> getFieldsOfGrid(IBizSystem system);

	/**
	 * 获取系统外键字段：即外键引用其他业务系统
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	public List<? extends IBizField> getFieldsOfSystemFK(IBizSystem system);

	/**
	 * 获取所有外键字段：包括外键系统引用和字典字段
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	public List<? extends IBizField> getFieldsOfFK(IBizSystem system);

	public List<? extends IBizField> getFieldsOfNavi(IBizSystem system);

	public Map<String, IBizField> makeFieldsMap(List<? extends IBizField> list);

	/**
	 * 获取引用了指定系统的字段
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统被哪些外间字段引用？
	 */
	public List<? extends IBizField> getFieldsOfExport(IBizSystem system);

	public List<? extends IBizSystem> getSystemsOfSlave(IBizSystem system);

	public List<? extends IBizField> getFieldsOfSlave(IBizSystem system);

	public boolean isSlave(IBizSystem system);

	public List<? extends IBizField> getChildren(IBizField field);

	public boolean isNumber(IBizField field);

	public boolean isRichText(IBizField field);

	public boolean isText(IBizField field);

	public boolean isDate(IBizField field);

	public boolean isTime(IBizField field);

	public boolean isString(IBizField field);

	public boolean isUpload(IBizField field);

	public boolean isBuildin(IBizSystem system, String prop);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	public boolean isManyToOne(IBizField field);

	/**
	 * 判断字段是否为一对一的字段
	 * 
	 * @return
	 */
	public abstract boolean isOneToOne(IBizField field);

	/**
	 * 判断字段是否为一对多的字段
	 * 
	 * @return
	 */
	public abstract boolean isOneToMany(IBizField field);

	/**
	 * 判断字段是否为多对多的字段
	 * 
	 * @return
	 */
	public abstract boolean isManyToMany(IBizField field);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	public boolean isDic(IBizField field);

	public boolean isBoolean(IBizField field);

	public boolean isEnabled(IBizField field);

	public boolean isGridField(IBizField field);

	public boolean isSystemFK(IBizField field);

	public boolean isFieldRef(IBizField field);

	public String getPropName(IBizField field);

	public int getGridWidth(IBizField field);

	public int getPrecision(IBizField field);

	public Class getGenericType(IBizField field) throws DemsyException;

	public Class getType(IBizField field) throws DemsyException;

	public String getMode(IBizField field, IAction action);

	public String getMode(IBizFieldGroup group, IAction action);

	public Nodes makeNaviNodes(IBizSystem system);

	public boolean hasField(Class klass, String fldname);

	public Nodes makeOptionNodes(IBizField field);

	public Option[] getOptions(IBizField field);

}
