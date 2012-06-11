<form onsubmit="return false;" method="post" action="{$contextPath}/config/save" class="bzsys">
	<table class="bzgrp" border="0" valign="top" cellpadding="0" cellspacing="0">
		<thead class="bzgrp_head">
			<tr>
				<td colspan=2 class="ui-state-default">参数配置</td>
			</tr>
		</thead>
		<tbody class="bzgrp_body">
			<tr>
				<th class="bzfld_head">切换DEMSY平台</th>
				<td>
				<select id='project'>
					{foreach from=$projects item="item" key="idx"}
						<option value="{$item}">{$item}</option>
					{/foreach}
						<option value="" selected></option>
				</select></td>
			</tr>
			<tr>
				<th class="bzfld_head">默认企业用户名称</th>
				<td>
				<input type='text' id="corpname" name='app.demsy.corpname' value='{tostring bean=$app prop="demsy.corpname"}' />
				<select id='corpnamesel' style="display: none;" disabled>
					{tostring assign="corpname" bean=$app prop="demsy.corpname"}
					{foreach from=$default_customer_names item="item" key="idx"}
						<option value="{$item}" {if $item==$corpname}selected{/if}>{$item}</option>
					{/foreach}
				</select>
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">默认企业用户编号</th>
				<td>
				<input type='text' id="corpcode" name='app.demsy.corpcode' value='{tostring bean=$app prop="demsy.corpcode"}' readonly />
				<select id='corpcodesel' style="display: none;" disabled>
					{tostring assign="corpcode" bean=$app prop="demsy.corpcode"}
					{foreach from=$default_customer_codes item="item" key="idx"}
						<option value="{$item}" {if $item == $corpcode} selected {/if}>{$item}</option>
					{/foreach}
				</select>
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">默认应用软件名称</th>
				<td>
				<input type='text' id="softname" name='app.demsy.softname' value='{tostring bean=$app prop="demsy.softname"}' />
				<select id='softnamesel' style="display: none;" disabled>
					{tostring assign="softname" bean=$app prop="demsy.softname"}
					{foreach from=$default_software_names item="item" key="idx"}
						<option value="{$item}" {if $item == $softname} selected {/if}>{$item}</option>
					{/foreach}
				</select>
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">默认应用软件编号</th>
				<td>
				<input type='text' id="softcode" name='app.demsy.softcode' value='{tostring bean=$app prop="demsy.softcode"}' readonly />
				<select id='softcodesel' style="display: none;" disabled>
					{tostring assign="softcode" bean=$app prop="demsy.softcode"}
					{foreach from=$default_software_codes item="item" key="idx"}
						<option value="{$item}" {if $item == $softcode} selected {/if}>{$item}</option>
					{/foreach}
				</select>
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">开发模式</th>
				<td>{tostring bean=$app prop="mode.product" assign="productMode"} <input id="productMode1" type='radio' name="app.mode.product" value="true" {if $productMode== "true"}checked{/if} /><label
					for="productMode1">否</label> <input id="productMode0" type='radio' name="app.mode.product" value="false" {if $productMode !="true" }checked{/if} /><label for="productMode0">是</label>
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">隐私模式</th>
				<td>{tostring bean=$app prop="security.privacyMode" assign="privacyMode"}<input id="privacyMode1" type='radio' name="app.security.privacyMode" value="true" {if $privacyMode == "true"}checked{/if} /><label
					for="privacyMode1">是</label> <input id="privacyMode0" type='radio' name="app.security.privacyMode" value="false" {if $privacyMode !="true" }checked{/if} /><label for="privacyMode0">否</label>
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">图片域名</th>
				<td><input type='text' name="app.domain.image" value='{tostring bean=$app prop="domain.image"}' /></td>
			</tr>
			<tr>
				<th class="bzfld_head">脚本域名</th>
				<td><input type='text' name="app.domain.script" value='{tostring bean=$app prop="domain.script"}' /></td>
			</tr>
			<tr>
				<th class="bzfld_head">样式域名</th>
				<td><input type='text' name="app.domain.css" value='{tostring bean=$app prop="domain.css"}' /></td>
			</tr>
			<tr>
				<th class="bzfld_head">上传后缀<br />(允许上传的文件类型)</th>
				<td><div class="textarea">
						<textarea name="app.upload.filter" style="height: 120px;">{tostring bean=$app prop="upload.filter"}</textarea>
					</div></td>
			</tr>
			<tr>
				<th class="bzfld_head">主题路径</th>
				<td><input type='text' name="app.path.theme" value='{tostring bean=$app prop="path.theme"}' disabled /></td>
			</tr>
			<tr>
				<th class="bzfld_head">上传路径</th>
				<td><input type='text' name="app.path.upload" value='{tostring bean=$app prop="path.upload"}' disabled /></td>
			</tr>
			<tr>
				<th class="bzfld_head">配置路径</th>
				<td><input type='text' name="app.path.config" value='{tostring bean=$app prop="path.config"}' disabled /></td>
			</tr>
			<tr>
				<th class="bzfld_head">日志路径</th>
				<td><input type='text' name="app.path.logs" value='{tostring bean=$app prop="path.logs"}' disabled /></td>
			</tr>
			<tr>
				<th class="bzfld_head">临时路径</th>
				<td><input type='text' name="app.path.temp" value='{tostring bean=$app prop="path.temp"}' disabled /></td>
			</tr>
			<tr>
				<th class="bzfld_head">IOC配置文件</th>
				<td>{tostring bean=$app prop="config.ioc"}</td>
			</tr>
			<tr>
				<th class="bzfld_head">用户配置文件</th>
				<td>{tostring bean=$app prop="config.rootuser"}</td>
			</tr>
			<tr>
				<th class="bzfld_head">数据库配置文件</th>
				<td>{tostring bean=$app prop="config.db"}</td>
			</tr>
			<tr>
				<th class="bzfld_head">国际化配置文件</th>
				<td>{tostring bean=$app prop="config.nls"}</td>
			</tr>
			<tr>
				<th class="bzfld_head">环境路径</th>
				<td>{$app.contextPath}</td>
			</tr>
			<tr>
				<th class="bzfld_head">环境目录</th>
				<td>{$app.contextDir}</td>
			</tr>
			<tr>
				<th class="bzfld_head">配置目录</th>
				<td>{$app.configDir}</td>
			</tr>
			<tr>
				<th class="bzfld_head">日志目录</th>
				<td>{$app.logsDir}</td>
			</tr>
			<tr>
				<th class="bzfld_head">临时目录</th>
				<td>{$app.tempDir}</td>
			</tr>
		</tbody>
	</table>
	<div class="bzsys_btns">
		<input type="submit" class="ajaxSubmit" value="保存" />
	</div>
</form>

<script type="text/javascript">
$(function(){
	$("#project").change(function(){
		var i=this.selectedIndex;
		var show=this.value.length>0;
		
		var sel=$("#corpnamesel");
		sel.get(0).selectedIndex=i;
		var input=$("#corpname");
		input.val(sel.val());
		if(show){
			sel.show();
			input.hide();
		}else{
			sel.hide();
			input.show();
		}
		
		sel=$("#corpcodesel");
		sel.get(0).selectedIndex=i;
		input=$("#corpcode");
		input.val(sel.val());
		if(show){
			sel.show();
			input.hide();
		}else{
			sel.hide();
			input.show();
		}
		
		sel=$("#softnamesel");
		sel.get(0).selectedIndex=i;
		input=$("#softname");
		input.val(sel.val());
		if(show){
			sel.show();
			input.hide();
		}else{
			sel.hide();
			input.show();
		}
		
		sel=$("#softcodesel");
		sel.get(0).selectedIndex=i;
		input=$("#softcode");
		input.val(sel.val());
		if(show){
			sel.show();
			input.hide();
		}else{
			sel.hide();
			input.show();
		}
		
		sel=$("#dbnamesel");
		sel.get(0).selectedIndex=i;
		input=$("#dbname");
		input.val(sel.val());
		if(show){
			sel.show();
			input.hide();
		}else{
			sel.hide();
			input.show();
		}
		
	});
});
</script>