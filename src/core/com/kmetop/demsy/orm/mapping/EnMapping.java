package com.kmetop.demsy.orm.mapping;

import java.util.Collection;
import java.util.List;

import com.kmetop.demsy.orm.generator.EntityIdGenerator;

public interface EnMapping<T> {

	String getIdProperty();

	/**
	 * 获取实体类型
	 * <p>
	 * 单表存储具有继承关系数据时需要一个字段来却别数据对应的实体类型
	 * 
	 * @return
	 */
	EnColumnMapping getDtype();

	String getTableName();

	EntityIdGenerator getIdGenerator();

	List getRelations(String regex);

	List getManyMany(String regex);

	<X extends EnColumnMapping> Collection<X> fields();

	Class getType();

	boolean isReadonly();
}
