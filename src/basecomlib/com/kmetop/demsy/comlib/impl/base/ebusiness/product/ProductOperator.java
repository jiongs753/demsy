package com.kmetop.demsy.comlib.impl.base.ebusiness.product;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.eshop.IProductOperator;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "运营机构设置", code = IProductOperator.SYS_CODE, orderby = 2,//
actions = {
//
		@BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c"),//
		@BzAct(jsonData = "CommonBizAction.data.js") //
},//
groups = {//
@BzGrp(name = "基本信息", code = "basic",//
fields = {
// 基本信息
		@BzFld(name = "名称", property = "name", mode = "c:M e:M"),//
		@BzFld(name = "编码", property = "code"),//
		// 其他信息
		@BzFld(name = "排序", property = "orderby", uiTemplate = "ui.widget.field.Spinner"), //
		@BzFld(name = "描述", property = "desc"), //
		@BzFld(name = "创建时间", property = "created", mode = "*:N v:P"), //
		@BzFld(name = "更新时间", property = "updated", mode = "*:N v:P"), //
		@BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:P"), //
		@BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:P") //
}) // @BzGrp
}// end groups
)
public class ProductOperator extends BizComponent implements IProductOperator {
}
