package com.kmetop.demsy.orm.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.generator.impl.EncodeNamingStrategy;

public class DatabaseMetadata {

	private static final Log log = Logs.getLog(DatabaseMetadata.class);

	// private final Map tables = new HashMap();
	// private final Set sequences = new HashSet();
	private final boolean extras;

	private DatabaseMetaData meta;

	public DatabaseMetadata(Connection connection, Dialect dialect) throws SQLException {
		this(connection, dialect, true);
	}

	public DatabaseMetadata(Connection connection, Dialect dialect, boolean extras) throws SQLException {
		meta = connection.getMetaData();
		this.extras = extras;
		// initSequences(connection, dialect);
	}

	private static final String[] TYPES = { "TABLE", "VIEW" };

	public TableMetadata getTableMetadata(String name) throws SQLException {
		return this.getTableMetadata(name, null, null, false);
	}

	public TableMetadata getTableMetadata(String name, String schema, String catalog, boolean isQuoted) throws SQLException {
		//
		// Object identifier = identifier(catalog, schema, name);
		// TableMetadata table = (TableMetadata) tables.get(identifier);
		// if (table != null) {
		// return table;
		// } else {
		ResultSet rs = null;
		try {
			if ((isQuoted && meta.storesMixedCaseQuotedIdentifiers())) {
				rs = meta.getTables(catalog, schema, name, TYPES);
			} else if ((isQuoted && meta.storesUpperCaseQuotedIdentifiers()) || (!isQuoted && meta.storesUpperCaseIdentifiers())) {
				rs = meta.getTables(Str.toUpperCase(catalog), Str.toUpperCase(schema), Str.toUpperCase(name), TYPES);
			} else if ((isQuoted && meta.storesLowerCaseQuotedIdentifiers()) || (!isQuoted && meta.storesLowerCaseIdentifiers())) {
				rs = meta.getTables(Str.toLowerCase(catalog), Str.toLowerCase(schema), Str.toLowerCase(name), TYPES);
			} else {
				rs = meta.getTables(catalog, schema, name, TYPES);
			}

			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				if (name.equalsIgnoreCase(tableName)) {
					// table = new TableMetadata(rs, meta, extras);
					// tables.put(identifier, table);
					// return table;
					return new TableMetadata(rs, meta, extras);
				}
			}

			log.info("table not found: " + name);
			return null;
		} finally {
			if (rs != null)
				rs.close();
		}
		// }

	}

	public Map<String, TableMetadata> getTableMetadatas(INamingStrategy naming) throws SQLException {
		if (naming == null) {
			naming = EncodeNamingStrategy.me;
		}
		return this.getTableMetadatas(naming, null, null, false);
	}

	public Map<String, TableMetadata> getTableMetadatas(INamingStrategy naming, String schema, String catalog, boolean isQuoted) throws SQLException {
		Map tables = new HashMap();
		ResultSet rs = null;
		try {
			String name = naming.getTablePrefix() + "%";
			if ((isQuoted && meta.storesMixedCaseQuotedIdentifiers())) {
				rs = meta.getTables(catalog, schema, name, TYPES);
			} else if ((isQuoted && meta.storesUpperCaseQuotedIdentifiers()) || (!isQuoted && meta.storesUpperCaseIdentifiers())) {
				rs = meta.getTables(Str.toUpperCase(catalog), Str.toUpperCase(schema), Str.toUpperCase(name), TYPES);
			} else if ((isQuoted && meta.storesLowerCaseQuotedIdentifiers()) || (!isQuoted && meta.storesLowerCaseIdentifiers())) {
				rs = meta.getTables(Str.toLowerCase(catalog), Str.toLowerCase(schema), Str.toLowerCase(name), TYPES);
			} else {
				rs = meta.getTables(catalog, schema, name, TYPES);
			}

			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				tables.put(tableName, new TableMetadata(rs, meta, extras));
			}

			return tables;

		} finally {
			if (rs != null)
				rs.close();
		}

	}

	// private Object identifier(String catalog, String schema, String name) {
	// return name;
	// }
	//
	// private void initSequences(Connection connection, Dialect dialect) throws
	// SQLException {
	// if (dialect.supportsSequences()) {
	// String sql = dialect.getQuerySequencesString();
	// if (sql != null) {
	//
	// Statement statement = null;
	// ResultSet rs = null;
	// try {
	// statement = connection.createStatement();
	// rs = statement.executeQuery(sql);
	//
	// while (rs.next()) {
	// sequences.add(rs.getString(1).toLowerCase().trim());
	// }
	// } finally {
	// if (rs != null)
	// rs.close();
	// if (statement != null)
	// statement.close();
	// }
	//
	// }
	// }
	// }
	//
	// public boolean isSequence(Object key) {
	// if (key instanceof String) {
	// String[] strings = Strings.split(".", (String) key);
	// return sequences.contains(strings[strings.length - 1].toLowerCase());
	// }
	// return false;
	// }

	public boolean isTable(Object key) throws SQLException {
		if (key instanceof String) {
			boolean quoted = false;
			String name = (String) key;
			if (name.charAt(0) == '`') {
				quoted = true;
				name = name.substring(1, name.length() - 1);
			}
			if (getTableMetadata(name, null, null, quoted) != null) {
				return true;
			} else {
				String[] strings = Str.toArray(".", (String) key);
				if (strings.length == 3) {
					name = strings[2];
					quoted = false;
					if (name.charAt(0) == '`') {
						quoted = true;
						name = name.substring(1, name.length() - 1);
					}
					String catalog = strings[0];
					String schema = strings[1];
					return getTableMetadata(name, schema, catalog, quoted) != null;
				} else if (strings.length == 2) {
					name = strings[1];
					quoted = false;
					if (name.charAt(0) == '`') {
						quoted = true;
						name = name.substring(1, name.length() - 1);
					}
					String schema = strings[0];
					String catalog = null;
					return getTableMetadata(name, schema, catalog, quoted) != null;
				}
			}
		}
		return false;
	}

	// public String toString() {
	// return "DatabaseMetadata" + tables.keySet().toString() +
	// sequences.toString();
	// }
}
