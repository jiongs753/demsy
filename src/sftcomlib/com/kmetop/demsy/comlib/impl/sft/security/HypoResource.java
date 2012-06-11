package com.kmetop.demsy.comlib.impl.sft.security;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class HypoResource extends Resource {

	@ManyToOne
	protected Resource hostResource;// 所属主资源

	@ManyToMany
	protected List<ResourceOperation> hostOperations;// 主资源的操作

	@Column(columnDefinition = "text")
	private String dataFilterValues;

	@Column(columnDefinition = "text")
	private String bandParamValues;

	public Resource getHostResource() {
		return hostResource;
	}

	public void setHostResource(Resource hostResource) {
		this.hostResource = hostResource;
	}

	public List<ResourceOperation> getOperations() {
		List<ResourceOperation> ret = getHostOperations();
		if (ret == null || ret.size() == 0) {
			return this.getHostResource().getOperations();
		}
		return ret;
	}

	public List<ResourceOperation> getHostOperations() {
		return hostOperations;
	}

	public void setHostOperations(List<ResourceOperation> hostOperations) {
		this.hostOperations = hostOperations;
	}

	public String getDataFilterValues() {
		return dataFilterValues;
	}

	public void setDataFilterValues(String dataFilterValues) {
		this.dataFilterValues = dataFilterValues;
	}

	public String getBandParamValues() {
		return bandParamValues;
	}

	public void setBandParamValues(String bandParamValueValues) {
		this.bandParamValues = bandParamValueValues;
	}
}
