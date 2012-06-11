package com.kmetop.demsy.comlib.impl;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.entity.IDynamic;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Props;

public abstract class DynaEntity extends BizEntity implements IDynamic {

	@Column(name = "_status_value")
	protected Integer maskValue;

	@Column(columnDefinition = "text")
	protected String dynamicPropString;

	@Transient
	protected Properties dynamicProps;

	public String getDynamicPropString() {
		if (dynamicProps != null) {
			try {
				dynamicPropString = Props.toString(dynamicProps);
			} catch (IOException e) {
				throw Ex.throwEx(e);
			}
		}
		return dynamicPropString;
	}

	public void setDynamicPropString(String props) {
		dynamicPropString = props;
		try {
			dynamicProps = Props.toProps(props);
		} catch (IOException e) {
			throw Ex.throwEx(e);
		}
	}

	@Override
	public Properties getDynaProp() {
		if (dynamicProps == null) {
			try {
				dynamicProps = Props.toProps(this.dynamicPropString);
			} catch (IOException e) {
				throw Ex.throwEx(e);
			}
		}
		return dynamicProps;
	}

	public void setDynaProp(Properties props) {
		dynamicProps = props;
		try {
			dynamicPropString = Props.toString(props);
		} catch (IOException e) {
			throw Ex.throwEx(e);
		}
	}

	public String get(String key) {
		return (String) getDynaProp().get(key);
	}

	public void set(String key, Object value) {
		if (value == null)
			getDynaProp().remove(key);
		else
			getDynaProp().put(key, value);

		try {
			this.dynamicPropString = Props.toString(dynamicProps);
		} catch (IOException e) {
			throw Ex.throwEx(e);
		}
	}

	protected void set(byte index, boolean flag) {
		int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
		if (maskValue == null) {
			maskValue = 0;
		}
		synchronized (maskValue) {
			if (flag) {// 隐藏
				maskValue |= MASK;
			} else {// 不隐藏
				int mask = ~MASK;// 反码
				maskValue &= mask;// 与
			}
		}
	}

	public boolean is(byte index) {
		int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
		if (maskValue == null) {
			return false;
		}
		synchronized (maskValue) {
			return (maskValue & MASK) > 0;
		}
	}
}
