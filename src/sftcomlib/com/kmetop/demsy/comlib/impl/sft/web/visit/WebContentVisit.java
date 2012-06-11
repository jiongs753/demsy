package com.kmetop.demsy.comlib.impl.sft.web.visit;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.impl.sft.web.content.WebContent;

@Entity
public class WebContentVisit extends SFTBizComponent {

	@ManyToOne
	protected WebContent content;

	// 访问计数器
	protected Long count;

	public Long getCount() {
		if (count == null) {
			return 0l;
		}
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public WebContent getContent() {
		return content;
	}

	public void setContent(WebContent content) {
		this.content = content;
	}
}
