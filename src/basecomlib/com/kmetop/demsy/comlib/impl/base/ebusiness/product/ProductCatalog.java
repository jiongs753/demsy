package com.kmetop.demsy.comlib.impl.base.ebusiness.product;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EXPORT_XLS;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_IMPORT_XLS;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.biz.field.SubSystem;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.eshop.IProductCatalog;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.impl.sft.system.AbstractSystemData;

@Entity
@BzSys(name = "产品分类设置", code = IProductCatalog.SYS_CODE, orderby = 3,//
actions = {
//
		@BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c"),//
		@BzAct(jsonData = "CommonBizAction.data.js") //
		, @BzAct(name = "导出到XLS", typeCode = TYPE_BZFORM_EXPORT_XLS, mode = "out") //
		, @BzAct(name = "从XLS导入", typeCode = TYPE_BZFORM_IMPORT_XLS, mode = "in") //
},//
groups = {//
@BzGrp(name = "基本信息", code = "basic",//
fields = {
// 基本信息
		@BzFld(name = "名称", property = "name", mode = "c:M e:M"),//
		@BzFld(name = "编码", property = "code"),//
		@BzFld(property = "parent"),//
		@BzFld(property = "image") //
}), @BzGrp(name = "下级分类", code = "chidlren",//
fields = { @BzFld(property = "chidlren") //
}), @BzGrp(name = "产品特征", code = "customFields",//
fields = { @BzFld(property = "customFields") //
// }), @BzGrp(name = "产品列表", code = "products",//
// fields = { @BzFld(property = "products") //
}), @BzGrp(name = "其他信息", code = "others",//
fields = { @BzFld(name = "创建时间", property = "created", mode = "*:N v:P"), //
		@BzFld(name = "更新时间", property = "updated", mode = "*:N v:P"), //
		@BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:P"), //
		@BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:P") //
}) // @BzGrp
}// end groups
)
public class ProductCatalog extends BizComponent implements IProductCatalog {
	@ManyToOne
	@BzFld(name = "上级分类")
	protected ProductCatalog parent;

	// @OneToMany(mappedBy = "catalog")
	// @BzFld(name = "产品列表", uploadType = "*.jpg;*.gif;*.png;*.bmp", isTransient
	// = true, gridField = false, mode = "c:E e:E v:S *:N", refrenceFields =
	// "name,image,price,keywords")
	// protected SubSystem<Product> products;

	@BzFld(name = "分类图片", uploadType = "*.jpg;*.gif;*.png;")
	protected Upload image;

	@OneToMany(mappedBy = "parent")
	@BzFld(name = "下级分类", isTransient = true, gridField = false, mode = "c:E e:E v:S *:N", refrenceFields = "name,code,image")
	protected SubSystem<ProductCatalog> chidlren;

	@BzFld(name = "产品特征", gridField = false, refrenceFields = "propName,name,mode,type,precision")
	protected FakeSubSystem<AbstractSystemData> customFields;

	public ProductCatalog getParent() {
		return parent;
	}

	public Upload getImage() {
		return image;
	}

	public void setParent(ProductCatalog parent) {
		this.parent = parent;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	@Override
	public FakeSubSystem getCustomFields() {
		return customFields;
	}

	public void setCustomFields(FakeSubSystem customFields) {
		this.customFields = customFields;
	}

	// public SubSystem<Product> getProducts() {
	// return products;
	// }
	//
	// public void setProducts(SubSystem<Product> products) {
	// this.products = products;
	// }

	public SubSystem<ProductCatalog> getChidlren() {
		return chidlren;
	}

	public void setChidlren(SubSystem<ProductCatalog> chidlren) {
		this.chidlren = chidlren;
	}

}
