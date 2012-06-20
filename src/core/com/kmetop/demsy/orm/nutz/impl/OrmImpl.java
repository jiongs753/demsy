package com.kmetop.demsy.orm.nutz.impl;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.nutz.dao.Condition;

import com.kmetop.demsy.config.IDataSource;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.ConfigException;
import com.kmetop.demsy.lang.JSON;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.IMetaDao;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.NullCndExpr;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.listener.EntityListeners;
import com.kmetop.demsy.orm.mapping.EnMapping;
import com.kmetop.demsy.orm.nutz.EnMappingHolder;
import com.kmetop.demsy.orm.nutz.EnMappingMaker;
import com.kmetop.demsy.orm.nutz.IExtDao;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class OrmImpl implements IOrm {
	private Log log = Logs.getLog(OrmImpl.class);

	// 依赖注入
	private IExtDao dao;

	public OrmImpl(IDataSource config, EnMappingHolder holder, EnMappingMaker maker, EntityListeners listeners) {
		try {
			dao = new ExtDaoImpl(getComboPooledDataSource(config), holder, maker, listeners);
		} catch (Throwable e) {
			throw new ConfigException(Ex.msg(e));
		}
	}

	protected DataSource getComboPooledDataSource(IDataSource config) {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setJdbcUrl(config.getUrl());
		try {
			ds.setDriverClass(config.getDriver());
		} catch (PropertyVetoException e) {
			log.errorf("创建数据库连接池出错! %s", Ex.msg(e));
		}

		Properties props = new Properties();

		Properties configProps = config.getProperties();
		if (configProps != null) {
			props.putAll(configProps);
		}

		if (props.getProperty("c3p0.testConnectionOnCheckout") == null)
			props.put("c3p0.testConnectionOnCheckout", "true");
		if (props.getProperty("c3p0.max_statement") == null)
			props.put("c3p0.max_statement", "500");
		if (props.getProperty("c3p0.timeout") == null)
			props.put("c3p0.timeout", "1000");
		if (props.getProperty("c3p0.initialPoolSize") == null)
			props.put("c3p0.initialPoolSize", "3");
		if (props.getProperty("c3p0.minPoolSize") == null)
			props.put("c3p0.minPoolSize", "5");
		if (props.getProperty("c3p0.maxPoolSize") == null)
			props.put("c3p0.maxPoolSize", "50");
		props.put("user", config.getUser());
		props.put("password", config.getPwd());

		ds.setProperties(props);
		/*
		 * 初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 5
		 */
		ds.setInitialPoolSize(Integer.parseInt(props.getProperty("c3p0.initialPoolSize")));
		/*
		 * 连接池中保留的最大连接数。Default: 15
		 */
		ds.setMinPoolSize(Integer.parseInt(props.getProperty("c3p0.minPoolSize")));
		/*
		 * 连接池中保留的最大连接数。Default: 1000
		 */
		ds.setMaxPoolSize(Integer.parseInt(props.getProperty("c3p0.maxPoolSize")));
		// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。
		ds.setAcquireIncrement(3);
		// 定义在从数据库获取新连接失败后重复尝试的次数
		ds.setAcquireRetryAttempts(100);
		// 次连接中间隔时间，单位毫秒
		ds.setAcquireRetryDelay(100);
		// 连接关闭时默认将所有未提交的操作提交
		ds.setAutoCommitOnClose(false);
		/*
		 * c3p0将建一张名为Test的空表，并使用其自带的查询语句进行测试。如果定义了这个参数那么
		 * 属性preferredTestQuery将被忽略。你不能在这张Test表上进行任何操作，它将只供c3p0测试 使用。Default:
		 * null
		 */
		ds.setAutomaticTestTable("demsy_c3p0_test");
		/*
		 * 获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效
		 * 保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试
		 * 获取连接失败后该数据源将申明已断开并永久关闭。Default: false
		 */
		ds.setBreakAfterAcquireFailure(true);
		/*
		 * 当连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时后将抛出
		 * SQLException,如设为0则无限期等待。单位毫秒。Default: 0 等待2分钟后抛异常
		 */
		ds.setCheckoutTimeout(120000);
		/*
		 * 通过实现ConnectionTester或QueryConnectionTester的类来测试连接。类名需制定全路径。 Default:
		 * com.mchange.v2.c3p0.impl.DefaultConnectionTester
		 */
		// ds.setConnectionTesterClassName();
		/*
		 * 最大空闲时间,XXX秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0 5分钟未使用则丢弃
		 */
		ds.setMaxIdleTime(300);

		ds.setIdleConnectionTestPeriod(30);
		/*
		 * JDBC的标准参数，用以控制数据源内加载的PreparedStatements数量。但由于预缓存的statements
		 * 属于单个connection而不是整个连接池。所以设置这个参数需要考虑到多方面的因素。
		 * 如果maxStatements与maxStatementsPerConnection均为0，则缓存被关闭。Default: 0
		 */
		ds.setMaxStatements(100);
		/*
		 * maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。Default: 0
		 */
		ds.setMaxStatementsPerConnection(10);
		/*
		 * c3p0是异步操作的，缓慢的JDBC操作通过帮助进程完成。扩展这些操作可以有效的提升性能
		 * 通过多线程实现多个操作同时被执行。Default: 5
		 */
		ds.setNumHelperThreads(5);

		return ds;
	}

	@Override
	public int save(Object obj, NullCndExpr fieldRexpr) {
		if (log.isTraceEnabled())
			log.tracef("保存%s......[obj: %s, fieldRexpr: %s]", Cls.getDisplayName(obj.getClass()), JSON.toJson(obj),
					fieldRexpr);

		int result = dao.save(obj, Cnds.fieldRexpr(fieldRexpr), Cnds.isIgloreNull(fieldRexpr));

		// result += dao.deleteRelations(obj, null);
		result += dao.saveLinks(obj, null);

		if (log.isDebugEnabled())
			log.debugf("保存%s成功!! [result: %s, fieldRexpr: %s]", Cls.getDisplayName(obj.getClass()), result, fieldRexpr);
		if (log.isTraceEnabled())
			log.trace(JSON.toJson(obj));

		return result;
	}

	@Override
	public int save(Object obj) {
		return this.save(obj, null);
	}

	@Override
	public int insert(Object obj, NullCndExpr fieldRexpr) {
		if (log.isTraceEnabled())
			log.tracef("新增%s......[obj: %s, fieldRexpr: %s]", Cls.getDisplayName(obj.getClass()), JSON.toJson(obj),
					fieldRexpr);

		int result = dao.insert(obj, Cnds.fieldRexpr(fieldRexpr), Cnds.isIgloreNull(fieldRexpr));
		// result += dao.deleteRelations(obj, null);
		result += dao.saveLinks(obj, null);

		if (log.isDebugEnabled())
			log.debugf("新增%s成功! [result: %s, fieldRexpr: %s]", Cls.getDisplayName(obj.getClass()), result, fieldRexpr);
		if (log.isTraceEnabled())
			log.trace(JSON.toJson(obj));

		return result;
	}

	@Override
	public int insert(Object obj) {
		return this.insert(obj, null);
	}

	@Override
	public int update(Object obj, NullCndExpr fieldRexpr) {
		if (log.isTraceEnabled())
			log.tracef("修改%s......[obj: %s, fieldRexpr: %s]", Cls.getDisplayName(obj.getClass()), JSON.toJson(obj),
					fieldRexpr);

		int result = dao.update(obj, Cnds.fieldRexpr(fieldRexpr), Cnds.isIgloreNull(fieldRexpr));
		// result += dao.deleteRelations(obj, null);
		result += dao.saveLinks(obj, null);

		if (log.isDebugEnabled())
			log.debugf("修改%s成功! [result: %s, fieldRexpr: %s]", Cls.getDisplayName(obj.getClass()), result, fieldRexpr);
		if (log.isTraceEnabled())
			log.trace(JSON.toJson(obj));

		return result;
	}

	@Override
	public int update(Object obj) {
		return this.update(obj, null);
	}

	@Override
	public int updateMore(Object obj, CndExpr expr) {
		Condition cnd = Cnds.toCnd(expr);
		if (log.isTraceEnabled())
			log.tracef("批量修改%s......[obj: %s, expr: %s]", Cls.getDisplayName(obj.getClass()), JSON.toJson(obj),
					cnd == null ? "" : cnd.toSql(null));

		int result = dao.update(obj, Cnds.fieldRexpr(expr), Cnds.isIgloreNull(expr), cnd);
		result += dao.deleteRelations(obj, null);
		result += dao.saveLinks(obj, null);

		if (log.isDebugEnabled())
			log.debugf("批量修改%s成功! %s [result: %s]", Cls.getDisplayName(obj.getClass()),
					cnd == null ? "" : cnd.toSql(null), result);
		if (log.isTraceEnabled())
			log.trace(JSON.toJson(obj));

		return result;
	}

	@Override
	public int delete(Object obj) {
		if (log.isTraceEnabled())
			log.tracef("删除%s......[obj: %s]", Cls.getDisplayName(obj.getClass()), JSON.toJson(obj));

		int result = dao.deleteMany(obj, null);
		result += dao.delete(obj);

		if (log.isDebugEnabled())
			log.debugf("删除%s成功! [result: %s]", Cls.getDisplayName(obj.getClass()), result);
		if (log.isTraceEnabled())
			log.trace(JSON.toJson(obj));

		return result;
	}

	@Override
	public int delete(Class klass, Serializable id) {
		return this.delete(this.load(klass, id, null));
	}

	@Override
	public int deleteMore(Class classOfEntity) {
		if (log.isDebugEnabled()) {
			log.tracef("批量删除%s......", Cls.getDisplayName(classOfEntity));

			int result = dao.clear(classOfEntity);

			log.debugf("批量删除%s成功! [result: %s]", Cls.getDisplayName(classOfEntity), result);

			return result;
		} else
			return dao.clear(classOfEntity);
	}

	@Override
	public int deleteMore(Class klass, CndExpr expr) {
		Condition cnd = Cnds.toCnd(expr);
		if (log.isDebugEnabled()) {
			log.tracef("批量删除%s......[expr: %s]", Cls.getDisplayName(klass), cnd == null ? "" : cnd.toSql(null));

			int result = dao.clear(klass, cnd);

			log.debugf("批量删除%s成功! %s [result: %s]", Cls.getDisplayName(klass), cnd == null ? "" : cnd.toSql(null),
					result);

			return result;
		} else
			return dao.clear(klass, cnd);
	}

	@Override
	public Object load(Class klass, Serializable id) {
		return this.load(klass, id, null);
	}

	@Override
	public Object load(Class klass, Serializable id, CndExpr fieldRexpr) {
		if (log.isDebugEnabled()) {
			log.tracef("加载%s......[id: %s, fieldRexpr: %s]", Cls.getDisplayName(klass), id, fieldRexpr);

			Object result = dao.fetch(klass, Cnds.fieldRexpr(fieldRexpr), Long.parseLong(id.toString()));

			log.debugf("加载%s成功! [id: %s, fieldRexpr: %s]", Cls.getDisplayName(klass), id, fieldRexpr);
			if (log.isTraceEnabled())
				log.trace(JSON.toJson(result));

			return result;
		} else
			return dao.fetch(klass, Cnds.fieldRexpr(fieldRexpr), Long.parseLong(id.toString()));
	}

	@Override
	public Object load(Class klass) {
		return this.load(klass, (CndExpr) null);
	}

	@Override
	public Object load(Class klass, CndExpr expr) {
		Condition cnd = Cnds.toCnd(expr);
		if (log.isDebugEnabled()) {
			log.tracef("加载%s......[expr: %s]", Cls.getDisplayName(klass), cnd == null ? "" : cnd.toSql(null));

			Object result = dao.fetch(klass, cnd);

			log.debugf("加载%s成功! %s", Cls.getDisplayName(klass), cnd == null ? "" : cnd.toSql(null));
			if (log.isTraceEnabled())
				log.trace(JSON.toJson(result));

			return result;
		} else
			return dao.fetch(klass, cnd);
	}

	@Override
	public List query(Class classOfEntity) {
		return query(classOfEntity, null);
	}

	@Override
	public List query(Class classOfEntity, CndExpr expr) {
		Condition cnd = Cnds.toCnd(expr);
		if (log.isDebugEnabled()) {
			log.tracef("查询%s......[expr: %s]", Cls.getDisplayName(classOfEntity), cnd == null ? "" : cnd.toSql(null));

			List result = dao.query(classOfEntity, Cnds.fieldRexpr(expr), cnd, Cnds.toPager(dao, expr));

			log.debugf("查询%s成功! %s [totalRecord: %s]", Cls.getDisplayName(classOfEntity),
					cnd == null ? "" : cnd.toSql(null), result == null ? 0 : result.size());

			return result;
		} else
			return dao.query(classOfEntity, Cnds.fieldRexpr(expr), cnd, Cnds.toPager(dao, expr));
	}

	@Override
	public List query(Pager pager) {
		CndExpr expr = pager.getQueryExpr();
		String fieldRexpr = Cnds.fieldRexpr(expr);
		Class klass = pager.getType();

		Condition cnd = Cnds.toCnd(expr);

		if (log.isTraceEnabled())
			log.tracef("分页查询%s......[expr: %s, fieldRexpr: %s]", Cls.getDisplayName(klass),
					cnd == null ? "" : cnd.toSql(null), fieldRexpr);

		int totalRecord = dao.count(klass, Cnds.cnd(expr));
		pager.setTotalRecord(totalRecord);
		pager.setResult(dao.query(klass, fieldRexpr, cnd, Cnds.toPager(dao, expr)));

		if (log.isDebugEnabled())
			log.debugf("分页查询%s成功! %s [fieldRexpr: %s, totalRecord: %s, pageRecord: %s]", Cls.getDisplayName(klass),
					cnd == null ? "" : cnd.toSql(null), fieldRexpr, totalRecord, pager.getResult().size());

		return pager.getResult();
	}

	@Override
	public int count(Class classOfEntity) {
		return this.count(classOfEntity, null);
	}

	@Override
	public int count(Class classOfEntity, CndExpr expr) {
		Condition cnd = Cnds.toCnd(expr);
		if (log.isDebugEnabled()) {
			log.tracef("统计%s......[expr: %s]", Cls.getDisplayName(classOfEntity), cnd == null ? "" : cnd.toSql(null));

			int result = dao.count(classOfEntity, cnd);

			log.debugf("统计%s成功! %s [result: %s]", Cls.getDisplayName(classOfEntity),
					cnd == null ? "" : cnd.toSql(null), result);

			return result;
		} else {
			return dao.count(classOfEntity, cnd);
		}
	}

	@Override
	public Object run(final NoTransConnCallback call) {
		return dao.run(call);
	}

	@Override
	public String getIdProperty(Class klass) {
		return dao.getEnMapping(klass).getIdProperty();
	}

	@Override
	public EnMapping getEnMapping(Class classOfT) {
		return this.getEnMapping(classOfT, true, true);
	}

	@Override
	public EnMapping getEnMapping(Class classOfT, boolean syncTable) {
		return this.getEnMapping(classOfT, syncTable, false);
	}

	@Override
	public EnMapping getEnMapping(Class classOfT, boolean syncTable, boolean syncRefTable) {
		if (log.isTraceEnabled())
			log.tracef("获取%s实体......[syncTable: %s, syncRefTable: %s]", Cls.getDisplayName(classOfT), syncTable,
					syncRefTable);

		return dao.getEnMapping(classOfT, syncTable, syncRefTable);
	}

	@Override
	public Dialect getDialect() {
		return dao.getDialect();
	}

	@Override
	public INamingStrategy getNamingStrategy() {
		return dao.getNamingStrategy();
	}

	public IExtDao getDao() {
		return dao;
	}

	@Override
	public IMetaDao getMetaDao() {
		return dao.getMetaDao();
	}

	@Override
	public void removeMapping(Class cls) {
		dao.getEntityHolder().remove(cls);
	}

	@Override
	public void clearMapping() {
		dao.getEntityHolder().clear();
	}
}