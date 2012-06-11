{prop bean=$data name=$ui.propName assign="value"}
{key bean=$value assign="fieldValue"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<select id="fld{$ui.id}" name="{$ui.name}"{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if} class="select{if $cascade != null} cascade{/if}">
<option value=""></option>
{if $ui.optionNode != null}{import file="../../../common/options.st" nodes=$ui.optionNode.children fieldValue=$fieldValue}{/if}	
</select>
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$fieldValue}" type="hidden" />
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$fieldValue}" type="hidden" />
{elseif $mode == "S"}
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{elseif $mode == "P" && $value.id != null && $value.id > 0}
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{/if}
