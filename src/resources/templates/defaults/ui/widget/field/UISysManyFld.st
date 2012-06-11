{prop bean=$data name=$ui.propName assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<select id="fld{$ui.id}" name="{$ui.name}" multiple="true"{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if}>
<option value="">---请选择---</option>
{if $ui.optionNode != null}{import file="../../../common/options.st" nodes=$ui.optionNode.children value=$value}{/if}	
</select>
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value.id}" type="hidden" />
<span>{tostring bean=$value}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value.id}" type="hidden" />
{elseif $mode == "S"}
<span>{tostring bean=$value}</span>
{elseif $mode == "P" && $value.id != null && $value.id > 0}
<span>{tostring bean=$value}</span>
{/if}
