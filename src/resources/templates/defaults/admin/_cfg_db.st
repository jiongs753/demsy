<form onsubmit="return false;" method="post" class="bzsys">
	<table class="bzgrp" border="0" valign="top" cellpadding="0" cellspacing="0">
		<thead class="bzgrp_head">
			<tr>
				<td colspan=2 class="ui-state-default">数据库配置</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th class="bzfld_head">数据库类型</th>
				<td>{tostring bean=$db prop="type" assign="dbtype"} <select name="db.type" onchange="
									if(this.value=='0'){
										dbdriver.disabled=false;
										dburl.disabled=false;
										dbname.disabled=true;
									}else{
										dbdriver.disabled=true;
										dburl.disabled=true;
										dbname.disabled=false;
									}
									">
						<option value="0" {if $dbtype==0}selected{/if}></option>
						<option value="1" {if $dbtype==1}selected{/if}>MSSQL Server</option>
				</select></td>
			</tr>
			<tr>
				<th class="bzfld_head">连接路径</th>
				<td><input type='text' id='dburl' {if $dbtype !=0} disabled{/if} name="db.url" value='{tostring bean=$db prop="url"}' />
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">驱动程序</th>
				<td><input type='text' id='dbdriver' {if $dbtype !=0} disabled{/if} name="db.driver" value='{tostring bean=$db prop="driver"}' />
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">数据库名</th>
				<td>
				<input type='text' id='dbname' {if $dbtype==0} disabled{/if} name="db.database" value='{tostring bean=$db prop="database"}' />
				<select id='dbnamesel' style="display: none;" disabled>				
					{foreach from=$databases item="dbitem" key="idx"}
						<option value="{$dbitem}" {if $dbitem==$db.database}selected{/if}>{$dbitem}</option>
					{/foreach}
				</select>
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">用户帐号</th>
				<td><input type='text' name="db.user" value='{tostring bean=$db prop="user"}' />
				</td>
			</tr>
			<tr>
				<th class="bzfld_head">用户密码</th>
				<td><input type='text' name="db.password" value='{tostring bean=$db prop="pwd"}' />
				</td>
			</tr>
		</tbody>
	</table>
	<div class="bzsys_btns">
		<input onclick='this.form.action="{$contextPath}/config/save"' type="submit" class="ajaxSubmit" value="保存" /> <input onclick='this.form.action="{$contextPath}/config/testconn"' type="submit" class="ajaxSubmit" value="测试" />
	</div>
</form>
