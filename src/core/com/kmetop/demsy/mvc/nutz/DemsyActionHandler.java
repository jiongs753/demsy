package com.kmetop.demsy.mvc.nutz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Loading;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.UrlMapping;
import org.nutz.mvc.impl.ActionInvoker;

import com.kmetop.demsy.Demsy;

/**
 * 该类在 {@link org.nutz.mvc.ActionHandler} 的基础上修改，详细参见 @DEMSY 标识部分
 * 
 * @author yongshan.ji
 * 
 */
public class DemsyActionHandler {

	private Loading loading;

	private UrlMapping mapping;

	private NutConfig config;

	public DemsyActionHandler(NutConfig config) {
		this.config = config;
		this.loading = config.createLoading();
		this.mapping = loading.load(config);
	}

	public void depose() {
		loading.depose(config);
	}

	// @DEMSY: 新增的方法，用于执行ACTION
	/**
	 * 执行“DEMSY平台”中的带应用编号的路径。
	 */
	public boolean execute(HttpServletRequest req, HttpServletResponse resp) {
		Demsy ctx = Demsy.me();
		ActionContext ac = ctx.actionContext();

		ActionInvoker invoker = ctx.actionInvoker();
		if (null == invoker)
			return false;

		invoker.invoke(ac);

		return true;
	}

	// @DEMSY: 新增的方法
	/**
	 * 获取UrlMapping对象，创建“DEMSY请求”上下文环境时调用该方法，用来获取请求路径对应的
	 * {@link org.nutz.mvc.ActionChain}.
	 */
	public UrlMapping getMapping() {
		return mapping;
	}

}
