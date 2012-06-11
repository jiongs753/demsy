package com.kmetop.demsy.comlib.impl.sft.security;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class CommonOperation extends AbstractOperation {

	private static final int MASK_SUPPORT_PLATFORM = 4;// 2^2

	private static final int MASK_SUPPORT_SYSTEM = 8;// 2^3

	private static final int MASK_SUPPORT_REPORT = 16;// 2^4

	private static final int MASK_SUPPORT_WORKFLOW = 32;// 2^5

	@OneToMany(mappedBy = "common", cascade = CascadeType.REMOVE)
	private List<ResourceOperation> resOps;

	public boolean isSystemEnabled() {
		return super.getMask(MASK_SUPPORT_SYSTEM);
	}

	public void setSystemEnabled(boolean disabled) {
		super.setMask(MASK_SUPPORT_SYSTEM, disabled);
	}

	public void setReportEnabled(boolean disabled) {
		super.setMask(MASK_SUPPORT_REPORT, disabled);
	}

	public boolean isReportEnabled() {
		return super.getMask(MASK_SUPPORT_REPORT);
	}

	public void setWorkflowEnabled(boolean disabled) {
		super.setMask(MASK_SUPPORT_WORKFLOW, disabled);
	}

	public boolean isWorkflowEnabled() {
		return super.getMask(MASK_SUPPORT_WORKFLOW);
	}

	public boolean isPlatformEnabled() {
		return super.getMask(MASK_SUPPORT_PLATFORM);
	}

	public void setPlatformEnabled(boolean disabled) {
		super.setMask(MASK_SUPPORT_PLATFORM, disabled);
	}

	public List<ResourceOperation> getResOps() {
		return resOps;
	}

	public void setResOps(List<ResourceOperation> resOps) {
		this.resOps = resOps;
	}

}
