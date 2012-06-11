package com.kmetop.demsy.comlib.impl.sft.workflow;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.kmetop.demsy.comlib.impl.sft.security.Resource;

@Entity
public class SFTWorkflowResource extends Resource {

	@OneToOne(mappedBy = "resource")
	protected SFTWorkflowDescriptor workflow;

	public SFTWorkflowDescriptor getWorkflow() {
		return workflow;
	}

	public void setWorkflow(SFTWorkflowDescriptor workflow) {
		this.workflow = workflow;
	}

}
