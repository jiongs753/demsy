[ {
	name : "视图控件",
	code : "basic",
	orderby : 99,
	type : 1,
	children : [ {
		name : "视图控制器",
		code : "viewController",
		orderby : 1,
		viewTemplate : "ui.view.UIViewController",
		viewController : "com.kmetop.demsy.mvc.controller.UIViewController",
		desc : "用于控制实体数据，但不直接将实体数据展现在界面上，展现任务交由子视图完成（如：在视图控制器内添加列表视图、表达式视图等）",
		defaultWidth : 300,
		defaultHeight : 300
	}, {
		name : "列表视图",
		code : "listView",
		orderby : 3,
		viewTemplate : "ui.view.UIListView",
		viewController : "com.kmetop.demsy.mvc.controller.UIViewController",
		desc : "列表视图中的子视图(集)将作为迭代模版，并循环显示数据源指定的数据集或父视图中指定的数据集",
		defaultWidth : 300,
		defaultHeight : 300
	}, {
		name : "表达式视图",
		code : "expressionView",
		orderby : 3,
		viewController : "com.kmetop.demsy.mvc.controller.UIViewController",
		desc : "按“视图表达式”中指定的内容展现数据",
		defaultWidth : 200,
		defaultHeight : 30
	}, {
		name : "视图面板",
		code : "panelView",
		orderby : 4,
		viewTemplate : "ui.view.UIPanelView",
		desc : "将子视图按行或列方式排列",
		defaultWidth : 300,
		defaultHeight : 300
	}, {
		name : "页面分享",
		code : "shareView",
		orderby : 5,
		viewTemplate : "ui.view.UIShareView",
		desc : "分享页面到博客、微博等",
		defaultWidth : 500,
		defaultHeight : 30
	} ]
}, {
	name : "更多控件",
	code : "_more",
	orderby : 100,
	type : 1,
	children : [ {
		name : "文字列表",
		code : "_list_text",
		orderby : 101,
		titleOptions : true,
		imageOptions : false,
		summOptions : false,
		scrollOptions : true,
		viewTemplate : "ui.lib.ListText",
		viewController : "UiDataset"
	}, {
		name : "图片列表",
		code : "_list_image",
		orderby : 102,
		titleOptions : true,
		imageOptions : true,
		summOptions : false,
		scrollOptions : true,
		viewTemplate : "ui.lib.ListImage",
		viewController : "UiDataset"
	}, {
		name : "图文列表-1",
		code : "_list_image_text",
		orderby : 103,
		titleOptions : true,
		imageOptions : true,
		summOptions : true,
		scrollOptions : true,
		viewTemplate : "ui.lib.ListImageText",
		viewController : "UiDataset"
	}, {
		name : "图文列表-2",
		code : "_list_image_text2",
		orderby : 104,
		titleOptions : true,
		imageOptions : true,
		summOptions : true,
		scrollOptions : true,
		viewTemplate : "ui.lib.ListImageText2",
		viewController : "UiDataset"
	}, {
		name : "图文列表-3",
		code : "_list_image_text3",
		orderby : 105,
		titleOptions : true,
		imageOptions : true,
		summOptions : true,
		scrollOptions : true,
		viewTemplate : "ui.lib.ListImageText3",
		viewController : "UiDataset"
	}, {
		name : "图片幻灯",
		code : "_list_image_slide",
		orderby : 106,
		titleOptions : true,
		imageOptions : true,
		summOptions : true,
		scrollOptions : true,
		viewTemplate : "ui.lib.ListImageSlide",
		viewController : "UiDataset",
		defaultWidth : 500,
		defaultHeight : 200
	}, {
		name : "选项卡(容器)",
		code : "_tabs",
		orderby : 107,
		titleOptions : false,
		imageOptions : false,
		summOptions : false,
		scrollOptions : false,
		viewTemplate : "ui.lib.Tabs",
		viewController : "TabsDataset"
	}, {
		name : "选项卡列表",
		code : "_tabs_text",
		orderby : 108,
		titleOptions : true,
		imageOptions : false,
		summOptions : false,
		scrollOptions : false,
		viewTemplate : "ui.lib.TabsText",
		viewController : "TabsDataset"
	}, {
		name : "网站信息(专题列表)",
		code : "_special_webinfo",
		titleOptions : true,
		imageOptions : true,
		summOptions : true,
		scrollOptions : false,
		orderby : 109,
		viewTemplate : "ui.lib.SpecialWebInfo",
		viewController : "SpecialWebInfo"
	}, {
		name : "检索结果",
		code : "_search_result",
		orderby : 110,
		titleOptions : true,
		imageOptions : true,
		summOptions : false,
		scrollOptions : true,
		viewTemplate : "ui.lib.SearchResult",
		viewController : "Search"
	}, {
		name : "自定义表格",
		code : "_table",
		orderby : 111,
		titleOptions : true,
		imageOptions : false,
		summOptions : false,
		scrollOptions : true,
		viewTemplate : "ui.lib.Table",
		viewController : "UiDataset"
	}, {
		name : "分组文字列表",
		code : "_grouplist_text",
		orderby : 112,
		titleOptions : true,
		imageOptions : false,
		summOptions : false,
		scrollOptions : true,
		viewTemplate : "ui.lib.GroupListText",
		viewController : "GroupDataset"
	}, {
		name : "论坛帖子列表",
		code : "_bbs_topiclist",
		orderby : 121,
		titleOptions : true,
		imageOptions : false,
		summOptions : false,
		scrollOptions : true,
		viewTemplate : "ui.lib.BbsForum",
		viewController : "LoadBbsForum"
	}, {
		name : "论坛帖子回复列表",
		code : "_bbs_topic",
		orderby : 122,
		titleOptions : true,
		viewTemplate : "ui.lib.BbsTopic",
		viewController : "LoadBbsTopic"
	}, {
		name : "博客文章列表",
		code : "_blog_topiclist",
		orderby : 123,
		titleOptions : true,
		imageOptions : false,
		summOptions : false,
		scrollOptions : true,
		viewTemplate : "ui.lib.BlogList",
		viewController : "LoadBlogList"
	}, {
		name : "博客文章全文浏览",
		code : "_blog_topic",
		orderby : 124,
		titleOptions : true,
		viewTemplate : "ui.lib.BlogTopic",
		viewController : "LoadBlogTopic"
	}, {
		name : "网站信息(全文阅读)",
		code : "_view_news",
		titleOptions : true,
		orderby : 201,
		viewTemplate : "ui.lib.DetailContent",
		viewController : "LoadDetailContent"
	}, {
		name : "网站信息(内容字段)",
		code : "_html_text",
		orderby : 202,
		viewTemplate : "ui.lib.HtmlText",
		viewController : "UiRecord"
	}, {
		name : "网站信息(摘要字段)",
		code : "_desc",
		orderby : 203,
		summOptions : true,
		viewTemplate : "ui.lib.Desc",
		viewController : "UiRecord"
	}, {
		name : "网站栏目(标题字段)",
		code : "_title",
		titleOptions : true,
		orderby : 204,
		viewTemplate : "ui.lib.Title",
		viewController : "UiRecord"
	}, {
		name : "图片字段",
		code : "_other_image",
		orderby : 205,
		imageOptions : true,
		viewTemplate : "ui.lib.OtherImage",
		viewController : "UiRecord"
	}, {
		name : "FLASH",
		code : "_other_media",
		orderby : 206,
		imageOptions : true,
		viewTemplate : "ui.lib.Media",
		viewController : "UiRecord"
	}, {
		name : "会员登录表单",
		code : "_form_login",
		orderby : 301,
		titleOptions : true,
		viewTemplate : "ui.lib.LoginForm",
		viewController : "LoginInfo"
	}, {
		name : "修改我的密码",
		code : "_my_pwd",
		orderby : 302,
		titleOptions : true,
		viewTemplate : "ui.lib.MyPwd",
		viewController : "LoginInfo"
	}, {
		name : "修改我的资料",
		code : "_my_info",
		orderby : 303,
		titleOptions : true,
		viewTemplate : "ui.lib.MyInfo",
		viewController : "MyInfo"
	}, {
		name : "我发表的评论",
		code : "_my_commentlist",
		orderby : 304,
		titleOptions : true,
		viewTemplate : "ui.lib.MyCommentList",
		viewController : "MyCommentList"
	}, {
		name : "我的留言咨询",
		code : "_my_questionlist",
		orderby : 305,
		titleOptions : true,
		viewTemplate : "ui.lib.MyQuestionList",
		viewController : "MyQuestionList"
	}, {
		name : "我的论坛帖子",
		code : "_my_bbslist",
		orderby : 306,
		titleOptions : true,
		viewTemplate : "ui.lib.MyBbsList",
		viewController : "MyBbsList"
	}, {
		name : "我的博客文章",
		code : "_my_bloglist",
		orderby : 307,
		titleOptions : true,
		viewTemplate : "ui.lib.MyBlogList",
		viewController : "MyBlogList"
	}, {
		name : "我的订单列表",
		code : "_my_orderlist",
		orderby : 308,
		titleOptions : true,
		viewTemplate : "ui.lib.MyOrderList",
		viewController : "MyOrderList"
	}, {
		name : "我参与的活动",
		code : "_my_activitylist",
		orderby : 309,
		titleOptions : true,
		viewTemplate : "ui.lib.MyActivityList",
		viewController : "MyActivityList"
	}, {
		name : "我的购物车",
		code : "_my_shopcar",
		orderby : 310,
		titleOptions : true,
		viewTemplate : "ui.lib.MyShopCart",
		viewController : "MyShopCart"
	}, {
		name : "我的订单确认",
		code : "_my_orderconfirm",
		orderby : 310,
		titleOptions : true,
		viewTemplate : "ui.lib.MyOrderConfirm",
		viewController : "MyOrderConfirm"
	}, {
		name : "业务表单",
		code : "_form_edit",
		orderby : 901,
		titleOptions : true,
		viewController : "BizRecord",
		viewTemplate : "ui.lib.BizForm"
	}, {
		name : "表单数据",
		code : "_view_form",
		orderby : 902,
		viewTemplate : "ui.lib.ViewForm"
	}, {
		name : "随机广告",
		code : "_other_advert",
		orderby : 903,
		imageOptions : true,
		viewTemplate : "ui.lib.OtherAdvert",
		viewController : "UiAdvertRecord"
	}, {
		name : "页面引用",
		code : "_ref_page",
		orderby : 906,
		viewTemplate : "ui.lib.RefPage"
	}, {
		name : "空白板块",
		code : "_empty",
		orderby : 907,
		viewTemplate : "ui.lib.Empty"
	}, {
		name : "搜索框",
		code : "_search_box",
		orderby : 908,
		viewTemplate : "ui.lib.SearchBox"
	} ]
} ]