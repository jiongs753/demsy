
<form onsubmit="return false;" method="post" class="bzsys">
	<fieldset>
		<input type="submit" class="ajaxSubmit" value="升级(安装)DEMSY平台" onclick='this.form.action="{$contextPath}/config/setupDemsy"' /> <input type="submit" class="ajaxSubmit" value="清空缓存"
			onclick='this.form.action="{$contextPath}/config/clrcache"' /> <input type="submit" class="ajaxSubmit" value="清空实体映射" onclick='this.form.action="{$contextPath}/config/clrmapping"' />
	</fieldset>

	<fieldset>
		<legend>升级旧版本</legend>
		<select id="" name="upgradeToSoft">
			<option value="">---选择应用软件---</option> {import file="../common/options.st" nodes=$softNodes.children}
		</select>
		<div class="space"></div>
		<input type="submit" class="ajaxSubmit" value="升级功能模块" onclick='this.form.action="{$contextPath}/config/upgradeModules/"+this.form.elements["upgradeToSoft"].value' />
		<input type="submit" class="ajaxSubmit" value="升级栏目信息" onclick='this.form.action="{$contextPath}/config/upgradeWebInfo/"+this.form.elements["upgradeToSoft"].value' />
	</fieldset>

	<fieldset>
		<legend>验证自定义系统</legend>
		<select id="" name="validateSoft">
			<option value="">---选择应用软件---</option> {import file="../common/options.st" nodes=$softNodes.children}
		</select>
		<div class="space"></div>
		<input type="submit" class="ajaxSubmit" value="验证" onclick='this.form.action="{$contextPath}/config/validateSystems/"+this.form.elements["validateSoft"].value' />
	</fieldset>
</form>
