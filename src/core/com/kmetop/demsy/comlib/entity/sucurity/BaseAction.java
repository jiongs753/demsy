package com.kmetop.demsy.comlib.entity.sucurity;

import javax.persistence.Column;

import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.BizComponent;

public abstract class BaseAction extends BizComponent {

	@Column(length = 255)
	@BzFld(name = "链接地址")
	private String targetUrl;

	@Column(length = 16)
	@BzFld(name = "业务窗口", disabledNavi = true, options = "_blank:新窗口,_self:自身窗口,_fixed:固定窗口")
	protected String targetWindow;

	protected Integer typeCode;

	@Column(length = 16)
	protected String mode;

	@Column(length = 256)
	protected String plugin;

	protected Upload logo;

	protected Upload image;

	@Column(length = 256)
	protected String params;

	private Long upgradeFrom;

	private String template;

	@Column(length = 256)
	private String info;

	@Column(length = 256)
	private String error;

	@Column(length = 256)
	private String warn;

	public Integer getTypeCode() {
		return typeCode;
	}

	public String getMode() {
		return mode;
	}

	public String getPlugin() {
		return plugin;
	}

	public Upload getLogo() {
		return logo;
	}

	public Upload getImage() {
		return image;
	}

	public void setTypeCode(int method) {
		this.typeCode = method;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public void setLogo(Upload logo) {
		this.logo = logo;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public Long getUpgradeFrom() {
		return upgradeFrom;
	}

	public void setUpgradeFrom(Long upgradeFrom) {
		this.upgradeFrom = upgradeFrom;
	}

	public String getTemplate() {
		return template;
	}

	public String getInfo() {
		return info;
	}

	public String getError() {
		return error;
	}

	public String getWarn() {
		return warn;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setInfo(String successInfo) {
		this.info = successInfo;
	}

	public void setError(String errorInfo) {
		this.error = errorInfo;
	}

	public void setWarn(String warnInfo) {
		this.warn = warnInfo;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getTargetWindow() {
		return targetWindow;
	}

	public void setTargetWindow(String targetWindow) {
		this.targetWindow = targetWindow;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
}
