{*加载全部子菜单*}
<dl>
	<dt>{import file="Title.st"}</dt>
	<dd class="d">
		<table width="100%">
			{foreach from=$data.items item="item" key="idx"}
			<tr class="i">
				<td class="il"></td>
				<td class="it"><a href="javascript:toggleAccordion({$item.obj.id})">{$item.name}</a></td>
			</tr>
			<tr>
				<td></td>
				<td id="acc_{$item.obj.id}">
                    {foreach from=$children item="childDataModel"}{assign var="childModel" value=$childDataModel.model}{assign var="childBlock" value=$childModel.param}
                    <div id="block_{$item.obj.id}_{$childBlock.id}" parentID="{$item.obj.id}" blockID='{$childModel.id}' style="position: relative; width: 100%;" class="subAccordion {$childModel.cssClass}"></div>
                    {/foreach}
                </td>
			</tr>
			{/foreach}
		</table>
		{import file="_pager.st"}
	</dd>
	<dd class="b"></dd>
</dl>
<script type="text/javascript">
var toggleAccordion=function(d){
	var $this = $("#block_"+d+"_{$childBlock.id}");
}
$(function(){
	$(".subAccordion").each(function(){
        var $this=$(this);
    	$this.hide();
    	$this.load("/ui/block/" + $this.attr("blockID") + "/{$blockParser.module.id}:"+$this.attr("parentID"),function(){
    		$this.show();
    	});	
    })
});
</script>
