package com.kmetop.demsy.orm.nutz;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.Link;
import org.nutz.dao.impl.DemsyInsertInvoker;
import org.nutz.lang.Each;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;

import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.orm.mapping.EnMapping;

public class SaveManyManyInvoker extends DemsyInsertInvoker {

	public SaveManyManyInvoker(Dao dao, Object mainObj, Mirror<?> mirror) {
		super(dao, mainObj, mirror);
	}

	public void invoke(final Link link, Object mm) {
		Object first = Lang.first(mm);
		if (null != first) {
			final Object fromValue = mirror.getValue(mainObj, link.getReferField());
			final Mirror<?> mta = Mirror.me(first.getClass());
			Lang.each(mm, new Each<Object>() {
				public void invoke(int i, Object ta, int length) {
					Exception failInsert = null;
					try {
						if (Obj.isEmpty((EnMapping) dao.getEntity(ta.getClass()), ta)) {
							dao.insert(ta);
						}
					} catch (Exception e) {
						ta = dao.fetch(ta);
						failInsert = e;
					}
					if (null == ta) {
						if (null != failInsert)
							throw new RuntimeException(failInsert);
						throw new RuntimeException("You set null to param '@ta[2]'");
					}
					insertManyManyRelation(link, fromValue, mta, ta);
				}
			});
		}
	}
}