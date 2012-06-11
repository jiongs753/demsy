package com.kmetop.demsy.comlib.biz.field;

import java.util.List;

/**
 * 伪子系统：与真实子系统的区别在于伪子系统数据被保存在主系统字段中，而真子系统数据被保存在子系统业务表中。
 * 
 * @author yongshan.ji
 * 
 * @param <T>
 */
public class FakeSubSystem<T> extends SubSystem<T> {

	public FakeSubSystem() {
		super("");
	}

	public FakeSubSystem(String str) {
		super(str);
	}

	public FakeSubSystem(List<T> list) {
		super(list);
	}

	public FakeSubSystem(String str, Class<T> type) {
		super(str, type);
	}
}
