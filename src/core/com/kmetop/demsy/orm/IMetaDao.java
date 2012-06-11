package com.kmetop.demsy.orm;

import java.util.Map;

import com.kmetop.demsy.orm.mapping.EnMapping;
import com.kmetop.demsy.orm.metadata.ColumnMetadata;
import com.kmetop.demsy.orm.metadata.ForeignKeyMetadata;
import com.kmetop.demsy.orm.metadata.TableMetadata;

public interface IMetaDao {

	/**
	 * 清空数据库表
	 */
	void clear();

	/**
	 * 统计数据库中有多少张数据表
	 * 
	 * @return
	 */
	int countTable();

	/**
	 * 计算实体有多少个字段
	 * 
	 * @param entity
	 * @return
	 */
	int countColumn(EnMapping mapping);

	/**
	 * 计算实体表外键数量
	 * 
	 * @param mapping
	 * @return
	 */
	int countFK(EnMapping mapping);

	/**
	 * 计算实体表被其他表外键引用的次数
	 * 
	 * @param mapping
	 * @return
	 */
	int countExportFK(EnMapping mapping);

	/**
	 * 删除实体表，同时删除多对多关联表
	 * 
	 * @param mapping
	 */
	void dropTables(EnMapping mapping);

	/**
	 * 检查实体表： 如果实体表不存在，则自动创建。
	 * <p>
	 * 该方法供EntityHolder加载实体时调用。
	 * 
	 * @param entity
	 *            待检查的实体
	 * @param dependMe
	 *            哪些实体引用到该实体
	 * @param syncRefTable
	 *            是否同步外键关联表
	 */
	void syncTables(EnMapping<?> mapping, boolean syncRefTable);

	/**
	 * 获取数据库中的所有数据表
	 * 
	 * @return
	 */
	Map<String, TableMetadata> tables();

	/**
	 * 获取实体表元数据信息
	 * 
	 * @param mapping
	 * @return
	 */
	TableMetadata table(EnMapping mapping);

	/**
	 * 获取表元数据信息
	 * 
	 * @param tableName
	 * @return
	 */
	TableMetadata table(String tableName);

	/**
	 * 获取实体的所有字段
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ColumnMetadata> columns(EnMapping mapping);

	/**
	 * 获取实体的所有字段
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ColumnMetadata> columns(String tableName);

	/**
	 * 获取实体被哪些外键所引用
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ForeignKeyMetadata> exportFks(String tableName);

	/**
	 * 获取实体被哪些外键所引用
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ForeignKeyMetadata> exportFks(EnMapping mapping);

	/**
	 * 获取实体外键
	 * 
	 * @param tableName
	 * @return
	 */
	Map<String, ForeignKeyMetadata> importFks(String tableName);

	/**
	 * 获取实体外键
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ForeignKeyMetadata> importFks(EnMapping mapping);
}
