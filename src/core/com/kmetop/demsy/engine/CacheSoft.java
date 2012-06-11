package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_SOFT;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_CONFIG;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_MODULE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_REALM;
import static com.kmetop.demsy.comlib.LibConst.F_CODE;
import static com.kmetop.demsy.comlib.LibConst.F_DOMAIN;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;
import static com.kmetop.demsy.comlib.LibConst.F_REF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.F_SOFT_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IRealm;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.expr.Expr;

class CacheSoft {

	IDemsySoft object;

	Map<String, ISoftConfig> configs;

	Map<String, CacheMdl> mdlGuidMap = new HashMap();

	Map<Long, CacheMdl> mdlIdMap = new HashMap();

	Map<Long, CacheMdl> mdlBizIdMap = new HashMap();

	List<? extends IModule> modules;

	Map<String, IRealm> realmMap = new HashMap();

	List<? extends IRealm> realms;

	private void cache(ModuleEngine engine) {
		if (object != null && Demsy.appconfig.isProductMode()) {
			String key = object.getDomain();
			if (Str.isEmpty(key)) {
				key = object.getCode();
			}
			if (!Str.isEmpty(key)) {
				engine.softCache.put(key, this);
			}
			engine.softIdCache.put(object.getId(), this);
		}

	}

	CacheSoft(ModuleEngine engine, String domainOrCode) {
		object = (IDemsySoft) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_DEMSY_SOFT), Expr.eq(F_DOMAIN, domainOrCode).or(Expr.eq(F_CODE, domainOrCode)));
		this.cache(engine);
	}

	CacheSoft(ModuleEngine engine, Long id) {
		object = (IDemsySoft) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_DEMSY_SOFT), id);
		this.cache(engine);
	}

	Map<String, ISoftConfig> configs() {
		if (configs == null) {
			configs = new HashMap();
			List<ISoftConfig> list = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_ADMIN_CONFIG), Expr.eq(F_SOFT_ID, object.getId()));
			for (ISoftConfig c : list) {
				configs.put(c.getCode(), c);
			}
		}

		return configs;
	}

	IDemsySoft get() {
		return object;
	}

	List<? extends IModule> modules() {
		if (modules == null) {
			modules = (List<? extends IModule>) Demsy.orm().query(bizEngine.getStaticType(BIZSYS_ADMIN_MODULE), Expr.eq(F_SOFT_ID, object.getId()).addAsc(F_ORDER_BY));
			for (IModule mdl : modules) {
				new CacheMdl(this, mdl);
			}
		}
		return modules;
	}

	IModule module(Long id) {
		if (id == null) {
			return null;
		}

		CacheMdl mdl = mdlIdMap.get(id);
		if (mdl == null)
			mdl = new CacheMdl(this, id);

		return mdl.get();
	}

	IModule module(String guid) {
		if (Str.isEmpty(guid)) {
			return null;
		}

		CacheMdl mdl = mdlGuidMap.get(guid);
		if (mdl == null)
			mdl = new CacheMdl(this, guid);

		return mdl.get();
	}

	IModule bizModule(Long bizID) {
		CacheMdl mdl = mdlBizIdMap.get(bizID);
		if (mdl == null)
			mdl = new CacheMdl(this, Expr.eq(F_REF_SYSTEM, bizID).and(Expr.eq(F_SOFT_ID, object.getId())));

		return mdl.get();
	}

	List<? extends IRealm> realms() {
		if (realms == null || realms.size() == 0) {
			realms = (List<? extends IRealm>) Demsy.orm().query(bizEngine.getStaticType(BIZSYS_ADMIN_REALM), Expr.eq(F_SOFT_ID, object.getId()).addAsc(F_ORDER_BY));
			for (IRealm realm : realms) {
				realmMap.put(realm.getCode(), realm);
			}
		}
		return realms;
	}

	public IRealm realm(String realmCode) {
		realms();
		return realmMap.get(realmCode);
	}
}
