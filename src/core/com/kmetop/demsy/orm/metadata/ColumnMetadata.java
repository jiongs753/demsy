package com.kmetop.demsy.orm.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class ColumnMetadata {
	private final String name;

	private final String typeName;

	private final int columnSize;

	private final int decimalDigits;

	private final String isNullable;

	private final int typeCode;

	private String columnDef;

	ColumnMetadata(ResultSet rs) throws SQLException {
		columnDef = rs.getString("COLUMN_DEF");
		name = rs.getString("COLUMN_NAME");
		columnSize = rs.getInt("COLUMN_SIZE");
		decimalDigits = rs.getInt("DECIMAL_DIGITS");
		isNullable = rs.getString("IS_NULLABLE");
		typeCode = rs.getInt("DATA_TYPE");
		typeName = new StringTokenizer(rs.getString("TYPE_NAME"), "() ").nextToken();
	}

	public String getName() {
		return name;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public String getNullable() {
		return isNullable;
	}

	public String toString() {
		return "ColumnMetadata(" + name + ')';
	}

	public int getTypeCode() {
		return typeCode;
	}

	public String getColumnDef() {
		return columnDef;
	}

	public void setColumnDef(String columnDef) {
		this.columnDef = columnDef;
	}

}
