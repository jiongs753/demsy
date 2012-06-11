[ {
	typeCode : 101,
	code : '101',
	name : '新增',
	orderby : 1,
	mode : 'c'
}, {
	typeCode : 102,
	code : '102',
	name : '编辑',
	orderby : 2,
	mode : 'e'
}, {
	typeCode : 299,
	code : '299',
	name : '删除',
	orderby : 3,
	mode : 'd'
}, {
	typeCode : 106,
	code : '106',
	name : '打印',
	orderby : 4,
	targetWindow : '_blank',
	template: 'ui.widget.BizPrint',
	mode : 'p'
}, {
	typeCode : 107,
	code : '107',
	name : '导出到XLS',
	orderby : 5,
	template: 'ui.widget.ExportToXls',
	mode : 'tox'
}, {
	typeCode : 108,
	code : '108',
	name : '从XLS导入',
	orderby : 6,
	template: 'ui.widget.ImportFromXls',
	targetWindow : '_blank',
	mode : 'inx'
}, {
	typeCode : 109,
	code : '109',
	name : '批量新增',
	orderby : 7,
	mode : 'c_n'
}, {
	typeCode : 298,
	code : '298',
	name : '清空',
	orderby : 7,
	mode : 'clr'
}, {
	typeCode : 250,
	code : '250',
	orderby : 10,
	name : '排序',
	mode : 'odr',
	children : [ {
		typeCode : 251,
		code : '251',
		name : '上移',
		orderby : 11,
		mode : 'odr1'
	}, {
		typeCode : 252,
		code : '252',
		name : '下移',
		orderby : 12,
		mode : 'odr2'
	}, {
		typeCode : 253,
		code : '253',
		name : '反转',
		orderby : 13,
		mode : 'odr3'
	} ]
}, {
	typeCode : 901,
	code : '901',
	name : '状态设置',
	orderby : 20,
	mode : 'st'
}, {
	typeCode : 201,
	code : '201',
	orderby : 101,
	name : '保存',
	mode : 'sv'
}, {
	typeCode : 103,
	code : '103',
	orderby : 102,
	name : '批量修改',
	mode : 'bu'
}, {
	typeCode : 104,
	code : '104',
	orderby : 103,
	name : '执行业务插件(带窗体)',
	mode : 'xf1'
}, {
	typeCode : 204,
	code : '204',
	orderby : 104,
	name : '执行业务插件(无窗体)',
	mode : 'xf2'
}, {
	typeCode : 105,
	code : '105',
	orderby : 105,
	name : '执行业务插件(带窗体|后台运行)',
	mode : 'x1'
}, {
	typeCode : 205,
	code : '205',
	orderby : 106,
	name : '执行业务插件(无窗体|后台运行)',
	mode : 'x2'
} ]