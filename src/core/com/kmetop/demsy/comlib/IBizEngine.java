package com.kmetop.demsy.comlib;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizFieldGroup;
import com.kmetop.demsy.comlib.biz.IBizFieldType;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.IRuntimeConfigable;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Option;
import com.kmetop.demsy.orm.expr.CndExpr;

/**
 * 业务系统引擎
 * 
 * @author yongshan.ji
 */
public interface IBizEngine {

	void clearCache();

	List<Class<?>> listTypes();

	IBizSystem setupSystemFromDB(IDemsySoft soft, String tableName);

	List<IBizSystem> setupSystemFromPackage(IDemsySoft soft) throws DemsyException;

	IBizSystem setupSystemFromClass(IDemsySoft soft, Class classOfEntity) throws DemsyException;

	void parseSystemByAnnotation(Class klass, IBizSystem system);

	List setupFromPackage(IDemsySoft soft);

	/**
	 * 获取业务实体扩展类
	 * 
	 * @param system
	 *            业务系统
	 * @return 扩展类全称
	 */
	public abstract String getExtendClassName(IBizSystem system);

	/**
	 * 从类注释中安装业务系统数据
	 * 
	 * @param soft
	 * @param type
	 * @return
	 */
	List setupFromJson(IDemsySoft soft, Class<?> type);

	/**
	 * 安装JSON数据到业务系统中
	 * 
	 * @param soft
	 * @param type
	 *            业务系统类
	 * @param json
	 *            JSON格式的业务数据
	 * @return
	 */
	<T> List<T> setupFromJson(IDemsySoft soft, Class<T> type, String json);

	/**
	 * 从文件夹中导入JSON数据，文件名即为类名。
	 * 
	 * @param soft
	 * @param folder
	 * @return
	 */
	int importFromJson(IDemsySoft soft, String folder);

	/**
	 * 导出满足条件的数据到文件夹，文件格式为JSON
	 * 
	 * @param soft
	 * @param folder
	 * @param expr
	 *            条件表达式
	 * @return 导出了多少条数据
	 * @throws IOException
	 */
	int exportToJson(IDemsySoft soft, String folder, CndExpr expr) throws IOException;

	/**
	 * 获取业务字段类型库，key为字段编码。
	 * 
	 * @return
	 */
	Map<String, IBizFieldType> getFieldTypes();

	Map<Long, IBizFieldType> getFieldTypesById();

	/**
	 * 获取业务系统列表
	 * 
	 * @return 业务系统集合
	 */
	List<? extends IBizSystem> getSystems(IDemsySoft soft);

	/**
	 * 根据业务系统ID获取业务系统对象
	 * <OL>
	 * <LI>如果业务系统ID是String型，将按编号查找业务系统；
	 * <LI>如果业务系统ID是Number型，将按ID查找业务系统；
	 * </OL>
	 * 
	 * @param systemID
	 *            业务系统ID
	 * @return 业务系统
	 */
	IBizSystem getSystem(Long systemID);

	// IBizSystem getSystem(IDemsySoft soft, String systemCode);

	/**
	 * 获取业务系统字段分组列表
	 * 
	 * @param system
	 *            业务系统
	 * @return 字段分组的集合
	 */
	List<? extends IBizFieldGroup> getFieldGroups(IBizSystem system);

	void validateSystems(IDemsySoft soft) throws DemsyException;

	/**
	 * 只编译业务系统，但不 copy 类文件到/WEB-INF/classes下
	 * 
	 * @param system
	 * @return
	 * @throws DemsyException
	 */
	List<String> compileSystem(IBizSystem system) throws DemsyException;

	/**
	 * 获取业务系统实体类
	 * 
	 * @param system
	 *            业务系统对象
	 * @return 实体类
	 * @throws DemsyException
	 *             编译业务实体类出错将抛出编译错误异常
	 */
	Class getType(IBizSystem system) throws DemsyException;

	Class getStaticType(String system);

	/**
	 * 获取业务系统自身引用的递归树节点属性。
	 * 
	 * @param system
	 *            业务系统
	 * @return 属性名称
	 */
	IBizField getFieldOfUnSelfTree(IBizSystem system);

	IBizField getFieldOfSelfTree(IBizSystem system);

	/**
	 * 获取系统字段：不受安全系统的限制。
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统字段列表
	 */
	List<? extends IBizField> getFields(IBizSystem system);

	List<? extends IBizField> getFields(IBizFieldGroup group);

	List<? extends IBizField> getFieldsOfEnabled(IBizSystem system);

	List<? extends IBizField> getFieldsOfEnabled(IBizFieldGroup group);

	List<? extends IBizField> getFieldsOfGrid(IBizSystem system, String fields);

	/**
	 * @param system
	 * @return
	 */
	List<? extends IBizField> getFieldsOfSystemFK(IBizSystem system, Class fkType);

	/**
	 * 获取系统外键字段：即外键引用其他业务系统
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IBizField> getFieldsOfSystemFK(IBizSystem system);

	/**
	 * 获取所有外键字段：包括外键系统引用和字典字段
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IBizField> getFieldsOfFK(IBizSystem system);

	List<? extends IBizField> getFieldsOfNavi(IBizSystem system);

	Map<String, IBizField> getFieldsMap(IBizSystem system);

	Map<String, IBizField> getFieldsMap(List<? extends IBizField> list);

	/**
	 * 获取引用了指定系统的字段
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统被哪些外间字段引用？
	 */
	List<? extends IBizField> getFieldsOfExport(IBizSystem system);

	List<? extends IBizSystem> getSystemsOfSlave(IBizSystem system);

	List<? extends IBizField> getFieldsOfSlave(IBizSystem system);

	boolean isSlave(IBizSystem system);

	// List<? extends IBizField> getChildren(IBizField field);

	boolean isNumber(IBizField field);

	boolean isInteger(IBizField field);

	boolean isRichText(IBizField field);

	boolean isText(IBizField field);

	boolean isDate(IBizField field);

	boolean isString(IBizField field);

	boolean isUpload(IBizField field);

	boolean isImage(IBizField field);

	boolean isMultiUpload(IBizField field);

	boolean isMultiImage(IBizField field);

	boolean isSubSystem(IBizField field);

	boolean isFakeSubSystem(IBizField field);

	boolean isBuildin(IBizSystem system, String prop);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isManyToOne(IBizField field);

	/**
	 * 判断字段是否为一对一的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToOne(IBizField field);

	/**
	 * 判断字段是否为一对多的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToMany(IBizField field);

	/**
	 * 判断字段是否为多对多的字段
	 * 
	 * @return
	 */
	abstract boolean isManyToMany(IBizField field);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isV1Dic(IBizField field);

	boolean isBoolean(IBizField field);

	boolean isEnabled(IBizField field);

	boolean isGridField(IBizField field);

	boolean isSystemFK(IBizField field);

	boolean isFieldRef(IBizField field);

	String getPropName(IBizField field);

	int getGridWidth(IBizField field);

	int getPrecision(IBizField field);

	Class getGenericType(IBizField field) throws DemsyException;

	Class getType(IBizField field) throws DemsyException;

	String getMode(IBizField field, IAction action, boolean mustPriority, String defaultMode);

	String getMode(IBizFieldGroup group, IAction action);

	Nodes makeNaviNodes(IBizSystem system, String idField);

	Nodes makeOptionNodes(IBizField field, String mode, Object data, String idField);

	List<String> makeCascadeExpr(Object obj, IBizField field, String mode);

	Option[] getOptions(IBizField field);

	String[] getCascadeMode(IBizField field, Object data);

	int getModeValue(String mode);

	Map<String, String> getMode(IBizSystem system, IAction action, Object data);

	void validate(IBizSystem system, IAction action, Object data, Map<String, String> fieldMode) throws DemsyException;

	void loadFieldValue(Object obj, IBizSystem system);

	IBizField getField(Long fieldID);

	IBizSystem getSystem(String systemCode);

	String getUiMode(String mode);

	Class getSystemClass(String sysCode);

	Class getType(String sysCode);

	/**
	 * 生成运行时自定义字段
	 * 
	 * @param runtimeCustom
	 * @return
	 */
	List<? extends IBizField> makeFields(IRuntimeConfigable runtimeCustom);
}
