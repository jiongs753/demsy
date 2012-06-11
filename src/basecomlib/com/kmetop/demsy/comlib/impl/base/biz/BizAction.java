package com.kmetop.demsy.comlib.impl.base.biz;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_ACTION;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_ACTION;
import static com.kmetop.demsy.comlib.LibConst.ORDER_BZUDF_ACTION;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.IBizAction;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.base.BaseAction;
import com.kmetop.demsy.comlib.impl.base.lib.ActionLib;
import com.kmetop.demsy.comlib.impl.sft.system.SFTSystem;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "业务操作", code = BIZSYS_BZUDF_ACTION, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_BZUDF_ACTION, buildin = true//
, actions = { @BzAct(name = "新增操作", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "操作名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "操作编号", property = "code") //
		, @BzFld(name = "人工顺序", property = "orderby") //
		, @BzFld(name = "业务系统", property = "system", refrenceSystem = BIZSYS_BZUDF_SYSTEM, masterMapping = true, mode = "c:M e:M") //
		, @BzFld(name = "操作组件", property = "actionLib", disabledNavi = true, refrenceSystem = BIZSYS_DEMSY_LIB_ACTION) //
		, @BzFld(name = "模式编码", property = "mode", desc = "用于识别执行该操作时各个字段的显示模式") //
		, @BzFld(name = "类型代码", property = "typeCode", desc = "该业务代码用于识别操作类型") //
		, @BzFld(name = "业务插件", property = "plugin", desc = "执行操作时将同时调用业务插件中的方法") //
		, @BzFld(name = "按钮徽标", property = "logo", uploadType = "*.jpg;*.gif;*.png") //
		, @BzFld(name = "按钮图片", property = "image", uploadType = "*.jpg;*.gif;*.png") //
		, @BzFld(name = "窗体模版", property = "template", desc = "用于展现业务操作界面") //
		, @BzFld(name = "成功提示", property = "info", desc = "执行业务操作成功后，弹出这里设置的提示信息") //
		, @BzFld(name = "警告提示", property = "warn", desc = "执行业务操作时，遇到警告则提示该信息") //
		, @BzFld(name = "错误提示", property = "error", desc = "执行业务操作出错时，弹出这里设置的提示信息") //
		, @BzFld(name = "参数设置", property = "params", desc = "参数格式: prop1=value1&prop2=value2...&propn=valuen") //
		, @BzFld(property = "targetUrl") //
		, @BzFld(property = "targetWindow") //
		, @BzFld(name = "停用状态", property = "disabled", disabledNavi = true, options = "1:停用,0:启用") //
		, @BzFld(name = "操作描述", property = "desc") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class BizAction extends BaseAction implements IBizAction {
	@ManyToOne
	private BizAction parentAction;

	@ManyToOne
	protected SFTSystem system;

	@ManyToOne
	protected ActionLib actionLib;

	@OneToMany(mappedBy = "parentAction")
	protected List<BizAction> children;

	public ActionLib getActionLib() {
		return actionLib;
	}

	public void setActionLib(ActionLib refAction) {
		this.actionLib = refAction;
	}

	public Integer getTypeCode() {
		if ((super.getTypeCode() == null || super.getTypeCode() <= 0) && getActionLib() != null) {
			return getActionLib().getTypeCode();
		}
		return super.getTypeCode();
	}

	public String getMode() {
		if (Str.isEmpty(super.getMode()) && getActionLib() != null) {
			return getActionLib().getMode();
		}
		return super.getMode();
	}

	public String getTemplate() {
		if (Str.isEmpty(super.getTemplate()) && getActionLib() != null) {
			return getActionLib().getTemplate();
		}
		return super.getTemplate();
	}

	public String getPlugin() {
		if (Str.isEmpty(super.getPlugin()) && getActionLib() != null) {
			return getActionLib().getPlugin();
		}
		return super.getPlugin();
	}

	public Upload getLogo() {
		if (Str.isEmpty(logo == null ? null : logo.toString()) && getActionLib() != null) {
			return getActionLib().getLogo();
		}
		return super.getLogo();
	}

	public Upload getImage() {
		if (Str.isEmpty(image == null ? null : image.toString()) && getActionLib() != null) {
			return getActionLib().getImage();
		}
		return super.getImage();
	}

	public SFTSystem getSystem() {
		return system;
	}

	public void setSystem(IBizSystem system) {
		this.system = (SFTSystem) system;
	}

	public String getSystemName() {
		return this.get("systemName");
	}

	public void setSystemName(String system) {
		this.set("systemName", system);
	}

	public BizAction getParentAction() {
		return parentAction;
	}

	public void setParentAction(BizAction parent) {
		this.parentAction = parent;
	}

	public String getParams() {
		if (Str.isEmpty(super.getParams()) && getActionLib() != null)
			return getActionLib().getParams();

		return params;
	}

	public String getTargetWindow() {
		if (Str.isEmpty(super.getTargetWindow()) && getActionLib() != null)
			return getActionLib().getTargetWindow();

		return targetWindow;
	}

	public List<BizAction> getChildren() {
		return children;
	}

	public void setChildren(List<BizAction> children) {
		this.children = children;
	}

}
