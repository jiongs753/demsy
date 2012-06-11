package org.nutz.dao.impl;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.nutz.castor.Castors;
import org.nutz.dao.Chain;
import org.nutz.dao.Condition;
import org.nutz.dao.ConnCallback;
import org.nutz.dao.DaoException;
import org.nutz.dao.DaoExecutor;
import org.nutz.dao.DaoRunner;
import org.nutz.dao.Daos;
import org.nutz.dao.DatabaseMeta;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.FieldMatcher;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.EntityHolder;
import org.nutz.dao.entity.EntityMaker;
import org.nutz.dao.entity.Link;
import org.nutz.dao.entity.Record;
import org.nutz.dao.entity.impl.DefaultEntityMaker;
import org.nutz.dao.entity.next.FieldQuery;
import org.nutz.dao.pager.DefaultPagerMaker;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.pager.PagerMaker;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlMaker;
import org.nutz.lang.Each;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.kmetop.demsy.comlib.biz.field.SubSystem;

/**
 * @author zozoh(zozohtnt@gmail.com)
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public class BaseNutzDao extends NutDao {

	protected static final Log log = Logs.get();

	protected DataSource dataSource;

	protected SqlMaker sqlMaker;

	protected PagerMaker pagerMaker;

	protected SqlManager sqls;

	protected EntityHolder entities;

	protected DatabaseMeta meta;

	protected EntityMaker entityMaker;

	protected DaoRunner runner;

	protected DaoExecutor execurtor;

	/* ========================================================== */
	public BaseNutzDao() {
		this.sqlMaker = new SqlMaker();
		this.pagerMaker = new DefaultPagerMaker();
		this.runner = new DefaultDaoRunner();
		this.execurtor = new DefaultDaoExecutor();
	}

	public BaseNutzDao(DataSource dataSource) {
		this();
		this.setDataSource(dataSource);
	}

	public BaseNutzDao(DataSource dataSource, SqlManager sqlManager) {
		this();
		this.setDataSource(dataSource);
		this.setSqlManager(sqlManager);
	}

	/* ========================================================== */
	protected static <T> EntityField checkIdField(Entity<T> en) {
		EntityField idField = en.getIdField();
		if (idField == null) {
			throw Lang.makeThrow("Entity<T> [%s] need @Id field", en.getMirror().getType().getName());
		}
		return idField;
	}

	protected static <T> EntityField checkNameField(Entity<T> en) {
		EntityField nameField = en.getNameField();
		if (nameField == null) {
			throw Lang.makeThrow("Entity<T> [%s] need @Name field", en.getMirror().getType().getName());
		}
		return nameField;
	}

	protected void checkPKs(Entity<?> entity, Object[] values) {
		if (null == values)
			throw Lang.makeThrow("fetchx<%s> can not accept null value array", entity.getType());
		if (null == entity.getPkFields())
			throw Lang.makeThrow("Entity<%s> need @PK", entity.getType());
		if (entity.getPkFields().length != values.length)
			throw Lang.makeThrow("fetchx(%s), expect %d values, but you give %d", entity.getType(), entity.getPkFields().length, values.length);
	}

	/* ========================================================== */

	public PagerMaker getPagerMaker() {
		return pagerMaker;
	}

	public void setPagerMaker(PagerMaker pagerMaker) {
		this.pagerMaker = pagerMaker;
	}

	public void setSqlManager(SqlManager sqlManager) {
		this.sqls = sqlManager;
	}

	public SqlMaker getSqlMaker() {
		return sqlMaker;
	}

	public void setSqlMaker(SqlMaker sqlMaker) {
		this.sqlMaker = sqlMaker;
	}

	public EntityMaker getEntityMaker() {
		return entityMaker;
	}

	public void setEntityMaker(EntityMaker entityMaker) {
		if (null == entityMaker) {
			log.error("!! Can't set entityMaker as NULL !");
			return;
		}
		this.entityMaker = entityMaker;
		entities = new EntityHolder(this.entityMaker);
	}

	public DaoRunner getRunner() {
		return runner;
	}

	public NutDao setRunner(DaoRunner runner) {
		this.runner = runner;
		return this;
	}

	public DaoExecutor getExecurtor() {
		return execurtor;
	}

	public NutDao setExecurtor(DaoExecutor execurtor) {
		this.execurtor = execurtor;
		return this;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 'databaseProductName' | 'driverName'
	 * 
	 * <pre>
	 * psql:	'PostgreSQL'	|'PostgreSQL Native Driver'
	 * MySQL:	'MySQL'			|'MySQL-AB JDBC Driver'
	 * Oracle:	'Oracle'		|'Oracle JDBC driver'
	 * db2:		'DB2/NT'		|'IBM DB2 JDBC Universal Driver Architecture'
	 * SQLServer:	'Microsoft SQL Serve'	|'SQL Serve'
	 * </pre>
	 */
	public void setDataSource(DataSource dataSource) {
		if (dataSource == null) {
			if (log.isWarnEnabled())
				log.warn("NULL DataSource!");
			throw new NullPointerException("!!dataSource is NULL!!!");
		}
		if (null == entityMaker)
			entities = new EntityHolder(new DefaultEntityMaker());
		this.dataSource = dataSource;
	}

	protected synchronized void checkDatabase() {
		meta = new DatabaseMeta();
		this.run(new ConnCallback() {
			public void invoke(Connection conn) throws Exception {
				DatabaseMetaData dmd = conn.getMetaData();
				meta.setProductName(dmd.getDatabaseProductName());
				meta.setVersion(dmd.getDatabaseProductVersion());
			}
		});
	}

	public DatabaseMeta meta() {
		if (null == meta) {
			checkDatabase();
		}
		return meta;
	}

	public Pager createPager(int pageNumber, int pageSize) {
		return pagerMaker.make(meta(), pageNumber, pageSize);
	}

	public SqlManager sqls() {
		return this.sqls;
	}

	public void execute(final Sql... sqls) {
		if (null == this.execurtor) {
			if (log.isWarnEnabled())
				log.warn("NULL Execurtor!");
			throw new NullPointerException("NULL Execurtor");
		}
		execurtor.execute(dataSource, runner, sqls);
	}

	public void run(ConnCallback callback) {
		if (null == runner) {
			if (log.isWarnEnabled())
				log.warn("NULL Runner!");
			throw new NullPointerException("NULL Runner");
		}
		runner.run(dataSource, callback);
	}

	public int count(Class<?> classOfT) {
		return count(classOfT, null);
	}

	public int count(Class<?> classOfT, Condition condition) {
		Entity<?> entity = getEntity(classOfT);
		String where = null == condition ? null : condition.toSql(entity);
		Sql sql = sqlMaker.func(entity.getViewName(), "COUNT", "*", where);
		sql.setEntity(entity);
		execute(sql);
		return sql.getInt();
	}

	public int count(String tableName) {
		return count(tableName, null);
	}

	public int count(String tableName, Condition condition) {
		String where = null == condition ? null : condition.toSql(null);
		Sql sql = sqlMaker.func(tableName, "COUNT", "*", where);
		execute(sql);
		return sql.getInt();
	}

	public int clear(Class<?> classOfT) {
		return this.clear(classOfT, null);
	}

	public int clear(String tableName) {
		return this.clear(tableName, null);
	}

	public int clear(Class<?> classOfT, Condition condition) {
		Entity<?> entity = getEntity(classOfT);
		Sql sql;
		if (null == condition) {
			sql = sqlMaker.truncate(entity.getTableName());
		} else {
			sql = sqlMaker.clear(entity).setCondition(condition);
		}
		execute(sql);

		return sql.getUpdateCount();
	}

	public int clear(String tableName, Condition condition) {
		Sql sql;
		if (null == condition) {
			sql = sqlMaker.truncate(tableName);
		} else {
			sql = sqlMaker.clear(tableName).setCondition(condition);
		}
		execute(sql);

		return sql.getUpdateCount();
	}

	public <T> T clearLinks(T obj, String regex) {
		return new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				lns.walkManys(new LinkWalker() {
					void walk(Link link) {
						if (link.getReferField() == null) {
							dao.clear(link.getTargetClass(), null);
						} else {
							Object value = entity.getMirror().getValue(ele, link.getReferField());
							Entity<?> ta = dao.getEntity(link.getTargetClass());
							Sql sql = dao.getSqlMaker().clear_links(ta, link, value);
							dao.execute(sql);
						}
					}
				});
				lns.walkManyManys(new LinkWalker() {
					void walk(Link link) {
						Object value = entity.getMirror().getValue(ele, link.getReferField());
						Sql sql = dao.getSqlMaker().clear_links(link.getRelation(), link.getFrom(), link.getFrom());
						sql.params().set(link.getFrom(), value);
						dao.execute(sql);
					}
				});
				lns.walkOnes(new LinkWalker() {
					void walk(Link link) {
						Object value = entity.getMirror().getValue(ele, link.getReferField());
						Entity<?> ta = dao.getEntity(link.getTargetClass());
						Sql sql = dao.getSqlMaker().clear_links(ta, link, value);
						dao.execute(sql);
					}
				});
			}
		}).run();
	}

	public int delete(Class<?> classOfT, long id) {
		Entity<?> entity = getEntity(classOfT);
		EntityField ef = checkIdField(entity);
		Sql sql = sqlMaker.delete(entity, ef);
		sql.params().set(ef.getName(), id);
		execute(sql);

		return sql.getUpdateCount();
	}

	public int delete(Class<?> classOfT, String name) {
		Entity<?> entity = getEntity(classOfT);
		EntityField ef = checkNameField(entity);
		Sql sql = sqlMaker.delete(entity, ef);
		sql.params().set(ef.getName(), name);
		execute(sql);

		return sql.getUpdateCount();
	}

	public <T> int deletex(Class<T> classOfT, Object... pks) {
		Entity<T> entity = getEntity(classOfT);
		checkPKs(entity, pks);
		Sql sql = sqlMaker.deletex(entity, pks);
		execute(sql);
		return sql.getUpdateCount();
	}

	protected int _deleteSelf(Entity<?> entity, Object obj) {
		if (null != obj) {
			EntityField idnf = entity.getIdentifiedField();
			if (null == idnf) {
				Object[] args = evalArgsByPks(entity, obj);
				if (null != args) {
					Sql sql = sqlMaker.deletex(entity, args);
					execute(sql);
					return sql.getUpdateCount();
				}
				throw DaoException.create(obj, "$IdentifiedField", "delete(Object obj)", null);
			}
			if (idnf.isId()) {
				long id = Castors.me().castTo(idnf.getValue(obj), Long.class);
				return delete(obj.getClass(), id);
			} else if (idnf.isName()) {
				String name = idnf.getValue(obj).toString();
				return delete(obj.getClass(), name);
			} else {
				throw DaoException.create(obj, "$IdentifiedField", "delete(Object obj)", new Exception("Wrong identified field"));
			}
		}
		return 0;
	}

	protected static Object[] evalArgsByPks(Entity<?> entity, Object obj) {
		Object[] args = null;
		EntityField[] pks = entity.getPkFields();
		if (null != pks && pks.length > 0) {
			args = new Object[pks.length];
			for (int i = 0; i < pks.length; i++)
				args[i] = pks[i].getValue(obj);
		}
		return args;
	}

	public int delete(Object obj) {
		if (null != obj) {
			Entity<?> entity = getEntity(obj.getClass());
			return _deleteSelf(entity, obj);
		}
		return 0;
	}

	public <T> void deleteWith(T obj, String regex) {
		new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				lns.invokeManys(new DeleteManyInvoker(dao));
				lns.invokeManyManys(new DeleteManyManyInvoker(dao));
				_deleteSelf(entity, ele);
				lns.invokeOnes(new DeleteOneInvoker(dao));
			}
		}).run();
	}

	public <T> void deleteLinks(T obj, String regex) {
		new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				lns.invokeManys(new DeleteManyInvoker(dao));
				lns.invokeManyManys(new DeleteManyManyInvoker(dao));
				lns.invokeOnes(new DeleteOneInvoker(dao));
			}
		}).run();
	}

	public <T> T fetch(Class<T> classOfT, long id) {
		Entity<T> entity = getEntity(classOfT);
		return fetch(entity, id);
	}

	public <T> T fetch(Entity<T> entity, long id) {
		EntityField ef = checkIdField(entity);
		Sql sql = sqlMaker.fetch(entity, ef);
		sql.params().set(ef.getName(), id);
		execute(sql);
		return sql.getObject(entity.getType());
	}

	public <T> T fetch(Class<T> classOfT, String name) {
		Entity<T> entity = getEntity(classOfT);
		return fetch(entity, name);
	}

	public <T> T fetchx(Class<T> classOfT, Object... pks) {
		Entity<T> entity = getEntity(classOfT);
		checkPKs(entity, pks);
		Sql sql = sqlMaker.fetchx(entity, pks);
		execute(sql);
		return sql.getObject(entity.getType());
	}

	public <T> T fetch(Entity<T> entity, String name) {
		EntityField ef = checkNameField(entity);
		Sql sql = sqlMaker.fetch(entity, ef);
		sql.params().set(ef.getName(), name);
		execute(sql);
		return sql.getObject(entity.getType());
	}

	public <T> T fetch(Class<T> classOfT, Condition condition) {
		Entity<T> entity = getEntity(classOfT);
		return fetch(entity, condition);
	}

	public <T> T fetch(Entity<T> entity, Condition condition) {
		List<T> list = this.query(entity, condition, this.createPager(1, 1));
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public Record fetch(String tableName, Condition condition) {
		Sql sql = sqlMaker.query(tableName, condition, null);
		sql.setCallback(Sqls.callback.record());
		execute(sql);
		return sql.getObject(Record.class);
	}

	public <T> T fetch(Class<T> classOfT) {
		return fetch(classOfT, (Condition) null);
	}

	@SuppressWarnings({ "unchecked" })
	public <T> T fetch(T obj) {
		if (null != obj) {
			Entity<?> entity = (Entity<?>) getEntity(obj.getClass());
			EntityField idnf = entity.getIdentifiedField();
			Sql sql;
			if (idnf == null) {
				Object[] args = evalArgsByPks(entity, obj);
				if (null != args) {
					sql = sqlMaker.fetchx(entity, args);
				} else {
					throw new DaoException(format("Entity <%s> need @Id or @Name or @PK to identify fetch operation!", entity.getType().getName()));
				}
			} else {
				sql = sqlMaker.fetch(entity, idnf);
				sql.params().set(idnf.getName(), idnf.getValue(obj));
			}
			execute(sql);
			return sql.getObject((Class<T>) entity.getType());
		}
		return null;
	}

	public <T> T fetchLinks(T obj, String regex) {
		return new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				// Many
				lns.walkManys(new LinkWalker() {
					void walk(Link link) {
						Condition c = null;
						if (link.getReferField() != null) {
							Object value = mirror.getValue(ele, link.getReferField());
							c = new ManyCondition(link, value);
						}
						List<?> list = query(link.getTargetClass(), c, null);
						Class fieldType = link.getOwnField().getType();
						if (SubSystem.class.isAssignableFrom(fieldType)) {
							mirror.setValue(ele, link.getOwnField(), new SubSystem(list));
						} else {
							mirror.setValue(ele, link.getOwnField(), Castors.me().cast(list, list.getClass(), link.getOwnField().getType(), link.getMapKeyField()));
						}
					}
				});
				// ManyMany
				lns.walkManyManys(new LinkWalker() {
					void walk(Link link) {
						ManyManyCondition mmc = new ManyManyCondition(dao, link, ele);
						List<?> list = query(link.getTargetClass(), mmc, null);
						mirror.setValue(ele, link.getOwnField(), Castors.me().cast(list, list.getClass(), link.getOwnField().getType(), link.getMapKeyField()));
					}
				});
				// one
				lns.walkOnes(new LinkWalker() {
					void walk(Link link) {
						Object one;
						Field ownField = link.getReferField();
						Mirror<?> ownType = Mirror.me(ownField.getType());
						if (ownType.isStringLike()) {
							String name = mirror.getValue(ele, ownField).toString();
							one = fetch(link.getTargetClass(), name);
						} else {
							long id = ((Number) mirror.getValue(ele, ownField)).longValue();
							one = fetch(link.getTargetClass(), id);
						}
						mirror.setValue(ele, link.getOwnField(), one);
					}
				});
			}
		}).setTransaction(false).run();
	}

	public <T> Entity<T> getEntity(final Class<T> classOfT) {
		checkDatabase();
		Entity<T> en = entities.getEntity(classOfT);
		// 如果未发现实体，创建实体
		if (null == en) {
			synchronized (entities) {
				en = entities.getEntity(classOfT);
				if (null == en) {
					final List<Entity<T>> re = new ArrayList<Entity<T>>(1);
					try {
						this.run(new ConnCallback() {
							public void invoke(Connection conn) {
								re.add(entities.reloadEntity(classOfT, conn, meta));
							}
						});
						en = re.get(0);
					} catch (Exception e) {
						throw Lang.wrapThrow(e, "Fail to make entity '%s'!", classOfT);
					}

				}
			}
		}
		return en;
	}

	public int getMaxId(Class<?> classOfT) {
		Entity<?> entity = getEntity(classOfT);
		EntityField ef = checkIdField(entity);
		Sql sql = sqlMaker.func(entity.getViewName(), "MAX", ef.getColumnName(), null);
		execute(sql);
		return sql.getInt();
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> classOfT, ResultSet rs, FieldMatcher fm) {
		return (T) getEntity(classOfT).getObject(rs, fm);
	}

	protected void runFieldQuery(FieldQuery[] qs, Object obj) {
		if (null != qs)
			for (FieldQuery nq : qs)
				nq.update(this, obj);
	}

	protected void _insertSelf(final Entity<?> entity, final Object obj) {
		// TODO 考虑一下修改 FieldQuery 接口，这样就不需要使用事务模板了
		Trans.exec(new Atom() {
			public void run() {
				// Before insert
				runFieldQuery(entity.getBefores(), obj);
				// Do Insert
				execute(sqlMaker.insert(entity, obj));
				// After insert
				runFieldQuery(entity.getAfters(), obj);
			}
		});
	}

	public <T> T fastInsert(T obj) {
		if (Lang.length(obj) > 0) {
			Object first = Lang.first(obj);
			final Entity<?> entity = this.getEntity(first.getClass());
			Lang.each(obj, new Each<Object>() {
				public void invoke(int i, Object ele, int length) {
					runFieldQuery(entity.getBefores(), ele);
					execute(sqlMaker.insert(entity, ele));
				}
			});
		}
		return obj;
	}

	public <T> T insert(T obj) {
		if (Lang.length(obj) > 0) {
			Object first = Lang.first(obj);
			final Entity<?> entity = getEntity(first.getClass());
			Lang.each(obj, new Each<Object>() {
				public void invoke(int i, Object ele, int length) {
					_insertSelf(entity, ele);
				}
			});
		}
		return obj;
	}

	public void insert(String tableName, Chain chain) {
		if (null != chain) {
			Sql sql = sqlMaker.insertChain(tableName, chain, null);
			execute(sql);
		}
	}

	public void insert(Class<?> classOfT, Chain chain) {
		Entity<?> en = getEntity(classOfT);
		if (null != chain) {
			Sql sql = sqlMaker.insertChain(en.getTableName(), chain, en);
			sql.setEntity(en);
			execute(sql);
		}
	}

	public <T> T insertWith(T obj, String regex) {
		return new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				lns.invokeOnes(new InsertOneInvoker(dao, ele, mirror));
				_insertSelf(entity, ele);
				lns.invokeManys(new InsertManyInvoker(dao, ele, mirror));
				lns.invokeManyManys(new InsertManyManyInvoker(dao, ele, mirror));
			}
		}).run();
	}

	public <T> T insertLinks(T obj, String regex) {
		return new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				lns.invokeOnes(new InsertOneInvoker(dao, ele, mirror));
				lns.invokeManys(new InsertManyInvoker(dao, ele, mirror));
				lns.invokeManyManys(new InsertManyManyInvoker(dao, ele, mirror));
			}
		}).run();
	}

	public <T> T insertRelation(T obj, String regex) {
		return new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				lns.invokeManyManys(new InsertManyManyRelationInvoker(dao, ele, mirror));
			}
		}).run();
	}

	public <T> List<T> query(Class<T> classOfT, Condition condition, Pager pager) {
		return query(getEntity(classOfT), condition, pager);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> query(Entity<?> entity, Condition condition, Pager pager) {
		// QuerySql<T> sql = maker.makeQuerySQL(entity, pager);
		Sql sql = sqlMaker.query(entity, condition, pager);
		execute(sql);
		return sql.getList((Class<T>) entity.getType());

	}

	public List<Record> query(String tableName, Condition condition, Pager pager) {
		Sql sql = sqlMaker.query(tableName, condition, pager);
		execute(sql);
		return sql.getList(Record.class);
	}

	public int update(Object obj) {
		if (null == obj)
			return -1;
		final int[] re = new int[1];
		if (Lang.length(obj) > 0) {
			Object first = Lang.first(obj);
			final Entity<?> entity = getEntity(first.getClass());
			Lang.each(obj, new Each<Object>() {
				public void invoke(int i, Object ele, int length) {
					Sql sql = sqlMaker.update(entity, ele);
					execute(sql);
					re[0] = re[0] + sql.getUpdateCount();
				}
			});
		}
		return re[0];
	}

	public int updateIgnoreNull(final Object obj) {
		if (null == obj)
			return 0;

		Object first = Lang.first(obj);
		if (null == first)
			return 0;

		final int[] re = { 0 };
		FieldFilter.create(first.getClass(), true).run(new Atom() {
			public void run() {
				re[0] = update(obj);
			}
		});
		return re[0];
	}

	public int update(Class<?> classOfT, Chain chain, Condition condition) {
		Entity<?> en = getEntity(classOfT);
		Sql sql = sqlMaker.updateBatch(en.getTableName(), chain, en).setCondition(condition);
		sql.setEntity(en);
		execute(sql);
		return sql.getUpdateCount();
	}

	public int update(String tableName, Chain chain, Condition condition) {
		Sql sql = sqlMaker.updateBatch(tableName, chain, null).setCondition(condition);
		execute(sql);
		return sql.getUpdateCount();
	}

	public int updateRelation(Class<?> classOfT, String regex, final Chain chain, final Condition condition) {
		final Links lns = new Links(null, getEntity(classOfT), regex);
		final int[] re = { 0 };
		Trans.exec(new Atom() {
			public void run() {
				lns.walkManyManys(new LinkWalker() {
					void walk(Link link) {
						Sql sql = sqlMaker.updateBatch(link.getRelation(), chain, null).setCondition(condition);
						execute(sql);
						re[0] += sql.getUpdateCount();
					}
				});
			}
		});
		return re[0];
	}

	public <T> T updateWith(T obj, String regex) {
		return new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				update(ele);
				lns.invokeAll(new UpdateInvokder(dao));
			}
		}).run();
	}

	public <T> T updateLinks(T obj, String regex) {
		return new LinksRunner<T>(this, obj, regex, new LinksAtom() {
			public void run() {
				lns.invokeAll(new UpdateInvokder(dao));
			}
		}).run();
	}

	public boolean exists(Class<?> classOfT) {
		return exists(getEntity(classOfT).getViewName());
	}

	public boolean exists(final String tableName) {
		final boolean[] ee = { false };
		this.run(new ConnCallback() {
			public void invoke(Connection conn) {
				Statement stat = null;
				ResultSet rs = null;
				try {
					stat = conn.createStatement();
					// 增加不等式,减少sql执行时间
					String sql = "SELECT COUNT(1) FROM " + tableName + " where 1!=1";
					rs = stat.executeQuery(sql);
					if (rs.next())
						ee[0] = true;
				} catch (SQLException e) {
				} finally {
					Daos.safeClose(stat, rs);
				}
			}
		});
		return ee[0];
	}

	public int func(Class<?> classOfT, String funcName, String fieldName) {
		Entity<?> entity = getEntity(classOfT);
		EntityField ef = entity.getField(fieldName);
		if (null == ef)
			return func(entity.getViewName(), funcName, fieldName);
		return func(entity.getViewName(), funcName, ef.getColumnName());
	}

	public int func(String tableName, String funcName, String colName) {
		Sql sql = sqlMaker.func(tableName, funcName, colName, null);
		execute(sql);
		return sql.getInt();
	}
}
