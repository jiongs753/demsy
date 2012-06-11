package com.kmetop.demsy.comlib.impl.sft.web.content;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.kmetop.demsy.comlib.impl.sft.security.Resource;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
public class WebContentCategoryResource extends Resource {

	@Prop("webContentCategory")
	@OneToOne(mappedBy = "resource")
	protected WebContentCategory infoCatalog;

	public void setInfoCatalog(WebContentCategory infoCatalog) {
		this.infoCatalog = infoCatalog;
	}

}
