// 以_开头的bean为内部使用
{
	_nlsConfig : {
		type : "com.kmetop.demsy.config.impl.NlsConfig"
	},
	_entityListeners : {// 实体DAO监听器
		type : "com.kmetop.demsy.orm.listener.EntityListeners",
		fields : {
			listeners : [ {
				type : "com.kmetop.demsy.orm.listener.impl.DemsyEntityListener"
			} ]
		}
	},
	_passwordEncoder : {// 密码加密器
		type : "com.kmetop.demsy.security.encoding.Md5PasswordEncoder"
	},
	bizSession : {// 业务会话
		type : "com.kmetop.demsy.biz.impl.BizSessionImpl"
	},
	security : {// 安全控制器
		type : "com.kmetop.demsy.security.impl.Security",
		fields : {
			defaultPasswordEncoder : {refer : "_passwordEncoder"}
		}
	},
	bizManagerFactory : {// 业务管理器工厂
		type : "com.kmetop.demsy.biz.impl.BizManagerFactory"
	},
	uiEngine : {// UI工厂
		type : "com.kmetop.demsy.engine.UiEngine"
	},
	
	// ==============================可扩展部分==============================
	// SFT版IOC配置：
	_dbConfig : {// 数据库默认配置
		type : "com.kmetop.demsy.config.impl.DbConfig"
	},
	moduleEngine : {// 组件库：SFT实现
		type : "com.kmetop.demsy.comlib.impl.engine.SFTModuleEngine"
	},
	bizEngine : {// 业务引擎：SFT实现
		type : "com.kmetop.demsy.comlib.impl.engine.SFTBizEngine",
		fields: {
			pkgs: ["com.kmetop.demsy.comlib.impl.entity"
					, "com.kmetop.demsy.comlib.impl.base"
					, "com.kmetop.demsy.comlib.impl.sft"
					, "com.kmetop.ebuziness"
				]
		}
	}
}