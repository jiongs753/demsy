package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.comlib.LibConst.*;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.expr.Expr;

class CacheWeb {

	private IWebContentCatalog obj;

	CacheWeb(UiEngine engine, Long id) {
		if (id != null) {
			obj = (IWebContentCatalog) Demsy.orm().load(bizEngine.getStaticType(IWebContentCatalog.SYS_CODE), id);

			if (obj != null) {
				if (Demsy.appconfig.isProductMode()) {
					engine.webCache.put(id, this);
					engine.webGuidCache.put(obj.getEntityGuid(), this);
				}
			}
		}
	}

	CacheWeb(UiEngine engine, String guid) {
		if (!Str.isEmpty(guid)) {
			obj = (IWebContentCatalog) Demsy.orm().load(bizEngine.getStaticType(IWebContentCatalog.SYS_CODE), Expr.eq(F_GUID, guid));

			if (obj != null) {
				if (Demsy.appconfig.isProductMode()) {
					engine.webCache.put(obj.getId(), this);
					engine.webGuidCache.put(guid, this);
				}
			}
		}
	}

	IWebContentCatalog get() {
		return obj;
	}

}
