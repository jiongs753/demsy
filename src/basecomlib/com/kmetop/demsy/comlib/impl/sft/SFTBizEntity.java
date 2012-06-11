package com.kmetop.demsy.comlib.impl.sft;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.kmetop.demsy.comlib.entity.IBizEntity;
import com.kmetop.demsy.comlib.entity.IDynamic;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Props;

public abstract class SFTBizEntity implements IDynamic, IBizEntity {
	@Id
	@Column(name = "_id")
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Long id;

	protected Long softID;

	@Version
	protected int demsyVersion = 0;

	@Column(updatable = false)
	protected String entityGuid;

	protected Integer orderby;

	@Column(name = "_status_value")
	protected Integer statusValue;

	@Column(name = "_created", updatable = false)
	protected Date created;

	@Column(name = "_updated")
	protected Date updated;

	@Column(name = "_created_by", length = 32, updatable = false)
	protected String createdBy;

	@Column(name = "_updated_by", length = 32)
	protected String updatedBy;

	// @ManyToOne
	// protected UserLogin loginOwner;

	// @ManyToOne
	// protected UserGroup groupOwner;

	// protected Long relatedSystemId;
	//
	// protected Long relatedId;

	@Column(columnDefinition = "text")
	protected String dynamicPropertiesText;

	@Transient
	protected Properties dynaProp;

	/**
	 * 数据临时状态，用来标识ajax批量操作时的操作状态，主要用来标识删除
	 */
	@Transient
	protected byte statusForJsonData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setUpdatedBy(String by) {
		this.updatedBy = by;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdated(Date u) {
		this.updated = u;
	}

	public Date getUpdated() {
		return updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getEntityGuid() {
		return entityGuid;
	}

	public void setEntityGuid(String entityId) {
		this.entityGuid = entityId;
	}

	public Long getSoftID() {
		return softID;
	}

	public void setSoftID(Long id) {
		softID = id;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer sortNum) {
		this.orderby = sortNum;
	}

	@Override
	public Properties getDynaProp() {
		if (dynaProp == null) {
			try {
				dynaProp = Props.toProps(this.dynamicPropertiesText);
			} catch (IOException e) {
			}
		}
		return dynaProp;
	}

	public String get(String key) {
		return (String) getDynaProp().get(key);
	}

	public boolean is(byte index) {
		return getMask(new Double(Math.pow(2, new Double(index - 1))).intValue());
	}

	protected void setMask(Integer MASK, Boolean flag) {
		if (statusValue == null) {
			statusValue = 0;
		}
		synchronized (statusValue) {
			if (flag) {// 隐藏
				statusValue |= MASK;
			} else {// 不隐藏
				int mask = ~MASK;// 反码
				statusValue &= mask;// 与
			}
		}
	}

	protected boolean getMask(Integer MASK) {
		if (statusValue == null) {
			return false;
		}
		synchronized (statusValue) {
			return (statusValue & MASK) > 0;
		}
	}

	protected void set(byte index, boolean flag) {
		this.setMask(new Double(Math.pow(2, new Double(index - 1))).intValue(), flag);
	}

	public String getDynamicPropertiesText() {
		if (dynaProp != null) {
			try {
				dynamicPropertiesText = Props.toString(dynaProp);
			} catch (IOException e) {
			}
		}
		return dynamicPropertiesText;
	}

	public void setDynamicPropertiesText(String props) {
		dynamicPropertiesText = props;
		try {
			dynaProp = Props.toProps(props);
		} catch (IOException e) {
		}
	}

	public void setDynaProp(Properties props) {
		dynaProp = props;
		try {
			dynamicPropertiesText = Props.toString(props);
		} catch (IOException e) {
		}
	}

	public void set(String key, Object value) {
		getDynaProp().put(key, value);
		try {
			this.dynamicPropertiesText = Props.toString(dynaProp);
		} catch (IOException e) {
		}
	}

	// public UserLogin getLoginOwner() {
	// return loginOwner;
	// }
	//
	// public void setLoginOwner(UserLogin loginOwner) {
	// this.loginOwner = loginOwner;
	// }
	//
	// public UserGroup getGroupOwner() {
	// return groupOwner;
	// }
	//
	// public void setGroupOwner(UserGroup groupOwner) {
	// this.groupOwner = groupOwner;
	// }

	// public Long getRelatedSystemId() {
	// return relatedSystemId;
	// }
	//
	// public void setRelatedSystemId(Long relatedSystemId) {
	// this.relatedSystemId = relatedSystemId;
	// }
	//
	// public Long getRelatedId() {
	// return relatedId;
	// }
	//
	// public void setRelatedId(Long relatedId) {
	// this.relatedId = relatedId;
	// }

	public int getDemsyVersion() {
		return demsyVersion;
	}

	public void setDemsyVersion(int demsyVersion) {
		this.demsyVersion = demsyVersion;
	}

	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		return 37 * 17 + new Long(id).hashCode();
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (!Cls.getType(getClass()).equals(Cls.getType(that.getClass()))) {
			return false;
		}

		SFTBizEntity thatEntity = (SFTBizEntity) that;
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
