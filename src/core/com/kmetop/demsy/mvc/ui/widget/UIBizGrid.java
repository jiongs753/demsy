package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.mvc.ui.widget.field.UIBizFld;

/**
 * 业务GRID: 是一个业务窗体，用来表示业务窗体为一个Grid表格。
 * <UL>
 * <LI>业务窗体更多特性请参见{@link UIBizModel}
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class UIBizGrid extends UIBizModel {

	public UIBizGrid(Map ctx, Serializable id) {
		super(ctx, id);
	}

	protected String idField = LibConst.F_ID;

	protected boolean gridview = false;

	protected int rowNum = 20;// 行数

	private boolean viewrecords = true;

	private boolean multiselect = true;

	private boolean multiboxonly = true;

	private String multikey = "shift";

	private boolean altRows = false;

	private boolean shrinkToFit = true;

	private boolean autowidth = true;

	private boolean rownumbers = true;

	private boolean hidegrid = true;

	private String resizeclass;

	private String sortname = "orderby,id";

	private String sortorder = "asc,desc";

	private boolean search = true;

	private String rowList = "[10,20,50,100,200]";

	protected final List<UIBizFld> fields = new LinkedList();

	public List<UIBizFld> getFields() {
		return fields;
	}

	public UIBizModel addField(UIBizFld field) {
		fields.add(field);
		return this;
	}

	public UIBizModel removeField(UIBizFld field) {
		fields.remove(field);
		return this;
	}

	public boolean getViewrecords() {
		return viewrecords;
	}

	public void setViewrecords(boolean viewrecords) {
		this.viewrecords = viewrecords;
	}

	public boolean getAltRows() {
		return altRows;
	}

	public void setAltRows(boolean altRows) {
		this.altRows = altRows;
	}

	public boolean getShrinkToFit() {
		return shrinkToFit;
	}

	public void setShrinkToFit(boolean shrinkToFit) {
		this.shrinkToFit = shrinkToFit;
	}

	public boolean getAutowidth() {
		return autowidth;
	}

	public void setAutowidth(boolean autowidth) {
		this.autowidth = autowidth;
	}

	public boolean getRownumbers() {
		return rownumbers;
	}

	public void setRownumbers(boolean rownumbers) {
		this.rownumbers = rownumbers;
	}

	public boolean getHidegrid() {
		return hidegrid;
	}

	public void setHidegrid(boolean hidegrid) {
		this.hidegrid = hidegrid;
	}

	public String getResizeclass() {
		return resizeclass;
	}

	public void setResizeclass(String resizeclass) {
		this.resizeclass = resizeclass;
	}

	public boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(boolean multiselect) {
		this.multiselect = multiselect;
	}

	public String getMultikey() {
		return multikey;
	}

	public void setMultikey(String multikey) {
		this.multikey = multikey;
	}

	public boolean getMultiboxonly() {
		return multiboxonly;
	}

	public void setMultiboxonly(boolean multiboxonly) {
		this.multiboxonly = multiboxonly;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int initialPageSize) {
		this.rowNum = initialPageSize;
	}

	public boolean getGridview() {
		return gridview;
	}

	public void setGridview(boolean gridview) {
		this.gridview = gridview;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public static class UIGridFld extends UIBizFld {

		public UIGridFld(Map ctx, Serializable id) {
			super(ctx, id);
		}

		protected boolean searchhidden = true;

		protected boolean sortable = true;

		protected boolean resizable = true;

		protected boolean initialSort = false;

		protected boolean hidden = false;

		protected Map editoptions = new HashMap();

		protected Map formoptions = new HashMap();

		protected Map editrules = new HashMap();

		protected Map<String, String> options;

		private boolean string;

		public boolean getSortable() {
			return sortable;
		}

		public void setSortable(boolean sortable) {
			this.sortable = sortable;
		}

		public boolean getInitialSort() {
			return initialSort;
		}

		public void setInitialSort(boolean initialSort) {
			this.initialSort = initialSort;
		}

		public boolean getResizable() {
			return resizable;
		}

		public void setResizable(boolean resizable) {
			this.resizable = resizable;
		}

		public Map getEditoptions() {
			return editoptions;
		}

		public Map getFormoptions() {
			return formoptions;
		}

		public Map getEditrules() {
			return editrules;
		}

		public void setEditoptions(Map editoptions) {
			this.editoptions = editoptions;
		}

		public void setFormoptions(Map formoptions) {
			this.formoptions = formoptions;
		}

		public void setEditrules(Map editrules) {
			this.editrules = editrules;
		}

		public boolean getHidden() {
			return hidden;
		}

		public void setHidden(boolean hidden) {
			this.hidden = hidden;
		}

		public Map<String, String> getOptions() {
			return options;
		}

		public void setOptions(Map<String, String> options) {
			this.options = options;
		}

		public boolean getSearchhidden() {
			return searchhidden;
		}

		public void setSearchhidden(boolean searchable) {
			this.searchhidden = searchable;
		}

		public boolean isString() {
			return string;
		}

		public void setString(boolean string) {
			this.string = string;
		}
	}

	public boolean getSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public String getRowList() {
		return rowList;
	}

	public void setRowList(String rowList) {
		this.rowList = rowList;
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}
}
