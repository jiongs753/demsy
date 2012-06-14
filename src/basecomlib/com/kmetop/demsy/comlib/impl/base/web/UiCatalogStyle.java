package com.kmetop.demsy.comlib.impl.base.web;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_STYLE;
import static com.kmetop.demsy.comlib.LibConst.ORDER_UIUDF_CATALOG_STYLE;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "样式管理", code = BIZSYS_UIUDF_STYLE, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_UIUDF_CATALOG_STYLE//
, layout = 1, actions = {
		@BzAct(name = "新增样式", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.web.SaveCatalogStyle")//
		// , @BzAct(name = "批量修改", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu")//
		,
		@BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = "com.kmetop.demsy.plugins.web.SaveCatalogStyle") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "catalog") //
		, @BzFld(property = "parent") //
		, @BzFld(property = "name", name = "样式名称", mode = "c:M e:M", gridOrder = 1)//
		, @BzFld(property = "code", name = "样式编号", mode = "c:E e:E")//
		, @BzFld(property = "created", name = "创建时间", mode = "*:N v:S") //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:N v:S") //
		, @BzFld(property = "detailState") //
		, @BzFld(property = "usage") //
}), // end groups
		@BzGrp(name = "样式编辑器", code = "cssDesigner"//
		, fields = { @BzFld(property = "items") //
		}), // end groups
		@BzGrp(name = "整体样式", code = "box"//
		, fields = { @BzFld(property = "box") //
		}), // end groups
		@BzGrp(name = "顶部", code = "top"//
		, fields = { @BzFld(property = "top") //
		}), // end groups
		@BzGrp(name = "顶部-链接", code = "topLink"//
		, fields = { @BzFld(property = "topLink") //
		}), // end groups
		@BzGrp(name = "顶部-左", code = "topL"//
		, fields = { @BzFld(property = "topL") //
		}), // end groups
		@BzGrp(name = "顶部-标题", code = "topT"//
		, fields = { @BzFld(property = "topT") //
		}), // end groups
		@BzGrp(name = "顶部-右", code = "topR"//
		, fields = { @BzFld(property = "topR") //
		}), // end groups
		@BzGrp(name = "内容", code = "data"//
		, fields = { @BzFld(property = "data") //
		}), // end groups
		@BzGrp(name = "内容-链接", code = "dataLink"//
		, fields = { @BzFld(property = "dataLink") //
		}), // end groups
		@BzGrp(name = "内容-条目", code = "item"//
		, fields = { @BzFld(property = "item") //
		}), // end groups
		@BzGrp(name = "条目-左(上)", code = "itemL"//
		, fields = { @BzFld(property = "itemL") //
		}), // end groups
		@BzGrp(name = "条目-标题", code = "itemT"//
		, fields = { @BzFld(property = "itemT") //
		}), // end groups
		@BzGrp(name = "条目-右(下)", code = "itemR"//
		, fields = { @BzFld(property = "itemR") //
		}), // end groups
		@BzGrp(name = "底部", code = "bottom"//
		, fields = { @BzFld(property = "bottom") //
		}), // end groups
		@BzGrp(name = "底部-链接", code = "bottomLink"//
		, fields = { @BzFld(property = "bottomLink") //
		}), // end groups
		@BzGrp(name = "底部-左", code = "bottomL"//
		, fields = { @BzFld(property = "bottomL") //
		}), // end groups
		@BzGrp(name = "底部-标题", code = "bottomT"//
		, fields = { @BzFld(property = "bottomT") //
		}), // end groups
		@BzGrp(name = "底部-右", code = "bottomR"//
		, fields = { @BzFld(property = "bottomR") //
		}) // end groups
//
})
public class UiCatalogStyle extends BaseStyle {

	@ManyToOne
	@BzFld(name = "界面分类", disabledNavi = true, groupBy = true, mode = "c:M e:M")
	@Prop("uiCatalog")
	protected UiCatalog catalog;

	@ManyToOne
	@BzFld(name = "上级样式", mode = "bu:E")
	protected UiCatalogStyle parent;

	@OneToMany(mappedBy = "parent")
	protected List<UiCatalogStyle> children;

	public UiCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(UiCatalog uiCatalog) {
		this.catalog = uiCatalog;
	}

	public UiCatalogStyle getParent() {
		return parent;
	}

	public void setParent(UiCatalogStyle parent) {
		this.parent = parent;
	}

	public List<UiCatalogStyle> getChildren() {
		return children;
	}

	public void setChildren(List<UiCatalogStyle> children) {
		this.children = children;
	}

}
