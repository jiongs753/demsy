{tostring bean=$data prop=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E"}
<input id="fld{$ui.id}" type="text" name="{$ui.name}" value="{$value}" class="datepicker input" />
{elseif $mode == "R" || $mode == "D"}
<input id="fld{$ui.id}" type="text" name="{$ui.name}" class="input" value="{$value}"{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if} />
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<span>{$value}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
{elseif $mode == "S"}
<span>{$value}</span>
{elseif $mode == "P" && $value != null && $value != ""}
<span>{$value}</span>
{/if}
