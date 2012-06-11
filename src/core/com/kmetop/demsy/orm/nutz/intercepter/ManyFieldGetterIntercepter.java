package com.kmetop.demsy.orm.nutz.intercepter;

import java.lang.reflect.Method;
import java.util.List;

import org.nutz.dao.entity.Link;
import org.nutz.lang.FailToGetValueException;
import org.nutz.lang.Mirror;

import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.orm.nutz.IExtDao;

public class ManyFieldGetterIntercepter extends LinkFieldGetterIntercepter {

	public ManyFieldGetterIntercepter(IExtDao dao, Link link) {
		this.dao = dao;
		this.link = link;
		// FetchType fetch = (FetchType) link.get("fetch");
		this.lazy = true;// FetchType.LAZY == fetch;
	}

	public Object afterInvoke(Object obj, Object returnObj, Method method, Object... args) {
		if (lazy && returnObj == null) {
			String name = link.getOwnField().getName();
			Link mappedLink = (Link) link.get("mappedLink");
			String mappedBy = null;
			if (mappedLink != null) {
				mappedBy = mappedLink.getOwnField().getName();
			}
			dao.fetchLinks(obj, name);
			Mirror me = Mirror.me(obj.getClass());
			try {
				returnObj = me.getValue(obj, me.getField(name));
			} catch (FailToGetValueException e) {
			} catch (NoSuchFieldException e) {
			}
			if (mappedBy != null && returnObj != null) {
				if (returnObj instanceof List) {
					List list = (List) returnObj;
					for (Object elm : list) {
						Obj.setValue(elm, mappedBy, obj);
					}
				} else {
					Obj.setValue(returnObj, mappedBy, obj);
				}
			}
		}

		return returnObj;
	}
}
