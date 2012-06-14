<table><tr>{assign var="count" value=0}{foreach from=$children item="ele" key="idx"}
    {math assign="count" equation="v + 1" v=$count}
    <td><div class="viewComponent" dataID="{tostring bean=$ele prop="dataID"}" type="{$ele.type}" title="{$ele.desc}" defaultWidth='{tostring bean=$ele prop="defaultWidth"}' defaultHeight='{tostring bean=$ele prop="defaultHeight"}' >
    <textarea class="expr" style="display:none;">{tostring bean=$ele prop="expr"}</textarea>
    <input  class="moduleGuid" value="{tostring bean=$ele prop="moduleGuid"}" type="hidden" />
	{$ele.name}
    </div></td>
    {if $count == 5}</tr><tr>{assign var="count" value=0}{/if}
{/foreach}</tr></table>