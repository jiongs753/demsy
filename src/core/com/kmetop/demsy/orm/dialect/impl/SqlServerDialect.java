package com.kmetop.demsy.orm.dialect.impl;

import java.sql.Types;

public class SqlServerDialect extends AbstractDialect {
	public SqlServerDialect() {
		super();
		registerColumnType(Types.BIT, "tinyint");
		registerColumnType(Types.BIGINT, "numeric(19,0)");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.INTEGER, "int");
		registerColumnType(Types.CHAR, "char(1)");
		registerColumnType(Types.VARCHAR, "varchar($l)");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.DOUBLE, "double precision");
		registerColumnType(Types.DATE, "datetime");
		registerColumnType(Types.TIME, "datetime");
		registerColumnType(Types.TIMESTAMP, "datetime");
		// registerColumnType(Types.VARBINARY, "varbinary($l)");//sybase
		registerColumnType(Types.NUMERIC, "numeric($p,$s)");
		registerColumnType(Types.BLOB, "image");
		registerColumnType(Types.CLOB, "text");

		registerColumnType(Types.VARBINARY, "image");
		registerColumnType(Types.VARBINARY, 8000, "varbinary($l)");
	}

	public String sqlAlterTableName(String newTableName, String oldTableName) {
		return new StringBuffer("exec sp_rename ").append("'").append(newTableName).append("'")
				.append(",'").append(oldTableName).append("'").toString();
	}

	public String sqlAlterColumnName(String tableName, String oldColumnName, String newColumnName) {
		return new StringBuffer("exec sp_rename ").append("'").append(tableName).append(".")
				.append(oldColumnName).append("'").append(",'").append(newColumnName).append("'")
				.toString();
	}

	public String sqlAddColumn(String tableName, String columnName, String sqlType) {
		return new StringBuffer("alter table ").append(tableName).append(' ').append(" add ")
				.append(columnName).append(' ').append(sqlType).toString();
	}

	public String getSelectGUIDString() {
		return "select newid()";
	}
}
