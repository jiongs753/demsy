package com.kmetop.demsy.orm.mapping;

public interface EnColumnMapping {
	EnMapping getMapping();

	String getColumnName();

	String getDefaultValue(Object object);

	String getSqlType();

	int getScale();

	int getLength();

	int getPrecision();
}
