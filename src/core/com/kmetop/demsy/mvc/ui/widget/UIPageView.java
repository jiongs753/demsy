package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.comlib.ui.IStyle;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ui.IUIView;
import com.kmetop.demsy.mvc.ui.model.UIBlockViewModel;

/**
 * 页面UI：用于显示URL地址对应的网页。
 * <UL>
 * <LI>页面UI中的 childUI 不能是 {@link UIPageView}；
 * <LI>与其他UI的区别在于，页面UI会输出HTML头信息； 如：
 * <p>
 * < !DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 * "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" >
 * <p>
 * < html xmlns="http://www.w3.org/1999/xhtml" >
 * <p>
 * < head >
 * <p>
 * < meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 * <p>
 * < title >...< /title >
 * <p>
 * < /head >
 * <p>
 * < body >
 * <p>
 * ......
 * <p>
 * < /body >
 * <p>
 * < /html >
 * 
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class UIPageView implements IUIView, MvcConst {

	private Map<String, Object> context;

	private List<IUIView> topBlocks;

	private List<IUIView> pageBlocks;

	private List<IUIView> bottomBlocks;

	private Map<String, IUIView> blocks;

	private List<IStyle> styles;
	private Map<String, IStyle> styleMap;

	// private DataModel placeholder;

	private Serializable id;

	private String uiid;

	private String name;

	private String template;

	private String expression;

	private String templateType;

	private String width;

	private String height;

	private String cssClass = "";

	private Object param;

	public UIPageView(Map ctx) {
		topBlocks = new LinkedList();
		pageBlocks = new LinkedList();
		bottomBlocks = new LinkedList();
		blocks = new HashMap();
		context = new HashMap();
		if (ctx != null)
			context.putAll(ctx);
		styles = new LinkedList();
		styleMap = new HashMap();
	}

	public void adjust() {
		for (int i = topBlocks.size() - 1; i >= 0; i--) {
			UIBlockViewModel child = (UIBlockViewModel) topBlocks.get(i);
			if (child.getParent() != null) {
				UIBlockViewModel parent = (UIBlockViewModel) blocks.get(child.getParent());
				if (parent != null) {
					parent.addChild(0, child);
				}
				topBlocks.remove(i);
			}
		}
		for (int i = pageBlocks.size() - 1; i >= 0; i--) {
			UIBlockViewModel child = (UIBlockViewModel) pageBlocks.get(i);
			if (child.getParent() != null) {
				UIBlockViewModel parent = (UIBlockViewModel) blocks.get("" + child.getParent());
				if (parent != null) {
					parent.addChild(0, child);
				}
				pageBlocks.remove(i);
			}
		}
		for (int i = bottomBlocks.size() - 1; i >= 0; i--) {
			UIBlockViewModel child = (UIBlockViewModel) bottomBlocks.get(i);
			if (child.getParent() != null) {
				UIBlockViewModel parent = (UIBlockViewModel) blocks.get(child.getParent());
				if (parent != null) {
					parent.addChild(0, child);
				}
				bottomBlocks.remove(i);
			}
		}
	}

	public Map getContext() {
		return context;
	}

	public void set(String key, Object value) {
		context.put(key, value);
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
		this.uiid = getClass().getSimpleName().toLowerCase() + id + "_" + Long.toHexString(new Date().getTime());
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

	public void setTemplate(String tpl) {
		this.template = tpl;
	}

	// public DataModel getPlaceholder() {
	// return placeholder;
	// }

	// public void setPlaceholder(DataModel childUI) {
	// this.placeholder = childUI;
	// this.set("title", childUI.getName());
	// }

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	@Override
	public String getDataType() {
		return null;
	}

	public void addBlock(String area, IUIView block) {
		blocks.put("" + block.getId(), block);
		if ("top".equals(area)) {
			if (!this.topBlocks.contains(block))
				this.topBlocks.add(block);
		} else if ("bottom".equals(area)) {
			if (!this.bottomBlocks.contains(block))
				this.bottomBlocks.add(block);
		} else {
			if (!this.pageBlocks.contains(block))
				this.pageBlocks.add(block);
		}
	}

	public IUIView getBlock(String key) {
		return blocks.get(key);
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public List<IStyle> getStyles() {
		return styles;
	}

	public void addStyle(IStyle style) {
		if (style == null) {
			return;
		}
		String key = style.getCssClass();
		if (styleMap.get(key) == null) {
			styleMap.put(key, style);
			styles.add(style);
		}
	}

	public List<IUIView> getTopBlocks() {
		return topBlocks;
	}

	public List<IUIView> getPageBlocks() {
		return pageBlocks;
	}

	public List<IUIView> getBottomBlocks() {
		return bottomBlocks;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
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
