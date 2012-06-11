package com.kmetop.demsy.comlib.impl.base.ebusiness.product;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.MultiUpload;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.eshop.IProductModel;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "产品型号", code = IProductModel.SYS_CODE, orderby = 4,//
actions = { @BzAct(name = "添加型号", typeCode = TYPE_BZFORM_NEW, mode = "c"),//
		@BzAct(jsonData = "CommonBizAction.data.js") //
},//
groups = { //
@BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(property = "product"),//
		@BzFld(name = "型号名称", property = "name", mode = "c:M e:M v:S *:N"),//
		@BzFld(name = "型号编码", property = "code", mode = "c:E e:E v:S *:N"),//
		@BzFld(property = "image"),//
		@BzFld(property = "images"),//
		@BzFld(property = "spec"), //
		@BzFld(name = "型号描述", property = "desc", type = "RichText", mode = "c:E e:E *:N v:S", gridField = false) //
}) }// end groups
)
public class ProductModel extends BizComponent implements IProductModel {
	@ManyToOne
	@BzFld(name = "产品名称", mode = "c:M e:M bu:E v:S *:N", disabledNavi = true, masterMapping = true)
	protected Product product;

	@BzFld(name = "型号图片", uploadType = "*.jpg;*.gif;*.png;*.bmp", mode = "c:E e:E v:S *:N")
	protected Upload image;

	@BzFld(name = "型号图库", uploadType = "*.jpg;*.gif;*.png;*.bmp;*.swf;*.flv;", mode = "c:E e:E v:S *:N", gridField = false)
	protected MultiUpload images;

	@BzFld(name = "产品规格", mode = "c:E e:E v:S *:N")
	protected String spec;

	@BzFld(name = "产品原价", mode = "c:E e:E v:S *:N", pattern = "#,##0.00")
	protected Double oldPrice;

	@BzFld(name = "产品现价", mode = "c:E e:E v:S *:N", pattern = "#,##0.00")
	protected Double price;

	@BzFld(name = "网购许可", mode = "c:E e:E v:S *:N", disabledNavi = true, options = "1:允许网购,0:禁止网购")
	protected byte allowBuy;

	@BzFld(name = "库存数量", mode = "c:E e:E v:S *:N")
	protected Integer stockNum;

	@BzFld(name = "销售数量", mode = "c:E e:E v:S *:N")
	protected Integer saleNum;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public MultiUpload getImages() {
		return images;
	}

	public void setImages(MultiUpload images) {
		this.images = images;
	}
}
