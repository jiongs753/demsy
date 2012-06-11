[ {
	type : 90,
	name : '平台管理',
	code : '_demsy_admin',
	orderby : -99,
	children : [ {
		type : 1,
		orderby : -10000,
		name : '平台环境配置',
		code : '_demsy_config',
		path : '/config/*'
	} ]
}, {
	type : 90,
	orderby : -98,
	code : '_soft_admin',
	name : '系统管理',
	children : [ {
		type : 1,
		orderby : -1,
		name : '上传文件管理',
		code : '_ajax_filemanager',
		path : '/scripts2/ckfinder/ckfinder.html'
	} ]
}, {
	type : 90,
	orderby : -97,
	code : '_demsy_console',
	name : '自定义控制台'
}, {
	type : 90,
	orderby : -96,
	code : '_soft_base',
	name : '基础数据维护'
}, {
	type : 90,
	orderby : -95,
	code : '_soft_web',
	name : '门户网站系统'
} ]