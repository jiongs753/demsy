package com.kmetop.demsy.biz;

import java.util.List;

import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.OrmCallback;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;

/**
 * 业务会话: 是ORM的封装。
 * <OL>
 * <LI>支持事务：每个方法都在一个事物单元中完成，ORM则不支持；
 * <LI>支持业务插件：调用方法时可以指定业务插件{@link IBizPlugin}，ORM 则不支持；
 * <LI>支持不同ORM的克隆； 参见{@link #me(IOrm)}
 * <LI>支持实体泛型的克隆；参见{@link #me()}
 * </OL>
 * 
 * @author yongshan.ji
 * @param <T>
 */
public interface IBizSession<T> {
	/**
	 * 克隆特定类型的业务会话
	 * 
	 * @param <X>
	 *            泛型类型
	 * @return 克隆对象
	 */
	public <X> IBizSession<X> me();

	/**
	 * 用指定的ORM克隆特定类型的业务会话
	 * 
	 * @param <X>
	 *            泛型类型
	 * @param orm
	 *            ORM
	 * @return 克隆对象
	 */
	public <X> IBizSession<X> me(IOrm orm);

	/**
	 * 获取ORM
	 * 
	 * @return
	 */
	public IOrm orm();

	/**
	 * 保存实体对象： 可以保存单个实体、实体集合、数组、Map等
	 * <p>
	 * 将同步检查实体对象中的Many/ManyMany关联对象。如果关联对象不存在，则自动保存；
	 * <p>
	 * 执行保存操作前后将调用业务插件中的相关方法，以执行业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param plugins
	 *            业务插件
	 * @return 保存了多少条记录
	 */
	public int save(T obj, IBizPlugin... plugins);

	/**
	 * 保存实体对象： 可以保存单个实体、实体集合、数组、Map等
	 * <p>
	 * 将同步检查实体对象中的Many/ManyMany关联对象。如果关联对象不存在，则自动保存；
	 * <p>
	 * 执行保存操作前后将调用业务插件中的相关方法，以执行业务逻辑；
	 * <p>
	 * 只有匹配正则表达式的字段会被保存，如果未指定字段正则表达式，则所有字段都将被保存。
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param plugins
	 *            业务插件
	 * @return 保存了多少条记录 b
	 */
	public int save(T obj, CndExpr fieldRexpr, IBizPlugin... plugins);

	/**
	 * 批量修改： 批量修改满足条件的数据字段。
	 * <p>
	 * 将同步检查实体对象中的Many/ManyMany关联对象。如果关联对象不存在，则自动保存；
	 * <p>
	 * 执行修改操作前后将调用业务插件中的相关方法，以执行业务逻辑；
	 * <p>
	 * 只有匹配正则表达式的字段会被修改，如果未指定字段正则表达式，则所有字段都将被修改；
	 * <p>
	 * 只有满足修改条件的数据才会被修改；
	 * <p>
	 * 满足条件的数据字段会被实体对象中的对应字段值替换；
	 * <p>
	 * 字段正则表达式来自参数表达式；
	 * 
	 * @param obj
	 *            实体对象
	 * @param expr
	 *            表达式
	 * @param plugins
	 *            业务插件
	 * @return 保存了多少条记录
	 */
	public int updateMore(T obj, CndExpr expr, IBizPlugin... plugins);

	/**
	 * 删除实体对象： 可以删除单个实体、实体集合、数组、Map等
	 * <p>
	 * 删除实体对象的同时“不会”级联删除关联的Many/ManyMany对象；
	 * <p>
	 * 执行删除操作前后将调用业务插件中的相关方法，以执行业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param plugins
	 *            业务插件
	 * @return 删除了多少条记录
	 */
	public int delete(T obj, IBizPlugin... plugins);

	/**
	 * 删除实体对象： 删除满足实体ID的实体对象
	 * <p>
	 * 删除实体对象的同时“不会”级联删除关联的Many/ManyMany对象；
	 * <p>
	 * 执行删除操作前后将调用业务插件中的相关方法，以执行业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param plugins
	 *            业务插件
	 * @param id
	 *            实体ID
	 * @return 删除了多少条记录
	 */
	public int delete(Class<T> klass, Long id, IBizPlugin... plugins);

	/**
	 * 批量删除： 删除满足条件的实体数据
	 * <p>
	 * 删除实体对象的同时“不会”级联删除关联的Many/ManyMany对象；
	 * 
	 * @param klass
	 *            实体类
	 * @param expr
	 *            删除条件
	 * @return 删除了多少条记录
	 */
	public int deleteMore(Class<T> klass, CndExpr expr, IBizPlugin... plugins);

	/**
	 * 获取实体对象
	 * <p>
	 * 获取与实体ID匹配的实体对象；
	 * <p>
	 * 获取实体对象的同时执行相关业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param plugins
	 *            业务插件
	 * @param id
	 *            实体ID
	 * @return 实体对象
	 */
	public T load(Class<T> klass, Long id, IBizPlugin... plugins);

	/**
	 * 获取实体对象
	 * <p>
	 * 获取与实体ID匹配的实体对象；
	 * <p>
	 * 只有匹配正则表达式的字段会被绑定到实体对象中，如果未指定字段正则表达式，则所有字段都会被绑定到实体对象中；
	 * <p>
	 * 获取实体对象的同时执行相关业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param plugins
	 *            业务插件
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @param id
	 *            实体ID
	 * @return 实体对象
	 */
	public T load(Class<T> klass, Long id, CndExpr fieldRexpr, IBizPlugin... plugins);

	/**
	 * 获取实体对象
	 * <p>
	 * 获取满足查询条件的实体对象，如果满足条件的记录超过一条，则获取第一条;
	 * <p>
	 * 只有匹配正则表达式的字段会被绑定到实体对象中，如果未指定字段正则表达式，则所有字段都会被绑定到实体对象中；
	 * <p>
	 * 获取实体对象的同时执行相关业务逻辑；
	 * <p>
	 * 字段正则表达式从表达式中获取；
	 * 
	 * @param klass
	 *            实体类
	 * @param plugins
	 *            业务插件
	 * @param expr
	 *            条件表达式
	 * @return 实体对象
	 */
	public T load(Class<T> klass, CndExpr expr, IBizPlugin... plugins);

	public int count(Class<T> klass, CndExpr expr, IBizPlugin... plugins);

	/**
	 * 查询实体对象
	 * <p>
	 * 查询满足条件表达式的实体对象集合，如果表达式为空，则查询全部结果集；
	 * <p>
	 * 分页和分组信息将从表达式中获取；
	 * <p>
	 * 只有匹配正则表达式的字段会被绑定到实体对象中，如果未指定字段正则表达式，则所有字段都会被绑定到实体对象中；
	 * <p>
	 * 多个表达式之间用AND关系处理；
	 * <p>
	 * 查询实体对象的同时调用插件，执行业务逻辑；
	 * <p>
	 * 字段正则表达式从表达式中获取；
	 * 
	 * @param klass
	 *            实体类
	 * @param plugins
	 *            业务插件
	 * @param expr
	 *            查询条件表达式
	 * @return 对象集合
	 */
	public List<T> query(Class<T> klass, CndExpr expr, IBizPlugin... plugins);

	/**
	 * 查询实体对象
	 * <p>
	 * 查询条件来自分页器；
	 * <p>
	 * 满足条件的总记录数自动被保存到分页器中；
	 * <p>
	 * 只有匹配正则表达式的字段会被绑定到实体对象中，如果未指定字段正则表达式，则所有字段都会被绑定到实体对象中；
	 * <p>
	 * 查询实体对象的同时调用插件，执行业务逻辑；
	 * <p>
	 * 字段正则表达式从表达式中获取；
	 * 
	 * @param pager
	 *            查询分页器
	 * @param plugins
	 *            业务插件
	 * @return 对相集合
	 */
	public List<T> query(Pager<T> pager, IBizPlugin... plugins);

	/**
	 * 运行插件业务逻辑
	 * <p>
	 * 字段正则表达式会被传递到业务事件对象中；
	 * <p>
	 * 可以在插件中将执行结果设置到业务事件的返回值中；
	 * <p>
	 * 最后一次设置到业务事件中的返回值将作为方法调用的返回值；
	 * 
	 * @param plugins
	 *            业务插件
	 * @param fieldRexpr
	 *            字段正则表达式
	 * @return 执行结果
	 */
	public Object run(Object obj, CndExpr expr, IBizPlugin... plugins);

	/**
	 * 用数据库连接执行业务逻辑
	 * 
	 * @param conn
	 *            数据库连接回调对象
	 * @return 执行结果
	 */
	public Object run(NoTransConnCallback conn);

	public Object run(final OrmCallback callback);

	/**
	 * 异步保存： 其他文档参将对应的同步方法
	 */
	public void asynSave(T obj, IBizPlugin... plugins);

	/**
	 * 异步保存： 其他文档参将对应的同步方法
	 */
	public void asynSave(T obj, CndExpr fieldRexpr, IBizPlugin... plugins);

	/**
	 * 异步批量修改： 其他文档参将对应的同步方法
	 */
	public void asynUpdateMore(T obj, CndExpr expr, IBizPlugin... plugins);

	/**
	 * 异步删除： 其他文档参将对应的同步方法
	 */
	public void asynDelete(T obj, IBizPlugin... plugins);

	/**
	 * 异步批量删除： 其他文档参将对应的同步方法
	 */
	public void asynDeleteMore(Class<T> klass, CndExpr expr, IBizPlugin... plugins);

	/**
	 * 异步分页查询： 其他文档参将对应的同步方法
	 */
	public void asynQuery(Pager<T> pager, IBizPlugin... plugins);

	/**
	 * 异步运行： 其他文档参将对应的同步方法
	 */
	public void asynRun(Object obj, CndExpr fieldRexpr, IBizPlugin... plugins);

}
