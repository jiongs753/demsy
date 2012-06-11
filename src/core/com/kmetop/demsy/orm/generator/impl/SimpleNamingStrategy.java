package com.kmetop.demsy.orm.generator.impl;

import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.generator.INamingStrategy;

public class SimpleNamingStrategy implements INamingStrategy {
	public static final INamingStrategy me = new SimpleNamingStrategy();

	protected SimpleNamingStrategy() {
	}

	public String classToTableName(String className) {
		return Str.addUnderscores(Str.unqualify(className));
	}

	public String propertyToColumnName(String propertyName) {
		return Str.addUnderscores(Str.unqualify(propertyName));
	}

	public String tableName(String tableName) {
		return Str.addUnderscores(tableName);
	}

	public String columnName(String columnName) {
		return Str.addUnderscores(columnName);
	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable,
			String associatedEntity, String associatedEntityTable, String propertyName) {
		return tableName(ownerEntityTable + '_' + propertyToColumnName(propertyName));
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return columnName(joinedColumn);
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName,
			String propertyTableName, String referencedColumnName) {
		String header = propertyName != null ? Str.unqualify(propertyName) : propertyTableName;
		if (header == null)
			throw new RuntimeException("NamingStrategy not properly filled");
		return columnName(header);
	}

	public String logicalColumnName(String columnName, String propertyName) {
		return Str.isEmpty(columnName) ? Str.unqualify(propertyName) : columnName;
	}

	public String logicalCollectionTableName(String tableName, String ownerEntityTable,
			String associatedEntityTable, String propertyName) {
		if (tableName != null) {
			return tableName;
		} else {
			return new StringBuffer(ownerEntityTable).append("_").append(
					associatedEntityTable != null ? associatedEntityTable : Str
							.unqualify(propertyName)).toString();
		}
	}

	public String logicalCollectionColumnName(String columnName, String propertyName,
			String referencedColumn) {
		return Str.isEmpty(columnName) ? Str.unqualify(propertyName) + "_"
				+ referencedColumn : columnName;
	}

	public String fkName(String entity, String columnName, String refrenceEntity) {
		int result = 0;
		if (columnName != null) {
			result += columnName.hashCode();
		}
		if (refrenceEntity != null) {
			result += refrenceEntity.hashCode();
		}
		return "FK_"
				+ (Integer.toHexString(entity.hashCode()) + Integer.toHexString(result))
						.toUpperCase();
	}

	@Override
	public String getTablePrefix() {
		return "";
	}
}
