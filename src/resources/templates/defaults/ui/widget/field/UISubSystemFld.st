{prop bean=$data name=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
{else}
{prop bean=$data name=$ui.propName assign="subSystemObj"}
{if $mode == "M" || $mode == "E"}
<textarea id="{$ui.uiid}" name="{$ui.name}" style="display:none;">{tostring bean=$value}</textarea>
{/if}
<table>
    <tr>
        <td class="bzsubsys_container"><table id="bzsubsys_{$ui.uiid}" class="bzsubsys">
           {if $mode == "M" || $mode == "E"}
           <tr>
                <td class="bzsubsys_btns" colspan=10>
                     <a href="#" class="op_btn_up" title="上移" alt="上移">↑</a>
                     <a href="#" class="op_btn_down" title="下移" alt="下移">↓</a>
                     <a href="#" class="op_btn_del" title="删除" alt="删除">×</a>
                     <a href="#" class="op_btn_add" title="添加" alt="添加">+</a>
                </td>
            </tr>
            {/if}
            <tr>
                {foreach from=$ui.children item="fld2"}<th class="bzsubfld_head">{$fld2.label}</th>{/foreach}
                <th class="bzsubfld_head_check">
                    <input type="checkbox" class="op_checkall" />
                </th>
            </tr>
            {foreach from=$subSystemObj.list item="subData" key="row"}
            <tr class="bzsubsys_item">
                {foreach from=$ui.children item="fld2"}<td class="bzsubfld_data" valign="top">{render ui=$fld2 uiRender=$uiRender data=$subData idPost=$row}</td>{/foreach}
                <td class="bzsubfld_data_check" valign="top">
                    <input type="checkbox" class="check" />
                    <input name="id" type="hidden" value="{tostring bean=$subData.id}"/>
                    {if $ui.fake}{else}<input name="statusForJsonData" type="hidden" class="statusForJsonData"/>{/if}
                </td>
            </tr>{/foreach}
        </table></td>
    </tr>
</table>
{if $mode == "M" || $mode == "E"}
<table id="bzsubsys_tpl_{$ui.uiid}" style="display:none;">
    <tr class="bzsubsys_item">
        {foreach from=$ui.children item="fld2"}<td class="bzsubfld_data" valign="top">{render ui=$fld2 uiRender=$uiRender data=""}</td>{/foreach}
        <td class="bzsubfld_data_check" valign="top">
            <input type="checkbox" class="check" />
            <input name="id" type="hidden" />
            {if $ui.fake}{else}<input name="statusForJsonData" type="hidden" class="statusForJsonData"/>{/if}
        </td>
    </tr>
</table>
{/if}
<script type="text/javascript">
$(function(){
	new SubModule($("#bzsubsys_{$ui.uiid}"),{
		field: $("#{$ui.uiid}"),
    	itemTemplate: $("#bzsubsys_tpl_{$ui.uiid}")
	});
});
</script>
{/if}
