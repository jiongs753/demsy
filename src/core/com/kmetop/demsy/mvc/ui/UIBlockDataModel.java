package com.kmetop.demsy.mvc.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.comlib.entity.IDynamic;
import com.kmetop.demsy.lang.Str;

/**
 * 页面板块数据：描述页面板块展现的运行时数据模型，包括运行时生成的链接、标题图片等等。
 * 
 * @author jiongs753
 * 
 */
public class UIBlockDataModel implements IDynamic {

	private List<UIBlockDataModel> items;
	// 链接地址
	private String href;

	// 链接目标
	private String target;

	// 固定长度标题：直接显示在页面上
	private String name;

	// 数据原始标题：鼠标停留时显示
	private String title;

	// 内容图片
	private String img;

	private String date = "";

	private Object obj;

	private boolean isnew;

	private Map dynaProps = new HashMap();

	public int getSize() {
		if (items != null)
			return items.size();

		return 0;
	}

	public String getHref() {
		return href;
	}

	public String getImg() {
		return img;
	}

	public String getName() {
		return name;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setImg(String image) {
		this.img = image;
	}

	public void setName(String label) {
		this.name = Str.escapeHTML(label);
	}

	public void setName(String label, int length) {
		label = Str.escapeHTML(label);
		if (length == 0) {
			this.name = label;
		} else {
			this.name = Str.substr(label, length);
		}
		this.title = label;
	}

	public String getTitle() {
		return title;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<UIBlockDataModel> getItems() {
		return items;
	}

	public void addItem(UIBlockDataModel item) {
		if (items == null) {
			items = new LinkedList();
		}
		items.add(item);
	}

	public void addItems(List<UIBlockDataModel> item) {
		if (items == null) {
			items = new LinkedList();
		}
		items.addAll(item);
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setItems(List object) {
		this.items = null;
	}

	public void setIsnew(boolean b) {
		this.isnew = b;
	}

	public boolean isIsnew() {
		return isnew;
	}

	public boolean getIsnew() {
		return isnew;
	}

	public void setTitle(String title) {
		this.title = Str.escapeHTML(title);
	}

	@Override
	public Object get(String property) {
		return dynaProps.get(property);
	}

	@Override
	public void set(String key, Object value) {
		dynaProps.put(key, value);
	}

	@Override
	public Map getDynaProp() {
		return dynaProps;
	}

	@Override
	public boolean is(byte index) {
		return false;
	}
}
