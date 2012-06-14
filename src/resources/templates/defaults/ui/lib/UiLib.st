<div id="viewComponents" class="accordion">{foreach from=$viewComponentNodes.children item="item"}
    <div class="menu"><span href="#viewcomponent_{$item.id}">{$item.name}</span></div>
    <div class="content" id="viewcomponent_{$item.id}">{import file="_UiLib.st" children=$item.children}</div>
{/foreach}</div>
<script type="text/javascript">
$(function(){
	demsyUIManager.setupViewComponent();
});
</script>