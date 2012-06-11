package com.kmetop.demsy.orm.nutz.impl;

import static com.kmetop.demsy.Demsy.bizEngine;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.nutz.aop.ClassAgent;
import org.nutz.aop.DefaultClassDefiner;
import org.nutz.aop.MethodInterceptor;
import org.nutz.aop.MethodMatcher;
import org.nutz.aop.matcher.MethodMatcherFactory;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.DatabaseMeta;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Link;
import org.nutz.dao.impl.BaseNutzDao;
import org.nutz.dao.impl.DemsyLinks;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Each;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.trans.Atom;

import com.kmetop.demsy.ctx.aop.LazyClassAgent;
import com.kmetop.demsy.ctx.aop.asm.DemsyAsmClassAgent;
import com.kmetop.demsy.ctx.aop.asm.LazyAsmClassAgent;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IMetaDao;
import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.dialect.impl.SqlServerDialect;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.listener.EntityListeners;
import com.kmetop.demsy.orm.mapping.EnColumnMapping;
import com.kmetop.demsy.orm.mapping.EnMapping;
import com.kmetop.demsy.orm.nutz.EnColumnMappingImpl;
import com.kmetop.demsy.orm.nutz.EnMappingHolder;
import com.kmetop.demsy.orm.nutz.EnMappingImpl;
import com.kmetop.demsy.orm.nutz.EnMappingMaker;
import com.kmetop.demsy.orm.nutz.IExtDao;
import com.kmetop.demsy.orm.nutz.SaveManyInvoker;
import com.kmetop.demsy.orm.nutz.SaveManyManyInvoker;
import com.kmetop.demsy.orm.nutz.intercepter.ManyFieldGetterIntercepter;
import com.kmetop.demsy.orm.nutz.intercepter.OneFieldGetterIntercepter;

public class ExtDaoImpl extends BaseNutzDao implements IExtDao {
	private static final Log log = Logs.getLog(ExtDaoImpl.class);

	// 实体映射持有者
	private EnMappingHolder entityHolder;

	// 数据库本地方言
	private Dialect dialect;

	// 实体监听器： 可以监听增、删、改等操作
	private EntityListeners listeners;

	// 延迟实例化
	private MetaDaoImpl metaDao;

	private INamingStrategy namingStrategy;

	ExtDaoImpl(DataSource dataSource, EnMappingHolder holder, EnMappingMaker maker, EntityListeners listeners) {
		super();

		this.setDataSource(dataSource);
		this.entityHolder = holder;
		this.entityMaker = maker;
		this.namingStrategy = maker.getNamingStrategy();
		this.listeners = listeners;

		checkDatabase();// 检查数据库

		dialect = initDialect(meta);
	}

	// =======================================================================
	// 重写父类中的方法
	// =======================================================================

	public void execute(final Sql... sqls) {
		if (null == this.execurtor) {
			if (log.isWarnEnabled())
				log.warn("NULL Execurtor!");
			throw new NullPointerException("NULL Execurtor");
		}
		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			for (Sql sql : sqls) {
				sb.append("\n").append(sql);
			}
			log.debug(sb.substring(1));
		}
		execurtor.execute(dataSource, runner, sqls);
	}

	/**
	 * 重写父类的实现：插入实体对象到数据库中
	 * <p>
	 * 插入实体对象之前，调用实体监听器的insert方法；
	 */
	@Override
	protected void _insertSelf(Entity<?> entity, Object obj) {
		// 插入实体前调用监听器的insert方法
		EnMappingImpl en = (EnMappingImpl) entity;
		listeners.insertBefore(this, en, obj);
		// 执行父类中的相同方法
		super._insertSelf(entity, obj);

		listeners.insertAfter(this, en, obj);
	}

	// protected int _updateSelf(Entity<?> entity, Object ele) {
	// Sql sql = sqlMaker.update(entity, ele);
	// execute(sql);
	// return sql.getUpdateCount();
	// }
	//

	protected int _updateSelf(Entity<?> entity, Object obj) {
		EnMappingImpl en = (EnMappingImpl) entity;
		listeners.updateBefore(this, en, obj);

		Sql sql = sqlMaker.update(entity, obj);
		execute(sql);
		int ret = sql.getUpdateCount();

		listeners.updateAfter(this, en, obj);

		return ret;
	}

	/**
	 * 重写父类的实现：获取实体映射
	 * <p>
	 * 加载实体的同时自动同步数据库实体表、字段、外键等；
	 */
	@Override
	public <T> EnMappingImpl<T> getEntity(final Class<T> classOfT) {
		return this.getEnMapping(classOfT, true, true);
	}

	/**
	 * 并且实体类型等于...
	 * <p>
	 * 限制单表多类（即：一张表存储多个具有继承关系的多个实体类对象）的实体类型；
	 */
	private Condition andEnTypeEq(Class<?> classOfT, Condition cnd) {
		EnMappingImpl<?> entity = getEnMapping(classOfT);
		return andEnTypeEq(entity, cnd);
	}

	/**
	 * 并且实体类型等于...
	 * <p>
	 * 限制单表多类（即：一张表存储多个具有继承关系的多个实体类对象）的实体类型；
	 */
	private Condition andEnTypeEq(EnMappingImpl<?> entity, Condition cnd) {
		EnColumnMappingImpl field = entity.getDtype();
		if (field != null) {
			String name = field.getName();
			Number value = (Number) field.getValue(null);
			List<Number> valueList = new ArrayList();
			valueList.add(value);

			List<EnMappingImpl> children = entity.getChildren();
			if (children != null) {
				for (EnMappingImpl child : children) {
					EnColumnMappingImpl childFld = child.getDtype();
					if (childFld != null) {
						value = (Number) childFld.getValue(null);
						valueList.add(value);
					}
				}
			} else {
				if (entity.getParent() == null) {
					return cnd;
				}
			}
			if (cnd != null) {
				String sql1 = cnd.toSql(entity);
				if (sql1.toUpperCase().trim().startsWith("ORDER BY")) {
					return Cnd.wrap(Cnds.in(name, valueList).toSql(entity) + " " + sql1);
				}
				return Cnds.and(Cnds.in(name, valueList), cnd);
			} else {
				return Cnds.in(name, valueList);
			}
		}

		return cnd;
	}

	/**
	 * 重写父类的实现： 统计实体类记录数
	 * <p>
	 * 需限制单表多类（即：一张表存储多个具有继承关系的多个实体类对象）的实体类型；
	 */
	@Override
	public int count(Class<?> classOfT, Condition condition) {
		return super.count(classOfT, andEnTypeEq(classOfT, condition));
	}

	/**
	 * 重写父类的实现： 清空数据表
	 * <p>
	 * 需限制单表多类（即：一张表存储多个具有继承关系的多个实体类对象）的实体类型；
	 */
	@Override
	public int clear(Class classOfT, Condition condition) {
		EnMappingImpl<?> entity = getEnMapping(classOfT);
		condition = andEnTypeEq(entity, condition);
		Sql sql = getSqlMaker().clear(entity).setCondition(condition);
		execute(sql);
		return sql.getUpdateCount();
	}

	/**
	 * 重写父类的实现： 分页查询
	 * <p>
	 * 需限制单表多类（即：一张表存储多个具有继承关系的多个实体类对象）的实体类型；
	 */
	@Override
	public <T> List<T> query(Entity<?> entity, Condition condition, Pager pager) {
		return super.query(entity, andEnTypeEq((EnMappingImpl) entity, condition), pager);
	}

	// =======================================================================
	// 扩展接口中的方法
	// =======================================================================

	/**
	 * 初始化数据库方言：根据不同数据库类型初始化数据库方言
	 * <p>
	 * TODO: 目前只支持MSSQL数据库
	 */
	private Dialect initDialect(DatabaseMeta meta) {
		if (meta.isOracle()) {
		} else if (meta.isMySql()) {
		} else if (meta.isPostgresql()) {
		} else if (meta.isSqlServer()) {
			return new SqlServerDialect();
		} else if (meta.isH2()) {
		}
		return null;
	}

	@Override
	public Dialect getDialect() {
		return dialect;
	}

	@Override
	public <T> EnMappingImpl<T> getEnMapping(final Class<T> classOfT) {
		return this.getEnMapping(classOfT, true, true);
	}

	@Override
	public <T> EnMappingImpl<T> getEnMapping(final Class<T> classOfT, boolean syncTable) {
		return this.getEnMapping(classOfT, syncTable, false);
	}

	@Override
	public <T> EnMappingImpl<T> getEnMapping(final Class<T> classOfT, boolean syncTable, boolean syncRefTable) {
		EnMappingHolder entityHolder = getEntityHolder();
		EnMappingImpl<T> entity = entityHolder.getEnMapping(classOfT);
		if (null == entity) {
			entity = loadEntity(classOfT, syncTable, syncRefTable);
		} else {
			if (!entity.isSyncedTable() && syncTable) {
				if (!entity.isSyncedRefTable() && syncRefTable) {
					syncTable(entity, true);
				} else {
					syncTable(entity, false);
				}
			} else if (!entity.isSyncedRefTable() && syncRefTable) {
				syncRefTable(entity);
			}
		}

		return entity;
	}

	public <T> EnMappingImpl<T> loadEntity(Class<T> classOfT, boolean autoCreateTable) {
		return this.loadEntity(classOfT, autoCreateTable, false);
	}

	private <T> EnMappingImpl<T> loadEntity(Class<T> classOfT, boolean syncTable, boolean syncRefTable) {
		String enInfo = Cls.getDisplayName(classOfT);
		log.tracef("加载%s实体......[syncTable=%s, syncRefTable=%s]", enInfo, syncTable, syncRefTable);

		EnMappingImpl<T> entity = (EnMappingImpl<T>) ((EnMappingMaker) entityMaker).make(getParentEntity(classOfT, syncTable, syncRefTable), classOfT);
		if (entity != null) {
			// 使用代理类缓存实体
			Class agentClass = classOfT;
			try {
				agentClass = agentClass(entity);
			} catch (Throwable e) {// 类不能被代理：使用自身类换成实体
				log.tracef("加载%s实体: <不能创建实体代理类>%s", enInfo, Ex.msg(e));
			}

			entityHolder.cacheEntity(agentClass, entity);
			entityHolder.cacheAgent(classOfT, agentClass);

			// 检查实体依赖
			checkFK(entity);

			// 同步表
			if (syncTable) {
				syncTable(entity, syncRefTable);
			} else if (syncRefTable) {
				syncRefTable(entity);
			}

			// 加载子类实体
			List<Class<?>> list = bizEngine.listTypes();
			if (list != null) {
				for (Class cls : list) {
					if (!classOfT.equals(cls) && classOfT.isAssignableFrom(cls)) {
						loadEntity(cls, syncTable, syncRefTable);
					}
				}
			}
		}

		log.infof("加载%s实体. [syncTable=%s, syncRefTable=%s]", enInfo, syncTable, syncRefTable);

		return (EnMappingImpl<T>) entity;
	}

	private <T> EnMappingImpl<T> getParentEntity(Class<T> classOfT, boolean syncTable, boolean syncRefTable) {
		Class parentType = classOfT.getSuperclass();

		EnMappingImpl<T> parentEntity = entityHolder.getEnMapping(parentType);
		if (parentEntity == null) {
			javax.persistence.Entity ann = (javax.persistence.Entity) parentType.getAnnotation(javax.persistence.Entity.class);
			if (ann != null) {
				log.tracef("加载%s父实体... [parentType=%s]", classOfT.getSimpleName(), parentType.getSimpleName());

				parentEntity = loadEntity(parentType, syncTable, syncRefTable);
			}
		}

		return parentEntity;
	}

	private void syncTable(EnMappingImpl entity, boolean syncRefTable) {
		EnMappingImpl parentEntity = entity.getParent();
		if (parentEntity != null) {
			if (!parentEntity.isSyncedTable()) {
				syncTable(parentEntity, syncRefTable);
			} else if (!parentEntity.isSyncedRefTable() && syncRefTable) {
				syncRefTable(parentEntity);
			}
		}

		getMetaDao().syncTables(entity, syncRefTable);
	}

	private void syncRefTable(EnMappingImpl entity) {
		EnMappingImpl parentEntity = entity.getParent();
		if (parentEntity != null && !parentEntity.isSyncedRefTable()) {
			syncRefTable(parentEntity);
		}

		getMetaDao().syncTables(entity, true);
	}

	private void checkFK(EnMappingImpl entity) {
		String enInfo = Cls.getDisplayName(entity.getType());
		log.tracef("加载%s实体: 检查关联实体...", enInfo);

		List<Link> links = entity.getLinks(null);
		// 检查实体应用到的外键
		for (Link link : links) {
			Class targetClass = link.getTargetClass();

			log.tracef("加载%s实体: 处理%s关联实体%s [columnName=%s]", enInfo, (link.isOne() ? "<ONE>" : link.isMany() ? "<MANY>" : "MANY2MANY"), Cls.getDisplayName(targetClass),
					Cls.getDisplayName(link.getOwnField()));

			EnMappingImpl refEntity = entityHolder.getEnMapping(targetClass);
			if (refEntity != null) {
				List<Link> targetEntityLinks = refEntity.getLinks(null);
				if (targetEntityLinks != null) {
					for (Link targetLink : targetEntityLinks) {
						if (link.getOwnField().getName().equals(targetLink.get("mappedBy")) && targetLink.getTargetClass().equals(entity.getType())) {
							if (log.isTraceEnabled()) {
								log.tracef("<%s.%s> --> <%s.%s>", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(refEntity.getType()), Cls.getDisplayName(targetLink.getOwnField()));
							}
							link.set("mappedLink", targetLink);
							targetLink.set("mappedLink", link);
						}
					}
				}
			}
			if (link.isOne()) {
				EnColumnMappingImpl ef = (EnColumnMappingImpl) link.get(EnColumnMapping.class.getSimpleName());
				String columnName = ef.getColumnName();
				entity.addForeignKey(columnName, link);
			}
		}

		log.tracef("加载%s实体: 检查关联实体: 结束.", enInfo);
	}

	// 创建代理类
	private Class agentClass(EnMappingImpl entity) {
		Class cls = entity.getType();
		if (Cls.isAgent(cls)) {
			return cls;
		}

		String enInfo = Cls.getDisplayName(cls);
		log.tracef("加载%s实体: 创建实体代理类...", enInfo);

		// 代理类
		ClassAgent classAgent = new DemsyAsmClassAgent();
		LazyClassAgent lazyClassAgent = new LazyAsmClassAgent();

		List<Link> links = entity.getLinks(null);
		if (links != null) {
			for (Link link : links) {
				String fieldName = link.getOwnField().getName();
				fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

				MethodInterceptor interceptor = null;
				if (link.isOne()) {
					interceptor = new OneFieldGetterIntercepter(this, link);
				} else {
					interceptor = new ManyFieldGetterIntercepter(this, link);
				}
				MethodMatcher matcher = MethodMatcherFactory.matcher("get" + fieldName + "$");

				classAgent.addInterceptor(matcher, interceptor);
				lazyClassAgent.addInterceptor(matcher, interceptor);
			}
		}

		Class agentClass = classAgent.define(new DefaultClassDefiner(cls.getClassLoader()), cls);
		Class lazyAgentClass = lazyClassAgent.define(new DefaultClassDefiner(cls.getClassLoader()), cls);
		entity.setAgentMirror(Mirror.me(agentClass));
		entity.setLazyAgentMirror(Mirror.me(lazyAgentClass));

		log.tracef("加载%s实体: 创建实体代理类: 结束. [result=%s]", enInfo, agentClass.getSimpleName());

		return agentClass;
	}

	@Override
	public synchronized EnMappingHolder getEntityHolder() {
		return entityHolder;
	}

	@Override
	public EnMappingMaker getEntityMaker() {
		return (EnMappingMaker) entityMaker;
	}

	/**
	 * 默认使用加密命名策略
	 * <p>
	 * 即对数据库表、字段进行混淆
	 */
	@Override
	public INamingStrategy getNamingStrategy() {
		return namingStrategy;
	}

	@Override
	public int save(Object obj) {
		return save(obj, null, true);
	}

	@Override
	public int save(Object obj, final String fieldRexpr, final boolean igloreNull) {
		int len = Lang.length(obj);
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, Object ele, int length) {
				if (Obj.isEmpty(getEntity(ele.getClass()), ele)) {
					insert(ele, fieldRexpr, igloreNull);
				} else {
					update(ele, fieldRexpr, igloreNull);
				}
			}
		});
		return len;
	}

	@Override
	public int insert(Object obj, final String fieldRexpr, final boolean igloreNull) {
		int len = Lang.length(obj);
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, final Object ele, int length) {
				FieldFilter.create(Cls.getType(ele.getClass()), fieldRexpr, igloreNull).run(new Atom() {
					public void run() {
						Entity<?> entity = getEntity(ele.getClass());
						_insertSelf(entity, ele);
					}
				});
			}
		});
		return len;
	}

	@Override
	public int update(Object obj, final String fieldRexpr, final boolean igloreNull) {
		int len = Lang.length(obj);
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, final Object ele, int length) {
				FieldFilter.create(Cls.getType(ele.getClass()), fieldRexpr, igloreNull).run(new Atom() {
					public void run() {
						Entity<?> entity = getEntity(ele.getClass());
						_updateSelf(entity, ele);
					}
				});
			}
		});
		return len;
	}

	@Override
	public int update(Object obj, final String fieldRexpr, final boolean igloreNull, final Condition cnd) {
		int len = Lang.length(obj);
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, final Object ele, int length) {
				FieldFilter.create(Cls.getType(ele.getClass()), fieldRexpr, igloreNull).run(new Atom() {
					public void run() {
						Chain chain = Chain.from(ele);
						update(Cls.getType(ele.getClass()), chain, cnd);
					}
				});
			}
		});
		return len;
	}

	@Override
	public int saveLinks(Object obj, final String regex) {
		int len = Lang.length(obj);
		final IExtDao dao = this;
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, final Object ele, int length) {
				final Entity<?> entity = getEntity(ele.getClass());
				final DemsyLinks lns = new DemsyLinks(ele, entity, regex);
				final Mirror<?> mirror = Mirror.me(ele.getClass());
				// lns.invokeOnes(new SaveOneInvoker(dao, ele, mirror));
				lns.invokeManys(new SaveManyInvoker(dao, ele, mirror));
				lns.invokeManyManys(new SaveManyManyInvoker(dao, ele, mirror));
			}
		});
		return len;
	}

	@Override
	public int insertRelations(Object obj, String regex) {
		this.insertRelation(obj, regex);
		return 1;
	}

	@Override
	public int deleteMany(Object obj, final String fieldRexpr) {
		int len = Lang.length(obj);
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, Object ele, int length) {
				EnMappingImpl entity = getEnMapping(ele.getClass());
				List<Link> links = entity.getLinks(fieldRexpr);
				if (links != null) {
					for (Link link : links) {
						if (link.isManyMany() || link.isMany()) {
							getEntity(link.getTargetClass());
							clearLinks(ele, link.getOwnField().getName());
						}
					}
				}
			}
		});
		return len;
	}

	@Override
	public int deleteRelations(Object obj, final String regex) {
		int len = Lang.length(obj);
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, Object ele, int length) {
				EnMappingImpl entity = getEnMapping(ele.getClass());
				List<Link> links = entity.getLinks(regex);
				if (links != null) {
					for (Link link : links) {
						if (link.isManyMany()) {
							getEntity(link.getTargetClass());
							clearLinks(ele, link.getOwnField().getName());
						}
					}
				}
			}
		});
		return len;
	}

	@Override
	public <T> List<T> query(final Class<T> classOfT, String fieldRexpr, final Condition condition, final Pager pager) {
		if (Str.isEmpty(fieldRexpr)) {
			return query(classOfT, condition, pager);
		}
		final List<List> ret = new ArrayList();
		FieldFilter.create(classOfT, fieldRexpr).run(new Atom() {
			public void run() {
				ret.add(query(classOfT, condition, pager));
			}
		});
		return ret.get(0);
	}

	@Override
	public <T> T fetch(final Class<T> classOfT, String fieldRexpr, final long id) {
		if (Str.isEmpty(fieldRexpr)) {
			return fetch(classOfT, id);
		}
		final List<T> ret = new ArrayList();
		FieldFilter.create(classOfT, fieldRexpr).run(new Atom() {
			public void run() {
				ret.add(fetch(classOfT, id));
			}
		});
		return ret.get(0);
	}

	// public <T> T fetch(Entity<T> entity, long id) {
	// return this.fetch(entity, Cnd.where(((EnMappingImpl)
	// entity).getIdProperty(), "=", id));
	// }

	@Override
	public <T> T fetch(final Class<T> classOfT, String fieldRexpr, final Condition cnd) {
		if (Str.isEmpty(fieldRexpr)) {
			return fetch(classOfT, cnd);
		}
		final List<T> ret = new ArrayList();
		FieldFilter.create(classOfT, fieldRexpr).run(new Atom() {
			public void run() {
				ret.add(fetch(classOfT, cnd));
			}
		});
		return ret.get(0);
	}

	public int delete(Object obj) {
		int len = Lang.length(obj);
		Lang.each(obj, new Each<Object>() {
			public void invoke(int i, Object ele, int length) {
				EnMappingImpl entity = getEnMapping(ele.getClass());
				_deleteSelf(entity, ele);
			}
		});
		return len;
	}

	@Override
	public Object run(final NoTransConnCallback call) {
		// Connection conn = null;
		// try {
		// conn = dataSource.getConnection();
		// return call.invoke(conn);
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// } finally {
		// if (conn != null)
		// try {
		// conn.close();
		// } catch (Throwable e) {
		// }
		// }
		final List ret = new ArrayList();
		run(new org.nutz.dao.ConnCallback() {
			public void invoke(Connection conn) throws Exception {
				ret.add(call.invoke(conn));
			}

		});
		return ret.get(0);
	}

	@Override
	public String getIdProperty(Class klass) {
		EnMapping m = getEnMapping(klass);
		if (m == null) {
			return null;
		}
		return m.getIdProperty();
	}

	public synchronized IMetaDao getMetaDao() {
		if (metaDao == null)
			metaDao = new MetaDaoImpl(this);
		return metaDao;
	}

}
