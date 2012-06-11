package com.kmetop.demsy.comlib.entity;

/**
 * 包含时序ID的实体，时序ID即业务实体唯一编号为时间 + 序号构成。如订单号：20111009-123456
 * 
 * @author Administrator
 * 
 */
public interface ITimeID {
	String getTimeID();

	void setTimeID(String id);
}
