{*每次只加载一个子菜单*}
<dl>
	<dt>{import file="Title.st"}</dt>
	<dd class="d">
		<table width="100%">
			{foreach from=$data.items item="item" key="idx"}
			<tr class="i">
				<td class="il"></td>
				<td class="it"><a href="javascript:loadAccordion({$item.obj.id})">{$item.name}</a></td>
			</tr>
			<tr>
				<td></td>
				<td id="acc_{$item.obj.id}"></td>
			</tr>
			{/foreach}
		</table>
		{import file="_pager.st"}
	</dd>
	<dd class="b"></dd>
</dl>
{foreach from=$children item="childDataModel"}{assign var="childModel" value=$childDataModel.model}{assign var="childBlock" value=$childModel.param}
<div id="block{$childBlock.id}" blockID="{$childModel.id}" style="position: relative; width: 100%;" class="block {$childModel.cssClass}"></div>
{/foreach}
<script type="text/javascript">
var loadAccordion=function(d){
	var $this = $("#block{$childBlock.id}");
	$this.block();
	$this.load("/ui/block/" + $this.attr("blockID") + "/{$blockParser.module.id}:"+d,function(){
		$("#acc_"+d).append($this);
	});	
}
{if $pathData != null}
loadAccordion({$pathData.catalog.id});
{/if}
</script>
