package com.kmetop.demsy.orm.nutz;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.mapping.EnMapping;

/**
 * 实体管理器：
 * <p>
 * 1. 通过实体类代理来管理实体
 * <p>
 * 
 * @TODO 2. 长时间不用的实体将被自动清除
 * @author yongshan.ji
 */
public class EnMappingHolder {
	static final Log log = Logs.getLog(EnMappingHolder.class);

	private Map<Class<?>, EnMapping<?>> mappings;// 实体映射<AOP代理类,实体>

	private Map<Class, Class> agents;// 实体代理<实体类，AOP代理类>

	public EnMappingHolder() {
		mappings = new HashMap<Class<?>, EnMapping<?>>();
		agents = new HashMap<Class, Class>();
	}

	public void remove(EnMappingImpl entity) {
		Class classOfT = Cls.getType(entity.getType());

		Class agentType = agents.get(classOfT);
		agents.remove(classOfT);

		if (agentType != null) {
			mappings.remove(agentType);
		}
	}

	public void remove(Class classOfT) {
		classOfT = Cls.getType(classOfT);

		Class agentType = agents.get(classOfT);
		agents.remove(classOfT);

		if (agentType != null) {
			EnMappingImpl entity = (EnMappingImpl) mappings.get(agentType);
			mappings.remove(agentType);

			entity.destroy(this);
		}
	}

	public void cacheEntity(Class agentClass, EnMapping entity) {
		mappings.put(agentClass, entity);
	}

	public void cacheAgent(Class classOfT, Class agentClass) {
		agents.put(classOfT, agentClass);
	}

	public <T> EnMappingImpl<T> getEnMapping(Class<T> classOfT) {
		if (Cls.isLazy(classOfT)) {
			return (EnMappingImpl<T>) getEnMapping(classOfT.getSuperclass());
		}
		if (!Cls.isAgent(classOfT)) {
			classOfT = agents.get(classOfT);
		}

		if (classOfT == null)
			return null;
		else
			return (EnMappingImpl<T>) mappings.get(classOfT);
	}

	public void clear() {
		Iterator<Class> it = agents.keySet().iterator();
		List<Class> list = new LinkedList();

		while (it.hasNext())
			list.add(it.next());

		for (Class cls : list)
			this.remove(cls);
	}

	public int countEntity() {
		return mappings.size();
	}
}
