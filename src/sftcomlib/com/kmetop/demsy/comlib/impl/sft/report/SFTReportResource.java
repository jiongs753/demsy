package com.kmetop.demsy.comlib.impl.sft.report;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.kmetop.demsy.comlib.impl.sft.security.Resource;

@Entity
public class SFTReportResource extends Resource {

	@OneToOne(mappedBy = "resource")
	protected SFTJasperDesign report;

	public SFTJasperDesign getReport() {
		return report;
	}

	public void setReport(SFTJasperDesign report) {
		this.report = report;
	}
}
