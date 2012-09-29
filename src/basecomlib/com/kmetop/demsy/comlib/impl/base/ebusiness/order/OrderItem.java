package com.kmetop.demsy.comlib.impl.base.ebusiness.order;

import static com.kmetop.demsy.biz.BizConst.*;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.comlib.eshop.IOrderItem;
import com.kmetop.demsy.comlib.eshop.IProduct;
import com.kmetop.demsy.comlib.eshop.IProductCatalog;
import com.kmetop.demsy.comlib.eshop.IProductOperator;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.impl.base.ebusiness.product.Product;
import com.kmetop.demsy.comlib.impl.base.ebusiness.product.ProductCatalog;
import com.kmetop.demsy.comlib.impl.base.ebusiness.product.ProductOperator;

@Entity
@BzSys(name = "订单清单", code = IOrderItem.SYS_CODE, orderby = 2,//
actions = { @BzAct(name = "备货", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu1", plugin = "com.kmetop.demsy.plugins.eshop.OrderSended")//
		, @BzAct(name = "打折", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu", plugin = "com.kmetop.demsy.plugins.eshop.OrderAdjustCast")//
		, @BzAct(name = "详情", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
// , @BzAct(name = "删除条目", typeCode = TYPE_BZ_DEL, mode = "d") //
},//
groups = { //
@BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(property = "order", gridOrder = 1) //
		, @BzFld(name = "商品名称", property = "name", mode = "*:N v:S", gridOrder = 2)//
		, @BzFld(name = "商品编码", property = "code", mode = "*:N v:S", gridOrder = 3)//
		, @BzFld(property = "productCatalog")//
		, @BzFld(property = "productOperator") //

}), @BzGrp(name = "订单费用", code = "stat",//
fields = { @BzFld(property = "price", gridOrder = 4)//
		, @BzFld(property = "amount", gridOrder = 5)//
		, @BzFld(property = "discount", gridOrder = 6)//
		, @BzFld(property = "subtotal", gridOrder = 7) //
		, @BzFld(property = "status", gridOrder = 8) //
}), // @BzGrp
		@BzGrp(name = "其他属性", code = "other",//
		fields = {
				// 其他信息
				@BzFld(name = "下单时间", property = "created", mode = "*:N v:S"), //
				@BzFld(name = "更新时间", property = "updated", mode = "*:N v:S"), //
				@BzFld(name = "下单帐号", property = "createdBy", mode = "*:N v:S"), //
				@BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:S"), //
				@BzFld(name = "下单IP", property = "createdIP", mode = "*:N v:S") //
		}) // @BzGrp
}// end groups
)
public class OrderItem extends BizComponent implements IOrderItem {
	@ManyToOne
	@BzFld(name = "订单", mode = "*:N v:S", disabledNavi = true, masterMapping = true)
	protected Order order;

	@ManyToOne
	@BzFld(name = "产品类别", mode = "*:N v:S")
	protected ProductCatalog productCatalog;

	@ManyToOne
	@BzFld(name = "运营商", mode = "*:N v:S")
	protected ProductOperator productOperator;

	@ManyToOne
	@BzFld(name = "产品", mode = "*:N v:S", disabledNavi = true)
	protected Product product;

	@BzFld(name = "单价(元)", mode = "*:N v:S", pattern = "#,##0.00")
	protected double price;

	@BzFld(name = "数量", mode = "*:N v:S")
	protected int amount;

	@BzFld(name = "折扣", mode = "*:N v:S bu:E")
	protected Double discount;

	@BzFld(name = "小计(元)", mode = "*:N v:S bu:E", pattern = "#,##0.00")
	protected double subtotal;

	@Column(length = 64)
	@BzFld(name = "IP地址", mode = "*:N v:S")
	protected String createdIP;

	// 0:购物车(未下单),9:已取消
	@BzFld(name = "状态", mode = "*:N v:S", options = "2:已下单(待备货...),3:已备货")
	protected byte status;

	public Order getOrder() {
		return order;
	}

	public void setOrder(IOrder order) {
		this.order = (Order) order;
	}

	public ProductCatalog getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(IProductCatalog catalog) {
		this.productCatalog = (ProductCatalog) catalog;
	}

	public ProductOperator getProductOperator() {
		return productOperator;
	}

	public void setProductOperator(IProductOperator vender) {
		this.productOperator = (ProductOperator) vender;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String createdIP) {
		this.createdIP = createdIP;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(IProduct product) {
		this.product = (Product) product;
	}
}
