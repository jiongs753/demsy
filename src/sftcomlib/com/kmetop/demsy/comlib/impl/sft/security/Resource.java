package com.kmetop.demsy.comlib.impl.sft.security;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.impl.sft.system.SFTSystem;

@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public class Resource extends SFTBizComponent {
	private static final byte HIDE_LINK = 1;

	private static final byte HIDE_MENU = 2;

	private static final byte DISABLED = 3;

	private static final byte BUILDIN = 4;

	private static final byte DISABLED_HTML = 5;

	protected String path;// 模块访问路径

	protected String logo;// 模块徽标

	protected String image;// 模块图片

	@OneToMany(mappedBy = "resource", cascade = CascadeType.REMOVE)
	protected List<Menu> menus;// Menu对象的child值为当前Resource 级联删除当前资源所对应的菜单

	@OneToMany(mappedBy = "parentResource", cascade = CascadeType.REMOVE)
	protected List<Menu> childrenMenus;// Menu对象parent值是当前Resource
										// 级联删除当前资源的所有子菜单

	@OneToMany(mappedBy = "resource", cascade = CascadeType.REMOVE)
	protected List<ResourceOperation> operations;// 模块操作

	private String relatedClassName;

	@ManyToOne
	protected SFTSystem bindingSystem;// 静态模块绑定子系统

	protected String bindingProps;

	protected String subheadExp;// 菜单副标题表达式

	public void setDisabledHtml(boolean flag) {
		super.set(DISABLED_HTML, flag);
	}

	public boolean isDisabledHtml() {
		return super.is(DISABLED_HTML);
	}

	public void setBuildin(boolean flag) {
		super.set(BUILDIN, flag);
	}

	public boolean isBuildin() {
		return super.is(BUILDIN);
	}

	public void setHideLink(boolean flag) {
		super.set(HIDE_LINK, flag);
	}

	public boolean isHideLink() {
		return super.is(HIDE_LINK);
	}

	public void setHideMenu(boolean flag) {
		super.set(HIDE_MENU, flag);
	}

	public boolean isHideMenu() {
		return super.is(HIDE_MENU);
	}

	public boolean isDisabled() {
		return super.is(DISABLED);
	}

	public void setDisabled(boolean disabled) {
		super.set(DISABLED, disabled);
	}

	public List<Menu> getChildrenMenus() {
		return childrenMenus;
	}

	public void setChildrenMenus(List<Menu> children) {
		this.childrenMenus = children;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> parents) {
		this.menus = parents;
	}

	public List<ResourceOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<ResourceOperation> operations) {
		this.operations = operations;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getRelatedClassName() {
		return relatedClassName;
	}

	public void setRelatedClassName(String relatedClassName) {
		this.relatedClassName = relatedClassName;
	}

	public SFTSystem getBindingSystem() {
		return bindingSystem;
	}

	public void setBindingSystem(SFTSystem bindingSystem) {
		this.bindingSystem = bindingSystem;
	}

	public String getBindingProps() {
		return bindingProps;
	}

	public void setBindingProps(String bindingProps) {
		this.bindingProps = bindingProps;
	}

	public String getSubheadExp() {
		return subheadExp;
	}

	public void setSubheadExp(String subheadExp) {
		this.subheadExp = subheadExp;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
