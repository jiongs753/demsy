package com.kmetop.demsy.comlib.impl.sft.security;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
public class OperationScript extends SFTBizComponent {

	@ManyToOne
	protected AbstractOperation op;

	protected String script;

	public AbstractOperation getOp() {
		return op;
	}

	public void setOp(AbstractOperation op) {
		this.op = op;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
