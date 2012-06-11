package com.kmetop.demsy.orm.generator;

public interface INamingStrategy {
	public String classToTableName(String className);

	public String propertyToColumnName(String propertyName);

	public String tableName(String tableName);

	public String columnName(String columnName);

	public String collectionTableName(String ownerEntity, String ownerEntityTable,
			String associatedEntity, String associatedEntityTable, String propertyName);

	public String joinKeyColumnName(String joinedColumn, String joinedTable);

	public String foreignKeyColumnName(String propertyName, String propertyEntityName,
			String propertyTableName, String referencedColumnName);

	public String logicalColumnName(String columnName, String propertyName);

	public String logicalCollectionTableName(String tableName, String ownerEntityTable,
			String associatedEntityTable, String propertyName);

	public String logicalCollectionColumnName(String columnName, String propertyName,
			String referencedColumn);

	public String fkName(String entityName, String columnName, String refrenceName);

	public String getTablePrefix();
}
