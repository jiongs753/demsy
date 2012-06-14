<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>{$title}</title>
<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.js" type="text/javascript"></script>
</head>

<style>
html,body {
    font-size: 12px;
    padding: 0px;
    margin: 0px;
    height: 100%;
    width: 100%;
}

.label {
    color: #585858;
    text-decoration: none;
    font-weight: bold;
    width: 30%;
    text-align: right;
}

.input input {
    width: 180px;
    height: 16px;
}
</style>
<body class="login_body">
    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
        <tr>
            <td>&nbsp;</td>
            <td width="500px">
                <div class="login_box">
                    {if $user == null}
                    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
                        <tr>
                            <td align="center"><span style="color: red">您尚未登录或登录已过期!</span></td>
                        </tr>
                    </table>
                    {else}
                    <form onsubmit="return false;">
                        <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
                            <tr>
                                <td class="label">登录帐号：</td>
                                <td class="input">{$user.username}</td>
                            </tr>
                            <tr>
                                <td class="label">原始密码：</td>
                                <td class="input"><input type='password' name="_loginpwd_" /></td>
                            </tr>
                            <tr>
                                <td class="label">新密码：</td>
                                <td class="input"><input type='password' name="rawPassword" /></td>
                            </tr>
                            <tr>
                                <td class="label">确认密码：</td>
                                <td class="input"><input type='password' name="rawPassword2" /></td>
                            </tr>
                            <tr align="left">
                                <td align="right">&nbsp;</td>
                                <td style="padding: 5px 0 0 0;"><input class="submitButton" onclick='this.form.action="{$contextPath}/admin/chgpwd"' name="Submit" type="submit" value="确定">
                                </td>
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
$(document).ready(function(){$.doTimeout(100, function() {
	$(".submitButton").click(function() {
        var $form=$(this.form);
        $form.block();
        $.post(this.form.action, $form.serialize(), function(json) {
            $form.get(0).reset();
            alert(json.message);
		}, "json");
        

		return false;
	});
});});

</script>
