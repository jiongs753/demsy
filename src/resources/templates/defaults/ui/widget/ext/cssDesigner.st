{prop bean=$data name=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

<style>
.bzsubsys_item_active td{
    background-color: #c5dbec;
}
</style>
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
                <th class="bzsubfld_head" style="width:140px;">CSS名称</th>
                <th class="bzsubfld_head" style="width:290px;">样式内容</th>
                <th class="bzsubfld_head_check bzsubsys_btns">
                    {if $mode == "M" || $mode == "E"}
                     <a href="#" class="op_btn_add" title="添加" alt="添加">+</a>
                    {/if}
                </th>
            </tr>
            {foreach from=$subSystemObj.list item="subData" key="row"}
            <tr class="bzsubsys_item">
                <td class="bzsubfld_data" valign="top" style="padding: 4px;"><input style="width:130px;height: 18px;" class="input" name="code" type="text" value="{tostring bean=$subData.code}" /></td>
                <td class="bzsubfld_data" valign="top" style="padding: 3px;"><textarea name="desc" readonly="true" style="width:280px;height: 20px;" class="textarea">{tostring bean=$subData.desc}</textarea></td>
                <td class="bzsubfld_data_check bzsubsys_btns" valign="top">
                     <a href="#" class="op_btn_del_item" title="删除" alt="删除" style="font-size: 17px;">×</a>
                    <input name="id" type="hidden" value="{tostring bean=$subData.id}"/>
                </td>
            </tr>{/foreach}
        </table>
        </td>
    </tr>
</table>
{if $mode == "M" || $mode == "E"}
<table id="bzsubsys_tpl_{$ui.uiid}" style="display:none;">
    <tr class="bzsubsys_item">
        <td class="bzsubfld_data" valign="top" style="padding: 4px;">
        	<div style="position:relative;width:132px;">
        	<div style="position:absolute;top:1px;left:1px;z-index:1;">
        	<select style="width:132px;height: 22px;" class="select"><option value="" selected>默认</option></select>
        	</div>
        	<div style="position:absolute;top:1px;left:1px;z-index:2;">
        	<input style="width:109px;height: 18px;border-right:0;" class="input" name="code" type="text" />
        	</div>
        	</div>
        </td>
        <td class="bzsubfld_data" valign="top" style="padding: 3px;"><textarea name="desc" readonly="true" style="width:280px;height: 20px;" class="textarea"></textarea></td>
        <td class="bzsubfld_data_check bzsubsys_btns" valign="top">
            <a href="#" class="op_btn_del_item" title="删除" alt="删除" style="font-size: 17px;">×</a>
            <input name="id" type="hidden" />
        </td>
    </tr>
</table>
{import file="cssDesigner/cssDesignerPad.st"}
{/if}

<script type="text/javascript">
$(function(){
	var $pad=$(".CssDesignerPad").tabs({
		currentTab: 0,
		switchingMode: 'c'
	});
	var designer = new CssDesigner($pad);
	var $bzsys = $("#bzsubsys_{$ui.uiid}");
	var getStyles=function(){
		var styles=new Array();
		$(".bzsubsys_item",$bzsys).each(function(){
			var $item=$(this);
			var cssname=$(".input",$item).val();
			var styletext=$(".textarea",$item).val();
			if(styles[cssname]){
				styletext=styles[cssname]+styletext;
			}
			styles[cssname]=styletext;
		});
		return styles;
	}
	var selectItem = function(){
		var $item = $(this).parent().parent();
		if(!$item.hasClass("bzsubsys_item")){
			$item = $item.parent().parent();
		}
		
		$(".bzsubsys_item", $bzsys).removeClass("bzsubsys_item_active");	
		$item.addClass("bzsubsys_item_active");

		designer.$pad.show();
		designer.outputEle = $(".textarea", $item).get(0);
		designer.targetSubSelector = $(".input", $item).val();

		designer.refreshCacheStyles(getStyles());
	}
	
    new SubModule($("#bzsubsys_{$ui.uiid}"),{
        field: $("#{$ui.uiid}"),
        itemTemplate: $("#bzsubsys_tpl_{$ui.uiid}"),
        onAddItem: function(self, $item, json){
            $(".input", $item).click(selectItem);
            $(".textarea", $item).click(selectItem);
            var $sel=$(".select", $item).change(function(){
            	var ths=$(this);
            	$(".input", $item).val(ths.val()).click();
            });
            
            var sel = $sel.get(0);
            var options=sel.options;
            
            //生成CSS名称下拉项
            var items = designer.makeCssOptions();
            for(item in items){
            	var option = new Option();
            	option.value = item;
            	option.text = items[item];
            	options.add(option);
            }
        },
        onDelItem: function(self, $item){	
    		if($item.hasClass("bzsubsys_item_active")){
    			designer.outputEle = null;
    			designer.targetSubSelector = null;
    			designer.$pad.hide();
    		}
    		
    		designer.refreshCacheStyles(getStyles());
        }
    });
    $(".input", $bzsys).click(selectItem);
    $(".textarea", $bzsys).click(selectItem);
});
</script>
{/if}