<div id="tabsUiLib">
<ul>
	{foreach from=$modelLibNodes.children item="item"}
	<li><a href="#uilib_{$item.id}">{$item.name}</a></li> {/foreach}
</ul>
{foreach from=$modelLibNodes.children item="item"}
<div id="uilib_{$item.id}">{import file="_UiLib.st" children=$item.children}</div>
{/foreach}
</div>
<script type="text/javascript">
$(function(){
demsyUIManager.setupUiLib();
});
</script>