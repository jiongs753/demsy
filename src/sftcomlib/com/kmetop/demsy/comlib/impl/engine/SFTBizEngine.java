package com.kmetop.demsy.comlib.impl.engine;

import static com.kmetop.demsy.comlib.LibConst.F_CODE;
import static com.kmetop.demsy.comlib.LibConst.F_MODE;
import static com.kmetop.demsy.comlib.LibConst.F_PROP_NAME;
import static com.kmetop.demsy.comlib.LibConst.F_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.F_TYPE_CODE;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.nutz.lang.Mirror;

import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizFieldType;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.IRuntimeConfigable;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzCata;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.SubSystem;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.impl.base.biz.BizAction;
import com.kmetop.demsy.comlib.impl.base.biz.BizCatalog;
import com.kmetop.demsy.comlib.impl.base.lib.ActionLib;
import com.kmetop.demsy.comlib.impl.sft.SFTBizEntity;
import com.kmetop.demsy.comlib.impl.sft.dic.Dic;
import com.kmetop.demsy.comlib.impl.sft.dic.DicCategory;
import com.kmetop.demsy.comlib.impl.sft.system.AbstractSystemData;
import com.kmetop.demsy.comlib.impl.sft.system.SFTSystem;
import com.kmetop.demsy.comlib.impl.sft.system.SystemDataGroup;
import com.kmetop.demsy.comlib.impl.sft.system.SystemDataType;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.engine.BizEngine;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.JSON;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Option;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

public class SFTBizEngine extends BizEngine {

	public SFTBizEngine() {
		compiler = new SFTBizCompiler(this);
	}

	@Override
	public String getPackageOfAutoSystem(IBizSystem system) {
		return "com.kmetop.demsy.comlib.impl.sft.dynamic.system";
	}

	public String getSimpleClassName(IBizSystem system) {
		return "SFTSystem_" + Long.toHexString(system.getId());
	}

	@Override
	public String getExtendClassName(IBizSystem system) {
		String extClass = system.getExtendClass();
		if (Str.isEmpty(extClass)) {
			return SFTBizEntity.class.getName();
		} else {
			extClass = extClass.substring(extClass.lastIndexOf(".") + 1);
			if (extClass.equals("AbstractSFTEntity")) {
				return SFTBizEntity.class.getName();
			}
		}
		if (extClass.indexOf(".") < 0) {
			return "com.kmetop.demsy.comlib.entity.base." + extClass;
		}
		return extClass;
	}

	@Override
	public String getExtendSimpleClassName(IBizSystem system) {
		String extClass = system.getExtendClass();
		if (Str.isEmpty(extClass)) {
			return SFTBizEntity.class.getSimpleName();
		} else {
			extClass = extClass.substring(extClass.lastIndexOf(".") + 1);
			if (extClass.equals("AbstractSFTEntity")) {
				return SFTBizEntity.class.getSimpleName();
			}
		}
		return extClass;
	}

	@Override
	public String getTargetPropName(IBizField field) {
		return ("childrenMapping_" + Integer.toHexString(field.getSystem().getCode().hashCode()) + "_" + getPropName(field))
				.toLowerCase();
	}

	protected Option[] geDicOptions(IBizField field) {
		AbstractSystemData fld = (AbstractSystemData) field;
		DicCategory dicc = fld.getDicCategory();

		List<Option> oplist = new ArrayList();
		if (dicc != null && !dicc.isDisabled()) {
			List<Dic> list = dicc.getDics();
			for (Dic dic : list) {
				if (!dic.isDisabled() && !dic.isDisabled())
					oplist.add(new Option("" + dic.getId(), dic.getName()));
			}
		}

		Option[] ret = new Option[oplist.size()];
		ret = oplist.toArray(ret);

		return ret;
	}

	protected boolean isMappingToMaster(IBizField fld) {
		AbstractSystemData data = (AbstractSystemData) fld;
		return data.isMappingToMaster() || data.isMapping();
	}

	public boolean isGridField(IBizField fld) {
		AbstractSystemData data = (AbstractSystemData) fld;
		return data.isGridField() || data.isShowInGrid();
	}

	@Override
	public IBizSystem setupSystemFromClass(IDemsySoft soft, Class klass) throws DemsyException {
		return setupSystemFromClass(soft.getId(), klass, true);
	}

	private IBizSystem setupSystemFromClass(Long soft, Class klass, boolean autoUpdate) throws DemsyException {
		Map<String, IBizFieldType> fieldLibs = this.getFieldTypes();
		if (klass == null) {
			return null;
		}
		BzSys sysann = (BzSys) klass.getAnnotation(BzSys.class);
		Entity entity = (Entity) klass.getAnnotation(Entity.class);
		if (sysann == null || entity == null) {
			return null;
		}

		if (log.isTraceEnabled())
			log.tracef("安装业务系统... [%s]", klass.getSimpleName());

		IOrm orm = orm();

		// 解析业务系统
		int sysOrder = sysann.orderby();
		String sysCode = sysann.code();
		if (Str.isEmpty(sysCode)) {
			sysCode = "_" + klass.getSimpleName();
		}
		SFTSystem system = (SFTSystem) orm.load(SFTSystem.class, CndExpr.eq(F_CODE, sysCode));
		if (system != null && !autoUpdate) {
			return system;
		}
		system = this.parseSystem(orm, soft, klass, system, sysann, sysOrder, sysCode);
		orm.save(system);

		Mirror me = Mirror.me(klass);

		// 计算业务操作
		List<BizAction> newActions = new LinkedList();
		BzAct[] actanns = sysann.actions();
		if (actanns != null) {
			int actionOrder = sysOrder * 100;
			for (int i = 0; i < actanns.length; i++) {
				BzAct actann = actanns[i];

				if (Str.isEmpty(actann.jsonData())) {
					BizAction action = this.parseBizAction(orm, soft, system, actann, actionOrder++);
					orm.save(action);
					newActions.add(action);
				} else {
					newActions.addAll(this.parseBizAction(orm, soft, system, actann.jsonData(), actionOrder++));
				}

			}
		}
		List<SystemDataGroup> newGroups = new LinkedList();
		List<IBizField> newFields = new LinkedList();
		BzGrp[] grpanns = sysann.groups();
		if (grpanns != null) {
			int groupOrder = sysOrder * 10;
			int fieldOrder = sysOrder * 100;
			int gridOrder = 100;
			for (int i = 0; i < grpanns.length; i++) {
				BzGrp grpann = grpanns[i];
				SystemDataGroup group = this.parseBizGroup(orm, soft, system, grpann, groupOrder++);
				orm.save(group);
				newGroups.add(group);

				// 计算业务字段
				BzFld[] fldanns = grpann.fields();
				if (fldanns != null) {
					for (int j = 0; j < fldanns.length; j++) {
						BzFld fldann = fldanns[j];
						AbstractSystemData field = this.parseBizField(orm, soft, me, system, group, fldann,
								gridOrder++, fieldOrder++, fieldLibs);
						orm.save(field);
						newFields.add(field);
					}
				}
			}
		}

		// 清除垃圾数据
		// if (false) {
		List<BizAction> oldActions = orm.query(BizAction.class, Expr.eq(F_SYSTEM, system));
		for (BizAction a : oldActions) {
			if (!newActions.contains(a)) {
				orm.delete(a);
			}
		}
		// }
		List<AbstractSystemData> oldFields = orm.query(AbstractSystemData.class, Expr.eq(F_SYSTEM, system));
		for (AbstractSystemData a : oldFields) {
			if (!newFields.contains(a)) {
				orm.delete(a);
			}
		}
		List<SystemDataGroup> oldGroups = orm.query(SystemDataGroup.class, Expr.eq(F_SYSTEM, system));
		for (SystemDataGroup a : oldGroups) {
			if (!newGroups.contains(a)) {
				orm.delete(a);
			}
		}

		return system;
	}

	public void parseSystemByAnnotation(Class klass, IBizSystem system) {
		IOrm orm = orm();
		Long soft = system.getSoftID();

		Map<String, IBizFieldType> fieldLibs = this.getFieldTypes();
		BzSys sysann = (BzSys) klass.getAnnotation(BzSys.class);
		if (sysann == null) {
			return;
		}

		Integer sysOrder = system.getOrderby();

		Mirror me = Mirror.me(klass);

		// 计算业务操作
		List<BizAction> newActions = new LinkedList();
		BzAct[] actanns = sysann.actions();
		if (actanns != null) {
			int actionOrder = sysOrder * 100;
			for (int i = 0; i < actanns.length; i++) {
				BzAct actann = actanns[i];

				if (Str.isEmpty(actann.jsonData())) {
					BizAction action = this.parseBizAction(orm, soft, system, actann, actionOrder++);
					orm.save(action);
					newActions.add(action);
				} else {
					newActions.addAll(this.parseBizAction(orm, soft, system, actann.jsonData(), actionOrder++));
				}

			}
		}
		List<SystemDataGroup> newGroups = new LinkedList();
		List<IBizField> newFields = new LinkedList();
		BzGrp[] grpanns = sysann.groups();
		if (grpanns != null) {
			int groupOrder = sysOrder * 10;
			int fieldOrder = sysOrder * 100;
			int gridOrder = 100;
			for (int i = 0; i < grpanns.length; i++) {
				BzGrp grpann = grpanns[i];
				SystemDataGroup group = this.parseBizGroup(orm, soft, system, grpann, groupOrder++);
				orm.save(group);
				newGroups.add(group);

				// 计算业务字段
				BzFld[] fldanns = grpann.fields();
				if (fldanns != null) {
					for (int j = 0; j < fldanns.length; j++) {
						BzFld fldann = fldanns[j];
						AbstractSystemData field = this.parseBizField(orm, soft, me, system, group, fldann,
								gridOrder++, fieldOrder++, fieldLibs);
						orm.save(field);
						newFields.add(field);
					}
				}
			}
		}
	}

	private SFTSystem parseSystem(IOrm orm, Long soft, Class klass, SFTSystem system, BzSys sysann, int sysOrder,
			String code) {
		if (system == null) {
			system = new SFTSystem();
		}
		system.setName(sysann.name());

		system.setCode(code);
		system.setDesc(sysann.desc());
		system.setLayout(sysann.layout());
		system.setOrderby(sysOrder);
		system.setTemplate(sysann.template());
		system.setMappingClass(klass.getName());
		system.setExtendClass(klass.getSuperclass().getSimpleName());
		system.setBuildin(true);

		BizCatalog catalog;
		if (!Str.isEmpty(sysann.catalog())) {
			catalog = (BizCatalog) orm.load(BizCatalog.class, Expr.eq(F_CODE, sysann.catalog()));
		} else {
			catalog = parseCatalog(orm, soft, klass.getPackage());
		}
		system.setCatalog(catalog);
		system.setSoftID(soft);

		Obj.makeSetupGuid(system, code);

		return system;
	}

	private BizCatalog parseCatalog(IOrm orm, Long soft, Package pkg) {
		if (pkg == null)
			return null;
		BzCata ann = pkg.getAnnotation(BzCata.class);
		if (ann == null)
			return null;

		// 计算code
		String code = ann.code();
		String pkgname = pkg.getName();
		int dot = pkgname.lastIndexOf(".");
		String ppkgname = pkgname.substring(0, dot);
		if (Str.isEmpty(code)) {
			code = "_" + pkgname.substring(dot + 1);
		}

		// 计算对象
		BizCatalog obj = (BizCatalog) orm.load(BizCatalog.class, CndExpr.eq(F_CODE, code));
		if (obj == null) {
			obj = new BizCatalog();
			obj.setName(ann.name());
			obj.setCode(code);
			obj.setOrderby(ann.orderby());
			obj.setBuildin(true);
			obj.setParent(parseCatalog(orm, soft, Package.getPackage(ppkgname)));
		}

		Obj.makeSetupGuid(obj, code);

		orm.save(obj);

		return obj;
	}

	private AbstractSystemData parseBizField(IOrm orm, Long soft, Mirror me, IBizSystem system, SystemDataGroup group,
			BzFld fldann, int gridOrder, int fieldOrder, Map fieldLibs) {
		String prop = fldann.property();

		AbstractSystemData field = (AbstractSystemData) orm.load(AbstractSystemData.class, CndExpr
				.eq(F_PROP_NAME, prop).and(CndExpr.eq(F_SYSTEM, system)));
		field = (AbstractSystemData) this.parseBizField(soft, me, prop, system, field);
		field.setDataGroup(group);

		Field f = null;
		try {
			f = me.getField(prop);
		} catch (NoSuchFieldException iglore) {
		}
		if (f == null || f.getAnnotation(BzFld.class) == null) {
			copyProperties(fldann, field);
			if (!Str.isEmpty(fldann.refrenceSystem())) {
				field.setRefrenceSystem((SFTSystem) setupSystemFromClass(soft, getStaticType(fldann.refrenceSystem()),
						false));
				field.setType((SystemDataType) fieldLibs.get("System"));
			}
		}
		field.setOrderby(fieldOrder);

		if (fldann.gridOrder() > 0)
			field.setGridOrder(fldann.gridOrder());
		else
			field.setGridOrder(gridOrder);

		// 字段类型
		if (field.getType() == null) {
			throw new DemsyException("未知字段类型! [system: %s, field: %s]", me.getType().getSimpleName(), prop);
		}
		field.setBuildin(true);
		Obj.makeSetupGuid(field, system.getCode(), field.getCode());

		return field;
	}

	private SystemDataGroup parseBizGroup(IOrm orm, Long soft, IBizSystem system, BzGrp grpann, int groupOrder) {
		SystemDataGroup group = (SystemDataGroup) orm.load(SystemDataGroup.class, CndExpr.eq(F_CODE, grpann.code())
				.and(CndExpr.eq(F_SYSTEM, system)));
		if (group == null) {
			group = new SystemDataGroup();
		}
		group.setSystem((SFTSystem) system);
		group.setName(grpann.name());
		group.setCode(grpann.code());
		group.setDesc(grpann.desc());
		group.setOrderby(groupOrder);
		group.setSoftID(soft);
		group.setBuildin(true);

		Obj.makeSetupGuid(group, system.getCode(), grpann.code());

		return group;
	}

	private BizAction parseBizAction(IOrm orm, Long soft, IBizSystem system, BzAct actann, int actionOrder) {
		BizAction action = (BizAction) orm.load(
				BizAction.class,
				CndExpr.eq(F_TYPE_CODE, actann.typeCode()).and(CndExpr.eq(F_MODE, actann.mode()))
						.and(CndExpr.eq(F_SYSTEM, system.getId())));
		if (action == null) {
			action = new BizAction();
		}
		action.setSystem(system);
		action.setName(actann.name());
		action.setTypeCode(actann.typeCode());
		action.setMode(actann.mode());
		action.setCode(actann.code());
		action.setDesc(actann.desc());
		action.setOrderby(actionOrder);
		action.setTargetUrl(actann.targetUrl());
		action.setTargetWindow(actann.targetWindow());
		action.setPlugin(actann.plugin());
		action.setImage(new Upload(actann.image()));
		action.setLogo(new Upload(actann.logo()));
		action.setTemplate(actann.template());
		action.setInfo(actann.info());
		action.setError(actann.error());
		action.setWarn(actann.warn());
		action.setParams(actann.params());
		action.setDisabled(actann.disabled());

		IAction actionLib = (IAction) orm.load(ActionLib.class, CndExpr.eq(F_TYPE_CODE, actann.typeCode()));
		action.setActionLib((ActionLib) actionLib);
		action.setSoftID(soft);
		action.setBuildin(true);
		Obj.makeSetupGuid(action, system.getCode(), "" + actann.typeCode(), actann.mode());

		return action;
	}

	private List<BizAction> parseBizAction(IOrm orm, Long soft, IBizSystem system, String jsonData, int actionOrder) {
		List<BizAction> actions = JSON.loadFromJson(BizAction.class, jsonData);
		List<BizAction> ret = new ArrayList();
		for (BizAction newAction : actions) {
			this.parseBizAction(ret, orm, soft, system, newAction, null, actionOrder++);
		}

		return ret;
	}

	private void parseBizAction(List<BizAction> list, IOrm orm, Long soft, IBizSystem system, BizAction newAction,
			BizAction parentAction, int actionOrder) {
		BizAction action = (BizAction) orm.load(
				BizAction.class,
				CndExpr.eq(F_TYPE_CODE, newAction.getTypeCode()).and(CndExpr.eq(F_MODE, newAction.getMode()))
						.and(CndExpr.eq(F_SYSTEM, system.getId())));
		if (action == null) {
			action = newAction;
		}
		action.setSystem(system);
		action.setName(newAction.getName());
		action.setTypeCode(newAction.getTypeCode());
		action.setMode(newAction.getMode());
		action.setCode(newAction.getCode());
		action.setDesc(newAction.getDesc());
		action.setOrderby(actionOrder++);
		action.setTargetUrl(newAction.getTargetUrl());
		action.setTargetWindow(newAction.getTargetWindow());
		action.setPlugin(newAction.getPlugin());
		action.setImage(newAction.getImage());
		action.setLogo(newAction.getLogo());
		action.setTemplate(newAction.getTemplate());
		action.setInfo(newAction.getInfo());
		action.setError(newAction.getError());
		action.setWarn(newAction.getWarn());
		action.setDisabled(newAction.isDisabled());
		action.setParentAction(parentAction);

		IAction actionLib = (IAction) orm.load(ActionLib.class, CndExpr.eq(F_TYPE_CODE, newAction.getTypeCode()));
		action.setActionLib((ActionLib) actionLib);
		action.setSoftID(soft);
		action.setBuildin(true);
		Obj.makeSetupGuid(action, system.getCode(), "" + newAction.getTypeCode(), newAction.getMode());

		orm.save(action);
		list.add(action);

		List<BizAction> children = newAction.getChildren();
		if (children != null)
			for (BizAction newchild : children) {
				this.parseBizAction(list, orm, soft, system, newchild, action, actionOrder++);
			}
	}

	public IBizField parseBizField(Long soft, Mirror entityObj, String fldProp, IBizSystem bzSystem,
			IBizField oldBzField) {
		Map<String, IBizFieldType> fieldLibs = this.getFieldTypes();

		AbstractSystemData newBzField = (AbstractSystemData) oldBzField;
		if (newBzField == null) {
			newBzField = new AbstractSystemData();
		}
		newBzField.setPropName(fldProp);
		newBzField.setCode(fldProp);
		newBzField.setSystem((SFTSystem) bzSystem);

		Class type = String.class;
		Class[] genericTypes = null;

		try {
			// 字段注释
			BzFld annBzFld = null;
			Column annColumn = null;
			ManyToOne manyToOne = null;
			try {
				Field f = entityObj.getField(fldProp);
				if (f != null) {
					type = f.getType();
					annColumn = f.getAnnotation(Column.class);
					annBzFld = f.getAnnotation(BzFld.class);
					manyToOne = f.getAnnotation(ManyToOne.class);
					genericTypes = Mirror.getGenericTypes(f);
				} else {
					throw new DemsyException("field not existed");
				}
			} catch (Throwable e) {
				String uprop = fldProp.substring(0, 1).toUpperCase() + fldProp.substring(1);
				Method m = null;
				try {
					m = entityObj.findMethod("get" + uprop);
				} catch (Throwable e1) {
					try {
						m = entityObj.findMethod("is" + uprop);
					} catch (Throwable e2) {
					}
				}
				if (m != null) {
					type = m.getReturnType();
					annColumn = m.getAnnotation(Column.class);
					annBzFld = m.getAnnotation(BzFld.class);
					manyToOne = m.getAnnotation(ManyToOne.class);
				}
			}

			// 字段精度
			if (annColumn != null) {
				int p = annColumn.precision();
				if (p == 0) {
					p = annColumn.length();
				}
				if (p > 0)
					newBzField.setPrecision(p);
				newBzField.setScale(annColumn.scale());
			}

			if (annBzFld != null) {
				copyProperties(annBzFld, newBzField);
			}

			// 解析字段类型
			IBizFieldType fieldLib;
			if (annBzFld != null && !Str.isEmpty(annBzFld.refrenceSystem())) {
				newBzField.setRefrenceSystem((SFTSystem) setupSystemFromClass(soft,
						getStaticType(annBzFld.refrenceSystem()), false));
				fieldLib = fieldLibs.get("System");
			} else if (manyToOne != null) {
				newBzField.setRefrenceSystem((SFTSystem) setupSystemFromClass(soft, type, false));
				fieldLib = fieldLibs.get("System");
			} else if (annBzFld != null && !Str.isEmpty(annBzFld.type())) {
				fieldLib = fieldLibs.get(annBzFld.type());
			} else if (SubSystem.class.isAssignableFrom(type)) {// 子系统字段
				if (genericTypes != null && genericTypes.length > 0) {
					newBzField.setRefrenceSystem((SFTSystem) setupSystemFromClass(soft, genericTypes[0], false));
				}
				newBzField.setRefrenceFields(annBzFld.refrenceFields());
				type = Cls.getObjectType(type);
				fieldLib = fieldLibs.get(type.getSimpleName());
			} else {
				type = Cls.getObjectType(type);
				fieldLib = fieldLibs.get(type.getSimpleName());
			}

			// reference field
			int dot = fldProp.indexOf(".");
			if (dot > 0) {
				String parentProp = fldProp.substring(0, dot);
				AbstractSystemData parentField = (AbstractSystemData) orm().load(AbstractSystemData.class,
						CndExpr.eq(F_PROP_NAME, parentProp).and(CndExpr.eq(F_SYSTEM, bzSystem)));

				SFTSystem refSystem = parentField.getRefrenceSystem();
				String refProp = fldProp.substring(dot + 1);
				newBzField.setRefrenceSystem(refSystem);
				newBzField.setRefrenceData((AbstractSystemData) orm().load(AbstractSystemData.class,
						CndExpr.eq(F_PROP_NAME, refProp).and(CndExpr.eq(F_SYSTEM, refSystem))));
			}

			newBzField.setType((SystemDataType) fieldLib);
			newBzField.setSoftID(soft);

			return newBzField;
		} catch (Throwable e) {
			log.error("解析字段出错", e);
			throw new DemsyException("解析字段出错! [%s.%s] %s", entityObj.getType().getSimpleName(), fldProp, e.getMessage());
		}

	}

	private void copyProperties(BzFld ann, AbstractSystemData fld) {
		fld.setName(ann.name());
		fld.setDesc(ann.desc());
		fld.setGridField(ann.gridField());
		fld.setPassword(ann.password());
		fld.setMode(ann.mode());
		fld.setUiTemplate(ann.uiTemplate());
		fld.setOptions(ann.options());
		fld.setPattern(ann.pattern());
		fld.setRegexpMask(ann.regexpMask());
		fld.setMappingToMaster(ann.masterMapping());
		fld.setDisabled(ann.disabled());
		fld.setDisabledNavi(ann.disabledNavi());
		fld.setCascadeMode(ann.cascadeMode());
		fld.setGroupBy(ann.groupBy());
		fld.setFileType(ann.uploadType());
		fld.setDefaultValue(ann.defalutValue());
		if (ann.order() > 0)
			fld.setOrderby(ann.order());
		if (ann.gridOrder() > 0)
			fld.setGridOrder(ann.gridOrder());
		fld.setTransientField(ann.isTransient());
		if (ann.precision() > 0)
			fld.setPrecision(ann.precision());
		fld.setPrivacy(ann.privacy() ? "1" : "0");
	}

	private void makeFields(IRuntimeConfigable runtimeCustom, List<AbstractSystemData> fields) {
		if (runtimeCustom == null)
			return;

		makeFields(runtimeCustom.getParent(), fields);

		if (runtimeCustom.getCustomFields() == null)
			return;

		List<AbstractSystemData> list = (List<AbstractSystemData>) runtimeCustom.getCustomFields().getList();

		if (list != null) {
			Map<String, AbstractSystemData> map = new HashMap();
			for (AbstractSystemData fld : fields) {
				String prop = fld.getPropName();
				if (map.get(prop) == null)
					map.put(prop, fld);
			}
			for (AbstractSystemData fld : list) {
				String prop = fld.getPropName();
				if (map.get(prop) == null) {
					map.put(prop, fld);
					fields.add(fld);
				}
			}
		}
	}

	public List<? extends IBizField> makeFields(IRuntimeConfigable runtimeCustom) {
		List<AbstractSystemData> fields = new LinkedList();

		Map<String, IBizFieldType> types = this.getFieldTypes();
		try {
			this.makeFields(runtimeCustom, fields);
		} catch (Throwable e) {
			log.errorf("创建运行时自定义字段出错！", e);
		}

		for (AbstractSystemData f : fields) {
			if (f.getType() == null) {
				f.setType(types.get("String"));
			}
		}

		return fields;
	}
}
