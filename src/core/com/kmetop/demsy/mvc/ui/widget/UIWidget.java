package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ui.IUIView;

/**
 * UI窗体：用来描述数据的展现方式
 * <UL>
 * <LI>UI窗体不含模型数据
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public abstract class UIWidget implements IUIView, MvcConst {

	private Serializable id;

	private String uiid;

	private String name;

	private String template;

	private String expression;

	protected Map<String, Object> context;

	private String dataType;//

	private String dataUrl;

	private String templateType = TPL_ST;

	private static long uiidLong = 1l;

	private Object param;

	private String cssClass = "";

	private String cssStyle;

	private String style;

	protected boolean asynLoad;

	private int width;

	private int height;

	protected UIWidget() {
		context = new HashMap();
	}

	public UIWidget(Map ctx) {
		this();

		if (ctx != null)
			context.putAll(ctx);
	}

	public UIWidget(Map ctx, Serializable id) {
		this();

		if (ctx != null)
			context.putAll(ctx);

		this.setId(id);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isAsynLoad() {
		return asynLoad;
	}

	public void setAsynLoad(boolean asynLoad) {
		this.asynLoad = asynLoad;
	}

	public String getCssClass() {
		return cssClass;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	private static synchronized long nextUiid() {
		return uiidLong++;
	}

	public Map getContext() {
		return context;
	}

	public UIWidget set(String key, Object value) {
		context.put(key, value);
		return this;
	}

	public Object get(String key) {
		return context.get(key);
	}

	public String getUiid() {
		return this.uiid;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
		this.uiid = getClass().getSimpleName().toLowerCase() + id + "_" + Long.toHexString(nextUiid());
	}

	public String getName() {
		return name;
	}

	public String getName2() {
		return name + 2;
	}

	public UIWidget setName(String name) {
		this.name = name;
		return this;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String tpl) {
		this.template = tpl;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
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
