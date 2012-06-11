<UL>{foreach from=$ctx.items item="item" key="idx"}
  <LI style="position: relative;height: 50px;">{foreach from=$children item="blockView"}{assign var="model" value=$blockView.model}{assign var="block" value=$ctx.block}
    {if $idx == 0}<div id="block{$model.id}" dataID="{$model.id}" refPage="{if $block.viewPage !=null}{$block.viewPage.id}{/if}" styleID="{if $block.style != null}{$block.style.id}{/if}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if};" class="block droppable {$model.cssClass}">
    {else}<div id="block{$model.id}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if};" class="{$model.cssClass}">{/if}
{render ui=$blockView uiRender=$uiRender item=$item}
    </div>{/foreach}
  </LI>{/foreach}
</UL>	