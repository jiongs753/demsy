package com.kmetop.demsy.orm.nutz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.nutz.castor.Castors;
import org.nutz.dao.DatabaseMeta;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.EntityMaker;
import org.nutz.dao.entity.EntityName;
import org.nutz.dao.entity.ErrorEntitySyntaxException;
import org.nutz.dao.entity.FieldType;
import org.nutz.dao.entity.Link;
import org.nutz.dao.entity.ValueAdapter;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.next.FieldQuery;
import org.nutz.dao.sql.FieldAdapter;
import org.nutz.lang.Mirror;
import org.nutz.lang.segment.CharSegment;

import com.kmetop.demsy.comlib.biz.field.IExtField;
import com.kmetop.demsy.comlib.biz.field.SubSystem;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.ann.Klass;
import com.kmetop.demsy.orm.ann.Prop;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.generator.impl.TableEntityIdGenerator;
import com.kmetop.demsy.orm.mapping.EnColumnMapping;
import com.kmetop.demsy.orm.mapping.EnMapping;

public class EnMappingMaker implements EntityMaker {
	private static final Log log = Logs.getLog(EnMappingMaker.class);

	private EnMappingHolder holder;

	private INamingStrategy namingStrategy;

	private Map<String, TableEntityIdGenerator> idGenerators;

	public EnMappingMaker() {
		idGenerators = new HashMap();
	}

	public Entity<?> make(DatabaseMeta db, Connection conn, Class<?> type) {
		return null;
	}

	public EnMapping<?> make(EnMapping<?> parent, Class<?> type) {
		String enInfo = Cls.getDisplayName(type);
		log.debugf("解析%s实体......[parentEntity=%s]", enInfo,
				parent == null ? "NULL" : Cls.getDisplayName(parent.getType()));

		// 解析实体表类型
		Class tableClass = this.evalTableNameClass(type);
		if (tableClass == null) {
			log.errorf("解析%s实体: 非法实体! [type=%s]", enInfo, type.getSimpleName());
			return null;
		}

		EnMappingImpl<?> entity = new EnMappingImpl<Object>((EnMappingImpl) parent);

		log.debugf("解析%s实体: 设置实体类镜像", enInfo);
		Mirror<?> mirror = Mirror.me(type);
		entity.setMirror(mirror);
		entity.setNaming(namingStrategy);

		boolean encoding = encode(tableClass);

		EntityName name = evalTableName(namingStrategy, tableClass);
		log.debugf("解析%s实体: 创建实体表名[%s]", enInfo, name);
		entity.setTableName(name);
		entity.setViewName(name);

		// Get relative meta data from DB
		List<FieldQuery> befores;
		List<FieldQuery> afters;
		befores = new ArrayList<FieldQuery>(5);
		afters = new ArrayList<FieldQuery>(5);

		EnColumnMappingImpl dtype = this.evalDtypeField(namingStrategy, mirror, entity);

		log.debugf("解析%s实体: 增加实体DTYPE字段[%s]", enInfo, dtype);
		if (dtype != null) {
			entity.addField(dtype);
			entity.setDtype(dtype);
		}

		HashMap<String, EntityField> pkmap = new HashMap<String, EntityField>();
		PK pk = type.getAnnotation(PK.class);
		if (null != pk) {
			for (String pknm : pk.value())
				pkmap.put(pknm, null);
		}

		Field[] props = mirror.getFields();
		log.debugf("解析%s实体: 解析%s个字段...", enInfo, props.length);
		for (Field f : props) {
			log.debugf("解析%s实体: 解析字段[%s]", enInfo, f.getName());
			try {
				Link link = this.evalLink(namingStrategy, mirror, entity, f);
				if (link != null) {
					entity.addLinks(link);
					if (!link.isOne()) {
						continue;
					}
					EnColumnMappingImpl ef = new EnColumnMappingImpl(holder, entity, link.getReferField());
					if (pkmap.containsKey(ef.getName())) {
						pkmap.put(ef.getName(), ef);
						if (!(ef.isId() || ef.isName()))
							ef.setType(FieldType.PK);
					}

					ef.setFieldAdapter(FieldAdapter.create(ef.getMirror(), ef.isEnumInt()));
					// ef.setValueAdapter(new
					// AsIdEntity(link.getTargetClass()));
					ef.setValueGetter(linkOneValueGetter(ef));
					ef.setLink(link);
					link.set(EnColumnMapping.class.getSimpleName(), ef);

					// 计算外键字段
					Column col = this.columnAnn(f);
					String columnName = null;
					if (col == null || Str.isEmpty(col.name())) {
						String propertyName = propName(f, f.getName());
						Class fieldEntityClass = f.getType();
						String propertyEntityName = klassName(fieldEntityClass, fieldEntityClass.getName());
						Class tableNameClass = evalTableNameClass(fieldEntityClass);
						String propertyTableName = Str.unqualify(klassName(tableNameClass,
								tableNameClass.getSimpleName()));

						String referencedColumnName = "";
						Field targetPk = idField(Mirror.me(link.getTargetClass()));
						ef.setColumnDefinition(targetPk.getType().getSimpleName().toLowerCase());
						Column annCol = columnAnn(targetPk);
						if (encoding) {
							if (null == annCol || Str.isEmpty(annCol.name())) {
								referencedColumnName = namingStrategy.logicalColumnName(null, targetPk.getName());
							} else {
								referencedColumnName = namingStrategy.logicalColumnName(annCol.name(),
										targetPk.getName());
							}

							columnName = namingStrategy.foreignKeyColumnName(propertyName, propertyEntityName,
									propertyTableName, referencedColumnName);
						} else {
							if (null == annCol || Str.isEmpty(annCol.name())) {
								columnName = propertyName;
							} else {
								columnName = annCol.name();
							}
						}

						log.tracef(
								"计算 FK 字段： result=%s, propertyName=%s, propertyEntityName=%s, propertyTableName=%s, referencedColumnName=%s",
								columnName, propertyName, propertyEntityName, propertyTableName, referencedColumnName);
					} else {
						Field targetPk = idField(Mirror.me(link.getTargetClass()));
						ef.setColumnDefinition(targetPk.getType().getSimpleName().toLowerCase());
						columnName = col.name();
					}
					ef.setColumnName(columnName);

					entity.addField(ef);
				} else {
					EntityField ef = evalField(mirror, entity, f, encoding);
					if (null != ef) {
						if (pkmap.containsKey(ef.getName())) {
							pkmap.put(ef.getName(), ef);
							if (!(ef.isId() || ef.isName()))
								ef.setType(FieldType.PK);
						}

						if (null != ef.getBeforeInsert()) {
							befores.add(ef.getBeforeInsert());
						} else if (null != ef.getAfterInsert()) {
							afters.add(ef.getAfterInsert());
						}
						entity.addField(ef);
					}
				}
			} catch (Exception e) {
				log.errorf("解析%s实体: 解析字段%s出错! %s", enInfo, Cls.getDisplayName(f), Ex.msg(e));
			}
		}

		if (pkmap.size() > 0) {
			EntityField[] pks = new EntityField[pkmap.size()];
			for (int i = 0; i < pk.value().length; i++)
				pks[i] = pkmap.get(pk.value()[i]);

			entity.setPkFields(pks);
		}

		log.debugf("解析%s实体: 设置%s个before字段查询和%s个after字段查询", enInfo, befores.size(), afters.size());
		entity.setBefores(befores.toArray(new FieldQuery[befores.size()]));
		entity.setAfters(afters.toArray(new FieldQuery[afters.size()]));
		EnBorning born = new EnBorning(entity);
		log.debugf("解析%s实体: 创建实体Borning[%s]", enInfo, born);
		entity.setBorning(born);

		if (log.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("解析" + enInfo + "实体: 结束. ");
			sb.append("\n" + entity.getTableName());
			sb.append(" [");
			Iterator<EntityField> fields = entity.fields().iterator();
			while (fields.hasNext()) {
				EnColumnMappingImpl f = (EnColumnMappingImpl) fields.next();
				sb.append("\n\t").append(f.getColumnName());
				if (f.getField() != null) {
					sb.append(" <" + f.getName() + " " + f.getField().getType().getSimpleName() + ">");
				} else {
					sb.append(" <" + f.getName() + " " + f.getSqlType() + ">");
				}
				String fk = f.getFkName();
				if (!Str.isEmpty(fk)) {
					sb.append("[");
					sb.append("fk: ").append(fk);
					sb.append("]");
				}
			}
			sb.append("\n]");
			log.info(sb);
		}

		return entity;
	}

	private ErrorEntitySyntaxException error(Entity<?> entity, String fmt, Object... args) {
		return new ErrorEntitySyntaxException(String.format("[%s] : %s", null == entity ? "NULL" : entity.getType()
				.getName(), String.format(fmt, args)));
	}

	private EnColumnMappingImpl evalDtypeField(INamingStrategy ns, Mirror<?> mirror, EnMappingImpl entity) {
		// parse dtype field
		DiscriminatorColumn dtypeColumn = mirror.getType().getAnnotation(DiscriminatorColumn.class);
		EnColumnMappingImpl dtype = null;
		if (dtypeColumn != null) {
			String name = dtypeColumn.name();
			if (Str.isEmpty(name)) {
				name = "DTYPE";
			}
			dtype = new EnColumnMappingImpl(holder, null, null);
			//
			dtype.setEntity(entity);
			String discriminatorValue = Str.unqualify(this.klassName(mirror.getType(), ""));
			dtype.setDefaultValue(new CharSegment(String.valueOf(discriminatorValue.hashCode())));
			dtype.setNotNull(true);
			dtype.setName(name);
			String columnName = ns.propertyToColumnName(name);
			String sqlType = dtypeColumn.discriminatorType().name();
			dtype.setColumnName(columnName);
			dtype.setSqlType(sqlType);

			log.tracef("计算 DTYPE 字段：dtype=%s, encodeName=%s", name, columnName);
			//
		} else {
			EnColumnMappingImpl parentDtype = null;
			if (entity.getParent() != null) {
				parentDtype = entity.getParent().getDtype();
			}
			if (parentDtype != null) {
				dtype = new EnColumnMappingImpl(holder, null, null);
				//
				dtype.setEntity(entity);
				String discriminatorValue = Str.unqualify(this.klassName(mirror.getType(), ""));
				dtype.setDefaultValue(new CharSegment(String.valueOf(discriminatorValue.hashCode())));
				dtype.setNotNull(true);
				String name = parentDtype.getName();
				dtype.setName(name);
				String columnName = parentDtype.getColumnName();
				dtype.setColumnName(columnName);
				dtype.setSqlType(parentDtype.getSqlType());

				log.tracef("计算 parent dtype 字段：dtype=%s, encodeName=%s", name, columnName);
			}
		}
		if (dtype != null) {
			dtype.setValueGetter(dtypeValueGetter(dtype));
			dtype.setValueSetter(dtypeValueSetter(dtype));
			dtype.setFieldAdapter(new FieldAdapter() {
				public void set(PreparedStatement stat, Object obj, int[] is) throws SQLException {
					if (null == obj) {
						for (int i : is)
							stat.setNull(i, Types.INTEGER);
					} else {
						int v;
						if (obj instanceof Number)
							v = ((Number) obj).intValue();
						else
							v = Castors.me().castTo(obj.toString(), int.class);
						for (int i : is)
							stat.setInt(i, v);
					}
				}
			});
			dtype.setValueAdapter(new ValueAdapter() {
				public Object get(ResultSet rs, String colnm) throws SQLException {
					return rs.getInt(colnm);
				}
			});
		}
		return dtype;
	}

	private FieldValueGetter dtypeValueGetter(final EnColumnMappingImpl dtype) {
		return new FieldValueGetter() {
			public Object get(Object obj) {
				return Long.parseLong(dtype.getDefaultValue(obj));
			}
		};
	}

	private FieldValueSetter dtypeValueSetter(final EnColumnMappingImpl dtype) {
		return new FieldValueSetter() {
			public void set(Object obj, Object value) {
			}
		};
	}

	private FieldValueSetter getFieldValueSetter(final EnColumnMappingImpl col) {
		final Mirror me = col.getMirror();
		final Class type = me.getType();
		if (IExtField.class.isAssignableFrom(type)) {
			return new FieldValueSetter() {
				public void set(Object obj, Object value) {
					IExtField fldvalue = null;
					if (SubSystem.class.isAssignableFrom(type)) {
						Mirror entype = col.getEntity().getMirror();
						Class<?>[] stype;
						try {
							stype = Mirror.getGenericTypes(entype.getField(col.getName()));
							if (stype.length > 0)
								fldvalue = (IExtField) me.born(value, stype[0]);
							else
								fldvalue = (IExtField) me.born(value);
						} catch (NoSuchFieldException e) {
							fldvalue = (IExtField) me.born(value);
						}
					} else {
						fldvalue = (IExtField) me.born(value);
					}

					Obj.setValue(obj, col.getName(), fldvalue);
				}
			};
		}
		return null;
	}

	private FieldValueGetter getFieldValueGetter(final EnColumnMappingImpl col) {
		final Mirror me = col.getMirror();
		if (IExtField.class.isAssignableFrom(me.getType())) {
			return new FieldValueGetter() {
				public Object get(Object obj) {
					IExtField fldvalue = Obj.getValue(obj, col.getName());
					if (fldvalue != null) {
						return fldvalue.toString();
					}
					return null;
				}
			};
		}
		return null;
	}

	private FieldValueGetter linkOneValueGetter(final EnColumnMappingImpl field) {
		return new FieldValueGetter() {
			public Object get(Object obj) {
				Object value = Obj.getValue(obj, field.getName());
				if (Obj.isEntity(value)) {
					value = Obj.getId(holder.getEnMapping(value.getClass()), value);
					if (value instanceof Long && ((Long) value) == 0) {
						return null;
					}
				}
				return value;
			}
		};
	}

	private EnColumnMappingImpl evalField(Mirror<?> mirror, EnMappingImpl<?> entity, Field field, boolean encoding) {
		Transient t = field.getAnnotation(Transient.class);
		if (t != null) {
			return null;
		}

		field.setAccessible(true);

		// 创建一个JPA实体字段
		EnColumnMappingImpl ef = new EnColumnMappingImpl(holder, entity, field);
		Column column = columnAnn(field);
		if (encoding) {
			if (null == column || Str.isBlank(column.name())) {
				String name = propName(field, field.getName());
				String columnName = namingStrategy.propertyToColumnName(name);
				ef.setColumnName(columnName);
				log.tracef("计算 PROPERTY 字段：result=%s, propertyName=%s, propAnnValue=%s", columnName, field.getName(),
						name);
			} else {
				String name = column.name();
				String columnName = namingStrategy.columnName(name);
				ef.setColumnName(columnName);
				log.tracef("计算 COLUMN 字段：result=%s, propertyName=%s, columnAnnName=%s", columnName, field.getName(),
						name);
			}
		} else {
			if (null == column || Str.isBlank(column.name())) {
				String name = propName(field, field.getName());
				ef.setColumnName(name);
				log.tracef("计算 PROPERTY 字段：result=%s, propertyName=%s, propAnnValue=%s", name, field.getName(), name);
			} else {
				String name = column.name();
				ef.setColumnName(name);
				log.tracef("计算 COLUMN 字段：result=%s, propertyName=%s, columnAnnName=%s", name, field.getName(), name);
			}
		}
		if (column != null) {
			ef.setReadonly(!column.insertable() && !column.updatable());
			ef.setNotNull(!column.nullable());
			ef.setColumnDefinition(column.columnDefinition());
			ef.setLength(column.length());
			ef.setPrecision(column.precision());
			ef.setScale(column.scale());
		}

		Version version = field.getAnnotation(Version.class);
		if (version != null) {
			ef.setNotNull(true);
		}

		FieldValueGetter getter = this.getFieldValueGetter(ef);
		if (getter != null) {
			ef.setValueGetter(getter);
		}
		FieldValueSetter setter = this.getFieldValueSetter(ef);
		if (setter != null) {
			ef.setValueSetter(setter);
		}

		// @Id
		Id id = idAnn(field);
		if (null != id) {
			if (!ef.getMirror().isIntLike()) {
				throw error(entity, "@Id field [%s] must be a Integer!", field.getName());
			}
			ef.setType(FieldType.ID);
			if (entity.getParent() != null && entity.getParent().getIdGenerator() != null) {
				entity.setIdGenerator(entity.getParent().getIdGenerator());
			} else {
				TableGenerator tg = field.getAnnotation(TableGenerator.class);
				if (tg != null) {
					TableEntityIdGenerator gen = idGenerators.get(tg.table());
					if (gen == null) {
						gen = new TableEntityIdGenerator(tg.table(), tg.pkColumnName(), tg.valueColumnName());
						idGenerators.put(tg.table(), gen);
					}
					entity.setIdGenerator(gen);
				}
			}
		}

		ef.setFieldAdapter(FieldAdapter.create(ef.getMirror(), ef.isEnumInt()));
		ef.setValueAdapter(ValueAdapter.create(ef.getMirror(), ef.isEnumInt()));

		return ef;
	}

	private Link evalLink(INamingStrategy ns, Mirror<?> mirror, Entity<?> entity, Field field) {
		try {
			// 一对一
			OneToOne one = field.getAnnotation(OneToOne.class);
			if (null != one) {
				Class targetClass = one.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = field.getType();
				}
				if (Str.isEmpty(one.mappedBy())) {
					Field referFld = mirror.getField(field.getName());
					Field targetPkFld = idField(Mirror.me(targetClass));
					Link link = Link.getLinkForOne(mirror, field, targetClass, referFld, targetPkFld);
					link.set("fetch", one.fetch());

					return link;
				} else {
					Field targetReferFld = Mirror.me(targetClass).getField(one.mappedBy());
					Field pkFld = idField(mirror);
					String key = null;
					Link link = Link.getLinkForMany(mirror, field, targetClass, targetReferFld, pkFld, key);
					link.set("mappedBy", one.mappedBy());
					link.set("fetch", one.fetch());

					return link;
				}
			}
			// 一对多
			OneToMany many = field.getAnnotation(OneToMany.class);
			if (null != many) {
				Class targetClass = many.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = genericType(field);
				}
				if (Str.isEmpty(many.mappedBy())) {
					throw Ex.throwEx("<%s.%s>@OneToMany mappedBy is mustable.", mirror.getType().getName(),
							field.getName());
				} else {
					Field targetReferFld = Mirror.me(targetClass).getField(many.mappedBy());
					Field pkFld = idField(mirror);
					String key = null;
					Link link = Link.getLinkForMany(mirror, field, targetClass, targetReferFld, pkFld, key);
					link.set("mappedBy", many.mappedBy());
					link.set("fetch", many.fetch());

					return link;
				}
			}
			// 多对一
			ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
			if (null != manyToOne) {
				Class targetClass = manyToOne.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = field.getType();
				}
				Field referFld = mirror.getField(field.getName());
				Field targetPkFld = idField(Mirror.me(targetClass));
				Link link = Link.getLinkForOne(mirror, field, targetClass, referFld, targetPkFld);
				link.set("fetch", manyToOne.fetch());
				if (targetClass.equals(mirror.getType())) {
					link.set("linkEntity", entity);
				} else {
					EnMapping linkEntity = holder.getEnMapping(targetClass);
					if (linkEntity == null) {
						linkEntity = this.make(null, targetClass);
					}
					link.set("linkEntity", linkEntity);
				}

				return link;
			}
			// 多对多
			ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
			if (manyToMany != null) {
				/*
				 * targetXXX 和 selfXXX 用于创建多对多Link对象。 ownerXXX 和 associatedXXX
				 * 用于生成中间表名称
				 */

				// POJO 相关变量——用于生成中间表“字段名”
				Class targetClass = manyToMany.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = genericType(field);
				}
				Mirror targetMirror = Mirror.me(targetClass);
				Field targetField = null;

				// 实体类——用于生成中间表“表名”
				Class ownerClass;
				Class associatedClass;
				Field ownerField = null;

				// 解析目标实体类
				Field[] targetFields = targetMirror.getFields(ManyToMany.class);
				String mappedBy = manyToMany.mappedBy();
				if (Str.isEmpty(mappedBy)) {// 该实体是多对多的主体
					ownerClass = mirror.getType();
					associatedClass = targetClass;
					ownerField = field;
					for (Field f : targetFields) {
						ManyToMany m2m = f.getAnnotation(ManyToMany.class);
						if (field.getName().equals(m2m.mappedBy())) {// 主对象字段名与目标多对多字段映射名相同
							targetField = f;
							break;
						}
					}
				} else {// 目标实体才是多对多的主体
					ownerClass = targetClass;
					associatedClass = mirror.getType();
					for (Field f : targetFields) {
						if (mappedBy.equals(f.getName())) {// 目标实体（多对多的主体）字段为该字段的映射
							ownerField = f;
							targetField = f;
							break;
						}
					}
				}
				if (ownerField == null) {
					throw Ex.throwEx("计算多对多主体字段失败! <%s.%s>", mirror.getType().getName(), field.getName());
				}

				// 计算生成中间表所需参数
				String ownerEntity = klassName(ownerClass, "");
				String associatedEntity = klassName(associatedClass, "");
				String ownerEntityTable = Str.unqualify(klassName(evalTableNameClass(ownerClass), ""));
				String associatedEntityTable = Str.unqualify(klassName(evalTableNameClass(associatedClass), ""));
				String propertyName = propName(ownerField, "");

				// 计算指向 本 POJO 主键的字段名
				String selfPropertyName = propName(targetField, "");
				String selfPropertyEntityName = klassName(mirror.getType(), "");
				String selfPropertyTableName = Str.unqualify(klassName(evalTableNameClass(mirror.getType()), ""));
				String fromReferencedColumnName;
				Field selfPk = idField(mirror);
				Column col = columnAnn(selfPk);
				if (null == col || Str.isBlank(col.name())) {
					fromReferencedColumnName = ns.logicalColumnName(null, propName(selfPk, selfPk.getName()));
				} else {
					fromReferencedColumnName = ns.logicalColumnName(col.name(), propName(selfPk, selfPk.getName()));
				}
				String from = ns.foreignKeyColumnName(selfPropertyName, selfPropertyEntityName, selfPropertyTableName,
						fromReferencedColumnName);

				// 计算指向 目标POJO 主键的字段名
				String targetPropertyName = propName(field, "");
				String targetPropertyEntityName = klassName(targetClass, "");
				String targetPropertyTableName = Str.unqualify(klassName(evalTableNameClass(targetClass), ""));
				String toReferencedColumnName;
				Field targetPk = idField(targetMirror);
				col = columnAnn(targetPk);
				if (null == col || Str.isBlank(col.name())) {
					toReferencedColumnName = ns.logicalColumnName(null, propName(targetPk, targetPk.getName()));
				} else {
					toReferencedColumnName = ns.logicalColumnName(col.name(), propName(targetPk, targetPk.getName()));
				}
				String to = ns.foreignKeyColumnName(targetPropertyName, targetPropertyEntityName,
						targetPropertyTableName, toReferencedColumnName);

				// 计算中间表名称
				String relation = ns.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity,
						associatedEntityTable, propertyName);

				if (log.isTraceEnabled()) {
					log.tracef("计算 MANY2MANY 主实体： [\nownerEntity=%s\nownerEntityTable=%s\nownerClass=%s\n]",
							ownerEntity, ownerEntityTable, ownerClass.getName());
					log.tracef(
							"计算 MANY2MANY 关联实体：[\nassociatedEntity=%s\nassociatedEntityTable=%s\nassociatedClass=%s\n]",
							associatedEntity, associatedEntityTable, associatedClass.getName());
					log.tracef(
							"计算 MANY2MANY 本POJO： [\nfrom=%s\nselfPk=%s\npropertyName=%s\npropertyEntityName=%s\npropertyTableName=%s\nreferencedColumnName=%s\n]",
							from, selfPk.getName(), selfPropertyName, selfPropertyEntityName, selfPropertyTableName,
							fromReferencedColumnName);
					log.tracef(
							"计算 MANY2MANY 目标POJO： [\nto=%s\ntargetPk=%s\npropertyName=%s\npropertyEntityName=%s\npropertyTableName=%s\nreferencedColumnName=%s\n]",
							to, targetPk.getName(), targetPropertyName, targetPropertyEntityName,
							targetPropertyTableName, toReferencedColumnName);
					log.tracef(
							"计算 MANY2MANY 中间表： [\nrelation=%s\nownerEntity=%s\nownerEntityTable=%s\nassociatedEntity=%s\nassociatedEntityTable=%s\npropertyName=%s\n]",
							relation, ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable,
							propertyName);
					log.tracef(
							"计算 MANY2MANY 映射：[\nselfClass=%s\nselfField=%s\ntargetClass=%s\nselfPk=%s\ntargetPk=%s\nrelation=%s\nfrom=%s\nto=%s\n]",
							mirror.getType().getName(), field.getName(), targetClass.getName(), selfPk, targetPk,
							relation, from, to);
				}

				// 创建多对多Link对象
				Link link = Link.getLinkForManyMany(mirror, field, targetClass, selfPk, targetPk, null, relation, from,
						to);
				link.set("fetch", manyToMany.fetch());
				if (!Str.isEmpty(mappedBy)) {
					link.set("mappedBy", mappedBy);
				}

				return link;
			}
		} catch (Exception e) {
			throw Ex.throwEx("Fail to eval linked field '%s' of class[%s] for the reason '%s'", field.getName(), mirror
					.getType().getName(), Ex.msg(e));
		}
		return null;
	}

	private Class genericType(Field field) {
		Class[] types = Mirror.getGenericTypes(field);
		if (types.length > 0) {
			return types[0];
		} else {
			return field.getType();
		}
	}

	private String klassName(Class cls, String defaultValue) {
		if (cls == null) {
			return defaultValue;
		}
		Klass klass = (Klass) cls.getAnnotation(Klass.class);
		if (klass != null && !Str.isEmpty(klass.value())) {
			return klass.value();
		}
		String pkgName = null;
		int idx = cls.getName().lastIndexOf(".");
		if (idx > 0) {
			pkgName = cls.getName().substring(0, idx);
		}
		while (pkgName != null) {
			Package pkg = Package.getPackage(pkgName);
			if (pkg != null) {
				klass = pkg.getAnnotation(Klass.class);
				if (klass != null && !Str.isEmpty(klass.value())) {
					return cls.getName().replace(pkgName, klass.value());
				}
			}

			idx = pkgName.lastIndexOf(".");
			if (idx > 0) {
				pkgName = pkgName.substring(0, idx);
			} else {
				pkgName = null;
			}
		}
		if (Str.isEmpty(defaultValue)) {
			return cls.getName();
		}
		return defaultValue;
	}

	private String propName(Field fld, String defaultValue) {
		if (fld == null) {
			return defaultValue;
		}
		Prop prop = (Prop) fld.getAnnotation(Prop.class);
		if (prop != null && !Str.isEmpty(prop.value())) {
			return prop.value();
		}
		if (Str.isEmpty(defaultValue)) {
			return fld.getName();
		}
		return defaultValue;
	}

	private Annotation ann(Field fld, Class annClas) {
		Annotation ret = fld.getAnnotation(annClas);
		if (ret == null) {
			String name = fld.getName();
			String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			try {
				Method m = fld.getDeclaringClass().getMethod(getter);
				ret = m.getAnnotation(annClas);
			} catch (SecurityException e) {
				return null;
			} catch (NoSuchMethodException e) {
				return null;
			}
		}
		return ret;
	}

	private Column columnAnn(Field fld) {
		return (Column) ann(fld, Column.class);
	}

	private Id idAnn(Field fld) {
		return (Id) ann(fld, Id.class);
	}

	private Field idField(Mirror mirror) throws NoSuchFieldException {
		Field ret = mirror.getField(Id.class);
		if (ret == null) {
			Method m = this.getMethod(mirror, Id.class);
			String name = m.getName();
			if (name.startsWith("get")) {
				name = name.replace("get", "");
				name = name.substring(0, 1).toLowerCase() + name.substring(1);
				ret = mirror.getField(name);
			}
		}
		return ret;
	}

	public <AT extends Annotation> Method getMethod(Mirror mirror, Class<AT> ann) {
		for (Method method : mirror.getMethods()) {
			if (method.isAnnotationPresent(ann))
				return method;
		}
		return null;
	}

	/**
	 * 计算表名从哪个类产生？用于继承关系的实体类。
	 * <p>
	 * 目前只支持继承类型为单表的情况。
	 * <p>
	 * 对于每个类一个表和使用连接表的情况有待实现。
	 */
	private Class<?> evalTableNameClass(Class type) {
		Class<?> me = type;
		// 代理类
		if (Cls.isAgent(me)) {
			me = me.getSuperclass();
		}
		javax.persistence.Entity ann = me.getAnnotation(javax.persistence.Entity.class);
		if (ann != null) {
			Class<?> parent = me.getSuperclass();
			javax.persistence.Entity parentAnn = parent.getAnnotation(javax.persistence.Entity.class);
			if (parentAnn == null) {
				return me;
			} else {
				return evalTableNameClass(parent);
			}
		} else {
			return null;
		}
	}

	private boolean encode(Class<?> type) {
		javax.persistence.Table table = type.getAnnotation(javax.persistence.Table.class);
		if (table != null && !Str.isBlank(table.name())) {
			return false;
		}

		return true;
	}

	// 计算实体表名称
	private EntityName evalTableName(INamingStrategy ns, Class<?> type) {
		javax.persistence.Table table = type.getAnnotation(javax.persistence.Table.class);
		if (table != null && !Str.isBlank(table.name())) {
			String tableName = ns.tableName(table.name());
			log.tracef("计算 ENTITY 表：result=%s, entityClass=%s, tableAnnName=%s", tableName, type.getName(),
					table.name());

			return EntityName.create(tableName);
		}

		String entityName = klassName(type, type.getName());
		String tableName = ns.classToTableName(Str.unqualify(entityName));
		log.tracef("计算 ENTITY 实体表：result=%s, typeName=%s, entityName=%s", tableName, type.getName(), entityName);

		return EntityName.create(tableName);
	}

	public void setHolder(EnMappingHolder holder) {
		this.holder = holder;
	}

	public void setNamingStrategy(INamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	public INamingStrategy getNamingStrategy() {
		return namingStrategy;
	}

}
