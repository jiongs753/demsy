package com.kmetop.demsy.mvc.ui;

import static com.kmetop.demsy.Demsy.appconfig;
import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.comlib.LibConst.F_CREATED;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizFieldType;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.entity.IBizEntity;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.comlib.ui.IUIViewComponent;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.widget.UIPageView;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.expr.ExprRule;

/**
 * 页面板块运行时上下文环境：封装了运行时的页面板块信息，解析自定义的页面板块。
 * 
 * @author jiongs753
 * 
 */
public class UIBlockContext {

	static Log log = Logs.getLog(UIBlockContext.class);

	// 上级板块上下文运行时环境
	private UIBlockContext parent;

	// 页面视图
	private UIPageView pageView;

	// 自定义页面板块
	private IPageBlock block;

	// 条目标题长度：即条目数据模型中的标题将不能超过指定长度（多少个汉字？）
	private Integer titleLen;

	// 条目单元数量：即每列多少条记录？和每行多少条记录？
	private Integer cellCount;

	// 扩展属性
	private Map props;

	// 板块数据源配置
	private Dataset datasource;

	/*
	 * 以下属性支持继承
	 */

	// 板块标题数据来自哪个模块？
	private IModule catalogModule;

	// 板块明细数据来自哪个模块？
	private IModule module;

	// 板块标题数据来自哪个业务系统？
	private IBizSystem catalogSystem;

	// 板块明细数据来自哪个业务系统？
	private IBizSystem system;

	// 栏目字段
	private IBizField catalogField;

	// 栏目业务系统类
	private Class catalogType;

	// 数据业务系统类
	private Class type;

	// 查询规则
	private List<String> rules;

	// 查询字段
	private List<String> fields;

	// 动态模块ID
	private Long dynamicModuleID;

	// 动态数据ID: 数据来自动态模块
	private Long dynamicDataID;

	// 页大小
	private Integer pageSize;

	// 页索引
	private Integer pageIndex;

	// 图片字段
	private String imageFld;

	// 排序字段
	private String orderBy;

	// 分组字段
	private String groupBy;

	// 分页查询
	private Pager pager;

	// 查询表达式
	private CndExpr expr;

	// 栏目对象集合
	private List catalogObjs;

	// 栏目对象
	private Object catalogObj;

	// 数据对象集合
	private List itemObjs;

	// 数据对象
	private Object itemObj;

	/*
	 * 板块数据模型，不支持继承
	 */
	// 栏目模型
	private UIBlockDataModel catalog;

	private List<UIBlockDataModel> catalogs;

	// 数据模型集合
	private List<UIBlockDataModel> items;

	// 数据模型
	private UIBlockDataModel item;

	/**
	 * 继承数据
	 */
	private static void inherit(UIBlockContext context) {
		if (context.parent != null) {
			context.catalogModule = context.parent.catalogModule;
			context.module = context.parent.module;
			context.catalogSystem = context.parent.catalogSystem;
			context.system = context.parent.system;
			context.catalogField = context.parent.catalogField;
			context.catalogType = context.parent.catalogType;
			context.type = context.parent.type;
			context.rules = context.parent.rules;
			context.fields = context.fields;
			context.dynamicModuleID = context.dynamicModuleID;
			context.dynamicDataID = context.parent.dynamicDataID;
			context.pageSize = context.parent.pageSize;
			context.pageIndex = context.parent.pageIndex;
			context.imageFld = context.parent.imageFld;
			context.orderBy = context.parent.orderBy;
			context.pager = context.parent.pager;
			context.expr = context.parent.expr;
			context.catalogObjs = context.parent.catalogObjs;
			context.catalogObj = context.parent.catalogObj;
			context.itemObjs = context.parent.itemObjs;
			context.itemObj = context.parent.itemObj;
		}
	}

	/**
	 * 构造板块运行时上下文环境
	 * 
	 * @param pageView
	 *            页面视图
	 * @param parent
	 *            上级板块运行时上下文环境
	 * @param block
	 *            板块
	 * @param dynamicModuleID
	 *            动态模块ID
	 * @param dynamicDataID
	 *            动态数据ID
	 */
	public UIBlockContext(UIPageView pageView, UIBlockContext parent, IPageBlock block, Long dynamicModuleID, Long dynamicDataID) {
		this.pageView = pageView;
		this.parent = parent;
		this.block = block;
		this.dynamicModuleID = dynamicModuleID;
		this.dynamicDataID = dynamicDataID;
		props = new HashMap();

		// 初始化
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 解析数据源
		this.parseDataset();

		// 集成上级板块数据集
		if (isShareResult()) {
			inherit(this);
		}
	}

	/**
	 * 判断是否共享上级板块数据集？
	 */
	private boolean isShareResult() {
		return isInherit() && Str.isEmpty(datasource.getModuleGuid());
	}

	/**
	 * 判断是否继承上级板块数据集
	 * 
	 * @return
	 */
	private boolean isInherit() {
		return datasource != null && datasource.isInherit();
	}

	/**
	 * 判断是否已指定数据源模块？
	 * 
	 * @return
	 */
	private boolean existModule() {
		return datasource != null && !Str.isEmpty(datasource.getModuleGuid());
	}

	/**
	 * 获取板块数据源。
	 * <UL>
	 * <LI>数据源不存在：返回上级板块的数据源；
	 * <LI>已指定数据源模块：返回板块自己的数据源；
	 * <LI>未指定数据源模块：
	 * <UL>
	 * <LI>未设置数据共享：返回上级板块的数据源；
	 * <LI>已设置数据共享：返回板块自己的数据源；
	 * </UL>
	 * </UL>
	 * 
	 * @param block
	 *            板块
	 * @return 数据源对象
	 */
	private static Dataset getDataset(IPageBlock block) {
		Dataset ds = block.getDataset();
		if (ds == null || !ds.isInherit()) {
			if ((ds == null || Str.isEmpty(ds.getModuleGuid())) && block.getParent(false) != null) {
				return getDataset(block.getParent(false));
			}
		}

		return ds;
	}

	/**
	 * 解析数据源
	 */
	private void parseDataset() {
		// 数据源已经解析
		if (rules != null)
			return;

		Demsy me = Demsy.me();
		IOrm orm = Demsy.orm();

		// 获取数据源
		this.datasource = getDataset(block);

		/*
		 * 解析板块设置
		 */
		// 解析分页参数
		pageIndex = me.param("page", Integer.class, 1);
		pageSize = me.param("pageSize", Integer.class, datasource == null ? 0 : datasource.getPageSize());

		// 解析列表行/列数量
		cellCount = block.getCellCount();
		if (cellCount == null || cellCount <= 0) {
			cellCount = 1;
		}

		// 解析列表标题长度
		titleLen = block.getTitleLength();
		if (titleLen == null) {
			titleLen = 0;
		}

		/*
		 * 解析数据源：解析结束
		 */
		// 模块未指定
		if (!existModule()) {
			rules = new ArrayList();
			catalogObjs = new ArrayList();

			if (log.isTraceEnabled())
				log.tracef("板块数据源模块不存在");

			return;
		}

		// 初始化解析规则和栏目对象集合
		List<String> srcRuleArray = datasource.getExprs();
		String[] srcRule2Array = Str.toArray(datasource.getRules2(), ",;");
		rules = new ArrayList(srcRuleArray.size());
		catalogObjs = new ArrayList(srcRuleArray.size());

		// 解析数据源模块
		String moduleGuid = datasource.getModuleGuid();
		module = moduleEngine.getModule(moduleGuid);
		system = moduleEngine.getSystem(module);
		type = bizEngine.getType(system);

		// 解析排序字段
		orderBy = datasource.getOrderBy();
		if (Str.isEmpty(orderBy)) {
			// 默认先按人工序正排，再按创建时间倒排
			if (Cls.hasField(type, F_ORDER_BY)) {
				orderBy = F_ORDER_BY + " asc";
			}
			if (Cls.hasField(type, F_CREATED)) {
				orderBy += (Str.isEmpty(orderBy) ? "" : ",") + F_CREATED + " desc";
			}
		}

		// 解析查询字段
		fields = Str.toList(datasource.getFieldRule(), ",|");

		// 解析分组字段
		groupBy = datasource.getGroupBy();

		// 解析栏目模块
		Map<String, IBizField> fldmap = bizEngine.getFieldsMap(system);
		for (String srcRule : srcRuleArray) {
			// 构造查询条件
			ExprRule exprRule = new ExprRule(srcRule);

			// 解析外键字段
			String fkfldName = exprRule.getField();
			if (!Str.isEmpty(fkfldName)) {
				int dot = fkfldName.indexOf(".");
				if (dot > 0) {
					fkfldName = fkfldName.substring(0, dot);
				}
				catalogField = fldmap.get(fkfldName);

				// 栏目模块解析成功
				if (catalogField != null && bizEngine.isSystemFK(catalogField)) {
					catalogModule = moduleEngine.getModule(Demsy.me().getSoft(), catalogField.getRefrenceSystem());
					catalogSystem = moduleEngine.getSystem(catalogModule);
					catalogType = bizEngine.getType(catalogSystem);

					break;
				}
			}
		}

		if (log.isTraceEnabled())
			log.tracef("板块数据集查询规则: [moduleGuid=%s, rules=%s, rules2=%s, ui:%s] module=%s, catalogModule=%s", moduleGuid, datasource.getRules(), datasource.getRules2(), block.getViewType(), module,
					catalogModule);

		// 解析动态模块
		boolean isFullDynaCatalogModule = false;
		boolean isDynaCatalogModule = false;
		boolean isDynaDataModule = false;
		if (datasource.isDynamic() && dynamicModuleID != null && dynamicModuleID > 0) {
			IModule dModule = moduleEngine.getModule(dynamicModuleID);
			if (dModule != null) {
				if (dModule.equals(catalogModule)) {
					isDynaCatalogModule = true;
				} else if (dModule.equals(module)) {
					isDynaDataModule = true;
				} else if (catalogModule == null) {
					// 全动态：数据源中没有指定外键字段，运行时自动获取外键字段
					catalogModule = dModule;
					catalogSystem = moduleEngine.getSystem(catalogModule);
					catalogType = bizEngine.getType(catalogSystem);

					isDynaCatalogModule = true;
					isFullDynaCatalogModule = true;
				}
			}
		}

		// 重构查询规则
		try {

			// 添加全动态外键字段作为板块数据集查询条件
			if (isFullDynaCatalogModule) {
				List<? extends IBizField> fields = bizEngine.getFieldsOfEnabled(system);
				IBizSystem refSystem = moduleEngine.getSystem(catalogModule);
				Class refType = bizEngine.getType(refSystem);
				for (IBizField fld : fields) {
					if (bizEngine.getType(fld).equals(refType)) {
						srcRuleArray.add(bizEngine.getPropName(fld) + ".id eq " + catalogModule.getId());

						break;
					}
				}
			}

			// 按上级板块过滤数据集：即查询主从属表
			if (this.isInherit() && parent.module != null && parent.itemObjs != null && parent.itemObjs.size() > 0) {
				List<? extends IBizField> fields = bizEngine.getFieldsOfEnabled(system);
				IBizSystem parentSystem = moduleEngine.getSystem(parent.module);
				Class parentType = bizEngine.getType(parentSystem);
				for (IBizField fld : fields) {
					if (bizEngine.getType(fld).equals(parentType)) {

						for (Object parentItem : parent.itemObjs) {
							srcRuleArray.add(bizEngine.getPropName(fld) + ".id eq " + Obj.getId(parentItem));
						}

						break;
					}
				}
			}

			// 解析查询条件
			int count = 0;
			for (String srcRule : srcRuleArray) {
				ExprRule exprRule = new ExprRule(srcRule);

				// 获取字段名称
				String fkFldName = exprRule.getField();
				if (Str.isEmpty(fkFldName)) {
					continue;
				}

				// 解析外键字段
				IBizField fkFld = null;
				String fkSubFldName = null;
				int dot = fkFldName.indexOf(".");
				if (dot > 0) {
					fkSubFldName = fkFldName.substring(dot + 1);
					fkFldName = fkFldName.substring(0, dot);
					fkFld = fldmap.get(fkFldName);
				}

				// 是外键字段且没有被解析过
				if (fkFld != null && bizEngine.isSystemFK(fkFld)) {
					// 外键字段类型
					Class fldClass = bizEngine.getType(fkFld);

					// 解析栏目对象
					Object catalogEntity = null;
					if (isDynaCatalogModule && fldClass.equals(catalogType)) {// 动态栏目
						catalogEntity = orm.load(fldClass, dynamicDataID);
					} else if (isDynaDataModule) {// 动态数据
						itemObj = orm.load(type, dynamicDataID);
						catalogEntity = Obj.getValue(itemObj, fkFldName);
					} else {// 固定栏目
						catalogEntity = orm.load(fldClass, Expr.eq(fkSubFldName, exprRule.getData()));
					}

					if (catalogEntity != null) {
						catalogObjs.add(catalogEntity);
						if (catalogObj == null) {
							catalogObj = catalogEntity;
						}
						rules.add(fkFldName + " eq " + Obj.getId(catalogEntity));
					} else {
						rules.add(fkFldName + " nu");
					}

					// 解析网站栏目 的 引用栏目
					if (IWebContentCatalog.class.isAssignableFrom(fldClass) && catalogEntity != null) {
						IWebContentCatalog webCata = (IWebContentCatalog) catalogEntity;
						if (webCata.getType() == IWebContentCatalog.TYPE_REF && webCata.getRefrence() != null) {
							if (webCata.getRefrence() != null)
								rules.add(fkFldName + " eq " + Obj.getId(webCata.getRefrence()));
						}
					}

				}
				// 不是外键栏目
				else {
					rules.add(srcRule);
					if (srcRule2Array.length > count)
						catalogObjs.add(srcRule2Array[count]);
				}

				count++;
			}

			if (isDynaDataModule && itemObj == null) {
				itemObj = orm.load(type, dynamicDataID);
			}
		} catch (Throwable e) {
			if (!appconfig.isProductMode() || log.isTraceEnabled())
				log.error("解析板块数据源出错! " + Ex.msg(e), e);
			else
				log.errorf("解析板块数据源出错! %s ", Ex.msg(e));
		}
	}

	public void put(String key, Object value) {
		props.put(key, value);
	}

	public <T> T get(String key) {
		return (T) props.get(key);
	}

	public String getImageField() {
		if (imageFld == null) {
			IUIViewComponent ui = block.getViewType();

			if (ui == null || !ui.isImageOptions()) {
				imageFld = "";
			} else {
				imageFld = getImageField(system);
				if (imageFld == null)
					imageFld = "";
			}
		}

		return imageFld;
	}

	private String getImageField(IBizSystem system) {
		IBizFieldType fldlib = bizEngine.getFieldTypes().get("Upload");
		for (IBizField f : bizEngine.getFieldsOfEnabled(system)) {
			if (!fldlib.equals(f.getType())) {
				continue;
			}

			String fileType = f.getUploadType();
			if (fileType != null && (fileType.indexOf(".jpeg") > -1 || fileType.indexOf(".jpg") > 0 || fileType.indexOf(".gif") > 0) || fileType.indexOf(".png") > 0) {
				String imgFld = bizEngine.getPropName(f);

				if (block.getDataset() != null) {
					String fldRule = block.getDataset().getFieldRule();
					if (!Str.isEmpty(fldRule)) {
						Pattern configPath = Pattern.compile(fldRule, Pattern.CASE_INSENSITIVE);
						if (configPath.matcher(imgFld).find()) {
							return imgFld;
						} else {
							continue;
						}
					} else {
						return imgFld;
					}
				}
			}
		}

		return null;
	}

	public UIBlockDataModel makeDataModel(Object obj) {
		return makeDataModel(obj, null, module == null ? null : module.getId());
	}

	public UIBlockDataModel makeDataModel(Object obj, Long linkID, Long moduleID) {
		UIBlockDataModel item = new UIBlockDataModel();

		item.setName(obj == null ? "" : obj.toString(), titleLen);

		if (obj instanceof IWebContent) {
			IWebContent info = (IWebContent) obj;
			if (info.getRefrence() != null) {
				obj = info.getRefrence();
			}
		}

		if (!Str.isEmpty(getImageField())) {
			Object img = Obj.getValue(obj, getImageField());
			if (img != null && !Str.isEmpty(img.toString())) {
				item.setImg(Demsy.contextPath + img.toString());
			} else {
				item.setImg("");
			}
		}

		if (linkID == null) {
			IPage link = block.getLink();
			if (link != null)
				linkID = link.getId();
		}
		if (obj != null) {
			if (obj instanceof IWebContent) {
				IWebContent info = (IWebContent) obj;

				if (!Str.isEmpty(info.getInfoLinkPath())) {
					item.setHref(MvcUtil.contextPath(info.getInfoLinkPath()));
				} else if (linkID != null && moduleID != null) {
					item.setHref(MvcUtil.contextPath(URL_UI, linkID, moduleID + ":" + Obj.getId(obj)));
				}

				item.setDate(info.getInfoDate());
			} else if (linkID != null && moduleID != null) {
				item.setHref(MvcUtil.contextPath(URL_UI, linkID, moduleID + ":" + Obj.getId(obj)));
				item.setDate(Dates.formatDate((Date) Obj.getValue(obj, F_CREATED)));
			}
			if (!Str.isEmpty(block.getLinkTarget())) {
				item.setTarget(block.getLinkTarget());
			}
		}
		if (obj instanceof IBizEntity) {
			Date updated = ((IBizEntity) obj).getUpdated();
			short days = 3;
			if (updated != null && new Date().getTime() - updated.getTime() <= days * 86400000) {
				if (titleLen != null && titleLen > 2)
					item.setName(obj == null ? "" : obj.toString(), titleLen - 2);

				item.setIsnew(true);
			}
		}

		item.setObj(obj);

		return item;
	}

	public List query() {
		return query(null);
	}

	public List query(CndExpr expr) {
		log.tracef("查询板块数据...");

		if (itemObjs == null) {
			if (this.datasource == null || Str.isEmpty(datasource.getModuleGuid())) {
				log.tracef("查询板块数据结束. [classOfEntity: null]");

				if (!isShareResult()) {
					itemObjs = new LinkedList();
				}
			} else {
				// 计算分页查询条件
				Pager pager = this.getPager(expr);

				// 获取业务管理器并查询数据
				IOrm orm = Demsy.orm();
				if (orm != null) {
					if (log.isTraceEnabled()) {
						itemObjs = orm.query(pager);

						log.tracef("查询板块数据结束. [result.size: %s]", itemObjs.size());

					} else {
						try {
							itemObjs = orm.query(pager);
						} catch (Throwable e) {
							log.errorf("查询板块数据出错! [page=%s, block=%s] %s", block.getPage(), block, e);
						}
					}
				}

				log.tracef("查询板块数据结束. [bizManager: null]");
			}
			if (itemObjs == null) {
				itemObjs = new LinkedList();
			}
		}

		return itemObjs;
	}

	/**
	 * 获取分页查询结果集： {$ctx.pager}
	 * 
	 * 
	 * @return
	 */
	public Pager getPager() {
		return getPager(null);
	}

	public Pager getPager(CndExpr expr) {
		if (pager == null) {
			pager = new Pager(type);
			if (expr == null)
				pager.setQueryExpr(getExpr());
			else {
				if (pageSize > 0) {
					expr.setPager(pageIndex, pageSize);
				}
				pager.setQueryExpr(expr);
			}
		}

		return pager;
	}

	public CndExpr getExpr() {
		if (getSystem() == null)
			return null;

		if (expr != null) {
			return expr;
		}

		expr = CndExpr.make(rules);
		String[] orderbys = Str.toArray(this.getOrderBy(), ",;");
		for (String orderby : orderbys) {
			if (expr == null) {
				expr = Expr.orderby(orderby);
			} else {
				expr = expr.addOrder(Expr.orderby(orderby));
			}
		}

		// if (!Str.isEmpty(groupBy)) {
		// if (expr == null)
		// expr = Expr.group(groupBy);
		// else
		// expr = expr.addGroup(groupBy);
		// }

		if (pageSize > 0) {
			if (expr == null)
				expr = Expr.page(pageIndex, pageSize);
			else
				expr.setPager(pageIndex, pageSize);
		}

		String imgFld = this.getImageField();
		if (!Str.isEmpty(imgFld) && !block.isAllowEmptyImg()) {
			if (IWebContent.class.isAssignableFrom(this.type)) {
				CndExpr subexpr = Expr.notNull(imgFld).and(Expr.ne(imgFld, "")).or(Expr.notNull("refrence"));
				expr = expr.and(subexpr);
			} else {
				expr = expr.and(Expr.notNull(imgFld)).and(Expr.ne(imgFld, ""));
			}
		}

		Dataset ds = block.getDataset();
		if (ds != null && fields != null && fields.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("id$");
			for (String f : fields) {
				sb.append("|").append(f);
			}
			expr.setFieldRexpr(sb.toString(), false);
		}

		return expr;
	}

	public List<List> querySplit(List result) {
		List<List> listlist = null;
		if (block.getHorizontal())
			listlist = splitListByRow(result, this.cellCount);
		else
			listlist = splitListByCol(result, this.cellCount);

		return listlist;
	}

	public static List splitListByRow(List datas, Integer rowCount) {
		if (rowCount == null || rowCount == 0) {
			rowCount = 1;
		}
		int datasLen = 0;
		if (datas != null) {
			datasLen = datas.size();
		}
		int columnCount = datasLen / rowCount;
		if (datasLen % rowCount != 0) {
			columnCount += 1;
		}
		return splitListByCol(datas, columnCount);
	}

	public static List<List> splitListByCol(List list, Integer columnCount) {
		if (columnCount == null || columnCount == 0) {
			columnCount = 1;
		}
		int datasLen = 0;
		if (list != null) {
			datasLen = list.size();
		}
		List<List> ret = new ArrayList();
		int fromIndex = 0;
		int toIndex = Math.min(columnCount, datasLen);
		while (fromIndex < datasLen) {
			ret.add(list.subList(fromIndex, toIndex));
			fromIndex = toIndex;
			toIndex = Math.min(fromIndex + columnCount, datasLen);
		}

		return ret;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public Class getType() {
		return type;

	}

	public List getCatalogObjs() {
		return catalogObjs;
	}

	public List getRules() {
		return rules;
	}

	public IModule getCatalogModule() {
		return catalogModule;
	}

	public IModule getModule() {
		return module;
	}

	public IBizSystem getSystem() {
		return system;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCellCount() {
		return cellCount;
	}

	public int getTitleLen() {
		return titleLen;
	}

	public Object getItemObj() {
		if (itemObj == null) {
			if (itemObjs != null && itemObjs.size() > 0) {
				itemObj = itemObjs.get(0);
			}
		}
		return itemObj;
	}

	public IPageBlock getBlock() {
		return block;
	}

	public Object getCatalogObj() {
		return catalogObj;
	}

	public IBizField getCatalogField() {
		return catalogField;
	}

	public UIPageView getPageView() {
		return pageView;
	}

	public List<String> getFields() {
		return fields;
	}

	public Long getDynamicDataID() {
		return dynamicDataID;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public Long getDynamicModuleID() {
		return dynamicModuleID;
	}

	private List copyQueryResult() {
		if (itemObjs == null) {
			return new ArrayList();
		}
		List ret = new ArrayList(itemObjs.size());
		ret.addAll(itemObjs);

		return ret;
	}

	/**
	 * 获取栏目数据模型。在模版代码中可以通过如下语句：
	 * <p>
	 * {$catalog}
	 * 
	 * @return
	 */
	public UIBlockDataModel getCatalog() {
		if (catalog == null) {

			catalog = new UIBlockDataModel();
			String t = block.getName();
			if (t == null)
				t = "";
			String postFlag = "{+}";

			if (!Str.isEmpty(t) && !t.startsWith(postFlag)) {
				catalog.setName(block.getName(), getTitleLen());
			} else if (catalogObj != null) {
				catalog.setName(catalogObj.toString() + t.replace(postFlag, ""), titleLen);
			} else if (module != null) {
				catalog.setName(module.getName() + t.replace(postFlag, ""), titleLen);
			} else {
				catalog.setName(t.replace(postFlag, ""), titleLen);
			}

			IPage link = block.getTitleLink();
			if (link != null) {
				if (catalogModule != null && catalogObj != null)
					catalog.setHref(MvcUtil.contextPath(URL_UI, link.getId(), catalogModule.getId() + ":" + Obj.getId(catalogObj)));
				else if (catalogModule != null)
					catalog.setHref(MvcUtil.contextPath(URL_UI, link.getId(), catalogModule.getId()));
				else
					catalog.setHref(MvcUtil.contextPath(URL_UI, link.getId()));
			}
			if (!Str.isEmpty(block.getTitleLinkTarget())) {
				catalog.setTarget(block.getTitleLinkTarget());
			}

			// Object img = Mirrors.getValue(catalog,
			// getImageField(this.catalogSystem));
			// if (img != null && !Str.isEmpty(img.toString())) {
			// data.setImg(Demsy.contextPath + img.toString());
			// }

			catalog.setObj(catalogObj);
		}

		return catalog;
	}

	/**
	 * 获取条目数据集合。在模版代码中可以通过如下语句：
	 * <p>
	 * {$datas}
	 * 
	 * @return
	 */
	public List<UIBlockDataModel> getItems() {
		if (items == null) {
			items = new LinkedList();

			List result = new ArrayList();
			if (getPageSize() == 1) {
				Object record = getItemObj();
				if (record != null) {
					result.add(record);
				}
			}
			if (result.size() == 0) {
				query();
				result = copyQueryResult();
			}

			if (block.isFillBlank()) {
				for (int i = result.size(); i < getPageSize(); i++) {
					result.add(null);
				}
			}

			if (getCellCount() == 1) {
				for (Object obj : result) {
					if (obj == null)
						items.add(new UIBlockDataModel());
					else
						items.add(makeDataModel(obj));
				}
			} else {
				List<List> listlist = querySplit(result);
				for (List list : listlist) {
					UIBlockDataModel row = new UIBlockDataModel();
					items.add(row);
					for (Object obj : list) {
						if (obj == null)
							row.addItem(makeDataModel(null));
						else
							row.addItem(makeDataModel(obj));
					}
				}
			}
		}

		return items;
	}

	public IBizSystem getCatalogSystem() {
		return catalogSystem;
	}

	public List<UIBlockDataModel> getCatalogs() {
		return catalogs;
	}

	public UIBlockDataModel getItem() {
		if (item == null && getItemObj() != null) {
			item = this.makeDataModel(itemObj);
		}
		return item;
	}
}
