{* 登录信息： 用于显示登录信息或自动跳转到登录页面。标题链接——未登录则自动跳转到该页面。条目链接——已经登录并点击会员中心按钮则跳转到该页面  *}
<form id="form_{$ui.uiid}" onsubmit="return false;">
	{if $login !=null}
		{if $block.horizontal}
		<table>
			<tr>
				<td class="it">登录帐号：</td>
				<td>{$login.username}</td>
				<td class="i">
					<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/logout"' value="重新登录" type="submit" />
					<input class="ir submitButton btn2" onclick='window.document.location.href="{$itemUrl}";' value="个人中心" type="submit" />
				</td>
			</tr>
		</table>
		{else}
		<ul>
			<li class="i">登录帐号：{$login.username}</li>
			<li class="i">
				<input class="ir submitButton login" onclick='this.form.action="{$contextPath}/logout"' value="重新登录" type="submit" />
				<input class="ir submitButton btn2" onclick='window.document.location.href="{$itemUrl}";' value="个人中心" type="submit" />
			</li>
		</ul>
		{/if}
	{else}
		<script type="text/javascript">
			window.document.location.href="{$titleUrl}";
		</script>
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
