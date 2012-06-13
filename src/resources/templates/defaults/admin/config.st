<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>{$title}</title>

<link href="{$stylePath}/jquery.uploadify.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.theme.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.tabs.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.button.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.datepicker.css" rel="stylesheet" type="text/css" media="screen" />

<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.core.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.widget.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.tabs.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.button.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.uploadify.js" type="text/javascript"></script>
<script src="{$scriptPath}/swfobject.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.datepicker-zh-CN.js" type="text/javascript"></script>

</head>

<style>
.bzsys,.bzgrp {
	font-size: 12px;
}

.bzgrp_head td{
	height: 28px;
	border: 0px solid;
}

.bzfld_head {
	width: 150px;
	white-space: nowrap;
}

input[type=text], input[type=password], select, textarea
	{
	width: 200px;
	height: 16px;
	padding-top: 4px;
	padding-left: 2px;
	border: solid 1px #c5dbec;
	margin: 0;
}

select {
	padding-top: 0;
	width: 205px;
	height: 23px;
}

option {
	height: 19px;
}

.textarea textarea {
	width: 80%;
	height: 80px;
	overflow: auto;
}

.bzgrp td {
	border-top: 1px solid #c5dbec;
	border-left: 1px solid #c5dbec;
	padding: 3px 6px 3px 3px;
	background: #fff;
}

.bzgrp .upload .progress {
	width: 502px;
}

.space {
  margin: 5px 0 5px 0;
  padding: 0;
  border-top: 1px solid #a6c9e2; 
  height: 1px;
}
fieldset{
  border: 1px solid #a6c9e2; 
  margin: 5px;
  padding: 10px 5px 10px 5px;
}
.upload{
	width: 100%;
	position: relative;
	left: 0;
	top: 0;
}
.uploadifyQueue{
	width: 100%;
	position: absolute;
	left: 0;
	top: 0;
}
</style>
<body>
	<div id="tabs" class="main_bg" style="display: none;">
		<ul>
			<li><a href="#appconfig">参数配置</a></li>
			<li><a href="#dbconfig">数据库配置</a></li>
			<li><a href="#utils">系统工具</a></li>
			<li><a href="#patch">更新补丁</a></li>
			<li><a href="#index">系统首页</a></li>
		</ul>
		<div id="appconfig">{import file="_cfg_app.st"}</div>
		<div id="dbconfig">{import file="_cfg_db.st"}</div>
		<div id="utils">{import file="_cfg_utils.st"}</div>
		<div id="patch">{import file="_cfg_patch.st"}</div>
		<div id="index">{import file="_cfg_idx.st"}</div>
	</div>
</body>
</html>

<script type="text/javascript">
	$(".datepicker").datepicker();
    $("#tabs").tabs().show();
	$("input:submit").button();
	$(".ajaxSubmit").click(function() {
		$(this.form).block(); 
		$.post(this.form.action, $(this.form).serialize(), function(json) {
           	alert(json.message);
		}, "json");

		return false;
	});
</script>
