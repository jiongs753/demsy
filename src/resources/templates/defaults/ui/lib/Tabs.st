<dl class="tabs">
	{assign var="parentBlock" value=$block}
	{foreach from=$children item="dataModel"}{assign var="model" value=$dataModel.model}{assign var="block" value=$model.param}
	<dt><div>{prop bean=$dataModel.context name="data.name"}</div></dt>
	<dd class="d" style="width:{$parentBlock.position.width}px; height:{$parentBlock.position.height}px;overflow: hidden;">
        <div id="block{$block.id}" dataID="{$block.id}" class="{$model.cssClass}{if $block.ajaxLoad} ajaxLoad{/if}">
        {render ui=$dataModel uiRender=$uiRender}
        </div>
	</dd>
	{/foreach}
</dl>