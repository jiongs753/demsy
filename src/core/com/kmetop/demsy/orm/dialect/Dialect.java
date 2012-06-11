package com.kmetop.demsy.orm.dialect;

import java.util.Map;

import com.kmetop.demsy.lang.DemsyException;

public interface Dialect {

	public String applyLocksToSql(String sql, Map aliasedLockModes,
			Map keyColumnNames);

	public String getTypeName(int code, int length, int precision, int scale)
			throws DemsyException;

	public String getTypeName(int code) throws DemsyException;

	public String sqlDropTable(String tableName);

	public String sqlDropIndex(String tableName, String indexName);

	public String sqlDropFk(String tableName, String fkName);

	public String sqlDropColumn(String tableName, String columnName);

	public String sqlAlterTableName(String oldTableName, String newTableName);

	public String sqlAlterColumnName(String tableName, String oldColumnName,
			String newColumnName);

	public String sqlAlterColumnType(String tableName, String columnName,
			String sqlType);

	public String sqlAddColumn(String tableName, String columnName,
			String sqlType);

	public String sqlAddFk(String tableName, String constraintName,
			String[] foreignKey, String referencedTable, String[] primaryKey,
			boolean referencesPrimaryKey);

	public String sqlAddPk(String tableName, String constraintName,
			String[] primaryKey);

	public String sqlAddUnique(String tableName, String constraintName,
			String[] primaryKey);

	public boolean supportsSequences();

	public String getQuerySequencesString();

	public boolean supportsCascadeDelete();

	public String getCreateTableString();

	public String getCreateMultisetTableString();

	public String getNullColumnString();

	public boolean supportsNotNullUnique();

	public boolean supportsUnique();

	public boolean supportsColumnCheck();

	public String getColumnComment(String columnComment);

	public String getTableTypeString();

	public String getTableComment(String comment);

	public String getSelectGUIDString();
}
