package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_ACTION;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_FIELD;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_FIELD_GROUP;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;
import static com.kmetop.demsy.comlib.LibConst.F_REFRENCE_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.F_SYSTEM;

import java.util.List;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.IBizAction;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizFieldGroup;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

class CacheBiz {

	private IBizSystem system;

	private List<? extends IBizFieldGroup> groups;

	private List<? extends IBizField> fields;

	private List<? extends IBizField> fieldsOfExport;

	private List<? extends IBizAction> actions;

	CacheBiz(BizEngine engine, Long systemID) {
		if (systemID != null) {
			system = (IBizSystem) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_BZUDF_SYSTEM), systemID);

			if (system != null && Demsy.appconfig.isProductMode())
				engine.bizCache.put(systemID, this);
		}
	}

	IBizSystem get() {
		return system;
	}

	List<? extends IBizFieldGroup> groups() {
		if (groups == null) {
			groups = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_BZUDF_FIELD_GROUP), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return groups;
	}

	List<? extends IBizField> fields() {
		if (fields == null) {
			fields = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_BZUDF_FIELD), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return fields;
	}

	List<? extends IBizAction> actions() {
		if (actions == null) {
			actions = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_BZUDF_ACTION), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return actions;
	}

	IBizAction action(Long id) {
		actions();
		for (IBizAction a : actions) {
			if (a.getId().equals(id)) {
				return a;
			}
		}

		return null;
	}

	IBizAction action(String mode) {
		actions();
		for (IBizAction a : actions) {
			if (mode.equals(a.getMode())) {
				return a;
			}
		}

		return null;
	}

	List<? extends IBizField> fieldsOfExport() {
		if (fieldsOfExport == null) {
			fieldsOfExport = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_BZUDF_FIELD), CndExpr.eq(F_REFRENCE_SYSTEM, system));
		}

		return fieldsOfExport;
	}
}
