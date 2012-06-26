{if $block.horizontal}<table>
    <tr>{foreach from=$children item="dataModel"} {assign var="model" value=$dataModel.model} {assign var="block" value=$model.param}
        <td valign="top"><div id="block{$block.id}" dataID="{$block.id}" refPage="{if $block.viewPage !=null}{$block.viewPage.id}{/if}" styleID="{if $block.style != null}{$block.style.id}{/if}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if}" class="block {$model.cssClass}{if $block.ajaxLoad} ajaxLoad{/if}">
        {if $block.ajaxLoad}{else}{render ui=$dataModel uiRender=$uiRender}{/if}
        </div></td> {/foreach}
    </tr>
</table>
{else}<table>{foreach from=$children item="dataModel"} {assign var="model" value=$dataModel.model} {assign var="block" value=$model.param}
    <tr><td valign="top"><div id="block{$block.id}" dataID="{$block.id}" refPage="{if $block.viewPage !=null}{$block.viewPage.id}{/if}" styleID="{if $block.style != null}{$block.style.id}{/if}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if}" class="block {$model.cssClass}{if $block.ajaxLoad} ajaxLoad{/if}">
        {if $block.ajaxLoad}{else}{render ui=$dataModel uiRender=$uiRender}{/if}
        </div></td>
    </tr>{/foreach}
</table>{/if}