package soom.entity;

import java.util.Date;

/**
 * <b>业务组件:</b>
 * 
 * @author yongshan.ji
 */
public interface IBizComponent extends IDataOptions {
	Long getId();

	void setId(Long id);

	Long getSoftID();

	void setSoftID(Long id);

	Integer getOrderby();

	void setOrderby(Integer orderby);

	String getName();

	String getCode();

	String getDesc();

	Date getCreated();

	void setCreated(Date created);

	Date getUpdated();

	void setUpdated(Date updated);

	String getCreatedBy();

	void setCreatedBy(String createdBy);

	String getUpdatedBy();

	void setUpdatedBy(String updatedBy);

	String getEntityGuid();

	boolean isDisabled();

	boolean isBuildin();
}
