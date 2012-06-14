<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>{$title}</title>
<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script type="text/javascript">
try {
	var doc=window.parent.document;
	var INDEX_MIDDLE_COLS = doc.getElementById("middle").cols;
	var INDEX_ROWS = doc.getElementById("index").rows;
}
catch (r) {
}
function hideLeft() {
	try {
		INDEX_MIDDLE_COLS = doc.getElementById("middle").cols;
		doc.getElementById("middle").cols = "0,5,*";
	}
	catch (r) {
	}
}
function showLeft(width) {
	try {
		if (width) {
			INDEX_MIDDLE_COLS = width + ",5,*";
			doc.getElementById("middle").cols = INDEX_MIDDLE_COLS;
			return;
		}
		doc.getElementById("middle").cols = INDEX_MIDDLE_COLS;
	}
	catch (r) {
	}
}
function hideTop() {
	try {
		INDEX_ROWS = doc.getElementById("index").rows;
		var top2Height = 35;
		try {
			var top2 = document.getElementById("top2");
			if (top2.clientHeight == 0) {
				setTimeout("hideTop();", 100);
				return;
			}
			top2Height = top2.clientHeight + 5;
		}
		catch (e) {
		}
		doc.getElementById("index").rows = top2Height + ",*,0";
	}
	catch (r) {
	}
}
function showTop(height) {
	try {
		if (height) {
			INDEX_ROWS = height + ",*,0";
			doc.getElementById("index").rows = INDEX_ROWS;
			return;
		}
		doc.getElementById("index").rows = INDEX_ROWS;
	}
	catch (r) {
	}
}
function setPageWidth(width) {
	try {
		if (width) {
			doc.getElementById("page").cols = "*," + width + ",*";
		} else {
			doc.getElementById("page").cols = "0,*,0";
		}
	}
	catch (r) {
	}
}
function logout(){
	$.post("{$contextPath}/logout", null, function(json) {
        if(json.success){
            if(json.redirect){
                window.parent.location.href=json.redirect;
            }else{
            	window.parent.location.href="{$contextPath}/admin";
            }
        }else{
            alert(json.message);
        }
	}, "json");
}
function exit(){
	$.post("{$contextPath}/logout", null, function(json) {
        if(json.success){
            window.parent.close();
        }else{
            alert(json.message);
        }
	}, "json");
}
</script>
<style>
.index_top {
	background-repeat: repeat;
	background-position: left top;
}

.index_top_1 {
	width: 100%;
	background-image: url({$contextPath}/themes2/images/index_top_1_bg.jpg);
	background-repeat: repeat-x;
	background-position: center top;
	padding: 0;
	margin: 0;
	height: {$topHeight}px;
	overflow: hidden;
}

.index_top_1 .begin {
	align: left;
	text-align: left;
}

.index_top_1 .end {
	align: right;
	text-align: right;
}

.index_top_2 {
	width: 100%;
	border: solid 1px #01728e;
	background-image: url({$contextPath}/themes2/images/index_top_2_bg.jpg);
	background-repeat: repeat-x;
	background-position: left top;
	padding: 0;
	margin: 0;
	height: 25px;
	overflow: hidden;
}

.index_top_2 .top_menu {
	text-align: right;
	font-size: 13px;
	font-weight: bold;
	v-align: middle;
	white-space: nowrap;
	color: #585858;
}

.index_top_2 .top_logininfo{
	padding: 5px 0 0 0;
	font-size: 13px;
	color: #585858;
	float: left;
	font-weight: bold;
    /*
	filter: Dropshadow(offx=1, offy=0, color=#585858)
		Dropshadow(offx=0, offy=1, color=#585858)
		Dropshadow(offx=-1, offy=0, color=#585858)
		Dropshadow(offx=0, offy=-1, color=#585858);
        */
}

.index_top_2 .top_dateinfo {
	padding: 5px 0 0 0;
	font-size: 13px;
	color: #585858;
	float: left;
	font-weight: bold;
    /*
	filter: Dropshadow(offx=1, offy=0, color=#585858)
		Dropshadow(offx=0, offy=1, color=#585858)
		Dropshadow(offx=-1, offy=0, color=#585858)
		Dropshadow(offx=0, offy=-1, color=#585858);
        */
}

.index_top_2 .arrows1 {
	cursor: pointer;
	padding: 5px 0 0 0;
}

.index_top_2 .arrows2 {
	cursor: pointer;
	padding: 2px 5px 0 0;
}

a {
	white-space: nowrap;
	color: #330099;
	text-decoration: none
}

a:hover {
	white-space: nowrap;
	color: #FF0000;
	text-decoration: underline
}

</style>
</head>
<body>
	<div id="top1" class="index_top_1">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="begin"><img border="0" src="{$logo}" /></td>
				<td class="text">&nbsp;</td>
				<td class="end"><img border="0" src="{$contextPath}/themes2/images/index_top_end.jpg" /></td>
			</tr>
		</table>
	</div>
	<table id="top2" class="index_top_2" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="5" align="left" valign="top"><div class="arrows1">
					<img id="imgLeftRight" onclick="
if(this.src.indexOf('left')>0){
  hideLeft();
  this.src='{$contextPath}/themes2/images/index_btn_arrow_right.jpg'
}else{
  showLeft();
  this.src='{$contextPath}/themes2/images/index_btn_arrow_left.jpg'
}
" src="{$contextPath}/themes2/images/index_btn_arrow_left.jpg" />
				</div></td>
			<td width="5">&nbsp;</td>
			<td class="top_logininfo">
                         登录帐号：{$user.username}&nbsp;{if $user.name != null}用户名称：{$user.name}{/if}
            </td>
			<td width="40">&nbsp;</td>
			<td width="40">&nbsp;</td>
            <td width="60" class="top_menu" align="right"><a href="javascript:logout();">退出系统</a></td>
            <td width="60" class="top_menu" align="right"><a href="{$contextPath}/admin/chgpwdform" target="body">修改密码</a></td>
			<td width="60" class="top_menu" align="right"><a href="{$contextPath}/index" target="_index_">网站首页</a></td>
			<td width="60" class="top_menu" align="right"><a href="{$contextPath}{$rightUrl}" target="body">后台首页</a></td>
			<td width="10">&nbsp;</td>
			<td width="5" align="right" valign="top"><div class="arrows2">
					<img id="imgTopDown" onclick="
if(this.src.indexOf('up')>0){
  document.getElementById('top1').style.display='none';
  hideTop();
  this.src='{$contextPath}/themes2/images/index_btn_arrow_down.jpg'
}else{
  document.getElementById('top1').style.display='block';
  showTop();
  this.src='{$contextPath}/themes2/images/index_btn_arrow_up.jpg'
}
" src="{$contextPath}/themes2/images/index_btn_arrow_up.jpg" />
				</div></td>
		</tr>
	</table>
</body>
</html>
