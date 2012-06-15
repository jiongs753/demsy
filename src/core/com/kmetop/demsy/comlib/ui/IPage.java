package com.kmetop.demsy.comlib.ui;

import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.entity.IBizComponent;

public interface IPage extends IBizComponent {
	public static final int USAGE_TPL = 0;

	public static final int USAGE_IDX = 1;

	public static final int USAGE_LOGIN = 2;

	public static final int USAGE_ADMIN = 3;

	public String getPageWidth();

	public String getPageHeight();

	public String getUiTemplate();

	public IStyle getStyle();

	String getKeywords();

	public FakeSubSystem<? extends IStyleItem> getStyleItems();
}
