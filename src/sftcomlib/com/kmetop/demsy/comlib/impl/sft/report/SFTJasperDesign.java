package com.kmetop.demsy.comlib.impl.sft.report;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
public class SFTJasperDesign extends SFTBizComponent {
	@OneToOne
	protected SFTReportResource resource;// 流程监管菜单

	public SFTReportResource getResource() {
		return resource;
	}

	public void setResource(SFTReportResource resource) {
		this.resource = resource;
	}

}
