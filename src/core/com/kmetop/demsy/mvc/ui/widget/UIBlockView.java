package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.Map;


/**
 * UI表达式视图: 可以直接指定模版内容，且模版内容中含有表达式
 * 
 * @author yongshan.ji
 * 
 */
public class UIBlockView extends UIWidget {

	public UIBlockView(Map ctx, Serializable id) {
		super(ctx, id);
	}

}
