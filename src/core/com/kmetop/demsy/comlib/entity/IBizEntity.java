package com.kmetop.demsy.comlib.entity;

import java.util.Date;

public interface IBizEntity {
	public Long getId();

	public void setId(Long id);

	public Long getSoftID();

	public void setSoftID(Long softID);

	public Date getCreated();

	public void setCreated(Date created);

	public Date getUpdated();

	public void setUpdated(Date updated);

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public String getUpdatedBy();

	public void setUpdatedBy(String updatedBy);

	public String getEntityGuid();

	public void setEntityGuid(String entityId);

	/**
	 * 
	 * 数据临时状态，用来标识ajax批量操作时的操作状态，主要用来标识删除.
	 * <p>
	 * 1——删除
	 * 
	 * @return
	 */
	public byte getStatusForJsonData();
}
