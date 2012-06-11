package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.comlib.LibConst.*;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

class CacheMdl {

	private IModule object;

	CacheMdl(CacheSoft soft, IModule mdl) {
		object = mdl;
		cache(soft);
	}

	CacheMdl(CacheSoft soft, Long moduleID) {
		if (moduleID != null) {
			object = (IModule) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_ADMIN_MODULE), moduleID);

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, String moduleGuid) {
		if (!Str.isEmpty(moduleGuid)) {
			object = (IModule) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_ADMIN_MODULE), Expr.eq(F_GUID, moduleGuid).or(Expr.eq(F_CODE, moduleGuid)));

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, CndExpr expr) {
		object = (IModule) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_ADMIN_MODULE), expr);

		cache(soft);
	}

	private void cache(CacheSoft soft) {
		if (object != null && Demsy.appconfig.isProductMode()) {
			soft.mdlIdMap.put(object.getId(), this);
			soft.mdlGuidMap.put(object.getEntityGuid(), this);
			if (object.getType() == IModule.TYPE_BIZ) {
				soft.mdlBizIdMap.put(object.getRefID(), this);
			}
		}
	}

	IModule get() {
		return object;
	}
}
