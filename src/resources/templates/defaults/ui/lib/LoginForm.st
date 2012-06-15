{* 登录表单： 用于嵌入式登录页面。标题链接——点击会员中心按钮则跳转到该页面；条目链接——点击注册则跳转到该页面；登录成功或退出登录将自动刷新当前页面。 *}
<form id="form_{$ui.uiid}" onsubmit="return false;">
	{if $block.horizontal}{if $login !=null}
	<table>
		<tr>
			<td class="it">帐号：</td>
			<td>{$login.username}</td>
			<td>
				<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/logout"' value="重新登录" type="submit" />
				<input class="ir submitButton" onclick='window.document.location.href="{$titleUrl}";' value="个人中心" type="submit" />
			</td>
		</tr>
	</table>
	{else}
	<input name="_loginrealm_" value="02" type="hidden" />
	<table>
		<tr>
			<td class="it"><label for="_loginuser_">帐号：</label></td>
			<td><input class="ir input" id="_loginuser_" type=“text“ name="_loginuser_" /></td>
			<td class="it"><label for="_loginpwd_">密码：</label></td>
			<td><input class="ir input" id="_loginpwd_" type="password" name="_loginpwd_" /></td>
			<td>
				<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/login"' value="登录" type="submit" />
				<input class="ir submitButton" onclick='window.document.location.href="{$itemUrl}";' value="注册" type="submit" />
			</td>
		</tr>
	</table>
	{/if} {else}{if $login !=null}
	<ul>
		<li class="i">帐号：{$login.username}</li>
		<li class="i">
			<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/logout"' value="重新登录" type="submit" />
			<input class="ir submitButton btn2" onclick='window.document.location.href="{$titleUrl}";' value="个人中心" type="submit" />
		</li>
	</ul>
	{else}
    <input name="_loginrealm_" value="02" type="hidden" />
	<ul>
		<li class="i"><label for="_loginuser_">帐号：</label><input class="ir input" type=“text“ id="_loginuser_" name="_loginuser_" /></li>
		<li class="i"><label for="_loginpwd_">密码：</label><input class="ir input" type="password" id="_loginpwd_" name="_loginpwd_" /></li>
		<li class="i">
			<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/login"' value="登录" type="submit" />
			<input class="ir submitButton" onclick='window.document.location.href="{$itemUrl}";' value="注册" type="submit" />
		</li>
	</ul>
	{/if} {/if}
</form>
<script type="text/javascript">
$(function() {
	var form=$("#form_{$ui.uiid}");
	$(".login", form).click(function() {
		form.block(); 
		$.post(this.form.action, form.serialize(), function(json) {
            if(json.success){
            	//$("#block{$ui.id}").load("{$loadBlockUrl}{$block.id}");
            	window.document.location.reload();
            	//window.document.location.href="{$myhomeUrl}";
            }else{
                form.get(0).reset();
                alert(json.message);
            }
		}, "json");

		return false;
	});
});
</script>
