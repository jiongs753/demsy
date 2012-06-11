package com.kmetop.demsy.orm.nutz;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.nutz.dao.FieldMatcher;
import org.nutz.dao.entity.Borning;
import org.nutz.dao.entity.EntityField;

import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.mapping.EnColumnMapping;

class EnBorning implements Borning {

	private EnMappingImpl entity;

	private Map<Long, EnMappingImpl> childrenEntity;

	private String dtypeColumnName;

	EnBorning(EnMappingImpl en) {
		entity = en;
		childrenEntity = new HashMap();
		EnColumnMapping dtype = entity.getDtype();
		if (dtype != null) {
			dtypeColumnName = dtype.getColumnName();
			if (entity.getParent() != null) {
				EnBorning parent = (EnBorning) entity.getParent().getBorning();
				Long key = Long.parseLong(dtype.getDefaultValue(null));
				// parent.children.put(key, this);
				parent.childrenEntity.put(key, entity);
			}
		}
	}

	public String toString() {
		return new StringBuffer().append("childrenEntity=").append(childrenEntity.size())//
				.append(", dtypeColumnName=").append(dtypeColumnName)//
				.toString();
	}

	/**
	 * 创建实体代理实例
	 * 
	 * @return
	 * @throws Exception
	 */
	private Object create(Long dtypeValue) throws Exception {
		EnMappingImpl en = getEntity(dtypeValue);
		if (en.getAgentMirror() == null) {
			return null;
		}
		return en.getAgentMirror().born();
	}

	private EnMappingImpl getEntity(Long dtypeValue) {
		EnMappingImpl ret = childrenEntity.get(dtypeValue);
		if (ret != null) {
			return ret;
		}
		return entity;
	}

	public Object born(ResultSet rs, FieldMatcher fm) throws Exception {
		Long dtypevalue = 0l;
		if (!Str.isEmpty(dtypeColumnName)) {
			dtypevalue = rs.getLong(dtypeColumnName);
		}
		Object obj = create(dtypevalue);
		if (obj != null) {
			Iterator<EntityField> it = getEntity(dtypevalue).fields().iterator();
			while (it.hasNext()) {
				EntityField ef = it.next();
				if (null == fm || fm.match(ef.getField().getName()))
					ef.fillValue(obj, rs);
			}
		}
		return obj;
	}
}
