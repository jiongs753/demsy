package com.kmetop.demsy.orm.generator;

import java.io.Serializable;

import com.kmetop.demsy.orm.nutz.IExtDao;

public interface Generator<T> {

	public Serializable generate(IExtDao dao, T obj);

}
