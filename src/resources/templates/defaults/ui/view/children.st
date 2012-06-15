{foreach from=$children item="dataModel"}{assign var="model" value=$dataModel.model}{assign var="block" value=$model.param}
<div id="block{$model.id}" dataID="{$model.id}" refPage="{if $block.viewPage !=null}{$block.viewPage.id}{/if}" styleID="{if $block.style != null}{$block.style.id}{/if}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if}" class="block {$model.cssClass}{if $block.ajaxLoad} ajaxLoad{/if}" >
{if $block.ajaxLoad}{else}{render ui=$dataModel uiRender=$uiRender}{/if}
</div>{/foreach}