package com.kmetop.demsy.comlib.impl.sft.security;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.impl.sft.system.AbstractSystemData;

@Entity
public class ResourceOperation extends AbstractOperation {

	@ManyToOne
	protected Resource resource;

	@ManyToOne
	protected CommonOperation common;

	@ManyToOne
	protected AbstractSystemData crossFormHData;

	@ManyToOne
	protected AbstractSystemData crossFormVData;

	protected Integer token;

	public void setCrossForm(boolean flag) {
		super.setMask(MASK_CROSS_FORM, flag);
	}

	public boolean isCrossForm() {
		return super.getMask(MASK_CROSS_FORM);
	}

	public boolean isDisabled() {
		return super.getMask(MASK_DISABLED);
	}

	public void setDisabled(boolean disabled) {
		super.setMask(MASK_DISABLED, disabled);
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public CommonOperation getCommon() {
		return common;
	}

	public void setCommon(CommonOperation common) {
		this.common = common;
	}

	public AbstractSystemData getCrossFormHData() {
		return crossFormHData;
	}

	public void setCrossFormHData(AbstractSystemData crossFormHData) {
		this.crossFormHData = crossFormHData;
	}

	public AbstractSystemData getCrossFormVData() {
		return crossFormVData;
	}

	public void setCrossFormVData(AbstractSystemData crossFormVData) {
		this.crossFormVData = crossFormVData;
	}

	public Integer getToken() {
		return token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}

}
