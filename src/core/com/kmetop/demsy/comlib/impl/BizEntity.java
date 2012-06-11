package com.kmetop.demsy.comlib.impl;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.entity.IBizEntity;
import com.kmetop.demsy.lang.Cls;

/**
 * ID数据实体： 有唯一标识(ID)的数据实体
 * <p>
 * 
 * @author yongshan.ji
 */
public abstract class BizEntity implements IBizEntity {
	@Id
	@Column(name = "_id")
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Long id;

	protected Long softID;

	@Column(name = "_created", updatable = false)
	protected Date created;

	@Column(name = "_updated")
	protected Date updated;

	@Column(name = "_created_by", length = 32, updatable = false)
	protected String createdBy;

	@Column(name = "_updated_by", length = 32)
	protected String updatedBy;

	@Column(updatable = false)
	protected String entityGuid;

	protected Integer orderby;

	@Transient
	protected byte statusForJsonData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(Serializable id) {
		this.id = (Long) id;
	}

	public Long getSoftID() {
		return softID;
	}

	public void setSoftID(Long id) {
		this.softID = id;
	}

	//
	// public void setApplication(IDemsyApp app) {
	// if (app != null && app.getId() > 0) {
	// this.application = app.getId();
	// }
	// }

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getEntityGuid() {
		return entityGuid;
	}

	public void setEntityGuid(String entityId) {
		this.entityGuid = (String) entityId;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	// ***Other
	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		return 37 * 17 + id.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (!Cls.getType(getClass()).equals(Cls.getType(that.getClass()))) {
			return false;
		}

		BizEntity thatEntity = (BizEntity) that;
		if (id == null || id == 0 || thatEntity.id == null || thatEntity.id == 0) {
			return this == that;
		}

		return thatEntity.id.equals(id);
	}

	@Override
	public String toString() {
		if (id == null || id == 0)
			return Cls.getType(getClass()).getSimpleName() + "@" + Integer.toHexString(hashCode());
		else
			return Cls.getType(getClass()).getSimpleName() + "#" + id;
	}

	public byte getStatusForJsonData() {
		return statusForJsonData;
	}

	public void setStatusForJsonData(byte statusForAjaxAction) {
		this.statusForJsonData = statusForAjaxAction;
	}

}
