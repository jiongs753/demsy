package com.kmetop.demsy.orm.nutz.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.nutz.dao.Sqls;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.Link;
import org.nutz.dao.sql.Sql;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IMetaDao;
import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.mapping.EnMapping;
import com.kmetop.demsy.orm.metadata.ColumnMetadata;
import com.kmetop.demsy.orm.metadata.DatabaseMetadata;
import com.kmetop.demsy.orm.metadata.ForeignKeyMetadata;
import com.kmetop.demsy.orm.metadata.TableMetadata;
import com.kmetop.demsy.orm.nutz.EnColumnMappingImpl;
import com.kmetop.demsy.orm.nutz.EnMappingHolder;
import com.kmetop.demsy.orm.nutz.EnMappingImpl;

public class MetaDaoImpl implements IMetaDao {
	protected static Log log = Logs.getLog(MetaDaoImpl.class);

	protected ExtDaoImpl extDao;

	protected Dialect dialect;

	protected EnMappingHolder mappingHolder;

	MetaDaoImpl(ExtDaoImpl dao) {
		extDao = dao;
		dialect = extDao.getDialect();
		mappingHolder = extDao.getEntityHolder();
	}

	public int countTable() {
		return tables().size();
	}

	public int countColumn(EnMapping entity) {
		return columns(entity).size();
	}

	public int countFK(EnMapping entity) {
		return importFks(entity).size();
	}

	public int countExportFK(EnMapping entity) {
		return exportFks(entity).size();
	}

	protected void execSqls(List<Sql> sqls, boolean igloreError) {
		if (sqls == null || sqls.size() == 0) {
			return;
		}
		log.debugf("执行<%s>条SQL......", sqls.size());
		Sql sql = null;
		int len = sqls.size();
		for (int i = 0; i < len; i++) {
			sql = sqls.get(i);
			try {
				this.extDao.execute(sql);
			} catch (RuntimeException e) {
				if (!igloreError) {
					log.warnf("执行<%s>SQL出错: %s", sqls.size(), e);
					throw e;
				} else {
					log.warnf("执行SQL出错! %s", sql);
				}
			}
		}
		log.infof("执行<%s>条SQL: 成功.", sqls.size());
	}

	public void clear() {
		log.debug("清空数据库......");
		try {
			final StringBuffer res = new StringBuffer();
			Trans.exec(new Atom() {
				public void run() {
					List<Sql> sqls = sqlClearDB();
					res.append(sqls.size());
					execSqls(sqls, false);
				}
			});
			log.debugf("清空数据库: 结束. [sqlSize: %s]", res);
		} catch (Exception e) {
			log.warnf("清空数据库出错: %s", Ex.msg(e));
		}
	}

	private void _syncTables(EnMapping mapping, boolean syncRefTable) {
		EnMappingImpl entity = (EnMappingImpl) mapping;

		String enInfo = Cls.getDisplayName(entity.getType());

		// 表已经存在: 检查外键、字段
		if (this.extDao.exists(entity.getTableName())) {
			log.debugf("同步%s数据库表: 检查新增字段", enInfo);
			this.execSqls(checkNewFields(entity), false);

			log.debugf("同步%s数据库表: 检查外键 [syncRefTable=%s]", enInfo, syncRefTable);
			this.execSqls(checkFks(entity, syncRefTable), true);

			log.debugf("同步%s数据库表: 检查更新字段", enInfo);
			this.execSqls(checkOldFields(entity), true);

			// TODO: 取消 删除字段 功能
			// this.execSqls(checkDropFields(entity));
		}
		// 实体表不存在: 创建表、主键、外键
		else {
			log.debugf("同步%s数据库表: 创建数据库表 [syncRefTable=%s]", enInfo, syncRefTable);
			this.execSqls(create(entity), false);

			log.debugf("同步%s数据库表: 创建外键 [syncRefTable=%s]", enInfo, syncRefTable);
			this.execSqls(createFks(entity, syncRefTable), false);
		}

		// 检查多对多的中间表
		List<Link> links = entity.getManyMany(null);
		log.debugf("同步%s数据库表: 检查<%s>个多对多关联表... [syncRefTable=%s]", enInfo, links.size(), syncRefTable);
		for (Link link : links) {
			if (!extDao.exists(link.getRelation())) {
				log.debugf("同步%s数据库表: 创建多对多关联表 [columnName=%s, syncRefTable=%s]", enInfo, Cls.getDisplayName(link.getOwnField()), syncRefTable);
				this.execSqls(create(entity, link, syncRefTable), false);
			} else
				log.debugf("同步%s数据库表: <多对多关联表已经存在>", enInfo);
		}
		log.debugf("同步%s数据库表: 检查<%s>个多对多关联表: 结束. [syncRefTable=%s]", enInfo, links.size(), syncRefTable);
	}

	public void syncTables(final EnMapping<?> entity, final boolean syncRefTable) {
		if (entity.isReadonly()) {
			return;
		}
		EnMappingImpl enImp = (EnMappingImpl) entity;

		String enInfo = Cls.getDisplayName(entity.getType());
		log.debugf("同步%s数据库表......[syncRefTable=%s]", enInfo, syncRefTable);

		try {
			Trans.exec(new Atom() {
				public void run() {
					_syncTables(entity, syncRefTable);
				}
			});

			log.debugf("同步%s实体表: 结束.", enInfo);

			enImp.setSyncedTable(true);
			enImp.setSyncedRefTable(syncRefTable);
		} catch (Throwable e) {
			enImp.setSyncedTable(false);
			enImp.setSyncedRefTable(false);

			log.warnf("同步%s实体表出错: [syncRefTable=%s] %s", Cls.getDisplayName(entity.getType()), syncRefTable, Ex.msg(e));
		}
	}

	public void dropTables(final EnMapping mapping) {
		if (mapping == null) {
			return;
		}
		String enInfo = Cls.getDisplayName(mapping.getType());
		log.debugf("删除%s数据库表......", enInfo);

		final EnMappingImpl entity = (EnMappingImpl) mapping;
		try {
			Trans.exec(new Atom() {
				public void run() {
					_dropTables(entity);
				}
			});

			log.debugf("删除%s数据库表: 结束.", enInfo);
		} catch (Exception e) {
			log.warnf("删除%s数据库表出错: %s", enInfo, e);
		}
	}

	protected void _dropTables(EnMapping mapping) {
		EnMappingImpl entity = (EnMappingImpl) mapping;
		String enInfo = Cls.getDisplayName(mapping.getType());

		// 删除实体多对多关联表
		List<Link> links = entity.getManyMany(null);
		log.debugf("删除%s数据库表: 删除<%s>个多对多关联表...", enInfo, links.size());

		for (Link link : links) {
			if (extDao.exists(link.getRelation())) {
				this.execSqls(drop(entity, link), false);
				log.debugf("删除%s数据库表: 删除多对多关联表成功! [columnName=%s]", enInfo, Cls.getDisplayName(link.getOwnField()));
			}
		}
		// 删除实体表
		if (extDao.exists(entity.getTableName())) {
			this.execSqls(drop(entity), false);
			log.debugf("删除%s数据库表: 删除实体数据库表结束! ", enInfo);
		} else
			log.debugf("删除%s数据库表: <实体数据库表不存在> ", enInfo);
	}

	protected String toSqlType(EnColumnMappingImpl col) {
		String type = col.getSqlType();
		String sqlType;
		sqlType = toSqlType(type, col.getLength(), col.getPrecision(), col.getScale());
		if (log.isTraceEnabled()) {
			log.tracef("SQL类型转换: %s %s [columnType=%s, length=%s, precision=%s, scale=%s]", col.getName(), sqlType, type, col.getLength(), col.getPrecision(), col.getScale());
		}
		return sqlType;
	}

	protected String toSqlType(Field fld) {
		Column column = fld.getAnnotation(Column.class);
		int l = 0;
		int p = 0;
		int s = 0;
		if (column != null) {
			l = column.length();
			p = column.precision();
			s = column.scale();
		}
		Class type = fld.getType();
		String sqlType = this.toSqlType(type, l, p, s);
		if (log.isTraceEnabled()) {
			log.tracef("SQL类型转换: %s [fieldName=%s,fieldType=%s, length=%s, precision=%s, scale=%s]", sqlType, fld.getName(), type, l, p, s);
		}
		return sqlType;
	}

	protected String toSqlType(Class classOfFld, int l, int p, int s) {
		return toSqlType(classOfFld.getSimpleName(), l, p, s);
	}

	protected String toSqlType(String srcType, int l, int p, int s) {
		String type = srcType.toLowerCase();
		if (type.equals("boolean"))
			return dialect.getTypeName(Types.BIT);
		if (type.equals("long")) {
			return dialect.getTypeName(Types.BIGINT, l, p, s);
		}
		if (type.equals("integer") || type.equals("int")) {
			return dialect.getTypeName(Types.INTEGER, l, p, s);
		}
		if (type.equals("short")) {
			return dialect.getTypeName(Types.SMALLINT);
		}
		if (type.equals("byte")) {
			return dialect.getTypeName(Types.TINYINT, l, p, s);
		}

		if (type.equals("double")) {
			return dialect.getTypeName(Types.DOUBLE, l, p, s);
		}
		if (type.equals("float")) {
			return dialect.getTypeName(Types.FLOAT, l, p, s);
		}

		if (type.equals("date")) {
			return dialect.getTypeName(Types.DATE);
		}

		if (type.equals("clob")) {
			return dialect.getTypeName(Types.CLOB);
		}
		if (type.equals("text")) {
			return dialect.getTypeName(Types.CLOB);
		}
		if (type.equals("bigdecimal")) {
			return dialect.getTypeName(Types.NUMERIC, l, p, s);
		}
		if (type.equals("biginteger")) {
			return dialect.getTypeName(Types.NUMERIC, l, p, s);
		}
		if (type.equals("character") || type.equals("char")) {
			return dialect.getTypeName(Types.CHAR, l, p, s);
		}
		if (type.equals("string")) {
			return dialect.getTypeName(Types.VARCHAR, l, p, s);
		}

		try {
			Class cls = Cls.forName(Demsy.appconfig.getCustomFieldPkg() + srcType);
			BzFld bzfld = (BzFld) cls.getAnnotation(BzFld.class);

			type = "String";
			if (bzfld != null) {
				if (!Str.isEmpty(bzfld.columnDefinition()))
					type = bzfld.columnDefinition();
				if (bzfld.precision() > 0)
					l = bzfld.precision();

			}

			return toSqlType(type, l, p, s);
		} catch (Throwable e) {
			throw new DemsyException("非法字段类型! [%s] %s", type, e);
		}
	}

	protected String sqlCreateTable(EnMapping mapping) {
		EnMappingImpl entity = (EnMappingImpl) mapping;
		StringBuffer buf = new StringBuffer(dialect.getCreateTableString()).append(' ').append(entity.getTableName()).append(" (\n");
		Iterator<EnColumnMappingImpl> iter = entity.fields().iterator();
		while (iter.hasNext()) {
			EnColumnMappingImpl col = iter.next();
			buf.append("\t").append(col.getColumnName()).append(' ').append(toSqlType(col));

			String defaultValue = "";// col.getDefaultValue("");
			if (!Str.isEmpty(defaultValue)) {
				buf.append(" default ").append(defaultValue);
			}

			if (col.isNotNull() || col.isPk() || col.isId()) {
				buf.append(" not null");
			} else {
				buf.append(dialect.getNullColumnString());
			}

			if (col.hasCheckConstraint() && dialect.supportsColumnCheck()) {
				buf.append(" check (").append(col.getCheckConstraint()).append(")");
			}

			String columnComment = col.getComment();
			if (columnComment != null) {
				buf.append(dialect.getColumnComment(columnComment));
			}

			if (iter.hasNext()) {
				buf.append(",\n");
			}
		}

		if (dialect.supportsColumnCheck()) {
			List checkConstraints = (List) entity.get("checkConstraints");
			if (checkConstraints != null) {
				Iterator<String> chiter = checkConstraints.iterator();
				while (chiter.hasNext()) {
					buf.append(", check (").append(chiter.next()).append(')');
				}
			}
		}

		buf.append("\n)");

		if (entity.getString("comment") != null) {
			buf.append(dialect.getTableComment(entity.getString("comment")));
		}
		buf.append(dialect.getTableTypeString());
		return buf.toString();
	}

	public List<Sql> create(EnMapping mapping) {
		String enInfo = Cls.getDisplayName(mapping.getType());
		log.debugf("同步%s数据库表: 创建生成SQL...", enInfo);

		EnMappingImpl entity = (EnMappingImpl) mapping;
		List<Sql> sqls = new ArrayList();
		StringBuffer logBuf = new StringBuffer();

		// 创建实体表
		String sqlStr = this.sqlCreateTable(entity);
		if (log.isInfoEnabled()) {
			logBuf.append(String.format("\n%s", sqlStr));
		}
		sqls.add(Sqls.create(sqlStr));

		// 创建主键
		String[] primaryKey = null;
		if (entity.getIdField() != null) {
			primaryKey = new String[1];
			primaryKey[0] = entity.getIdField().getColumnName();
		} else {
			EntityField[] pks = entity.getPkFields();
			if (pks != null && pks.length > 0) {
				primaryKey = new String[pks.length];
				int count = 0;
				for (EntityField pk : pks) {
					primaryKey[count++] = pk.getColumnName();
				}
			}
		}
		sqlStr = dialect.sqlAddPk(entity.getTableName(), "PK_" + entity.getTableName(), primaryKey);
		if (!Str.isEmpty(sqlStr)) {
			if (log.isInfoEnabled()) {
				logBuf.append(String.format("\n%s", sqlStr));
			}
			sqls.add(Sqls.create(sqlStr));
		}

		log.infof("同步%s数据库表: 创建表生成SQL结束. %s", enInfo, logBuf);

		return sqls;
	}

	protected List<Sql> drop(final EnMapping mapping) {

		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("删除%s数据库表: 生成SQL...", enInfo);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				List<String> sqlStrs = new ArrayList();
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(entity.getTableName());
				Iterator<ForeignKeyMetadata> fks = tableInfo.iteratorForeignKeyMetadatas();
				while (fks.hasNext()) {
					ForeignKeyMetadata fk = fks.next();
					String sqlStr = dialect.sqlDropFk(entity.getTableName(), fk.getName());
					if (sqlStrs.contains(sqlStr)) {
						continue;
					}
					sqlStrs.add(sqlStr);
					if (log.isInfoEnabled()) {
						logBuf.append(String.format("\nIMPORT FK: %s", sqlStr));
					}
					sqls.add(Sqls.create(sqlStr));
				}
				fks = tableInfo.iteratorForeignedKeyMetadatas();
				while (fks.hasNext()) {
					ForeignKeyMetadata fk = fks.next();
					String sqlStr = dialect.sqlDropFk(fk.getTableName(), fk.getName());
					if (sqlStrs.contains(sqlStr)) {
						continue;
					}
					sqlStrs.add(sqlStr);
					if (log.isInfoEnabled()) {
						logBuf.append(String.format("\nEXPORT FK: %s", sqlStr));
					}
					sqls.add(Sqls.create(sqlStr));
				}

				String sqlStr = dialect.sqlDropTable(entity.getTableName());
				if (log.isInfoEnabled()) {
					logBuf.append(String.format("\nTABLE: %s", sqlStr));
				}
				sqls.add(Sqls.create(sqlStr));

				log.infof("删除%s数据库表: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}
		});
	}

	protected List<Sql> drop(final EnMapping mapping, final Link link) {

		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("删除%s多对多关联表: 生成SQL...[columnName=%s]", enInfo, Cls.getDisplayName(link.getOwnField()));

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(link.getRelation());
				Iterator<ForeignKeyMetadata> fks = tableInfo.iteratorForeignKeyMetadatas();
				while (fks.hasNext()) {
					ForeignKeyMetadata fk = fks.next();
					String sqlStr = dialect.sqlDropFk(link.getRelation(), fk.getName());
					if (log.isInfoEnabled())
						logBuf.append(String.format("\nFK: %s", sqlStr));

					sqls.add(Sqls.create(sqlStr));
				}

				String sqlStr = dialect.sqlDropTable(link.getRelation());
				if (log.isInfoEnabled())
					logBuf.append(String.format("\nTABLE: %s", sqlStr));

				sqls.add(Sqls.create(sqlStr));

				if (log.isInfoEnabled())
					log.infof("删除%s多对多关联表: 生成SQL结束. [columnName=%s] %s", entity.getMirror().getType().getName(), link.getOwnField().getName(), logBuf);

				return sqls;
			}
		});
	}

	protected List<Sql> checkDropFields(final EnMapping mapping) {
		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("删除%s多余字段: 生成SQL...", enInfo);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl demsyEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(demsyEntity.getTableName());
				Iterator<ColumnMetadata> columns = tableInfo.iteratorColumnMetaDatas();
				while (columns.hasNext()) {
					ColumnMetadata columnInfo = columns.next();
					EnColumnMappingImpl field = demsyEntity.getFieldByColumn(columnInfo.getName());
					if (field == null) {// 删除多余的字段
						if (demsyEntity.isChild()) {
							continue;
						}
						if (demsyEntity.isAbstract()) {
							continue;
						}
						String sqlStr = dialect.sqlDropColumn(tableName, columnInfo.getName());
						if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n%s", sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}
				if (logBuf.length() == 0)
					log.debugf("删除%s多余字段: 生成SQL结束. %s", enInfo, "<删除0个多余字段>");
				else
					log.infof("删除%s多余字段: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	protected List<Sql> dropWasteFields(final EnMapping mapping) {
		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("删除%s垃圾字段: 生成SQL...", enInfo);
				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl demsyEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata table = db.getTableMetadata(demsyEntity.getTableName());
				Iterator<ColumnMetadata> columns = table.iteratorColumnMetaDatas();
				while (columns.hasNext()) {
					ColumnMetadata column = columns.next();
					EnColumnMappingImpl field = demsyEntity.getFieldByColumn(column.getName());
					if (field == null) {// 删除多余的字段
						String sqlStr = dialect.sqlDropColumn(tableName, column.getName());
						if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n%s", sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				if (logBuf.length() == 0)
					log.debugf("删除%s垃圾字段: 生成SQL结束. %s", enInfo, "<删除0个垃圾字段>");
				else
					log.infof("删除%s垃圾字段: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	protected List<Sql> checkOldFields(final EnMapping mapping) {

		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("同步%s数据库表: 检查更新字段生成SQL...", enInfo);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl demsyEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(demsyEntity.getTableName());
				Iterator<ColumnMetadata> columns = tableInfo.iteratorColumnMetaDatas();
				while (columns.hasNext()) {
					ColumnMetadata columnInfo = columns.next();
					EnColumnMappingImpl field = demsyEntity.getFieldByColumn(columnInfo.getName());
					if (field != null) {
						// 修改类型不匹配的字段
						String sqlType = toSqlType(field);
						if (sqlType.equals(columnInfo.getTypeName())) {
							continue;
						}
						String sqlTypeOld = dialect.getTypeName(columnInfo.getTypeCode(), columnInfo.getColumnSize(), columnInfo.getColumnSize(), columnInfo.getDecimalDigits());
						if (sqlType.equals(sqlTypeOld)) {
							continue;
						}
						String sqlStr = dialect.sqlAlterColumnType(tableName, columnInfo.getName(), sqlType);
						if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  [sqlOldType=%s, sqlType=%s] %s", field.getName(), sqlTypeOld, sqlType, sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					} else {
						String nullable = columnInfo.getNullable();
						if (!"YES".equals(nullable.toUpperCase()) && Str.isEmpty(columnInfo.getColumnDef())) {
							String sqlTypeOld = dialect.getTypeName(columnInfo.getTypeCode(), columnInfo.getColumnSize(), columnInfo.getColumnSize(), columnInfo.getDecimalDigits());

							String sqlStr = dialect.sqlAlterColumnType(tableName, columnInfo.getName(), sqlTypeOld);
							sqlStr = sqlStr + " null";
							sqls.add(Sqls.create(sqlStr));
						}
					}
				}

				if (logBuf.length() == 0)
					log.debugf("同步%s数据库表: 检查更新字段生成SQL结束. %s", enInfo, "<更新0个字段>");
				else
					log.infof("同步%s数据库表: 检查更新字段生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	protected List<Sql> checkNewFields(final EnMapping mapping) {
		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				final String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("同步%s数据库表: 检查新增字段生成SQL...", enInfo);

				final EnMappingImpl entity = (EnMappingImpl) mapping;
				final List<Sql> sqls = new ArrayList();
				final StringBuffer logBuf = new StringBuffer();

				final EnMappingImpl demsyEntity = (EnMappingImpl) entity;
				final String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(demsyEntity.getTableName());
				// 添加不存在的字段
				Iterator<EnColumnMappingImpl> fields = demsyEntity.fields().iterator();

				while (fields.hasNext()) {
					EnColumnMappingImpl f = fields.next();
					String columnName = f.getColumnName();
					if (tableInfo.getColumnMetadata(columnName) == null) {
						String sqlStr = dialect.sqlAddColumn(tableName, columnName, toSqlType(f));
						if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  %s", f.getName(), sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				if (logBuf.length() == 0)
					log.debugf("同步%s数据库表: 检查新增字段生成SQL结束. %s", enInfo, "<新增0个字段>");
				else
					log.infof("同步%s数据库表: 检查新增字段生成SQL结束. %s", enInfo, logBuf);

				return sqls;

			}

		});
	}

	protected List<Sql> checkFks(final EnMapping mapping, final boolean syncRefTable) {
		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				if (mapping == null) {
					return new ArrayList();
				}
				String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("同步%s数据库表: 检查外键生成SQL...[syncRefTable=%s]", enInfo, syncRefTable);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl demsyEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata table = db.getTableMetadata(demsyEntity.getTableName());
				Iterator<ForeignKeyMetadata> fkInfos = table.iteratorForeignKeyMetadatas();

				// 删除多余外键
				while (fkInfos.hasNext()) {
					ForeignKeyMetadata fkInfo = fkInfos.next();
					if (demsyEntity.getLink(fkInfo.getColumnName()) == null) {
						if (demsyEntity.isChild()) {
							continue;
						}
						if (demsyEntity.isAbstract()) {
							continue;
						}
						String fkName = fkInfo.getName();
						String sqlStr = dialect.sqlDropFk(tableName, fkName);
						if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n[%s.%s-->%s]:  %s", demsyEntity.getTableName(), fkInfo.getColumnName(), fkInfo.getRefTableName(), sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				// 添加缺少的外键
				Iterator<EnColumnMappingImpl> fields = demsyEntity.fields().iterator();
				while (fields.hasNext()) {
					EnColumnMappingImpl ef = fields.next();
					Link link = ef.getLink();
					if (link == null) {
						continue;
					}
					Class targetClass = link.getTargetClass();
					if (log.isDebugEnabled())
						log.debugf("同步%s数据库表: 检查外键 获取外键%s关联实体%s", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass));
					EnMappingImpl refEntity = (EnMappingImpl) mappingHolder.getEnMapping(targetClass);
					if (refEntity == null) {
						if (syncRefTable) {
							log.debugf("同步%s数据库表: 检查外键 加载外键%s关联实体%s...", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass));
							refEntity = extDao.loadEntity(targetClass, true);
							log.debugf("同步%s数据库表: 检查外键 加载外键%s关联实体%s结束.", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass));
						} else if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  外键关联实体%s未加载", Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass)));
							continue;
						}
					}
					if (refEntity == null) {
						continue;
					}
					String refTableName = refEntity.getTableName();
					if (table.getForeignKeyMetadata(ef.getColumnName(), refTableName) == null) {
						String fkName = ef.getFkName();
						String[] foreignKey = new String[1];
						foreignKey[0] = ef.getColumnName();
						String sqlStr = dialect.sqlAddFk(tableName, fkName, foreignKey, refTableName, null, true);
						if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  %s", Cls.getDisplayName(link.getOwnField()), sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				if (logBuf.length() == 0)
					log.debugf("同步%s数据库表: 检查外键 生成SQL结束. %s", enInfo, "<新增0个外键>");
				else
					log.infof("同步%s数据库表: 检查外键 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	protected List<Sql> createFks(EnMapping entity, boolean syncRefTable) {
		String enInfo = Cls.getDisplayName(entity.getType());
		log.debugf("同步%s数据库表: 创建外键生成SQL...[syncRefTable=%s]", enInfo, syncRefTable);

		List<Sql> sqls = new ArrayList();
		StringBuffer logBuf = new StringBuffer();

		EnMappingImpl demsyEntity = (EnMappingImpl) entity;
		String tableName = entity.getTableName();
		Iterator<EnColumnMappingImpl> fields = demsyEntity.fields().iterator();

		while (fields.hasNext()) {
			EnColumnMappingImpl f = fields.next();
			Link link = f.getLink();
			if (link == null) {
				continue;
			}
			String fkName = f.getFkName();
			if (!Str.isEmpty(fkName)) {
				Class targetClass = link.getTargetClass();
				EnMappingImpl refEntity = mappingHolder.getEnMapping(targetClass);
				if (refEntity == null) {
					if (syncRefTable) {
						log.debugf("同步%s数据库表: 创建外键加载外键%s关联实体%s...", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass));
						refEntity = extDao.loadEntity(targetClass, true);
						log.debugf("同步%s数据库表: 创建外键 加载外键%s关联实体%s结束.", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass));
					} else {
						if (log.isInfoEnabled()) {
							logBuf.append(String.format("\n%s: 外键关联实体%s未加载", Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass)));
						}
						continue;
					}
				}
				String refTable = refEntity.getTableName();
				String[] foreignKey = new String[1];
				foreignKey[0] = f.getColumnName();
				String sqlStr = dialect.sqlAddFk(tableName, fkName, foreignKey, refTable, null, true);
				if (log.isInfoEnabled()) {
					logBuf.append(String.format("\n%s: %s", Cls.getDisplayName(link.getOwnField()), sqlStr));
				}
				sqls.add(Sqls.create(sqlStr));
			}
		}

		if (logBuf.length() == 0)
			log.debugf("同步%s数据库表: 创建外键生成SQL结束. %s", enInfo, "<创建0个外键>");
		else
			log.infof("同步%s数据库表: 创建外键生成SQL结束. %s", enInfo, logBuf);

		return sqls;
	}

	protected List<Sql> dropFks(final EnMapping mapping) {

		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				final String enInfo = Cls.getDisplayName(mapping.getType());
				log.debugf("删除%s实体外键: 生成SQL...", enInfo);

				final EnMappingImpl entity = (EnMappingImpl) mapping;
				final List<Sql> sqls = new ArrayList();
				final StringBuffer logBuf = new StringBuffer();

				final EnMappingImpl demsyEntity = (EnMappingImpl) entity;
				final String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata table = db.getTableMetadata(demsyEntity.getTableName());
				Iterator<ForeignKeyMetadata> fkInfos = table.iteratorForeignKeyMetadatas();
				// 删除外键
				while (fkInfos.hasNext()) {
					ForeignKeyMetadata fkInfo = fkInfos.next();
					String fkName = fkInfo.getName();
					String sqlStr = dialect.sqlDropFk(tableName, fkName);
					if (log.isInfoEnabled()) {
						logBuf.append("\n" + sqlStr);
					}
					sqls.add(Sqls.create(sqlStr));
				}

				if (logBuf.length() == 0)
					log.debugf("删除%s实体外键: 生成SQL结束. %s", "<删除0个外键>");
				else
					log.infof("删除%s实体外键: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	protected List<Sql> create(EnMapping mapping, Link link, boolean syncRefTable) {
		String enInfo = Cls.getDisplayName(mapping.getType());
		log.debugf("同步%s数据库表: 创建多对多关联表生成SQL...[link=%s, syncRefTable=%s]", enInfo, Cls.getDisplayName(link.getOwnField()), syncRefTable);

		EnMappingImpl entity = (EnMappingImpl) mapping;
		if (!link.isManyMany()) {
			return null;
		}
		List<Sql> sqls = new ArrayList();
		StringBuffer logBuf = new StringBuffer();

		Class targetClass = link.getTargetClass();
		EnMappingImpl targetEntity = mappingHolder.getEnMapping(targetClass);
		if (targetEntity == null) {
			if (syncRefTable) {
				log.debugf("同步%s数据库表: 创建多对多关联表加载%s关联实体%s...", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass));
				targetEntity = extDao.loadEntity(targetClass, true);
				log.debugf("同步%s数据库表: 创建多对多关联表加载%s关联实体%s结束.", enInfo, Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass));
			} else {
				if (log.isInfoEnabled()) {
					logBuf.append(String.format("\n%s: 关联实体%s未加载", Cls.getDisplayName(link.getOwnField()), Cls.getDisplayName(targetClass)));
				}
			}
		}

		// 创建多对多实体的中间表
		if (targetEntity != null) {
			StringBuffer buf = new StringBuffer(dialect.getCreateTableString()).append(' ').append(link.getRelation()).append(" (\n");
			buf.append("\t").append(link.getFrom()).append(' ').append(toSqlType(link.getReferField())).append(" not null").append(",\n");
			buf.append("\t").append(link.getTo()).append(' ').append(toSqlType(link.getTargetField())).append(" not null").append("\n)");
			if (log.isInfoEnabled()) {
				logBuf.append(String.format("\n%s", buf));
			}
			sqls.add(Sqls.create(buf.toString()));

			// 创建实体中间表主对象外键
			String tableName = entity.getTableName();
			String[] foreignKey = new String[1];
			foreignKey[0] = link.getFrom();
			String constraintName = entity.getNaming().fkName(link.getRelation(), Str.join("_", foreignKey), tableName);
			String sqlStr = dialect.sqlAddFk(link.getRelation(), constraintName, foreignKey, tableName, null, true);
			if (log.isInfoEnabled()) {
				logBuf.append(String.format("\n主表外键<%s.%s>: %s", enInfo, Cls.getDisplayName(link.getOwnField()), sqlStr));
			}
			sqls.add(Sqls.create(sqlStr));

			// 创建实体中间表目标对象外键
			tableName = targetEntity.getTableName();
			foreignKey = new String[1];
			foreignKey[0] = link.getTo();
			constraintName = entity.getNaming().fkName(link.getRelation(), Str.join("_", foreignKey), tableName);
			sqlStr = dialect.sqlAddFk(link.getRelation(), constraintName, foreignKey, tableName, null, true);
			if (log.isInfoEnabled()) {
				logBuf.append(String.format("\n从表外键<%s>: %s", Cls.getDisplayName(link.getTargetClass()), sqlStr));
			}
			sqls.add(Sqls.create(sqlStr));
		}

		if (log.isInfoEnabled())
			log.infof("同步%s数据库表: 创建多对多关联表生成SQL结束. [columnName=%s, syncRefTable=%s] %s", enInfo, Cls.getDisplayName(link.getOwnField()), syncRefTable, logBuf);

		return sqls;
	}

	protected List<Sql> sqlClearDB() {
		return (List<Sql>) extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				log.debugf("清空数据库: 生成SQL...");
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				Map mp = db.getTableMetadatas(null);
				if (log.isInfoEnabled()) {
					logBuf.append("<删除" + mp.size() + "张数据表>");
				}
				// 删除外键
				Iterator<TableMetadata> tables = mp.values().iterator();
				while (tables.hasNext()) {
					TableMetadata table = tables.next();
					String tablename = table.getName();
					Iterator<ForeignKeyMetadata> fkInfos = table.iteratorForeignKeyMetadatas();
					while (fkInfos.hasNext()) {
						ForeignKeyMetadata fkInfo = fkInfos.next();
						String fkName = fkInfo.getName();
						String sqlStr = dialect.sqlDropFk(tablename, fkName);
						if (log.isInfoEnabled()) {
							logBuf.append("\n" + sqlStr);
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}
				// 删除表
				tables = mp.values().iterator();
				while (tables.hasNext()) {
					TableMetadata table = tables.next();
					String tablename = table.getName();
					String sqlStr = dialect.sqlDropTable(tablename);
					if (log.isInfoEnabled()) {
						logBuf.append("\n" + sqlStr);
					}
					sqls.add(Sqls.create(sqlStr));
				}

				if (logBuf.length() == 0)
					log.debugf("清空数据库: 生成SQL结束. <清除了0张数据表>");
				else
					log.infof("清空数据库: 生成SQL结束. %s", logBuf);

				return sqls;
			}

		});
	}

	public Map<String, TableMetadata> tables() {
		final Map<String, TableMetadata> tables = new HashMap();

		extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				tables.putAll(db.getTableMetadatas(null));

				return null;
			}

		});

		return tables;
	}

	public TableMetadata table(EnMapping entity) {
		return table(entity.getTableName());
	}

	public TableMetadata table(final String tableName) {
		final TableMetadata[] tables = new TableMetadata[1];

		extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				tables[0] = db.getTableMetadata(tableName);

				return null;
			}

		});

		return tables[0];
	}

	public Map<String, ForeignKeyMetadata> importFks(EnMapping entity) {
		return this.importFks(entity.getTableName());
	}

	public Map<String, ForeignKeyMetadata> importFks(final String tableName) {
		final Map<String, ForeignKeyMetadata> map = new HashMap();

		extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				map.putAll(db.getTableMetadata(tableName).getForeignKeys());

				return null;
			}

		});

		return map;
	}

	public Map<String, ForeignKeyMetadata> exportFks(final EnMapping entity) {
		return this.exportFks(entity.getTableName());
	}

	public Map<String, ForeignKeyMetadata> exportFks(final String tableName) {
		final Map<String, ForeignKeyMetadata> map = new HashMap();

		extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				map.putAll(db.getTableMetadata(tableName).getForeignedKeys());

				return null;
			}

		});

		return map;
	}

	public Map<String, ColumnMetadata> columns(final EnMapping entity) {
		return this.columns(entity.getTableName());
	}

	public Map<String, ColumnMetadata> columns(final String tableName) {
		final Map<String, ColumnMetadata> map = new HashMap();

		extDao.run(new NoTransConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				map.putAll(db.getTableMetadata(tableName).getColumns());

				return null;
			}

		});

		return map;
	}
}
