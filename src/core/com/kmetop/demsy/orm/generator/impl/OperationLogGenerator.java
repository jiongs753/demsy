package com.kmetop.demsy.orm.generator.impl;

import static com.kmetop.demsy.comlib.LibConst.F_CREATED;
import static com.kmetop.demsy.comlib.LibConst.F_CREATED_BY;
import static com.kmetop.demsy.comlib.LibConst.F_CREATED_IP;
import static com.kmetop.demsy.comlib.LibConst.F_ID;
import static com.kmetop.demsy.comlib.LibConst.F_UPDATED;
import static com.kmetop.demsy.comlib.LibConst.F_UPDATED_BY;
import static com.kmetop.demsy.comlib.LibConst.F_UPDATED_IP;

import java.io.Serializable;
import java.util.Date;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.orm.generator.Generator;
import com.kmetop.demsy.orm.nutz.IExtDao;

public class OperationLogGenerator implements Generator {

	@Override
	public Serializable generate(IExtDao dao, Object obj) {

		Demsy me = Demsy.me();

		Serializable id = (Serializable) Obj.getValue(obj, F_ID);

		if (id == null || (id instanceof Number && ((Number) id).longValue() <= 0)) {
			if (Cls.hasField(obj.getClass(), F_CREATED))
				Obj.setValue(obj, F_CREATED, new Date());

			if (Cls.hasField(obj.getClass(), F_CREATED_BY) && me != null)
				Obj.setValue(obj, F_CREATED_BY, me.username());

			if (Cls.hasField(obj.getClass(), F_CREATED_IP) && me != null)
				Obj.setValue(obj, F_CREATED_IP, me.request().getRemoteAddr());
		}

		if (Cls.hasField(obj.getClass(), F_UPDATED))
			Obj.setValue(obj, F_UPDATED, new Date());

		if (Cls.hasField(obj.getClass(), F_UPDATED_BY) && me != null)
			Obj.setValue(obj, F_UPDATED_BY, me.username());

		if (Cls.hasField(obj.getClass(), F_UPDATED_IP) && me != null)
			Obj.setValue(obj, F_UPDATED_IP, me.request().getRemoteAddr());

		return null;
	}
}
