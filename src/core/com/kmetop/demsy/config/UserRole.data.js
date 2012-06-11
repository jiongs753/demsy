[ {
	name : '内部角色',
	code : 'InnerUsers',
	orderby : -100,
	inner : true,
	children : [ {
		name : '登录用户',
		code : 'LoginUser',
		inner : true,
		type : 1,
		orderby : -90
	}, {
		name : '匿名用户',
		code : 'AnonymousUser',
		inner : true,
		type : 0,
		orderby : -80
	} ]
}, {
	name : '普通管理员',
	code : 'User',
	type : 90,
	orderby : 1
}, {
	name : '超级管理员',
	code : 'AdminUser',
	type : 100,
	orderby : 1
} ]