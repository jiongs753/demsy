package com.kmetop.demsy.orm.nutz;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.Link;

import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.mapping.EnColumnMapping;
import com.kmetop.demsy.orm.mapping.EnMapping;

public class EnColumnMappingImpl extends EntityField implements EnColumnMapping {
	private static final Log log = Logs.getLog(EnMappingMaker.class);

	public static final int DEFAULT_LENGTH = 255;

	public static final int DEFAULT_PRECISION = 19;

	public static final int DEFAULT_SCALE = 2;

	private int length = DEFAULT_LENGTH;

	private int precision = DEFAULT_PRECISION;

	private int scale = DEFAULT_SCALE;

	private String columnDefinition;

	private String name;

	private String sqlType;

	private FieldValueGetter valueGetter;

	private FieldValueSetter valueSetter;

	private EnMappingHolder holder;

	public EnColumnMappingImpl(EnMappingHolder holder, EnMappingImpl<?> entity, Field field) {
		super(entity, field);
		this.holder = holder;
		init();
	}

	private void init() {
	}

	public String getName() {
		if (!Str.isEmpty(name)) {
			return name;
		}
		// if (getField() == null) {
		// return "";
		// }
		return super.getName();
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		if (length > 0)
			this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		if (precision > 0)
			this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		if (scale > 0)
			this.scale = scale;
	}

	public String getColumnDefinition() {
		return columnDefinition;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public boolean isUnique() {
		return false;
	}

	public boolean hasCheckConstraint() {
		return !Str.isEmpty(getCheckConstraint());
	}

	public String getCheckConstraint() {
		return "";
	}

	public String getComment() {
		return null;
	}

	public String getFkName() {
		Link link = getLink();
		if (link != null) {
			return ((EnMappingImpl) getMapping()).getNaming().fkName(this.getEntity().getTableName(), getColumnName(), link.getTargetClass().getSimpleName());
		}
		return "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSqlType() {
		if (!Str.isEmpty(this.columnDefinition)) {
			return columnDefinition;
		}
		if (!Str.isEmpty(sqlType)) {
			return sqlType;
		}
		return getField().getType().getSimpleName();
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public void fillValue(Object obj, ResultSet rs) {
		// 填充 link one 字段的值为一个“懒代理对象”
		Link linkOne = this.getLink();
		if (linkOne != null) {
			try {
				EnMappingImpl m = (EnMappingImpl) holder.getEnMapping(linkOne.getTargetClass());

				long id = rs.getLong(this.getColumnName());
				if (id > 0 && m != null && m.getLazyAgentMirror() != null) {
					Object lazyObj = m.getLazyAgentMirror().born();
					Obj.setId(m, lazyObj, id);
					Obj.setValue(obj, this.getName(), lazyObj);
				}
			} catch (SQLException e) {
				log.warnf("从ResultSet获取实体<%s>关联字段<%s>出错! 原因： \n%s", obj.getClass().getSimpleName(), this.getName(), e);
			}
			return;
		}

		super.fillValue(obj, rs);
	}

	public void setValue(Object obj, Object value) {
		if (valueSetter == null) {
			super.setValue(obj, value);
		} else {
			valueSetter.set(obj, value);
		}
	}

	public Object getValue(Object obj) {
		if (valueGetter == null) {
			return super.getValue(obj);
		}
		return valueGetter.get(obj);
	}

	public void setValueGetter(FieldValueGetter getter) {
		this.valueGetter = getter;
	}

	public void setValueSetter(FieldValueSetter setter) {
		this.valueSetter = setter;
	}

	@Override
	public EnMapping getMapping() {
		return (EnMapping) this.getEntity();
	}

	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}
}
