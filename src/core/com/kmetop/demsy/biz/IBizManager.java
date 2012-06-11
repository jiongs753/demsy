package com.kmetop.demsy.biz;

import java.util.List;

import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;

/**
 * 业务管理器:
 * <UL>
 * <LI>自动加载操作码对应的业务插件，以便执行操作的时候执行相关的业务逻辑；
 * <LI>自动加载操作码对应的字段过滤表达式，以便只对特定的字段进行业务操作；
 * </UL>
 * 
 * @author yongshan.ji
 * @param <T>
 */
public interface IBizManager<T> {
	public <X> IBizManager<X> me(IOrm orm);

	public IBizSession bizSession();

	public IOrm orm();

	public Class getType();

	public IModule getModule();

	public IBizSystem getSystem();

	/**
	 * 保存实体对象： 可以保存单个实体、实体集合、数组、Map等。
	 * <p>
	 * 不存在的关联(Many/ManyMany)对象将“会”被同步保存；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param actionID
	 *            操作码
	 * @return 保存了多少条记录
	 */
	public int save(T obj, String actionID) throws DemsyException;

	/**
	 * 批量修改： 修改满足条件的数据记录。
	 * <p>
	 * 不存在的关联(Many/ManyMany)对象将“会”被同步保存；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象: 存放最新字段值
	 * @param actionID
	 *            操作码
	 * @param expr
	 *            表达式: 用于描述满足条件的记录、要修改的字段等
	 * @return 修改了多少条数据
	 */
	public int updateMore(T obj, String actionID, CndExpr expr) throws DemsyException;

	/**
	 * 删除实体对象： 可以删除单个数据实体、实体集合、数组、Map等。
	 * <p>
	 * 关联(Many/ManyMany)对象将“不会”被同步删除；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param actionID
	 *            操作码
	 * @return 删除了多少条记录
	 */
	public int delete(T obj, String actionID) throws DemsyException;

	/**
	 * 删除实体对象： 删除实体ID与指定ID相同的对象
	 * <p>
	 * 关联(Many/ManyMany)对象将“不会”被同步删除；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param actionID
	 *            操作码
	 * @param expr
	 *            表达式
	 * @return 删除了多少条记录
	 * @throws DemsyException
	 */
	public int delete(Long id, String actionID) throws DemsyException;

	/**
	 * 批量删除： 删除满足条件的实体数据记录
	 * <p>
	 * 关联(Many/ManyMany)对象将“不会”被同步删除；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param actionID
	 *            操作码
	 * @param expr
	 *            表达式
	 * @return 删除了多少条记录
	 */
	public int deleteMore(String actionID, CndExpr expr) throws DemsyException;

	/**
	 * 加载实体对象
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param actionID
	 *            操作码
	 * @param id
	 *            实体ID
	 * @return 实体对象
	 */
	public T load(Long id, String actionID) throws DemsyException;

	public int count(String actionID, CndExpr expr) throws DemsyException;

	public List<T> query(String actionID, CndExpr expr) throws DemsyException;

	/**
	 * 查询分页数据集
	 * <p>
	 * 查询条件或查询字段从分页器中获取；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param pager
	 *            查询分页器
	 * @param actionID
	 *            操作码
	 * @return 查询到的结果集
	 */
	public List<T> query(Pager<T> pager, String actionID) throws DemsyException;

	/**
	 * 执行业务逻辑，最后一个业务插件设置到业务事件中的返回值就是执行的结果。
	 * 
	 * @param actionID
	 *            操作码
	 * @return 执行结果
	 */
	public Object run(Object obj, String actionID) throws DemsyException;

	/**
	 * 异步保存实体对象： 可以保存单个实体、实体集合、数组、Map等。
	 * <p>
	 * 不存在的关联(Many/ManyMany)对象将被同步保存；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param actionID
	 *            操作码
	 */
	public void asynSave(Object obj, String actionID) throws DemsyException;

	/**
	 * 异步批量修改： 修改满足条件的数据记录。
	 * <p>
	 * 同步保存不存在的关联(Many/ManyMany)对象；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            单个实体对象
	 * @param actionID
	 *            操作码
	 * @param igloreNull
	 *            是否忽略空值
	 * @param expr
	 *            条件表达式
	 */
	public void asynUpdateMore(T obj, String actionID, CndExpr expr) throws DemsyException;

	/**
	 * 异步删除实体对象： 可以删除单个数据实体、实体集合、数组、Map等。
	 * <p>
	 * 同步删除关联(Many/ManyMany)对象；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param actionID
	 *            操作码
	 * @return 删除了多少条记录
	 */
	public void asynDelete(Object obj, String actionID) throws DemsyException;

	/**
	 * 异步批量删除： 删除满足条件的实体数据记录
	 * <p>
	 * 关联对象“不会被删除”；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param actionID
	 *            操作码
	 * @param expr
	 *            表达式
	 */
	public void asynDeleteMore(String actionID, CndExpr expr) throws DemsyException;

	/**
	 * 异步查询分页数据集
	 * <p>
	 * 查询条件或查询字段从分页器中获取；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param pager
	 *            查询分页器
	 * @param actionID
	 *            操作码
	 */
	public void asynQuery(Pager<T> pager, String actionID) throws DemsyException;

	/**
	 * 异步执行业务逻辑，最后一个业务插件设置到业务事件中的返回值就是执行的结果。
	 * 
	 * @param actionID
	 *            操作码
	 */
	public void asynRun(Object obj, String actionID) throws DemsyException;
}
