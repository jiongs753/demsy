package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.Demsy.security;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_FIELD;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_FIELD;
import static com.kmetop.demsy.comlib.LibConst.F_BUILDIN;
import static com.kmetop.demsy.comlib.LibConst.F_CODE;
import static com.kmetop.demsy.comlib.LibConst.F_DISABLED;
import static com.kmetop.demsy.comlib.LibConst.F_GRID_ORDER;
import static com.kmetop.demsy.comlib.LibConst.F_GUID;
import static com.kmetop.demsy.comlib.LibConst.F_ID;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;
import static com.kmetop.demsy.comlib.LibConst.F_SOFT_ID;
import static com.kmetop.demsy.comlib.LibConst.F_UPDATED;
import static com.kmetop.demsy.comlib.LibConst.F_VERSION;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.nutz.castor.Castors;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Files;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.resource.Scans;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.IBizEngine;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizFieldGroup;
import com.kmetop.demsy.comlib.biz.IBizFieldType;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.IExtField;
import com.kmetop.demsy.comlib.entity.IBizEntity;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Img;
import com.kmetop.demsy.lang.JSON;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Nodes.Node;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Option;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.expr.ExprRule;
import com.kmetop.demsy.security.ILogin;
import com.kmetop.demsy.util.sort.SortUtils;

public abstract class BizEngine implements IBizEngine {
	protected static Log log = Logs.getLog(BizEngine.class);

	protected Map<Long, Class> bizSystemTypes;

	protected Map<String, Class> staticSystemTypes;

	protected BizCompiler compiler;

	private String[] pkgs;

	protected Map<Long, CacheBiz> bizCache;

	protected Map<String, IBizFieldType> fieldLibCache;

	public BizEngine() {
		bizSystemTypes = new HashMap();
		staticSystemTypes = new HashMap();
		bizCache = new HashMap();
	}

	public void clearCache() {
		synchronized (BizEngine.class) {
			bizSystemTypes.clear();
			staticSystemTypes.clear();
			bizCache.clear();

			fieldLibCache = null;
		}
	}

	protected IOrm orm() {
		return Demsy.orm();
	}

	public Class getType(String sysCode) {
		return this.getType(this.getSystem(sysCode));
	}

	protected Map<String, IBizFieldType> getFieldTypes(boolean nocache) {
		if (fieldLibCache == null || nocache) {
			fieldLibCache = new HashMap();
			List<IBizFieldType> list = orm().query(this.getStaticType(BIZSYS_DEMSY_LIB_FIELD), Expr.eq(F_VERSION, 2));
			for (IBizFieldType type : list) {
				fieldLibCache.put(type.getCode(), type);
			}
		}

		return fieldLibCache;
	}

	public Map<Long, IBizFieldType> getFieldTypesById() {
		Map<String, IBizFieldType> types = getFieldTypes(false);
		Map<Long, IBizFieldType> ret = new HashMap();
		Iterator<IBizFieldType> it = types.values().iterator();
		while (it.hasNext()) {
			IBizFieldType t = it.next();
			ret.put(t.getId(), t);
		}

		return ret;
	}

	public Map<String, IBizFieldType> getFieldTypes() {
		return this.getFieldTypes(false);
	}

	@Override
	public List<Class<?>> listTypes() {
		List<Class<?>> ret = new LinkedList();
		for (String pkg : pkgs) {
			ret.addAll(Scans.me().scanPackage(pkg));
		}
		return ret;
	}

	public void validateSystems(IDemsySoft soft) throws DemsyException {
		List<? extends IBizSystem> systems = getSystems(soft);
		StringBuffer sb = new StringBuffer();
		for (IBizSystem sys : systems) {
			try {
				this.getType(sys);
			} catch (Throwable e) {
				log.errorf("校验业务系统失败<%s>! 错误详情: \n%s", sys.getName(), Ex.msg(e));
				sb.append(sys.getName()).append("\n");
			}
		}
		if (sb.length() > 0) {
			throw new DemsyException(sb.toString());
		}
	}

	private void initStaticSystems() {
		List<Class<?>> types = listTypes();
		for (Class type : types) {
			if (type.getName().startsWith(this.getPackageOfAutoSystem(null))) {
				continue;
			}
			try {
				BzSys ann = (BzSys) type.getAnnotation(BzSys.class);
				if (ann != null) {
					if (Str.isEmpty(ann.code())) {
						staticSystemTypes.put("_" + type.getSimpleName(), type);
					} else {
						staticSystemTypes.put(ann.code(), type);
					}
				}
			} catch (Throwable e) {
				log.errorf("初始化静态业务系统失败! %s", e);
			}
		}
	}

	@Override
	public List<IBizSystem> setupSystemFromPackage(IDemsySoft soft) throws DemsyException {
		if (staticSystemTypes.size() == 0) {
			initStaticSystems();
		}
		List<IBizSystem> ret = new LinkedList();

		Iterator<Class> types = staticSystemTypes.values().iterator();
		while (types.hasNext()) {
			IBizSystem sys = setupSystemFromClass(soft, types.next());
			if (sys != null) {
				ret.add(sys);
			}
		}

		return ret;
	}

	@Override
	public List setupFromPackage(IDemsySoft soft) {
		List ret = new LinkedList();

		Iterator<Class> types = staticSystemTypes.values().iterator();
		while (types.hasNext()) {
			List list = setupFromJson(soft, types.next());
			if (list != null)
				ret.addAll(list);
		}

		return ret;
	}

	@Override
	public Class getStaticType(String system) {
		if (staticSystemTypes.size() == 0) {
			initStaticSystems();
		}
		return this.staticSystemTypes.get(system);
	}

	@Override
	public IBizSystem setupSystemFromDB(IDemsySoft soft, String tableName) {
		// TODO:
		return null;
	}

	/**
	 * 获取业务实体 package
	 * 
	 * @return 包名称
	 */
	public abstract String getPackageOfAutoSystem(IBizSystem system);

	/**
	 * 获取业务实体扩展类
	 * 
	 * @param system
	 *            业务系统
	 * @return 扩展类全称
	 */
	public abstract String getExtendSimpleClassName(IBizSystem system);

	/**
	 * 获取业务实体类简称
	 * 
	 * @param system
	 *            业务系统
	 * @return 类简称
	 */
	public abstract String getSimpleClassName(IBizSystem system);

	/**
	 * 获取一对多或多对多的目标属性名
	 * 
	 * @param field
	 *            业务字段
	 * @return
	 */
	public abstract String getTargetPropName(IBizField field);

	public abstract IBizField parseBizField(Long soft, Mirror entityObj, String prop, IBizSystem system, IBizField field);

	@Override
	public int getPrecision(IBizField fld) {
		Integer p = fld.getPrecision();
		if (p == null || p == 0) {
			if (isString(fld))
				return LibConst.DEFAULT_PRECISION;
			else
				return 0;
		}

		return p;
	}

	public boolean isSystemFK(IBizField fld) {
		if (fld.getRefrenceSystem() != null && !this.isSubSystem(fld)) {
			return this.getPropName(fld) != null && fld.getRefrenceField() == null;
		}

		return false;
	}

	public boolean isFieldRef(IBizField fld) {
		if (fld.getRefrenceField() != null)
			return true;

		return false;
	}

	@Override
	public boolean isEnabled(IBizField fld) {
		IBizFieldGroup group = fld.getFieldGroup();
		if (group == null) {
			return false;
		}
		if (group.isDisabled())
			return false;
		if (group.getMode() != null && group.getMode().contains("*:N"))
			return false;

		if (fld.isDisabled())
			return false;
		else if (fld.getRefrenceSystem() != null && fld.getRefrenceSystem().isDisabled())
			return false;
		else if (Str.isEmpty(getClassName(fld)))
			return false;

		return true;
	}

	@Override
	public List<? extends IBizField> getFieldsOfEnabled(IBizSystem system) {
		List<? extends IBizField> list = this.getFields(system);

		List<IBizField> ret = new LinkedList();
		for (IBizField f : list) {
			if (this.isEnabled(f))
				ret.add(f);
		}

		return ret;
	}

	@Override
	public List<? extends IBizField> getFieldsOfEnabled(IBizFieldGroup group) {
		List<? extends IBizField> list = this.getFields(group);

		List<IBizField> ret = new LinkedList();
		for (IBizField f : list) {
			if (this.isEnabled(f))
				ret.add(f);
		}

		return ret;
	}

	@Override
	public IBizField getFieldOfUnSelfTree(IBizSystem system) {
		List<? extends IBizField> refFields = biz(system.getId()).fields();
		for (IBizField fld : refFields) {
			if (fld.isGroupBy()) {
				return fld;
			}
		}

		return null;
	}

	@Override
	public IBizField getFieldOfSelfTree(IBizSystem system) {
		if (system == null) {
			return null;
		}
		List<? extends IBizField> refFields = biz(system.getId()).fields();
		for (IBizField fld : refFields) {
			if (fld.getSystem().equals(fld.getRefrenceSystem()))
				return fld;
		}

		for (IBizField fld : getFieldsOfSystemFK(system)) {
			if (fld.getRefrenceSystem().equals(system))
				return fld;
		}

		return null;
	}

	@Override
	public List<? extends IBizField> getFieldsOfSystemFK(IBizSystem system) {
		List<IBizField> ret = new LinkedList();

		List<? extends IBizField> fields = this.getFieldsOfEnabled(system);
		if (fields != null) {
			for (IBizField field : fields) {
				if (this.isSystemFK(field)) {
					ret.add(field);
				}
			}
		}

		return ret;
	}

	@Override
	public List<? extends IBizField> getFieldsOfSystemFK(IBizSystem system, Class fkType) {
		List<IBizField> ret = new LinkedList();

		List<? extends IBizField> fields = getFieldsOfEnabled(system);
		if (fields != null) {
			for (IBizField field : fields) {
				if (this.isSystemFK(field)) {
					Class fieldType = this.getType(field);
					if (fkType.isAssignableFrom(fieldType) && !getType(system).equals(fieldType))
						ret.add(field);
				}
			}
		}

		return ret;
	}

	protected abstract Option[] geDicOptions(IBizField field);

	public Option[] getOptions(IBizField field) {
		if (!this.isSystemFK(field)) {
			String str = field.getOptions();
			if (!Str.isEmpty(str)) {
				return evalOptions(str);
			} else if (isBoolean(field)) {
				return new Option[] { new Option(Obj.toKey(true), "是"), new Option(Obj.toKey(false), "否") };
			} else if (isV1Dic(field)) {
				return geDicOptions(field);
			}
		}
		return new Option[0];
	}

	protected Option[] evalOptions(String str) {
		str = str.trim();
		if (str.startsWith("[") && str.endsWith("]")) {
			try {
				return Json.fromJson(Option[].class, str);
			} catch (Throwable e) {
				log.errorf("解析字段选项出错：%s", str);
			}
		} else if (str.startsWith("{") && str.endsWith("}")) {
			String key = str.substring(1, str.length() - 1);
			ISoftConfig config = moduleEngine.getSoftConfig(key);
			if (config != null && !Str.isEmpty(config.getValue())) {
				return evalOptions(config.getValue());
			}
		} else {
			String[] strs = Str.toArray(str, ",;，；\r\t\n");
			Option[] options = new Option[strs.length];
			int i = 0;
			for (String item : strs) {
				item = item.trim();
				int idx = item.indexOf(":");
				if (idx < 0) {
					idx = item.indexOf("：");
				}
				if (idx > -1) {
					options[i++] = new Option(item.substring(0, idx).trim(), item.substring(idx + 1).trim());
				} else {
					options[i++] = new Option(item, item);
				}
			}
			return options;
		}

		return new Option[0];
	}

	@Override
	public List<? extends IBizField> getFieldsOfFK(IBizSystem system) {
		List<IBizField> ret = new LinkedList();

		List<? extends IBizField> fields = getFieldsOfEnabled(system);
		if (fields != null) {
			for (IBizField field : fields) {
				if (this.isSystemFK(field) || this.isV1Dic(field))
					ret.add(field);
			}
		}

		return ret;
	}

	@Override
	public List<? extends IBizField> getFieldsOfNavi(IBizSystem system) {
		List<IBizField> ret = new LinkedList();

		List<? extends IBizField> fields = getFieldsOfEnabled(system);
		for (IBizField field : fields) {
			if (field.isDisabledNavi()) {
				continue;
			}
			if (this.isSystemFK(field) || this.isV1Dic(field) || isBoolean(field) || !Str.isEmpty(field.getOptions()))
				ret.add(field);
		}

		return ret;
	}

	@Override
	public int getGridWidth(IBizField field) {
		int w = field.getGridWidth();
		if (w > 0)
			return w;

		if (isNumber(field) || isBoolean(field)) {
			if (this.getOptions(field).length > 0) {
				return 120;
			}
			return 80;
		} else if (isText(field)) {
			return 300;
		} else if (isDate(field)) {
			String p = field.getPattern();
			if (p != null && p.length() > 10)
				return 150;
			else
				return 100;
		} else if (isString(field)) {
			int l = getPrecision(field) + 100;
			if (l > 200)
				return 200;
			else
				return l;
		} else if (isSystemFK(field)) {
			return 200;
		} else if (isUpload(field)) {
			return 200;
		}

		return 100;
	}

	@Override
	public boolean isManyToOne(IBizField field) {
		IBizFieldType type = field.getType();
		return (type.isSystem() || (type.isV1Dic() && !type.isManyToMany()) || type.isV1GEO()) && !field.isSysMultiple();
	}

	@Override
	public boolean isV1Dic(IBizField field) {
		return field.getType().isV1Dic();
	}

	@Override
	public boolean isBoolean(IBizField field) {
		return field.getType().isBoolean();
	}

	@Override
	public boolean isOneToOne(IBizField field) {
		return false;
	}

	@Override
	public boolean isOneToMany(IBizField field) {
		return false;
	}

	@Override
	public boolean isManyToMany(IBizField field) {
		return field.isSysMultiple() || field.getType().isManyToMany();
	}

	@Override
	public boolean isNumber(IBizField field) {
		return field.getType().isNumber();
	}

	@Override
	public boolean isInteger(IBizField field) {
		return field.getType().isNumber();
	}

	@Override
	public boolean isRichText(IBizField field) {
		return field.getType().isRichText();
	}

	@Override
	public boolean isDate(IBizField field) {
		return field.getType().isDate();
	}

	@Override
	public boolean isText(IBizField field) {
		if (field.getType().isString()) {
			return getPrecision(field) > LibConst.INPUT_FIELD_MAX_LENGTH;
		}

		return false;
	}

	@Override
	public boolean isString(IBizField field) {
		return field.getType().isString();
	}

	@Override
	public boolean isUpload(IBizField field) {
		return field.getType().isUpload();
	}

	@Override
	public boolean isImage(IBizField field) {
		boolean upload = isUpload(field);
		if (upload) {
			String[] exts = Str.toArray(field.getUploadType(), ",;|");
			for (String ext : exts) {
				if (Img.isImage(ext)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isMultiUpload(IBizField field) {
		return "MultiUpload".equals(field.getType().getCode());
	}

	@Override
	public boolean isMultiImage(IBizField field) {
		boolean upload = isMultiUpload(field);
		if (upload) {
			String[] exts = Str.toArray(field.getUploadType(), ",;|");
			for (String ext : exts) {
				if (Img.isImage(ext)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isSubSystem(IBizField field) {
		String type = field.getType().getCode();
		return "SubSystem".equals(type) || "FakeSubSystem".equals(type);
	}

	@Override
	public boolean isFakeSubSystem(IBizField field) {
		String type = field.getType().getCode();
		return "FakeSubSystem".equals(type);
	}

	@Override
	public boolean isBuildin(IBizSystem system, String prop) {
		try {
			Class cls = Cls.forName(getExtendClassName(system));
			Mirror me = Mirror.me(cls);
			Field[] fields = me.getFields();
			if (fields != null)
				for (Field f : fields) {
					if (f.getName().toLowerCase().equals(prop == null ? "" : prop.toLowerCase())) {
						return true;
					}
				}
		} catch (Throwable e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean isGridField(IBizField field) {
		return field.isGridField();
	}

	private String parseMode(String actionMode, String mode) {
		if (actionMode == null || actionMode.trim().length() == 0) {
			actionMode = "v";
		}
		if (mode == null || mode.trim().length() == 0) {
			return "";
		}
		mode = mode.trim();
		String[] dataModes = Str.toArray(mode, " ");
		String defaultActionMode = "*";
		String defaultMode = "";
		for (String dataMode : dataModes) {
			if (dataMode == null) {
				continue;
			}
			dataMode = dataMode.trim();
			int index = dataMode.indexOf(":");
			if (index < 0) {
				continue;
			}
			String actMode = dataMode.substring(0, index);
			if (actMode.equals(actionMode)) {
				return dataMode.substring(index + 1);
			}
			if (defaultActionMode.equals(actMode)) {
				defaultMode = dataMode.substring(index + 1);
			}
		}
		return defaultMode;
	}

	public String getUiMode(String mode) {
		if (mode == null)
			return null;

		if (mode.length() > 1)
			return mode.replace("M", "");

		return mode;
	}

	public String getMode(IBizFieldGroup group, IAction action) {
		if (group == null) {
			return "";
		}
		String actionMode = action == null ? "" : action.getMode();

		String mode = group.getMode();
		return parseMode(actionMode, mode);
	}

	@Override
	public String getMode(IBizField field, IAction action, boolean mustPriority, String defalutMode) {
		if (field == null) {
			return "";
		}

		String actionMode = action == null ? "" : action.getMode();
		String groupMode = getMode(field.getFieldGroup(), action);
		String mode = field.getMode();
		String ret = parseMode(actionMode, mode);
		if (ret == null || ret.length() == 0) {
			if (groupMode != null && groupMode.length() > 0) {
				return groupMode;
			}
		}
		if (mustPriority && ret.indexOf("M") > -1) {
			return "M";
		}
		if (!ret.equals("M")) {
			ret = ret.replace("M", "");
		}
		if (ret.length() > 0) {
			return ret;
		}
		// 创建或编辑时：默认可编辑
		if (actionMode.startsWith("c") || actionMode.startsWith("e")) {
			if (Str.isEmpty(defalutMode))
				return "E";
			else
				return defalutMode;
		}
		// 批量修改时：默认不显示
		if (actionMode.startsWith("bu")) {
			if (Str.isEmpty(defalutMode))
				return "N";
			else
				return defalutMode;
		}
		// 浏览数据时：默认检查模式显示
		if (actionMode.startsWith("v")) {
			if (Str.isEmpty(defalutMode))
				return "S";
			else
				return defalutMode;
		}

		// 检查模式显示
		return "S";
	}

	@Override
	public Map<String, String> getMode(IBizSystem system, IAction action, Object data) {
		List<? extends IBizField> fields = this.getFieldsOfEnabled(system);
		Map<String, String> fieldMode = new HashMap();
		for (IBizField field : fields) {
			String mode = this.getMode(field, action, true, null);
			String cascadeMode = this.getCascadeMode(field, data)[1];
			if (getModeValue(mode) < getModeValue(cascadeMode)) {
				mode = cascadeMode;
			}
			fieldMode.put(this.getPropName(field), mode);
		}

		return fieldMode;
	}

	// @Override
	// public List<IBizField> getChildren(IBizField parent) {
	// List<IBizField> ret = new ArrayList();
	// // 哪些字段引用了该系统
	// List<IBizField> fields = orm().query(this.getStaticType(SYS_BIZ_FIELD),
	// CndExpr.eq(F_PARENT, parent).addAsc(F_ORDER_BY));
	// if (fields != null) {
	// for (IBizField field : fields) {
	// if (this.isEnabled(field)) {
	// ret.add(field);
	// }
	// }
	// }
	//
	// return ret;
	// }

	protected abstract boolean isMappingToMaster(IBizField fld);

	@Override
	public List<IBizField> getFieldsOfSlave(IBizSystem system) {
		List<IBizField> ret = new LinkedList();

		List<IBizField> fields = this.getFieldsOfExport(system);
		for (IBizField f : fields) {
			if (!isMappingToMaster(f)) {
				continue;
			}
			IBizSystem sys = f.getSystem();
			if (!f.isDisabled() && !sys.isDisabled()) {
				if (!ret.contains(f) && !system.equals(sys))
					ret.add(f);
			}
		}

		return ret;
	}

	public boolean isSlave(IBizSystem system) {
		List<? extends IBizField> fields = this.getFieldsOfSystemFK(system);
		for (IBizField f : fields) {
			if (isMappingToMaster(f)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<? extends IBizSystem> getSystemsOfSlave(IBizSystem system) {
		List<IBizSystem> ret = new LinkedList();

		List<IBizField> fields = this.getFieldsOfSlave(system);
		for (IBizField f : fields) {
			ret.add(f.getSystem());
		}

		return ret;
	}

	@Override
	public List<IBizField> getFieldsOfExport(IBizSystem system) {
		List<IBizField> ret = new ArrayList();
		// 哪些字段引用了该系统
		List<? extends IBizField> fields = biz(system.getId()).fieldsOfExport();
		if (fields != null) {
			for (IBizField field : fields) {
				if (field.getRefrenceField() != null) {
					continue;
				}
				if (!ret.contains(field)) {
					ret.add(field);
				}
			}
		}

		return ret;
	}

	@Override
	public List<? extends IBizField> getFieldsOfGrid(IBizSystem system, String fields) {
		List<? extends IBizField> list = this.getFieldsOfEnabled(system);
		SortUtils.sort(list, F_GRID_ORDER, true);

		List<IBizField> ret = new LinkedList();
		List<IBizField> other = new LinkedList();

		List<String> names = Str.toList(fields, ",");
		int byName = names == null ? 0 : names.size();
		int byNum = 0;
		if (byName == 1) {
			try {
				byNum = Integer.parseInt(names.get(0));
				byName = 0;
			} catch (Throwable e) {
			}
		}
		int count = 0;
		for (IBizField f : list) {
			count++;
			if (byName > 0) {
				if (names.contains(f.getPropName())) {
					ret.add(f);
				}
			} else {
				if (isGridField(f))
					ret.add(f);
				else
					other.add(f);
			}

			if (byNum > 0 && count >= byNum) {
				break;
			}
		}

		ret.addAll(other);

		return ret;
	}

	@Override
	public List<? extends IBizField> getFields(IBizSystem system) {
		if (system == null)
			return new LinkedList();

		return biz(system.getId()).fields();
	}

	@Override
	public List<? extends IBizField> getFields(IBizFieldGroup group) {
		List<? extends IBizField> list = biz(group.getSystem().getId()).fields();
		List<IBizField> ret = new LinkedList();
		for (IBizField f : list) {
			if (group.equals(f.getFieldGroup())) {
				ret.add(f);
			}
		}
		return ret;
	}

	@Override
	public String getPropName(IBizField f) {
		if (f == null)
			return null;

		String prop = f.getPropName();
		if (Str.isEmpty(prop)) {
			String code = f.getCode();
			prop = code == null ? "" : code.toLowerCase();
		} else if (prop.startsWith(".")) {
			String pp = getPropName(f.getParent());
			if (Str.isEmpty(pp))
				return null;

			return pp + prop;
		}

		return prop;
	}

	public String getTargetClassName(IBizField field) {
		return "List<" + getSimpleClassName(field.getSystem()) + ">";
	}

	/**
	 * 获取业务字段类型
	 * 
	 * @param field
	 *            业务字段
	 * @return 字段类简称
	 */
	public String getClassName(IBizField field) {
		String name = "";

		IBizSystem refSystem = field.getRefrenceSystem();
		if (refSystem != null) {
			if (this.isManyToMany(field))
				name = "List<" + this.getSimpleClassName(refSystem) + ">";
			else
				name = this.getClassName(refSystem);
		} else {
			IBizFieldType fieldLib = field.getType();
			name = fieldLib.getType();
		}

		if (name != null) {
			int idx = name.lastIndexOf(".");
			if (idx > -1) {
				return name.substring(idx + 1);
			}
		}

		return name;
	}

	/**
	 * 包名 com.kmetop.demsy.comlib.impl.sft 将会被自动映射到 com.sft.entity
	 * 以确保实体映射表与遗留系统兼容
	 * 
	 * @param system
	 * @return
	 */
	public String getClassName(IBizSystem system) {
		return getPackageOfAutoSystem(system) + "." + getSimpleClassName(system);
	}

	public Class getGenericType(IBizField field) throws DemsyException {
		Class type = getType(field);
		if (type.equals(List.class)) {
			return getType(field.getRefrenceSystem());
		}
		return type;
	}

	@Override
	public Class getType(IBizField field) throws DemsyException {
		Class type = this.getType(field.getSystem());
		String propname = this.getPropName(field);
		if (propname == null)
			return null;

		try {
			return Cls.getType(type, propname);
		} catch (Throwable e) {
			throw new DemsyException(e);
		}
	}

	@Override
	public Class getType(IBizSystem system) throws DemsyException {
		if (system == null || system.getId() <= 0) {
			return null;
		}
		if (!Str.isEmpty(system.getMappingClass())) {
			Class cls = bizSystemTypes.get(system.getId());
			try {
				if (cls == null) {
					cls = Cls.forName(system.getMappingClass());
					bizSystemTypes.put(system.getId(), cls);
				}
			} catch (Throwable e) {
				throw new DemsyException(e);
			}

			return cls;
		}
		synchronized (bizSystemTypes) {
			String clsname = getClassName(system);
			Class cls = bizSystemTypes.get(system.getId());
			try {
				if (cls == null) {
					cls = Cls.forName(clsname);
					bizSystemTypes.put(system.getId(), cls);
				}

				// 版本验证
				BzSys info = (BzSys) cls.getAnnotation(BzSys.class);
				String newVersion = getVersion(system);
				String oldVersion = info.version();
				if (!newVersion.equals(oldVersion)) {
					log.debugf("业务系统类版本不一致[oldVersion=%s, newVersion=%s]", oldVersion, newVersion);
				} else {
					return cls;
				}
			} catch (ClassNotFoundException noFound) {
			}

			log.infof("编译<%s>业务系统......", system.getName());
			List<String> infos = compiler.compileSystem(system, true);
			StringBuffer sb = new StringBuffer();
			for (String info : infos) {
				sb.append("\n").append(info);
			}
			log.infof("编译<%s>业务系统: 结束. %s", system, sb);

			try {
				orm().removeMapping(cls);

				cls = Cls.reloadClass(clsname);
				bizSystemTypes.put(system.getId(), cls);

				return cls;
			} catch (ClassNotFoundException e) {
				throw new DemsyException(sb.toString());
			}

		}
	}

	public String getVersion(IBizSystem system) {
		String version = system.getVersion();
		if (version == null) {
			long v = system.getUpdated().getTime();
			List<? extends IBizField> fields = this.getFieldsOfEnabled(system);
			for (IBizField f : fields) {
				Date d = f.getUpdated();
				if (d != null && d.getTime() > v) {
					v = d.getTime();
				}
			}
			version = BizCompiler.version + "_" + v;
			system.setVersion(version);
		}

		return version;
	}

	public synchronized List<String> compileSystem(IBizSystem system) throws DemsyException {
		return compiler.compileSystem(system, false);
	}

	public String getTableName(IBizSystem system) {
		return system.getMappingTable();
	}

	public Map<String, IBizField> getFieldsMap(IBizSystem system) {
		return this.getFieldsMap(this.getFieldsOfEnabled(system));
	}

	public Map<String, IBizField> getFieldsMap(List<? extends IBizField> list) {
		Map<String, IBizField> map = new HashMap();
		for (IBizField fld : list) {
			map.put(this.getPropName(fld), fld);
		}
		return map;
	}

	//
	// public <T> T loadEntity(Class<T> classOfEntity, Serializable entityID) {
	// if (entityID instanceof Number) {
	// return (T) orm().load(classOfEntity, ((Number) entityID).longValue());
	// } else if (entityID instanceof String) {
	// return (T) orm().load(classOfEntity, Expr.eq(F_CODE, entityID));
	// }
	//
	// return null;
	// }
	//
	// protected List sortedEntity(Class classOfEntity) {
	// return orm().query(classOfEntity, bizEngine.hasField(classOfEntity,
	// F_ORDER_BY) ? CndExpr.asc(F_ORDER_BY) : null);
	// }
	//
	// protected List sortedEntity(Class classOfEntity, CndExpr expr) {
	// if (bizEngine.hasField(classOfEntity, F_ORDER_BY))
	// expr = expr.addAsc(F_ORDER_BY);
	//
	// return orm().query(classOfEntity, expr);
	// }

	CacheBiz biz(Long systemID) {
		CacheBiz biz = bizCache.get(systemID);
		if (biz == null) {
			biz = new CacheBiz(this, systemID);
		}

		return biz;
	}

	@Override
	public IBizSystem getSystem(Long systemID) {
		return biz(systemID).get();
	}

	// @Override
	// public IBizSystem getSystem(IDemsySoft soft, String systemID) {
	// List<IBizSystem> systems =
	// sortedEntity(bizEngine.getStaticType(SYS_BIZ_SYSTEM), Expr.eq(F_CODE,
	// systemID));
	// if (systems.size() > 0) {
	// long softID = 0;
	// if (soft != null) {
	// softID = soft.getId();
	// }
	// for (IBizSystem sys : systems) {
	// long sid = 0;
	// if (sys.getSoftID() != null) {
	// sid = sys.getSoftID().longValue();
	// }
	// if (softID == sid) {
	// return sys;
	// }
	// }
	//
	// for (IBizSystem sys : systems) {
	// if (sys.getSoftID() == null || sys.getSoftID() == 0) {
	// return sys;
	// }
	// }
	// }
	// return null;
	// }

	@Override
	public List<? extends IBizSystem> getSystems(IDemsySoft soft) {
		return orm().query(bizEngine.getStaticType(BIZSYS_BZUDF_SYSTEM), Expr.eq(F_SOFT_ID, soft.getId()));
	}

	@Override
	public List<? extends IBizFieldGroup> getFieldGroups(IBizSystem system) {
		return biz(system.getId()).groups();
	}

	public int getModeValue(String mode) {
		if (mode != null && mode.length() > 1) {
			mode = mode.replace("M", "");
		}
		if ("E".equals(mode))
			return 1;
		if ("M".equals(mode))
			return 2;
		if ("R".equals(mode))
			return 3;
		if ("D".equals(mode))
			return 4;
		if ("P".equals(mode))
			return 5;
		if ("I".equals(mode))
			return 6;
		if ("S".equals(mode))
			return 7;
		if ("H".equals(mode))
			return 8;
		if ("N".equals(mode))
			return 9;

		return 0;
	}

	public void loadFieldValue(Object obj, IBizSystem system) {
		if (log.isTraceEnabled())
			log.tracef("加载业务数据... [%s] %s", system, JSON.toJson(obj));

		List<? extends IBizField> fks = this.getFieldsOfSystemFK(system);
		for (IBizField fk : fks) {
			try {
				loadFieldValue(obj, fk);
			} catch (DemsyException e) {
				log.errorf("加载字段值出错! [system: %s, field: %s] %s", system.getName(), fk.getName(), e);
			}
		}

		for (IBizField fk : this.getFieldsOfEnabled(system)) {
			try {
				loadFieldDefaultValue(obj, fk);
			} catch (DemsyException e) {
				log.errorf("加载字段值出错! [system: %s, field: %s] %s", system.getName(), fk.getName(), e);
			}
		}

		if (log.isTraceEnabled())
			log.tracef("加载业务结束. [%s] %s", system, JSON.toJson(obj));
	}

	public String[] getCascadeMode(IBizField field, Object obj) {
		String[] strs = Str.toArray(field.getCascadeMode(), " ");
		if (strs == null) {
			return new String[2];
		}

		Map<String, String> excludeModes = new HashMap();
		Map<String, String> includeModes = new HashMap();
		for (String str : strs) {
			String[] array = Str.toArray(str, ":");
			if (array == null || array.length < 3) {
				continue;
			}

			String cascadeField = array[0];
			int dot = cascadeField.indexOf(".");
			if (dot > 0) {
				cascadeField = cascadeField.substring(0, dot);
			}
			String cascadeList = array[1];
			String cascadeMode = array[2];

			// 选项值级联
			if ("*".equals(cascadeList)) {
				includeModes.put(cascadeField, "*");
			} else {// 模式级联
				try {
					List<String> valueList = Str.toList(cascadeList, ",");

					Object parentValue = Obj.getValue(obj, cascadeField);
					IBizField parentField = this.getField(field.getSystem(), cascadeField);
					String code = "";
					if (parentValue == null) {
					} else if (this.isSystemFK(parentField)) {
						String fld = dot > 0 ? array[0].substring(dot + 1) : F_CODE;
						Object v = Obj.getValue(orm().load(this.getType(parentField.getRefrenceSystem()), Expr.eq(F_ID, Obj.getId(parentValue))), fld);
						if (v instanceof Boolean) {
							if ((Boolean) v) {
								code = "1";
							} else {
								code = "0";
							}
						} else {
							code = v == null ? "" : v.toString();
						}
					} else if (this.isBoolean(parentField)) {
						if ((Boolean) parentValue) {
							code = "1";
						} else {
							code = "0";
						}
					} else {
						code = parentValue.toString();
					}

					if (!valueList.contains(code)) {
						excludeModes.put(cascadeField, cascadeMode);
					} else {
						includeModes.put(cascadeField, cascadeMode);
					}
				} catch (Throwable e) {
					log.errorf("加载字段级联模式出错! [%s(%s)] %s", field.getName(), getPropName(field), e);
				}
			}

		}

		StringBuffer prop = new StringBuffer();
		String mode = null;
		Iterator<String> keys = includeModes.keySet().iterator();
		int size = includeModes.size();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = includeModes.get(key);
			if (size > 1 && "*".equals(value)) {
				continue;
			}
			if (value.length() == 0) {
				if (mode == null) {
					mode = "";
				}
			} else {
				if (!Str.isEmpty(mode)) {
					if (this.getModeValue(value) > this.getModeValue(mode)) {
						mode = value;
					}
				} else {
					mode = value;
				}
			}

			prop.append(",").append(key);
		}
		keys = excludeModes.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();

			if (!Str.isEmpty(includeModes.get(key))) {
				continue;
			}

			String value = excludeModes.get(key);
			if (value.indexOf("N") > -1 || value.indexOf("H") > -1) {
				value = "";
			} else {
				value = "N";
			}

			if (!Str.isEmpty(mode)) {
				if (this.getModeValue(value) > this.getModeValue(mode)) {
					mode = value;
				}
			} else {
				mode = value;
			}

			prop.append(",").append(key);
		}

		if (prop.length() > 0) {
			String fld = prop.substring(1);
			log.tracef("字段级联模式：[%s(%s): %s]", field, fld, mode);
			return new String[] { fld, mode };
		} else {
			return new String[2];
		}
	}

	private CndExpr toExpr(List<String> rules) {
		if (rules == null || rules.size() == 0) {
			return null;
		}

		CndExpr expr = null;
		if (rules != null)
			for (String naviRule : rules) {
				if (Str.isEmpty(naviRule)) {
					continue;
				}

				ExprRule rule = new ExprRule(naviRule);
				if (expr == null) {
					expr = rule.toExpr();
				} else {
					expr = expr.and(rule.toExpr());
				}
			}

		return expr;
	}

	private Object getFieldValue(Object obj, IBizField field) {
		return Obj.getValue(obj, getPropName(field));
	}

	private Object loadFieldValue(Object obj, IBizField field) throws DemsyException {
		String fldname = this.getPropName(field);
		Object fldvalue = Obj.getValue(obj, fldname);
		if (fldvalue == null) {
			return null;
		}

		if (Obj.isAgent(fldvalue)) {
			return fldvalue;
		}

		// 加载多对一字段值
		Class fieldType = this.getType(field.getRefrenceSystem());
		if (fldvalue instanceof String) {
			fldvalue = orm().load(fieldType, Expr.eq(F_GUID, fldvalue));
		} else
			fldvalue = orm().load(fieldType, Expr.eq(F_ID, fldvalue));

		if (fldvalue == null) {
			return null;
		}

		// 是否虚拟外键？不是则回写字段值
		if (fieldType.isAssignableFrom(fldvalue.getClass())) {
			Obj.setValue(obj, fldname, fldvalue);
		}

		String[] cascadeStrs = Str.toArray(field.getCascadeMode(), " ");
		if (cascadeStrs == null || cascadeStrs.length == 0) {
			return fldvalue;
		}

		for (String cascadeStr : cascadeStrs) {
			String[] array = Str.toArray(cascadeStr, ":");
			if (array == null || array.length < 3 || !"*".equals(array[1])) {
				continue;
			}

			String prevFld = array[0];
			String refPrevFld = array[2];

			// 检查级联上级，如果级联上级没有值，则自动根据当前字段加载
			int dot = prevFld.indexOf(".");
			if (dot > 0) {
				prevFld = prevFld.substring(0, dot);
			}
			Object prevValue = Obj.getValue(obj, prevFld);
			if (prevValue == null) {
				prevValue = Obj.getValue(fldvalue, refPrevFld);
			}
			if (prevValue != null) {
				Obj.setValue(obj, refPrevFld, prevValue);
				if (!prevValue.equals(Obj.getValue(fldvalue, refPrevFld))) {
					Obj.setValue(obj, fldname, null);
				}
			}
		}

		return fldvalue;
	}

	private Object loadFieldDefaultValue(Object obj, IBizField field) throws DemsyException {
		String fldname = this.getPropName(field);
		Object fldvalue = Obj.getValue(obj, fldname);
		if (fldvalue != null)
			return fldvalue;

		String expr = field.getDefaultValue();
		if (Str.isEmpty(expr))
			return null;

		if (expr.startsWith("{") && expr.endsWith("}")) {
			expr = expr.substring(1, expr.length() - 1);
			fldvalue = Obj.getValue(obj, expr);
		} else {
			try {
				fldvalue = Castors.me().cast(expr, String.class, this.getType(field));
			} catch (Throwable e) {
				log.errorf("加载字段默认值出错! [field: %s, defaultValue: %s]", field, expr);
			}
		}
		if (fldvalue != null) {
			Obj.setValue(obj, fldname, fldvalue);
		}

		return fldvalue;
	}

	public void validate(IBizSystem system, IAction action, Object data, Map<String, String> fieldMode) throws DemsyException {
		if (fieldMode == null) {
			fieldMode = this.getMode(system, action, data);
		}
		Iterator<String> keys = fieldMode.keySet().iterator();
		List<String> props = new LinkedList();
		while (keys.hasNext()) {
			String key = keys.next();
			String mode = fieldMode.get(key);
			if (mode != null && mode.indexOf("M") > -1) {
				Object v = Obj.getValue(data, key);
				if (v instanceof IExtField) {
					v = v.toString();
				}
				if (v == null) {
					props.add(key);
				} else if (v instanceof String) {
					if (Str.isEmpty((String) v))
						props.add(key);
				} else if (Obj.isEntity(v)) {
					if (Obj.isEmpty(null, v))
						props.add(key);
				}
			}
		}

		if (props.size() > 0) {
			Map<String, IBizField> map = this.getFieldsMap(this.getFieldsOfEnabled(system));
			StringBuffer sb = new StringBuffer();
			for (String prop : props) {
				sb.append(",").append(map.get(prop).getName());
			}
			throw new DemsyException("“%s”必需填写!", sb.toString().substring(1));
		}
	}

	private IBizField getField(IBizSystem system, String prop) {
		return this.getFieldsMap(this.getFieldsOfEnabled(system)).get(prop);
	}

	@Override
	public Nodes makeOptionNodes(IBizField field, String mode, Object data, String idField) {
		Nodes root = Nodes.make();

		try {
			if (("E".equals(mode) || "M".equals(mode))) {
				this.makeFieldOptions(root, null, data, field, mode, null, idField);

				if (root.is((byte) 1) && bizEngine.isSystemFK(field)) {
					Object value = getFieldValue(data, field);
					if (value != null) {
						value = loadFieldValue(data, field);
						if (value != null) {
							String key = Obj.toKey(value, idField);
							String name = value.toString();
							if (!Str.isEmpty(key))
								root.addNode(null, key).setName(name).setParams(value);
						}
					}
				}
			} else if (data != null) {
				Object value = getFieldValue(data, field);
				if (value != null) {
					if (bizEngine.isSystemFK(field)) {
						value = loadFieldValue(data, field);
						if (value != null) {
							String key = Obj.toKey(value);
							String name = value.toString();
							if (!Str.isEmpty(key))
								root.addNode(null, key).setName(name).setParams(value);
						}
					} else {
						Option[] options = bizEngine.getOptions(field);
						for (Option o : options) {
							if (Obj.toKey(value).equals(Obj.toKey(o.getValue()))) {
								root.addNode(null, o.getValue()).setName(o.getText()).setParams(o.getValue());
							}
						}
					}
				}
			}
		} catch (DemsyException e) {
			log.errorf("加载字段Options出错! [%s(%s)] %s", field.getName(), getPropName(field), e);
		}

		return root;
	}

	// 解析自身树节点
	private Node mountToSelf(Nodes maker, Object obj, String rootID, String group, String selfTree, String groupTree, String groupParam, String paramPrefix, String prefix, boolean selectable,
			String idField, List selfList) {
		boolean isSelfTree = !Str.isEmpty(selfTree);

		if (isSelfTree) {// 自身树
			Object parentObj = Obj.getValue(obj, selfTree);
			boolean pselect = true;
			if (selfList != null && !selfList.contains(parentObj)) {
				pselect = false;
			}
			if (parentObj != null && !parentObj.equals(obj)) {
				// 将节点挂在父亲的下面
				Serializable parentID = Obj.getId(parentObj);
				makeNode(maker, obj, prefix + parentID, prefix, paramPrefix, pselect, idField);

				// 解析自身树上级
				return mountToSelf(maker, parentObj, rootID, group, selfTree, groupTree, groupParam, paramPrefix, prefix, false, idField, selfList);
			} else {
				return mountToGroup(maker, obj, rootID, group, groupTree, groupParam, paramPrefix, prefix, selectable, idField);
			}
		} else {
			return mountToGroup(maker, obj, rootID, group, groupTree, groupParam, paramPrefix, prefix, selectable, idField);
		}
	}

	// 将节点挂在分组上
	private Node mountToGroup(Nodes maker, Object obj, String rootID, String group, String groupTree, String groupParam, String paramPrefix, String prefix, boolean selectable, String idField) {
		if (!Str.isEmpty(group)) {
			Object groupObj = Obj.getValue(obj, group);
			if (groupObj != null) {
				// 将节点挂在分组的下面
				String groupPrefix = prefix + "_";
				Serializable groupID = Obj.getId(groupObj);
				makeNode(maker, obj, groupPrefix + groupID, prefix, paramPrefix, selectable, idField);

				// 构建分组树
				return mountToSelf(maker, groupObj, rootID, null, groupTree, null, null, groupParam, groupPrefix, !Str.isEmpty(groupParam), idField, null);
			} else {
				return makeNode(maker, obj, rootID, prefix, paramPrefix, selectable, idField);
			}
		} else {
			return this.makeNode(maker, obj, rootID, prefix, paramPrefix, selectable, idField);
		}
	}

	private Node makeNode(Nodes maker, Object obj, String parentNode, String prefix, String paramPrefix, boolean selectable, String idField) {
		Serializable id = Obj.getId(obj);

		Node item = maker.addNode(parentNode, prefix + id).setName(obj.toString());
		if (Str.isEmpty(paramPrefix)) {
			item.setParams(Obj.getId(obj, idField));// 为保证下拉框value为entityGuid
		} else {
			item.setParams(paramPrefix + Obj.getId(obj, idField));
		}
		if (selectable)
			item.set("isSelf", selectable);

		Integer ord = Obj.getValue(obj, LibConst.F_ORDER_BY);
		item.setOrder(ord);

		return item;
	}

	public List<String> makeCascadeExpr(Object obj, IBizField field, String mode) {
		if (obj == null) {
			return null;
		}

		// 字段选项查询条件
		List<String> rules = new LinkedList();

		String optionsRule = field.getOptions();
		if (!Strings.isEmpty(optionsRule)) {
			if (optionsRule.startsWith("[") && optionsRule.endsWith("]")) {
				rules = (List) Json.fromJson(optionsRule);
			} else {
				rules = Str.toList(optionsRule, ",");
			}
		}

		// 计算字段级联表达式
		String[] cascadeStrs = Str.toArray(field.getCascadeMode(), " ");
		if (cascadeStrs != null) {
			for (String cascadeStr : cascadeStrs) {
				String[] array = Str.toArray(cascadeStr, ":");
				if (array == null || array.length < 3) {
					continue;
				}

				if (!"*".equals(array[1])) {
					continue;
				}

				String cascadeField = array[0];
				String cascadeFK = array[2];

				Object parentValue = Obj.getValue(obj, cascadeField);
				if (parentValue == null) {
					rules.add(cascadeFK + " nu");
				} else if (Obj.isEntity(parentValue)) {
					rules.add(cascadeFK + " eq " + Obj.getId(parentValue));
				} else {
					rules.add(cascadeFK + " eq " + parentValue);
				}
			}
		}

		return rules;
	}

	private void makeFieldOptions(Nodes root, Node node, Object obj, IBizField field, String mode, String paramPrefix, String idField) throws DemsyException {
		String parentNode = node == null ? "" : "" + node.getId();
		String nodePrefix = Str.isEmpty(parentNode) ? "_" : parentNode + "_";

		StringBuffer notInList = new StringBuffer();
		ILogin login = Demsy.me().login();
		if (isSystemFK(field)) {
			List<String> rules = this.makeCascadeExpr(obj, field, mode);
			CndExpr expr = this.toExpr(rules);

			// 获取该字段引用的外键系统
			IModule module = moduleEngine.getModule(Demsy.me().getSoft(), field.getSystem());
			IBizSystem refSys = field.getRefrenceSystem();
			String fkField = bizEngine.getPropName(field);
			Class refType = bizEngine.getType(refSys);

			CndExpr fkExpr = security.getPermissionFkExpr(module, fkField);
			if (fkExpr != null) {
				if (expr == null)
					expr = fkExpr;
				else
					expr = expr.and(fkExpr);
			}

			// 获取外键系统业务管理器
			IOrm orm = Demsy.orm();
			// IBizManager bizManager =
			// bizManagerFactory.getManager(moduleEngine.getModule(Demsy.me().getSoft(),
			// refSys));

			String group = null;// 数据分组
			String selfTree = null;// 数据自身树
			String groupTree = null;// 数据非自身树

			// 计算数据分组、自身分组
			IBizField groupByFld = getFieldOfUnSelfTree(refSys);
			if (groupByFld != null) {
				// if (node != null) {
				// root.getChildren().remove(node);
				// }
				// parentNode = null;

				group = getPropName(groupByFld);
				IBizField groupTreeFld = getFieldOfSelfTree(groupByFld.getRefrenceSystem());
				if (groupTreeFld != null) {
					groupTree = getPropName(groupTreeFld);
				}
			}
			IBizField selfTreeFld = getFieldOfSelfTree(refSys);
			if (selfTreeFld != null) {
				selfTree = getPropName(selfTreeFld);
			}

			// 记录数太多将被忽略或采用Combobox方式
			if (orm.count(refType, expr) > 200) {
				if (node == null) {// 创建下拉选项时
					root.set((byte) 1, true);// 设置标志以便使用Combobox
					return;
				} else if (selfTree == null) {// 非自身树不能创建导航树
					return;
				}
			}

			// 计算排序表达式
			Class type = getType(refSys);
			if (Cls.hasField(type, F_ORDER_BY)) {
				expr = expr == null ? CndExpr.asc(F_ORDER_BY) : expr.addAsc(F_ORDER_BY);
			}
			if (Cls.hasField(type, F_DISABLED)) {
				expr = expr == null ? Expr.eq(F_DISABLED, 0).or(Expr.isNull(F_DISABLED)) : expr.and(Expr.eq(F_DISABLED, 0).or(Expr.isNull(F_DISABLED)));
			}

			// 计算字段是否属于真实的外键引用
			try {
				if (Cls.isEntityType(getType(field)) && !Str.isEmpty(paramPrefix)) {
					paramPrefix += Str.isEmpty(idField) ? ".id" : ("." + idField);
				}
			} catch (Throwable igl) {
				log.info(Ex.msg(igl));
			}

			// 计算参数前缀
			if (!Str.isEmpty(paramPrefix)) {
				paramPrefix += " eq ";
			}

			// 查询外键数据
			List<Node> result = new LinkedList();

			List datas = orm.query(refType, expr);
			if (Cls.hasField(type, F_ORDER_BY))
				SortUtils.sort(datas, F_ORDER_BY, true);

			// if (datas.size() == 1 && obj != null) {
			// String propname = field.getPropName();
			// Object v = Mirrors.getValue(obj, field.getPropName());
			// if (v == null) {
			// if (EnObj.isEntityType(getType(field))) {
			// Mirrors.setValue(obj, propname, datas.get(0));
			// } else {
			// Mirrors.setValue(obj, propname, EnObj.getId(datas.get(0)));
			// }
			// }
			// }

			for (Object ele : datas) {
				Node item = this.mountToSelf(root, ele, parentNode, group, selfTree, groupTree, null, paramPrefix, nodePrefix, true, idField, datas);
				if (!result.contains(item)) {
					result.add(item);
				}
				notInList.append(",").append(Obj.getValue(ele, Str.isEmpty(idField) ? F_ID : idField));
			}

			// 如果只有一个分组则移除
			if (result.size() == 1) {
				Node item = result.get(0);
				if (!Boolean.parseBoolean((String) item.getString("isSelf"))) {
					List<Node> list = root.getChildren();
					if (node != null) {
						list = node.getChildren();
					}
					int idx = list.indexOf(item);
					list.remove(item);
					for (Node ele : item.getChildren()) {
						list.add(idx, ele);
						idx++;
					}
				}
			}
		} else {
			Option[] options = this.getOptions(field);
			if (options == null || options.length == 0 || options.length > 50) {
				return;
			}

			if (!Str.isEmpty(paramPrefix)) {
				paramPrefix += " eq ";
			} else {
				paramPrefix = "";
			}

			for (Option op : options) {
				root.addNode(parentNode, parentNode + op.getValue().hashCode()).setName(op.getText()).setParams(paramPrefix + op.getValue());
				notInList.append(",").append(op.getValue());
			}
		}

		if (login != null && login.getRoleType() >= IUserRole.ROLE_ADMIN_ROOT && !Str.isEmpty(paramPrefix) && node != null && node.getSize() > 0) {
			String param = paramPrefix.replace("eq", "ni") + (notInList.length() > 0 ? notInList.substring(1) : "");
			// String param = paramPrefix.replace("eq", "nu");
			root.addNode(parentNode, nodePrefix + "nu").setName("---其他---").setParams(param);
		}
	}

	@Override
	public Nodes makeNaviNodes(IBizSystem bizSystem, String idField) {
		log.debugf("计算业务系统导航菜单数据......[system=%s]", bizSystem);

		Nodes root = Nodes.make();

		List<? extends IBizField> fkFields = this.getFieldsOfNavi(bizSystem);

		for (IBizField fld : fkFields) {

			Node node = root.addNode(null, "fld_" + fld.getId()).setName("按 " + fld.getName());
			String propname = getPropName(fld);

			try {
				this.makeFieldOptions(root, node, null, fld, "E", propname, idField);
			} catch (DemsyException e) {
				log.errorf("加载字段快速查询项出错! [%s(%s)] %s", fld.getName(), getPropName(fld), e);
			}
			if (node.getSize() == 0) {
				root.getChildren().remove(node);
			}
		}

		// 如果导航树节点总数没有超过边框则全部展开
		root.optimizeRoot();

		log.debugf("计算业务系统导航菜单数据: 结束. [system=%s]", bizSystem);

		root.order();

		return root;
	}

	@Override
	public List setupFromJson(IDemsySoft soft, Class klass) {
		if (klass == null) {
			return null;
		}
		BzSys sysann = (BzSys) klass.getAnnotation(BzSys.class);
		if (sysann == null || Str.isEmpty(sysann.jsonData())) {
			return null;
		}

		return setupFromJson(soft, klass, sysann.jsonData());
	}

	@Override
	public <T> List<T> setupFromJson(IDemsySoft soft, Class<T> klass, String json) {
		return setupFromJson(orm(), soft, klass, F_CODE, json);
	}

	private <T> List<T> setupFromJson(IOrm orm, IDemsySoft soft, Class<T> klass, String idFld, String jsonData) {
		List<T> newList = new ArrayList();

		List<T> array = (List<T>) JSON.loadFromJson(klass, jsonData);
		if (array != null) {

			int count = 1;
			for (T ele : array) {
				if (Obj.getValue(ele, F_ORDER_BY) == null) {
					Obj.setValue(ele, F_ORDER_BY, count++);
				}
				Obj.setValue(ele, F_BUILDIN, true);

				List savedlist = saveObj(orm, soft, ele, null, idFld);
				for (Object obj : savedlist) {
					if (klass.isAssignableFrom(obj.getClass()))
						newList.add((T) obj);
				}
			}
		}

		return newList;
	}

	private List saveObj(IOrm orm, IDemsySoft soft, Object newObj, CndExpr expr, String idFld) {
		List ret = new LinkedList();

		Mirror me = Mirror.me(newObj);
		Field[] fields = me.getFields();

		Object idValue = Obj.getValue(newObj, idFld);
		if (idValue == null || Str.isEmpty(idValue.toString())) {
			log.warnf("保存数据失败. [%s.%s = %s] %s", newObj.getClass().getSimpleName(), idFld, idValue, JSON.toJson(newObj));
			return ret;
		}

		if (expr == null) {
			expr = CndExpr.eq(idFld, idValue);
		} else {
			expr = expr.and(expr = CndExpr.eq(idFld, idValue));
		}

		Object obj = orm.load(newObj.getClass(), expr);
		if (obj == null) {
			if (log.isTraceEnabled())
				log.tracef("创建数据记录... [%s] %s", newObj.getClass().getSimpleName(), JSON.toJson(newObj));

			obj = newObj;
		} else {// 拷贝字段值
			if (log.isTraceEnabled())
				log.tracef("编辑数据记录... [%s] %s", newObj.getClass().getSimpleName(), JSON.toJson(obj));

			for (Field fld : fields) {
				String fldname = fld.getName();
				if (fldname.equals("clickNum") || fldname.equals("saleNum") || fldname.equals("commentNum"))
					continue;

				Object fldval = me.getValue(newObj, fldname);

				// load entity
				if (Obj.isEntity(fldval) && Obj.isEmpty(fldval)) {
					String code = Obj.getValue(fldval, idFld);
					fldval = orm.load(fldval.getClass(), Expr.eq(idFld, code).and(Expr.eq(F_SOFT_ID, soft)));
				}

				// 回写
				if (fldval != null && !(fldval instanceof Map) && !(fldval instanceof Collection)) {
					me.setValue(obj, fldname, fldval);
				}
			}
		}
		Obj.setValue(obj, F_SOFT_ID, soft.getId());
		if (!idFld.equals(F_GUID)) {
			Obj.makeSetupGuid(obj, (String) Obj.getValue(newObj, idFld));
		}

		orm.save(obj);

		// 处理列表字段
		for (Field f : fields) {
			String name = f.getName();
			Object v = me.getValue(newObj, name);
			if (v != null && v instanceof List) {
				OneToMany many = f.getAnnotation(OneToMany.class);
				if (many == null || Str.isEmpty(many.mappedBy())) {
					continue;
				}

				String mappedBy = many.mappedBy();
				List list = (List) v;
				int count = 1;
				for (Object ele : list) {
					Obj.setValue(ele, mappedBy, obj);
					if (Obj.getValue(ele, F_ORDER_BY) == null) {
						Obj.setValue(ele, F_ORDER_BY, count++);
					}

					// 自身树外键
					if (Cls.getType(ele.getClass()).equals(Cls.getType(obj.getClass()))) {
						for (Field f1 : fields) {
							ManyToOne one = f1.getAnnotation(ManyToOne.class);
							if (one != null) {
								String name1 = f1.getName();
								if (Obj.getValue(ele, name1) == null && Obj.getValue(obj, name1) != null) {
									Obj.setValue(ele, name1, Obj.getValue(obj, name1));
								}
							}
						}
					}

					ret.addAll(saveObj(orm, soft, ele, null, idFld));
				}
			}
		}

		ret.add(obj);

		return ret;
	}

	public int importFromJson(final IDemsySoft soft, final String folder) {
		Trans.exec(new Atom() {
			public void run() {
				log.debugf("导入JSON软件数据... [soft: %s, folder: %s]", soft, folder);
				if (soft == null) {
					throw new DemsyException("未指定应用软件!");
				}
				IOrm orm = orm();

				File ffolder = new File(folder);

				log.trace("导入无外键数据... ");
				importFromJson(soft, orm, ffolder, false);

				log.trace("导入含外键数据... ");
				int len = importFromJson(soft, orm, ffolder, true);

				log.debugf("导入JSON数据结束. [ret: %s]", len);

			}
		});

		return 0;
	}

	protected int importFromJson(IDemsySoft soft, IOrm orm, File ffolder, boolean includeFK) {
		int len = 0;
		File[] files = ffolder.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				this.importFromJson(soft, orm, file, includeFK);
				continue;
			}

			String className = file.getName();
			if (!className.endsWith(".json")) {
				continue;
			}

			className = className.substring(0, className.length() - 5);
			int idx = className.indexOf("-");
			if (idx > -1) {
				className = className.substring(0, idx);
			}

			if (log.isTraceEnabled())
				log.tracef("转换文件名为类名 [class: %s, file: %s]", className, file.getName());

			InputStream is = null;
			try {

				log.debugf("加载JSON数据文件... [%s]", file.getName());
				is = Files.findFileAsStream(file.getAbsolutePath());
				String json = com.kmetop.demsy.lang.Files.readAll(is, "UTF-8");

				List<String> refflds = new ArrayList();
				if (!includeFK) {
					log.trace("计算外键字段...");
					Class type = Cls.forName(className);
					Field[] fields = Mirror.me(type).getFields();
					for (Field f : fields) {
						Class ftype = f.getType();
						if (Cls.isEntityType(ftype)) {
							refflds.add(f.getName());
						}
					}
				}

				log.tracef("解析JSON数据对象 [class: %s]", className);
				Object[] array = (Object[]) Json.fromJson(Cls.forName(className + "[]"), json);

				log.tracef("解析JSON数据对象 [size: %s]", array.length);
				len += array.length;
				for (Object obj : array) {
					Obj.setId(null, obj, null);
					for (String reffld : refflds) {
						Obj.setValue(obj, reffld, null);
					}

					this.saveObj(orm, soft, obj, null, F_GUID);

					if (log.isTraceEnabled())
						log.tracef("导入数据 [cls: %s, includeFK: %s]\n%s", obj.getClass().getSimpleName(), includeFK, JSON.toJson(obj));
				}

				log.debugf("导入数据结束. [%s]", className);

			} catch (ClassNotFoundException cnfe) {
				log.warnf("导入数据出错! %s", cnfe);
			} catch (Throwable e) {
				throw new DemsyException("导入数据失败!", e);
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException e) {
					}
			}
		}

		return len;
	}

	public int exportToJson(IDemsySoft soft, String folder, CndExpr expr) throws IOException {
		log.debugf("导出JSON数据... [soft: %s, folder: %s]", soft, folder);

		int ret = 0;
		if (soft == null) {
			throw new DemsyException("未指定应用软件!");
		}

		List<? extends IBizSystem> systems = getSystems(soft);
		if (expr == null) {
			expr = Expr.eq(F_SOFT_ID, soft.getId());
		} else {
			expr = expr.and(Expr.eq(F_SOFT_ID, soft.getId()));
		}
		for (IBizSystem sys : systems) {
			Class type = null;
			try {
				type = getType(sys);
				if (Mirror.me(type).getField(F_UPDATED) == null)
					continue;
			} catch (Throwable e) {
				log.warnf("加载业务类出错! [system: %s] %s", sys, e);
				continue;
			}
			if (type == null) {
				log.warnf("业务类不存在! [system: %s]", sys);
				continue;
			}

			int pageIndex = 0;
			while (true) {
				pageIndex++;
				Pager pager = new Pager(type);
				pager.setQueryExpr(expr.setPager(pageIndex, 200));

				List<IBizEntity> datas = null;
				try {
					datas = orm().query(pager);
				} catch (Throwable e) {
					log.warnf("查询数据出错![%s] %s", sys, e);
					break;
				}
				log.tracef("查询业务数据 [system: %s, pageIndex:%s, data.size: %s]", sys, pageIndex, datas == null ? 0 : datas.size());
				if (datas == null || datas.size() == 0) {
					break;
				}
				String filePost = "" + pageIndex;
				if (pageIndex < 10) {
					filePost = "000" + filePost;
				} else if (pageIndex < 100) {
					filePost = "00" + filePost;
				} else if (pageIndex < 1000) {
					filePost = "0" + filePost;
				}

				ret += datas.size();
				Writer writer = null;
				try {
					File file = new File(folder + "/" + type.getName() + "-" + filePost + ".json");
					file.getParentFile().mkdirs();
					file.createNewFile();

					log.tracef("写业务数据到文件... [%s]", file.getName());

					OutputStream os = new FileOutputStream(file);
					writer = new OutputStreamWriter(os, "UTF-8");

					writer.write("[");
					int len = datas.size();
					for (int i = 0; i < len; i++) {
						if (i != 0) {
							writer.write(",");
						}
						new JSON(writer, JsonFormat.nice()).render(datas.get(i));
					}
					writer.write("]");
					writer.flush();
				} finally {
					if (writer != null)
						writer.close();
				}
			}
		}

		log.debugf("导出业务数据结束. [ret: %s]", ret);

		return ret;
	}

	public IBizField getField(Long fieldID) {
		return (IBizField) orm().load(this.getStaticType(BIZSYS_BZUDF_FIELD), fieldID);
	}

	@Override
	public IBizSystem getSystem(String systemCode) {
		return (IBizSystem) orm().load(this.getStaticType(BIZSYS_BZUDF_SYSTEM), Expr.eq(F_CODE, systemCode));
	}

	@Override
	public Class getSystemClass(String sysCode) {
		return this.getType(getSystem(sysCode));
	}
}
