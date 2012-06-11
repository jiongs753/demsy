package com.kmetop.demsy.comlib.impl.sft.system;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_CATALOG;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.ORDER_BZUDF_SYSTEM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.base.biz.BizCatalog;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@BzSys(name = "系统自定义", code = BIZSYS_BZUDF_SYSTEM, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_BZUDF_SYSTEM, buildin = true//
, actions = { @BzAct(name = "新增系统", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.biz.CreateBizSystem")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
		, @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = "com.kmetop.demsy.plugins.biz.EditBizSystem") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "系统名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "系统编号", property = "code") //
		, @BzFld(name = "人工顺序", property = "orderby") //
		, @BzFld(name = "系统分类", property = "catalog", groupBy = true, refrenceSystem = BIZSYS_BZUDF_CATALOG) //
}), @BzGrp(name = "扩展信息", code = "ext"//
, fields = { @BzFld(name = "窗体模版", property = "template", gridField = false) //
		, @BzFld(name = "映射实体类", property = "mappingClass", gridField = false, mode = "*:P") //
		, @BzFld(name = "实体类扩展", property = "extendClass", gridField = false) //
		, @BzFld(name = "映射数据表", property = "mappingTable", gridField = false, mode = "*:P") //
		, @BzFld(name = "系统描述", property = "desc") //
		, @BzFld(property = "layout") //
		, @BzFld(name = "停用状态", property = "disabled", disabledNavi = true, options = "1:停用,0:启用") //
		, @BzFld(name = "内置状态", property = "buildin", disabledNavi = true, mode = "*:N") //
		, @BzFld(name = "父系统", property = "parent", disabledNavi = true, mode = "*:N") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:N v:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:N v:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:P") //
}) }// end groups
)
public class SFTSystem extends SFTBizComponent implements IBizSystem {

	@ManyToOne
	private BizCatalog catalog;

	@ManyToOne
	protected SFTSystem parent;

	@BzFld(name = "表单布局", options = "0:列表结构,1:纵向TAB结构")
	protected byte layout;

	/*
	 * 老系统字段
	 */
	@OneToOne
	private SystemResource resource;// 系统对应的菜单资源

	@Transient
	private String version;

	public SystemResource getResource() {
		return resource;
	}

	public void setResource(SystemResource resource) {
		this.resource = resource;
	}

	@Override
	public String getEntityOwnerField() {
		return get("entityOwnerField");
	}

	public void setEntityOwnerField(String value) {
		this.set("entityOwnerField", value);
	}

	@Override
	public String getMappingTable() {
		return get("mappingTable");
	}

	public void setMappingTable(String value) {
		this.set("mappingTable", value);
	}

	@Column(length = 256)
	public String getMappingClass() {
		return get("mappingClass");
	}

	public void setMappingClass(String value) {
		this.set("mappingClass", value);
	}

	@Column(length = 256)
	public String getExtendClass() {
		return get("extendClass");
	}

	public void setExtendClass(String value) {
		this.set("extendClass", value);
	}

	public void setTemplate(String value) {
		this.set("template", value);
	}

	@Column(length = 256)
	public String getTemplate() {
		return get("template");
	}

	@Override
	public String getVersion() {
		return version;
	}

	public void setVersion(String v) {
		this.version = v;
	}

	public BizCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(BizCatalog catalog) {
		this.catalog = catalog;
	}

	public byte getLayout() {
		return layout;
	}

	public void setLayout(byte layout) {
		this.layout = layout;
	}

}
