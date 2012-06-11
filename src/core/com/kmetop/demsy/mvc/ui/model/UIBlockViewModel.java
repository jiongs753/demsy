package com.kmetop.demsy.mvc.ui.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.mvc.ui.widget.UIBlockView;

public class UIBlockViewModel extends UIWidgetModel<UIBlockView, Object> {

	private Long parent;

	private List<UIBlockViewModel> children;

	private Map<Long, UIBlockViewModel> childrenMap;

	private String expression;

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public UIBlockViewModel(UIBlockView model, Object data) {
		super(model, data);
		children = new LinkedList();
		childrenMap = new HashMap();

		super.set("children", children);
	}

	public List<UIBlockViewModel> getChildren() {
		return children;
	}

	public void setChildren(List<UIBlockViewModel> children) {
		this.children = children;
	}

	public void addChild(UIBlockViewModel child) {
		children.add(child);
		childrenMap.put((Long) child.getId(), child);
	}

	public void addChild(int idx, UIBlockViewModel child) {
		children.add(idx, child);
		childrenMap.put((Long) child.getId(), child);
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public void adjust() {
		for (int i = children.size() - 1; i >= 0; i--) {
			UIBlockViewModel child = (UIBlockViewModel) children.get(i);
			if (child.getParent() != null) {
				UIBlockViewModel parent = (UIBlockViewModel) childrenMap.get(child.getParent());
				if (parent != null) {
					parent.addChild(0, child);
				}
				if (child.getParent() != this.getId())
					children.remove(i);
			}
		}
	}

}
