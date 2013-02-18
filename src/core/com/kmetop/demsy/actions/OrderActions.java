package com.kmetop.demsy.actions;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.comlib.eshop.IOrder.STATUS_TITLES;
import static com.kmetop.demsy.comlib.eshop.IOrder.STATUS_TRADE_FINISHED;
import static com.kmetop.demsy.comlib.eshop.IOrder.STATUS_TRADE_SUCCESS;
import static com.kmetop.demsy.comlib.eshop.IOrder.STATUS_WAIT_BUYER_CONFIRM_GOODS;
import static com.kmetop.demsy.comlib.eshop.IOrder.STATUS_WAIT_BUYER_CONFIRM_REFUND;
import static com.kmetop.demsy.comlib.eshop.IOrder.STATUS_WAIT_BUYER_PAY;
import static com.kmetop.demsy.comlib.eshop.IOrder.STATUS_WAIT_SELLER_SEND_GOODS;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Mirror;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizConst;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.common.IContact;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.eshop.ILogistics;
import com.kmetop.demsy.comlib.eshop.ILogisticsItem;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.comlib.eshop.IOrderItem;
import com.kmetop.demsy.comlib.eshop.IProduct;
import com.kmetop.demsy.comlib.eshop.IProductDeliver;
import com.kmetop.demsy.comlib.eshop.IProductOperator;
import com.kmetop.demsy.comlib.impl.base.ebusiness.order.Logistics;
import com.kmetop.demsy.comlib.impl.base.ebusiness.order.LogisticsItem;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.config.SoftConfigManager;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.modules.alipay.AlipayNotify;
import com.kmetop.demsy.modules.alipay.AlipayService;
import com.kmetop.demsy.modules.alipay.AlipaySubmit;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.nutz.DemsyAdaptor;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.OrmCallback;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.security.ILogin;

@Ok("json")
@Fail("json")
@AdaptBy(type = DemsyAdaptor.class)
public class OrderActions extends ModuleActions implements BizConst, MvcConst {
	public static final String SESSION_KEY_CART = "Order.Cart";

	public static final String SESSION_KEY_CART_SELECTED = "Order.Cart.Selected";

	@At(URL_BZ_ORDER_ALIPAYNOTIFY)
	@Ok("void")
	public void alipaynotify() {
		HttpServletRequest request = Demsy.me().request();
		HttpServletResponse response = Demsy.me().response();

		Map<String, String> params = new HashMap<String, String>();
		String trade_no = request.getParameter("trade_no"); // 支付宝交易号
		String order_no = request.getParameter("out_trade_no"); // 获取订单号

		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", -1);
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = null;
		try {
			out = response.getWriter();

			// 获取支付宝POST过来反馈信息
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}

			String trade_status = request.getParameter("trade_status"); // 交易状态

			// 查询订单
			Class ordClass = bizEngine.getSystemClass(IOrder.SYS_CODE);
			IOrm orm = Demsy.orm();
			IDemsySoft soft = Demsy.me().getSoft();
			IOrder order = (IOrder) orm.load(ordClass, Expr.eq(LibConst.F_TIME_ID, order_no).and(Expr.eq(LibConst.F_SOFT_ID, soft.getId())));

			if (order == null) {
				log.infof("支付宝通知：订单不存在！订单号(%s),交易号(%s)", order_no, trade_no);
			}

			if (AlipayNotify.verify(params)) {// 验证成功
				if (trade_status.equals("WAIT_BUYER_PAY")) {
					// 该判断表示买家已在支付宝交易管理中产生了交易记录，但没有付款

					if (order != null)
						log.infof("支付宝通知：未处理！订单号(%s),交易号(%s),交易状态(%s),订单当前状态(%s)", order_no, trade_no, trade_status, STATUS_TITLES[order.getStatus()]);

					out.println("success"); // 请不要修改或删除
				} else if (trade_status.equals("WAIT_SELLER_SEND_GOODS")) {
					// 该判断表示买家已在支付宝交易管理中产生了交易记录且付款成功，但卖家没有发货

					if (order != null) {
						log.infof("支付宝通知：处理订单... 订单号(%s),交易号(%s),交易状态(%s),订单当前状态(%s),订单下一状态(%s)", order_no, trade_no, trade_status, STATUS_TITLES[order.getStatus()],
								STATUS_TITLES[STATUS_WAIT_SELLER_SEND_GOODS]);
						processOrder(order, trade_no, STATUS_WAIT_SELLER_SEND_GOODS);
					}

					out.println("success"); // 请不要修改或删除
				} else if (trade_status.equals("TRADE_SUCCESS")) {//

					if (order != null) {
						// 即时到账接口：交易成功 ——> 等待卖家发货(已付款)
						byte nextStatus = STATUS_TRADE_SUCCESS;
						String service = SoftConfigManager.me().getAlipayPayService();
						if (service.equals("create_direct_pay_by_user")) {
							nextStatus = STATUS_WAIT_SELLER_SEND_GOODS;
						}

						log.infof("支付宝通知：处理订单... 订单号(%s),交易号(%s),交易状态(%s),支付类型(%s),订单当前状态(%s),订单下一状态(%s)", order_no, trade_no, trade_status, service, STATUS_TITLES[order.getStatus()],
								STATUS_TITLES[nextStatus]);
						processOrder(order, trade_no, nextStatus);
					}

					out.println("success"); // 请不要修改或删除

				} else if (trade_status.equals("WAIT_BUYER_CONFIRM_GOODS")) {
					// 该判断表示卖家已经发了货，但买家还没有做确认收货的操作

					if (order != null) {
						log.infof("支付宝通知：处理订单...订单号(%s),交易号(%s),交易状态(%s),订单当前状态(%s),订单下一状态(%s)", order_no, trade_no, trade_status, STATUS_TITLES[order.getStatus()],
								STATUS_TITLES[STATUS_WAIT_BUYER_CONFIRM_GOODS]);
						processOrder(order, trade_no, STATUS_WAIT_BUYER_CONFIRM_GOODS);
					}

					out.println("success"); // 请不要修改或删除
				} else if (trade_status.equals("TRADE_FINISHED")) {
					// 该判断表示买家已经确认收货，这笔交易完成

					if (order != null) {
						log.infof("支付宝通知：处理订单... 订单号(%s),交易号(%s),交易状态(%s),订单当前状态(%s),订单下一状态(%s)", order_no, trade_no, trade_status, STATUS_TITLES[order.getStatus()],
								STATUS_TITLES[STATUS_TRADE_SUCCESS]);
						processOrder(order, trade_no, STATUS_TRADE_FINISHED);
					}

					out.println("success"); // 请不要修改或删除
				} else {

					if (order != null)
						log.infof("支付宝通知：未处理！订单号(%s),交易号(%s),交易状态(%s),订单当前状态(%s)", order_no, trade_no, trade_status, STATUS_TITLES[order.getStatus()]);

					out.println("success"); // 请不要修改或删除
				}
			} else {// 验证失败

				log.infof("支付宝通知：验证失败！订单号(%s),交易号(%s)", order_no, trade_no);

				out.println("fail");
			}
		} catch (Throwable e) {
			log.error(String.format("支付宝通知：处理订单出错！订单号(%s),交易号(%s)", order_no, trade_no), e);
		} finally {
			Iterator<String> keys = params.keySet().iterator();
			StringBuffer logbuf = new StringBuffer("支付宝通知：数据【");
			while (keys.hasNext()) {
				String key = keys.next();
				String value = params.get(key);
				logbuf.append("\n").append(key + "=" + value);
			}
			logbuf.append("\n】");
			log.info(logbuf);
			if (out != null)
				out.close();
		}
	}

	@At(URL_BZ_ORDER_ALIPAYRETURN)
	@Ok("void")
	public void alipayreturn() {
		HttpServletRequest request = Demsy.me().request();
		HttpServletResponse response = Demsy.me().response();

		Map<String, String> params = new HashMap<String, String>();
		String trade_no = request.getParameter("trade_no"); // 支付宝交易号
		String order_no = request.getParameter("out_trade_no"); // 获取订单号

		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", -1);
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = null;
		String show_url = null;
		try {
			out = response.getWriter();

			// 获取支付宝GET过来反馈信息
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}

			// 计算得出通知验证结果
			boolean verify_result = Demsy.me().isLocal() ? true : AlipayNotify.verify(params);

			// 查询订单
			Class ordClass = bizEngine.getSystemClass(IOrder.SYS_CODE);
			IOrm orm = Demsy.orm();
			final IDemsySoft soft = Demsy.me().getSoft();
			final IOrder order = (IOrder) orm.load(ordClass, Expr.eq(LibConst.F_TIME_ID, order_no).and(Expr.eq(LibConst.F_SOFT_ID, soft.getId())));

			if (order == null) {
				log.infof("支付宝返回：订单不存在！订单号(%s),交易号(%s)", order_no, trade_no);
			}

			// 跳转到订单页面
			IModule pmodule = moduleEngine.getModule(soft, bizEngine.getSystem(IOrder.SYS_CODE));
			if (pmodule == null) {
				log.infof("支付宝返回：订单管理模块不存在！订单号(%s),交易号(%s)", order_no, trade_no);
			}

			if (order != null && pmodule != null) {
				show_url = MvcUtil.contextPath(SoftConfigManager.me().getAlipayShowUrl(), pmodule.getId() + ":" + order.getId());
			} else {
				show_url = MvcUtil.contextPath(SoftConfigManager.me().getAlipayShowUrl());
			}

			if (verify_result) {// 验证成功

				if (Demsy.appconfig.isProductMode()) {
					out.println("<script language=\"javascript\">alert('交易成功！交易号：" + trade_no + "');</script>");
				} else {
					if (order != null) {
						log.infof("支付宝返回：开发模式下处理订单... 订单号(%s),交易号(%s),订单当前状态(%s),订单下一状态(%s)", order_no, trade_no, STATUS_TITLES[order.getStatus()], STATUS_TITLES[STATUS_WAIT_SELLER_SEND_GOODS]);
						processOrder(order, trade_no, STATUS_WAIT_SELLER_SEND_GOODS);
					}

					out.println("<script language=\"javascript\">alert('开发模式下测试交易成功！交易号：" + trade_no + "，扣款金额：0.01元');</script>");
				}

				out.println(AlipaySubmit.buildForm(params, show_url + "?", "POST", "Submit"));

			} else {

				out.println("<script language=\"javascript\">alert('交易失败!');</script>");
				out.println(AlipaySubmit.buildForm(params, show_url + "?", "POST", "Submit"));

			}

		} catch (Throwable e) {

			out.println("<script language=\"javascript\">alert('交易出错!" + e.getMessage().replace("'", "\'") + "');</script>");
			if (show_url != null)
				out.println(AlipaySubmit.buildForm(params, show_url + "?", "POST", "Submit"));

			log.error(String.format("支付宝返回：处理订单出错！订单号(%s),交易号(%s)", order_no, trade_no), e);
		} finally {

			Iterator<String> keys = params.keySet().iterator();
			StringBuffer logbuf = new StringBuffer("支付宝返回：数据【");
			while (keys.hasNext()) {
				String key = keys.next();
				String value = params.get(key);
				logbuf.append("\n").append(key + "=" + value);
			}
			logbuf.append("\n】");
			log.info(logbuf);

			if (out != null)
				out.close();
		}
	}

	private void processOrder(final IOrder order, final String trade_no, final byte orderstatus) {
		Demsy.bizSession.run(new OrmCallback() {

			@Override
			public Object invoke(IOrm orm) {
				processOrder(orm, order, trade_no, orderstatus);
				orm.save(order);
				return null;
			}
		});
	}

	public static void processOrder(IOrm orm, IOrder order, String trade_no, byte orderNewStatus) {
		log.infof("处理订单...  [订单号(%s),交易号(%s),订单原状态(%s),订单新状态(%s)]", order.getOrderID(), trade_no, STATUS_TITLES[order.getStatus()], STATUS_TITLES[orderNewStatus]);

		IDemsySoft soft = Demsy.me().getSoft();

		// 已付款（等待卖家发货）
		if (orderNewStatus == STATUS_WAIT_SELLER_SEND_GOODS) {
			// 生成物流单
			if (order.getStatus() == STATUS_WAIT_BUYER_PAY || (order.getStatus() == STATUS_WAIT_SELLER_SEND_GOODS && order.getLogisticsNum() <= 0)) {

				Class operatorClass = bizEngine.getSystemClass(IProductOperator.SYS_CODE);
				Class deliverClass = bizEngine.getSystemClass(IProductDeliver.SYS_CODE);
				Class logisticsClass = bizEngine.getSystemClass(ILogistics.SYS_CODE);
				Class logisticsItemClass = bizEngine.getSystemClass(ILogisticsItem.SYS_CODE);
				Class ordiClass = bizEngine.getSystemClass(IOrderItem.SYS_CODE);

				// 已经处理过了
				if (orm.count(logisticsClass, Expr.eq("orderID", order.getOrderID())) > 0) {
					log.infof("处理订单状态：忽略！订单号(%s),交易号(%s),订单状态(%s),新状态(%s)", order.getOrderID(), trade_no, STATUS_TITLES[order.getStatus()], STATUS_TITLES[orderNewStatus]);
					return;
				}

				IProduct product;
				IProductDeliver deliver;
				IProductOperator operator = null;

				List<IProductDeliver> defaultStorages = orm.query(deliverClass, Expr.eq(LibConst.F_SOFT_ID, soft.getId()));
				List<IProductOperator> defaultOperators = orm.query(operatorClass, Expr.eq(LibConst.F_SOFT_ID, soft.getId()));

				Map<Long, IProductOperator> operatorsMap = new HashMap();
				Map<Long, IProductDeliver> deliversMap = new HashMap();
				// <发货地址， <运营机构，物流清单>>
				Map<Long, Map<Long, List<IOrderItem>>> mapByDeliver = new HashMap();

				// 解析产品发货地点 将订单分解成物流单
				List<IOrderItem> items = orm.query(ordiClass, Expr.eq("order", order));
				for (IOrderItem item : items) {
					product = item.getProduct();

					// 解析订单条目所属的发货地址和运营机构
					deliver = product.getStorage();
					if (deliver == null) {
						deliver = defaultStorages.get(0);
					}
					Long deliverID = deliver == null ? 0 : deliver.getId();
					if (deliver != null)
						deliversMap.put(deliverID, deliver);
					operator = product.getOperator();
					if (operator == null) {
						operator = defaultOperators.get(0);
					}
					Long operatorID = operator == null ? 0 : operator.getId();
					if (operator != null)
						operatorsMap.put(operatorID, operator);

					// 每个【发货地址】有多个【运营机构】，因此会有多个物流单
					Map<Long, List<IOrderItem>> mapByOperator = mapByDeliver.get(deliverID);
					if (mapByOperator == null) {
						mapByOperator = new HashMap();
						mapByDeliver.put(deliverID, mapByOperator);
					}

					// 每个【发货地址】下面的【运营机构】只有一个物流单
					List<IOrderItem> list = mapByOperator.get(operatorID);
					if (list == null) {
						list = new LinkedList();
						mapByOperator.put(operatorID, list);
					}
					list.add(item);

					moduleEngine.increase(orm, item.getProduct(), "saleNum", item.getAmount());
					moduleEngine.decrease(orm, item.getProduct(), "stockNum", item.getAmount());
				}

				int logisticsNum = mapByDeliver.size();
				int logisticsCount = 0;

				// 同一【发货地址】有多个【运营机构】，因此会有多个物流单
				Iterator<Long> deliverIDs = mapByDeliver.keySet().iterator();
				while (deliverIDs.hasNext()) {
					Long deliverID = deliverIDs.next();

					Map<Long, List<IOrderItem>> mapByOperator = mapByDeliver.get(deliverID);
					Iterator<Long> operatorsIDs = mapByOperator.keySet().iterator();

					// 相同【发货地址】同一个【运营机构】只有一个物流单
					while (operatorsIDs.hasNext()) {
						Long operatorID = operatorsIDs.next();

						List<IOrderItem> itemsByOperator = mapByOperator.get(operatorID);

						ILogistics logistics = (ILogistics) Mirror.me(logisticsClass).born();
						logisticsCount++;
						logistics.setOrderID(order.getOrderID() + (logisticsNum > 1 ? "_" + logisticsCount : ""));
						logistics.setAddress(order.getAddress());
						logistics.setPostcode(order.getPostcode());
						logistics.setTelcode(order.getTelcode());
						logistics.setItemsCatalog(itemsByOperator.size());
						logistics.setPersonName(order.getPersonName());
						if (order.getPayTime() != null)
							logistics.setOrderDate(order.getPayTime());
						else
							logistics.setOrderDate(new Date());
						logistics.setNote(order.getNote());
						logistics.setSoftID(soft.getId());

						orm.save(logistics);

						int amount = 0;
						double totalCast = 0;
						StringBuffer desc = new StringBuffer();
						for (IOrderItem item : itemsByOperator) {
							amount += item.getAmount();
							ILogisticsItem logisticsItem = (ILogisticsItem) Mirror.me(logisticsItemClass).born();
							logisticsItem.setLogistics(logistics);
							logisticsItem.setName(item.getName());
							logisticsItem.setCode(item.getCode());
							logisticsItem.setPrice(item.getPrice());
							logisticsItem.setAmount(item.getAmount());
							logisticsItem.setSubtotal(item.getSubtotal());
							logisticsItem.setSoftID(soft.getId());
							logisticsItem.setOrderID(order.getOrderID());
							logisticsItem.setOrderItemID(item.getId());
							logisticsItem.setProductID(item.getProduct().getId());

							totalCast += item.getSubtotal();
							desc.append(item.getName() + " × " + item.getAmount() + " = ￥" + Obj.format(item.getSubtotal(), "#,##0.00") + "；");

							orm.save(logisticsItem);
						}

						logistics.setItemsAmount(amount);
						logistics.setDeliver(deliversMap.get(deliverID));
						logistics.setOperator(operatorsMap.get(operatorID));
						logistics.setTotalCost(totalCast);
						logistics.setDesc(desc.toString());

						orm.save(logistics);
					}
				}

				order.setLogisticsNum(logisticsNum);// 物流单据数
				order.setTradeID(trade_no);
				order.setPayTime(new Date());

				order.setStatus(orderNewStatus);
			} else {
				log.infof("处理订单状态：忽略！订单号(%s),交易号(%s),订单状态(%s),新状态(%s)", order.getOrderID(), trade_no, STATUS_TITLES[order.getStatus()], STATUS_TITLES[orderNewStatus]);
			}
		} else
		// 已发货（等待买家确认收货）
		if (STATUS_WAIT_BUYER_CONFIRM_GOODS == orderNewStatus) {
			// 如果是担保交易、关联物流单发货完毕，则通知支付宝——已经发货

			// 物流单数量
			int lnum = order.getLogisticsNum();
			// 已经发货的物流单数量
			int lnoNum = Str.toArray(order.getLogisticsID()).length;
			// 担保交易
			if (IOrder.PAYTYPE_DB.equals(order.getPaytype())) {
				if (order.getStatus() < STATUS_WAIT_BUYER_CONFIRM_GOODS// 订单状态为未发货
						&& lnoNum >= lnum// 物流单发货完毕
				) {
					// 物流公司名称
					String logistics_name = order.getLogisticsName();

					// 发货时的运输类型
					String transport_type = "EXPRESS";

					// 物流发货单号
					String invoice_no = order.getLogisticsID();
					// ////////////////////////////////////////////////////////////////////////////////

					// 把请求参数打包成数组
					Map<String, String> sParaTemp = new HashMap<String, String>();
					sParaTemp.put("trade_no", trade_no);
					sParaTemp.put("logistics_name", logistics_name);
					sParaTemp.put("invoice_no", invoice_no);
					sParaTemp.put("transport_type", transport_type);

					String sHtmlText = "";
					try {
						sHtmlText = AlipayService.send_goods_confirm_by_platform(sParaTemp);
					} catch (Throwable e) {
						sHtmlText = e.toString();
						log.errorf("处理订单状态：出错！详情请看支付宝返回结果。订单号(%s),交易号(%s),订单状态(%s),新状态(%s)", order.getOrderID(), trade_no, STATUS_TITLES[order.getStatus()], STATUS_TITLES[orderNewStatus]);
					} finally {
						Iterator<String> keys = sParaTemp.keySet().iterator();
						StringBuffer logbuf = new StringBuffer("支付宝发货：提交数据【");
						while (keys.hasNext()) {
							String key = keys.next();
							String value = sParaTemp.get(key);
							logbuf.append("\n").append(key + "=" + value);
						}
						logbuf.append("\n】");
						logbuf.append("\n返回结果【\n").append(sHtmlText).append("\n】");

						log.info(logbuf);
					}

					order.setStatus(orderNewStatus);
				} else {
					log.infof("处理订单状态：忽略！订单号(%s),交易号(%s),订单状态(%s),新状态(%s)", order.getOrderID(), trade_no, STATUS_TITLES[order.getStatus()], STATUS_TITLES[orderNewStatus]);
				}
			} else {
				if (order.getStatus() < STATUS_WAIT_BUYER_CONFIRM_GOODS// 订单状态为未发货
						&& lnoNum >= lnum// 物流单发货完毕
				) {
					order.setStatus(orderNewStatus);
				}
			}
		} else if (STATUS_WAIT_BUYER_CONFIRM_REFUND == orderNewStatus) {
			List<Logistics> lOrders = orm.query(Logistics.class, Expr.beginWith("orderID", order.getOrderID()));
			for (Logistics lorder : lOrders) {
				List<LogisticsItem> litems = orm.query(LogisticsItem.class, Expr.eq("logistics", lorder));
				for (LogisticsItem litem : litems) {
					orm.delete(litem);
				}
				orm.delete(lorder);
			}
		} else {
			order.setStatus(orderNewStatus);
		}

	}

	@At(URL_BZ_ORDER_ALIPAYTO)
	@Ok("void")
	public void alipayto(String orderID) {
		HttpServletResponse response = Demsy.me().response();
		HttpServletRequest request = Demsy.me().request();

		String payment = request.getParameter("payment");
		String note = request.getParameter("note");

		Map<String, String> sParaTemp = new HashMap<String, String>();
		String show_url = null;

		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", -1);
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = null;
		try {
			out = response.getWriter();

			// 查询订单
			Class ordClass = bizEngine.getSystemClass(IOrder.SYS_CODE);
			IOrm orm = Demsy.orm();
			IDemsySoft soft = Demsy.me().getSoft();
			IOrder order = (IOrder) orm.load(ordClass, Expr.eq(LibConst.F_TIME_ID, orderID).and(Expr.eq(LibConst.F_SOFT_ID, soft.getId())));

			if (order == null) {
				log.infof("支付宝收银台：订单不存在！订单号(%s)", orderID);
			}
			orderID = order.getOrderID();

			String out_trade_no = orderID;

			String sHtmlText = "";
			if (order.getStatus() == STATUS_WAIT_BUYER_PAY) {

				// 订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
				String subject = order.getBuyerHideInfo();

				// 订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
				Class ordiClass = bizEngine.getSystemClass(IOrderItem.SYS_CODE);
				List<IOrderItem> items = orm.query(ordiClass, Expr.eq("order", order));
				int amount = 0;
				for (IOrderItem item : items) {
					// String name = item.getName();
					amount += item.getAmount();
					// body.append(",").append(name).append("(" + amount + ")");
				}

				// 订单总金额，显示在支付宝收银台里的“应付总额”里
				String price = Demsy.me().isLocal() ? "0.01" : order.getTotalCost().toString();

				// 物流费用，即运费。
				String logistics_fee = "0.00";

				// 物流类型，三个值可选：EXPRESS（快递）、POST（平邮）、EMS（EMS）
				String logistics_type = "EXPRESS";

				// 物流支付方式，两个值可选：SELLER_PAY（卖家承担运费）、BUYER_PAY（买家承担运费）
				String logistics_payment = "SELLER_PAY";

				// 商品数量，建议默认为1，不改变值，把一次交易看成是一次下订单而非购买一件商品。
				String quantity = "1";

				// 扩展参数//

				// 买家收货信息（推荐作为必填）
				// 该功能作用在于买家已经在商户网站的下单流程中填过一次收货信息，而不需要买家在支付宝的付款流程中再次填写收货信息。
				// 若要使用该功能，请至少保证receive_name、receive_address有值
				String receive_name = order.getPersonName(); // 收货人姓名，如：张三
				String receive_address = order.getAddress(); // 收货人地址，如：XX省XXX市XXX区XXX路XXX小区XXX栋XXX单元XXX号
				String receive_zip = order.getPostcode(); // 收货人邮编，如：123456
				String receive_phone = order.getTelcode(); // 收货人电话号码，如：0571-81234567
				String receive_mobile = order.getTelcode(); // 收货人手机号码，如：13312341234

				// 网站商品的展示地址，不允许加?id=123这类自定义参数
				IModule pmodule = moduleEngine.getModule(soft, bizEngine.getSystem(IOrder.SYS_CODE));
				show_url = MvcUtil.contextPath(SoftConfigManager.me().getAlipayShowUrl(), pmodule.getId() + ":" + order.getId());

				// ////////////////////////////////////////////////////////////////////////////////

				// 把请求参数打包成数组
				sParaTemp.put("payment_type", "1");
				sParaTemp.put("show_url", show_url);
				sParaTemp.put("out_trade_no", out_trade_no);
				sParaTemp.put("subject", subject);
				sParaTemp.put("body", amount + "件商品");
				sParaTemp.put("price", price);
				sParaTemp.put("logistics_fee", logistics_fee);
				sParaTemp.put("logistics_type", logistics_type);
				sParaTemp.put("logistics_payment", logistics_payment);
				sParaTemp.put("quantity", quantity);
				sParaTemp.put("receive_name", receive_name);
				sParaTemp.put("receive_address", receive_address);
				sParaTemp.put("receive_zip", receive_zip);
				sParaTemp.put("receive_phone", receive_phone);
				sParaTemp.put("receive_mobile", receive_mobile);

				String service = "create_partner_trade_by_buyer";// 担保交易
				if (!Str.isEmpty(payment) && !payment.equals(IOrder.PAYTYPE_DB)) {
					service = "create_direct_pay_by_user";// 即时到帐签约网银

					sParaTemp.put("payment", "bankPay");
					sParaTemp.put("defaultbank", payment.equals("1") ? "" : payment);
				}
				if (!Str.isEmpty(payment))
					order.setPaytype(payment);// 支付方式：支付宝
				order.setNote(note);

				orm.save(order, Expr.fieldRexpr("paytype$|note$", false));

				sHtmlText = AlipayService.getAlipayPayGateway(sParaTemp, service);

				log.infof("支付宝收银台：跳转...");
			} else {
				sHtmlText = "只能为“待付款”状态的订单结账！订单号(" + orderID + "),订单当前状态(" + STATUS_TITLES[order.getStatus()] + ")";

				log.infof("支付宝收银台：跳转失败！%s", sHtmlText);
			}

			out.write(sHtmlText);
		} catch (Throwable e) {
			out.println("<script language=\"javascript\">alert('跳转到支付宝收银台结账出错!" + e.getMessage().replace("'", "\'") + "');</script>");
			if (show_url != null)
				out.println(AlipaySubmit.buildForm(sParaTemp, show_url + "?", "POST", "Submit"));

			log.error(String.format("支付宝收银台：跳转出错! 订单号(%s),用户帐号(%s),用户IP(%s)", orderID, Demsy.me().username(), request.getRemoteAddr()), e);
		} finally {
			StringBuffer logbuf = new StringBuffer("支付宝收银台：跳转数据【");
			Iterator<String> keys = sParaTemp.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = sParaTemp.get(key);
				logbuf.append("\n").append(key + "=" + value);
			}
			logbuf.append("\n】");
			log.info(logbuf);

			if (out != null)
				out.close();
		}

	}

	private String setOrderAddress(IOrder order) {
		IOrm orm = Demsy.orm();
		Demsy me = Demsy.me();

		// 验证收货人信息
		String address = me.param("address", String.class, "").trim();
		String province = me.param("province", String.class, "").trim();
		String city = me.param("city", String.class, "").trim();
		String area = me.param("area", String.class, "").trim();
		String street = me.param("street", String.class, "").trim();
		String postcode = me.param("postcode", String.class, "").trim();
		String person = me.param("person", String.class, "").trim();
		String telcode = me.param("telcode", String.class, "").trim();
		String note = me.param("note", String.class, "").trim();
		IContact contact = null;
		Class type = null;
		StringBuffer error = new StringBuffer();
		try {
			IBizSystem contactSys = bizEngine.getSystem(IContact.SYS_CODE);
			type = bizEngine.getType(contactSys);
			Long addressID = Long.parseLong(address);
			contact = (IContact) orm.load(type, addressID);
			if (contact != null) {
				province = contact.getProvince();
				city = contact.getCity();
				area = contact.getArea();
				street = contact.getStreet();
				postcode = contact.getPostcode().trim();
				person = contact.getPerson().trim();
				telcode = contact.getTelcode().trim();
			}
		} catch (Throwable e) {
		}
		if (Str.isEmpty(province))
			error.append("省份必填；");
		if (Str.isEmpty(city))
			error.append("城市必填；");
		// if (Str.isEmpty(area))
		// error.append("区县必填；");
		if (!Str.hasChinese(street))
			error.append("街道名称必须包含汉字且长度不得小于4；");
		else if (street.length() < 4)
			error.append("街道名称必须包含汉字且长度不得小于4；");
		if (!Str.isPostcode(postcode))
			error.append("邮政编码必须是6位数字；");
		if (!Str.isRealname(person))
			error.append("收件人姓名必须是2个以上的汉字；");
		if (!Str.isTelcode(telcode))
			error.append("电话号码必须是11位数字，固定电话请加上区号；");

		if (error.length() > 0) {
			return "请检查如下错误：" + error.toString();
		}

		address = province + city + area + street;

		if (contact == null && !Str.isEmpty(me.username())) {
			contact = (IContact) Mirror.me(type).born();
			contact.setProvince(province);
			contact.setCity(city);
			contact.setArea(area);
			contact.setStreet(street);
			contact.setPostcode(postcode);
			contact.setPerson(person);
			contact.setTelcode(telcode);
			contact.setSoftID(Demsy.me().getSoft().getId());
			orm.save(contact);
		}

		order.setPersonName(person);
		order.setTelcode(telcode);
		order.setAddress(address);
		order.setPostcode(postcode);
		order.setNote(note);

		return null;
	}

	@At(URL_BZ_ORDER_DIRECTBUY)
	public Map directbuy(String pID, String num) {
		Map ret = new HashMap();

		Demsy me = Demsy.me();
		HttpServletRequest request = me.request();
		IOrm orm = Demsy.orm();
		IDemsySoft soft = Demsy.me().getSoft();

		long productID = 0;
		int amount = 0;
		try {
			productID = Long.parseLong(pID);
			amount = Integer.parseInt(num);
		} catch (Throwable e) {
		}
		IModule ordModule = moduleEngine.getModule(soft, bizEngine.getSystem(IOrder.SYS_CODE));
		Class prdClass = bizEngine.getSystemClass(IProduct.SYS_CODE);
		IProduct product = (IProduct) orm.load(prdClass, productID);

		// 产品信息校验
		if (product == null) {
			ret.put("error", "购买的产品【" + productID + "】不存在!");
			return ret;
		}
		// 产品价格校验
		Double price = product.getNowPrice();
		if (price == null || price == 0) {
			ret.put("error", "购买的产品【" + product.getName() + "】价格未知!");
			return ret;
		}
		// 校验购买数量
		if (amount <= 0) {
			ret.put("error", "填写的购买数量非法!");
			return ret;
		}

		Class ordClass = bizEngine.getSystemClass(IOrder.SYS_CODE);
		IOrder order = (IOrder) Mirror.me(ordClass).born();
		order.setSoftID(soft.getId());

		String error = setOrderAddress(order);
		if (!Str.isEmpty(error)) {
			ret.put("error", error);
			return ret;
		}

		List list = new LinkedList();
		list.add(order);

		double itemsPrice = 0;
		int itemsAmount = 0;
		Class itemClass = bizEngine.getSystemClass(IOrderItem.SYS_CODE);
		IOrderItem item = (IOrderItem) Mirror.me(itemClass).born();
		item.setProduct(product);
		item.setProductCatalog(product.getCatalog());
		item.setProductOperator(product.getOperator());
		item.setAmount(amount);
		item.setPrice(price);
		item.setSubtotal(amount * price);
		item.setStatus(IOrderItem.STATUS_CART);
		item.setCreatedIP(request.getRemoteAddr());
		item.setName(product.getName());
		item.setCode(product.getCode());
		item.setSoftID(me.getSoft().getId());
		item.setStatus(IOrderItem.STATUS_PREPARING);
		item.setOrder(order);
		Integer stockNum = product.getStockNum();
		if (stockNum != null && stockNum >= 0 && amount > stockNum) {
			ret.put("error", "【" + product.getName() + "】库存只有 " + stockNum + " 件，不足 " + amount);
			return ret;
		}

		list.add(item);

		itemsPrice += item.getSubtotal();
		itemsAmount += item.getAmount();

		Double postFee = itemsPrice >= SoftConfigManager.me().getEshopNotPostFee() ? 0 : SoftConfigManager.me().getEshopPostFee();
		double totalPrice = itemsPrice + postFee;

		order.setLogisticsCost(postFee);// 邮寄费
		order.setItemsCost(itemsPrice);// 商品金额
		order.setItemsAmount(itemsAmount);// 商品数量
		order.setItemsCatalog(1);// 商品种类
		order.setTotalCost(totalPrice);// 订单金额
		// order.setDiscount(discount);
		order.setStatus(STATUS_WAIT_BUYER_PAY);
		order.setType(IOrder.TYPE_DIRECT_BUY);
		order.setDesc(item.getName() + " × " + item.getAmount() + " = ￥" + Obj.format(item.getSubtotal(), "#,##0.00"));

		Demsy.bizSession.save(list);

		ret.put("success", true);
		ret.put("guid", order.getTimeID());
		ret.put("totalPrice", totalPrice);
		ret.put("sid", ordModule.getId() + ":" + order.getId());

		return ret;
	}

	@At(URL_BZ_ORDER_SUBMIT)
	public Map saveorder() {
		Map ret = new HashMap();

		Demsy me = Demsy.me();
		HttpServletRequest request = me.request();
		IDemsySoft soft = Demsy.me().getSoft();

		Object syncObj = me.login();
		if (syncObj == null) {
			syncObj = request.getSession();
		}

		synchronized (syncObj) {
			Map<Long, IOrderItem> cart = OrderActions.getShopCart();
			String[] pids = (String[]) request.getSession().getAttribute(OrderActions.SESSION_KEY_CART_SELECTED);
			List<IOrderItem> items = new LinkedList();
			if (pids == null || pids.length == 0) {
				ret.put("error", "请先选择要购买的商品");
				return ret;
			}
			for (String pid : pids) {
				IOrderItem oi = cart.get(Long.parseLong(pid));
				items.add(oi);
			}

			IModule ordModule = moduleEngine.getModule(soft, bizEngine.getSystem(IOrder.SYS_CODE));
			Class ordClass = bizEngine.getSystemClass(IOrder.SYS_CODE);
			IOrder order = (IOrder) Mirror.me(ordClass).born();
			order.setSoftID(soft.getId());

			// 验证收货人信息
			String error = setOrderAddress(order);
			if (!Str.isEmpty(error)) {
				ret.put("error", error);
				return ret;
			}

			List list = new LinkedList();
			list.add(order);

			// Double discount = 10.0;
			double itemsPrice = 0;
			int itemsAmount = 0;
			StringBuffer desc = new StringBuffer();
			for (IOrderItem item : items) {
				item.setOrder(order);
				item.setStatus(IOrderItem.STATUS_PREPARING);
				list.add(item);

				int amount = item.getAmount();
				itemsPrice += item.getSubtotal();
				itemsAmount += amount;
				IProduct p = item.getProduct();
				Integer stockNum = p.getStockNum();
				if (stockNum != null && stockNum >= 0 && amount > stockNum) {
					ret.put("error", "【" + p.getName() + "】库存只有 " + stockNum + " 件，不足 " + amount);
					return ret;
				}

				desc.append(item.getName() + " × " + item.getAmount() + " = ￥" + Obj.format(item.getSubtotal(), "#,##0.00") + "；");
			}
			Double postFee = itemsPrice >= SoftConfigManager.me().getEshopNotPostFee() ? 0 : SoftConfigManager.me().getEshopPostFee();

			double totalPrice = itemsPrice + postFee;

			order.setLogisticsCost(postFee);// 邮寄费
			order.setItemsCost(itemsPrice);// 商品金额
			order.setItemsAmount(itemsAmount);// 商品数量
			order.setItemsCatalog(items.size());// 商品种类
			order.setTotalCost(totalPrice);// 订单金额
			// order.setDiscount(discount);
			order.setStatus(STATUS_WAIT_BUYER_PAY);
			order.setType(IOrder.TYPE_CART);
			order.setDesc(desc.toString());

			Demsy.bizSession.save(list);

			ret.put("success", true);
			ret.put("guid", order.getTimeID());
			ret.put("totalPrice", totalPrice);
			ret.put("sid", ordModule.getId() + ":" + order.getId());

			request.getSession().removeAttribute(OrderActions.SESSION_KEY_CART_SELECTED);
			for (IOrderItem item : items) {
				cart.remove(item.getProduct().getId());
			}
		}

		return ret;
	}

	@At(URL_BZ_ORDER_PREPARE)
	public Map prepare() {
		Map ret = new HashMap();

		Demsy me = Demsy.me();
		HttpServletRequest request = me.request();
		String[] pids = me.param("products", String[].class, null);
		if (pids != null && pids.length > 0) {
			request.getSession().setAttribute(SESSION_KEY_CART_SELECTED, pids);
			ret.put("success", true);
		} else
			ret.put("error", "请先选中要结算的商品!");

		return ret;
	}

	@At(URL_BZ_ORDER_REFRESHCART)
	public Map refreshcart(String pID, String num) {
		return this.changecart(pID, num, false);
	}

	@At(URL_BZ_ORDER_DELETECART)
	public Map deletecart(String pID) {
		Map ret = new HashMap();
		long productID = 0;
		try {
			productID = Long.parseLong(pID);
		} catch (Throwable e) {
		}

		Demsy me = Demsy.me();
		HttpServletRequest request = me.request();
		IOrm orm = Demsy.orm();

		Class prdClass = bizEngine.getSystemClass(IProduct.SYS_CODE);
		IProduct product = (IProduct) orm.load(prdClass, productID);

		// 产品信息校验
		if (product == null) {
			ret.put("error", "购买的产品【" + productID + "】不存在!");
			return ret;
		}

		// 获取购物车
		Object syncObj = me.login();
		if (syncObj == null) {
			syncObj = request.getSession();
		}

		synchronized (syncObj) {
			Map<Long, IOrderItem> cart = getShopCart();
			IOrderItem item = cart.get(productID);
			if (item != null) {
				cart.remove(productID);
				if (syncObj instanceof ILogin) {
					Demsy.bizSession.delete(item);
				}
			}

			Iterator<Long> items = cart.keySet().iterator();
			double totalPrice = 0;
			int totalAmount = 0;
			List list = new LinkedList();
			while (items.hasNext()) {
				IOrderItem oi = cart.get(items.next());
				list.add(oi);
				totalPrice += oi.getSubtotal();
				totalAmount += oi.getAmount();
			}
			ret.put("success", true);
			ret.put("itemSize", list.size());
			ret.put("totalPrice", totalPrice);
			ret.put("totalAmount", totalAmount);
		}

		return ret;
	}

	@At(URL_BZ_ORDER_ADDTOCART)
	public Map addtocart(String pID, String num) {
		return changecart(pID, num, true);
	}

	public Map changecart(String pID, String num, boolean add) {
		Map ret = new HashMap();
		long productID = 0;
		int amount = 0;
		try {
			productID = Long.parseLong(pID);
			amount = Integer.parseInt(num);
		} catch (Throwable e) {
		}

		Demsy me = Demsy.me();
		HttpServletRequest request = me.request();
		IOrm orm = Demsy.orm();

		Class prdClass = bizEngine.getSystemClass(IProduct.SYS_CODE);
		IProduct product = (IProduct) orm.load(prdClass, productID);

		// 产品信息校验
		if (product == null) {
			ret.put("error", "购买的产品【" + productID + "】不存在!");
			return ret;
		}
		// 产品价格校验
		Double price = product.getNowPrice();
		if (price == null || price == 0) {
			ret.put("error", "购买的产品【" + product.getName() + "】价格未知!");
			return ret;
		}
		// 校验购买数量
		if (amount <= 0) {
			ret.put("error", "填写的购买数量非法!");
			return ret;
		}

		// 获取购物车
		Class ordiClass = bizEngine.getSystemClass(IOrderItem.SYS_CODE);
		Object syncObj = me.login();
		if (syncObj == null) {
			syncObj = request.getSession();
		}

		synchronized (syncObj) {
			Map<Long, IOrderItem> cart = getShopCart();
			IOrderItem item = cart.get(productID);
			if (item == null) {
				item = (IOrderItem) Mirror.me(ordiClass).born();
				cart.put(productID, item);
			} else if (add) {
				amount = amount + item.getAmount();
			}

			item.setProduct(product);
			item.setProductCatalog(product.getCatalog());
			item.setProductOperator(product.getOperator());
			item.setAmount(amount);
			item.setPrice(price);
			item.setSubtotal(amount * price);
			item.setStatus(IOrderItem.STATUS_CART);
			item.setCreatedIP(request.getRemoteAddr());
			item.setName(product.getName());
			item.setCode(product.getCode());
			item.setSoftID(me.getSoft().getId());
			if (syncObj instanceof ILogin) {
				Demsy.bizSession.save(item);
			}

			Iterator<Long> items = cart.keySet().iterator();
			double totalPrice = 0;
			int totalAmount = 0;
			List list = new LinkedList();
			while (items.hasNext()) {
				IOrderItem oi = cart.get(items.next());
				list.add(oi);
				totalPrice += oi.getSubtotal();
				totalAmount += oi.getAmount();
			}
			ret.put("success", true);
			ret.put("itemSize", list.size());
			ret.put("totalPrice", totalPrice);
			ret.put("totalAmount", totalAmount);
		}

		return ret;
	}

	public static List<IOrder> getMyOrders(Long orderID) {
		Demsy me = Demsy.me();
		IOrm orm = Demsy.orm();

		Class ordClass = bizEngine.getSystemClass(IOrder.SYS_CODE);

		CndExpr expr = Expr.eq(LibConst.F_SOFT_ID, me.getSoft().getId()).addDesc(LibConst.F_CREATED);
		if (Obj.isPositive(orderID))
			expr = expr.and(Expr.eq(LibConst.F_ID, orderID));
		else {
			String user = me.username();
			if (Str.isEmpty(user))
				return new ArrayList();

			expr = expr.and(Expr.eq(LibConst.F_CREATED_BY, user));
		}

		List<IOrder> ret = orm.query(ordClass, expr);

		return ret;
	}

	public static Map<Long, IOrderItem> getShopCart() {
		Demsy me = Demsy.me();
		HttpServletRequest request = me.request();
		IOrm orm = Demsy.orm();

		String user = me.username();
		Class ordiClass = bizEngine.getSystemClass(IOrderItem.SYS_CODE);

		Map<Long, IOrderItem> cartMap;

		if (!Str.isEmpty(user)) {
			cartMap = new HashMap();
			CndExpr expr = Expr.eq(LibConst.F_CREATED_BY, user).and(Expr.eq(LibConst.F_SOFT_ID, me.getSoft().getId()));
			expr = expr.and(Expr.eq("status", IOrderItem.STATUS_CART)).and(Expr.isNull("order"));
			List<IOrderItem> savedItems = orm.query(ordiClass, expr);
			for (IOrderItem item : savedItems) {
				Long id = item.getProduct().getId();
				cartMap.put(id, item);
			}

			// 合并订单条目
			Map<Long, IOrderItem> sessionCart = (HashMap) request.getSession().getAttribute(SESSION_KEY_CART);
			if (sessionCart != null) {
				Iterator<Long> it = sessionCart.keySet().iterator();
				while (it.hasNext()) {
					IOrderItem ele = sessionCart.get(it.next());
					IProduct p = ele.getProduct();
					IOrderItem cartitem = cartMap.get(p.getId());
					if (cartitem != null) {
						int amount = ele.getAmount() + cartitem.getAmount();
						Double price = p.getNowPrice();

						cartitem.setAmount(amount);
						if (price == null) {
							price = 0d;
						}
						cartitem.setPrice(price);
						cartitem.setSubtotal(price * amount);
					} else {
						cartMap.put(p.getId(), ele);
					}
				}
				request.getSession().removeAttribute(SESSION_KEY_CART);
			}
			// if (sessionCart != null && sessionCart.size() > 0) {
			Iterator<Long> items = cartMap.keySet().iterator();
			List list = new LinkedList();
			while (items.hasNext()) {
				IOrderItem oi = cartMap.get(items.next());
				list.add(oi);
			}
			Demsy.bizSession.save(list);
			// }
		} else {
			cartMap = (HashMap) request.getSession().getAttribute(SESSION_KEY_CART);
			if (cartMap == null) {
				cartMap = new HashMap();
				request.getSession().setAttribute(SESSION_KEY_CART, cartMap);
			}
		}

		return cartMap;
	}
}
