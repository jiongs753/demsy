package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_DATASOURCE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_MODULE;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_MODULE;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.impl.base.lib.DemsyDS;
import com.kmetop.demsy.comlib.impl.sft.system.SFTSystem;
import com.kmetop.demsy.comlib.security.IModule;

@Entity
@BzSys(name = "功能模块设置", code = BIZSYS_ADMIN_MODULE, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_MODULE, buildin = true//
, actions = { @BzAct(name = "新增模块", typeCode = TYPE_BZFORM_NEW, mode = "c", disabled = true)//
		, @BzAct(name = "批量修改", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(name = "模块名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "模块编号", property = "code") //
		, @BzFld(name = "模块类型", property = "type", disabledNavi = true, mode = "c:M e:M", options = "[" + //
				"{value:'90',text:'文件夹'}\n" + //
				",{value:'1',text:'静态模块'}\n" + //
				",{value:'2',text:'业务模块'}\n" + //
				// ",{value:'3',text:'网站栏目'}\n" + //
				// ",{value:'4',text:'报表模块'}\n" + //
				// ",{value:'5',text:'流程模块'}\n" + //
				",{value:'6',text:'动态模块'}\n" + //
				"]") //
		, @BzFld(name = "软件名称", property = "softName", mode = "*:P") //
		, @BzFld(name = "绑定组件", property = "refName", mode = "*:P") //
		, @BzFld(name = "停用状态", property = "disabled", disabledNavi = true, mode = "bu:E", options = "1:停用,0:启用", gridField = false) //
		, @BzFld(name = "隐藏状态", property = "hidden", disabledNavi = true, mode = "bu:E", options = "1:隐藏,0:显示", gridField = false) //
}),//
		@BzGrp(name = "模块属性设置", code = "properties"//
		, fields = {
				//
				@BzFld(name = "上级模块", property = "parent", refrenceSystem = BIZSYS_ADMIN_MODULE, mode = "bu:E", cascadeMode = "type:1,2,3,4,5,6:M type:90:E", options = "['type eq 90']"), //
				@BzFld(name = "业务系统", property = "refSystem", refrenceSystem = BIZSYS_BZUDF_SYSTEM, gridField = false, disabledNavi = true, cascadeMode = "type:2:M"), //
				@BzFld(name = "模块数据源", property = "dataSource", refrenceSystem = BIZSYS_DEMSY_DATASOURCE, cascadeMode = "type:2:E", gridField = false),//
				@BzFld(name = "模块路径", property = "path", cascadeMode = "type:1:E", gridField = false) //
		}) //
		, @BzGrp(name = "其他属性设置", code = "other"//
		, fields = {
				//
				@BzFld(name = "模块操作", property = "refActions", gridField = false) //
				, @BzFld(name = "模块徽标", property = "logo", uploadType = "*.jpg;*.gif;*.png", gridField = false) //
				, @BzFld(name = "模块图片", property = "image", uploadType = "*.jpg;*.gif;*.png", gridField = false) //
				, @BzFld(name = "窗体模版", property = "template", desc = "用于展现业务操作界面", gridField = false) //
				, @BzFld(name = "模块描述", property = "desc", gridField = false) //
				, @BzFld(name = "内置状态", property = "buildin", disabledNavi = true, mode = "*:N", gridField = false) //
				, @BzFld(property = "refSystemExtends") //
				, @BzFld(property = "refSystemClass") //
				, @BzFld(name = "创建时间", property = "created", mode = "*:P", pattern = "yyyy-MM-dd HH:mm:ss") //
				, @BzFld(name = "更新时间", property = "updated", mode = "*:P", pattern = "yyyy-MM-dd HH:mm:ss") //
				, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
				, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
		}) //

}// end groups
, jsonData = "Module.data.js"// end root
)
public class Module extends BizComponent implements IModule<Module> {
	@ManyToOne
	protected Module parent;

	@OneToMany(mappedBy = "parent")
	protected List<Module> children;

	@ManyToOne
	private DemsyDS dataSource;

	protected String path;

	protected Upload logo;

	protected Upload image;

	protected int type;

	@ManyToOne
	protected SFTSystem refSystem;

	// 冗余数据：用于查询过滤相关模块
	@BzFld(name = "业务系统扩展类")
	protected String refSystemExtends;

	// 冗余数据：用于查询过滤相关模块
	@BzFld(name = "业务系统类")
	protected String refSystemClass;

	// 升级自... 或 从...升级而来
	protected Long upgradeFrom;

	@Column(length = 255)
	protected String refActions;

	private Boolean hidden;

	public Boolean getHidden() {
		return hidden;
	}

	public void setTemplate(String value) {
		this.set("template", value);
	}

	@Column(length = 256)
	public String getTemplate() {
		return get("template");
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Upload getLogo() {
		return logo;
	}

	public void setLogo(Upload logo) {
		this.logo = logo;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public String getRefActions() {
		return refActions;
	}

	public void setRefSystem(IBizSystem system) {
		this.refSystem = (SFTSystem) system;
		if (system != null) {
			IBizSystem sys = bizEngine.getSystem(system.getId());
			if (sys != null) {
				refSystemExtends = sys.getExtendClass();
				refSystemClass = sys.getMappingClass();
				if (refSystemClass != null)
					refSystemClass = refSystemClass.substring(refSystemClass.lastIndexOf(".") + 1);
			}
		}
	}

	public void setRefActions(String refActions) {
		this.refActions = refActions;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getUpgradeFrom() {
		return upgradeFrom;
	}

	public void setUpgradeFrom(Long upgradeFrom) {
		this.upgradeFrom = upgradeFrom;
	}

	public String getSoftName() {
		if (moduleEngine != null) {
			IDemsySoft soft = moduleEngine.getSoft(softID);
			if (soft != null)
				return soft.getName();
		}
		return "" + softID;
	}

	public boolean isHidden() {
		return hidden != null && hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Module getParent() {
		return parent;
	}

	public void setParent(Module parentModule) {
		this.parent = parentModule;
	}

	public DemsyDS getDataSource() {
		return dataSource;
	}

	public void setDataSource(DemsyDS dataSource) {
		this.dataSource = dataSource;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	@Override
	public Long getRefID() {
		switch (type) {
		case TYPE_BIZ:
			return this.refSystem.getId();
			// case IModule.TYPE_WEB:
			// return this.refWebID;

		}

		return 0l;
	}

	public String getRefName() {
		switch (type) {
		case TYPE_BIZ:
			return bizEngine.getSystem(refSystem.getId()).getName();

		}

		return "";
	}

	public SFTSystem getRefSystem() {
		return refSystem;
	}
}
