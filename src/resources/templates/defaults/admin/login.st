<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>{$title}</title>
<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.js" type="text/javascript"></script>
<script type="text/javascript">
function logout(){
    $.post("{$contextPath}/logout", null, function(json) {
        if(json.success){
            if(json.redirect){
                window.parent.location.href=json.redirect+"?_date_="+new Date().getTime();
            }else{
                window.parent.location.href="{$contextPath}/admin?_date_="+new Date().getTime();
            }
        }else{
            alert(json.message);
        }
    }, "json");
}
</script>
</head>

<style>
html,body {
	font-size: 12px;
	padding: 0px;
	margin: 0px;
	height: 100%;
	width: 100%;
}

.login_body {
	text-align: center;
	background-image: url({$loginBodyBg});
	background-position: center top;
	background-repeat: repeat;
    color: #ffffff;
	font-weight: bold;
}

.login_box {
	background-image: url({$loginBoxBg});
	background-position: center middle;
	background-repeat: no-repeat;
	text-align: left;
	width: 772px;
	height: 313px;
	align: left;
	position: relative;
	top: 80px;
	padding: 180px 0 0 0;
}

.login_errorinfo {
	position: relative;
	top: -20px;
	text-align: left;
	align: left;
	width: 772px;
	color: red;
	font-size: 15px;
	text-align: left;
}

.label {
	color: #ffffff;
	text-decoration: none;
	font-weight: bold;
	width: 30%;
	text-align: right;
}

.input input {
	width: 180px;
	height: 16px;
	border: 1 solid #ffffff;
    font-size: 12px;
}

.input select {
    font-size: 12px;
	width: 186px;
	height: 22px;
	border: 1 solid #ffffff;
}
</style>
<body class="login_body">
	<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
		<tr>
			<td>&nbsp;</td>
			<td width="500px">
				<div class="login_box">
					{if $user != null}
					<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td width="30%" align="right">【{$user.username}】您已经登录! </td>
							<td width="70%">&nbsp;&nbsp;<a href="javascript:logout();">重新登录</a>&nbsp;&nbsp;<a target="_parent" href="{$contextPath}/admin">进入系统</a></td>
						</tr>
					</table>
					{else}
					<form onsubmit="return false;">
						<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
							<tr>
								<td class="label">登录类型：</td>
								<td class="input"><select name="_loginrealm_">
                                        <option value="01">管理员登录</option>
                                        <option value="">---无---</option>
								</select></td>
							</tr>
							<tr>
								<td class="label">登录帐号：</td>
								<td class="input"><input type='text' name="_loginuser_" /></td>
							</tr>
							<tr>
								<td class="label">登录密码：</td>
								<td class="input"><input type='password' name="_loginpwd_" /></td>
							</tr>
							<tr align="left">
								<td align="right">&nbsp;</td>
								<td style="padding: 5px 0 0 0;"><input class="submitButton" onclick='this.form.action="{$contextPath}/login"' name="Submit" type="image" src="{$loginBtn}"></td>
							</tr>
						</table>
					</form>
				{/if}</div></td>
			<td>&nbsp;</td>
		</tr>
	</table>
</body>
</html>
<script type="text/javascript">
	$(".submitButton").click(function() {
        var $form=$(this.form);
        $form.block();
        var data=$form.serialize()+"&clientWidth="+document.body.clientWidth+"&clientHeight="+document.body.clientHeight;
        $.post(this.form.action, data, function(json) {
            if(json.success){
	            if(json.redirect){
	                window.location.href=json.redirect;
	            }else{
	            	window.location.href="{$contextPath}/admin";
	            }
            }else{
                alert(json.message);
            }
		}, "json");
        

		return false;
	});

</script>
<link href="{$stylePath}/jquery.ui.core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.theme.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.button.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />

<script src="{$scriptPath}/jquery.ui.core.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.widget.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.button.js" type="text/javascript"></script>
