package com.kmetop.demsy.orm.nutz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.Link;
import org.nutz.lang.Mirror;

import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.generator.EntityIdGenerator;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.mapping.EnMapping;
import com.kmetop.demsy.util.sort.SortUtils;

public class EnMappingImpl<T> extends Entity<T> implements EnMapping {
	private Map<String, EnColumnMappingImpl> columnMap;// 字段映射:<数据表列名，字段>

	private Map<String, Link> fkMap;// 外键映射：<外键名，字段>

	private List<EntityField> sortedFields;// 按字段名排序后的字段列表

	private Mirror<? extends T> agentMirror;// 代理类：

	private Mirror<? extends T> lazyAgentMirror;// 懒加载代理类

	// 实体间的继承关系
	private EnColumnMappingImpl enTypeCol;// 使用单表时用该字段来区分

	private EnMappingImpl<?> parent;

	private List<EnMappingImpl> children;

	private EntityIdGenerator idTableGenerator;

	private INamingStrategy naming;

	//
	private boolean syncedTable = false;

	private boolean syncedRefTable = false;

	private boolean readonly;

	public EnMappingImpl(EnMappingImpl parent) {
		super();
		columnMap = new HashMap<String, EnColumnMappingImpl>();
		sortedFields = new ArrayList();
		fkMap = new HashMap<String, Link>();
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public void destroy(EnMappingHolder holder) {
		if (children != null) {
			List<EnMappingImpl> list = new LinkedList();
			for (EnMappingImpl c : children) {
				list.add(c);
			}
			for (EnMappingImpl c : list) {
				holder.remove(c);
				c.destroy(holder);
			}
		}

		if (columnMap != null) {
			columnMap.clear();
			columnMap = null;
		}
		if (fkMap != null) {
			fkMap.clear();
			fkMap = null;
		}
		if (sortedFields != null) {
			sortedFields.clear();
			sortedFields = null;
		}
		agentMirror = null;
		lazyAgentMirror = null;
		enTypeCol = null;

		if (parent != null && parent.children != null)
			parent.children.remove(this);

		idTableGenerator = null;
		naming = null;
	}

	public boolean isAbstract() {
		return this.getChildren() != null;
	}

	private void addChild(EnMappingImpl child) {
		if (children == null) {
			children = new ArrayList();
		}
		child.parent = this;
		children.add(child);
	}

	public void addField(EntityField ef) {
		String name = ef.getColumnName();
		if (columnMap.get(name) == null) {
			columnMap.put(name, (EnColumnMappingImpl) ef);
			// 对字段按数据表列名进行排序
			sortedFields.add(ef);
			SortUtils.sort(sortedFields, "columnName", false);

			super.addField(ef);
		}
	}

	public void addForeignKey(String fkColumnName, Link link) {
		fkMap.put(fkColumnName, link);
	}

	public EnColumnMappingImpl getFieldByColumn(String name) {
		EnColumnMappingImpl ret = this.columnMap.get(name);
		if (ret == null)
			ret = this.columnMap.get(name.toUpperCase());
		if (ret == null)
			ret = this.columnMap.get(name.toLowerCase());

		return ret;
	}

	public Link getLink(String fkColumnName) {
		return fkMap.get(fkColumnName);
	}

	public Collection<EntityField> fields() {
		return sortedFields;
	}

	public Mirror<? extends T> getAgentMirror() {
		return agentMirror;
	}

	public void setAgentMirror(Mirror<? extends T> agentMirror) {
		this.agentMirror = agentMirror;
	}

	public Mirror<? extends T> getLazyAgentMirror() {
		return lazyAgentMirror;
	}

	public void setLazyAgentMirror(Mirror<? extends T> agentMirror) {
		this.lazyAgentMirror = agentMirror;
	}

	public EnMappingImpl getParent() {
		return parent;
	}

	public boolean isChild() {
		return getParent() != null;
	}

	public List<EnMappingImpl> getChildren() {
		return children;
	}

	public EnColumnMappingImpl getDtype() {
		return enTypeCol;
	}

	public void setDtype(EnColumnMappingImpl dtype) {
		this.enTypeCol = dtype;
	}

	public EntityIdGenerator getIdGenerator() {
		return idTableGenerator;
	}

	public void setIdGenerator(EntityIdGenerator idTableGenerator) {
		this.idTableGenerator = idTableGenerator;
	}

	public INamingStrategy getNaming() {
		return naming;
	}

	public void setNaming(INamingStrategy naming) {
		this.naming = naming;
	}

	public String getIdProperty() {
		return getIdentifiedField() == null ? null : getIdentifiedField().getName();
	}

	@Override
	public List getRelations(String regex) {
		return this.getLinks(regex);
	}

	public List getManyMany(String regex) {
		List<Link> links = getLinks(regex);
		List<Link> ret = new ArrayList();
		for (Link link : links) {
			if (link.isManyMany() && Str.isEmpty((String) link.get("mappedBy"))) {
				ret.add(link);
			}
		}
		return ret;
	}

	public List getTargetManyMany(String regex) {
		List<Link> links = getLinks(regex);
		List<Link> ret = new ArrayList();
		for (Link link : links) {
			if (link.isManyMany() && !Str.isEmpty((String) link.get("mappedBy"))) {
				ret.add(link);
			}
		}
		return ret;
	}

	public boolean isSyncedTable() {
		return syncedTable;
	}

	public void setSyncedTable(boolean syncTable) {
		this.syncedTable = syncTable;
	}

	public boolean isSyncedRefTable() {
		return syncedRefTable;
	}

	public void setSyncedRefTable(boolean syncRefTable) {
		this.syncedRefTable = syncRefTable;
	}

	public String toString() {
		return "@" + Integer.toHexString(hashCode());
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
}
