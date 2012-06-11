package com.kmetop.demsy.orm.nutz.intercepter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

import org.nutz.dao.entity.Link;

import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.orm.nutz.IExtDao;

public class OneFieldGetterIntercepter extends LinkFieldGetterIntercepter {

	public OneFieldGetterIntercepter(IExtDao dao, Link link) {
		this.dao = dao;
		this.link = link;
		// FetchType fetch = (FetchType) link.get("fetch");
		this.lazy = true;// FetchType.LAZY == fetch;
	}

	public Object afterInvoke(Object obj, Object returnObj, Method method, Object... args) {
		if (lazy && Cls.isLazy(returnObj)) {
			Link mappedLink = (Link) link.get("mappedLink");

			String name = link.getOwnField().getName();
			String mappedBy = null;
			if (mappedLink != null) {
				mappedBy = mappedLink.getOwnField().getName();
			}
			Class type = Cls.getType(returnObj.getClass());
			Serializable id = Obj.getId(dao.getEntityHolder().getEnMapping(type), returnObj);
			returnObj = dao.fetch(type, id == null ? 0l : Long.parseLong(id.toString()));
			Obj.setValue(obj, name, returnObj);
			if (mappedBy != null && returnObj != null) {
				Class targetClass = mappedLink.getOwnField().getType();
				if (!Collection.class.isAssignableFrom(targetClass)) {
					Obj.setValue(returnObj, mappedBy, obj);
				}
			}
		}

		return returnObj;
	}
}
