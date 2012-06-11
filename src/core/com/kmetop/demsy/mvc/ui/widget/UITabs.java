package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.Map;


/**
 * TAB结构: 用来表示以TAB形式布局列表中的UI项。
 * 
 * <UL>
 * <LI>UI布局更多特性请参见{@link UIBlockView}；
 * <LI>支持TAB项置顶、居右、居左、置底等；
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class UITabs extends UIWidget {
	public UITabs(Map ctx, Serializable id) {
		super(ctx, id);
	}

}
