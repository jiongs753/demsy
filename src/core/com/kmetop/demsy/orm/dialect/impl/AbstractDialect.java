package com.kmetop.demsy.orm.dialect.impl;

import java.util.Map;

import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.dialect.TypeNames;

public abstract class AbstractDialect implements Dialect {
	private final TypeNames typeNames = new TypeNames();

	protected AbstractDialect() {
	}

	protected void registerColumnType(int code, String name) {
		typeNames.put(code, name);
	}

	protected void registerColumnType(int code, int capacity, String name) {
		typeNames.put(code, capacity, name);
	}

	public String getTypeName(int code, int length, int precision, int scale)
			throws DemsyException {
		String result = typeNames.get(code, length, precision, scale);
		if (result == null) {
			throw new DemsyException(
					"No type mapping for java.sql.Types code: " + code
							+ ", length: " + length);
		}
		return result;
	}

	public String getTypeName(int code) throws DemsyException {
		String result = typeNames.get(code);
		if (result == null) {
			throw new DemsyException(
					"No default type mapping for (java.sql.Types) " + code);
		}
		return result;
	}

	protected String getCascadeConstraintsString() {
		return "";
	}

	protected boolean supportsIfExistsBeforeTableName() {
		return false;
	}

	protected boolean supportsIfExistsAfterTableName() {
		return false;
	}

	public String applyLocksToSql(String sql, Map aliasedLockModes,
			Map keyColumnNames) {
		return sql + getForUpdateString();
	}

	protected String getForUpdateString() {
		return " for update";
	}

	public String getCreateTableString() {
		return "create table";
	}

	public String getCreateMultisetTableString() {
		return getCreateTableString();
	}

	public String sqlDropTable(String tableName) {
		StringBuffer buf = new StringBuffer("drop table ");
		if (supportsIfExistsBeforeTableName()) {
			buf.append("if exists ");
		}
		buf.append(tableName).append(getCascadeConstraintsString());
		if (supportsIfExistsAfterTableName()) {
			buf.append(" if exists");
		}
		return buf.toString();
	}

	public String sqlDropIndex(String tableName, String indexName) {
		return new StringBuffer("drop index ").append(tableName).append('.')
				.append(indexName).toString();
	}

	public String sqlDropFk(String tableName, String fkName) {
		return new StringBuffer("alter table ").append(tableName).append(' ')
				.append("drop constraint ").append(fkName).toString();
	}

	public String sqlDropColumn(String tableName, String columnName) {
		return new StringBuffer("alter table ").append(tableName).append(' ')
				.append(" drop column ").append(columnName).toString();
	}

	public String sqlAlterColumnType(String tableName, String columnName,
			String sqlType) {
		return new StringBuffer("alter table ").append(tableName).append(' ')
				.append(" alter column ").append(columnName).append(' ')
				.append(sqlType).toString();
	}

	public String sqlAlterTableName(String newTableName, String oldTableName) {
		throw new UnsupportedOperationException(
				"No alter table name syntax supported by Dialect");
	}

	public String sqlAlterColumnName(String tableName, String oldColumnName,
			String newColumnName) {
		throw new UnsupportedOperationException(
				"No alter column name syntax supported by Dialect");
	}

	public String sqlAddColumn(String tableName, String columnName,
			String sqlType) {
		throw new UnsupportedOperationException(
				"No add column syntax supported by Dialect");
	}

	public String sqlAddFk(String tableName, String constraintName,
			String[] foreignKey, String referencedTable, String[] primaryKey,
			boolean referencesPrimaryKey) {
		StringBuffer res = new StringBuffer("alter table ");
		res.append(tableName);
		res.append(" add constraint ").append(constraintName).append(
				" foreign key (").append(Str.join(", ", foreignKey))
				.append(") references ").append(referencedTable);

		if (!referencesPrimaryKey) {
			res.append(" (").append(Str.join(", ", primaryKey)).append(')');
		}
		if (supportsCascadeDelete()) {
			res.append(" on delete cascade");
		}
		return res.toString();
	}

	public String sqlAddPk(String tableName, String constraintName,
			String[] primaryKey) {
		StringBuffer res = new StringBuffer("alter table ");
		res.append(tableName);
		res.append(" add constraint ").append(constraintName).append(
				" primary key ");
		res.append(" (").append(Str.join(", ", primaryKey)).append(')');
		return res.toString();
	}

	public String sqlAddUnique(String tableName, String constraintName,
			String[] primaryKey) {
		StringBuffer res = new StringBuffer("alter table ");
		res.append(tableName);
		res.append(" add constraint ").append(constraintName)
				.append(" unique ");
		res.append(" (").append(Str.join(", ", primaryKey)).append(')');
		return res.toString();
	}

	@Override
	public boolean supportsCascadeDelete() {
		return false;
	}

	@Override
	public String getQuerySequencesString() {
		return null;
	}

	@Override
	public boolean supportsSequences() {
		return false;
	}

	@Override
	public String getColumnComment(String columnComment) {
		return "";
	}

	@Override
	public String getNullColumnString() {
		return " null";
	}

	@Override
	public String getTableComment(String comment) {
		return "";
	}

	@Override
	public String getTableTypeString() {
		return "";
	}

	@Override
	public boolean supportsColumnCheck() {
		return true;
	}

	@Override
	public boolean supportsNotNullUnique() {
		return true;
	}

	@Override
	public boolean supportsUnique() {
		return true;
	}
}
