package com.kmetop.demsy.orm.nutz;

import java.sql.SQLException;
import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;

import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.IMetaDao;
import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.mapping.EnMapping;

/**
 * 对单表操作的 Nutz DAO 扩展接口
 * 
 * @author yongshan.ji
 */
public interface IExtDao extends Dao {
	public IMetaDao getMetaDao();

	public int save(Object obj);

	/**
	 * 保存实体对象
	 * <p>
	 * 可以保存单个实体对象、实体对象集合、数组、Map等；
	 * <p>
	 * 如果待保存的实体对象已经存在，则对数据记录作修改操作；否则对记录作插入操作；
	 * <p>
	 * 只有匹配正则表达式的字段会被保存，如果没有指定字段正则表达式，则所有字段都将被保存；
	 * <p>
	 * 如果igloreNull参数为true，则空值字段即使匹配也不会被保存；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param igloreNull
	 *            是否忽略实体对象中的空值字段
	 * @return 保存了多少条记录
	 */
	public int save(Object obj, String fieldRexpr, boolean igloreNull);

	/**
	 * 插入实体对象
	 * <p>
	 * 可以插入单个实体对象、实体对象集合、数组、Map等；
	 * <p>
	 * 只有匹配正则表达式的字段会被插入，如果没有指定字段正则表达式，则所有字段都将被插入；
	 * <p>
	 * 如果igloreNull参数为true，则空值字段即使匹配也不会被插入；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param igloreNull
	 *            是否忽略实体对象中的空值字段
	 * @return 保存了多少条记录
	 */
	public int insert(Object obj, String fieldRexpr, boolean igloreNull);

	/**
	 * 修改实体对象
	 * <p>
	 * 可以修改单个实体对象、实体对象集合、数组、Map等；
	 * <p>
	 * 只有匹配正则表达式的字段会被修改，如果没有指定字段正则表达式，则所有字段都将被修改；
	 * <p>
	 * 如果igloreNull参数为true，则空值字段即使匹配也不会被修改；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param igloreNull
	 *            是否忽略实体对象中的空值字段
	 * @return 保存了多少条记录
	 */
	public int update(Object obj, String fieldRexpr, boolean igloreNull);

	/**
	 * 批量修改满足条件的数据字段
	 * <p>
	 * 只有匹配正则表达式的字段会被修改，如果没有指定字段正则表达式，则所有字段都将被修改；
	 * <p>
	 * 如果igloreNull参数为true，则空值字段即使匹配也不会被修改；
	 * 
	 * @param obj
	 *            实体对象： 对象中的匹配字段用来替换批量修改的记录字段
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param igloreNull
	 *            是否忽略空值
	 * @param cnd
	 *            条件表达式
	 * @return 修改了多少条记录
	 */
	public int update(Object obj, String fieldRexpr, boolean igloreNull, Condition cnd);

	/**
	 * 按ID抓取实体对象
	 * <p>
	 * 只有匹配的字段被绑定到对象中；
	 * 
	 * @param <T>
	 *            实体类型
	 * @param classOfT
	 *            实体类
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param id
	 *            实体ID
	 * @return 实体对象
	 */
	public <T> T fetch(Class<T> classOfT, String fieldRexpr, long id);

	/**
	 * 抓取满足条件的实体对象
	 * <p>
	 * 只有匹配字段被绑定到对象中；
	 * <p>
	 * 如果满足条件的数据有多条，则只抓取第一条；
	 * 
	 * @param <T>
	 *            实体类型
	 * @param klass
	 *            实体类
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param cnd
	 *            抓取条件
	 * @return 实体对象
	 */
	public <T> T fetch(Class<T> klass, String fieldRexpr, Condition cnd);

	/**
	 * 分页查询满足条件的数据集
	 * <p>
	 * 只有匹配正则表达式的字段被绑定到实体对象中；
	 * 
	 * @param <T>
	 *            实体类型
	 * @param classOfT
	 *            实体类
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param cnd
	 *            查询条件
	 * @param pager
	 *            分页器
	 * @return 数据列表
	 */
	public <T> List<T> query(Class<T> classOfT, String fieldRexpr, Condition cnd, Pager pager);

	/**
	 * 保存实体对象中匹配的关联对象
	 * <p>
	 * 实体对象可以是单个实体、实体集合、数组、Map等；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @return 保存了多少条数据
	 */
	public int saveLinks(Object obj, String fieldRexpr);

	/**
	 * 插入匹配的多对多关系，即：插入中间表关联数据
	 * <p>
	 * 实体对象可以是单个实体、实体集合、数组、Map等；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @return 插入了多少条记录
	 */
	public int insertRelations(Object obj, String fieldRexpr);

	/**
	 * 删除匹配的多对多关系
	 * <p>
	 * 实体对象可以是单个实体、实体集合、数组、Map等；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            实体正则表达式
	 * @return 删除了多少条记录
	 */
	public int deleteMany(Object obj, String fieldRexpr);

	/**
	 * 删除匹配的多对多关系，即删除中间表中的记录。
	 * <p>
	 * 实体对象可以是单个实体、实体集合、数组、Map等；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            实体正则表达式
	 * @return 删除了多少条记录
	 */
	public int deleteRelations(Object obj, String fieldRexpr);

	/**
	 * 获取实体映射
	 * <p>
	 * 默认自动同步数据库表、字段、外键关联等；自动同步外键关联的表是否存在，不存在则自动创建；
	 * 
	 * @param <T>实体类泛型
	 * @param klass
	 *            实体类
	 * @return 实体映射
	 */
	public <T> EnMapping<T> getEnMapping(Class<T> klass);

	/**
	 * 获取实体映射
	 * <p>
	 * 如果参数checkTable为true，则自动同步数据库表、字段、外键关联等
	 * <p>
	 * 不会自动同步外键关联的表
	 * 
	 * @param <T>
	 *            实体类型
	 * @param klass
	 *            实体映射类
	 * @param syncTable
	 *            加载实体的过程中是否自动同步实体表
	 * @return 实体映射
	 */
	public <T> EnMapping<T> getEnMapping(Class<T> klass, boolean syncTable);

	/**
	 * 获取实体映射
	 * <p>
	 * 如果参数checkTable为true，则自动同步数据库表、字段、外键关联等；
	 * <p>
	 * 如果参数checkRefTable为true，则自动同步外键关联的表是否存在，不存在则自动创建；
	 * 
	 * @param <T>
	 *            实体类型
	 * @param klass
	 *            实体映射类
	 * @param syncTable
	 *            加载实体的过程中是否自动同步实体表
	 * @param syncRefTable
	 *            加载实体的过程中是否自动检查外键表
	 * @return 实体映射
	 */
	public <T> EnMapping<T> getEnMapping(Class<T> klass, boolean syncTable, boolean syncRefTable);

	/**
	 * 获取实体映射持有者
	 * 
	 * @return 实体映射持有者
	 */
	public EnMappingHolder getEntityHolder();

	/**
	 * 获取数据库本地方言
	 * 
	 * @return 数据库本地方言
	 */
	public Dialect getDialect();

	/**
	 * 获取命名策略
	 * <p>
	 * 命名策略将Java类名、属性名转换成表名、字段名等；
	 * <p>
	 * 不同的Java类可以有不同的命名策略；
	 * 
	 * @param cls
	 *            实体类
	 * @return 命名策略
	 */
	public INamingStrategy getNamingStrategy();

	/**
	 * 运行Connection回调，便于直接使用数据库Connection对象
	 * 
	 * @param conn
	 *            回调对象
	 * @return 运行结果
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object run(NoTransConnCallback conn);

	String getIdProperty(Class klass);
}
