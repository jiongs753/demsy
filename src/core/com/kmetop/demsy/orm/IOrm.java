package com.kmetop.demsy.orm;

import java.io.Serializable;
import java.util.List;

import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.NullCndExpr;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.mapping.EnMapping;

/**
 * 数据库ORM： 封装底层DAO对数据库的操作。
 * <UL>
 * <LI>支持条件表达式；
 * <LI>不受事务控制；
 * <LI>不能执行业务插件；
 * <LI>不支持直接对表的操作；
 * <LI>支持关联实体的同步操作：底层DAO不支持关联实体的同步操作；
 * </UL>
 * 
 * @author yongshan.ji
 * @param <T>
 */
public interface IOrm {
	public IMetaDao getMetaDao();

	/**
	 * <b>保存实体对象</b>
	 * <p>
	 * 1. 保存的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 如果实体数据已经存在则对数据作修改操作，否则插入一条新记录到数据表中；
	 * <p>
	 * 3. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 4. 只有匹配正则表达式的字段才会被保存，如果未指定字段正则表达式，则所有字段都将被保存；
	 * <p>
	 * 5. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式(如:“id|name”)
	 * @return 新增和修改的总记录数
	 */
	public int save(Object obj, NullCndExpr fieldRexpr);

	public int save(Object obj);

	/**
	 * <b>插入实体对象</b>
	 * <p>
	 * 1. 插入的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 3. 只有匹配正则表达式的字段才会被插入，如果未指定字段正则表达式，则所有字段都将被插入；
	 * <p>
	 * 4. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式(如“id|name”)
	 * @return 新增的记录数
	 */
	public int insert(Object obj, NullCndExpr fieldRexpr);

	public int insert(Object obj);

	/**
	 * <b>修改实体对象</b>
	 * <p>
	 * 1. 修改的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 3. 只有匹配正则表达式的字段才会被修改，如果未指定字段正则表达式，则所有字段都将被修改；
	 * <p>
	 * 4. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式(如“id|name”)
	 * @return 修改的记录数
	 */
	public int update(Object obj, NullCndExpr fieldRexpr);

	public int update(Object obj);

	/**
	 * <b>批量修改</b>
	 * <p>
	 * 1. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 2. 只修改匹配正则表达式的字段，未指定正则表达式，则修改所有字段；
	 * <p>
	 * 3. 多个表达式中的条件表达式按“AND”关系处理；字段表达式按“|”关系处理；
	 * <p>
	 * 4. 满足条件的数据字段将被实体对象中的字段值替换；
	 * 
	 * @param obj
	 *            实体对象
	 * @param expr
	 *            表达式
	 * @return 受影响的记录数
	 */
	public int updateMore(Object obj, CndExpr expr);

	/**
	 * <b>删除实体对象</b>
	 * <p>
	 * 1. 删除的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * 
	 * @param obj
	 *            实体对象
	 * @return 成功删除的记录数
	 */
	public int delete(Object obj);

	/**
	 * <b>删除实体对象</b>
	 * <p>
	 * 1. 只删除实体ID与指定ID相同的实体对象；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param id
	 *            实体ID
	 * @return 成功删除的记录数
	 */
	public int delete(Class<Object> classOfEntity, Serializable id);

	/**
	 * <b>批量删除</b>
	 * <p>
	 * 1. 删除满足条件的实体数据，如果没有指定删除条件，则将清空整张表；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * <p>
	 * 3. 多个表达式中的条件表达式按“AND”关系处理；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 成功删除的记录数
	 */
	public int deleteMore(Class classOfEntity, CndExpr expr);

	public int deleteMore(Class classOfEntity);

	/**
	 * <b>加载实体对象</b>
	 * <p>
	 * 1. 加载实体ID与指定ID相等的实体对象；
	 * <p>
	 * 2. 只有匹配正则表达式的字段才会被绑定到实体对象中，如果未指定字段表达式，则所有字段都将被绑定到实体对象中；
	 * <p>
	 * 3. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param id
	 *            实体数据ID
	 * @param fieldRexpr
	 *            字段正则表达式(如“id|name”)
	 * @return 实体对象
	 */
	public Object load(Class classOfEntity, Serializable id, CndExpr fieldRexpr);

	public Object load(Class classOfEntity, Serializable id);

	/**
	 * <b>加载实体对象<b>
	 * <p>
	 * 1. 加载满足条件的实体对象；
	 * <p>
	 * 2. 如果未指定条件则将对整张表进行查询；
	 * <p>
	 * 3. 如果满足条件的数据有多条，则加载第一条；
	 * <p>
	 * 4. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定；
	 * <p>
	 * 5. 多个表达式中：条件表达式按“AND”关系处理；字段表达式按“|”关系处理；排序、分组、分页等表达式来自第一个表达式；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            条件表达式
	 * @return 实体对象
	 */
	public Object load(Class classOfEntity, CndExpr expr);

	public Object load(Class classOfEntity);

	/**
	 * <b>查询分页实体集</b>
	 * <p>
	 * 1. 如果未指定查询条件，则将对整表进行查询;
	 * <p>
	 * 2. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定。
	 * <p>
	 * 3. 多个表达式中：条件表达式按“AND”关系处理；字段表达式按“|”关系处理；排序、分组、分页等表达式来自第一个表达式；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 分页结果集
	 */
	public List query(Class classOfEntity, CndExpr expr);

	public List query(Class classOfEntity);

	/**
	 * <b>查询分页实体集</b>
	 * <p>
	 * 1. 如果未指定查询条件，则将对整表进行查询;
	 * <p>
	 * 2. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定。
	 * <p>
	 * 3. 多个表达式中：条件表达式按“AND”关系处理；字段表达式按“|”关系处理；排序、分组、分页等表达式来自第一个表达式；
	 * <p>
	 * 4. 表达式来自查询分页器；
	 * <p>
	 * 5. 满足条件的总记录数将被回填到查询分页器中；
	 * 
	 * @param pager
	 *            查询分页器
	 * @return 分页结果集
	 */
	public List query(Pager pager);

	/**
	 * <b>统计</b>
	 * <p>
	 * 1. 若未指定统计条件，则将对整表进行统计；
	 * <p>
	 * 2. 多个表达式中：条件表达式按“AND”关系处理；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 实体数据记录数
	 */
	public int count(Class classOfEntity, CndExpr expr);

	public int count(Class classOfEntity);

	/**
	 * 获取指定实体类的映射，默认将自动同步数据库表、字段、外键等。
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @return 实体映射
	 */
	public EnMapping getEnMapping(Class classOfEntity);

	/**
	 * 获取指定实体类的映射，并根据autoDDL决定是否同步数据库表、字段、外键等。
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param asynTable
	 *            是否自动同步数据库表、字段、外键等。
	 * @return 实体映射
	 */
	public EnMapping getEnMapping(Class classOfEntity, boolean asynTable);

	public EnMapping getEnMapping(Class classOfT, boolean syncTable, boolean syncRefTable);

	/**
	 * 获取实体类的ID字段属性名
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @return 属性名
	 */
	public String getIdProperty(Class classOfEntity);

	/**
	 * 获取数据库本地方言
	 * 
	 * @return
	 */
	public Dialect getDialect();

	/**
	 * 获取命名策略
	 * <p>
	 * 命名策略用来将Java实体类名、属性名转换成数据库表名、字段名等。
	 * 
	 * @param classOfEntity
	 *            实体类，不同的实体类可以有不同的命名策略
	 * @return 命名策略
	 */
	public INamingStrategy getNamingStrategy();

	/**
	 * 运行Connection回调，便于直接使用数据库Connection对象
	 * 
	 * @param conn
	 *            回调对象
	 * @return 运行结果
	 * @throws Exception
	 */
	public Object run(NoTransConnCallback conn);

	public void removeMapping(Class cls);

	public void clearMapping();
}
