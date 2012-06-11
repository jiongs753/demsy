package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.ui.widget.UIWidget;

/**
 * 业务字段UI：用于“编辑”字段值，被嵌入到业务窗口中。
 * 
 * <p>
 * <B>表单字段显示模式：</B>用空格分隔，与子系统数据操作中指定的动作模式组合使用。
 * <UL>
 * <LI>M: Must 必需的
 * <LI>E: Edit 可编辑的 (即可读写)
 * <LI>I: Inspect 检查（带有一个隐藏字段存放其值）
 * <LI>S: Show 显示（但不带隐藏字段）
 * <LI>N: None 不显示
 * <LI>P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段
 * <LI>H: Hidden 隐藏 (不显示，但有一个隐藏框存在)
 * <LI>R: Read only 只读
 * <LI>D: Disable 禁用
 * <LI>举例说明：
 * <UL>
 * <LI>v:I——查看数据时，该字段处于检查模式
 * <LI>e:E——编辑数据时，字段可编辑
 * <LI>bu:N——批量修改数据时，字段不可见
 * <LI>v:I e:E bu:U ——综合描述同上
 * </UL>
 * </UL>
 * 
 * @author yongshan.ji
 * 
 * @param <T>
 */
public abstract class UIBizFld extends UIWidget {

	protected String mode;// 字段显示模式

	protected Align align = Align.LEFT;// 对齐方式

	protected String pattern;

	protected String label = "";

	protected String propName = "";

	protected List<UIBizFld> children = new LinkedList();

	protected List<UIBizFld> shownFields = new LinkedList();

	protected List<UIBizFld> hiddenFields = new LinkedList();

	protected int fieldCount = 0;

	protected int rowSize = 1;

	protected boolean rowBegin = true;

	protected boolean rowEnd = true;

	protected int colSpan = 1;

	private Nodes optionNode;

	private String comboboxUrl;

	private String keyProp = "id";

	public UIBizFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public boolean supportColSpan() {
		return false;
	}

	public int getSize() {
		return children.size();
	}

	public int getShownFieldsSize() {
		return shownFields.size();
	}

	public List<UIBizFld> getShownFields() {
		return shownFields;
	}

	public void setShownFields(List<UIBizFld> shownFields) {
		this.shownFields = shownFields;
	}

	public List<UIBizFld> getHiddenFields() {
		return hiddenFields;
	}

	public void setHiddenFields(List<UIBizFld> hiddenFields) {
		this.hiddenFields = hiddenFields;
	}

	public int getRowSize() {
		return rowSize;
	}

	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}

	public Nodes getOptionNode() {
		return optionNode;
	}

	public UIBizFld setOptionNode(Nodes options) {
		this.optionNode = options;

		return this;
	}

	public List<UIBizFld> getChildren() {
		return children;
	}

	public void addChild(UIBizFld field, Object data) {
		String mode = field.getMode();
		if ("N".equals(mode)) {
			this.children.add(field);
			return;
		} else if ("H".equals(mode)) {
			hiddenFields.add(field);
		} else {
			if ("P".equals(mode)) {
				Object value = Obj.getValue(data, field.getPropName());
				if (value == null || Str.isEmpty(value.toString()))
					return;
			}

			if (field.supportColSpan()) {
				field.rowBegin = true;
				field.rowEnd = true;

				field.colSpan = rowSize * 2 - 1;

				fillBlank();

			} else {
				if (fieldCount % rowSize == 0) {
					field.rowBegin = true;

					if (shownFields.size() > 1) {
						UIBizFld prev = shownFields.get(shownFields.size() - 1);
						prev.rowEnd = true;
					}
					if (fieldCount != 0)
						fieldCount = 0;
				} else {
					field.rowBegin = false;
				}
				field.rowEnd = false;
				fieldCount++;
			}

			shownFields.add(field);
		}

		this.children.add(field);
	}

	public void fillBlank() {
		if (fieldCount % rowSize != 0)
			for (int i = fieldCount; i < rowSize; i++) {
				UIEmptyFld ef = new UIEmptyFld();
				ef.rowBegin = false;
				ef.rowEnd = false;
				shownFields.add(ef);
			}

		if (shownFields.size() > 1) {
			UIBizFld prev = shownFields.get(shownFields.size() - 1);
			prev.rowEnd = true;
		}
		fieldCount = 0;
	}

	public String getPropName() {
		return propName;
	}

	public String getPropName2() {
		return propName + 2;
	}

	public UIBizFld setPropName(String propertyPath) {
		this.propName = propertyPath;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public UIBizFld setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getPattern() {
		if (pattern == null)
			return "";
		else
			return pattern;
	}

	public UIBizFld setPattern(String pattern) {
		this.pattern = pattern;

		return this;
	}

	public String getLabel() {
		return label;
	}

	public UIBizFld setLabel(String label) {
		this.label = label;

		return this;
	}

	public Align getAlign() {
		return align;
	}

	public UIBizFld setAlign(Align align) {
		this.align = align;

		return this;
	}

	public boolean getRowBegin() {
		return rowBegin;
	}

	public void setRowBegin(boolean rowBegin) {
		this.rowBegin = rowBegin;
	}

	public boolean getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(boolean rowEnd) {
		this.rowEnd = rowEnd;
	}

	public String toString() {
		return this.label;
	}

	public String getComboboxUrl() {
		return comboboxUrl;
	}

	public void setComboboxUrl(String comboboxUrl) {
		this.comboboxUrl = comboboxUrl;
	}

	public int getColSpan() {
		return colSpan;
	}

	public String getKeyProp() {
		return keyProp;
	}

	public void setKeyProp(String keyProp) {
		this.keyProp = keyProp;
	}
}
