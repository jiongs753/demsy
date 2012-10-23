{if $block.cellCount == null || $block.cellCount == 0} {if $block.horizontal}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
    <tr>
        {foreach from=$data.items item="item"}
        <td class="i">
            <table width="100%">
              <tr><td class="it" valign="middle">{import file="../a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td></tr>
              <tr><td class="ir">&nbsp;{import file="../a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td></tr>
              <tr>
                <td class="il">{if $item.obj != null}
                    <span class="price">{if $item.obj.nowPrice != null}￥{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</span>
                    {if $item.obj.nowPrice != $item.obj.oldPrice}
                    <span class="oldprice">{if $item.obj.oldPrice != null}{tostring bean=$item.obj prop="oldPrice" pattern="#,##0.00"}{/if}</span>
                    {/if}
                    {else}&nbsp;{/if}
                </td>
              </tr>
            </table>
        </td>{/foreach}
    </tr>
</table>
{else}
<ul>
    {foreach from=$data.items item="item"}
    <li class="i">
            <table width="100%">
              <tr><td class="it" valign="middle">{import file="../a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td></tr>
              <tr><td class="ir">&nbsp;{import file="../a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td></tr>
              <tr>
                <td class="il">{if $item.obj != null}
                    <span class="price">{if $item.obj.nowPrice != null}￥{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</span>
                    {if $item.obj.nowPrice != $item.obj.oldPrice}
                    <span class="oldprice">{if $item.obj.oldPrice != null}{tostring bean=$item.obj prop="oldPrice" pattern="#,##0.00"}{/if}</span>
                    {/if}
                    {else}&nbsp;{/if}
                </td>
              </tr>
            </table>
    </li>{/foreach}
</ul>
{/if} {else}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
    {foreach from=$data.items item="row"}
    <tr>
        {foreach from=$row.items item="item"}
        <td>
        <div class="i">
            <table width="100%">
              <tr><td class="it" valign="middle">{import file="../a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td></tr>
              <tr><td class="ir">&nbsp;{import file="../a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td></tr>
              <tr>
                <td class="il">{if $item.obj != null}
                    <span class="price">{if $item.obj.nowPrice != null}￥{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</span>
                    {if $item.obj.nowPrice != $item.obj.oldPrice}
                    <span class="oldprice">{if $item.obj.oldPrice != null}{tostring bean=$item.obj prop="oldPrice" pattern="#,##0.00"}{/if}</span>
                    {/if}
                    {else}&nbsp;{/if}
                </td>
              </tr>
            </table>
        </div>
        </td> {/foreach}
    </tr>
    {/foreach}
</table>
{/if}