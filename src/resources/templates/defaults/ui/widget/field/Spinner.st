{tostring bean=$data prop=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<input id="fld{$ui.id}" type="text" title="{$ui.label}" name="{$ui.name}" value="{$value}" class="input spinner" {if $style != null}style="{$style}"{/if} {if $mode=="R"}readonly{/if}{if $mode=="D"} disabled{/if} />
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<span>{$value}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
{elseif $mode == "S"}
<span>{$value}</span>
{elseif $mode == "P" && $value != null && $value > 0}
<span>{$value}</span>
{/if}

