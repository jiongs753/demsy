package com.kmetop.demsy.comlib.impl.sft.system;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_FIELD;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_FIELD_GROUP;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_FIELD;
import static com.kmetop.demsy.comlib.LibConst.ORDER_BZUDF_FIELD;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizFieldGroup;
import com.kmetop.demsy.comlib.biz.IBizFieldType;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.impl.sft.dic.DicCategory;
import com.kmetop.demsy.orm.ann.Prop;

/**
 * 表单字段显示模式
 * <p>
 * 字段显示模式：用空格分隔，与子系统数据操作中指定的动作模式组合使用。
 * <p>
 * M: Must 必需的
 * <p>
 * E: Edit 可编辑的 (即可读写)
 * <p>
 * I: Inspect 检查（带有一个隐藏字段存放其值）
 * <p>
 * S: Show 显示（但不带隐藏字段）
 * <p>
 * N: None 不显示
 * <p>
 * P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段
 * <p>
 * H: Hidden 隐藏 (不显示，但有一个隐藏框存在)
 * <p>
 * R: Read only 只读
 * <p>
 * D: Disable 禁用
 * <p>
 * 举例说明：
 * <p>
 * v:I——查看数据时，该字段处于检查模式
 * <p>
 * e:E——编辑数据时，字段可编辑
 * <p>
 * bu:N——批量修改数据时，字段不可见
 */
@Entity
@BzSys(name = "业务字段", code = BIZSYS_BZUDF_FIELD, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_BZUDF_FIELD, buildin = true//
, actions = { @BzAct(name = "新增字段", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(name = "批量修改", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { //
		@BzGrp(name = "基本信息", code = "basic"//
		, fields = {
				//
				@BzFld(name = "字段名称", property = "name", mode = "c:M e:M", gridOrder = 1, desc = "字段业务名称")//
				,
				@BzFld(name = "字段编号", property = "code", mode = "c:M e:M", gridOrder = 2, desc = "由数字、字母、下划线组成，且只能有数字、字母开头") //
				,
				@BzFld(name = "字段属性", property = "propName") //
				,
				@BzFld(name = "人工顺序", property = "orderby", gridOrder = 6) //
				,
				@BzFld(name = "所属系统", property = "system", refrenceSystem = BIZSYS_BZUDF_SYSTEM, masterMapping = true, mode = "*:S bu:N c:M e:M") //
				,
				@BzFld(name = "所属分组", property = "dataGroup", refrenceSystem = BIZSYS_BZUDF_FIELD_GROUP, disabledNavi = true, cascadeMode = "system:*:system", mode = "bu:N c:M e:M *:S", gridOrder = 3) //
				,
				@BzFld(name = "字段类型", property = "type", refrenceSystem = BIZSYS_DEMSY_LIB_FIELD, masterMapping = false, disabledNavi = true, mode = "*:S bu:N c:M e:M", options = "['version eq 2']", gridOrder = 4) //
				, @BzFld(name = "字段模式", property = "mode", gridOrder = 5) //
				, @BzFld(name = "级联模式", property = "cascadeMode") //
				, @BzFld(name = "GRID表头", property = "gridField", disabledNavi = true, mode = "bu:E", options = "1:显示,0:不显示") //
				, @BzFld(property = "privacy", name = "隐私字段", options = "1:隐私,0:公开", mode = "*:S") //
		})// end group
		, @BzGrp(name = "字段属性设置", code = "properties"//
		, fields = {
				// GRID
				@BzFld(name = "表头顺序", property = "gridOrder", cascadeMode = "gridField:1:E") //
				, @BzFld(name = "表头宽度", property = "gridWidth", cascadeMode = "gridField:1:E") //
				// 系统引用
				, @BzFld(name = "引用系统", property = "refrenceSystem", refrenceSystem = BIZSYS_BZUDF_SYSTEM, disabledNavi = true, cascadeMode = "type:7,System:M", mode = "*:S c:M e:M") //
				, @BzFld(name = "引用字段", property = "refrenceFields", mode = "*:S cE e:E") //
				, @BzFld(name = "从属系统", property = "mappingToMaster", disabledNavi = true, cascadeMode = "type:7,System:E") //
				, @BzFld(name = "数据多选", property = "sysMultiple", gridField = false, disabledNavi = true, options = "1:多选,0:单选", cascadeMode = "type:7,System:E") //
				, @BzFld(name = "数据冗余", property = "sysRedundancy", gridField = false, disabledNavi = true, cascadeMode = "type:7,System:E") // })
				// 数值
				, @BzFld(name = "显示格式", property = "pattern", gridField = false, cascadeMode = "type:2,5,Date,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @BzFld(name = "字段精度", property = "precision", gridField = false, cascadeMode = "type:1,2,String,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @BzFld(name = "小数位数", property = "scale", gridField = false, cascadeMode = "type:2,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @BzFld(name = "密码输入", property = "password", gridField = false, disabledNavi = true, cascadeMode = "type:1,String:E") //
				, @BzFld(name = "字段校验", property = "regexpMask", gridField = false, cascadeMode = "type:1,String:E") //
				, @BzFld(name = "计量单位", property = "uomOption", gridField = false, cascadeMode = "type:2,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @BzFld(name = "计量单位选项", property = "uomOptions", gridField = false, cascadeMode = "type:2,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				//
				, @BzFld(name = "字典选项", property = "options", gridField = false, cascadeMode = "type:1,2,13,String,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal,Boolean:E") //
				, @BzFld(name = "文件类型", property = "fileType", gridField = false, cascadeMode = "type:4,Upload:M") //
		}) // end groups
		, @BzGrp(name = "其他属性设置", code = "other"//
		, fields = {
				//
				@BzFld(name = "停用状态", property = "disabled", gridField = false, mode = "bu:E", options = "1:停用,0:启用") //
				, @BzFld(name = "数据导航", property = "disabledNavi", gridField = false, disabledNavi = true, options = "1:隐藏,0:显示") //
				, @BzFld(name = "默认分组", property = "groupBy", disabledNavi = true, gridField = false) //
				, @BzFld(name = "临时字段", property = "transientField", disabledNavi = true, gridField = false) //
				, @BzFld(name = "字段描述", property = "desc", desc = "对字段进行详细的描述，说明字段的用途、目的") //
				, @BzFld(name = "窗体模版", property = "uiTemplate", gridField = false) //
				, @BzFld(name = "内置字段", property = "buildin", disabledNavi = true, mode = "*:N") //
				, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
				, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
				, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
				, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
		}) // end groups
})
// 为避免多实体一个表的情况，现将SystemData改为AbstractSystemData，从而避免DTYPE字段。
public class AbstractSystemData extends SFTBizComponent implements IBizField {

	private boolean gridField;

	private boolean mappingToMaster;

	private boolean sysMultiple;

	private boolean sysRedundancy;

	private boolean password;

	private boolean disabledNavi;

	private boolean transientField;

	private boolean groupBy;

	@ManyToOne
	private SFTSystem system;

	@ManyToOne
	private SystemDataGroup dataGroup;

	@ManyToOne
	private SFTSystem refrenceSystem;// “系统引用”类型——所引用的系统

	@ManyToOne
	private AbstractSystemData refrenceData;// “字段引用”类型——所引用的系统字段

	@Column(length = 255)
	private String propName;

	@ManyToOne
	private AbstractSystemData parent;

	@Column(length = 255)
	private String mode;

	private String fileType;// 上传附件类型：分号分隔的文件扩展名

	@Prop("orderbygrid")
	private int gridOrder;

	private int gridWidth;

	@Column(name = "_precision")
	protected Integer precision;// 数据型和字符型——精度

	@Column(name = "_scale")
	protected Integer scale;

	@Column(length = 255)
	private String cascadeMode;

	@Column(length = 255)
	private String defaultValue;

	@ManyToOne
	protected SystemDataType type;// 数据类型

	// @ManyToOne
	// protected Dic mask;// 字符型——校验掩码

	@ManyToOne
	protected DicCategory dicCategory;// 字典类型—— 引用的字典类别

	public boolean isSysCombobox() {
		return super.getMask(4096);
	}

	public void setSysCombobox(boolean flag) {
		super.setMask(4096, flag);
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer stringLength) {
		this.precision = stringLength;
	}

	public SystemDataType getType() {
		return type;
	}

	public void setType(IBizFieldType type) {
		this.type = (SystemDataType) type;
	}

	public DicCategory getDicCategory() {
		return dicCategory;
	}

	public void setDicCategory(DicCategory dicCategory) {
		this.dicCategory = dicCategory;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	@Override
	public IBizFieldGroup getFieldGroup() {
		return this.getDataGroup();
	}

	@Override
	public IBizField getRefrenceField() {
		return this.getRefrenceData();
	}

	@Override
	public String getUploadType() {
		return fileType;
	}

	public void setUiTemplate(String value) {
		this.set("uiTemplate", value);
	}

	@Override
	@Column(length = 256)
	public String getUiTemplate() {
		return get("uiTemplate");
	}

	@Override
	@Column(length = 256)
	public String getOptions() {
		return this.get("options");
	}

	public void setOptions(String options) {
		this.set("options", options);
	}

	@Override
	public String getPattern() {
		return this.get("pattern");
	}

	public void setPattern(String pattern) {
		this.set("pattern", pattern);
	}

	@Override
	@Column(length = 256)
	public String getRegexpMask() {
		return this.get("regexpMask");
	}

	public void setRegexpMask(String regexpMask) {
		this.set("regexpMask", regexpMask);
	}

	@Override
	@Column(length = 256)
	public String getUomOptions() {
		return this.get("uomOptions");
	}

	public void setUopOptions(String value) {
		this.set("uomOptions", value);
	}

	@Override
	public String getUomOption() {
		return this.get("uomOption");
	}

	public void setUopOption(String value) {
		this.set("uomOption", value);
	}

	public boolean isGridField() {
		return gridField;
	}

	public boolean isMappingToMaster() {
		return mappingToMaster;
	}

	public boolean isSysMultiple() {
		return sysMultiple;
	}

	public boolean isSysRedundancy() {
		return sysRedundancy;
	}

	public boolean isPassword() {
		return password;
	}

	public boolean isDisabledNavi() {
		return disabledNavi;
	}

	public boolean isTransientField() {
		return transientField;
	}

	public SFTSystem getSystem() {
		return system;
	}

	public SystemDataGroup getDataGroup() {
		return dataGroup;
	}

	public SFTSystem getRefrenceSystem() {
		return refrenceSystem;
	}

	public AbstractSystemData getRefrenceData() {
		return refrenceData;
	}

	public String getPropName() {
		return propName;
	}

	public String getMode() {
		return mode;
	}

	public String getFileType() {
		return fileType;
	}

	public int getGridOrder() {
		return gridOrder;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridField(boolean gridField) {
		this.gridField = gridField;
	}

	public void setMappingToMaster(boolean mappingToMaster) {
		this.mappingToMaster = mappingToMaster;
	}

	public void setSysMultiple(boolean sysMultiple) {
		this.sysMultiple = sysMultiple;
	}

	public void setSysRedundancy(boolean sysRedundancy) {
		this.sysRedundancy = sysRedundancy;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public void setDisabledNavi(boolean disabledNavi) {
		this.disabledNavi = disabledNavi;
	}

	public void setTransientField(boolean transientField) {
		this.transientField = transientField;
	}

	public void setSystem(SFTSystem system) {
		this.system = system;
	}

	public void setDataGroup(SystemDataGroup dataGroup) {
		this.dataGroup = dataGroup;
	}

	public void setRefrenceSystem(SFTSystem refrenceSystem) {
		this.refrenceSystem = refrenceSystem;
	}

	public void setRefrenceData(AbstractSystemData refrenceData) {
		this.refrenceData = refrenceData;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setGridOrder(int gridOrderBy) {
		this.gridOrder = gridOrderBy;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public boolean isShowInGrid() {
		return super.getMask(2);
	}

	public boolean isMapping() {
		return getMask(16) && this.getRefrenceSystem() != null && this.getRefrenceData() == null;
	}

	public String getCascadeMode() {
		return cascadeMode;
	}

	public void setCascadeMode(String cascadeMode) {
		this.cascadeMode = cascadeMode;
	}

	public boolean isGroupBy() {
		return groupBy;
	}

	public boolean getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(boolean tree) {
		this.groupBy = tree;
	}

	public AbstractSystemData getParent() {
		return parent;
	}

	public void setParent(AbstractSystemData parent) {
		this.parent = parent;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isPrivacy() {
		return getPrivacy();
	}

	public Boolean getPrivacy() {
		String v = get("privacy");
		return "1".equals(v);
	}

	public void setPrivacy(String value) {
		this.set("privacy", value);
	}

	public void setRefrenceFields(String str) {
		this.set("refrenceFields", str);
	}

	public String getRefrenceFields() {
		return this.get("refrenceFields");
	}
}
