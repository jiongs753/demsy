package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_CORP;
import static com.kmetop.demsy.comlib.LibConst.F_CODE;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.entity.IDemsyCorp;
import com.kmetop.demsy.orm.expr.Expr;

class CacheCorp {

	private IDemsyCorp corp;

	CacheCorp(ModuleEngine engine, String code) {
		corp = (IDemsyCorp) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_DEMSY_CORP), Expr.eq(F_CODE, code));

		if (corp != null && Demsy.appconfig.isProductMode())
			engine.corpCache.put(code, this);
	}

	IDemsyCorp get() {
		return corp;
	}
}
