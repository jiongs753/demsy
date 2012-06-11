{tostring bean=$data prop=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<input id="fld{$ui.id}" title="{$ui.label}" name="{$ui.name}" class="input color" {if $style != null}style="{$style}"{/if} value="{$value}" type="{$ui.inputType}"{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if} />
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<span>{tostring bean=$value}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
{elseif $mode == "S"}
<span>{tostring bean=$value}</span>
{elseif $mode == "P" && $value != null && $value != ""}
<span>{tostring bean=$value}</span>
{/if}