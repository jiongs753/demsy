package com.kmetop.demsy.orm.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForeignKeyMetadata {
	private final String name;
	private final String columnName;
	private final String refTableName;
	private final String tableName;
	private final List columns = new ArrayList();

	ForeignKeyMetadata(ResultSet rs) throws SQLException {
		name = rs.getString("FK_NAME");
		// int count = rs.getMetaData().getColumnCount();
		// for (int i = 1; i <= count; i++) {
		// System.out.println(rs.getMetaData().getColumnName(i));
		// }
		refTableName = rs.getString("PKTABLE_NAME");
		tableName = rs.getString("FKTABLE_NAME");
		columnName = rs.getString("FKCOLUMN_NAME");
	}

	public String getName() {
		return name;
	}

	void addColumn(ColumnMetadata column) {
		if (column != null)
			columns.add(column);
	}

	public ColumnMetadata[] getColumns() {
		return (ColumnMetadata[]) columns.toArray(new ColumnMetadata[0]);
	}

	public String toString() {
		return "ForeignKeyMetadata(" + name + ')';
	}

	public String getColumnName() {
		return columnName;
	}

	public String getRefTableName() {
		return refTableName;
	}

	public String getTableName() {
		return tableName;
	}
}
