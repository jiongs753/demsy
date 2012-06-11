package com.kmetop.demsy.comlib.impl.sft.security;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public abstract class AbstractOperation extends SFTBizComponent {

	public static final int MASK_DOWN_DRAW_SELECT = 1;// 2^0

	public static final int MASK_DIVIDER = 2;// 2^1

	public static final int MASK_CROSS_FORM = 4;//

	public static final int MASK_DISABLED = 8;// 2^2

	@ManyToOne
	private AbstractOperation parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	private List<AbstractOperation> children;

	protected Integer width;

	protected Integer height;

	@Column(length = 128)
	protected String enableImageName;

	@Column(length = 128)
	protected String disableImageName;

	@Column(length = 32)
	protected String actionMode;

	@Column(length = 128)
	protected String formBtnMode;

	@OneToMany(mappedBy = "op", cascade = CascadeType.REMOVE)
	protected List<OperationScript> scripts;

	@Column(length = 255)
	protected String script;

	@Column(length = 255)
	protected String loadPluginStrs;

	@Column(length = 255)
	protected String beforePluginStrs;

	@Column(length = 255)
	protected String afterPluginStrs;

	@Column(length = 255)
	protected String promptExpression;// 操作成功后的提示信息——表达式

	@Column(length = 255)
	protected String errorPromptExpression;// 操作出错后的提示信息——表达式

	@Column(length = 255)
	protected String moduleTitleExpression;// 模块标题——表达式

	public void setDownDrawSelect(boolean flag) {
		super.setMask(MASK_DOWN_DRAW_SELECT, flag);
	}

	public boolean isDownDrawSelect() {
		return super.getMask(MASK_DOWN_DRAW_SELECT);
	}

	public void setDivider(boolean flag) {
		super.setMask(MASK_DIVIDER, flag);
	}

	public boolean isDivider() {
		return super.getMask(MASK_DIVIDER);
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public List<OperationScript> getScripts() {
		return scripts;
	}

	public void setScripts(List<OperationScript> scripts) {
		this.scripts = scripts;
	}

	public String getEnableImageName() {
		return enableImageName;
	}

	public void setEnableImageName(String enableImageName) {
		this.enableImageName = enableImageName;
	}

	public String getDisableImageName() {
		return disableImageName;
	}

	public void setDisableImageName(String disableImageName) {
		this.disableImageName = disableImageName;
	}

	public String getMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getLoadPluginStrs() {
		return loadPluginStrs;
	}

	public void setLoadPluginStrs(String loadPluginStrs) {
		this.loadPluginStrs = loadPluginStrs;
	}

	public String getBeforePluginStrs() {
		return beforePluginStrs;
	}

	public void setBeforePluginStrs(String beforePluginStrs) {
		this.beforePluginStrs = beforePluginStrs;
	}

	public String getAfterPluginStrs() {
		return afterPluginStrs;
	}

	public String getErrorPromptExpression() {
		return errorPromptExpression;
	}

	public void setErrorPromptExpression(String errorPromptExpression) {
		this.errorPromptExpression = errorPromptExpression;
	}

	public void setAfterPluginStrs(String afterPluginStrs) {
		this.afterPluginStrs = afterPluginStrs;
	}

	public String getPromptExpression() {
		return promptExpression;
	}

	public void setPromptExpression(String promptExpression) {
		this.promptExpression = promptExpression;
	}

	public String getModuleTitleExpression() {
		return moduleTitleExpression;
	}

	public void setModuleTitleExpression(String moduleTitleExpression) {
		this.moduleTitleExpression = moduleTitleExpression;
	}

	public String getFormBtnMode() {
		return formBtnMode;
	}

	public void setFormBtnMode(String formActionMode) {
		this.formBtnMode = formActionMode;
	}

	public AbstractOperation getParent() {
		return parent;
	}

	public void setParent(AbstractOperation parent) {
		this.parent = parent;
	}

	public List<AbstractOperation> getChildren() {
		return children;
	}

	public void setChildren(List<AbstractOperation> children) {
		this.children = children;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
