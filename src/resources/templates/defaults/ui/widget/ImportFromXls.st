<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />
<form id="{$ui.uiid}" class="bzsys bzsys_{$mode}" method="POST" action="{$submitUrl}" enctype="multipart/form-data">
    <div class="bzgrp">
    {if $info != null}
    <div style="color: red; margin: 20px; padding: 10px;">{$info}</div>
    {/if}
	<fieldset style="margin: 20px; padding: 10px;">
		<legend> Excel数据文件说明</legend>
		<OL>
			<LI>数据表表头不能重复；</LI>
			<LI>数据表表头在系统字段中必须存在并在新增时允许编辑；</LI>
			<LI>数据表至少需要两列；</LI>
			<LI>数据表中的第一列必须是主键字段；（即第一列的值能够与功能模块中数据一一对应）</LI>
			<LI>数据表的第一列不允许为空；</LI>
			<LI>数据表的第一列不允许重复；</LI>
			<LI>数据表第一列对应的数据在功能模块中已经存在，则将用Excel行数据修改对应的功能模块数据；</LI>
			<LI>数据表第一列对应的数据在功能模块中不存在，则将在功能模块中添加一条新数据；</LI>
			<LI>数据表中的某一列对应的是其他功能模块数据，则其值必须在相应的功能模块中存在；（如：
员工管理系统中的“机构名称”来自“组织机构管理”模块，因此数据表格中“机构名称”这一列的值必须在“组织机构管理”模块中存在）
			</LI>
		</OL>
        <br />
        <input type="file" name="upload"  />
	</fieldset>
    </div>
    <div class="bzsys_btns">
        <input type="submit" value="提交" class="submitButton" /> <input type="submit" value="返回" class="returnButton" style="display: none;" />
    </div>
</form>
