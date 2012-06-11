package com.kmetop.demsy.biz;

public interface IBizPlugin {
	public void before(BizEvent event);

	public void after(BizEvent event);

	public void load(BizEvent event);

	public void loaded(BizEvent event);
}
