package com.kmetop.demsy.comlib.impl.base.web;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.ORDER_UIUDF_THEME_STYLE;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.ui.IStyle;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "主题样式", catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_UIUDF_THEME_STYLE//
, actions = { @BzAct(name = "新增样式", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "theme") //
		, @BzFld(property = "style") //
		, @BzFld(property = "catalog") //
		, @BzFld(property = "created", name = "创建时间", mode = "*:N v:S") //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:N v:S") //
		, @BzFld(property = "desc", name = "行内样式", gridField = false) //
		, @BzFld(property = "detailState") //
		, @BzFld(property = "usage") //
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
})
public class UiThemeStyle extends BaseStyle {
	@ManyToOne
	@BzFld(name = "界面分类", groupBy = true, mode = "c:M e:M")
	@Prop("uiCatalog")
	protected UiCatalog catalog;

	@ManyToOne
	@BzFld(name = "界面主题", mode = "c:M e:M", masterMapping = true, cascadeMode = "catalog:*:catalog")
	@Prop("uiTheme")
	protected UiCatalogTheme theme;

	@ManyToOne
	@BzFld(name = "界面样式", mode = "c:M e:M", cascadeMode = "catalog:*:catalog")
	@Prop("uiStyle")
	protected UiCatalogStyle style;

	public UiCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(UiCatalog uiCatalog) {
		this.catalog = uiCatalog;
	}

	public UiCatalogTheme getTheme() {
		return theme;
	}

	public void setTheme(UiCatalogTheme uiTheme) {
		this.theme = uiTheme;
	}

	public UiCatalogStyle getStyle() {
		return style;
	}

	public void setStyle(UiCatalogStyle uiStyle) {
		this.style = uiStyle;
	}

	@Override
	public IStyle getParent() {
		return null;
	}

	@Override
	public FakeSubSystem<StyleItem> getItems() {
		return null;
	}
}
