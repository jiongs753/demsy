package com.kmetop.demsy.comlib.ui;

public interface IStyle {
	public Long getId();

	/**
	 * 获取样式名字，用作html元素的class属性
	 * 
	 * @return
	 */
	public String getCssClass();

	/**
	 * 获取样式内容，用于生成&lt;style&gt;...&lt;style&gt;的内容部分。
	 * 
	 * @return
	 */
	public String getCssStyle();
}
