package com.kmetop.demsy.comlib.impl.sft.system;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.nutz.lang.Strings;

import com.kmetop.demsy.comlib.impl.sft.security.Resource;

@Entity
public class SystemResource extends Resource {

	@OneToOne(mappedBy = "resource")
	protected SFTSystem system;

	public String getName() {
		String name = super.getName();
		if (Strings.isEmpty(name)) {
			return getSystem().getName();
		}
		return name;
	}

	public SFTSystem getSystem() {
		return system;
	}

	public void setSystem(SFTSystem system) {
		this.system = system;
	}
}
