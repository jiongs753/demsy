package com.kmetop.demsy.mvc.ui.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ui.IUIView;
import com.kmetop.demsy.mvc.ui.widget.UIWidget;

/**
 * 界面窗体：用来描述以什么形式展现数据，包括两部分（数据和模型）。
 * <UL>
 * <LI>数据模型由两部分组成：其一为待展现的数据；其二为展现模型；
 * <LI>数据对象类型请参见相应的UI模型所支持的数据类型；
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class UIWidgetModel<MODEL extends UIWidget, DATA> implements IUIView, MvcConst {

	private final MODEL model;// 展现模型

	private DATA data;// 业务数据

	protected Serializable dacorator;// 装饰器：即页面模板ID，表示该数据模型将用来填充指定的页面模板占位符

	/**
	 * 是否只请求AJAX数据？
	 * <p>
	 * true——表示当前客户端请求的只是窗体数据，而不含窗体模型；
	 * <p>
	 * false——表示当前客户端请求的不只是窗体数据，还包括或者只包括窗体模型；
	 * 
	 * @return
	 */
	protected boolean ajaxData;

	private Serializable id;

	private String uiid;

	private String name;

	private String template;

	private String expression;

	private Map<String, Object> context;

	private String dataType;//

	private String dataUrl;

	private String templateType = TPL_ST;

	private static long uiidLong = 1l;

	private static synchronized long nextUiid() {
		return uiidLong++;
	}

	public UIWidgetModel(MODEL model, DATA data) {
		this.model = model;
		this.data = data;

		if (model != null) {
			this.name = model.getName();
		}

		if (Str.isEmpty(this.name))
			this.name = model == null ? "" : model.getClass().getSimpleName();

		context = new HashMap();
	}

	public MODEL getModel() {
		return model;
	}

	public DATA getData() {
		return data;
	}

	public UIWidgetModel<MODEL, DATA> setData(DATA data) {
		if (data instanceof Map) {
			this.context.putAll((Map) data);
		} else
			this.data = data;

		return this;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
		this.uiid = getClass().getSimpleName().toLowerCase() + id + "_" + Long.toHexString(nextUiid());
	}

	public String getUiid() {
		return uiid;
	}

	public void setUiid(String uiid) {
		this.uiid = uiid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void set(String key, Object value) {
		context.put(key, value);
	}

	public Object get(String key) {
		return context.get(key);
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public UIWidgetModel<MODEL, DATA> setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;

		return this;
	}

	public String getTemplateType() {
		return templateType;
	}

	public UIWidgetModel<MODEL, DATA> setTemplateType(String templateType) {
		this.templateType = templateType;

		return this;
	}

	public Serializable getDacorator() {
		return dacorator;
	}

	public UIWidgetModel<MODEL, DATA> setDacorator(Serializable pageID) {
		this.dacorator = pageID;

		return this;
	}

	public boolean isAjaxData() {
		return ajaxData;
	}

	public UIWidgetModel<MODEL, DATA> setAjaxData(boolean ajaxData) {
		this.ajaxData = ajaxData;

		return this;
	}

	@Override
	public String toString() {
		if (id == null)
			return Cls.getType(getClass()).getSimpleName() + "@" + Integer.toHexString(hashCode());
		else
			return Cls.getType(getClass()).getSimpleName() + "#" + id;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
