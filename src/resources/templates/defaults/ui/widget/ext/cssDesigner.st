{prop bean=$data name=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "H"}
<textarea id="{$ui.uiid}" name="{$ui.name}" style="display: none;">{tostring bean=$value}</textarea>
{else}
{prop bean=$data name=$ui.propName assign="subSystemObj"}
{if $mode == "M" || $mode == "E"}
<textarea id="{$ui.uiid}" name="{$ui.name}" style="display: none;">{tostring bean=$value}</textarea>
{/if}
<table>
    <tr>
        <td class="bzsubsys_container"><table id="bzsubsys_{$ui.uiid}" class="bzsubsys">
            <tr>
                <th class="bzsubfld_head" style="width:88px;">CSS名称</th>
                <th class="bzsubfld_head" style="width:258px;">CSS内容</th>
                <th class="bzsubfld_head_check">
                    {if $mode == "M" || $mode == "E"}
                     <a href="#" class="op_btn_del" title="删除" alt="删除" style="font-size: 17px;">×</a>
                     <a href="#" class="op_btn_add" title="添加" alt="添加">+</a>
                    {/if}
                </th>
            </tr>
            {foreach from=$subSystemObj.list item="subData" key="row"}
            <tr class="bzsubsys_item">
                <td class="bzsubfld_data" valign="top"><input type="text" style="width:80px;height: 18px;" class="input" name="code" value="{tostring bean=$subData.value}" /></td>
                <td class="bzsubfld_data" valign="top"><textarea name="desc" style="width:250px;height: 20px;" class="textarea">{tostring bean=$subData.value}</textarea></td>
                <td class="bzsubfld_data_check" valign="top">
                    <input type="checkbox" class="check" />
                    <input name="id" type="hidden" value="{tostring bean=$subData.id}"/>
                    {if $ui.fake}{else}<input name="statusForJsonData" type="hidden" class="statusForJsonData"/>{/if}
                </td>
            </tr>{/foreach}
        </table>
        {import file="cssDesigner/cssDesignerPad.st"}
        </td>
    </tr>
</table>
{if $mode == "M" || $mode == "E"}
<table id="bzsubsys_tpl_{$ui.uiid}" style="display:none;">
    <tr class="bzsubsys_item">
        <td class="bzsubfld_data" valign="top"><input type="text" style="width:80px;height: 18px;" class="input" name="code" value="" /></td>
        <td class="bzsubfld_data" valign="top"><textarea name="desc" style="width:250px;height: 20px;" class="textarea"></textarea></td>
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
	var $pad=$(".CssDesignerPad").tabs({
		currentTab: 0,
		switchingMode: 'c'
	});
	var designer = new CssDesigner($pad);
	var $bzsys = $("#bzsubsys_{$ui.uiid}");
	var selectItem = function(){
		var $item = $(this).parent().parent();
		var checked = true;
		if($(this).hasClass("check")){
			checked = this.checked;
		}
		$(".check", $bzsys).attr("checked", false);
		$(".check", $item).attr("checked", checked);
		
		//reset
		if(checked){
			var output = $(".textarea", $item);
			designer.output = output.get(0);
			designer.$pad.show();
		}else{
    		designer.output = null;
    		designer.$pad.hide();
		}
		designer.reset();
	}
	
    new SubModule($("#bzsubsys_{$ui.uiid}"),{
        field: $("#{$ui.uiid}"),
        itemTemplate: $("#bzsubsys_tpl_{$ui.uiid}"),
        onAddItem: function(self, $item, json){
            $(".check", $item).click(selectItem);
            $(".input", $item).click(selectItem);
            $(".textarea", $item).click(selectItem);
        },
        onDelItem: function(self, $item){
    		designer.output = null;
    		designer.reset();
    		designer.$pad.hide();
        }
    });
    
    $(".check", $bzsys).click(selectItem);
    $(".input", $bzsys).click(selectItem);
    $(".textarea", $bzsys).click(selectItem);
});
</script>
{/if}