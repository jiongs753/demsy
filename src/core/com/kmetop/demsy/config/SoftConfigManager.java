package com.kmetop.demsy.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;

public class SoftConfigManager {
	public static final String ADMIN_UI_TOP_HEIGHT = "admin.ui.topHeight";
	public static final String ADMIN_UI_LEFT_WIDTH = "admin.ui.leftWidth";

	private static Map<Long, SoftConfigManager> cache = new HashMap();

	private Map<String, ISoftConfig> map = new HashMap();

	protected IDemsySoft soft;

	private SoftConfigManager(IDemsySoft soft) {
		this.soft = soft;

	}

	public static void clearCache() {
		Long soft = Demsy.me().getSoft().getId();
		SoftConfigManager config = cache.get(soft);
		if (config != null)
			config.map.clear();

		cache.remove(soft);
	}

	public static SoftConfigManager me() {
		Long softID = Demsy.me().getSoft().getId();
		SoftConfigManager config = cache.get(softID);
		if (config != null)
			return config;

		config = new SoftConfigManager(Demsy.me().getSoft());
		cache.put(softID, config);

		IOrm orm = Demsy.orm();
		List<ISoftConfig> list = orm.query(Demsy.bizEngine.getStaticType(LibConst.BIZSYS_ADMIN_CONFIG), Expr.eq(LibConst.F_SOFT_ID, softID));
		for (ISoftConfig ele : list) {
			config.map.put(ele.getCode(), ele);
		}

		return config;
	}

	public String getAlipayPartner() {
		ISoftConfig c = map.get("alipay.partner");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipayKey() {
		ISoftConfig c = map.get("alipay.key");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipaySellerEmail() {
		ISoftConfig c = map.get("alipay.seller_email");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipayNotifyUrl() {
		ISoftConfig c = map.get("alipay.notify_url");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		if (ret.startsWith("/")) {
			String domain = Demsy.me().getDomain();
			int port = Demsy.me().getPort();
			return "http://" + domain + (port != 80 ? (":" + port) : "") + ret;
		}

		return ret;
	}

	public String getAlipayReturnUrl() {
		ISoftConfig c = map.get("alipay.return_url");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		if (ret.startsWith("/")) {
			String domain = Demsy.me().getDomain();
			int port = Demsy.me().getPort();
			return "http://" + domain + (port != 80 ? (":" + port) : "") + ret;
		}

		return ret;
	}

	public String getAlipayShowUrl() {
		ISoftConfig c = map.get("alipay.show_url");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		if (ret.startsWith("/")) {
			String domain = Demsy.me().getDomain();
			int port = Demsy.me().getPort();
			return "http://" + domain + (port != 80 ? (":" + port) : "") + ret;
		}

		return ret;
	}

	/**
	 * 获取支付宝支付服务类型 可选值：标准双接口(trade_create_by_buyer)即时到帐接口
	 * (create_direct_pay_by_user)担保交易接口 (create_partner_trade_by_buyer)
	 * 
	 * @return
	 */
	public String getAlipayPayService() {
		ISoftConfig c = map.get("alipay.pay_service");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		return ret;
	}

	public Double getEshopNotPostFee() {
		ISoftConfig c = map.get("eshop.not_post_fee");

		String ret = "";
		if (c != null)
			ret = c.getValue();
		try {
			if (!Str.isEmpty(ret)) {
				return new Double(ret);
			}
		} catch (Throwable e) {
		}

		return 100.0;
	}

	public String getEshopNotPostFeeDesc() {
		ISoftConfig c = map.get("eshop.not_post_fee");

		String ret = "";
		if (c != null)
			ret = c.getDesc();

		return ret;
	}

	public Double getEshopPostFee() {
		ISoftConfig c = map.get("eshop.post_fee");

		String ret = "";
		if (c != null)
			ret = c.getValue();
		try {
			if (!Str.isEmpty(ret)) {
				return new Double(ret);
			}
		} catch (Throwable e) {
		}

		return 0.0;
	}

	public String getEshopPostFeeDesc() {
		ISoftConfig c = map.get("eshop.post_fee");

		String ret = "";
		if (c != null)
			ret = c.getDesc();

		return ret;
	}

	public String get(String key, String defaultValue) {
		ISoftConfig c = map.get(key);

		if (c != null && !Str.isEmpty(c.getValue()))
			return c.getValue();

		return defaultValue;
	}

	public int getInt(String key, int defaultValue) {
		ISoftConfig c = map.get(key);

		try {
			if (c != null && !Str.isEmpty(c.getValue()))
				return Integer.parseInt(c.getValue());
		} catch (Throwable e) {
		}

		return defaultValue;
	}

	public ISoftConfig getConfig(String key) {
		return map.get(key);
	}
}
