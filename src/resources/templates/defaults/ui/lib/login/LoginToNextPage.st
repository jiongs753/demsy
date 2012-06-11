{* 登录表单： 用于独立登录页面。标题链接——登录成功或已经登录则自动跳转到该页面；条目链接——点击注册则跳转到该页面。 *}
<form id="form_{$ui.uiid}" onsubmit="return false;">
	{if $login !=null}
		<script type="text/javascript">
			window.document.location.href="{$titleUrl}";
		</script>
	{else}
		{if $block.horizontal}
		<input name="_loginrealm_" value="02" type="hidden" />
		<table>
			<tr>
				<td class="it">登录帐号：</td>
				<td><input class="ir input" type='text' name="_loginuser_" /></td>
				<td class="it">登录密码：</td>
				<td><input class="ir input" type='password' name="_loginpwd_" /></td>
				<td>
					<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/login"' value="登录" type="submit" />
					<input class="ir submitButton" onclick='window.document.location.href="{$itemUrl}";' value="注册" type="submit" />
				</td>
			</tr>
		</table>
		{else}
	    <input name="_loginrealm_" value="02" type="hidden" />
		<ul>
			<li class="i">登录帐号：<input class="ir input" type='text' name="_loginuser_" /></li>
			<li class="i">登录密码：<input class="ir input" type='password' name="_loginpwd_" /></li>
			<li class="i">
				<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/login"' value="登录" type="submit" />
				<input class="ir submitButton" onclick='window.document.location.href="{$itemUrl}";' value="注册" type="submit" />
			</li>
		</ul>
		{/if}
	{/if}
</form>
<script type="text/javascript">
$(function() {
	var form=$("#form_{$ui.uiid}");
	$(".login", form).click(function() {
		form.block(); 
		$.post(this.form.action, form.serialize(), function(json) {
            if(json.success){
            	window.document.location.href="{$titleUrl}";
            }else{
                form.get(0).reset();
                alert(json.message);
            }
		}, "json");

		return false;
	});
});
</script>
