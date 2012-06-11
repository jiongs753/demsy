package com.kmetop.demsy.comlib.impl.sft.security;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public class Menu extends SFTBizComponent {
	protected String target;

	protected String image;

	protected String title;

	protected String onclick;

	protected String onmouseover;

	protected String onmouseout;

	protected String altimage;

	protected String tooltip;

	@Transient
	protected Boolean leaf = false;

	@ManyToOne
	protected Resource parentResource;// 当前菜单资源的上级菜单资源

	@ManyToOne
	protected Resource resource;// 当前菜单资源即为当前菜单的child对象

	protected Boolean productMust;// 平台菜单：产品必需标识

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getAltimage() {
		return altimage;
	}

	public void setAltimage(String altimage) {
		this.altimage = altimage;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOnmouseout() {
		return onmouseout;
	}

	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	public String getOnmouseover() {
		return onmouseover;
	}

	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource child) {
		this.resource = child;
	}

	public Resource getParentResource() {
		return parentResource;
	}

	public void setParentResource(Resource parent) {
		this.parentResource = parent;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public Boolean getProductMust() {
		return productMust;
	}

	public void setProductMust(Boolean productMust) {
		this.productMust = productMust;
	}
}
