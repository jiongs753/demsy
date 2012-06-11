function getLodop(oOBJECT, oEMBED) {
	/***************************************************************************
	 * 本函数根据浏览器类型决定采用哪个对象作为控件实例： IE系列、IE内核系列的浏览器采用oOBJECT，
	 * 其它浏览器(Firefox系列、Chrome系列、Opera系列、Safari系列等)采用oEMBED。
	 **************************************************************************/
	var strHtml1 = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='/scripts2/plugins/install_lodop.exe'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
	var strHtml2 = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='/scripts2/plugins/install_lodop.exe'>执行升级</a>,升级后请重新进入。</font>";
	var strHtml3 = "<br><br><font color='#FF00FF'>(注：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸载它)</font>";
	var LODOP = oEMBED;
	try {
		if (navigator.appVersion.indexOf("MSIE") >= 0)
			LODOP = oOBJECT;

		if ((LODOP == null) || (typeof (LODOP.VERSION) == "undefined")) {
			if (navigator.userAgent.indexOf('Firefox') >= 0)
				document.documentElement.innerHTML = strHtml3 + document.documentElement.innerHTML;
			if (navigator.appVersion.indexOf("MSIE") >= 0)
				document.write(strHtml1);
			else
				document.documentElement.innerHTML = strHtml1 + document.documentElement.innerHTML;
		} else if (LODOP.VERSION < "6.0.1.0") {
			if (navigator.appVersion.indexOf("MSIE") >= 0)
				document.write(strHtml2);
			else
				document.documentElement.innerHTML = strHtml2 + document.documentElement.innerHTML;
		}
		// *****如下空白位置适合调用统一功能:*********

		LODOP.SET_LICENSES("昆明易极信息技术有限公司","055716580837383919278901905623","","");
		// *******************************************	
		return LODOP;
	} catch (err) {
		document.documentElement.innerHTML = "Error:" + strHtml1 + document.documentElement.innerHTML;
		return LODOP;
	}
}
/*
 * Lodop(4.0及之后版本)的注册号和使用方式：
在页面装载之后，或者在程序调用普通函数之前执行一次如下语句：
LODOP.SET_LICENSES("昆明易极信息技术有限公司","055716580837383919278901905623","","");
假如你采用了LodopFuncs.js文件(6.0版)，简单办法是把该语句放到文件内部那个注明“空白位置”的地方。
5.0及更早版本可以放到CheckActivX.js文件的合适位置。
另外：最新版本及其技术手册可从如下地址下载：
 http://mt.runon.cn/download.html
或 http://mtsoftware.v053.gokao.net/download.html
如果注册号测试无效，可用以下简单步骤排查：
一、在SET_LICENSES语句前后加alert提示语句，例如：
   LODOP.SET_LICENSES("昆明易极信息技术有限公司","055716580837383919278901905623","","");
   alert("SET_LICENSES执行了");
二、执行后如果没提示信息，就检查程序代码，很多时候是没调用getlodop缘故;
三、不想使用getlodop时可以把SET_LICENSES放到每个具体打印过程中，例如PRINT_INIT前面;
四、如仍有问题，请联系QQ:932131686
谢谢注册，感谢您对Lodop的支持！
 * 
 */