package com.kmetop.demsy.comlib.impl;

import javax.persistence.Column;

import org.nutz.lang.Strings;

import com.kmetop.demsy.comlib.entity.IBizComponent;

public abstract class BizComponent extends DynaEntity implements IBizComponent {

	@Column(name = "_name", length = 255)
	protected String name;

	@Column(name = "_code", length = 64)
	protected String code;

	@Column(name = "_desc", length = 2000)
	protected String desc;

	protected Boolean disabled;

	protected Boolean buildin;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isDisabled() {
		return disabled != null && disabled;
	}

	public boolean isBuildin() {
		return buildin != null && buildin;
	}

	public Boolean getDisabled() {
		return isDisabled();
	}

	public Boolean getBuildin() {
		return buildin;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public void setBuildin(Boolean buildin) {
		this.buildin = buildin;
	}

	@Override
	public String toString() {
		if (Strings.isEmpty(name))
			return super.toString();

		return name;
	}
}
