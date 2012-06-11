package com.kmetop.demsy.orm.generator.impl;

import java.util.ArrayList;
import java.util.List;

import com.kmetop.demsy.orm.generator.INamingStrategy;

public class EncodeNamingStrategy extends SimpleNamingStrategy {
	// protected Log log = Logs.getLog(this.getClass());
	public static final String TABLE_PREFIX = "DEMSY_T_";

	public static final String FIELD_PREFIX = "DEMSY_F_";

	protected static List<String> ENCODE_COLUMN_NAMES;

	protected static List<String> ENCODE_TABLE_NAMES;

	public static final INamingStrategy me = new EncodeNamingStrategy();

	private EncodeNamingStrategy() {
	}

	public String getTablePrefix() {
		return TABLE_PREFIX;
	}

	private synchronized List<String> getEncodeTableNames() {
		if (ENCODE_TABLE_NAMES == null) {
			ENCODE_TABLE_NAMES = new ArrayList();
			ENCODE_TABLE_NAMES.add("");
		}
		return ENCODE_TABLE_NAMES;
	}

	private synchronized List<String> getEncodeColumnNames() {
		if (ENCODE_COLUMN_NAMES == null) {
			ENCODE_COLUMN_NAMES = new ArrayList();
			ENCODE_COLUMN_NAMES.add("DTYPE");
		}
		return ENCODE_COLUMN_NAMES;
	}

	protected boolean dontEncodeTableName(String tableName) {
		boolean dontEncode = true;
		if (tableName == null || tableName.trim().length() == 0 || getEncodeTableNames().contains(tableName)) {
			dontEncode = false;
		}
		// if (!dontEncode && log.isDebugEnabled()) {
		// log.trace("不对数据表加密: " + tableName);
		// }
		return dontEncode;
	}

	protected boolean dontEncodeColumnName(String columnName) {
		boolean dontEncode = true;
		if (columnName == null || columnName.trim().length() == 0) {
			dontEncode = false;
		} else if (columnName.startsWith("_")) {
			dontEncode = false;
		} else if (getEncodeColumnNames().contains(columnName)) {
			dontEncode = false;
		}
		// if (dontEncode && log.isDebugEnabled()) {
		// log.trace("不对数据字段加密: " + columnName);
		// }
		return dontEncode;
	}

	private String encodeTable(String s) {
		return TABLE_PREFIX + Integer.toHexString(s.hashCode()).toUpperCase();
	}

	private String encodeField(String s) {
		return FIELD_PREFIX + Integer.toHexString(s.hashCode()).toUpperCase();
	}

	public String classToTableName(String className) {
		if (className == null) {
			className = "";
		}

		// if (log.isDebugEnabled()) {
		// String tblName = encodeTable(className);
		// log.tracef("数据表: %s[className=%s]", tblName, className);
		// return tblName;
		// }

		return encodeTable(className);
	}

	public String tableName(String tableName) {
		if (tableName == null) {
			tableName = "";
		}

		// if (log.isDebugEnabled()) {
		// String tblName = tableName;
		// if (dontEncodeTableName(tableName)) {
		// tblName = super.tableName(tableName);
		// } else {
		// tblName = encodeTable(tblName);
		// }
		// log.tracef("数据表: %s[tableName=%s]", tblName, tableName);
		// return tblName;
		// }

		if (dontEncodeTableName(tableName)) {
			return super.tableName(tableName);
		} else {
			return encodeTable(tableName);
		}
	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
		if (ownerEntity == null) {
			ownerEntity = "";
		}
		if (ownerEntityTable == null) {
			ownerEntityTable = "";
		}
		if (associatedEntity == null) {
			associatedEntity = "";
		}
		if (associatedEntityTable == null) {
			associatedEntityTable = "";
		}
		if (propertyName == null) {
			propertyName = "";
		}

		String entityName = ownerEntity + ownerEntityTable + associatedEntity + associatedEntityTable + propertyName;

		// if (log.isDebugEnabled()) {
		// String tblName = encodeTable(entityName);
		// log.trace("数据表: " + tblName + "[ownerEntity = " + ownerEntity +
		// ",ownerEntityTable = "
		// + ownerEntityTable + ",associatedEntity = " + associatedEntity
		// + ",associatedEntityTable = " + associatedEntityTable +
		// ",propertyName = "
		// + propertyName + "]");
		// return tblName;
		// }

		return encodeTable(entityName);
	}

	public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
		if (tableName == null) {
			tableName = "";
		}
		if (ownerEntityTable == null) {
			ownerEntityTable = "";
		}
		if (associatedEntityTable == null) {
			associatedEntityTable = "";
		}
		if (propertyName == null) {
			propertyName = "";
		}

		String entityName = tableName + ownerEntityTable + associatedEntityTable + propertyName;

		String tblName = entityName;

		if (dontEncodeTableName(tableName)) {
			tblName = super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName);
		} else {
			tblName = encodeTable(entityName);
		}

		// if (log.isInfoEnabled()) {
		// log.debug("中间表: " + tblName + "[ tableName = " + tableName +
		// ",ownerEntityTable = "
		// + ownerEntityTable + ",associatedEntityTable = " +
		// associatedEntityTable
		// + ",propertyName = " + propertyName + "]");
		// }

		return tblName;
	}

	@Override
	public String columnName(String columnName) {
		if (dontEncodeColumnName(columnName)) {
			String fldName = super.columnName(columnName);
			// if (log.isDebugEnabled()) {
			// log.tracef("数据字段: %s[columnName = %s]", fldName, columnName);
			// }
			return fldName;
		}

		if (columnName == null) {
			columnName = "";
		}
		String fldName = encodeField(columnName);

		// if (log.isDebugEnabled()) {
		// log.tracef("数据字段: %s[columnName=%s]", fldName, columnName);
		// }

		return fldName;
	}

	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
		if (propertyName == null) {
			propertyName = "";
		}
		if (propertyEntityName == null) {
			propertyEntityName = "";
		}
		if (propertyTableName == null) {
			propertyTableName = "";
		}
		if (referencedColumnName == null) {
			referencedColumnName = "";
		}
		String s = propertyName + propertyEntityName + propertyTableName + referencedColumnName;

		String fldName = encodeField(s);

		// if (log.isInfoEnabled()) {
		// log.debug("外键字段: " + fldName + "[propertyName = " + propertyName//
		// + ", propertyEntityName = " + propertyEntityName//
		// + ", propertyTableName = " + propertyTableName//
		// + ", referencedColumnName = " + referencedColumnName//
		// + "]");
		// }

		return fldName;
	}

	@Override
	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		if (joinedColumn == null) {
			joinedColumn = "";
		}
		if (joinedTable == null) {
			joinedTable = "";
		}
		String fldName = encodeField(joinedColumn + joinedTable);

		// if (log.isDebugEnabled()) {
		// log.trace("数据字段: " + fldName + "[joinedColumn = " + joinedColumn//
		// + ", joinedTable = " + joinedTable//
		// + "]");
		// }

		return fldName;
	}

	@Override
	public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
		if (dontEncodeColumnName(columnName)) {
			String fldName = super.logicalCollectionColumnName(columnName, propertyName, referencedColumn);
			// if (log.isDebugEnabled()) {
			// log.trace("数据字段: " + fldName + "[columnName = " + columnName//
			// + ", propertyName = " + propertyName//
			// + ", referencedColumn = " + referencedColumn//
			// + "]");
			// }
			return fldName;
		}

		if (columnName == null) {
			columnName = "";
		}
		if (propertyName == null) {
			propertyName = "";
		}
		if (referencedColumn == null) {
			referencedColumn = "";
		}
		String fldName = encodeField(columnName + propertyName + referencedColumn);

		// if (log.isDebugEnabled()) {
		// log.trace("数据字段: " + fldName + "[columnName = " + columnName//
		// + ", propertyName = " + propertyName//
		// + ", referencedColumn = " + referencedColumn//
		// + "]");
		// }

		return fldName;
	}

	@Override
	public String logicalColumnName(String columnName, String propertyName) {
		if (dontEncodeColumnName(columnName)) {
			String fldName = super.logicalColumnName(columnName, propertyName);
			// if (log.isDebugEnabled()) {
			// log.trace("数据字段: " + fldName + "[columnName = " + columnName//
			// + ", propertyName = " + propertyName//
			// + "]");
			// }
			return fldName;
		}

		if (columnName == null) {
			columnName = "";
		}
		if (propertyName == null) {
			propertyName = "";
		}
		String fldName = encodeField(columnName + propertyName);

		// if (log.isDebugEnabled()) {
		// log.trace("数据字段: " + fldName + "[columnName = " + columnName//
		// + ", propertyName = " + propertyName//
		// + "]");
		// }

		return fldName;
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		if (propertyName == null) {
			propertyName = "";
		}

		// if (log.isDebugEnabled()) {
		// String fldName = encodeField(propertyName);
		// log.trace("数据字段: " + fldName + "[propertyName=" + propertyName +
		// "]");
		// return fldName;
		// }

		return encodeField(propertyName);
	}
}
