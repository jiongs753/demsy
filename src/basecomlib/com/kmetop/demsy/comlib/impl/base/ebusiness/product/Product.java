package com.kmetop.demsy.comlib.impl.base.ebusiness.product;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.MultiUpload;
import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.biz.field.SubSystem;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.eshop.IProduct;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "产品信息管理", code = IProduct.SYS_CODE, orderby = 4,//
actions = {
//
		@BzAct(name = "添加产品", typeCode = TYPE_BZFORM_NEW, mode = "c"),//
		@BzAct(name = "调整分类", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu"),//
		@BzAct(name = "批量推荐", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu2"),//
		// @BzAct(name = "调整销售数据", typeCode = TYPE_BZFORM_EDIT, mode = "e1"),//
		// @BzAct(name = "批量调整运营商", typeCode = TYPE_BZFORM_EDIT_N, mode =
		// "bu1"),//
		// @BzAct(name = "批量调整发货点", typeCode = TYPE_BZFORM_EDIT_N, mode =
		// "bu3"),//
		// @BzAct(name = "批量设置网购", typeCode = TYPE_BZFORM_EDIT_N, mode =
		// "bu4"),//
		@BzAct(jsonData = "CommonBizAction.data.js") //
},//
groups = { @BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(property = "catalog", gridOrder = 1)//
		, @BzFld(name = "产品名称", property = "name", mode = "c:M e:M v:S *:N", gridOrder = 2)//
		, @BzFld(name = "产品编码", property = "code", mode = "c:E e:E v:S *:N")//
		, @BzFld(property = "operator")//
		, @BzFld(property = "storage")//
		, @BzFld(property = "image")//
		, @BzFld(property = "spec") //
		, @BzFld(property = "recommend") //
		, @BzFld(property = "onlineDate") //
		, @BzFld(property = "oldPrice", gridOrder = 4)//
		, @BzFld(property = "price", gridOrder = 5)//
		, @BzFld(property = "allowBuy", gridOrder = 3)//
		, @BzFld(property = "saleNum", gridOrder = 6)//
		, @BzFld(property = "stockNum", gridOrder = 7) //
		, @BzFld(property = "keywords") //
		, @BzFld(name = "产品说明", property = "desc", mode = "c:E e:E v:S *:N") //
		, @BzFld(name = "人工顺序", property = "orderby", uiTemplate = "ui.widget.field.Spinner", mode = "v:S *:N", gridOrder = 8) //
}), @BzGrp(name = "产品详情", code = "content",//
fields = { @BzFld(property = "content") //
}), @BzGrp(name = "产品特征", code = "attributes",//
fields = { @BzFld(property = "attributes") //
}), @BzGrp(name = "产品型号", code = "models",//
fields = { @BzFld(property = "models") //
}), @BzGrp(name = "产品图库", code = "images",//
fields = { @BzFld(property = "images") //
}), @BzGrp(name = "其他属性", code = "other",//
fields = { @BzFld(property = "clickNum", gridOrder = 8)//
		, @BzFld(property = "commentNum")//
		, @BzFld(name = "创建时间", property = "created", mode = "*:N v:S") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:N v:S") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:S") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:S") //
}) // @BzGrp
}// end groups
)
public class Product extends BizComponent implements IProduct {
	@ManyToOne
	@BzFld(name = "产品类别", mode = "c:M e:M bu:E v:S *:N")
	protected ProductCatalog catalog;

	@OneToMany(mappedBy = "product")
	@BzFld(name = "产品特征", uploadType = "*.jpg;*.gif;*.png;*.bmp", isTransient = true, gridField = false, mode = "c:E e:E v:S *:N", refrenceFields = "name,image,desc")
	protected SubSystem<ProductAttribute> attributes;

	@OneToMany(mappedBy = "product")
	@BzFld(name = "产品型号", uploadType = "*.jpg;*.gif;*.png;*.bmp", isTransient = true, gridField = false, mode = "c:E e:E v:S *:N", refrenceFields = "name,image,desc")
	protected SubSystem<ProductModel> models;

	@ManyToOne
	@BzFld(name = "发货地址", mode = "c:E e:E bu3:E v:S *:N", disabledNavi = true)
	protected ProductDeliver storage;

	@ManyToOne
	@BzFld(name = "运营商", mode = "c:E e:E bu1:E v:S *:N", disabledNavi = true)
	protected ProductOperator operator;

	@BzFld(name = "推荐标志", options = "1:推荐,0:取消推荐", mode = "c:E e:E bu2:E v:S *:N")
	protected Boolean recommend;

	@BzFld(name = "产品图片", uploadType = "*.jpg;*.gif;*.png;*.bmp", mode = "c:E e:E v:S *:N")
	protected Upload image;

	@BzFld(name = "产品图库", uploadType = "*.jpg;*.gif;*.png;*.bmp;*.swf;*.flv;*.zip", mode = "c:E e:E v:S *:N", gridField = false)
	protected MultiUpload images;

	@BzFld(name = "产品规格", mode = "c:E e:E v:S *:N")
	protected String spec;

	@BzFld(name = "上架时间", pattern = "yyyy-MM-dd", mode = "c:E e:E v:S *:N")
	protected Date onlineDate;

	@BzFld(name = "产品详情", gridField = false, mode = "c:E e:E v:S *:N")
	protected RichText content;

	@Column(length = 256)
	@BzFld(name = "关键字", mode = "c:E e:E v:S *:N")
	protected String keywords;

	/*
	 * 销售数据
	 */
	@BzFld(name = "产品原价", mode = "c:E e:E e1:E v:S *:N", pattern = "#,##0.00")
	protected Double oldPrice;

	@BzFld(name = "产品现价", mode = "c:E e:E e1:E v:S *:N", pattern = "#,##0.00")
	protected Double price;

	@BzFld(name = "产品状态", mode = "c:E e:E e1:E bu4:E v:S *:N", options = "1:允许网购,0:禁止网购,2:已下架")
	protected byte allowBuy;

	@BzFld(name = "库存数量", mode = "c:E e:E e1:E v:S *:N")
	protected Integer stockNum;

	@BzFld(name = "销售数量", mode = "c:E e:E e1:E v:S *:N")
	protected Integer saleNum;

	// 统计信息收集=====================
	@BzFld(name = "点击次数", mode = "*:N v:S")
	protected Integer clickNum;

	@BzFld(name = "评论次数", mode = "*:N v:S")
	protected Integer commentNum;

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public ProductCatalog getCatalog() {
		return catalog;
	}

	public ProductOperator getOperator() {
		return operator;
	}

	public Upload getImage() {
		return image;
	}

	@Transient
	private Double nowPrice;

	/**
	 * 获取产品现价：如果有促销活动则返回促销价，否则返回正常网购价
	 */
	public Double getNowPrice() {
		if (nowPrice == null) {
			// 允许网购
			if (this.allowBuy == 1) {
				// 促销价
				nowPrice = getBarginPrice();

				// 正常价格
				if (nowPrice == null) {
					nowPrice = getPrice();
				}

				// 库存不足
				if (this.stockNum != null && this.stockNum <= 0)
					nowPrice = 0.0;

				if (nowPrice == null)
					nowPrice = 0.0;
			} else {
				nowPrice = 0.0;
			}
		}

		return nowPrice == 0.0 ? null : nowPrice;
	}

	/*
	 * 获取 产品促销价
	 */
	public Double getBarginPrice() {
		// TODO:
		return null;
	}

	public String getBarginNote() {
		// TODO:
		return "";
	}

	public Double getPrice() {
		return price;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCatalog(ProductCatalog catalog) {
		this.catalog = catalog;
	}

	public void setOperator(ProductOperator vender) {
		this.operator = vender;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	public RichText getContent() {
		return content;
	}

	public void setContent(RichText content) {
		this.content = content;
	}

	public Double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public Double getBalance() {
		if (oldPrice != null && getNowPrice() != null)
			return oldPrice - getNowPrice();

		return null;
	}

	public ProductDeliver getStorage() {
		return storage;
	}

	public void setStorage(ProductDeliver logisticsAddress) {
		this.storage = logisticsAddress;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getStockNum() {
		return stockNum;
	}

	public void setStockNum(Integer stockNum) {
		this.stockNum = stockNum;
	}

	public Date getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}

	public byte getAllowBuy() {
		return allowBuy;
	}

	public void setAllowBuy(byte allowBuy) {
		this.allowBuy = allowBuy;
	}

	public MultiUpload getImages() {
		return images;
	}

	public void setImages(MultiUpload images) {
		this.images = images;
	}

	// @Override
	// public String getDesc() {
	// if (Str.isEmpty(this.desc)) {
	// String str = this.content == null ? "" : content.toString();
	// return Str.substr(Str.escapeHTML(str).trim(), 50);
	// }
	//
	// return Str.substr(Str.escapeHTML(desc).trim(), 50);
	// }

	public SubSystem<ProductAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(SubSystem<ProductAttribute> attributes) {
		this.attributes = attributes;
	}

	public SubSystem<ProductModel> getModels() {
		return models;
	}

	public void setModels(SubSystem<ProductModel> models) {
		this.models = models;
	}
}
