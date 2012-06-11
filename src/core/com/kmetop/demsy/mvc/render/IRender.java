package com.kmetop.demsy.mvc.render;

import java.io.Writer;
import java.util.Map;

import com.kmetop.demsy.mvc.ui.IUIView;

public interface IRender {

	public void render(Writer out, Throwable ex, Map context) throws Exception;

	public void render(Writer out, IUIView ui, Map context) throws Exception;

	public String getContentType(IUIView ui);

}
