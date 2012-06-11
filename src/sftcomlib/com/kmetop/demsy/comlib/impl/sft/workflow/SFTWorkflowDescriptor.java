package com.kmetop.demsy.comlib.impl.sft.workflow;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
public class SFTWorkflowDescriptor extends SFTBizComponent {
	@OneToOne(mappedBy = "resource")
	protected SFTWorkflowResource resource;// 流程监管菜单

	public SFTWorkflowResource getResource() {
		return resource;
	}

	public void setResource(SFTWorkflowResource resource) {
		this.resource = resource;
	}
}
