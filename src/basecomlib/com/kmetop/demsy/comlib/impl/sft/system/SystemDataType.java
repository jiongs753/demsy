package com.kmetop.demsy.comlib.impl.sft.system;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_DEMSY_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_FIELD;
import static com.kmetop.demsy.comlib.LibConst.ORDER_DEMSY_LIB_FIELD;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.IBizFieldType;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "字段类型组件库", code = BIZSYS_DEMSY_LIB_FIELD, catalog = BIZCATA_DEMSY_ADMIN, orderby = ORDER_DEMSY_LIB_FIELD, buildin = true//
, actions = { @BzAct(name = "新增", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "类型名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "类型编号", property = "code", mode = "c:M e:M") //
		, @BzFld(name = "版本号", property = "version", mode = "c:M e:M", options = "2:DEMSY-V2") //
		, @BzFld(name = "窗体模版", property = "uiTemplate") //
		, @BzFld(name = "类型描述", property = "desc", gridField = false) //
		, @BzFld(name = "停用状态", property = "disabled", options = "1:停用,0:启用", gridField = false) //
		, @BzFld(name = "字段分类", property = "parent", refrenceSystem = BIZSYS_DEMSY_LIB_FIELD, gridField = false) //
		, @BzFld(name = "字段类型", property = "className", gridField = false) //
		, @BzFld(name = "字段排序", property = "orderby", gridField = false) //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
, jsonData = "BizFieldLib.data.js")
public class SystemDataType extends SFTBizComponent implements IBizFieldType {

	@ManyToOne
	private SystemDataType parent;

	@OneToMany(mappedBy = "parent")
	private List<SystemDataType> children;

	@Column(length = 255)
	private String className;

	private String uiTemplate;

	private Byte version;

	public SystemDataType getParent() {
		return parent;
	}

	public void setParent(SystemDataType parent) {
		this.parent = parent;
	}

	@Override
	public boolean isBoolean() {
		return "Boolean".equals(code)//
				|| "13".equals(code);
	}

	@Override
	public boolean isV1Dic() {
		return "3".equals(code)//
				|| "9".equals(code)//
				|| "10".equals(code)//
				|| "11".equals(code);
	}

	public boolean isV1GEO() {
		return "18".equals(code)//
		;
	}

	public boolean isManyToMany() {
		return "9".equals(code)//
				|| "11".equals(code);
	}

	@Override
	public boolean isNumber() {
		return "Integer".equals(code)//
				|| "Double".equals(code)//
				|| "Byte".equals(code)//
				|| "Short".equals(code)//
				|| "Long".equals(code)//
				|| "BigInteger".equals(code)//
				|| "Float".equals(code)//
				|| "BigDecimal".equals(code)//
				|| "2".equals(code)//

		;
	}

	@Override
	public boolean isInteger() {
		return "Integer".equals(code)//
				|| "Byte".equals(code)//
				|| "Short".equals(code)//
				|| "Long".equals(code)//

		;
	}

	@Override
	public boolean isRichText() {
		return "RichText".equals(code)//
				|| "6".equals(code);
	}

	@Override
	public boolean isDate() {
		return "Date".equals(code)//
				|| "5".equals(code)//
				|| "12".equals(code);
	}

	@Override
	public boolean isString() {
		return "String".equals(code)//
				|| "1".equals(code)//
				|| "14".equals(code);
	}

	@Override
	public boolean isUpload() {
		return "Upload".equals(code)//
				|| "4".equals(code);
	}

	@Override
	public boolean isSystem() {
		return "System".equals(code)//
				|| "7".equals(code);
	}

	public String getClassName() {
		return className;
	}

	@Override
	public String getType() {
		if (Str.isEmpty(className))
			return code;

		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Byte getVersion() {
		return version;
	}

	public void setVersion(Byte version) {
		this.version = version;
	}

	public String getUiTemplate() {
		return uiTemplate;
	}

	public void setUiTemplate(String uiTemplate) {
		this.uiTemplate = uiTemplate;
	}

	public List<SystemDataType> getChildren() {
		return children;
	}

	public void setChildren(List<SystemDataType> children) {
		this.children = children;
	}

}
