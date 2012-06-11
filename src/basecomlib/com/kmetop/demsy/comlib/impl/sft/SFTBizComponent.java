package com.kmetop.demsy.comlib.impl.sft;

import javax.persistence.Column;

import org.nutz.lang.Strings;

public abstract class SFTBizComponent extends SFTBizEntity {
	@Column(name = "_name", length = 255)
	protected String name;

	@Column(name = "_code", length = 64)
	protected String code;

	@Column(name = "_desc", length = 2000)
	protected String desc;

	private Boolean buildin;

	private Boolean disabled;

	public boolean isBuildin() {
		return buildin != null && buildin;
	}

	public Boolean getBuildin() {
		return buildin;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setBuildin(Boolean b) {
		this.buildin = b;
	}

	public boolean isDisabled() {
		return disabled != null && disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		if (Strings.isEmpty(name))
			return super.toString();

		return name;
	}
}
