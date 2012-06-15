<UL>{foreach from=$ctx.items item="item" key="idx"}
  <LI style="position: relative;"><div class="blank" style="height:25px;"></div>{foreach from=$children item="blockView"}{assign var="model" value=$blockView.model}{assign var="block" value=$ctx.block}
    <div id="block{$model.id}" dataID="{$model.id}"{if $block.viewPage !=null} refPage="{$block.viewPage.id}"{/if}{if $block.style != null} styleID="{$block.style.id}"{/if}{if $model.cssStyle != null} style="{$model.cssStyle}"{/if} class="block {$model.cssClass}">
		{render ui=$blockView uiRender=$uiRender item=$item}
    </div>{/foreach}
  </LI>{/foreach}
</UL>	