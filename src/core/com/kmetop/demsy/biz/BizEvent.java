package com.kmetop.demsy.biz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;

/**
 * 业务事件
 * 
 * @author yongshan.ji
 */
public class BizEvent<T> {
	private Map param = new HashMap();

	// 方法参数
	private T entity;

	private Class klass;

	private Serializable id;

	private CndExpr expr;

	// 操作返回值
	private T returnValue;
	
	private IOrm orm;

	/**
	 * 获取事件实体对象，事件实体可以是单个数据实体、实体集合、查询分页等。
	 * 
	 * @return
	 */
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Map getParam() {
		return param;
	}

	public void addParam(String key, Object value) {
		this.param.put(key, value);
	}

	public Class getKlass() {
		return klass;
	}

	public void setKlass(Class klass) {
		this.klass = klass;
	}

	public CndExpr getExpr() {
		return expr;
	}

	public void setExpr(CndExpr expr) {
		this.expr = expr;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public <X> X getReturnValue() {
		return (X) returnValue;
	}

	public void setReturnValue(T resultEntity) {
		this.returnValue = resultEntity;
	}

	public IOrm getOrm() {
		return orm;
	}

	public void setOrm(IOrm orm) {
		this.orm = orm;
	}

}
