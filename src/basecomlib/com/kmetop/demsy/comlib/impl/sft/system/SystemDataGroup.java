package com.kmetop.demsy.comlib.impl.sft.system;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_FIELD_GROUP;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.ORDER_BZUDF_FIELD_GROUP;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.IBizFieldGroup;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@BzSys(name = "字段分组", code = BIZSYS_BZUDF_FIELD_GROUP, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_BZUDF_FIELD_GROUP, buildin = true//
, actions = { @BzAct(name = "新增分组", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
		@BzFld(name = "分组名称", property = "name", mode = "c:M e:M")//
		,
		@BzFld(name = "分组编号", property = "code") //
		,
		@BzFld(name = "所属系统", property = "system", refrenceSystem = BIZSYS_BZUDF_SYSTEM, masterMapping = true, mode = "c:M *:S") //
		, @BzFld(name = "人工顺序", property = "orderby") //
		, @BzFld(name = "字段模式", property = "mode") //
		, @BzFld(name = "表单列数", property = "columns") //
		, @BzFld(name = "停止状态", property = "disabled", disabledNavi = true, options = "1:停用,0:启用") //
		, @BzFld(name = "分组描述", property = "desc") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class SystemDataGroup extends SFTBizComponent implements IBizFieldGroup {
	// public static final int MASK_HIDDEN = 1;// 2^0

	public static final int MASK_DISABLED = 4;// 2^2

	// public static final int MASK_SHOW_OWNER = 8;// 2^3

	// public static final int MASK_SHOW_OWNER_IN_TOP = 16;// 2^4

	@ManyToOne
	protected SFTSystem system;

	// protected Double width;
	//
	// protected Double height;

	/**
	 * 字段分组显示模式：用空格分隔，与子系统数据维护功能中的动作模式相对应。
	 * <p>
	 * E: 可编辑的 (即可读写)
	 * <p>
	 * I: 检查（带有一个隐藏字段存放其值）
	 * <p>
	 * S: 显示（但不带隐藏字段）
	 * <p>
	 * N: 不显示
	 * <p>
	 * P: 如果该字段有值就显示，否则如果没有值就不显示该字段
	 * <p>
	 * H: 隐藏 (不显示，但有一个隐藏框存在)
	 * <p>
	 * R : 只读
	 * <p>
	 * D : 禁用
	 * <p>
	 * 举例说明：
	 * <p>
	 * v:I——查看数据时，该字段处于检查模式
	 * <p>
	 * e:E——编辑数据时，字段可编辑
	 * <p>
	 * bu:N——批量修改数据时，字段不可见
	 */
	@Column(length = 255)
	protected String mode;

	// @Column(length = 255)
	// protected String ownerMode;

	protected Integer columns;

	// @OneToMany(mappedBy = "dataGroup", cascade = CascadeType.REMOVE)
	// protected List<AbstractSystemData> datas;

	// public void setShowOwnerInTop(boolean flag) {
	// super.setMask(MASK_SHOW_OWNER_IN_TOP, flag);
	// }
	//
	// public boolean isShowOwnerInTop() {
	// return super.getMask(MASK_SHOW_OWNER_IN_TOP);
	// }
	//
	// public void setHidden(boolean flag) {
	// super.setMask(MASK_HIDDEN, flag);
	// }
	//
	// public boolean isHidden() {
	// return super.getMask(MASK_HIDDEN);
	// }
	//
	// public void setShowOwner(boolean flag) {
	// super.setMask(MASK_SHOW_OWNER, flag);
	// }
	//
	// public boolean isShowOwner() {
	// return super.getMask(MASK_SHOW_OWNER);
	// }

	public boolean isDisabled() {
		return super.getMask(MASK_DISABLED);
	}

	public Boolean getDisabled() {
		return this.isDisabled();
	}

	public void setDisabled(Boolean disabled) {
		super.setMask(MASK_DISABLED, disabled);
	}

	// public List<AbstractSystemData> getDatas() {
	// return datas;
	// }
	//
	// public void setDatas(List<AbstractSystemData> properties) {
	// this.datas = properties;
	// }

	public SFTSystem getSystem() {
		return system;
	}

	public void setSystem(SFTSystem entity) {
		this.system = entity;
	}

	// public Double getHeight() {
	// return height;
	// }
	//
	// public void setHeight(Double height) {
	// this.height = height;
	// }
	//
	// public Double getWidth() {
	// return width;
	// }
	//
	// public void setWidth(Double width) {
	// this.width = width;
	// }

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	// public String getOwnerMode() {
	// return ownerMode;
	// }
	//
	// public void setOwnerMode(String ownerMode) {
	// this.ownerMode = ownerMode;
	// }

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}
}
