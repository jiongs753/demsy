package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;
import com.kmetop.demsy.mvc.ui.widget.field.UIGroupFld;
import com.kmetop.demsy.mvc.ui.widget.menu.UIButtonMenu;

/**
 * 业务表单：是一个业务窗体，用来表示业务窗体为一个表单。
 * <UL>
 * <LI>业务窗体更多特性请参见{@link UIBizModel}
 * </UL>
 * 
 * @author yongshan.ji
 * 
 * @param <T>
 */
public class UIBizForm extends UIBizModel {
	// 表单特征
	private boolean hasDate;

	private boolean hasUpload;

	private boolean hasRichText;

	private boolean hasMultiSelect;

	private boolean hasColor;

	private boolean hasCombobox;
	
	private byte layout;

	protected final List<UIGroupFld> groups = new LinkedList();

	protected UIWidgetModel<UIButtonMenu, IAction> buttonMenu;// 嵌入式表单按钮

	public UIBizForm(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public UIWidgetModel<UIButtonMenu, IAction> getButtonMenu() {
		return buttonMenu;
	}

	public void setButtonMenu(UIWidgetModel<UIButtonMenu, IAction> buttonMenu) {
		this.buttonMenu = buttonMenu;
	}

	public List<UIGroupFld> getGroups() {
		return groups;
	}

	public UIBizForm addGroup(UIGroupFld group) {
		groups.add(group);
		return this;
	}

	public UIBizForm removeGroup(UIGroupFld group) {
		groups.remove(group);
		return this;
	}

	public int getSize() {
		return groups.size();
	}

	public boolean getHasDate() {
		return hasDate;
	}

	public boolean getHasUpload() {
		return hasUpload;
	}

	public void setHasDate(boolean hasDate) {
		this.hasDate = hasDate;
	}

	public void setHasUpload(boolean hasUpload) {
		this.hasUpload = hasUpload;
	}

	public boolean getHasRichText() {
		return hasRichText;
	}

	public void setHasRichText(boolean hasRichText) {
		this.hasRichText = hasRichText;
	}

	public boolean getHasMultiSelect() {
		return hasMultiSelect;
	}

	public void setHasMultiSelect(boolean hasMultiSelect) {
		this.hasMultiSelect = hasMultiSelect;
	}

	public void setHasColor(boolean b) {
		hasColor = b;
	}

	public boolean getHasColor() {
		return hasColor;
	}

	public boolean getHasCombobox() {
		return hasCombobox;
	}

	public void setHasCombobox(boolean hasCombobox) {
		this.hasCombobox = hasCombobox;
	}

	public int getFieldsLength() {
		int ret = 0;
		for (UIGroupFld g : groups) {
			ret += g.getShownFieldsSize();
		}

		return ret;
	}

	public byte getLayout() {
		return layout;
	}

	public void setLayout(byte layout) {
		this.layout = layout;
	}
}
