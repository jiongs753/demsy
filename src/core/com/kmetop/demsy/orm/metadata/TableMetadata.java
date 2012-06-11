package com.kmetop.demsy.orm.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class TableMetadata {

	private static final Log log = Logs.getLog(TableMetadata.class);

	private final String catalog;

	private final String schema;

	private final String name;

	private final Map columns = new HashMap();

	private final Map foreignKeys = new HashMap();// 表外键

	private final Map foreignedKeys = new HashMap();// 应用该表的外键：被外键引用

	private final Map indexes = new HashMap();

	void printMeta(ResultSet rs) throws SQLException {
		ResultSetMetaData resultmeta = rs.getMetaData();
		for (int i = resultmeta.getColumnCount(); i > 0; i--) {
			String name = resultmeta.getColumnName(i);
			System.out.println(name + " = " + rs.getString(name));
		}
	}

	TableMetadata(ResultSet rs, DatabaseMetaData meta, boolean extras) throws SQLException {
		catalog = rs.getString("TABLE_CAT");
		schema = rs.getString("TABLE_SCHEM");
		name = rs.getString("TABLE_NAME");

		initColumns(meta);
		if (extras) {
			initForeignKeys(meta);
			initIndexes(meta);
		}
		String cat = catalog == null ? "" : catalog + '.';
		String schem = schema == null ? "" : schema + '.';
		log.trace("table found: " + cat + schem + name);
		log.trace("columns: " + columns.keySet());
		if (extras) {
			log.trace("foreign keys: " + foreignKeys.keySet());
			log.trace("indexes: " + indexes.keySet());
		}
	}

	public String getName() {
		return name;
	}

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}

	public String toString() {
		return "TableMetadata(" + name + ')';
	}

	public Iterator<ColumnMetadata> iteratorColumnMetaDatas() {
		return columns.values().iterator();
	}

	public Iterator<ForeignKeyMetadata> iteratorForeignKeyMetadatas() {
		return foreignKeys.values().iterator();
	}

	public Iterator<ForeignKeyMetadata> iteratorForeignedKeyMetadatas() {
		return foreignedKeys.values().iterator();
	}

	public Iterator<IndexMetadata> iteratorIndexMetadatas() {
		return indexes.values().iterator();
	}

	public ColumnMetadata getColumnMetadata(String columnName) {
		return (ColumnMetadata) columns.get(columnName.toLowerCase());
	}

	public ForeignKeyMetadata getForeignKeyMetadata(String keyName) {
		return (ForeignKeyMetadata) foreignKeys.get(keyName.toLowerCase());
	}

	public ForeignKeyMetadata getForeignedKeyMetadata(String keyName) {
		return (ForeignKeyMetadata) foreignedKeys.get(keyName.toLowerCase());
	}

	public ForeignKeyMetadata getForeignKeyMetadata(String columnName, String refTableName) {
		Iterator<ForeignKeyMetadata> fks = foreignKeys.values().iterator();
		while (fks.hasNext()) {
			ForeignKeyMetadata fk = fks.next();
			if (columnName.equals(fk.getColumnName()) && refTableName.equals(fk.getRefTableName())) {
				return fk;
			}
		}
		return null;
	}

	public IndexMetadata getIndexMetadata(String indexName) {
		return (IndexMetadata) indexes.get(indexName.toLowerCase());
	}

	private void addForeignKey(ResultSet rs) throws SQLException {
		String fk = rs.getString("FK_NAME");

		if (fk == null)
			return;

		ForeignKeyMetadata info = getForeignKeyMetadata(fk);
		if (info == null) {
			info = new ForeignKeyMetadata(rs);
			foreignKeys.put(info.getName().toLowerCase(), info);
		}

		info.addColumn(getColumnMetadata(rs.getString("FKCOLUMN_NAME")));
	}

	private void addForeignedKey(ResultSet rs) throws SQLException {
		String fk = rs.getString("FK_NAME");

		if (fk == null)
			return;

		ForeignKeyMetadata info = getForeignedKeyMetadata(fk);
		if (info == null) {
			info = new ForeignKeyMetadata(rs);
			foreignedKeys.put(info.getName().toLowerCase(), info);
		}

		info.addColumn(getColumnMetadata(rs.getString("FKCOLUMN_NAME")));
	}

	private void addIndex(ResultSet rs) throws SQLException {
		String index = rs.getString("INDEX_NAME");

		if (index == null)
			return;

		IndexMetadata info = getIndexMetadata(index);
		if (info == null) {
			info = new IndexMetadata(rs);
			indexes.put(info.getName().toLowerCase(), info);
		}

		info.addColumn(getColumnMetadata(rs.getString("COLUMN_NAME")));
	}

	public void addColumn(ResultSet rs) throws SQLException {
		String column = rs.getString("COLUMN_NAME");

		if (column == null)
			return;

		if (getColumnMetadata(column) == null) {
			ColumnMetadata info = new ColumnMetadata(rs);
			columns.put(info.getName().toLowerCase(), info);
		}
	}

	private void initForeignKeys(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;

		try {

			rs = meta.getImportedKeys(catalog, schema, name);
			while (rs.next())
				addForeignKey(rs);
		} finally {
			if (rs != null)
				rs.close();
		}
		try {
			rs = meta.getExportedKeys(catalog, schema, name);
			while (rs.next())
				addForeignedKey(rs);
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	private void initIndexes(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;

		try {
			rs = meta.getIndexInfo(catalog, schema, name, false, true);

			while (rs.next()) {
				if (rs.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic)
					continue;
				addIndex(rs);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	private void initColumns(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;

		try {
			rs = meta.getColumns(catalog, schema, name, "%");
			while (rs.next()) {
				// if (name.equals("lybbs_db")) {
				// ResultSetMetaData resultmeta = rs.getMetaData();
				// for (int i = resultmeta.getColumnCount(); i > 0; i--) {
				// String name = resultmeta.getColumnName(i);
				// System.out.println(name + " = " + rs.getString(name));
				// }
				// }
				addColumn(rs);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public static Log getLog() {
		return log;
	}

	public Map<String, ColumnMetadata> getColumns() {
		return columns;
	}

	public Map<String, ForeignKeyMetadata> getForeignKeys() {
		return foreignKeys;
	}

	public Map<String, ForeignKeyMetadata> getForeignedKeys() {
		return foreignedKeys;
	}

	public Map<String, IndexMetadata> getIndexes() {
		return indexes;
	}
}
